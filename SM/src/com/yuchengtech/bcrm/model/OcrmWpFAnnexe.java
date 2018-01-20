package com.yuchengtech.bcrm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the OCRM_WP_F_ANNEXE database table.
 * 
 */
@Entity
@Table(name="OCRM_F_WP_ANNEXE")
public class OcrmWpFAnnexe implements Serializable {
	
	@Id
	@SequenceGenerator(name="OCRM_F_WP_ANNEXE_ID_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ANNEXE_ID")
	private Long annexeId;

	@Column(name="ANNEXE_NAME")
	private String annexeName;

	@Column(name="ANNEXE_SER_NAME")
	private String annexeSerName;

	@Column(name="ANNEXE_SIZE")
	private BigDecimal annexeSize;

	@Column(name="ANNEXE_TYPE")
	private String annexeType;

	@Column(name="CLIENT_NAME")
	private String clientName;
	@Temporal( TemporalType.DATE)
	@Column(name="CREATE_TIME")
	private Date createTime;
	@Temporal( TemporalType.DATE)
	@Column(name="LAST_LOAD_TIME")
	private Date lastLoadTime;

	@Column(name="LAST_LOADER")
	private String lastLoader;

	@Column(name="LOAD_COUNT")
	private Integer loadCount;

	@Column(name="PHYSICAL_ADDRESS")
	private String physicalAddress;

	@Column(name="RELATION_INFO")
	private long relationInfo;

	public Long getAnnexeId() {
		return annexeId;
	}

	public void setAnnexeId(Long annexeId) {
		this.annexeId = annexeId;
	}

	public String getAnnexeName() {
		return annexeName;
	}

	public void setAnnexeName(String annexeName) {
		this.annexeName = annexeName;
	}

	public String getAnnexeSerName() {
		return annexeSerName;
	}

	public void setAnnexeSerName(String annexeSerName) {
		this.annexeSerName = annexeSerName;
	}

	public BigDecimal getAnnexeSize() {
		return annexeSize;
	}

	public void setAnnexeSize(BigDecimal annexeSize) {
		this.annexeSize = annexeSize;
	}

	public String getAnnexeType() {
		return annexeType;
	}

	public void setAnnexeType(String annexeType) {
		this.annexeType = annexeType;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoadTime() {
		return lastLoadTime;
	}

	public void setLastLoadTime(Date lastLoadTime) {
		this.lastLoadTime = lastLoadTime;
	}

	public String getLastLoader() {
		return lastLoader;
	}

	public void setLastLoader(String lastLoader) {
		this.lastLoader = lastLoader;
	}

	public Integer getLoadCount() {
		return loadCount;
	}

	public void setLoadCount(Integer loadCount) {
		this.loadCount = loadCount;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public long getRelationInfo() {
		return relationInfo;
	}

	public void setRelationInfo(long relationInfo) {
		this.relationInfo = relationInfo;
	}
}