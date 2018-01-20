package com.yuchengtech.bcrm.workplat.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yuchengtech.bob.model.WorkingplatformNotice;

/**
 * The persistent class for the OCRM_F_WP_NOTICE_ORG database table.
 * 
 * FDM层(基础数据层)，基于工作平台的第二主题，数据为公告接收机构
 */
@Entity
@Table(name = "OCRM_F_WP_NOTICE_ORG")
public class WorkingplatformNoticeOrganizer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 168226633414525594L;

	/**接收id*/
	@Id	
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name = "REC_ID", nullable = false, precision = 22)
	private Long receiveId;

	/** 接收机构 */
	@Column(name = "RECIEVE_ORG", length = 32)
	private String recieveOrganizer;

	@ManyToOne
	@JoinColumn(name = "NOTICE_ID", nullable = false )
	private WorkingplatformNotice noticeId;

	public void setNoticeId(WorkingplatformNotice noticeId) {
		this.noticeId = noticeId;
	}

	public WorkingplatformNotice getNoticeId() {
		return noticeId;
	}

	public void setReceiveId(Long receiveId) {
		this.receiveId = receiveId;
	}

	public Long getReceiveId() {
		return receiveId;
	}

	public void setRecieveOrganizer(String recieveOrganizer) {
		this.recieveOrganizer = recieveOrganizer;
	}

	public String getRecieveOrganizer() {
		return recieveOrganizer;
	}

}