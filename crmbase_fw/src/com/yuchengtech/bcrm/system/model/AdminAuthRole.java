package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yuchengtech.crm.constance.SystemConstance;

/**
 * 角色信息表
 * @author weilh
 * @since 2012-9-20
 * 
 */
@Entity
@Table(name="ADMIN_AUTH_ROLE")
public class AdminAuthRole implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//角色ID

	@Column(name="ACCOUNT_ID")
	private String accountId;//所属机构代码

	@Column(name="APP_ID")
	private String appId=SystemConstance.LOGIC_SYSTEM_APP_ID;//逻辑系统ID

	@Column(name="ROLE_CODE")
	private String roleCode;//角色代码

	@Column(name="ROLE_NAME")
	private String roleName;//角色名称

	@Column(name="ROLE_TYPE")
	private String roleType;//角色类型
	
	@Column(name="ROLE_LEVEL")
	private String roleLevel;//角色级别

    public AdminAuthRole() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAppId() {
		return this.appId;
	}

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}
}