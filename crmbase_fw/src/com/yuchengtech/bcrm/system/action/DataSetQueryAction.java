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
 * @describtion: 数据集关联设置
 *
 * @author : 未知
 */
@ParentPackage("json-default")
@Action(value = "/datasetquery", results = { @Result(name = "success", type = "json") })
public class DataSetQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	private HttpServletRequest request;

	@Override
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);

		StringBuilder s = new StringBuilder("select t1.* from MTOOL_DBTABLE t1 where PARENT_ID <>'0' ");
		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key)
					&& !this.getJson().get(key).equals("")) {
				if (key.equals("dataSetType")){
					s.append(" and PARENT_ID='" + this.getJson().get(key) + "'");
				}
			}
		}
		SQL = s.toString();
		datasource = ds;
	}
}