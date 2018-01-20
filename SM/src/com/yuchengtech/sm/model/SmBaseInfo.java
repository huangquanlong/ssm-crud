package com.yuchengtech.sm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the sm_base_info database table.
 * 
 */
@Entity
@Table(name="sm_base_info")
@NamedQuery(name="SmBaseInfo.findAll", query="SELECT s FROM SmBaseInfo s")
public class SmBaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="id")
	private Long id;

	@Column(name="atta_file")
	private Integer attaFile;

	@Column(name="bank_name")
	private String bankName;

	@Column(name="checked_by")
	private String checkedBy;

	@Column(name="checked_state")
	private String checkedState;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="checked_time")
	private Date checkedTime;

	@Column(name="clientele_mobile")
	private String clienteleMobile;

	@Column(name="clientele_name")
	private String clienteleName;

	@Column(name="company_name")
	private String companyName;

	@Column(name="contacts_mobile")
	private String contactsMobile;

	@Column(name="contacts_name")
	private String contactsName;

	@Column(name="crt_by")
	private String crtBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="crt_time")
	private Date crtTime;

	@Column(name="custom_id")
	private String customId;

	@Column(name="ec_desc")
	private String ecDesc;

	@Column(name="ec_type")
	private String ecType;

	@Column(name="email")
	private String email;

	@Column(name="file_archive_area")
	private String fileArchiveArea;

	@Column(name="fund_dimensions")
	private String fundDimensions;

	@Column(name="fund_manager_name")
	private String fundManagerName;

	@Column(name="fund_reg_no")
	private String fundRegNo;

	@Column(name="fund_type")
	private String fundType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="input_time")
	private Date inputTime;

	@Column(name="is_join")
	private String isJoin;

	@Column(name="is_latest")
	private String isLatest;

	@Column(name="is_pivot")
	private String isPivot;

	@Column(name="letter_con")
	private String letterCon;

	@Column(name="letter_no")
	private String letterNo;

	@Column(name="org_name")
	private String orgName;

	@Column(name="partner_num")
	private Integer partnerNum;

	@Column(name="partner_type")
	private String partnerType;

	@Column(name="person_id_no")
	private String personIdNo;

	@Column(name="person_mobile")
	private String personMobile;

	@Column(name="person_name")
	private String personName;

	@Column(name="person_phone")
	private String personPhone;

	@Column(name="person_type")
	private String personType;

	@Column(name="reg_money")
	private BigDecimal regMoney;

	@Column(name="rules_file")
	private int rulesFile;

	@Column(name="second_name")
	private String secondName;

	@Column(name="state")
	private String state;

	@Column(name="type_identify")
	private String typeIdentify;

	@Column(name="upd_by")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upd_time")
	private Date updTime;

	@Column(name="ver")
	private String ver;

	@Column(name="work_address")
	private String workAddress;

	public SmBaseInfo() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAttaFile() {
		return this.attaFile;
	}

	public void setAttaFile(Integer attaFile) {
		this.attaFile = attaFile;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCheckedBy() {
		return this.checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	public String getCheckedState() {
		return this.checkedState;
	}

	public void setCheckedState(String checkedState) {
		this.checkedState = checkedState;
	}

	public Date getCheckedTime() {
		return this.checkedTime;
	}

	public void setCheckedTime(Date checkedTime) {
		this.checkedTime = checkedTime;
	}

	public String getClienteleMobile() {
		return this.clienteleMobile;
	}

	public void setClienteleMobile(String clienteleMobile) {
		this.clienteleMobile = clienteleMobile;
	}

	public String getClienteleName() {
		return this.clienteleName;
	}

	public void setClienteleName(String clienteleName) {
		this.clienteleName = clienteleName;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactsMobile() {
		return this.contactsMobile;
	}

	public void setContactsMobile(String contactsMobile) {
		this.contactsMobile = contactsMobile;
	}

	public String getContactsName() {
		return this.contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
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

	public String getCustomId() {
		return this.customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getEcDesc() {
		return this.ecDesc;
	}

	public void setEcDesc(String ecDesc) {
		this.ecDesc = ecDesc;
	}

	public String getEcType() {
		return this.ecType;
	}

	public void setEcType(String ecType) {
		this.ecType = ecType;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFileArchiveArea() {
		return this.fileArchiveArea;
	}

	public void setFileArchiveArea(String fileArchiveArea) {
		this.fileArchiveArea = fileArchiveArea;
	}

	public String getFundDimensions() {
		return this.fundDimensions;
	}

	public void setFundDimensions(String fundDimensions) {
		this.fundDimensions = fundDimensions;
	}

	public String getFundManagerName() {
		return this.fundManagerName;
	}

	public void setFundManagerName(String fundManagerName) {
		this.fundManagerName = fundManagerName;
	}

	public String getFundRegNo() {
		return this.fundRegNo;
	}

	public void setFundRegNo(String fundRegNo) {
		this.fundRegNo = fundRegNo;
	}

	public String getFundType() {
		return this.fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public Date getInputTime() {
		return this.inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public String getIsJoin() {
		return this.isJoin;
	}

	public void setIsJoin(String isJoin) {
		this.isJoin = isJoin;
	}

	public String getIsLatest() {
		return this.isLatest;
	}

	public void setIsLatest(String isLatest) {
		this.isLatest = isLatest;
	}

	public String getIsPivot() {
		return this.isPivot;
	}

	public void setIsPivot(String isPivot) {
		this.isPivot = isPivot;
	}

	public String getLetterCon() {
		return this.letterCon;
	}

	public void setLetterCon(String letterCon) {
		this.letterCon = letterCon;
	}

	public String getLetterNo() {
		return this.letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getPartnerNum() {
		return this.partnerNum;
	}

	public void setPartnerNum(Integer partnerNum) {
		this.partnerNum = partnerNum;
	}

	public String getPartnerType() {
		return this.partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getPersonIdNo() {
		return this.personIdNo;
	}

	public void setPersonIdNo(String personIdNo) {
		this.personIdNo = personIdNo;
	}

	public String getPersonMobile() {
		return this.personMobile;
	}

	public void setPersonMobile(String personMobile) {
		this.personMobile = personMobile;
	}

	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonPhone() {
		return this.personPhone;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}

	public String getPersonType() {
		return this.personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public BigDecimal getRegMoney() {
		return this.regMoney;
	}

	public void setRegMoney(BigDecimal regMoney) {
		this.regMoney = regMoney;
	}

	public int getRulesFile() {
		return this.rulesFile;
	}

	public void setRulesFile(int rulesFile) {
		this.rulesFile = rulesFile;
	}

	public String getSecondName() {
		return this.secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTypeIdentify() {
		return this.typeIdentify;
	}

	public void setTypeIdentify(String typeIdentify) {
		this.typeIdentify = typeIdentify;
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

	public String getVer() {
		return this.ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getWorkAddress() {
		return this.workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

}