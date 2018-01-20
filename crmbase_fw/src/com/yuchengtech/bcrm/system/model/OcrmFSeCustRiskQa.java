package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_SE_CUST_RISK_QA database table.
 * 
 */
@Entity
@Table(name="OCRM_F_SE_CUST_RISK_QA")
public class OcrmFSeCustRiskQa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SE_CUST_RISK_QA_ID_GENERATOR" , sequenceName="ID_SEQUENCE" ,allocationSize=1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)//(strategy=GenerationType.SEQUENCE, generator="OCRM_F_SE_CUST_RISK_QA_ID_GENERATOR")
	@Column(name="CUST_Q_T_ID",unique=true, nullable=false)
	private Long custqtId;
	
	@Column(name="CUST_Q_ID", length=200)
	private Long custqId;
	
	@Column(name="CUST_SELECT_CONTENT", length=200)
	private String custSelectContent;

	@Column(name="QA_TITLE", length=200)
	private String qaTitle;

	@Column(precision=10, scale=6)
	private BigDecimal scoring;

	@Column(name="TITLE_REMARK", length=800)
	private String titleRemark;

    public OcrmFSeCustRiskQa() {
    }

	public Long getCustqtId() {
		return custqtId;
	}

	public void setCustqtId(Long custqtId) {
		this.custqtId = custqtId;
	}
	
	public Long getCustqId() {
		return custqId;
	}

	public void setCustqId(Long custqId) {
		this.custqId = custqId;
	}

	public String getCustSelectContent() {
		return this.custSelectContent;
	}

	public void setCustSelectContent(String custSelectContent) {
		this.custSelectContent = custSelectContent;
	}

	public String getQaTitle() {
		return this.qaTitle;
	}

	public void setQaTitle(String qaTitle) {
		this.qaTitle = qaTitle;
	}

	public BigDecimal getScoring() {
		return this.scoring;
	}

	public void setScoring(BigDecimal scoring) {
		this.scoring = scoring;
	}

	public String getTitleRemark() {
		return this.titleRemark;
	}

	public void setTitleRemark(String titleRemark) {
		this.titleRemark = titleRemark;
	}

}