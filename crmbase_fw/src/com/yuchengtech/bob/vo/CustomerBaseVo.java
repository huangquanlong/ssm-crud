package com.yuchengtech.bob.vo;

import java.util.Date;

/**
 * 客户群信息表
 */
public class CustomerBaseVo extends VoBasic{


	private Long id;

	/** 客户群编号 */
	private String customerBaseNumber;

	/** 客户群名称 */
	private String customerBaseName;

	/** 客户群创建日期 */
	private Date customerBaseCreateDate;

	/** 客户群成员数 */
	private int customerBaseMemberNum;

	/** 客户群描述 */
	private String customerBaseDescribe;

    public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerBaseNumber() {
		return this.customerBaseNumber;
	}

	public void setCustomerBaseNumber(String customerBaseNumber) {
		this.customerBaseNumber = customerBaseNumber;
	}

	public String getCustomerBaseName() {
		return this.customerBaseName;
	}

	public void setCustomerBaseName(String customerBaseName) {
		this.customerBaseName = customerBaseName;
	}

	public Date getCustomerBaseCreateDate() {
		return this.customerBaseCreateDate;
	}

	public void setCustomerBaseCreateDate(Date customerBaseCreateDate) {
		this.customerBaseCreateDate = customerBaseCreateDate;
	}

	public int getCustomerBaseMemberNum() {
		return this.customerBaseMemberNum;
	}

	public void setCustomerBaseMemberNum(int customerBaseMemberNum) {
		this.customerBaseMemberNum = customerBaseMemberNum;
	}

	public String getCustomerBaseDescribe() {
		return this.customerBaseDescribe;
	}

	public void setCustomerBaseDescribe(String customerBaseDescribe) {
		this.customerBaseDescribe = customerBaseDescribe;
	}

}
