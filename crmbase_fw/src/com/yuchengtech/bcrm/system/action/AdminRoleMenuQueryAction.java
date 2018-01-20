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
 * 
 * @author 
 * @since 2012-10-9
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value="/adminRoleMenuQuery", results={
    @Result(name="success", type="json"),
})
public class AdminRoleMenuQueryAction extends CommonAction{
	
	//数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
	private HttpServletRequest request;
	
	/**
	 * 
	 */
	public void prepare(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String roleId = request.getParameter("roleId");
		SystemConstance sc = new SystemConstance();
		StringBuffer sb = new StringBuffer();
		sb.append("select m.id,m.version,m.app_id,m.type,m.res_id,m.attr_id,m.res_code,m.attr_code,m.operate_key " +
				  "from auth_res_attr_data m where 1=1 and m.app_id = " + sc.LOGIC_SYSTEM_APP_ID );
		sb.append(" and m.attr_code = '" + roleId +"'");
		SQL=sb.toString();
		datasource = ds;
	}
}	







