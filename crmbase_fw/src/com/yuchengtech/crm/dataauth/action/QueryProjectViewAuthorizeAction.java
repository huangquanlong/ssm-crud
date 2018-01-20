package com.yuchengtech.crm.dataauth.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.GrantProxyProjView;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.dataauth.service.ProUserRelateViewService;

/**
 * 
 * @描述: 项目/需求视图树相关查询Action类
 * @author : wzy
 * @date : 2015-01-30
 */
@ParentPackage("json-default")
@Action(value = "/queryProjectViewAuthorizeAction", results = { @Result(name = "success", type = "json"), })
public class QueryProjectViewAuthorizeAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	private HttpServletRequest request;

	@Autowired
	private ProUserRelateViewService proUserRelateViewService;

	private Map<String, Object> JSON;

	public Map<String, Object> getJSON() {
		return JSON;
	}

	public void setJSON(Map<String, Object> jSON) {
		JSON = jSON;
	}

	// 查询所有的授权信息
	public String index() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		try {
			StringBuilder sb = new StringBuilder(
					"select o.* from ocrm_sys_view_manager o  order by o.orders");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}

	// 查询角色保存授权信息
	public String queryAuthorizeData() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		try {
			StringBuilder sb = new StringBuilder(
					"select o.view_id from ocrm_sys_view_user_relation o  where 1=1");
			sb.append(" and role_id='" + request.getParameter("role_id") + "'");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}

	// 根据授权，查询项目视图树形菜单，根据视图类型或项目ID进行查询
	// 视图类型：1、项目视图，2、需求视图
	public String queryCustViewTree() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String viewType = request.getParameter("viewtype");// 视图类型：1项目视图、2需求视图
		String projId = request.getParameter("projId");// 项目ID
		String proRoleIds = proUserRelateViewService.getProRoleByUserId(auth,
				projId);// 查询当前用户在当前项目中具有的角色
		Map<?, ?> numMap = searchCustViewTree(proRoleIds, projId);
		List<?> numList = (List<?>) numMap.get("data");
		Map<?, ?> numMap2 = (Map<?, ?>) numList.get(0);
		StringBuilder sb = new StringBuilder("");
		boolean isSysManager = new GrantProxyProjView().isLogicSysManager(auth);
		try {
			if (!"0".equals(numMap2.get("NUM_ID").toString())) {
				// 有授权菜单的情况，查询出有哪些授权菜单
				sb.append("select distinct o1.* ");
				sb.append(" from ocrm_sys_view_manager o1 ");
				sb.append(" inner join ocrm_sys_view_user_relation o2 ");
				sb.append(" on o1.id = o2.view_id ");
				sb.append(" where 1 = 1");
				sb.append(" and o2.role_id in (");
				sb.append(proRoleIds);
				sb.append(")");
				if (viewType != null && !"".equals(viewType)) {
					sb.append(" and o1.viewtype='" + viewType + "'");
				}
				sb.append(" and o2.proj_id = " + projId);// 项目ID
				sb.append(" order by o1.orders");
			} else {
				if (isSysManager) {
					// 无授权菜单的情况下，如果是系统管理员，查询所有视图菜单
					sb.append("select * from ocrm_sys_view_manager o where 1 = 1");
					if (viewType != null && !"".equals(viewType)) {
						sb.append(" and viewtype='" + viewType + "'");
					}
				} else {
					sb.append("select * from ocrm_sys_view_manager o where 1 != 1");
				}
				sb.append(" order by o.orders");
			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}

	// 查询我的视图树形菜单，根据视图类型或客户号1、项目视图，2、需求视图
	public String queryMyViewTree() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String viewType = request.getParameter("viewtype");// 视图类型：1项目视图、2需求视图
		String projId = request.getParameter("projId");// 项目ID
		StringBuilder roleIdSb = new StringBuilder("");
		boolean isSysManager = new GrantProxyProjView().isLogicSysManager(auth);
		for (int i = 0; i < auth.getAuthorities().size(); i++) {
			if (!"".equals(auth.getAuthorities().get(i).toString())
					&& auth.getAuthorities().get(i) != null) {
				if (i == 0) {
					roleIdSb.append("'"
							+ auth.getAuthorities().get(i).toString() + "'");
				} else {
					roleIdSb.append(",'"
							+ auth.getAuthorities().get(i).toString() + "'");
				}
			}
		}
		String proRoleIds = proUserRelateViewService.getProRoleByUserId(auth,
				projId);// 查询当前用户在当前项目中具有的角色
		String allRole = proRoleIds;
		if (roleIdSb.toString() != null && !"".equals(roleIdSb.toString())) {
			allRole += ("," + roleIdSb.toString());
		}
		Map<?, ?> numMap = searchCustViewTree(allRole, projId);
		List<?> numList = (List<?>) numMap.get("data");
		Map<?, ?> numMap2 = (Map<?, ?>) numList.get(0);
		StringBuilder sb = new StringBuilder("");
		try {
			if (!"0".equals(numMap2.get("NUM_ID").toString())) {
				sb.append("select t1.id,t1.name,t1.addr,t1.orders,t1.viewtype,'0' as parentid,");
				sb.append(" t.id as my_view_id from ocrm_sys_view_manager_comm t inner join ( ");
				sb.append(" select distinct o1.* from ocrm_sys_view_manager o1 ");
				sb.append(" inner join ocrm_sys_view_user_relation o2 on o1.id=o2.view_id ");
				sb.append(" where 1 = 1");
				sb.append(" and o2.role_id in (" + allRole + ")");
				if (viewType != null && !"".equals(viewType)) {
					sb.append(" and viewtype='" + viewType + "'");
				}
				sb.append(") t1 on t1.id = t.view_id ");
				sb.append(" and t.user_id = '" + auth.getUserId() + "'");
				sb.append(" and t.proj_id = " + projId);
				sb.append(" order by t1.orders");
			} else {
				if (isSysManager) {
					sb.append("select t1.id,t1.name,t1.addr,t1.orders,t1.viewtype,'0' as parentid,");
					sb.append(" t.id as my_view_id from ocrm_sys_view_manager_comm t inner join ( ");
					sb.append("select * from ocrm_sys_view_manager o where 1 = 1");
					if (viewType != null && !"".equals(viewType)) {
						sb.append(" and viewtype='" + viewType + "'");
					}
					sb.append(") t1 on t1.id = t.view_id ");
					sb.append(" and t.user_id = '" + auth.getUserId() + "'");
				} else {
					sb.append("select t1.id,t1.name,t1.addr,t1.orders,t1.viewtype,'0' as parentid,");
					sb.append(" t.id as my_view_id from ocrm_sys_view_manager_comm t inner join ( ");
					sb.append("select * from ocrm_sys_view_manager o where 1 != 1");
					sb.append(") t1 on t1.id = t.view_id ");
				}
				sb.append(" order by t1.orders");
			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}

	// 查角色是否被授权过
	public Map<String, Object> searchCustViewTree(String useId, String projId) {
		String s = " select count(t1.id) as num_id "
				+ " from ocrm_sys_view_user_relation t1 "
				+ " where t1.role_id in(" + useId + ") and t1.proj_id = "
				+ projId;
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, ds.getConnection());
			return qh.getJSON();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 查询视图基本信息
	public String queryViewBase() {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			String busiId = request.getParameter("busiId");
			String viewType = request.getParameter("viewtype");
			StringBuffer sb = new StringBuffer();
			if ("0".equals(viewType)) {
				sb.append("SELECT ID,NAME,TYPE FROM ACRM_F_CI_CUSTOMER WHERE CUST_ID = '"
						+ busiId + "'");
			} else if ("1".equals(viewType)) {
				sb.append("select ID,CUST_BASE_NAME AS NAME,GROUP_TYPE AS TYPE from OCRM_F_CI_BASE WHERE ID = '"
						+ busiId + "'");
			} else if ("2".equals(viewType)) {
				sb.append("select ID,GROUP_NAME AS NAME,GROUP_TYPE AS TYPE from OCRM_F_CI_GROUP_INFO WHERE ID = '"
						+ busiId + "'");
			} else if ("3".equals(viewType)) {
				sb.append("select ACCOUNT_NAME AS ID,USER_NAME AS NAME,'' AS TYPE from ADMIN_AUTH_ACCOUNT WHERE ACCOUNT_NAME = '"
						+ busiId + "'");
			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	// 根据项目ID，查询出该项目下所有的需求目录数据（按树结构的形式）
	public String queryProReqDir() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String projId = request.getParameter("projId");// 项目ID
		StringBuilder sb = new StringBuilder("");
		sb.append("select distinct\n");
		sb.append("	t.req_dir_id,\n");
		sb.append("	t.req_dir_no,\n");
		sb.append("	t.req_dir_name,\n");
		sb.append("	t.req_dir_seq,\n");
		sb.append("	'1' as isdir,\n");
		sb.append("	(\n");
		sb.append("		case\n");
		sb.append("		when t1.req_dir_id is null\n");
		sb.append("		or t1.req_dir_id = '' then\n");
		sb.append("			0\n");
		sb.append("		else\n");
		sb.append("			t1.req_dir_id\n");
		sb.append("		end\n");
		sb.append("	) as req_dir_parent_no\n");
		sb.append("from\n");
		sb.append("	ppmp_reqment_dir t\n");
		sb.append("left join ppmp_reqment_dir t1 on t.req_dir_parent_no = t1.req_dir_id\n");
		sb.append("and t.proj_id = t1.proj_id\n");
		sb.append("left join ppmp_project t2 on t2.proj_id = t.proj_id\n");
		sb.append("where\n");
		sb.append("	t.proj_id = " + projId + "\n");
		sb.append("union all\n");
		sb.append("	select\n");
		sb.append("		t.req_id as req_dir_id,\n");
		sb.append("		t.req_no as req_dir_no,\n");
		sb.append("		concat(concat('<font color=\"red\">',t.req_name),'</font>') as req_dir_name,\n");
		sb.append("		'0' as req_dir_seq,\n");
		sb.append("	    '0' as isdir,\n");
		sb.append("		t.req_dir_id as req_dir_parent_no\n");
		sb.append("	from\n");
		sb.append("		ppmp_reqment_base t\n");
		sb.append("	where\n");
		sb.append("		t.if_new = '1'\n");
		sb.append("	and t.req_dir_id is not null\n");
		sb.append("	and t.proj_id = " + projId);
		sb.append("	order by REQ_DIR_ID asc ");
		try {
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}

}
