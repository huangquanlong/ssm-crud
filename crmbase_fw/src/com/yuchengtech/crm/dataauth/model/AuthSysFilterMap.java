package com.yuchengtech.crm.dataauth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_CM_ACCORDION_CUST database table.
 * @author wz
 * @since 2012-11-16
 */
@Entity
@Table(name="AUTH_SYS_FILTER_MAP")
public class AuthSysFilterMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//主键ID

	@Column(name="FUNCTION_ID")
	private Long functionId;//功能点ID

	@Column(name="CLASS_NAME")
	private String className;//操作类名

	@Column(name="CLASS_DESC")
	private String classDesc;//操作描述

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}
	


}