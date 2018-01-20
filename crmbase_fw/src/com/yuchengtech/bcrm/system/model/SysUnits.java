package com.yuchengtech.bcrm.system.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.ReadOnly;

/**
 * 机构表(视图)
 * @author WillJoe
 */
@Entity
@ReadOnly
@Table(name = "SYS_UNITS")
public class SysUnits {
	//主键ID
	@Id
	private String id;
	//APP_ID
	private String appId;
	//机构ID
	private String unitid;
	//机构名称
	private String unitname;
	//上级机构ID
	private String superunitid;
	//机构层级
	private String levelunit;
	//机构序列
	private String unitseq;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getSuperunitid() {
		return superunitid;
	}
	public void setSuperunitid(String superunitid) {
		this.superunitid = superunitid;
	}
	public String getLevelunit() {
		return levelunit;
	}
	public void setLevelunit(String levelunit) {
		this.levelunit = levelunit;
	}
	public String getUnitseq() {
		return unitseq;
	}
	public void setUnitseq(String unitseq) {
		this.unitseq = unitseq;
	}

}
