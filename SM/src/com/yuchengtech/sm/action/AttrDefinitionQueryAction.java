package com.yuchengtech.sm.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

/**
 * @Title: AttrDefinitionQueryAction
 * @Description:属性定义按条件查找
 * @date 2017年2月16日
 */
@ParentPackage("json-default")
@Action(value = "attrDefinitionQueryAction", results = { @Result(name = "success", type = "json"), })
public class AttrDefinitionQueryAction extends CommonAction {

	private static final long serialVersionUID = 402127864376324618L;
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	public void prepare() {
		StringBuilder sb = new StringBuilder(
				"SELECT ad.`id`,ad.`name`,ad.`comm`,ad.`attr_type`,ad.`display_name`,ad.classify ,"
						+ "ad.type_length,ad.identify FROM sm_attr_definition ad WHERE 1>0");
		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("NAME"))
					sb.append(" and ad." + key + " like '%" + this.getJson().get(key) + "%'");
				else if (key.equals("CLASSIFY"))
					sb.append(" and ad." + key + " = " + this.getJson().get(key));
				else if (key.equals("DISPLAY_NAME"))
					sb.append(" and ad." + key + " like '%" + this.getJson().get(key) + "%'");
				else {
					sb.append(" and ad." + key + " = " + this.getJson().get(key));
				}
			}
		}
		setPrimaryKey("ad.ID");

		SQL = sb.toString();
		datasource = ds;
	}

	public String addAttrClassify() {
		return "success";
	}

}
