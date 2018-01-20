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
 * 模块查询
 * @author GUOCHI
 * @since 2012-10-15
 */

@ParentPackage("json-default")
@Action(value = "/controllerManagerQuery", results = { @Result(name = "success", type = "json") })
public class ControllersManagerQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源

	/**
	 * 创建查询SQL并为父类中相应属性赋值
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String fwFunId = request.getParameter("fwFunId");
		StringBuilder querySql = new StringBuilder("SELECT * from AUTH_RES_CONTROLLERS WHERE 1>0 ");
		if (fwFunId!=null && !fwFunId.equals("")){
			querySql.append(" and FW_FUN_ID='"+fwFunId+"'");
		} else{
		    querySql.append(" and 1<0" );
		}
		SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
		datasource = ds; //为父类数据源赋值
	}
}
