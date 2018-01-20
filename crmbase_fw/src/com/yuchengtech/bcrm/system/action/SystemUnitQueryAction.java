package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthOrg;
import com.yuchengtech.bcrm.system.service.SystemOrganizationService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.common.DataType;
import com.yuchengtech.bob.common.JPAAnnotationMetadataUtil;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;

/**
 * 机构管理-查询、维护、新增
 * @author lixb
 * @since 2012-09-20 
 */
@SuppressWarnings("serial")
@Action("/systemUnit-query")
public class SystemUnitQueryAction  extends CommonAction {
	
    @Autowired
    private SystemOrganizationService systemOrganizationService ;
    
    @Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
    
    @Autowired
	public void init(){
	  	model = new AdminAuthOrg(); 
		setCommonService(systemOrganizationService);
		//新增修改删除记录是否记录日志,默认为false，不记录日志
		//needLog=true;
	}
    
    /**
     * 删除机构树节点
     */
    public String batchDestroy(){
	   	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		String jql="delete from AdminAuthOrg c where c.id in ("+idStr+")";
		Map<String,Object> values=new HashMap<String,Object>();
		systemOrganizationService.batchUpdateByName(jql, values);
		addActionMessage("batch removed successfully");
        return "success";
    }
    
   /**
    * 查询机构树节点信息
    */
    public void prepare() {
		String id = (String)this.getJson().get("id");//获取查看、修改信息的ID
		String orgIds =  (String)this.getJson().get("TREE_ORG");//获取机构树ID
		StringBuilder sql;
		if(null != id && !id.equals("")){
			 sql = new StringBuilder("SELECT DISTINCT C.* FROM ADMIN_AUTH_ORG C "
		    		+ " WHERE C.APP_ID='"+SystemConstance.LOGIC_SYSTEM_APP_ID + "' AND C.id='"+id+"'");
		}else{
			 sql = new StringBuilder("SELECT DISTINCT C.*,A.ORG_ID AS AC_ORG_ID,B.ORG_NAME AS PARENT_ORG_NAME FROM ADMIN_AUTH_ORG C "
					+ " LEFT JOIN ADMIN_AUTH_ACCOUNT A ON C.ORG_ID=A.ORG_ID "
					+ " INNER JOIN SYS_UNITS S ON C.ORG_ID=S.UNITID  "
		    		+ " LEFT JOIN ADMIN_AUTH_ORG B ON B.ORG_ID=C.UP_ORG_ID "
		    		+ " WHERE C.APP_ID='"+SystemConstance.LOGIC_SYSTEM_APP_ID + "' ");
			if(null != orgIds && !orgIds.equals("")){
				sql.append(" AND ( S.SUPERUNITID  = '" +orgIds+ "' OR S.UNITID='"+orgIds+ "')");
			}
//			if(null != orgName && !orgName.equals("")){
//				sql.append(" AND C.ORG_NAME LIKE" + " '%" + orgName + "%'");
//			}
		}
		
    	setBranchFileldName("c.ORG_ID");
        SQL = sql.toString();
        this.setPrimaryKey("C.ORG_LEVEL,C.ORG_ID");
        datasource=ds;
       	configCondition("C.ORG_ID","=","ORG_ID",DataType.String);
       	configCondition("C.ORG_NAME","like","ORG_NAME",DataType.String);
       	configCondition("B.ORG_NAME","like","PARENT_ORG_NAME",DataType.String);
       	configCondition("C.ORG_LEVEL","=","ORG_LEVEL",DataType.String);
       	configCondition("C.UP_ORG_ID","=","UP_ORG_ID",DataType.String);
       	configCondition("C.LAUNCH_DATE","=","LAUNCH_DATE",DataType.Date);
       	configCondition("C.POST_CODE","=","POST_CODE",DataType.String);
       	configCondition("C.ORG_ADDR","like","ORG_ADDR",DataType.String);

    }  
    
    /**
     * 保存机构信息，判断机构名称和机构ID是否重复
     */
    public DefaultHttpHeaders create() {
    	String message = "";//抛出异常信息
    	try{
    		AdminAuthOrg adminAuthOrg = (AdminAuthOrg) model;
    		String orgId = adminAuthOrg.getOrgId();//获取新增机构编号
    		String name = adminAuthOrg.getOrgName();//获取新增机构名称
    		//按照机构号查询的对象
    		AdminAuthOrg aaoId = (AdminAuthOrg)systemOrganizationService.findUniqueByOrgId("orgId",orgId);
    		//按照机构名称查询的对象
    		AdminAuthOrg aaoName = (AdminAuthOrg)systemOrganizationService.findUniqueByOrgId("orgName",name);
    		//判断获取机构表主键，如果主键值为null,新增的操作
    		if ((null != orgId && !orgId.equals(""))&&(null != name && !name.equals("")) && null == adminAuthOrg.getId()) {
    			if (null != aaoId && null == aaoName) {
    				message = "机构编号重复，请重新填写！"; 
    				throw new BizException(0,0,"1002",message);
    			} else if (null == aaoId && null != aaoName) {
    				message = "机构名称重复，请重新填写！"; 
    				throw new BizException(0,0,"1002",message);
    			} else if (null != aaoId && null != aaoName) {
    				message = "机构名称与机构编号重复，请重新填写！"; 
    				throw new BizException(0,0,"1002",message);
    			}else {
    				systemOrganizationService.save(model);
    				AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	    		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
    	    		auth.setPid(metadataUtil.getId(model).toString());
    			}
    		}//判断获取机构表主键，如果主键值不为null,修改的操作 
    		else if ((null != orgId && !orgId.equals(""))&&(null != name && !name.equals("")) && null != adminAuthOrg.getId()) {
    			//此处判断机构名称是否重复，得排除是否是当前修改机构的机构名称
    			if (null != aaoName  && !aaoName.getOrgId().equals(orgId)) {
    				message = "机构名称重复，请重新填写！"; 
    				throw new BizException(0,0,"1002",message);
    			} else {
    				systemOrganizationService.save(model);
    				AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	    		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
    	    		auth.setPid(metadataUtil.getId(model).toString());
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new BizException(0,0,"1002",message);
    	}
    	return new DefaultHttpHeaders("success");
    }
}