package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_SYS_RICHEDIT_INFO database table.
 * 
 */
/***
 * 基础示例
 * @author zhangmin
 *
 */
@Entity
@Table(name="OCRM_SYS_RICHEDIT_INFO")
public class OcrmSysRicheditInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID",unique=true, nullable=false)
	private Long id;

	@Column(name="REL_ID")
	private Long relId;

	@Column(name="CONTENT")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRelId() {
		return relId;
	}

	public void setRelId(Long relId) {
		this.relId = relId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}