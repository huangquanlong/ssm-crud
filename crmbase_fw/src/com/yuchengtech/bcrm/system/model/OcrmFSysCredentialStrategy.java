package com.yuchengtech.bcrm.system.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_SYS_CREDENTIAL_STRATEGY database table.
 * 用户认证策略
 */
@Entity
@Table(name="OCRM_F_SYS_CREDENTIAL_STRATEGY")
public class OcrmFSysCredentialStrategy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="ACTIONTYPE")
	private String actiontype;

	@Column(name="DETAIL")
	private String detail;

	@Column(name="ENABLE_FLAG")
	private int enableFlag;
	
	@Column(name="NAME")
	private String name;

    public OcrmFSysCredentialStrategy() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActiontype() {
		return this.actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getEnableFlag() {
		return this.enableFlag;
	}

	public void setEnableFlag(int enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}