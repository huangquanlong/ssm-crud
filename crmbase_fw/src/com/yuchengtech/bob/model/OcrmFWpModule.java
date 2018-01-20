package com.yuchengtech.bob.model;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_WP_MODULE database table.
 * 
 */
@Entity
@Table(name="OCRM_F_WP_MODULE")
public class OcrmFWpModule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	@Column(name="MODULE_ID")
	private String moduleId;

	@Column(name="MODULE_SEQ")
	private BigDecimal moduleSeq;

	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="ICON_URL")
	private String iconurl;

    public OcrmFWpModule() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public BigDecimal getModuleSeq() {
		return this.moduleSeq;
	}

	public void setModuleSeq(BigDecimal moduleSeq) {
		this.moduleSeq = moduleSeq;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	public String getIconurl() {
		return iconurl;
	}

}