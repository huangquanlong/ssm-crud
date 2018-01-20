package com.yuchengtech.bcrm.workplat.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the OCRM_F_WP_INFO database table.
 * 
 * FDM层(基础数据层)，基于工作平台的第二主题，数据为资讯信息
 */
@Entity
@Table(name = "OCRM_F_WP_INFO")
public class WorkingplatformInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6694647280476234690L;

	/** 资讯ID */
	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "MESSAGE_ID", nullable = false)
	private Long messageId;

	/**父节点Id*/
	@Column(name = "MESSAGE_TYPE", length = 20)
	private String messageType;

	/** 题目 */
	@Column(name = "MESSAGE_TITLE", length = 100)
	private String messageTitle;

	/** 摘要 */
	@Column(name = "MESSAGE_SUMMARY", length = 200)
	private String messageSummary;

	/** 描述 */
	@Column(name = "MESSAGE_INTRODUCE", length = 500)
	private String messageIntroduce;

	/** 产品类型 */
	@Column(name = "PRODUCT_TYPE", length = 100)
	private String productType;

	/** 发布机构名称 */
	@Column(name = "PUBLISH_ORG", length = 100)
	private String publishOrg;

	/** 发布时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "PUBLISH_DATE")
	private Date publishDate;

	/** 发布人名称 */
	@Column(name = "PUBLISH_USER", length = 100)
	private String publishUser;
	

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageSummary() {
		return messageSummary;
	}

	public void setMessageSummary(String messageSummary) {
		this.messageSummary = messageSummary;
	}

	public String getMessageIntroduce() {
		return messageIntroduce;
	}

	public void setMessageIntroduce(String messageIntroduce) {
		this.messageIntroduce = messageIntroduce;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getPublishOrg() {
		return publishOrg;
	}

	public void setPublishOrg(String publishOrg) {
		this.publishOrg = publishOrg;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(String publishUser) {
		this.publishUser = publishUser;
	}

}