package com.yuchengtech.bob.vo;

import java.util.Date;

/**
 * 客户摘要信息
 */
public class CustomerDescriptionVo extends VoBasic{

    private Long id;

	private Date crmDate;// 平台日期

	private String customerEnglishName;// 客户英文名称

	private String customerId;// 客户编号

	private String customerLevel;// 客户级别

	private String customerType;// 客户类型

	private String customerChineseName;// 客户（中文）名称

	private String customerOrgNo;// 组织机构代码
	

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCrmDate() {
		return crmDate;
	}

	public void setCrmDate(Date crmDate) {
		this.crmDate = crmDate;
	}

	public String getCustomerEnglishName() {
		return customerEnglishName;
	}

	public void setCustomerEnglishName(String customerEnglishName) {
		this.customerEnglishName = customerEnglishName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerChineseName() {
		return customerChineseName;
	}

	public void setCustomerChineseName(String customerChineseName) {
		this.customerChineseName = customerChineseName;
	}

	public String getCustomerOrgNo() {
		return customerOrgNo;
	}

	public void setCustomerOrgNo(String customerOrgNo) {
		this.customerOrgNo = customerOrgNo;
	}


}