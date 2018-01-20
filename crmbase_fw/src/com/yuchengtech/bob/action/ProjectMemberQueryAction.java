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

@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/projectMemberQueryAction", results = { @Result(name = "success", type = "json"), })
/**
 * @描述：项目成员查询放大镜Action类，必选传入项目ID，查询这个项目下的所有成员信息
 * @author：wzy
 * @date：2015-02-07
 */
public class ProjectMemberQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	private HttpServletRequest request;

	// 覆盖父类的构造SQL语句的方法
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		StringBuffer sb = new StringBuffer("");
		sb.append("select distinct\n");
		sb.append("	t1.id,\n");
		sb.append("	t1.account_name,\n");
		sb.append("	t1.user_name,\n");
		sb.append("	t2.org_id,\n");
		sb.append("	t2.org_name,\n");
		sb.append("	t5.prole_code as proj_role_code\n");
		sb.append("from\n");
		sb.append("	admin_auth_account t1\n");
		sb.append("left join admin_auth_org t2 on t1.org_id = t2.org_id\n");
		sb.append("left join ppmp_project_member t3 on t1.account_name = t3.account_id\n");
		sb.append("inner join ppmp_project_rolememr_rel t4 on t3.mem_id = t4.mem_id\n");
		sb.append("inner join ppmp_project_role t5 on t4.prole_id = t5.prole_id\n");
		sb.append("where\n");
		sb.append("	1 = 1\n");
		// 隐含查询条件：项目ID
		String proj_id = request.getParameter("proj_id");// 项目ID
		if (proj_id != null && !"".equals(proj_id)) {// 项目ID不为空，进行正常查询
			sb.append(" and t3.proj_id = " + proj_id);
			// 增加前台页面传入的查询条件
			for (String key : this.getJson().keySet()) {
				if (null != this.getJson().get(key)
						&& !this.getJson().get(key).equals("")) {
					if (null != key && key.equals("USER_NAME")) {// 成员名称（模糊查询）
						sb.append(" and (t1.user_name like '%"
								+ this.getJson().get(key)
								+ "%' or t1.account_name like '%"
								+ this.getJson().get(key) + "%')");
					} else if (key.equals("PROLE_CODE")) {// 成员角色
						sb.append(" and (t5.prole_code in ("
								+ this.getJson().get(key) + "))");
					}
				}
			}
		} else {// 项目ID为空，查询不出任何数据
			sb.append(" and 1 != 1");
		}
		setPrimaryKey("t1.account_name asc ");// 排序
		// 设置数据字典数据
		addOracleLookup("PROJ_ROLE_CODE", "PROJ_ROLE_CODE");// 项目角色
		// 给父类属性赋值
		SQL = sb.toString();
		datasource = ds;
	}

}
