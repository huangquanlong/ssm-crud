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

/**
 * The persistent class for the ADMIN_AUTH_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="ADMIN_AUTH_ACCOUNT")
public class AdminAuthAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;

	@Column(name="ACCOUNT_ID")
	private Long accountId;

	@Column(name="ACCOUNT_NAME")
	private String accountName;

	@Column(name="APP_ID")
	private String appId;

    @Temporal( TemporalType.DATE)
	private Date birthday;

    @Temporal( TemporalType.DATE)
	private Date deadline;

	@Column(name="DIR_ID")
	private String dirId;

	@Column(name="EMAIL")
	private String email;

	@Column(name="MOBILEPHONE")
	private String mobilephone;

	private String officetel;

	@Column(name="ORG_ID")
	private String orgId;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="SEX")
	private String sex;

	@Column(name="TEMP1")
	private String temp1;

	@Column(name="TEMP2")
	private String temp2;

	@Column(name="USER_CODE")
	private String userCode;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="USER_STATE")
	private String userState;
	
	@Column(name="OFFENIP")
	private String offenip;

	@Column(name="LASTLOGINTIME")
	private String lastLoginTime;
	
	@Column(name="EDUCATION")
	private String education;
	
	@Column(name="CERTIFICATE")
	private String certificate;
	
	@Column(name="ENTRANTS_DATE")
	private String entrantsDate;
	
	@Column(name="POSITION_TIME")
	private String positionTime;
	
	@Column(name="POSITION_DEGREE")
	private String positionDegree;
	
	@Column(name="BELONG_BUSI_LINE")
	private String belongBusiLine;
	
	@Column(name="BELONG_TEAM_HEAD")
	private String belongTeamHead;
	
	@Column(name="FINANCIAL_JOB_TIME")
	private String financialJobTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getDirId() {
		return dirId;
	}

	public void setDirId(String dirId) {
		this.dirId = dirId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getOfficetel() {
		return officetel;
	}

	public void setOfficetel(String officetel) {
		this.officetel = officetel;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password =password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}
	
	public String getOffenip() {
		return offenip;
	}

	public void setOffenip(String offenip) {
		this.offenip = offenip;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}