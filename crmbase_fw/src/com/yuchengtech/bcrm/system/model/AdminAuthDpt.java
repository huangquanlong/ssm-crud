package com.yuchengtech.bcrm.system.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the ADMIN_AUTH_DPT database table.
 * 
 */
@Entity
@Table(name="ADMIN_AUTH_DPT")
public class AdminAuthDpt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="APP_ID")
	private String appId;

	@Column(name="BELONG_ORG_ID")
	private String belongOrgId;

	@Column(name="INCLUDE_ORG_IDS")
	private String includeOrgIds;

	@Column(name="DPT_ID")
	private String dptId;

	@Column(name="DPT_PARENT_ID")
	private String dptParentId;

	@Column(name="DPT_NAME")
	private String dptName;

	@Column(name="DPT_TYPE")
	private String dptType;

	private String remark;

    public AdminAuthDpt() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBelongOrgId() {
		return this.belongOrgId;
	}

	public void setBelongOrgId(String belongOrgId) {
		this.belongOrgId = belongOrgId;
	}

	public String getDptId() {
		return this.dptId;
	}

	public void setDptId(String dptId) {
		this.dptId = dptId;
	}

	public String getDptName() {
		return this.dptName;
	}

	public void setDptName(String dptName) {
		this.dptName = dptName;
	}

	public String getDptType() {
		return this.dptType;
	}

	public void setDptType(String dptType) {
		this.dptType = dptType;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setIncludeOrgIds(String includeOrgIds) {
		this.includeOrgIds = includeOrgIds;
	}

	public String getIncludeOrgIds() {
		return includeOrgIds;
	}

	public void setDptParentId(String dptParentId) {
		this.dptParentId = dptParentId;
	}

	public String getDptParentId() {
		return dptParentId;
	}

}