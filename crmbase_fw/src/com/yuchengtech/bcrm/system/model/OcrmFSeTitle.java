package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the OCRM_F_SE_TITLE database table.
 * 
 */
@Entity
@Table(name="OCRM_F_SE_TITLE")
public class OcrmFSeTitle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SE_TITLE_TITLEID_GENERATOR" , sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)//(strategy=GenerationType.SEQUENCE, generator="OCRM_F_SE_TITLE_TITLEID_GENERATOR")
	@Column(name="TITLE_ID", unique=true, nullable=false)
	private Long titleId;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "titleId", orphanRemoval = true)
	private List<OcrmFSeTitleResult> titleIdL;

	private String available;

	@Column(name="TITLE_SORT")
	private BigDecimal titleSort;

	@Column(name="TITLE_TYPE")
	private String titleType;

    @Temporal( TemporalType.DATE)
	@Column(name="UPDATE_DATE")
	private Date updateDate;

	private String updator;
	
	@Column(name="TITLE_NAME", length=200)
	private String titleName;

	@Column(name="TITLE_REMARK", length=800)
	private String titleRemark;


	public void setTitleId(long titleId) {
		this.titleId = titleId;
	}

	public Long getTitleId() {
		return titleId;
	}

	public void setTitleId(Long titleId) {
		this.titleId = titleId;
	}

	public String getTitleName() {
		return this.titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getTitleRemark() {
		return this.titleRemark;
	}

	public void setTitleRemark(String titleRemark) {
		this.titleRemark = titleRemark;
	}


	public List<OcrmFSeTitleResult> getTitleIdL() {
		return titleIdL;
	}

	public void setTitleIdL(List<OcrmFSeTitleResult> titleIdL) {
		this.titleIdL = titleIdL;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public BigDecimal getTitleSort() {
		return titleSort;
	}

	public void setTitleSort(BigDecimal titleSort) {
		this.titleSort = titleSort;
	}

	public String getTitleType() {
		return titleType;
	}

	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}
	
	
}