package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_SE_TITLE_RESULT database table.
 * 
 */
@Entity
@Table(name="OCRM_F_SE_TITLE_RESULT")
public class OcrmFSeTitleResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SE_TITLE_RESULT_RESULTID_GENERATOR" , sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)//(strategy=GenerationType.SEQUENCE, generator="OCRM_F_SE_TITLE_RESULT_RESULTID_GENERATOR")
	@Column(name="RESULT_ID", unique=true, nullable=false)
	private Long resultId;

	@Column(length=400)
	private String result;

	@Column(name="RESULT_SCORING", precision=10, scale=6)
	private BigDecimal resultScoring;

//	//bi-directional many-to-one association to OcrmFSeTitle
////    @ManyToOne
//	@Column(name="TITLE_ID", nullable=false)
//	private Long titleId;
	@ManyToOne
	@JoinColumn(name = "TITLE_ID", nullable = false)
	private OcrmFSeTitle titleId;
	
	@Column(name="RESULT_SORT", precision=3)
	private BigDecimal resultSort;
    public OcrmFSeTitleResult() {
    }

    
	public BigDecimal getResultSort() {
		return resultSort;
	}


	public void setResultSort(BigDecimal resultSort) {
		this.resultSort = resultSort;
	}


	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public BigDecimal getResultScoring() {
		return this.resultScoring;
	}

	public void setResultScoring(BigDecimal resultScoring) {
		this.resultScoring = resultScoring;
	}

	public OcrmFSeTitle getTitleId() {
		return titleId;
	}

	public void setTitleId(OcrmFSeTitle titleId) {
		this.titleId = titleId;
	}


	
}