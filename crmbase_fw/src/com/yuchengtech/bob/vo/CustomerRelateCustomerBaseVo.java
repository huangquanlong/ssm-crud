package com.yuchengtech.bob.vo;

import java.util.Date;

/**
 * 客户归属客户群表
 */
public class CustomerRelateCustomerBaseVo extends VoBasic{

	private Long id;

	/** 创建人 */
	private String relationCreateName;

	/** 创建日期 */
	private Date relationCreateDate;



	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getRelationCreateName() {
		return this.relationCreateName;
	}

	public void setRelationCreateName(String relationCreateName) {
		this.relationCreateName = relationCreateName;
	}

	public Date getRelationCreateDate() {
		return this.relationCreateDate;
	}

	public void setRelationCreateDate(Date relationCreateDate) {
		this.relationCreateDate = relationCreateDate;
	}






}
