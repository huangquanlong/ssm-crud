package com.yuchengtech.sm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the sm_person_info database table.
 * 
 */
@Entity
@Table(name="sm_person_info")
@NamedQuery(name="SmPersonInfo.findAll", query="SELECT s FROM SmPersonInfo s")
public class SmPersonInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="id")
	private Long id;

	@Column(name="base_info_id")
	private int baseInfoId;

	@Column(name="classify")
	private String classify;

	@Column(name="crt_by")
	private String crtBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="crt_time")
	private Date crtTime;

	@Column(name="examine_name")
	private String examineName;

	@Column(name="identity_no")
	private String identityNo;

	@Column(name="is_obtain")
	private String isObtain;

	@Column(name="name")
	private String name;

	@Column(name="person_type")
	private String personType;

	@Column(name="phone")
	private String phone;

	@Column(name="state")
	private String state;

	@Column(name="str1")
	private String str1;

	@Column(name="str2")
	private String str2;

	@Column(name="str3")
	private String str3;

	@Column(name="upd_by")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upd_time")
	private Date updTime;

	public SmPersonInfo() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getBaseInfoId() {
		return this.baseInfoId;
	}

	public void setBaseInfoId(int baseInfoId) {
		this.baseInfoId = baseInfoId;
	}

	public String getClassify() {
		return this.classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public String getCrtBy() {
		return this.crtBy;
	}

	public void setCrtBy(String crtBy) {
		this.crtBy = crtBy;
	}

	public Date getCrtTime() {
		return this.crtTime;
	}

	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	public String getExamineName() {
		return this.examineName;
	}

	public void setExamineName(String examineName) {
		this.examineName = examineName;
	}

	public String getIdentityNo() {
		return this.identityNo;
	}

	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}

	public String getIsObtain() {
		return this.isObtain;
	}

	public void setIsObtain(String isObtain) {
		this.isObtain = isObtain;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonType() {
		return this.personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStr1() {
		return this.str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return this.str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public String getStr3() {
		return this.str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3;
	}

	public String getUpdBy() {
		return this.updBy;
	}

	public void setUpdBy(String updBy) {
		this.updBy = updBy;
	}

	public Date getUpdTime() {
		return this.updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

}