package com.yuchengtech.bcrm.workreport.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="OCRM_F_SYS_REPORT")
public class OcrmFSysReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SYS_REPORT_ID_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;

	@Column(name="REPORT_CODE")
	private String reportCode;
	
	@Column(name="REPORT_TYPE")
	private String reportType;
	
	@Column(name="REPORT_SERVER_TYPE")
	private String reportServerType;

	@Column(name="REPORT_NAME")
	private String reportName;

	@Column(name="APP_ID")
	private String appId;

	@Column(name="REPORT_STATUS")
	private Long reportStatus;

	@Column(name="REPORT_URL")
	private String reportUrl;

	@Column(name="REPORT_SORT")
	private Long reportSort;

	private String creator;

	@Column(name="REPORT_DESC")
	private String reportDesc;
	
	
	@Column(name="REPORT_GROUP")
	private String reportGroup;

	public Long getId() {
		return id;
	}

	public String getReportGroup() {
		return reportGroup;
	}

	public void setReportGroup(String reportGroup) {
		this.reportGroup = reportGroup;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Long reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public Long getReportSort() {
		return reportSort;
	}

	public void setReportSort(Long reportSort) {
		this.reportSort = reportSort;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getReportDesc() {
		return reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public String getReportServerType() {
		return reportServerType;
	}

	public void setReportServerType(String reportServerType) {
		this.reportServerType = reportServerType;
	}

	
}