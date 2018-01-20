package com.yuchengtech.bob.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the OCRM_F_WP_REMIND database table.
 * 
 */
@Entity
@Table(name="OCRM_F_WP_REMIND")
public class WorkplatRemindList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="ACCT_AMT")
	private BigDecimal acctAmt;

	@Column(name="ACCT_BAL")
	private BigDecimal acctBal;

	@Column(name="ACCT_NAME")
	private String acctName;

	@Column(name="ACCT_NO")
	private String acctNo;

	@Column(name="ACCT_ORGNO")
	private String acctOrgno;

	private String addr;

	@Column(name="BAL_DIR")
	private String balDir;

	private String birthday1;

	private String birthday2;

	@Column(name="CARD_NO")
	private String cardNo;

	private String ccy;

	@Column(name="CREDIT_TYP")
	private String creditTyp;

	@Column(name="CUST_ID")
	private String custId;

	@Column(name="CUST_NAME")
	private String custName;

	@Column(name="CUST_ZZDM")
	private String custZzdm;

    @Temporal( TemporalType.DATE)
	@Column(name="DQ_DATE")
	private Date dqDate;

	@Column(name="EVENT_NAME")
	private String eventName;

	@Column(name="EVENT_TYP")
	private String eventTyp;

    @Temporal( TemporalType.DATE)
	@Column(name="FF_DATE")
	private Date ffDate;

	@Column(name="JK_AMT")
	private BigDecimal jkAmt;

    @Temporal( TemporalType.DATE)
	@Column(name="JY_DATE")
	private Date jyDate;

	@Column(name="JY_TYP")
	private String jyTyp;

    @Temporal( TemporalType.DATE)
	@Column(name="KH_DATE")
	private Date khDate;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_HK_DATE")
	private Date lastHkDate;

	@Column(name="MANAGER_NAME")
	private String managerName;

	@Column(name="MANAGER_PHONE")
	private String managerPhone;

	@Column(name="MSG_CRT_DATE")
	private String msgCrtDate;

	@Column(name="MSG_END_DATE")
	private String msgEndDate;

	@Column(name="MSG_LAST")
	private int msgLast;

	@Column(name="MSG_REMARK")
	private String msgRemark;

	@Column(name="MSG_STS")
	private String msgSts;

	@Column(name="MSG_TYP")
	private String msgTyp;

	private String njbz;

	@Column(name="QF_ORG")
	private String qfOrg;

	private String qfgj;

	@Column(name="QX_AMT")
	private BigDecimal qxAmt;

	@Column(name="READ_DATE")
	private String readDate;

	@Column(name="TZ_AMT")
	private BigDecimal tzAmt;

	@Column(name="USER_NO")
	private String userNo;

	@Column(name="USER_UNITID")
	private String userUnitid;

	private int whqs;

	private BigDecimal yjlx;

    @Temporal( TemporalType.DATE)
	@Column(name="ZJDQ_DATE")
	private Date zjdqDate;

    @Temporal( TemporalType.DATE)
	@Column(name="ZJQF_DATE")
	private Date zjqfDate;

    public WorkplatRemindList() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAcctAmt() {
		return this.acctAmt;
	}

	public void setAcctAmt(BigDecimal acctAmt) {
		this.acctAmt = acctAmt;
	}

	public BigDecimal getAcctBal() {
		return this.acctBal;
	}

	public void setAcctBal(BigDecimal acctBal) {
		this.acctBal = acctBal;
	}

	public String getAcctName() {
		return this.acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public String getAcctNo() {
		return this.acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getAcctOrgno() {
		return this.acctOrgno;
	}

	public void setAcctOrgno(String acctOrgno) {
		this.acctOrgno = acctOrgno;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getBalDir() {
		return this.balDir;
	}

	public void setBalDir(String balDir) {
		this.balDir = balDir;
	}

	public String getBirthday1() {
		return this.birthday1;
	}

	public void setBirthday1(String birthday1) {
		this.birthday1 = birthday1;
	}

	public String getBirthday2() {
		return this.birthday2;
	}

	public void setBirthday2(String birthday2) {
		this.birthday2 = birthday2;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCcy() {
		return this.ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getCreditTyp() {
		return this.creditTyp;
	}

	public void setCreditTyp(String creditTyp) {
		this.creditTyp = creditTyp;
	}

	public String getCustId() {
		return this.custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustZzdm() {
		return this.custZzdm;
	}

	public void setCustZzdm(String custZzdm) {
		this.custZzdm = custZzdm;
	}

	public Date getDqDate() {
		return this.dqDate;
	}

	public void setDqDate(Date dqDate) {
		this.dqDate = dqDate;
	}

	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventTyp() {
		return this.eventTyp;
	}

	public void setEventTyp(String eventTyp) {
		this.eventTyp = eventTyp;
	}

	public Date getFfDate() {
		return this.ffDate;
	}

	public void setFfDate(Date ffDate) {
		this.ffDate = ffDate;
	}

	public BigDecimal getJkAmt() {
		return this.jkAmt;
	}

	public void setJkAmt(BigDecimal jkAmt) {
		this.jkAmt = jkAmt;
	}

	public Date getJyDate() {
		return this.jyDate;
	}

	public void setJyDate(Date jyDate) {
		this.jyDate = jyDate;
	}

	public String getJyTyp() {
		return this.jyTyp;
	}

	public void setJyTyp(String jyTyp) {
		this.jyTyp = jyTyp;
	}

	public Date getKhDate() {
		return this.khDate;
	}

	public void setKhDate(Date khDate) {
		this.khDate = khDate;
	}

	public Date getLastHkDate() {
		return this.lastHkDate;
	}

	public void setLastHkDate(Date lastHkDate) {
		this.lastHkDate = lastHkDate;
	}

	public String getManagerName() {
		return this.managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerPhone() {
		return this.managerPhone;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public String getMsgCrtDate() {
		return this.msgCrtDate;
	}

	public void setMsgCrtDate(String msgCrtDate) {
		this.msgCrtDate = msgCrtDate;
	}

	public String getMsgEndDate() {
		return this.msgEndDate;
	}

	public void setMsgEndDate(String msgEndDate) {
		this.msgEndDate = msgEndDate;
	}

	public int getMsgLast() {
		return this.msgLast;
	}

	public void setMsgLast(int msgLast) {
		this.msgLast = msgLast;
	}

	public String getMsgRemark() {
		return this.msgRemark;
	}

	public void setMsgRemark(String msgRemark) {
		this.msgRemark = msgRemark;
	}

	public String getMsgSts() {
		return this.msgSts;
	}

	public void setMsgSts(String msgSts) {
		this.msgSts = msgSts;
	}

	public String getMsgTyp() {
		return this.msgTyp;
	}

	public void setMsgTyp(String msgTyp) {
		this.msgTyp = msgTyp;
	}

	public String getNjbz() {
		return this.njbz;
	}

	public void setNjbz(String njbz) {
		this.njbz = njbz;
	}

	public String getQfOrg() {
		return this.qfOrg;
	}

	public void setQfOrg(String qfOrg) {
		this.qfOrg = qfOrg;
	}

	public String getQfgj() {
		return this.qfgj;
	}

	public void setQfgj(String qfgj) {
		this.qfgj = qfgj;
	}

	public BigDecimal getQxAmt() {
		return this.qxAmt;
	}

	public void setQxAmt(BigDecimal qxAmt) {
		this.qxAmt = qxAmt;
	}

	public String getReadDate() {
		return this.readDate;
	}

	public void setReadDate(String readDate) {
		this.readDate = readDate;
	}

	public BigDecimal getTzAmt() {
		return this.tzAmt;
	}

	public void setTzAmt(BigDecimal tzAmt) {
		this.tzAmt = tzAmt;
	}

	public String getUserNo() {
		return this.userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserUnitid() {
		return this.userUnitid;
	}

	public void setUserUnitid(String userUnitid) {
		this.userUnitid = userUnitid;
	}

	public int getWhqs() {
		return this.whqs;
	}

	public void setWhqs(int whqs) {
		this.whqs = whqs;
	}

	public BigDecimal getYjlx() {
		return this.yjlx;
	}

	public void setYjlx(BigDecimal yjlx) {
		this.yjlx = yjlx;
	}

	public Date getZjdqDate() {
		return this.zjdqDate;
	}

	public void setZjdqDate(Date zjdqDate) {
		this.zjdqDate = zjdqDate;
	}

	public Date getZjqfDate() {
		return this.zjqfDate;
	}

	public void setZjqfDate(Date zjqfDate) {
		this.zjqfDate = zjqfDate;
	}

}