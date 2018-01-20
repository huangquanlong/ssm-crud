package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 数据集方案表
 */
@Entity
@Table(name = "MTOOL_DBTABLE")
public class DataSet implements Serializable {

	private static final long serialVersionUID = -3071512732613148823L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "id")
	private Long id;

	/**  */
	@Column(name = "APP_ID", length = 30)
	private String appId ;

	/** 父节点id */
	@Column(name = "PARENT_ID")
	private Long parentId;

	/** 数据集名称 */
	@Column(name = "NAME", length = 200)
	private String name;

	/** 数据集类型*/
	@Column(name = "TYPE")
	private Long type;
	/** 数据集类型名称*/
	@Column(name = "TYPE_NAME", length = 200)
	private String typeName;

	/** sql或表名*/
	@Column(name = "VALUE", length = 4000)
	private String value;
	@Column(name = "NOTES", length = 256)
	private String notes;

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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
