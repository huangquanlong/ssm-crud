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
 * The persistent class for the OCRM_F_SM_PAPERS_QUESTION_REL database table.
 * 
 */
@Entity
@Table(name="OCRM_F_SM_PAPERS_QUESTION_REL")
public class OcrmFSmPapersQuestionRel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SM_PAPERS_QUESTION_REL", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)//(strategy=GenerationType.SEQUENCE, generator="OCRM_F_SM_PAPERS_QUESTION_REL")
	private Long id;

	@Column(name="PAPER_ID")
	private BigDecimal paperId;

	private BigDecimal power;

	@Column(name="QUESTION_ID")
	private BigDecimal questionId;

	@Column(name="QUESTION_ORDER")
	private BigDecimal questionOrder;

    public OcrmFSmPapersQuestionRel() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPaperId() {
		return this.paperId;
	}

	public void setPaperId(BigDecimal paperId) {
		this.paperId = paperId;
	}

	public BigDecimal getPower() {
		return this.power;
	}

	public void setPower(BigDecimal power) {
		this.power = power;
	}

	public BigDecimal getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(BigDecimal questionId) {
		this.questionId = questionId;
	}

	public BigDecimal getQuestionOrder() {
		return this.questionOrder;
	}

	public void setQuestionOrder(BigDecimal questionOrder) {
		this.questionOrder = questionOrder;
	}

}