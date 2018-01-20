package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 账户信息表
 * @author Weilh
 * @since 2012-09-25
 */
@Entity
@Table(name="ADMIN_AUTH_ACCOUNT_ROLE")
public class AdminAuthAccountRole implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//id

	@Column(name="ACCOUNT_ID")
	private Long accountId;//用户ID

	@Column(name="APP_ID")
	private String appId;//逻辑系统ID

	@Column(name="ROLE_ID")
	private Long roleId;//角色ID

    public AdminAuthAccountRole() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}