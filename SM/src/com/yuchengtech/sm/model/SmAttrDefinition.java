package com.yuchengtech.sm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the sm_attr_definition database table.
 * 
 */
@Entity
@Table(name="sm_attr_definition")
public class SmAttrDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;

	@Column(name="comm")
	private String comm;
	
	@Column(name="display_name")
	private String displayName;
	
	@Column(name="attr_type")
	private String attrType;
	
	@Column(name="type_length")
	private Long typeLength;

	@Column(name="classify")
	private Integer classify;


	@Column(name="identify")
	private String identify;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="crt_time")
	
	private Date crtTime;
	@Column(name="crt_by")
	private String crtBy;


	@Column(name="upd_by")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upd_time")
	private Date updTime;

	public SmAttrDefinition() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttrType() {
		return this.attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	public Integer getClassify() {
		return this.classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}

	public String getComm() {
		return this.comm;
	}

	public void setComm(String comm) {
		this.comm = comm;
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

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getIdentify() {
		return this.identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTypeLength() {
		return this.typeLength;
	}

	public void setTypeLength(Long typeLength) {
		this.typeLength = typeLength;
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

}