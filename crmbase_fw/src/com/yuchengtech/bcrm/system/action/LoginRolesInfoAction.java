package com.yuchengtech.bcrm.system.action;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.common.service.SecGrantService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 查询角色信息
 * @author changzh
 * @since 2012-11-30
 */

@SuppressWarnings("serial")
@Action("/loginRolesInfo")
public class LoginRolesInfoAction extends CommonAction {
	
	@Autowired
	private SecGrantService service;//定义SecGrantService属性
	
	 /**
	  * 查询角色信息
	  * @return 成功标识
	  */
    @SuppressWarnings("unchecked")
	public String index(){
    	ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String userId = request.getParameter("userId");
		String whereSQL = " WHERE 1=2";
    	//查询出用户有权限的角色信息
		if (userId != null && !"".equals(userId)) {
			 whereSQL = " WHERE A.ACCOUNT_NAME = '" + userId + "'";
		}
		StringBuilder sb = new StringBuilder( "SELECT DISTINCT(R.ROLE_CODE),ROLE_ID,R.ROLE_NAME FROM ADMIN_AUTH_ACCOUNT_ROLE P " +
											  " LEFT JOIN ADMIN_AUTH_ROLE R ON P.ROLE_ID = R.ID " +
											  " LEFT JOIN ADMIN_AUTH_ACCOUNT A ON A.ID = P.ACCOUNT_ID" + whereSQL );
		
		this.json = service.getRolesInfo(sb.toString());
		addActionMessage("get rolesInfo successfully");
        return "success";
    }
}


