package com.yuchengtech.sm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the sm_attr_classify database table.
 * 
 */
@Entity
@Table(name="sm_attr_classify")
public class SmAttrClassify implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="id")
	private Long id;
	
	@Column(name="comm")
	private String comm;

	@Column(name="crt_by")
	private String crtBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="crt_time")
	private Date crtTime;

	@Column(name="name")
	private String name;

	@Column(name="upd_by")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upd_time")
	private Date updTime;

	public SmAttrClassify() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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