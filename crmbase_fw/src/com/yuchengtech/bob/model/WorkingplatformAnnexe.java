package com.yuchengtech.bob.model;

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
 * The persistent class for the OCRM_F_WP_ANNEXE database table.
 * 
 * FDM层(基础数据层)，归属于工作平台的第二主题，数据为附件信息
 */
@Entity
@Table(name = "OCRM_F_WP_ANNEXE")
public class WorkingplatformAnnexe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2310748837420523663L;

	/** 附件id */
	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "ANNEXE_ID", nullable = false)
	private Long annexeId;

	/** 附件名称 */
	@Column(name = "ANNEXE_NAME", length = 200)
	private String annexeName;

	/** 福建服务器名称 */
	@Column(name = "ANNEXE_SER_NAME", length = 200)
	private String annexeServerName;

	/** 附件大小 */
	@Column(name = "ANNEXE_SIZE", precision = 22, scale = 2)
	private BigDecimal annexeSize;

	/** 附件类型 */
	@Column(name = "ANNEXE_TYPE", length = 20)
	private String annexeType;

	/** 客户端名称 */
	@Column(name = "CLIENT_NAME", length = 200)
	private String clientName;

	/** 创建时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	/** 最后下载时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_LOAD_TIME")
	private Date lastLoadTime;

	/** 最后下载人id */
	@Column(name = "LAST_LOADER", length = 30)
	private String lastLoader;

	/** 下载次数 */
	@Column(name = "LOAD_COUNT")
	private Long loadCount;

	/** 物理地址 */
	@Column(name = "PHYSICAL_ADDRESS", length = 200)
	private String physicalAddress;

	/** 关系信息id */
	@Column(name = "RELATION_INFO", length = 20)
	private String relationInfo;

	@Column(name = "RELATIOIN_MOD", length = 20)
	private String relationMod;
	
	public String getRelationMod() {
        return relationMod;
    }

    public void setRelationMod(String relationMod) {
        this.relationMod = relationMod;
    }

    public String getAnnexeName() {
		return this.annexeName;
	}

	public void setAnnexeName(String annexeName) {
		this.annexeName = annexeName;
	}

	public String getAnnexeType() {
		return this.annexeType;
	}

	public void setAnnexeType(String annexeType) {
		this.annexeType = annexeType;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoadTime() {
		return this.lastLoadTime;
	}

	public void setLastLoadTime(Date lastLoadTime) {
		this.lastLoadTime = lastLoadTime;
	}

	public String getLastLoader() {
		return this.lastLoader;
	}

	public void setLastLoader(String lastLoader) {
		this.lastLoader = lastLoader;
	}
	
	public String getPhysicalAddress() {
		return this.physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public String getRelationInfo() {
		return this.relationInfo;
	}

	public void setRelationInfo(String relationInfo) {
		this.relationInfo = relationInfo;
	}

	public void setAnnexeServerName(String annexeServerName) {
		this.annexeServerName = annexeServerName;
	}

	public String getAnnexeServerName() {
		return annexeServerName;
	}

	public void setAnnexeId(Long annexeId) {
		this.annexeId = annexeId;
	}

	public Long getAnnexeId() {
		return annexeId;
	}

	public void setAnnexeSize(BigDecimal annexeSize) {
		this.annexeSize = annexeSize;
	}

	public BigDecimal getAnnexeSize() {
		return annexeSize;
	}

	public void setLoadCount(Long loadCount) {
		this.loadCount = loadCount;
	}

	public Long getLoadCount() {
		return loadCount;
	}

}