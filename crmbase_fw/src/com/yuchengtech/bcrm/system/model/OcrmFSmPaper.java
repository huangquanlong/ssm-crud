package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
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
 * �ʾ����model
 * @author wangwan
 * @since 2012-12-07
 */
@Entity
@Table(name="OCRM_F_SM_PAPERS")
public class OcrmFSmPaper implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SM_PAPERS", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)//(strategy=GenerationType.SEQUENCE, generator="OCRM_F_SM_PAPERS")

	private Long id;

	private String available;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;
    
    @Temporal( TemporalType.DATE)
	@Column(name="PUBLISH_DATE")
	private Date publishDate;

	@Column(name="CREATE_ORG")
	private String createOrg;

	private String creator;

	@Column(name="OPTION_TYPE")
	private String optionType;

	@Column(name="PAPER_NAME")
	private String paperName;

	private String remark;

    public OcrmFSmPaper() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getAvailable() {
		return this.available;
	}

	public void setAvailable(String available) {
		this.available = available;
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

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOptionType() {
		return this.optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getPaperName() {
		return this.paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}