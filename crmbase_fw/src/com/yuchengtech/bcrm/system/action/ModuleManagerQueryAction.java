package com.yuchengtech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

/**
 * 模块查询
 * @author GUOCHI
 * @since 2012-10-09
 */

@ParentPackage("json-default")
@Action(value = "/moduleManagerQuery", results = { @Result(name = "success", type = "json") })

public class ModuleManagerQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源

	/**
	 * 创建查询SQL并为父类中相应属性赋值
	 */
	public void prepare() {
		
		
		StringBuilder querySql = new StringBuilder("SELECT A.*," +//创建查询SQL
				" (select count(*) from FW_FUNCTION B WHERE A.ID=B.MODULE_ID) as AMOUNT"+ 
				" from FW_MODULE A"+
				" WHERE 1>0 ");
		
		for (String key : this.getJson().keySet()) { //循环获取查询条件
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("MDUL_NAME")){ //模块名称
					querySql.append(" AND A.MDUL_NAME like '%" + this.getJson().get(key) + "%'");
				} 
			}
		}
		
		SQL = querySql.toString(); //为父类SQL属性赋值（设置查询SQL）
		
		setPrimaryKey("A.MDUL_NAME"); //设置查询排序条件
		
		datasource = ds; //为父类数据源赋值
	}
}
