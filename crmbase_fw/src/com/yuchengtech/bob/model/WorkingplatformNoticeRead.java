package com.yuchengtech.bob.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @Date 2011-07-25
 * @author WillJoe
 * @describe Entity of the table "OCRM_F_WP_NOTICE_READ"
 * FDM层(基础数据层)，用户查阅信息历史信息
 */
@Entity
@Table(name = "OCRM_F_WP_NOTICE_READ")
public class WorkingplatformNoticeRead implements Serializable {

	/**
	 * for warning!
	 */
	private static final long serialVersionUID = 1796461376678511919L;

	/***/
	@Id
	@Column(name = "ID", nullable = false, precision = 22)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "NOTICE_ID", nullable = false)
	private WorkingplatformNotice noticeId;

	/** 用户id */
	@Column(name = "USER_ID", length = 200)
	private String userId;

	/** 功能类型 */
	@Column(name = "FUNCTION_TYPE", length = 20)
	private String functionType;

	/** 是否下载 */
	@Column(name = "IS_LOAD")
	private Boolean isLoad;

	/** 阅读时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "READ_TIME")
	private Date readTime;

	/** 下载时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "LOAD_TIME")
	private Date loadTime;

	public Date getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(Date loadTime) {
		this.loadTime = loadTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getReadTime() {
		return readTime;
	}

	public WorkingplatformNotice getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(WorkingplatformNotice noticeId) {
		this.noticeId = noticeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setIsLoad(Boolean isLoad) {
		this.isLoad = isLoad;
	}

	public Boolean getIsLoad() {
		return isLoad;
	}

}
