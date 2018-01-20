package com.yuchengtech.bob.vo;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;
import com.yuchengtech.crm.sec.vo.CredentialInfo;

public interface AuthUser extends UserDetails {
	
	public static int METHOD_SELECT = 0;
	public static int METHOD_UPDATE = 1;
	public static int METHOD_DELETE = 2;
	public static int METHOD_INSERT = 3;

	public abstract String getPid();

	public abstract void setPid(String pid);

	public abstract String getMenuid();

	public abstract void setMenuid(String menuid);

	public abstract String getUnituid();

	public abstract void setUnituid(String unituid);

	public abstract String getUnitlevel();

	public abstract void setUnitlevel(String unitlevel);

	public abstract String getUsertteam();

	public abstract void setUsertteam(String usertteam);

	public abstract String getUsername();

	public abstract void setUsername(String username);

	public abstract String getCname();

	public abstract void setCname(String cname);

	public abstract String getPassword();

	public abstract void setPassword(String password);

	public abstract String getEmail();

	public abstract void setEmail(String email);

	public abstract String getPhone();

	public abstract void setPhone(String phone);

	public abstract String getUnitName();

	public abstract void setUnitName(String unitName);

	public abstract boolean isAccountNonExpired();

	public abstract void setAccountNonExpired(boolean accountNonExpired);

	public abstract boolean isAccountNonLocked();

	public abstract void setAccountNonLocked(boolean accountNonLocked);

	public abstract boolean isCredentialsNonExpired();

	public abstract void setCredentialsNonExpired(boolean credentialsNonExpired);

	public abstract boolean isEnabled();

	public abstract void setEnable(boolean enable);

	public abstract String getUserId();

	public abstract void setUserId(String userId);

	public abstract String getUnitId();

	public abstract void setUnitId(String unitId);

	public abstract String getGroupId();

	public abstract void setGroupId(String groupId);

	public abstract String getGroupName();

	public abstract void setGroupName(String groupName);
	
	public abstract String getAuthUnits();

	public abstract void setAuthUnits(String authUnits);
	
	public abstract List<GrantedAuthority> getAuthorities();
	
	public abstract void setAuthorities(List<GrantedAuthority> authorities);
	
	@SuppressWarnings("unchecked")
	public abstract void setGrant(Map grant);
	
	@SuppressWarnings("unchecked")
	public abstract Map getGrant();
	
	public abstract List<String> findGrantByRes(String resId);

	public abstract void setAuthOrgList(Map<String,Object> orgData);

	@SuppressWarnings("unchecked")
	public abstract List getUpOrgList();
	
	public abstract void setUpOrgList(Map<String, Object> orgData);

	@SuppressWarnings("unchecked")
	public abstract List getSubOrgList();
	
	public abstract void setSubOrgList(Map<String, Object> orgData);

	@SuppressWarnings("unchecked")
	public abstract List getPathOrgList();
	
	public abstract void setPathOrgList(Map<String, Object> orgData);
	
	@SuppressWarnings("unchecked")
	public abstract List getAuthOrgList1();
	
	@SuppressWarnings("unchecked")
	public abstract List getDataAuthInfo();
	
	public abstract List<AuthSysFilterAuth> getAuthInfos();
	
	@SuppressWarnings("unchecked")
	public abstract void setDataAuthInfo(List authInfo);
	
	public abstract String getFilterString(String className, int methodName);
	
	@SuppressWarnings("unchecked")
	public abstract void setAuthInfos(List authInfos);
	
	public abstract void putAttribute(String key, Object value);
	
	public abstract Object getAttribute(String key);
	
	@SuppressWarnings("unchecked")
	public abstract Map getCRMNavigationMap();
	
	@SuppressWarnings("unchecked")
	public abstract void setCRMNavigationMap(Map navigationMap);
	
	public abstract String[] getCurrentMenuPosition(String currentPageUrl);
	
	@SuppressWarnings("unchecked")
	public abstract List getRolesInfo() ;

	@SuppressWarnings("unchecked")
	public abstract void setRolesInfo(List roleInfo);
	
	public abstract String getCurrentIP();

	public abstract void setCurrentIP(String CurrentIP);
	
	public abstract String getLastLoginTime();

	public abstract void setLastLoginTime(String LastLoginTime);
	
	public abstract CredentialInfo getCredentialInfo();

	public abstract void setCredentialInfo(CredentialInfo CredentialInfo);

	public abstract String getOffenIP();

	public abstract void setOffenIP(String offenIP);
	
	public abstract String getLoginType();
	
	public abstract void setLoginType(String loginType);
	
	public abstract String getDptId();
	
	public abstract void setDptId(String dptId);
	
	public abstract String getTemp1();
	
	public abstract void setTemp1(String temp1);
	
	public abstract String getTemp2();
	
	public abstract void setTemp2(String temp2);
	
	public abstract String getUserCode();
	
	public abstract void setUserCode(String userCode);
	
	public abstract String getBelongBusiLine();
	
	public abstract void setBelongBusiLine(String belongBusiLine);
	
	public abstract String getBelongTeamHead();
	
	public abstract void setBelongTeamHead(String belongTeamHead);

	public abstract Map getUnitInfo();

	public abstract void setUnitInfo(Map unitInfo) ;
	
	public abstract String getCuserSql();
	
}