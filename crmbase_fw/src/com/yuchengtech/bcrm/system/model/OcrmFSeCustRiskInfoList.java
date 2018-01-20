package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the OCRM_F_SE_CUST_RISK_INFO_LIST database table.
 * 
 */
@Entity
@Table(name="OCRM_F_SE_CUST_RISK_INFO_LIST")
public class OcrmFSeCustRiskInfoList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SE_CUST_RISK_INFO_LIST_ID_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize=1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)//(strategy=GenerationType.SEQUENCE, generator="OCRM_F_SE_CUST_RISK_INFO_LIST_ID_GENERATOR")
	@Column(name="CUST_Q_ID",unique=true, nullable=false)
	private Long custqId;

	@Column(name="ADJUSTMENT_VALUE", precision=10, scale=4)
	private BigDecimal adjustmentValue;

	@Column(name="CUST_NAME", length=200)
	private String custName;

	@Column(name="CUST_NO", length=21)
	private String custNo;

	@Column(name="CUST_OTHER_INFO", length=800)
	private String custOtherInfo;

	@Column(name="CUST_RISK_CHARACT", length=200)
	private String custRiskCharact;

    @Temporal( TemporalType.DATE)
	@Column(name="EVALUATE_DATE")
	private Date evaluateDate;

	@Column(name="EVALUATE_INST", length=200)
	private String evaluateInst;

	@Column(name="EVALUATE_NAME", length=200)
	private String evaluateName;

	@Column(name="EVALUATE_RELAT_TELEPHONE", length=20)
	private String evaluateRelatTelephone;

	@Column(name="INDAGETE_QA_SCORING", precision=10, scale=4)
	private BigDecimal indageteQaScoring;

	@Column(name="RISK_CHARACT_TYPE", length=13)
	private String riskCharactType;
	
	@Column(name="HIS_FLAG", length=13)
	private String hisFlag;
	
	@Temporal( TemporalType.DATE)
	@Column(name="LIMIT_DATE")
	private Date limitDate;
	
	@Column(name="PAPERS_ID",length=20)
	private Long paperId;

    public OcrmFSeCustRiskInfoList() {
    }



	public Long getCustqId() {
		return custqId;
	}



	public void setCustqId(Long custqId) {
		this.custqId = custqId;
	}



	public BigDecimal getAdjustmentValue() {
		return this.adjustmentValue;
	}

	public void setAdjustmentValue(BigDecimal adjustmentValue) {
		this.adjustmentValue = adjustmentValue;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustNo() {
		return this.custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getCustOtherInfo() {
		return this.custOtherInfo;
	}

	public void setCustOtherInfo(String custOtherInfo) {
		this.custOtherInfo = custOtherInfo;
	}

	public String getCustRiskCharact() {
		return this.custRiskCharact;
	}

	public void setCustRiskCharact(String custRiskCharact) {
		this.custRiskCharact = custRiskCharact;
	}

	public Date getEvaluateDate() {
		return this.evaluateDate;
	}

	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
	}

	public String getEvaluateInst() {
		return this.evaluateInst;
	}

	public void setEvaluateInst(String evaluateInst) {
		this.evaluateInst = evaluateInst;
	}

	public String getEvaluateName() {
		return this.evaluateName;
	}

	public void setEvaluateName(String evaluateName) {
		this.evaluateName = evaluateName;
	}

	public String getEvaluateRelatTelephone() {
		return this.evaluateRelatTelephone;
	}

	public void setEvaluateRelatTelephone(String evaluateRelatTelephone) {
		this.evaluateRelatTelephone = evaluateRelatTelephone;
	}

	public BigDecimal getIndageteQaScoring() {
		return this.indageteQaScoring;
	}

	public void setIndageteQaScoring(BigDecimal indageteQaScoring) {
		this.indageteQaScoring = indageteQaScoring;
	}

	public String getRiskCharactType() {
		return this.riskCharactType;
	}

	public void setRiskCharactType(String riskCharactType) {
		this.riskCharactType = riskCharactType;
	}

	public String getHisFlag() {
		return hisFlag;
	}

	public void setHisFlag(String hisFlag) {
		this.hisFlag = hisFlag;
	}

	public Date getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(Date limitDate) {
		this.limitDate = limitDate;
	}
	
	public Long getPaperId(){
		return paperId;
	}
	public void setPaperId(Long paperId){
		this.paperId = paperId;
	}
}