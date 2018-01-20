package com.yuchengtech.bob.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.JdbcUtil;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.crm.sec.common.SystemUserConstance;

/**
 * @描述：项目角色对应的视图控制点处理类
 * @author：wzy
 * @date：2015-02-02
 */
public class GrantProxyProjView {

	// 日志组件
	private static Logger log = Logger.getLogger(GrantProxyProjView.class);

	// 角色视图控制点权限常量
	private static final String VIEW_CONTROL_ROLE = "controlByRoleIdAndView";

	/**
	 * 根据角色ID和视图ID查询权限控制点
	 * 
	 * @param viewId
	 *            ：视图ID
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getControlByRoleIdAndViewId(AuthUser auth,
			String viewId, String projId) {
		List<String> retList = new ArrayList<String>();
		if (viewId == null || "".equals(viewId)) {
			return retList;
		}
		// 判断session里是否已有视图控制点的list集合
		// 由于每一个项目的控制点授权不一样，所以，每次打开项目视图菜单进行查询，保证后面不覆盖前面的数据
		initViewControlRole(projId);
		Map allViewMap = (Map) auth.getAttribute(VIEW_CONTROL_ROLE);
		if (allViewMap != null && allViewMap.get(projId) != null) {
			List<Map> allViewList = (List<Map>) allViewMap.get(projId);
			int size = allViewList.size();
			Map<String, String> tempMap = null;
			for (int i = 0; i < size; i++) {
				tempMap = allViewList.get(i);
				// 只取当前视图viewId对应的控制点
				if (viewId.equals(tempMap.get("MANAGER_ID"))
						&& projId.equals(tempMap.get("PROJ_ID"))) {
					retList.add(tempMap.get("CONTROL_CODE"));
				}
			}
		}
		return retList;
	}

	/**
	 * 初始化角色视图控制点权限
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initViewControlRole(String projId) {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Map allViewMap = auth.getAttribute(VIEW_CONTROL_ROLE) == null ? null
				: (Map) auth.getAttribute(VIEW_CONTROL_ROLE);
		if (allViewMap != null && allViewMap.get(projId) != null) {
			// 如果会话中已有当前用户、对应的项目的控制点信息，那么不再进行查询
			return;
		}
		boolean isLogicSysManagerFlag = this.isLogicSysManager(auth);
		Map proControl = new TreeMap();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Map> retList = new ArrayList<Map>();
		StringBuffer roleIdStr = new StringBuffer("''");
		// 取当前用户所属角色id集合,查询所有角色权限的控制点
		for (int i = 0; i < auth.getRolesInfo().size(); i++) {
			Map<?, ?> tempMap = (HashMap<?, ?>) auth.getRolesInfo().get(i);
			roleIdStr.append(",'").append(tempMap.get("ID")).append('\'');
		}
		roleIdStr.append("," + this.getProRoleByUserId(auth, projId));
		StringBuffer sb = new StringBuffer("");
		// 根据是否逻辑系统管理员,获取权限不同.若是系统管理员权限直接获取所有控制点权限
		if (isLogicSysManagerFlag) {
			sb.append("select distinct a.manager_id,a.control_code ");
			sb.append(" from ocrm_sys_view_manager_control a order by a.manager_id ");
		} else {
			sb.append("Select distinct a.manager_id,a.control_code ");
			sb.append(" from ocrm_sys_view_manager_control a left join ");
			sb.append(" ocrm_sys_view_user_relation b on b.view_id=a.id ");
			sb.append(" where ");
			sb.append(" b.if_flag = '1' ");// 类型是控制点
			sb.append(" and b.proj_id = " + projId);// 项目ID
			sb.append(" and  b.role_id in (");
			sb.append(roleIdStr.toString() + ")");// 角色ID
			sb.append(" order by a.manager_id asc");
		}
		// db2数据库,添加with ur 避免造成锁表
		if ("DB2".equals(SystemConstance.DB_TYPE)) {
			sb.append(" with ur");
		}
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			log.info("JDBC原始查询语句：" + sb.toString());
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("MANAGER_ID", rs.getString("MANAGER_ID"));
				tempMap.put("CONTROL_CODE", rs.getString("CONTROL_CODE"));
				tempMap.put("PROJ_ID", projId);
				retList.add(tempMap);
			}
			if (allViewMap != null) {
				allViewMap.put(projId, retList);
			} else {
				proControl.put(projId, retList);
				auth.putAttribute(VIEW_CONTROL_ROLE, proControl);
			}
		} catch (SQLException e) {
			throw new BizException(1, 2, "1002", e.getMessage());
		} finally {
			JdbcUtil.close(rs, stmt, conn);
		}
	}

	/**
	 * 逻辑系统管理判断
	 * 
	 * @param auth
	 *            session用户信息
	 * @return boolean true表示是,false表示否
	 **/
	public boolean isLogicSysManager(AuthUser auth) {
		boolean isLogicSysManagerFlag = false;
		for (int i = 0; i < auth.getRolesInfo().size(); i++) {
			Map<?, ?> tempMap = (HashMap<?, ?>) auth.getRolesInfo().get(i);
			String roleCode = (String) tempMap.get("ROLE_CODE");
			// 是否系统管理员
			if (SystemUserConstance.LOGIC_SYSTEM_USER_ID.equals(roleCode)
					|| SystemUserConstance.SUPER_SYSTEM_USER_ID
							.equals(roleCode)
					|| SystemUserConstance.SYSTEM_ADMIN_ID.equals(roleCode)) {
				isLogicSysManagerFlag = true;
				break;
			}
		}
		return isLogicSysManagerFlag;
	}

	// 根据项目ID及当前用户账户，查询当前用户具有哪些项目角色
	// 返回格式为：'code1','code2'...
	public String getProRoleByUserId(AuthUser auth, String projId) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String rsStr = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("select\n");
		sb.append("	t3.prole_code\n");
		sb.append("from\n");
		sb.append("	ppmp_project_member t1,\n");
		sb.append("	ppmp_project_rolememr_rel t2,\n");
		sb.append("	ppmp_project_role t3\n");
		sb.append("where\n");
		sb.append("	t2.mem_id = t1.mem_id\n");
		sb.append("and t2.prole_id = t3.prole_id\n");
		sb.append("and t1.account_id = '" + auth.getUserId() + "'\n");
		sb.append("and t1.proj_id = " + projId);
		try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			log.info("JDBC原始查询语句：" + sb.toString());
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				rsStr += "'p_" + rs.getString("prole_code") + "',";
			}
		} catch (SQLException e) {
			throw new BizException(1, 2, "1002", e.getMessage());
		} finally {
			JdbcUtil.close(rs, stmt, conn);
		}
		// 去掉最后的逗号
		if (!"".equals(rsStr)
				&& ",".equals(rsStr.substring(rsStr.length() - 1))) {
			rsStr = rsStr.substring(0, rsStr.length() - 1);
		}
		if ("".equals(rsStr)) {
			rsStr = "''";
		}
		return rsStr;
	}
}
