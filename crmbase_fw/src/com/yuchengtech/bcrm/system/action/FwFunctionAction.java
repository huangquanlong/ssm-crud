package com.yuchengtech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;
/**
 * 
 * 菜单项管理--右侧功能模块树的初始化
 * @author songxs
 * @since 2012-9-23
 * 
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/fwFunction-Action", results = { @Result(name = "success", type = "json")})
public class FwFunctionAction extends CommonAction {
	
	//数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
   
	/**
	 *模块功能查询
	 */
	public void prepare() {
		StringBuilder sb = new StringBuilder(
				" select c.MODULE_ID as parent_id ,c.FUNC_NAME AS NAME ,c.ID AS ID,c.ACTION AS ACTION from  FW_FUNCTION c union ALL " +
				" select  0 as parent_id,t.MDUL_NAME AS NAME ,t.id as id ,'' AS ACTION from FW_MODULE t  where 1>0");
		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("c.ID")){
					sb.append(" and " + key + " like '%"
							+ this.getJson().get(key) + "%'");
				} else {
					sb.append(" and " + key + " = " + this.getJson().get(key));
				}
			}
		}
		setPrimaryKey("NAME");
		SQL=sb.toString();
		datasource = ds;
	}
}

