package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.yuchengtech.crm.constance.SystemConstance;

/**
 * 机构树model
 * @author lixb
 * @since 2012-9-23
 */
@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_AUTH_ORG")
public class AdminAuthOrg implements Serializable {

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="ACCOUNT_ID")
	private Long accountId;

	@Column(name="APP_ID")
	private String appId=SystemConstance.LOGIC_SYSTEM_APP_ID;

	@Column(name="ORG_ID")
	private String orgId;

	@Column(name="ORG_LEVEL")
	private String orgLevel;

	@Column(name="ORG_NAME")
	private String orgName;

	@Column(name="UP_ORG_ID")
	private String upOrgId;
	
	@Column(name="ORG_ADDR")
	private String orgAddr;
	
	@Column(name="POST_CODE")
	private String postCode;
	
	@Temporal( TemporalType.DATE)
	@Column(name="LAUNCH_DATE")
	private Date launchDate;

    public AdminAuthOrg() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUpOrgId() {
		return this.upOrgId;
	}

	public void setUpOrgId(String upOrgId) {
		this.upOrgId = upOrgId;
	}

	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Date getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}

}