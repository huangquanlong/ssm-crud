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
 * 
 * 菜单项管理--左侧功能模块树的初始化
 * 
 * @author GUOCHI
 * @since 2012-10-12
 * 
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/fwFunctionTree-action", results = { @Result(name = "success", type = "json") })
public class FwFunctionTreeAction extends CommonAction {

	// 数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	/**
	 *模块功能查询
	 *修改了sql，Oracle中链接字符串用||，mysql不认识这个，改为concat函数
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String moduleId = request.getParameter("mdulId");
		StringBuilder sb = new StringBuilder(
				" select  0 as parent_id, " +
				" CONCAT(c.func_name,'(',(select count(*) from AUTH_RES_CONTROLLERS B WHERE c.ID=B.FW_FUN_ID),')') as AMOUNT_NAME," +
				"  c.* "+ 
				" from FW_FUNCTION c " + 
				" where 1>0 ");
		if (null != moduleId && !"".equals(moduleId.trim())) {
			sb.append(" and  c.MODULE_ID = '" + moduleId + "' ");
		}
		
		setPrimaryKey("AMOUNT_NAME");
		SQL = sb.toString();
		datasource = ds;
	}
}
