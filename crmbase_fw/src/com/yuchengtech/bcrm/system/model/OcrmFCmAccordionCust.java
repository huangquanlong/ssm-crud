package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the OCRM_F_CM_ACCORDION_CUST database table.
 * @author wz
 * @since 2012-11-7
 */
@Entity
@Table(name="OCRM_F_CM_ACCORDION_CUST")
public class OcrmFCmAccordionCust implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//主键ID

	@Column(name="CUST_ID")
	private String custId;//客户ID

	@Column(name="GROUP_ID")
	private String groupId;//所属分组ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


}