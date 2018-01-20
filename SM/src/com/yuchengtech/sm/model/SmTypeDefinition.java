package com.yuchengtech.sm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the sm_type_definition database table.
 * 
 */
@Entity
@Table(name="sm_type_definition")
@NamedQuery(name="SmTypeDefinition.findAll", query="SELECT s FROM SmTypeDefinition s")
public class SmTypeDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="id")
	private Long id;
	
	@Column(name="classify")
	private String classify;
	
	@Column(name="comm")
	private String comm;

	@Column(name="crt_by")
	private String crtBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="crt_time")
	private Date crtTime;

	@Column(name="display_name")
	private String displayName;

	@Column(name="identify")
	private String identify;

	@Column(name="name")
	private String name;

	@Column(name="state")
	private String state;

	@Column(name="upd_by")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upd_time")
	private Date updTime;

	public SmTypeDefinition() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassify() {
		return this.classify;
	}

	public void setClassify(String classify) {
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

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
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