package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;
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
 * 用户修改密码记录表
 * @author wangwan
 * @since 2012-11-06
 */
@Entity
@Table(name="ADMIN_AUTH_PASSWORD_LOG")
public class AdminAuthPasswordLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ADMIN_AUTH_PASSWORD_LOG_ID_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;

    @Temporal( TemporalType.DATE)
	@Column(name="PSWD_UP_TIME")
	private Date pswdUpTime;

	@Column(name="PSWD_UPED")
	private String pswdUped;

	@Column(name="UPDATE_USER")
	private String updateUser;

	@Column(name="USER_ID")
	private String userId;

    public AdminAuthPasswordLog() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPswdUpTime() {
		return this.pswdUpTime;
	}

	public void setPswdUpTime(Date pswdUpTime) {
		this.pswdUpTime = pswdUpTime;
	}

	public String getPswdUped() {
		return this.pswdUped;
	}

	public void setPswdUped(String pswdUped) {
//		this.pswdUped = EndecryptUtils.encrypt(pswdUped);//存储密文至后台数据库
		this.pswdUped=pswdUped;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}