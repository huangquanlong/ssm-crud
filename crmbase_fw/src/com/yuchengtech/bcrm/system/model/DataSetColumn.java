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
@Table(name = "MTOOL_DBCOL")
public class DataSetColumn implements Serializable {

	private static final long serialVersionUID = -3071512732613148823L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "ID")
	private Long id;

	/**  */
	@Column(name = "APP_ID", length = 30)
	private String appId ;

	/**中文名称 */
	@Column(name = "COL_NAME_E", length = 250)
	private String colNameE;
	/**英文名称 */
	@Column(name = "COL_NAME_C", length = 250)
	private String colNameC;
	/**字段长度 */
	@Column(name = "COL_SIZE")
	private Long colSize;
	/**数据类型 */
	@Column(name = "COL_TYPE", length = 20)
	private String colType;

	/** 是否能为空 */
	@Column(name = "IS_NULL", length = 20)
	private String isNull;
	
	/** 是否能为空 */
	@Column(name = "PRIMARY_KEY_FLAG", length = 20)
	private String primaryKeyFlag;
	/** 关联表id */
	@Column(name = "DBTABLE_ID")
	private Long dbtableId;
	/** 数据字典 */
	@Column(name = "NOTES", length = 250)
	private String notes;
	/** 是否启用参数 */
	@Column(name = "IS_ENABLE", length = 20)
	private String isEnable;
	@Column(name = "COL_SORT")
	private Long colSort;
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
	public String getColNameE() {
		return colNameE;
	}
	public void setColNameE(String colNameE) {
		this.colNameE = colNameE;
	}
	public String getColNameC() {
		return colNameC;
	}
	public void setColNameC(String colNameC) {
		this.colNameC = colNameC;
	}
	public Long getColSize() {
		return colSize;
	}
	public void setColSize(Long colSize) {
		this.colSize = colSize;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getPrimaryKeyFlag() {
		return primaryKeyFlag;
	}
	public void setPrimaryKeyFlag(String primaryKeyFlag) {
		this.primaryKeyFlag = primaryKeyFlag;
	}
	public Long getDbtableId() {
		return dbtableId;
	}
	public void setDbtableId(Long dbtableId) {
		this.dbtableId = dbtableId;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	public Long getColSort() {
		return colSort;
	}
	public void setColSort(Long colSort) {
		this.colSort = colSort;
	}




}
