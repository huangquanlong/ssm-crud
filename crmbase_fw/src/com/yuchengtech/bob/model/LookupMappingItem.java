package com.yuchengtech.bob.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Lookup 字典表子表
 */
@Entity
@Table(name="OCRM_SYS_LOOKUP_ITEM")
public class LookupMappingItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "F_ID")
	private Long id;

	/** 字典种类ID（字典类型） */
	@Column(name = "F_LOOKUP_ID", nullable = false)
	private String lookup;

	/** 属性代码 */
	@Column(name = "F_CODE", length = 1250, nullable = false)
	private String code;

	/** 属性值 */
	@Column(name = "F_VALUE", length = 200, nullable = false)
	private String value;

	/** 属性说明 */
	@Column(name = "F_COMMENT", length = 200)
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLookup() {
		return lookup;
	}

	public void setLookup(String lookup) {
		this.lookup = lookup;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
