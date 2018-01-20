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
 * 控制点查询
 * @author wz
 * @since 2012-10-9
 */
@ParentPackage("json-default")
@Action(value="/optionCtlQuery", results={
    @Result(name="success", type="json"),
})
public class OptionCtlQueryAction extends CommonAction{
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	private HttpServletRequest request;
	
	/**
	 * 控制点查询SQL
	 * @author wz
	 * @since 2012-10-17
	 */
	public void prepare(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String nodeId = request.getParameter("nodeId");
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id,a.name,a.con_code,a.app_id,a.version,a.remark,a.fw_fun_id " +
				  "from auth_res_controllers a " );
		sb.append(" left join CNT_MENU c on c.MOD_FUNC_ID = a.FW_FUN_ID ");
		sb.append(" where 1=1 ");
		sb.append(" and c.id = '" + nodeId +"'");
		SQL=sb.toString();
		datasource = ds;
	}
}	







