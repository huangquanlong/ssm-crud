package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
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
 * 模块管理model
 * @author GUOHCI
 * @since 2012-10-09
 */
@Entity
@Table(name="FW_MODULE")
public class FwModule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

	
    @Temporal( TemporalType.DATE)
    
    
	@Column(name="CRT_DATE")
	private Date crtDate;//创建日期

	@Column(name="IS_OUTER")
	private Integer isOuter;

	@Column(name="IS_RIDE")
	private Integer isRide;

	@Column(name="MDUL_DESC")
	private String mdulDesc;

	@Column(name="MDUL_NAME")
	private String mdulName;

	private String password;

	@Column(name="PWD_KEY")
	private String pwdKey;

	private String url;

	@Column(name="USER_KEY")
	private String userKey;

	@Column(name="USER_NAME")
	private String userName;

	private Integer version;

    public FwModule() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCrtDate() {
		return this.crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	public Integer getIsOuter() {
		return this.isOuter;
	}

	public void setIsOuter(Integer isOuter) {
		this.isOuter = isOuter;
	}

	public Integer getIsRide() {
		return this.isRide;
	}

	public void setIsRide(Integer isRide) {
		this.isRide = isRide;
	}

	public String getMdulDesc() {
		return this.mdulDesc;
	}

	public void setMdulDesc(String mdulDesc) {
		this.mdulDesc = mdulDesc;
	}

	public String getMdulName() {
		return this.mdulName;
	}

	public void setMdulName(String mdulName) {
		this.mdulName = mdulName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPwdKey() {
		return this.pwdKey;
	}

	public void setPwdKey(String pwdKey) {
		this.pwdKey = pwdKey;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserKey() {
		return this.userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}