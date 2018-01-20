package com.yuchengtech.bcrm.system.model;

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


@Entity
@Table(name="OCRM_F_SYS_DOWNLOAD_RECORD")
public class OcrmFSysDownloadRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="USER_ID")
	private String userId;

	@Column(name="FILENAME")
	private String fileName;
	
	@Column(name="QUERY_CONDTION")
	private String queryCondtion;
	
	@Column(name="MENU_ID")
	private String menuId;

	@Column(name="THREAD_STATUS")
	private String threadStatus;
	
	@Column(name="ORG_ID")
	private String orgId;
	
	@Column(name="THREAD_ID")
	private String threadId;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="START_TIME")
	private Date startTime;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FINISH_TIME")
	private Date finishTime;
	
	@Column(name="APP_ID")
	private String appId;
	
	@Column(name="MEMO")
	private String memo;
	
    public OcrmFSysDownloadRecord() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getThreadStatus() {
		return threadStatus;
	}

	public void setThreadStatus(String threadStatus) {
		this.threadStatus = threadStatus;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getThreadId() {
		return threadId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getQueryCondtion() {
		return queryCondtion;
	}

	public void setQueryCondtion(String queryCondtion) {
		this.queryCondtion = queryCondtion;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

}