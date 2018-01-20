package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_CM_ACCORDION_GROUP database table.
 * @author wz
 * @since 2012-11-7
 */
@Entity
@Table(name="OCRM_F_CM_ACCORDION_GROUP")
public class OcrmFCmAccordionGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private String id;//主键ID
	
	@Column(name="GROUP_NAME")
	private String groupName;//分组名称

	@Column(name="GROUP_ID")
	private String groupId;//分组ID

	@Column(name="USER_ID")
	private String userId;//用户ID

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}