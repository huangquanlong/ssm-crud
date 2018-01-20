package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 模块功能model
 * @author songxs
 * @since 2012-9-23
 */
@Entity
@Table(name="FW_FUNCTION")
public class FwFunction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//模块ID同时也是主键

	private String action;//动作

	@Column(name="FUNC_DESC")
	private String funcDesc;//

	@Column(name="FUNC_NAME")
	private String funcName;//功能名称

	private Integer isfixed;//

	@Column(name="MODULE_ID")
	private Long moduleId;//模块ID

	@Column(name="PARENT_ID")
	private Long parentId;//父节点ID

	private Integer version;//

    public FwFunction() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFuncDesc() {
		return this.funcDesc;
	}

	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}

	public String getFuncName() {
		return this.funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public Integer getIsfixed() {
		return this.isfixed;
	}

	public void setIsfixed(Integer isfixed) {
		this.isfixed = isfixed;
	}

	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}