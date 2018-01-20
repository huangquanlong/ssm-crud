package com.yuchengtech.bob.model;

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
 * The persistent class for the ADMIN_LOG_INFO database table. 
 * 日志记录表
 */ 
@Entity
@Table(name = "ADMIN_LOG_INFO")
public class BIPLogInfo implements Serializable {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public Long getOperObjId() {
		return operObjId;
	}

	public void setOperObjId(Long operObjId) {
		this.operObjId = operObjId;
	}

	public String getBeforeValue() {
		return beforeValue;
	}

	public void setBeforeValue(String beforeValue) {
		this.beforeValue = beforeValue;
	}

	public String getAfterValue() {
		return afterValue;
	}

	public void setAfterValue(String afterValue) {
		this.afterValue = afterValue;
	}

	public Long getOperFlag() {
		return operFlag;
	}

	public void setOperFlag(Long operFlag) {
		this.operFlag = operFlag;
	}

	public Long getLogTypeId() {
		return logTypeId;
	}

	public void setLogTypeId(Long logTypeId) {
		this.logTypeId = logTypeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	private static final long serialVersionUID = 2476367195732252769L;

	/** ID */
	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "ID",nullable = false)
	private Long id;

	/** 版本 */
	@Column(name = "VERSION")
	private Long version;

	/** 逻辑系统 */
	@Column(name = "APP_ID")
	private String appId="";

	/** 操作人ID */
	@Column(name = "USER_ID")
	private String userId="";

	/** 操作时间*/
	@Temporal(TemporalType.DATE)
	@Column(name = "OPER_TIME")
	private Date operTime;

	/** 操作对象主键 */
	@Column(name = "OPER_OBJ_ID")
	private Long operObjId;

	/**操作前值 */
	@Column(name = "BEFORE_VALUE")
	private String beforeValue="";

	/** 操作后值 */
	@Column(name = "AFTER_VALUE")
	private String afterValue="";

	/** 操作类型 */
	@Column(name = "OPER_FLAG")
	private Long operFlag;

	/** 日志类型 */
	@Column(name = "LOG_TYPE_ID")
	private Long logTypeId;
	
	/** 日志内容*/
	
	@Column(name = "CONTENT")
	private String content="";

	/** 机构 */
	@Column(name = "ORG_ID")
	private String orgId="";

	/** 登录ID */
	@Column(name = "LOGIN_IP")
	private String loginIp="";


}
