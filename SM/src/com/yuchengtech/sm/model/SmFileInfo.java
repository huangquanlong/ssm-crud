package com.yuchengtech.sm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the sm_file_info database table.
 * 
 */
@Entity
@Table(name="sm_file_info")
@NamedQuery(name="SmFileInfo.findAll", query="SELECT s FROM SmFileInfo s")
public class SmFileInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;

	@Column(name="comm")
	private String comm;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;

	@Column(name="crt_by")
	private String crtBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="crt_time")
	private Date crtTime;

	@Column(name="download_times")
	private Integer downloadTimes;

	@Column(name="file_id")
	private String fileId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modify_time")
	private Date modifyTime;

	@Column(name="name")
	private String name;

	@Column(name="path")
	private String path;

	@Column(name="upd_by")
	private String updBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="upd_time")
	private Date updTime;

	public SmFileInfo() {
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

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Integer getDownloadTimes() {
		return this.downloadTimes;
	}

	public void setDownloadTimes(Integer downloadTimes) {
		this.downloadTimes = downloadTimes;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
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