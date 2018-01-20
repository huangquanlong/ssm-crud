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

/**
 * 反选控制点用查询
 * @author wz
 * @since 2012-10-17
 */
@ParentPackage("json-default")
@Action(value="/ctlAttrDataQuery", results={
    @Result(name="success", type="json"),
})
public class CtlAttrDataQueryAction extends CommonAction{
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	private HttpServletRequest request;
	
	/**
	 * 反选控制点用SQL
	 * @author wz
	 * @since 2012-10-17
	 */
	public void prepare(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String nodeId = request.getParameter("nodeId");
		String roleCode = request.getParameter("roleCode");
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id,a.app_id,a.version,a.type,a.res_code,a.attr_code,a.operate_key,a.res_id,a.attr_id " +
				  "from auth_res_control_attr_data a " );
		sb.append(" where 1=1 ");
		sb.append(" and a.res_code = '" + nodeId +"'");
		sb.append(" and a.attr_code = '" + roleCode + "'");
		SQL=sb.toString();
		datasource = ds;
	}
}	







