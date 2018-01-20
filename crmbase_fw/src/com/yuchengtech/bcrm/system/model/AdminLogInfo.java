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
 * 系统日志表
 * @author weijl
 * @since 2012-09-24
 */
@Entity
@Table(name="ADMIN_LOG_INFO")
public class AdminLogInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "ID",nullable = false)
	private Long id; //日志id

	@Column(name="AFTER_VALUE")
	private String afterValue; //参数

	@Column(name="APP_ID")
	private String appId ; 

	@Column(name="BEFORE_VALUE")
	private String beforeValue;
	
	@Column(name="CONTENT")
	private String content; //操作信息

	@Column(name="LOG_TYPE_ID")
	private Long logTypeId;

	@Column(name="LOGIN_IP")
	private String loginIp; //登陆IP地址

	@Column(name="OPER_FLAG")
	private Integer operFlag;

	@Column(name="OPER_OBJ_ID")
	private Long operObjId;
	@Temporal( TemporalType.DATE)
	@Column(name="OPER_TIME")
	private Date operTime; //时间

	@Column(name="ORG_ID")
	private String orgId; //机构id

	@Column(name="USER_ID")
	private String userId; //操作用户id

	@Column(name="VERSION")
	private Integer version;

    public AdminLogInfo() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAfterValue() {
		return afterValue;
	}

	public void setAfterValue(String afterValue) {
		this.afterValue = afterValue;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBeforeValue() {
		return beforeValue;
	}

	public void setBeforeValue(String beforeValue) {
		this.beforeValue = beforeValue;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getLogTypeId() {
		return logTypeId;
	}

	public void setLogTypeId(Long logTypeId) {
		this.logTypeId = logTypeId;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getOperFlag() {
		return operFlag;
	}

	public void setOperFlag(Integer operFlag) {
		this.operFlag = operFlag;
	}

	public Long getOperObjId() {
		return operObjId;
	}

	public void setOperObjId(Long operObjId) {
		this.operObjId = operObjId;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}