package com.yuchengtech.bcrm.system.action;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 部门信息查询Action
 * @author wangwan
 * @since 2012-10-08 
 */

@SuppressWarnings("serial")
@Action("/dptQuery")
public class DptQueryAction extends CommonAction {
	
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	/**
	 * 查询拼装SQL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		this.setJson(request.getParameter("condition"));
		String belongOrgId = request.getParameter("belongOrgId");
		
		//查询出角色信息,通过对用户表，用户角色信息表，用户授权角色信息表做关联，设置标志位，以显示用户已被授权的角色信息
		StringBuilder sb = new StringBuilder("select t.* from admin_auth_dpt t where t.belong_org_id="+belongOrgId );
        
        
        SQL = sb.toString();
        datasource=ds;
	}
}


