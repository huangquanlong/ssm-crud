package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.constance.SystemConstance;

/**
 * 反选菜单栏查询
 * @author 
 * @since 2012-10-9
 */
@ParentPackage("json-default")
@Action(value="/roleMenuQuery", results={
    @Result(name="success", type="json"),
})
public class RoleMenuQueryAction extends CommonAction{
	
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	private HttpServletRequest request;
	
	/**
	 * 反选菜单栏SQL
	 * @author wz
	 * @since 2012-10-17
	 */
	public void prepare(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String roleId = request.getParameter("roleId");
		StringBuffer sb = new StringBuffer();
		sb.append("select m.id, m.app_id,  m.res_id, m.attr_id, m.res_code,m.attr_code, m.operate_key ,'1' as SORT " +
				"from auth_res_attr_data m where 1=1 and m.app_id = " + SystemConstance.LOGIC_SYSTEM_APP_ID );
		sb.append(" and m.attr_code = '" + roleId +"'");
		sb.append(" union all ");
		sb.append("select t.id, t.app_id,t.res_id, t.attr_id, t.res_code, t.attr_code,t.operate_key ,'2' as SORT " +
				" from AUTH_RES_CONTROL_ATTR_DATA t " +
				" where t.attr_code = '" + roleId +"' and t.app_id = " + SystemConstance.LOGIC_SYSTEM_APP_ID);
		SQL=sb.toString();
		datasource = ds;
		setPrimaryKey("SORT");
		this.limit= 10000000;}
}	







