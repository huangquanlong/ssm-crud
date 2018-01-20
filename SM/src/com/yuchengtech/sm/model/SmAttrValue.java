package com.yuchengtech.sm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the sm_attr_value database table.
 * 
 */
@Entity
@Table(name="sm_attr_value")
@NamedQuery(name="SmAttrValue.findAll", query="SELECT s FROM SmAttrValue s")
public class SmAttrValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="id")
	private Long id;

	@Column(name="attr_def_id")
	private Integer attrDefId;

	@Column(name="base_info_id")
	private Integer baseInfoId;

	@Column(name="crt_by")
	private String crtBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="crt_time")
	private Date crtTime;

	@Column(name="upd_by")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upd_time")
	private Date updTime;

	@Column(name="value")
	private String value;

	@Column(name="value2")
	private String value2;

	public SmAttrValue() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAttrDefId() {
		return this.attrDefId;
	}

	public void setAttrDefId(Integer attrDefId) {
		this.attrDefId = attrDefId;
	}

	public Integer getBaseInfoId() {
		return this.baseInfoId;
	}

	public void setBaseInfoId(Integer baseInfoId) {
		this.baseInfoId = baseInfoId;
	}

	public String getCrtBy() {
		return this.crtBy;
	}

	public void setCrtBy(String crtBy) {
		this.crtBy = crtBy;
	}

	public Date getCrtTime() {
		return this.crtTime;
	}

	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	public String getUpdBy() {
		return this.updBy;
	}

	public void setUpdBy(String updBy) {
		this.updBy = updBy;
	}

	public Date getUpdTime() {
		return this.updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue2() {
		return this.value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

}