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
 * 数据权限过滤器配置Filter查询
 * @author wz
 * @since 2012-11-16
 */

@ParentPackage("json-default")
@Action(value = "/dataGrantFilterQueryAction", results = { @Result(name = "success", type = "json") })

public class DataGrantFilterQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	private HttpServletRequest request;

	/**
	 * 创建查询SQL
	 */
	public void prepare() {
		
		ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String selectName = request.getParameter("selectName");
		StringBuilder querySql = new StringBuilder();
		querySql.append("select f.id, f.class_name, f.method_name, f.role_id, f.sql_string, f.describetion ");
		querySql.append("from auth_sys_filter f ");
		querySql.append("where 1=1 ");
		querySql.append("and f.class_name ='"+selectName+"'");
		
		SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
		
		setPrimaryKey("f.id"); //设置查询排序条件
		
		datasource = ds; //为父类数据源赋值
		this.limit= 10000000;
	}
}
