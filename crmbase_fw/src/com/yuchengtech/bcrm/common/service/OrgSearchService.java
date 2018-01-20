package com.yuchengtech.bcrm.common.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.core.QueryHelper;


@Service
@Transactional
public class OrgSearchService {
	
	@Autowired
    @Qualifier("dsOracle")
    private DataSource dsOracle;
	
	/**
	 * 通过机构号，查询机构对象
	 * 
	 * @param org
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getOrgInfo(String orgId) {
		String s = "SELECT UNIT.* FROM SYS_UNITS UNIT WHERE UNIT.UNITID='"
				+ orgId + "' ";
		QueryHelper qh;
		List org;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			org = (List) qh.getJSON().get("data");
			if (org != null && org.size() > 0) {
				Map res = (Map) org.get(0);
				return res;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 查询子机构树
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> searchSubOrgTree(String orgId){
		
        String seq = orgSeq(orgId);
		String s = "SELECT UNIT.* FROM SYS_UNITS UNIT WHERE UNITSEQ LIKE '"+seq+"%' order by UNIT.levelunit,UNIT.id";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			return qh.getJSON();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
//	/**
//	 * 查询指标类型
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String,Object> searchSubIndexTypeTree()
//	{
//		
//		String s = "SELECT * FROM OCRM_SYS_INDEX_TYPE";
//		QueryHelper qh;
//		try 
//		{
//			qh = new QueryHelper(s, dsOracle.getConnection());
//			return qh.getJSON();
//		} 
//		catch (SQLException e) 
//		{
//			e.printStackTrace();
//			return null;
//		}
//		
//	}
	
	/**
	 * 查询直接子机构
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> searchSubOrgs(String orgId){
        
        StringBuffer sb = new StringBuffer("SELECT UNIT.ID,UNIT.APP_ID,UNIT.UNITID,UNIT.UNITNAME,UNIT.SUPERUNITID,UNIT.LEVELUNIT " +
        		"FROM SYS_UNITS UNIT WHERE UNIT.SUPERUNITID='" +orgId+"' order by UNIT.UNITID");
        QueryHelper qh;
		try {
			qh = new QueryHelper(sb.toString(), dsOracle.getConnection());
			return qh.getJSON();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		
	}
	
	/**
	 * 查询所有机构
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> searchAllOrgs(){
        
        StringBuffer sb = new StringBuffer("SELECT UNIT.ID,UNIT.APP_ID,UNIT.UNITID,UNIT.UNITNAME,UNIT.SUPERUNITID,UNIT.LEVELUNIT " +
        		"FROM SYS_UNITS UNIT");
        QueryHelper qh;
		try {
			qh = new QueryHelper(sb.toString(), dsOracle.getConnection());
			return qh.getJSON();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 查询父机构
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchParentOrg(String orgId) throws Exception{
		
		StringBuffer sb = new StringBuffer("SELECT UNIT.ID,UNIT.APP_ID,UNIT.UNITID,UNIT.UNITNAME,UNIT.SUPERUNITID,UNIT.LEVELUNIT " +
				"FROM SYS_UNITS UNIT " +
				"WHERE UNIT.UNITID IN ( SELECT U.SUPERUNITID FROM SYS_UNITS U WHERE U.UNITID = '"+orgId+"'  )");
		QueryHelper qh = new QueryHelper(sb.toString(), dsOracle.getConnection());
		return qh.getJSON();
	}
	
	/**
	 * 查询所有父、祖机构
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchPathInOrgTree(String orgId) throws Exception{
		StringBuffer sb = new StringBuffer("select * from sys_units t where unitid='"+orgId+"' ");
		QueryHelper qh = new QueryHelper(sb.toString(), dsOracle.getConnection());
		return qh.getJSON();
	}
	
	/**
	 * 通过机构号，查询机构名称
	 * @param org
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String orgName(String org){
		String s = "SELECT UNIT.UNITNAME FROM SYS_UNITS UNIT WHERE UNIT.UNITID='"+org+"' ";
		QueryHelper qh;
		List names;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			names = (List)qh.getJSON().get("data");
			if(names!=null&&names.size()>0){
				Map res = (Map)names.get(0);
				return (String)res.get("UNITNAME");
			}else return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 通过用户ID，查询用户姓名
	 * @param org
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getUserName(String userId){
		String s = "select user_name from admin_auth_account where account_name='"+userId+"' ";
		QueryHelper qh;
		List names;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			names = (List)qh.getJSON().get("data");
			if(names!=null&&names.size()>0){
				Map res = (Map)names.get(0);
				return (String)res.get("USER_NAME");
			}else return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 通过机构号，查询机构层级序列
	 * @param org
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String orgSeq(String org){
		String s = "SELECT UNIT.UNITSEQ FROM SYS_UNITS UNIT WHERE UNIT.UNITID='"+org+"' ";
		QueryHelper qh;
		List names;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			names = (List)qh.getJSON().get("data");
			if(names!=null&&names.size()>0){
				Map res = (Map)names.get(0);
				return (String)res.get("UNITSEQ");
			}else return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询所有用户信息
	 * @return
	 */
	public Map<String,Object> searchAllUsers(){
		String s = "SELECT USERS.* FROM SYS_USERS USERS";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			Map<String, Object> users = qh.getJSON();
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询所有用户信息指定角色
	 * @param roleId
	 * @return
	 */
	public Map<String,Object> searchAllUsers(String roleId){
		String s = "SELECT USERS.* FROM SYS_USERS USERS left join ADMIN_AUTH_ACCOUNT_ROLE b on  b.ACCOUNT_ID=USERS.ID ";
		
		if(roleId!=null&&!"".equals(roleId))
			s=s+"  Where b.ROLE_ID='"+roleId+"'";
		
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			Map<String, Object> users = qh.getJSON();
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询指定机构用户信息
	 * @param org
	 * @return
	 */
	public Map<String,Object> SearchOrgUsers(String org,Map condition){
		String s = "SELECT USERS.* FROM SYS_USERS USERS WHERE USERS.UNITID='"+org+"' ";
		String userId = (String)condition.get("COM_USER_ID");
		String userName = (String)condition.get("COM_USER_NAME");
		if(userId!=null)
			s += " AND USERS.USERID='"+userId+"' ";
		if(userName!=null)
			s += " AND USERS.USERNAME LIKE '%"+userName+"%' ";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			Map<String, Object> users = qh.getJSON();
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询指定机构用户信息指定角色
	 * @param org
	 * @return
	 */
	public Map<String,Object> SearchOrgUsers(String org,String Role){
		
		String s = "select a.* from sys_users a left join ADMIN_AUTH_ACCOUNT_ROLE b on  b.ACCOUNT_ID=a.ID where a.UNITID='"+org+"'";
		if(Role!=null&&!"".equals(Role))
			s=s+"  and b.ROLE_ID='"+Role+"'";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			Map<String, Object> users = qh.getJSON();
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询下属机构用户
	 * @param org
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String,Object> SearchSubOrgUsers(String orgId,boolean type){
		Map<String, Object> subOrgs = new HashMap<String,Object>();
		try {
			subOrgs = searchSubOrgs(orgId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		List orgList = (List) subOrgs.get("data");
		String s = "SELECT USERS.* FROM SYS_USERS USERS WHERE USERS.UNITID in (";
		if(type){
			s += "'"+orgId+"'";
		}else{
			s += "'-1'";
		}
		if(orgList!=null){
			for(Object m : orgList){
				Map mt = (Map)m;
				s +=",'"+mt.get("UNITID")+"'";
			}
		}
		s += ")";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			Map<String, Object> users = qh.getJSON();
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询下属机构用户指定角色
	 * @param org
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String,Object> SearchSubOrgUsers(String orgId,boolean type,String role){
		Map<String, Object> subOrgs = new HashMap<String,Object>();
		try {
			subOrgs = searchSubOrgs(orgId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		List orgList = (List) subOrgs.get("data");
		String s = "SELECT USERS.* FROM SYS_USERS USERS left join ADMIN_AUTH_ACCOUNT_ROLE b on  b.ACCOUNT_ID=USERS.ID   WHERE USERS.UNITID in (";
		if(type){
			s += "'"+orgId+"'";
		}else{
			s += "'-1'";
		}
		if(orgList!=null){
			for(Object m : orgList){
				Map mt = (Map)m;
				s +=",'"+mt.get("UNITID")+"'";
			}
		}
		s += ")";
		if(role!=null&&!"".equals(role))
			s=s+"  and b.ROLE_ID='"+role+"'";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			Map<String, Object> users = qh.getJSON();
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 通过角色ID，查询角色信息
	 * 
	 * @param org
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getRoleInfo(List authList) {
		if (authList.size() > 0) {
			StringBuffer sql = new StringBuffer(
					"SELECT * FROM admin_auth_role A WHERE A.Id IN (");
			for (int i = 0; i < authList.size(); i++) {
				if (i == 0) {
					sql.append(((GrantedAuthority) authList.get(i)).getAuthority() );
				} else
					sql.append(","+ ((GrantedAuthority) authList.get(i)).getAuthority());
			}
			sql.append(" )");
			QueryHelper qh;
			List roles;
			try {
				qh = new QueryHelper(sql.toString(), dsOracle.getConnection());
				roles = (List) qh.getJSON().get("data");
				if (roles != null && roles.size() > 0) {
					return roles;
				} else
					return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}
	// 客户经理主协办类型查询
	@SuppressWarnings("unchecked")
	public Map<String, Object> isMainType(String mgrId, String custId) {

		String s = "SELECT M.Main_Type FROM ocrm_f_ci_belong_custmgr M WHERE M.Mgr_Id='"
				+ mgrId + "' AND M.Cust_Id='" + custId + "'";
		QueryHelper qh;
		List names;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			names = (List) qh.getJSON().get("data");
			if (names != null && names.size() > 0) {
				Map res = (Map) names.get(0);
				return res;
				// return (String) res.get("MAIN_TYPE");
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 
}
