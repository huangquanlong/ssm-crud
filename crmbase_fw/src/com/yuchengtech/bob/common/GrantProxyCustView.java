package com.yuchengtech.bob.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.JdbcUtil;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.crm.sec.common.SystemUserConstance;

/**
 * @describtion: 角色视图控制点处理类
 *
 * @author : chengmeng
 * @date : 2015年1月16日 上午11:18:10
 */
public class GrantProxyCustView {
	
	/**
	 * 日志组件
	 */
	private static Logger log = Logger.getLogger(GrantProxyCustView.class);
	
	/**
	 * 角色视图控制点权限常量
	 */
	private static final String VIEW_CONTROL_ROLE = "controlByRoleIdAndView";

	/**
	 * 根据角色ID和视图ID查询权限控制点
	 * @param viewId ：视图ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getControlByRoleIdAndViewId(AuthUser auth, String viewId) {
		List<String> retList = new ArrayList<String>();
		if (viewId == null || "".equals(viewId)){
			return retList;
		}
		
		//判断session里是否已有视图控制点的list集合
		if (auth.getAttribute(VIEW_CONTROL_ROLE) == null) {
			initViewControlRole();
		}
		
		List<Map<String,String>> allViewList = (List<Map<String,String>>) auth.getAttribute(VIEW_CONTROL_ROLE);
		int size = allViewList.size();
		Map<String,String> tempMap = null;
		for (int i = 0; i < size; i++) {
			tempMap = allViewList.get(i);
			//只取当前视图viewId对应的控制点
			if (viewId.equals(tempMap.get("MANAGER_ID"))) {
				retList.add(tempMap.get("CONTROL_CODE"));
			}
		}
		return retList;
	}
	
	/**
	 * 初始化角色视图控制点权限
	 */
	@SuppressWarnings("rawtypes")
	private void initViewControlRole(){
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean isLogicSysManagerFlag = this.isLogicSysManager(auth);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<Map> retList = new ArrayList<Map>();
		StringBuffer roleIdStr = new StringBuffer("''");
		
		//取当前用户所属角色id集合,查询所有角色权限的控制点
		for (int i = 0; i < auth.getRolesInfo().size(); i++) {
			Map<?, ?> tempMap = (HashMap<?, ?>) auth.getRolesInfo().get(i);
			roleIdStr.append(",'").append(tempMap.get("ID")).append('\'');
		}
		StringBuffer sb = new StringBuffer(160);
		//根据是否逻辑系统管理员,获取权限不同.若是系统管理员权限直接获取所有控制点权限
		if (isLogicSysManagerFlag){
			sb.append("SELECT DISTINCT A.MANAGER_ID,A.CONTROL_CODE FROM OCRM_SYS_VIEW_MANAGER_CONTROL A ORDER BY A.MANAGER_ID ");
		} else {
			sb.append("SELECT DISTINCT A.MANAGER_ID,A.CONTROL_CODE FROM OCRM_SYS_VIEW_MANAGER_CONTROL A LEFT JOIN OCRM_SYS_VIEW_USER_RELATION B ON B.VIEW_ID=A.ID WHERE B.IF_FLAG='1' AND  B.ROLE_ID IN( ")
			    .append(roleIdStr.toString()).append(") ORDER BY A.MANAGER_ID ");
		}
		//db2数据库,添加with ur 避免造成锁表
		if ("DB2".equals(SystemConstance.DB_TYPE)){
       	 	sb.append(" WITH UR");
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
				retList.add(tempMap);
			}
			auth.putAttribute(VIEW_CONTROL_ROLE,retList);
		} catch (SQLException e) {
			throw new BizException(1, 2, "1002", e.getMessage());
		} finally {
			JdbcUtil.close(rs, stmt, conn);
		}
	}

	/**
	 * 逻辑系统管理判断
	 * @param auth session用户信息
	 * @return boolean true表示是,false表示否
	 **/
	private boolean isLogicSysManager(AuthUser auth) {
		boolean isLogicSysManagerFlag = false;
		for (int i = 0; i < auth.getRolesInfo().size(); i++) {
			Map<?, ?> tempMap = (HashMap<?, ?>) auth.getRolesInfo().get(i);
			String roleCode = (String) tempMap.get("ROLE_CODE");
			//是否系统管理员
			if (SystemUserConstance.LOGIC_SYSTEM_USER_ID.equals(roleCode)
					|| SystemUserConstance.SUPER_SYSTEM_USER_ID.equals(roleCode)
					|| SystemUserConstance.SYSTEM_ADMIN_ID.equals(roleCode)) {
				isLogicSysManagerFlag = true;
				break;
			}
		}
		return isLogicSysManagerFlag;
	}
}
