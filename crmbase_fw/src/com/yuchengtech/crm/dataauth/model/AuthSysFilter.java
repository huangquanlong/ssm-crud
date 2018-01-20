package com.yuchengtech.crm.dataauth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the AUTH_SYS_FILTER database table.
 * 
 */
@Entity
@Table(name="AUTH_SYS_FILTER")
public class AuthSysFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private Long id;//主键约束
	
	@Column(name="CLASS_NAME", nullable=false, length=200)
	private String className;//功能点action类名
	
	@Column(nullable=false, length=2000)
	private String describetion;//过滤器作用描述
	
	@Column(name="METHOD_NAME", nullable=false, length=200)
	private Integer methodName;//所访问action方法名
	
	@Column(name="ROLE_ID", nullable=false, length=40)
	private String roleId;//所控制角色ID
	
	@Column(name="SQL_STRING", nullable=false, length=2000)
	private String sqlString;//数据过滤SQL条件

    public AuthSysFilter() {
    }
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescribetion() {
		return this.describetion;
	}

	public void setDescribetion(String describetion) {
		this.describetion = describetion;
	}
	
	public Integer getMethodName() {
		return this.methodName;
	}

	public void setMethodName(Integer methodName) {
		this.methodName = methodName;
	}
	
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getSqlString() {
		return this.sqlString;
	}

	public void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}

}