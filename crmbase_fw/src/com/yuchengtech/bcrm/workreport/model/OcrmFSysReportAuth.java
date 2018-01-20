package com.yuchengtech.bcrm.workreport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "OCRM_F_SYS_REPORT_AUTH")
public class OcrmFSysReportAuth {

	@Id
	@SequenceGenerator(name = "OCRM_F_SYS_REPORT_AUTH_ID_GENERATOR", sequenceName = "ID_SEQUENCE", allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	/**id*/
	@Column(name = "ID")
	private Long id;

	@Column(name = "APP_ID")
	private String appId;

	@Column(name = "ROLE_CODE")
	private String roleCode;

	@Column(name = "REPORT_CODE")
	private String reportCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

}
