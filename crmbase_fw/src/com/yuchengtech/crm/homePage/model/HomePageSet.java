package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


/**
 * 
 * 页签配置信息模块
 * @author zkl
 *
 */
@SuppressWarnings("serial")
@Entity
@IdClass(HomePagePK.class)
@Table(name="OCRM_F_CI_HOMEPAGESET")
public class HomePageSet implements Serializable{
	
	/**
	 * 用户 ID
	 */
	@Id
	@Column(name="USER_ID")
	private String userID;
	
	
	/**
	 * 页签ID
	 */
	@Id
	@Column(name="TABS_ID")
	private String tabID;
	
	/**
	 * 
	 * 模块ID
	 */
	@Id
	@Column(name="MODILD_ID")
	private String modID;
	
	/**
	 * 模块列号
	 */
	@Column(name="MODILD_COLUMN")
	private int modCol;
	
	
	/**
	 * 模块行号
	 */
	@Column(name="MODILD_ROWNUM")
	private int modRow;
	
	
	public HomePageSet(){
		
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String getTabID() {
		return tabID;
	}


	public void setTabID(String tabID) {
		this.tabID = tabID;
	}


	public String getModID() {
		return modID;
	}


	public void setModID(String modID) {
		this.modID = modID;
	}


	public int getModCol() {
		return modCol;
	}


	public void setModCol(int modCol) {
		this.modCol = modCol;
	}


	public int getModRow() {
		return modRow;
	}


	public void setModRow(int modRow) {
		this.modRow = modRow;
	}
	
}
