package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HomePagePK implements Serializable {
	
	private String userID;
	
	private String modID;
	
	private String tabID;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getModID() {
		return modID;
	}

	public void setModID(String modID) {
		this.modID = modID;
	}

	public String getTabID() {
		return tabID;
	}

	public void setTabID(String tabID) {
		this.tabID = tabID;
	}

}
