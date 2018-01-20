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

/**
 * 菜单项model
 * @author songxs
 * @since 2012-9-23
 */
@Entity
@Table(name="CNT_MENU")
public class CntMenu implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//主键ID

	@Column(name="APP_ID")
	private String appId;//逻辑ID

    @Temporal( TemporalType.DATE)
	@Column(name="CRT_DATE")
	private Date crtDate;//创建日期

	private String icon;//图标

	private Integer isfixed;//

	private Integer issamewin;//是否新窗口打开

	@Column(name="LEAF_FLAG")
	private Integer leafFlag;//节点标志

	@Column(name="MOD_FUNC_ID")
	private Long modFuncId;//模块功能ID

	private String name;//菜单项名称

	@Column(name="ORDER_")
	private Integer order;//排序

	@Column(name="PARENT_ID")
	private Long parentId;//父节点ID

	private String tip;//

	private Integer type;//

	private Integer version;//

    public CntMenu() {
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

	public Date getCrtDate() {
		return this.crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getIsfixed() {
		return this.isfixed;
	}

	public void setIsfixed(Integer isfixed) {
		this.isfixed = isfixed;
	}

	public Integer getIssamewin() {
		return this.issamewin;
	}

	public void setIssamewin(Integer issamewin) {
		this.issamewin = issamewin;
	}

	public Integer getLeafFlag() {
		return this.leafFlag;
	}

	public void setLeafFlag(Integer leafFlag) {
		this.leafFlag = leafFlag;
	}

	public Long getModFuncId() {
		return this.modFuncId;
	}

	public void setModFuncId(Long modFuncId) {
		this.modFuncId = modFuncId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getTip() {
		return this.tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}