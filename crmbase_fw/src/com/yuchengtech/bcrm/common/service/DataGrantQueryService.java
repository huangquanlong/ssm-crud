package com.yuchengtech.bcrm.common.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.core.QueryHelper;


@Service
@Transactional
public class DataGrantQueryService {
	
	@Autowired
    @Qualifier("dsOracle")
    private DataSource dsOracle;
	
	/**
	 * 查询子机构树
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> searchSubOrgTree(){
		
//        String seq = orgSeq(orgId);
//		String s = "SELECT UNIT.* FROM (select t1.FILTER_MAP_NAME, t.CLASS_NAME,t.ID,t1.SUPER_ID,t.DESCRIBETION,t.METHOD_NAME,t.ROLE_ID,t.SQL_STRING  from AUTH_SYS_FILTER_MAP t1 left join AUTH_SYS_FILTER t on t.ID=t1.ID) UNIT";
	String  s = "select unit.ID,unit.APP_ID,unit.CRT_DATE,unit.ICON,unit.ISFIXED,unit.NAME,unit.PARENT_ID from CNT_MENU unit LEFT JOIN FW_FUNCTION FW ON unit.MOD_FUNC_ID = FW.ID ";
		QueryHelper qh;
		try {
			qh = new QueryHelper(s, dsOracle.getConnection());
			return qh.getJSON();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 查询直接子机构
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> searchSubOrgs(String orgId){
        
        StringBuffer sb = new StringBuffer("SELECT UNIT.ID,UNIT.APP_ID,UNIT.UNITID,UNIT.UNITNAME,UNIT.SUPERUNITID,UNIT.LEVELUNIT " +
        "FROM SYS_UNITS UNIT WHERE UNIT.SUPERUNITID='" +orgId+"'");
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
        
        StringBuffer sb = new StringBuffer("SELECT GRANT.GRANT_ID,GRANT.GRANT_NAME,GRANT.LEVEL,GRANT.SUPER_ID FROM TEST_FOR_GRANT GRANT");
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
	public Map<String,Object> SearchOrgUsers(String org){
		String s = "SELECT USERS.* FROM SYS_USERS USERS WHERE USERS.UNITID='"+org+"'";
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
}
