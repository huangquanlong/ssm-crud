package com.yuchengtech.bob.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.yuchengtech.bcrm.workplat.model.WorkingplatformNoticeOrganizer;

/**
 * @date 2011-07-25
 * @author WillJoe
 * @describe Entity of the table OCRM_F_WP_NOTICE().
 *           FDM(基础数据层),归属于工作平台的第二主题，数据为公告信息
 */

@Entity
@Table(name = "OCRM_F_WP_NOTICE")
public class WorkingplatformNotice implements Serializable {

	/**
	 * for warning!
	 */
	private static final long serialVersionUID = -4896335541713574523L;

	/** 公告id */
	@Id
	@Column(name = "NOTICE_ID")
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long noticeId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "noticeId", orphanRemoval = true)
	private List<WorkingplatformNoticeRead> noticeIdL;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "noticeId", orphanRemoval = true)
	private List<WorkingplatformNoticeOrganizer> noticeIdO;

	/** 公告标题 */
	@Column(name = "NOTICE_TITLE", length = 200)
	private String noticeTitle;

	/** 公告内容 */
	@Column(name = "NOTICE_CONTENT", length = 2000)
	private String noticeContent;

	/** 公告重要程度 */
	@Column(name = "NOTICE_LEVEL", length = 20)
	private String noticeLevel;

	/** 公告发布人id */
	@Column(name = "PUBLISHER", length = 30)
	private String publisher;

	/** 公告发布机构id */
	@Column(name = "PUBLISH_ORG", length = 32)
	private String publishOrganizer;

	/** 是否置顶 */
	@Column(name = "IS_TOP", length = 20)
	private String isTop;

	/** 公告发布时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "PUBLISH_TIME")
	private Date publishTime;

	/** 功能类型 */
	@Column(name = "MOD_TYPE", length = 50)
	private String moduleType;

	/** 是否发布 */
	@Column(name = "PUBLISHED", length = 20)
	private String published;

	@Column(name = "CREATOR", length = 20)
	private String creator;
	
	public String getPublishOrganizer() {
		return publishOrganizer;
	}

	public void setPublishOrganizer(String publishOrganizer) {
		this.publishOrganizer = publishOrganizer;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	/** 有效期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ACTIVE_DATE")
	private Date activeDate;

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	/** 置顶有效期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "TOP_ACTIVE_DATE")
	private Date topActiveDate;

	/** 文档类型 */
	@Column(name = "NOTICE_TYPE", length = 20)
	private String noticeType;
	
	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getNoticeLevel() {
		return noticeLevel;
	}

	public void setNoticeLevel(String noticeLevel) {
		this.noticeLevel = noticeLevel;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public Date getTopActiveDate() {
		return topActiveDate;
	}

	public void setTopActiveDate(Date topActiveDate) {
		this.topActiveDate = topActiveDate;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public List<WorkingplatformNoticeRead> getNoticeIdL() {
		return noticeIdL;
	}

	public void setNoticeIdL(List<WorkingplatformNoticeRead> noticeIdL) {
		this.noticeIdL = noticeIdL;
	}

	public void setNoticeIdO(List<WorkingplatformNoticeOrganizer> noticeIdO) {
		this.noticeIdO = noticeIdO;
	}

	public List<WorkingplatformNoticeOrganizer> getNoticeIdO() {
		return noticeIdO;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getPublished() {
		return published;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public Long getNoticeId() {
		return noticeId;
	}

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
