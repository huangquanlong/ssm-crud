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
 * 左表关联字段查询
 * helin,20140626,解决当传入参数为null时报错问题
 * 
 * @author songxs
 * @since 2012-12-6
 */
@ParentPackage("json-default")
@Action(value = "/queryJoinLeftCol", results = { @Result(name = "success", type = "json") })
public class QueryJoinLeftColAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	@Override
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String joinLeftTable = request.getParameter("joinLeftCols");
		if (joinLeftTable == null) {
			joinLeftTable = "-1";
		}
		Long a = Long.valueOf(joinLeftTable);
		StringBuffer builder = new StringBuffer("select c.id as value,c.col_name_e as name from MTOOL_DBCOL c where 1=1 and c.DBTABLE_ID = "+ a + "");
		setPrimaryKey("c.id");
		// 设置父类中的信息
		SQL = builder.toString();
		datasource = ds;
	}

}
