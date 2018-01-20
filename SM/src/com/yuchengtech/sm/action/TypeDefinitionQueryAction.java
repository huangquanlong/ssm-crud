package com.yuchengtech.sm.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

/**
 * @Title: TypeDefinitionQueryAction 
* @Description:类型定义按条件查找  
* @date 2017年2月20日 
 */
@ParentPackage("json-default")
@Action(value = "typeDefinitionQuery", results = { @Result(name = "success", type = "json"), })
public class TypeDefinitionQueryAction extends CommonAction {

	private static final long serialVersionUID = 402127864376324618L;
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	public void prepare() {
		StringBuilder sb = new StringBuilder(
				"SELECT t.`id`,t.`name`,t.`comm`,t.`display_name`,t.`classify`,t.`identify`,t.`state`"
				+ " FROM sm_type_definition  t WHERE 1>0");
		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("NAME"))
					sb.append(" and t." + key + " like '%" + this.getJson().get(key) + "%'");
				else if (key.equals("STATE"))
					sb.append(" and t." + key + " like '%" + this.getJson().get(key) + "%'");
				else if (key.equals("DISPLAY_NAME"))
					sb.append(" and t." + key + " like '%" + this.getJson().get(key) + "%'");
				else {
					sb.append(" and t." + key + " = " + this.getJson().get(key));
				}
			}
		}
		setPrimaryKey("t.id");

		SQL = sb.toString();
		datasource = ds;
	}

	public String addAttrClassify() {
		return "success";
	}

}
