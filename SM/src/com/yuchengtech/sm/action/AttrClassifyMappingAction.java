package com.yuchengtech.sm.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

@ParentPackage("json-default")
@Action(value = "/attrClassifyMappingAction", results = { @Result(name = "success", type = "json"), })
public class AttrClassifyMappingAction extends CommonAction {

	private static final long serialVersionUID = 5065660773429771001L;
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	public void prepare() {
		StringBuilder sb = new StringBuilder("SELECT ac.`id`,ac.`name` FROM sm_attr_classify ac WHERE 1>0");
		setPrimaryKey("id");

		SQL = sb.toString();
		datasource = ds;
	}
}
