package com.yuchengtech.bcrm.workplat.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the OCRM_F_WP_REMIND_RULE database table.
 * 
 */
@Entity
@Table(name="OCRM_F_WP_REMIND_RULE")
public class OcrmFWpRemindRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="RULE_ID", unique=true, nullable=false, precision=22)
	private Long ruleId;

	@Column(name="ADJUST_CUST", length=10)
	private String adjustCust;

	@Column(name="BEFORE_DAYS", precision=2)
	private BigDecimal beforeDays;

	@Column(name="COM_CUST_LEVEL", length=10)
	private String comCustLevel;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;

	@Column(name="CREATE_ORG", length=10)
	private String createOrg;

	@Column(name="CREATE_USER", length=25)
	private String createUser;

	@Column(name="CUST_AGE", length=10)
	private String custAge;

	@Column(name="FOLLOW_DEAL", length=2)
	private String followDeal;

	@Column(name="IF_MESSAGE", length=2)
	private String ifMessage;

	@Column(name="INDIV_CUST_LEVEL", length=10)
	private String indivCustLevel;

	@Column(name="LAST_DAYS", precision=2)
	private BigDecimal lastDays;

	@Column(name="MESSAGE_MODEL", length=800)
	private String messageModel;

	@Column(name="REMIND_MODLE", length=800)
	private String remindModle;

	@Column(name="RULE_CODE", length=10)
	private String ruleCode;

	@Column(name="RULE_NAME", length=100)
	private String ruleName;

	@Column(name="RULE_ROLE", length=100)
	private String ruleRole;

	@Column(length=10)
	private String sex;

	@Column(precision=22, scale=2)
	private BigDecimal threshhold;

    @Temporal( TemporalType.DATE)
	@Column(name="UPDATE_DATE")
	private Date updateDate;

	@Column(name="UPDATE_ORG", length=10)
	private String updateOrg;

	@Column(name="UPDATE_USER", length=25)
	private String updateUser;

    public OcrmFWpRemindRule() {
    }

	public Long getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getAdjustCust() {
		return this.adjustCust;
	}

	public void setAdjustCust(String adjustCust) {
		this.adjustCust = adjustCust;
	}

	public BigDecimal getBeforeDays() {
		return this.beforeDays;
	}

	public void setBeforeDays(BigDecimal beforeDays) {
		this.beforeDays = beforeDays;
	}

	public String getComCustLevel() {
		return this.comCustLevel;
	}

	public void setComCustLevel(String comCustLevel) {
		this.comCustLevel = comCustLevel;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateOrg() {
		return this.createOrg;
	}

	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCustAge() {
		return this.custAge;
	}

	public void setCustAge(String custAge) {
		this.custAge = custAge;
	}

	public String getFollowDeal() {
		return this.followDeal;
	}

	public void setFollowDeal(String followDeal) {
		this.followDeal = followDeal;
	}

	public String getIfMessage() {
		return this.ifMessage;
	}

	public void setIfMessage(String ifMessage) {
		this.ifMessage = ifMessage;
	}

	public String getIndivCustLevel() {
		return this.indivCustLevel;
	}

	public void setIndivCustLevel(String indivCustLevel) {
		this.indivCustLevel = indivCustLevel;
	}

	public BigDecimal getLastDays() {
		return this.lastDays;
	}

	public void setLastDays(BigDecimal lastDays) {
		this.lastDays = lastDays;
	}

	public String getMessageModel() {
		return this.messageModel;
	}

	public void setMessageModel(String messageModel) {
		this.messageModel = messageModel;
	}

	public String getRemindModle() {
		return this.remindModle;
	}

	public void setRemindModle(String remindModle) {
		this.remindModle = remindModle;
	}

	public String getRuleCode() {
		return this.ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleRole() {
		return this.ruleRole;
	}

	public void setRuleRole(String ruleRole) {
		this.ruleRole = ruleRole;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BigDecimal getThreshhold() {
		return this.threshhold;
	}

	public void setThreshhold(BigDecimal threshhold) {
		this.threshhold = threshhold;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateOrg() {
		return this.updateOrg;
	}

	public void setUpdateOrg(String updateOrg) {
		this.updateOrg = updateOrg;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

}