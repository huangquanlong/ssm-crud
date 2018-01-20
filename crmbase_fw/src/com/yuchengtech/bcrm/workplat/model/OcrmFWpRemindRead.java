package com.yuchengtech.bcrm.workplat.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the OCRM_F_WP_REMIND_READ database table.
 * 
 */
@Entity
@Table(name="OCRM_F_WP_REMIND_READ")
public class OcrmFWpRemindRead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

    @Temporal( TemporalType.DATE)
	@Column(name="READ_TIME")
	private Date readTime;

	@Column(name="REMIND_ID", precision=22)
	private BigDecimal remindId;

	@Column(name="USER_ID", length=30)
	private String userId;

    public OcrmFWpRemindRead() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getReadTime() {
		return this.readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public BigDecimal getRemindId() {
		return this.remindId;
	}

	public void setRemindId(BigDecimal remindId) {
		this.remindId = remindId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}