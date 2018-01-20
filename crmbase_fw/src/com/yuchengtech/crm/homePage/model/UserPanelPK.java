package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

/**
 * UserPabelMode 联合主键类
 * @author lenovo
 *
 */
@SuppressWarnings("serial")
public class UserPanelPK implements Serializable {

	//页签ID
	private String tabID;

	//用户 ID
	private String userID;

	public String getTabID() {
		return tabID;
	}

	public void setTabID(String tabID) {
		this.tabID = tabID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
