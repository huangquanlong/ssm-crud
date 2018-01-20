package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="OCRM_F_CI_TABPNEL")
public class TabPanel implements Serializable{
	
	//页签ID
	@Id
	@Column(name="TABS_ID")
	private String tabID;
	
	//页签名字
	@Column(name="TABS_NAME")
	private String tabName;
	
	public TabPanel(){
		
	}

	public String getTabID() {
		return tabID;
	}

	public void setTabID(String tabID) {
		this.tabID = tabID;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

}
