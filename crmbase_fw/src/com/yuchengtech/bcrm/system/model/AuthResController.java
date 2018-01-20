package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 功能点控制
 * @author GUOCHI
 * @since 2012-10-15
 */
@Entity
@Table(name="AUTH_RES_CONTROLLERS")
public class AuthResController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="APP_ID")
	private String appId;

	@Column(name="CON_CODE")
	private String conCode;

	@Column(name="FW_FUN_ID")
	private Long fwFunId;

	private String name;

	private String remark;

	private Long version;

    public AuthResController() {
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

	public String getConCode() {
		return this.conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

	public Long getFwFunId() {
		return this.fwFunId;
	}

	public void setFwFunId(Long fwFunId) {
		this.fwFunId = fwFunId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}