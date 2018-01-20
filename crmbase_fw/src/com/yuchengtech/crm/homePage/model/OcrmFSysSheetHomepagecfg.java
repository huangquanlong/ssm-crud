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
@Table(name="OCRM_F_SYS_SHEETHOMEPAGECFG")
public class OcrmFSysSheetHomepagecfg implements Serializable {
	
	@Id
	@SequenceGenerator(name="OcrmFSysSheetHomepagecfg_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;

	@Column(name="APP_ID")
	private String appId;
	
	@Column(name="USER_ID")
	private String userId;

	@Column(name="TAB_ID")
	private String tabId;

	@Column(name="MODEL_ID")
	private String modelId;

	@Column(name="MODEL_COLUMN")
	private String modelColumn;

	@Column(name="MODEL_ROWNUM")
	private String modelRownum;

	@Column(name="TEMP1")
	private String temp1;

	@Column(name="TEMP2")
	private String temp2;

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

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String tabId) {
		this.tabId = tabId;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelColumn() {
		return modelColumn;
	}

	public void setModelColumn(String modelColumn) {
		this.modelColumn = modelColumn;
	}

	public String getModelRownum() {
		return modelRownum;
	}

	public void setModelRownum(String modelRownum) {
		this.modelRownum = modelRownum;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	
}
