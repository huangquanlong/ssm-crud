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
@Table(name="OCRM_F_SYS_REPORT_CFG")
public class OcrmFSysReportCfg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCRM_F_SYS_REPORT_CFG_ID_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;

	@Column(name="REPORT_CODE")
	private String reportCode;

	@Column(name="CONDITION_FIELD")
	private String conditionField;

	@Column(name="APP_ID")
	private String appId;

	@Column(name="CONDITION_NAME")
	private String conditionName;

	@Column(name="CONDITION_TYPE")
	private String conditionType;
	
	@Column(name="IS_ALLOW_BLANK")
	private String isAllowBlank;
	
	@Column(name="IS_HIDDEN")
	private String isHidden;

	@Column(name="CONDITION_DEFAULT")
	private String conditionDefault;

	public Long getId() {
		return id;
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

	public String getConditionField() {
		return conditionField;
	}

	public void setConditionField(String conditionField) {
		this.conditionField = conditionField;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public String getConditionDefault() {
		return conditionDefault;
	}

	public void setConditionDefault(String conditionDefault) {
		this.conditionDefault = conditionDefault;
	}

	public String getIsAllowBlank() {
		return isAllowBlank;
	}

	public void setIsAllowBlank(String isAllowBlank) {
		this.isAllowBlank = isAllowBlank;
	}

	public String getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(String isHidden) {
		this.isHidden = isHidden;
	}	
	
	
}