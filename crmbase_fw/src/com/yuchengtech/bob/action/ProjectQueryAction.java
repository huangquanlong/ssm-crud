package com.yuchengtech.bob.action;

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

@ParentPackage("json-default")
@Action(value = "/projectQueryAction", results = { @Result(name = "success", type = "json"), })
/**
 * @描述：项目查询放大镜Action类
 * @author：wzy
 * @date：2015-02-03
 */
public class ProjectQueryAction extends CommonAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	private HttpServletRequest request;

	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);

		StringBuffer sb = new StringBuffer("");
		sb.append("select\n");
		sb.append("	t1.proj_id,\n");
		sb.append("	t1.proj_no,\n");
		sb.append("	t1.proj_name,\n");
		sb.append("	t1.bank_nane,\n");
		sb.append("	t1.proj_manager,\n");
		sb.append("	t2.user_name as proj_manager_name,\n");
		sb.append("	t1.apply_area,\n");
		sb.append("	t1.proj_type,\n");
		sb.append("	t1.proj_status\n");
		sb.append("from\n");
		sb.append("	ppmp_project t1\n");
		sb.append("left join admin_auth_account t2 on t1.proj_manager = t2.account_name\n");
		sb.append("where\n");
		sb.append("	1 = 1");
		// 如果“项目基线”不为空，增加做为查询条件
		String proBaseLineType = request.getParameter("proBaseLineType");// 基线类型
		if ("1".equals(proBaseLineType)) {
			// 产品项目
			sb.append(" and t1.proj_class = '1'");
		} else if ("2".equals(proBaseLineType)) {
			// 实施项目
			sb.append(" and t1.proj_class = '2'");
		}
		// 增加前台传入的查询条件作为SQL语句中的限制条件
		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key)
					&& !this.getJson().get(key).equals("")) {
				if (null != key && key.equals("PROJ_NO")) {// 项目编号(模糊查询)
					sb.append(" and t1.proj_no like '%"
							+ this.getJson().get(key) + "%'");
				} else if (key.equals("PROJ_NAME")) {// 项目名称(模糊查询)
					sb.append(" and t1.proj_name like '%"
							+ this.getJson().get(key) + "%'");
				} else if (key.equals("PROJ_MANAGER")) {// 项目经理(精确查询)
					sb.append(" and t1.proj_manager = '"
							+ this.getJson().get(key) + "'");
				} else if (key.equals("APPLY_AREA")) {// 实施大区(精确查询)
					sb.append(" and t1.apply_area = '"
							+ this.getJson().get(key) + "'");
				} else if (key.equals("PROJ_TYPE")) {// 项目类型(精确查询)
					sb.append(" and t1.proj_type = '" + this.getJson().get(key)
							+ "'");
				} else if (key.equals("PROJ_STATUS")) {// 项目状态(精确查询)
					sb.append(" and t1.proj_status = '"
							+ this.getJson().get(key) + "'");
				}
			}
		}
		setPrimaryKey("t1.proj_no asc ");// 排序
		// 设置数据字典数据
		addOracleLookup("APPLY_AREA", "APPLY_AREA");// 实施大区
		addOracleLookup("PROJ_TYPE", "PROJ_TYPE");// 项目类型
		addOracleLookup("PROJ_STATUS", "PROJ_STATUS");// 项目状态
		// 给父类属性赋值
		SQL = sb.toString();
		datasource = ds;
	}

}
