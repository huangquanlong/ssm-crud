package com.yuchengtech.bob.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Lookup 字典表
 */
@Entity
@Table(name="OCRM_SYS_LOOKUP")
public class LookupMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "F_ID")
	private Long id;
	
	/** 字典名称 */
	@Column(name = "F_NAME", length = 100)
	private String name;

	/** 字典名称中文备注 */
	@Column(name = "F_COMMENT", length = 200)
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
