package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the FW_SYS_PROP database table.
 * 
 */
@Entity
@Table(name="FW_SYS_PROP")
public class FwSysProp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="APP_ID")
	private String appId;

	@Column(name="PROP_DESC")
	private String propDesc;

	@Column(name="PROP_NAME")
	private String propName;

	@Column(name="PROP_VALUE")
	private String propValue;

	private String remark;

	private int version;

    public FwSysProp() {
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

	public String getPropDesc() {
		return this.propDesc;
	}

	public void setPropDesc(String propDesc) {
		this.propDesc = propDesc;
	}

	public String getPropName() {
		return this.propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropValue() {
		return this.propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}