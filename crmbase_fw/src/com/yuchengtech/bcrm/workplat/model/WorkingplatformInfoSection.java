package com.yuchengtech.bcrm.workplat.model;

import java.io.Serializable;
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
 * The persistent class for the OCRM_F_WP_INFO_SECTION database table.
 * 
 * FDM层(基础数据层)，基于工作平台的第二主题，数据为资讯栏目的信息
 */
@Entity
@Table(name = "OCRM_F_WP_INFO_SECTION")
public class WorkingplatformInfoSection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8287199333541098533L;

	/** 栏目id */
	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "SECTION_ID", nullable = false)
	private Long sectionId;

	/** 创建机构 */
	@Column(name = "CREATE_ORG", length = 32)
	private String createOrganizer;

	/** 创建机构名称 */
	@Column(name = "CREATE_ORG_NAME", length = 100)
	private String createOrganizerName;

	/** 创建时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/** 创建人id */
	@Column(length = 30)
	private String creator;

	/** 创建人名称 */
	@Column(name = "CREATOR_NAME", length = 50)
	private String creatorName;

	/** 父类栏目 */
	@Column(name = "PARENT_SECTION", length = 100)
	    private String parentSection;	

	/** 栏目分类 */
	@Column(name = "SECTION_CATEGORY", length = 20)
	private String sectionCategory;

	/** 栏目描述 */
	@Column(name = "SECTION_DISTRIBUTE", length = 500)
	private String sectionDistribute;

	/** 栏目名称 */
	@Column(name = "SECTION_NAME", length = 200)
	private String sectionName;

	/** 栏目摘要说明 */
	@Column(name = "SECTION_SUMMARY", length = 200)
	private String sectionSummary;
	
	// bi-directional many-to-one association to workingplatformInfo
	//@OneToMany(mappedBy = "ocrmFWpInfoSection")
	//private Set<WorkingplatformInfo> ocrmFWpInfos;

	public Long getSectionId() {
		return this.sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return this.creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getParentSection() {
		return this.parentSection;
	}

	public void setParentSection(String parentSection) {
		this.parentSection = parentSection;
	}

	public String getSectionCategory() {
		return this.sectionCategory;
	}

	public void setSectionCategory(String sectionCategory) {
		this.sectionCategory = sectionCategory;
	}

	public String getSectionDistribute() {
		return this.sectionDistribute;
	}

	public void setSectionDistribute(String sectionDistribute) {
		this.sectionDistribute = sectionDistribute;
	}

	public String getSectionName() {
		return this.sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionSummary() {
		return this.sectionSummary;
	}

	public void setSectionSummary(String sectionSummary) {
		this.sectionSummary = sectionSummary;
	}

//	public Set<WorkingplatformInfo> getOcrmFWpInfos() {
//		return this.ocrmFWpInfos;
//	}
//
//	public void setOcrmFWpInfos(Set<WorkingplatformInfo> ocrmFWpInfos) {
//		this.ocrmFWpInfos = ocrmFWpInfos;
//	}

	public void setCreateOrganizer(String createOrganizer) {
		this.createOrganizer = createOrganizer;
	}

	public String getCreateOrganizer() {
		return createOrganizer;
	}

	public void setCreateOrganizerName(String createOrganizerName) {
		this.createOrganizerName = createOrganizerName;
	}

	public String getCreateOrganizerName() {
		return createOrganizerName;
	}

}