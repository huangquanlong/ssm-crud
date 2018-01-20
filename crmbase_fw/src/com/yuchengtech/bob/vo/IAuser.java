package com.yuchengtech.bob.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.yuchengtech.crm.dataauth.model.AuthSysFilter;
import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;
import com.yuchengtech.crm.sec.vo.CredentialInfo;

public class IAuser implements AuthUser {

	private static final long serialVersionUID = 1L;
	
	// 用户userid
	private String userId;
	// 用户登陆ID
	private String username;
	// 用户中文名称
	private String cname;
	// 用户密码
	private String password;
	// 用户电子邮件
	private String email;
	// 用户电话
	private String phone;
	// 用户机构ID
	private String unitId;
	// 用户机构名称
	private String unitName;
	//用户机构信息
	private Map unitInfo;
	// 用户组ID
	private String groupId;
	// 用户组名称
	private String groupName;
	// 用户营销团队
	private String usertteam;
	// 机构ID
	private String unituid;
	// 机构层级
	private String unitlevel;
	//授权机构
	private String authUnits;
	// 菜单
	private String menuid;
	//最近一次操作数据主键
	private String pid;
	//角色列表
	private List<GrantedAuthority> authorities;
	//角色详细信息列表
	private List rolesinfo;
	//数据权限信息，包括所有对于数据权限的授权。
	private List<AuthSysFilter> dataAuthInfo = new ArrayList();
	//数据权限授权信息对象列表，包括用户已被授权的所有信息
	private List<AuthSysFilterAuth> authInfos;
	//菜单及功能点授权信息
	private Map grant;
	//授权机构列表
	private List authOrgList;
	//直接上级机构
	private List upOrgList;
	//直接子机构列表
	private List subOrgList;
	//机构树路径列表
	private List pathOrgList;
	//
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired = true;
	
	private boolean enable = true;
	//角色列表，调用GrantedAuthority的toString方法可获取角色ID。
	//private Collection<GrantedAuthority> authorities;
	//sessionMap。用户动态设置session级别信息，如：导入、导出线程句柄。
	private Map<String,Object> ctxSession = new HashMap<String, Object>();
	
	private Map crmnavigationMap;
	//用户当前ip地址
	private String currentIP;
	//用户最近登录时间
	private String lastLoginTime;
	//认证策略信息
	private CredentialInfo credentialInfo;
	//常用IP
	private String offenIP;
	//登录类型（现用于是否单角色登录）
	private String loginType;
	
	//部门ID
	private String dptId;
	
	//头像路径
	private String temp1;
	
	//密码修改标志
	private String temp2;
	
	//员工号
	private String userCode;
	
	//归属业务条线
	private String belongBusiLine;
	
	//归属teamhead
	private String belongTeamHead;
	
	public List getUpOrgList() {
		return upOrgList;
	}
	
	public void setUpOrgList(Map<String, Object> orgData) {
		if(orgData!=null){
			this.upOrgList = (List)orgData.get("data");
		}
	}
	
	public List getSubOrgList() {
		return subOrgList;
	}
	
	public void setSubOrgList(Map<String, Object> orgData) {
		if(orgData!=null){
			this.subOrgList = (List)orgData.get("data");
		}
	}
	
	public List getPathOrgList() {
		return pathOrgList;
	}
	
	public void setPathOrgList(Map<String, Object> orgData) {
		if(orgData!=null){
			this.pathOrgList = (List)orgData.get("data");
		}
	}

	public Map getCRMNavigationMap() {
		return crmnavigationMap;
	}
	
	public void setCRMNavigationMap(Map navigationMap) {
		this.crmnavigationMap=navigationMap;
	}
	
	/**
	 * 返回数组预留将来放导航条
	 */
	public String[] getCurrentMenuPosition(String currentPageUrl) {
		Map map= this.getCRMNavigationMap();
		String uri=currentPageUrl.replace(".jsp", ".jsf");
		if(uri.contains("?")){
			uri=uri.substring(0,uri.indexOf("?"));
		}
		Long id = (Long)map.get(uri);
		String[] arr=new String[]{""};
		if(id!=null)
			arr=new String[]{String.valueOf(id)};
		return arr;
	}
	
	public List getAuthOrgList1(){
		return this.authOrgList;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getUnituid() {
		return unituid;
	}

	public void setUnituid(String unituid) {
		this.unituid = unituid;
	}

	public String getUnitlevel() {
		return unitlevel;
	}

	public void setUnitlevel(String unitlevel) {
		this.unitlevel = unitlevel;
	}

	public String getUsertteam() {
		return usertteam;
	}

	public void setUsertteam(String usertteam) {
		this.usertteam = usertteam;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return this.enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAuthUnits() {
		return authUnits;
	}

	public void setAuthUnits(String authUnits) {
		this.authUnits = authUnits;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public List<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public void setGrant(Map grant) {
		this.grant = grant;
	}

	public Map getGrant() {
		return grant;
	}

	public List<String> findGrantByRes(String resId) {
		return (List<String>) grant.get(resId);
	}

	public void setAuthOrgList(Map<String, Object> orgData) {
		if(orgData!=null){
			this.authOrgList = (List)orgData.get("data");
		}
	}

	public List getDataAuthInfo() {
		return dataAuthInfo;
	}

	/**
	 * 获取该用户在该功能点上的最终SQL\JQL的过滤条件
	 */
	public String getFilterString(String className, int methodName) {
		if(ifAll(className)){
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for(AuthSysFilter authFilter:dataAuthInfo){
			if(null!=className&&className.equals(authFilter.getClassName())&&methodName==authFilter.getMethodName())
			{
				if(null!=authFilter.getSqlString()){
					String tmpSql = "( "+authFilter.getSqlString()+" )";
					if(stringBuffer.length()!=0){
						stringBuffer.append(" OR ");
					}
					stringBuffer.append(tmpSql);
				}
			}
		}
		
		if(stringBuffer.length()==0){
			return "";
		}
		try {
			return "( "+DataAuthType.processParameter(this,stringBuffer.toString())+" )";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private boolean ifAll(String className){
		boolean result = false;
		for(GrantedAuthority ga : authorities){
			if(!getFilterByRole(ga.getAuthority(), className)){
				return true;
			}
		}
		return result;
	}
	
	private boolean getFilterByRole(String role, String className ){
		boolean result = false;
		for(AuthSysFilterAuth authTmp : authInfos){
			if(authTmp.getRoleId().equals(role))
				for(AuthSysFilter filterTmp: dataAuthInfo){
					if(filterTmp.getClassName().equals(className))
						return true;
				}
		}
		return result;
	}

	public void setDataAuthInfo(List authInfo) {
		dataAuthInfo = authInfo;
	}

	public void setAuthInfos(List authInfos) {
		this.authInfos = authInfos;
	}
	
	public List<AuthSysFilterAuth> getAuthInfos() {
		return authInfos;
	}
	
	public void putAttribute(String key, Object value){
		ctxSession.put(key, value);
	}
	
	public Object getAttribute(String key){
		return ctxSession.get(key);
	}
	
	public Object removeAttribute(String key){
		return ctxSession.remove(key);
	}
	
	public void clearAttribute(){
		ctxSession.clear();
	}
	
	public List getRolesInfo() {
		return rolesinfo;
	}

	public void setRolesInfo(List roleInfo) {
		this.rolesinfo=roleInfo;
	}

	public String getCurrentIP() {
		return currentIP;
	}

	public void setCurrentIP(String CurrentIP) {
		currentIP = CurrentIP;
	}
	
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String LastLoginTime) {
		lastLoginTime = LastLoginTime;
	}

	public CredentialInfo getCredentialInfo() {
		return credentialInfo;
	}

	public void setCredentialInfo(CredentialInfo CredentialInfo) {
		credentialInfo = CredentialInfo;		
	}

	public String getOffenIP() {
		return offenIP;
	}

	public void setOffenIP(String OffenIP) {
		offenIP = OffenIP;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getDptId() {
		return this.dptId;
	}

	public void setDptId(String dptId) {
		this.dptId = dptId;		
	}
	
	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	
	public String getTemp2() {
		// TODO Auto-generated method stub
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getBelongBusiLine() {
		return belongBusiLine;
	}

	public void setBelongBusiLine(String belongBusiLine) {
		this.belongBusiLine = belongBusiLine;
	}

	public String getBelongTeamHead() {
		return belongTeamHead;
	}

	public void setBelongTeamHead(String belongTeamHead) {
		this.belongTeamHead = belongTeamHead;
	}

	public Map getUnitInfo() {
		return unitInfo;
	}

	public void setUnitInfo(Map unitInfo) {
		this.unitInfo = unitInfo;
	}

	/**
	 * 辖内所属条线用户sql
	 */
	public String getCuserSql() {
		if(this.getUnitInfo() == null){
			return "";
		}
		String cuserSql = "SELECT A.ACCOUNT_NAME FROM ADMIN_AUTH_ACCOUNT A WHERE 1=1 ";
		if(this.getBelongBusiLine() != null && !"".equals(this.getBelongBusiLine()) && !"0".equals(this.getBelongBusiLine())){
			cuserSql += " AND A.BELONG_BUSI_LINE = '"+this.getBelongBusiLine()+"'";
		}
		cuserSql += " AND A.ORG_ID IN (SELECT U.UNITID FROM SYS_UNITS U WHERE U.UNITSEQ LIKE '"+this.getUnitInfo().get("UNITSEQ")+"%' )";
		return cuserSql;
	}



	
}