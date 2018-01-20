package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the AUTH_RES_ATTR_DATA database table.
 * @author wz
 * @since 2012-10-17
 */
@Entity
@Table(name="AUTH_RES_ATTR_DATA")
public class AuthResAttrData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//主键ID

	@Column(name="APP_ID")
	private String appId;//逻辑系统ID

	@Column(name="ATTR_CODE")
	private String attrCode;//授权属性编号

	@Column(name="ATTR_ID")
	private Long attrId;//授权属性ID

	@Column(name="OPERATE_KEY")
	private String operateKey;//授权操作集合

	@Column(name="RES_CODE")
	private String resCode;//授权资源编号

	@Column(name="RES_ID")
	private Long resId;//授权资源ID

	private Integer type;//认证类型（0-按授权表配置，1-全有，2-全无）

	private Integer version;//实现Hibernate逻辑锁

    public AuthResAttrData() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAttrCode() {
		return this.attrCode;
	}

	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	public Long getAttrId() {
		return this.attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public String getOperateKey() {
		return this.operateKey;
	}

	public void setOperateKey(String operateKey) {
		this.operateKey = operateKey;
	}

	public String getResCode() {
		return this.resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public long getResId() {
		return this.resId;
	}

	public void setResId(long resId) {
		this.resId = resId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}