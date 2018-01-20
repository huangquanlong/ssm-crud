package com.yuchengtech.crm.dataauth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色与视图关系
 */
@Entity
@Table(name = "OCRM_SYS_VIEW_USER_RELATION")
public class UserViewRelation implements Serializable {

	private static final long serialVersionUID = -3071512732613148823L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	/** 角色id */
	@Column(name = "role_id",length = 30)
	private String roleId;

	/** 视图id*/
	@Column(name = "view_id")
	private Long viewId;

	@Column(name = "if_flag")
	private String ifFlag;
	
	/** 项目id*/
	@Column(name = "proj_id")
	private Long projId;
	
	public Long getProjId() {
		return projId;
	}

	public void setProjId(Long projId) {
		this.projId = projId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Long getViewId() {
		return viewId;
	}

	public void setViewId(Long viewId) {
		this.viewId = viewId;
	}

	public void setIfFlag(String ifFlag) {
		this.ifFlag = ifFlag;
	}

	public String getIfFlag() {
		return ifFlag;
	}


}
