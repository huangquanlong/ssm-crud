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
 * The persistent class for the OCRM_F_WP_REMIND_MSG database table.
 * 
 */
@Entity
@Table(name="OCRM_F_WP_REMIND_MSG")
public class OcrmFWpRemindMsg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="PROD_ID", length=20)
	private String prodId;
	
	@Column(name="PROD_NAME", length=20)
	private String prodName;
	
	@Column(name="MODEL_ID", length=20)
	private String modelId;
	
	@Column(name="CELL_NUMBER", length=20)
	private String cellNumber;

    @Temporal( TemporalType.DATE)
	@Column(name="CTR_DATE")
	private Date ctrDate;

	@Column(name="CUST_ID", length=50)
	private String custId;

	@Column(name="CUST_NAME", length=100)
	private String custName;

	@Column(name="IF_SEND", length=2)
	private String ifSend;

	@Column(name="MESSAGE_REMARK", length=800)
	private String messageRemark;

	@Column(name="REMIND_ID", precision=22)
	private BigDecimal remindId;

    @Temporal( TemporalType.DATE)
	@Column(name="SEND_DATE")
	private Date sendDate;

    @Column(name="USER_ID", length=50)
	private String userId;
	
//	@Column(name="FR_ID", length=50)
//	private String frId;
    
    public OcrmFWpRemindMsg() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

//	public String getFrId() {
//		return frId;
//	}
//
//	public void setFrId(String frId) {
//		this.frId = frId;
//	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getCellNumber() {
		return this.cellNumber;
	}

	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
	}

	public Date getCtrDate() {
		return this.ctrDate;
	}

	public void setCtrDate(Date ctrDate) {
		this.ctrDate = ctrDate;
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

	public String getIfSend() {
		return this.ifSend;
	}

	public void setIfSend(String ifSend) {
		this.ifSend = ifSend;
	}

	public String getMessageRemark() {
		return this.messageRemark;
	}

	public void setMessageRemark(String messageRemark) {
		this.messageRemark = messageRemark;
	}

	public BigDecimal getRemindId() {
		return this.remindId;
	}

	public void setRemindId(BigDecimal remindId) {
		this.remindId = remindId;
	}

	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

}