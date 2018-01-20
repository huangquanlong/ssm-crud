package com.yuchengtech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

/**
 * 数据权限过滤器配置Map查询
 * @author wz
 * @since 2012-11-16
 */

@ParentPackage("json-default")
@Action(value = "/dataGrantMapQueryAction", results = { @Result(name = "success", type = "json") })

public class DataGrantMapQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源

	/**
	 * 创建查询SQL
	 */
	public void prepare() {
		
		StringBuilder querySql = new StringBuilder();
		querySql.append("select m.id, m.function_id, m.class_name, m.class_desc ");
		querySql.append("from auth_sys_filter_map m ");
		querySql.append("where 1=1 ");
		
		SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
		
		setPrimaryKey("m.id"); //设置查询排序条件
		
		datasource = ds; //为父类数据源赋值
		this.limit= 10000000;
	}
}
