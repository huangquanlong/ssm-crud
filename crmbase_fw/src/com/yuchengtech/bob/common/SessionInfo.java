package com.yuchengtech.bob.common;

import java.util.Date;

/**
 * <pre>
 * Title: SessionInfo
 * Description: SessionInfo
 * 为了提供用户方便提取信息，把一些信息存入session中
 * 
 * </pre>
 * 
 * @version 1.00.00
 * 
 */

public class SessionInfo{

	//用户登录号
	private String loginId;

	//用户ID
	private String userId;

	//用户名称
	private String userName;

    //登录时间
	private Date loginTime;
	
	//用户所属机构ID
	private String orgId;
	
	//用户所属机构名称
	private String orgName;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}