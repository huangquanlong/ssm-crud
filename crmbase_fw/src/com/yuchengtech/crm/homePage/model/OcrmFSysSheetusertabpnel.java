package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="OCRM_F_SYS_SHEETUSERTABPNEL")
public class OcrmFSysSheetusertabpnel implements Serializable {
	
	@Id
	@SequenceGenerator(name="OcrmFSysSheetusertabpnel_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;

	@Column(name="APP_ID")
	private String appId;
	
	@Column(name="USER_ID")
	private String userId;

	@Column(name="TAB_NAME")
	private String tabName;

	@Column(name="TEMP1")
	private String temp1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	
}
