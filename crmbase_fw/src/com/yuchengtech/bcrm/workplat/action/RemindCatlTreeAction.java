package com.yuchengtech.bcrm.workplat.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

/**
 * 
 * 信息提醒类型树--左侧功能模块树的初始化
 * @author luyy
 * @since 2014-2-18
 */

@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/remindType", results = { @Result(name = "success", type = "json")})
public class RemindCatlTreeAction extends CommonAction {
	
	//数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
   
	/**
	 *类型信息查询SQL
	 */
	public void prepare() {
		StringBuilder sb = new StringBuilder("select f_value,f_code,f_id,'t'||SUBSTR(f_code,0,1) as type from OCRM_SYS_LOOKUP_ITEM where f_lookup_id='REMIND_TYPE' ");
		setPrimaryKey(" f_id ASC");
		SQL=sb.toString();
		datasource = ds;
	}
}


