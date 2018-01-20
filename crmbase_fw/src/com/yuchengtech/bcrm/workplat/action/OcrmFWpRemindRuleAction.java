package com.yuchengtech.bcrm.workplat.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.workplat.model.OcrmFWpRemindRule;
import com.yuchengtech.bcrm.workplat.service.OcrmFWpRemindRuleService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;

/**
 * 
 * 信息提醒规则处理类
 * @author luyy
 * @since 2014-2-19
 */

@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/remindrule", results = { @Result(name = "success", type = "json")})
public class OcrmFWpRemindRuleAction extends CommonAction {
	
	//数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
   
	 @Autowired
	    private  OcrmFWpRemindRuleService  ocrmFWpRemindRuleService;
	 
	 AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
	 @Autowired
		public void init(){
		  	model = new OcrmFWpRemindRule(); 
			setCommonService(ocrmFWpRemindRuleService);
			//新增修改删除记录是否记录日志,默认为false，不记录日志
			needLog=true;
		}

	/**
	 *信息查询SQL
	 */
	public void prepare() {
		 ActionContext ctx = ActionContext.getContext();
    	 request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
    	 //如果是总行，直接查询总行的规则设置，如果是分行，首先查询分行是否有记录，否则查询总行配置
		StringBuilder sb = new StringBuilder(" ");
//		String frid = request.getParameter("frid").toString();
		String ruleCode = request.getParameter("ruleCode").toString();
//		if("".equals(frid)||frid==null){
//			sb.append(" select * from OCRM_F_WP_REMIND_RULE where RULE_CODE='"+ruleCode+"' and CREATE_ORG='100000'");
//		}else{
			//判断是否有记录
//			List list = ocrmFWpRemindRuleService.getBaseDAO().findByNativeSQLWithIndexParam("select * from OCRM_F_WP_REMIND_RULE where RULE_CODE='"+ruleCode+"' and CREATE_ORG='"+frid+"'");
//			if(list.size()>0){
//				sb.append(" select * from OCRM_F_WP_REMIND_RULE where RULE_CODE='"+ruleCode+"' and CREATE_ORG='"+frid+"'");
				sb.append(" select * from OCRM_F_WP_REMIND_RULE where RULE_CODE='"+ruleCode+"' and CREATE_ORG='"+auth.getUnitId()+"'");
//			}else{
//				sb.append(" select * from OCRM_F_WP_REMIND_RULE where RULE_CODE='"+ruleCode+"' and CREATE_ORG='100000'");
//			}
				
//		}
		SQL=sb.toString();
		datasource = ds;
	}
	
	/**
	 * 数据保存
	 */
	public DefaultHttpHeaders saveData(){
		//首先根据是否是总行区分处理
		String frid = (String)auth.getUnitInfo().get("FR_ID");
//		if("".equals(frid)||frid==null){//总行      直接保存数据即可
			if(((OcrmFWpRemindRule)model).getRuleId()==null){//新增
				((OcrmFWpRemindRule)model).setCreateUser(auth.getUserId());
				((OcrmFWpRemindRule)model).setCreateDate(new Date());
				((OcrmFWpRemindRule)model).setCreateOrg(auth.getUnitId());
				((OcrmFWpRemindRule)model).setUpdateUser(auth.getUserId());
				((OcrmFWpRemindRule)model).setUpdateOrg(auth.getUnitId());
				((OcrmFWpRemindRule)model).setUpdateDate(new Date());
				
				ocrmFWpRemindRuleService.save(model);
			}else{//修改
				Long ruleId = ((OcrmFWpRemindRule)model).getRuleId();
				OcrmFWpRemindRule oldInfo = (OcrmFWpRemindRule)ocrmFWpRemindRuleService.find(ruleId);
				((OcrmFWpRemindRule)model).setCreateUser( oldInfo.getCreateUser());
				((OcrmFWpRemindRule)model).setCreateDate( oldInfo.getCreateDate());
				((OcrmFWpRemindRule)model).setCreateOrg(oldInfo.getCreateOrg() );
				
				((OcrmFWpRemindRule)model).setUpdateUser(auth.getUserId());
				((OcrmFWpRemindRule)model).setUpdateOrg(auth.getUnitId());
				((OcrmFWpRemindRule)model).setUpdateDate(new Date());
				
				ocrmFWpRemindRuleService.save(model);
			}
//		}else{//法人机构，判断原纪录是总行的还是法人机构的
//			Long ruleId = ((OcrmFWpRemindRule)model).getRuleId();
//			OcrmFWpRemindRule oldInfo = (OcrmFWpRemindRule)ocrmFWpRemindRuleService.find(ruleId);
//			if("100000".equals(oldInfo.getCreateOrg())){//是总行的数据   需要保存成一条法人机构的新纪录
//				((OcrmFWpRemindRule)model).setRuleId(null);
//				((OcrmFWpRemindRule)model).setCreateUser(auth.getUserId());
//				((OcrmFWpRemindRule)model).setCreateDate(new Date());
//				((OcrmFWpRemindRule)model).setCreateOrg(frid);
//				((OcrmFWpRemindRule)model).setUpdateUser(auth.getUserId());
//				((OcrmFWpRemindRule)model).setUpdateOrg(frid);
//				((OcrmFWpRemindRule)model).setUpdateDate(new Date());
//				
//				ocrmFWpRemindRuleService.save(model);
//			}else{//是法人机构的数据，直接修改
//				((OcrmFWpRemindRule)model).setCreateUser( oldInfo.getCreateUser());
//				((OcrmFWpRemindRule)model).setCreateDate( oldInfo.getCreateDate());
//				((OcrmFWpRemindRule)model).setCreateOrg(oldInfo.getCreateOrg() );
//				
//				((OcrmFWpRemindRule)model).setUpdateUser(auth.getUserId());
//				((OcrmFWpRemindRule)model).setUpdateOrg(frid);
//				((OcrmFWpRemindRule)model).setUpdateDate(new Date());
//				
//				ocrmFWpRemindRuleService.save(model);
//			}
//		}
		
		return new DefaultHttpHeaders("success");
	}
}


