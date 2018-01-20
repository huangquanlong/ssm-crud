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
@Action(value = "/fwFunctionExtTree-action", results = { @Result(name = "success", type = "json") })
public class FwFunctionExtTreeAction extends CommonAction {

	// 数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	/**
	 *模块功能查询
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String moduleId = request.getParameter("id");
		StringBuilder sb = new StringBuilder(
				" select *from OCRM_F_SYS_FW_FUNCTION_EXT c where 1>0 ");
		if (null != moduleId && !"".equals(moduleId.trim())) {
			sb.append(" and  c.MODULE_ID = '" + moduleId + "' ");
		}
		
//		setPrimaryKey("AMOUNT_NAME");
		SQL = sb.toString();
		datasource = ds;
	}
}
