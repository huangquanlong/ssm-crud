package com.yuchengtech.bcrm.system.action;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.constance.SystemConstance;

/**
 * 角色授权用户信息查询Action
 * @author weilh
 * @since 2012-10-11 
 */

@SuppressWarnings("serial")
@Action("/roleWarrantUserInfoQuery")
public class RoleWarrantUserInfoQueryAction extends CommonAction {
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性

	/**
	 * 角色授权用户信息查询拼装SQL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		String roleId = request.getParameter("roleId");
		
		//查询出角色授权用户信息
		StringBuilder sb = new StringBuilder("SELECT ACC.USER_NAME, ACC.ACCOUNT_NAME, ACC.ORG_ID, ORG.UNITNAME,AR.ID "
				+"FROM ADMIN_AUTH_ACCOUNT_ROLE  AR INNER  JOIN ADMIN_AUTH_ACCOUNT ACC ON ACC.ID = AR.ACCOUNT_ID " 
				+" INNER  JOIN SYS_UNITS ORG ON ORG.UNITID = ACC.ORG_ID WHERE AR.APP_ID = ");
		sb.append(SystemConstance.LOGIC_SYSTEM_APP_ID);
		
		if (roleId != null) {//判断，根据角色Id不同，查询出对应角色授权用户信息
			if (roleId.length() > 0) {
				sb.append(" AND AR.ROLE_ID = "+roleId);
			}
		}
        
        SQL = sb.toString();
        this.setPrimaryKey("ACC.ORG_ID");
        datasource=ds;
	}
}



