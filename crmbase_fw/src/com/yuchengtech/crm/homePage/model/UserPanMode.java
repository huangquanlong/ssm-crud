package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@IdClass(UserPanelPK.class)
@Table(name="OCRM_F_CI_USERTABPNEL")
public class UserPanMode implements Serializable {
		
	//页签ID
	@Id
	@Column(name = "TABS_ID" )
	private String tabID;

	//用户 ID
	@Id
	@Column(name = "USER_ID")
	private String userID;

	//顺序
	@Column(name = "TABS_NUM")
	private int tabNum;

	
	//样式表
	@Column(name = "LAYOUT_ID")
	private String layID;

	

	public UserPanMode() {

	}
	
	public UserPanMode(String tabID ,String userID) {
		super();
		this.tabID = tabID;
		this.userID = userID;

	}
	

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

	public int getTabNum() {
		return tabNum;
	}

	public void setTabNum(int tabNum) {
		this.tabNum = tabNum;
	}

	public String getLayID() {
		return layID;
	}

	public void setLayID(String layID) {
		this.layID = layID;
	}
}
