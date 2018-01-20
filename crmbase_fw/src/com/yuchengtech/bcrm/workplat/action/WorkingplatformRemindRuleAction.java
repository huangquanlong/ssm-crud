package com.yuchengtech.bcrm.workplat.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.workplat.model.WorkingplatformRemindRule;
import com.yuchengtech.bcrm.workplat.service.WorkingplatformRemindRuleService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;
/**
 * 公共参数管理
 */

@SuppressWarnings("serial")
@Action("/workplatremindrule")
public class WorkingplatformRemindRuleAction  extends CommonAction{
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds; //声明数据源
	private HttpServletRequest request;
	
    @Autowired
    private WorkingplatformRemindRuleService workingplatformRemindRuleService ;
    AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    @Autowired
	public void init(){
        	model = new WorkingplatformRemindRule();  
        	setCommonService(workingplatformRemindRuleService);
		//新增修改删除记录是否记录日志,默认为false，不记录日志
		//needLog=true;
	}
    
    //新增或修改方法
    public String saveData(){
    	ActionContext ctx = ActionContext.getContext();
    	request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
   	
    	String iS_CUST_MGR = "0";
    	for (int i = 0; i < auth.getRolesInfo().size(); i ++ ) {
			Map tempMap = (HashMap)auth.getRolesInfo().get(i);
			if ("zhhcm".equals(tempMap.get("ID"))) {//包含支行客户经理则认为是客户经理
				iS_CUST_MGR = "1";
			} 
		}
    	Long rule_id = 0l ;
    	
        try{
        	rule_id = ((WorkingplatformRemindRule) model).getRULE_ID();
        }catch(Exception e){}
        ((WorkingplatformRemindRule) model).setCREATOR(auth.getUserId());//创建人
        ((WorkingplatformRemindRule) model).setCREATOR_NAME(auth.getUsername());
        ((WorkingplatformRemindRule) model).setCREATE_ORG(auth.getUnitId());
        ((WorkingplatformRemindRule) model).setCREATE_ORG_NAME(auth.getUnitName());
        ((WorkingplatformRemindRule) model).setIS_CUST_MGR(iS_CUST_MGR);
//       ((WorkingplatformRemindRule) model).setCREATE_TIME(new Date());//创建时间
        if(rule_id==0l){
          workingplatformRemindRuleService.saveData(model);
        }
        else
          workingplatformRemindRuleService.merge(model);
        
    		addActionMessage("saveData successfully");
    	    return "success";	
    	
    }

    
}