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
 * The persistent class for the OCRM_F_WP_REMIND_INFO database table.
 * 
 * FDM层(基础数据层)，归属于工作平台的第二主题，数据为自动提醒信息
 */
@Entity
@Table(name = "OCRM_F_WP_REMIND_INFO")
public class WorkingplatformRemindInfo implements Serializable {

	private static final long serialVersionUID = 5286266842517278719L;

	/** 信息id */
	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "REMIND_ID", nullable = false)
	private Long remindId;

	/** 账号 */
	@Column(length = 30)
	private String acount;

    /** user id */
    @Column(name="user_id",length = 30)
    private String userId;

	/** 当日余额 */
	@Column(name = "AMOUNT_TODY", precision = 24, scale = 6)
	private BigDecimal amountTody;

	/** 变动余额 */
	@Column(name = "CHANGE_AMOUNT", precision = 24, scale = 6)
	private BigDecimal changeAmount;

	/** 客户名称 */
	@Column(name = "CUST_NAME", length = 200)
	private String custName;

	/** 消息状态 */
	@Column(name = "IS_READ")
	private Boolean isRead;

	/** 高管姓名 */
	@Column(name = "OFFICER_NAME", length = 200)
	private String officerName;

	/** 组织机构代码 */
	@Column(name = "ORG_CODE", length = 20)
	private String organizerCode;

	/** 详细内容 */
	@Column(name = "REMIND_CONTENT", length = 2000)
	private String remindContent;

	/** 到达时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "REMIND_TIME")
	private Date remindTime;

	/** 信息标题 */
	@Column(name = "REMIND_TITLE", length = 200)
	private String remindTitle;

	/** 消息类别id */
	@Column(name = "REMIND_TYPE", length = 20)
	private String remindType;

	/** 统计日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "STATISTICS_DATE")
	private Date statisticsDate;

	public String getAcount() {
		return this.acount;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setAcount(String acount) {
		this.acount = acount;
	}

	public BigDecimal getAmountTody() {
		return this.amountTody;
	}

	public void setAmountTody(BigDecimal amountTody) {
		this.amountTody = amountTody;
	}

	public BigDecimal getChangeAmount() {
		return this.changeAmount;
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getOfficerName() {
		return this.officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	public String getRemindContent() {
		return this.remindContent;
	}

	public void setRemindContent(String remindContent) {
		this.remindContent = remindContent;
	}

	public Date getRemindTime() {
		return this.remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public String getRemindTitle() {
		return this.remindTitle;
	}

	public void setRemindTitle(String remindTitle) {
		this.remindTitle = remindTitle;
	}

	public String getRemindType() {
		return this.remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public Date getStatisticsDate() {
		return this.statisticsDate;
	}

	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setOrganizerCode(String organizerCode) {
		this.organizerCode = organizerCode;
	}

	public String getOrganizerCode() {
		return organizerCode;
	}

	public void setRemindId(Long remindId) {
		this.remindId = remindId;
	}

	public Long getRemindId() {
		return remindId;
	}

}