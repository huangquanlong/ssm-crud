package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="OCRM_F_CI_TABGENERATOR")
public class PreFix implements Serializable{  
	
	@Id
	@Column(name="PREFIX")
	private String preFix;
	
	@Column(name="EXTENT")
	private Integer extent;
	
	@Column(name="LASTNUM")
	private Integer lastNum;

	public PreFix() {
	}

	public String getPreFix() {
		return preFix;
	}

	public void setPreFix(String preFix) {
		this.preFix = preFix;
	}

	public Integer getExtent() {
		return extent;
	}

	public void setExtent(Integer extent) {
		this.extent = extent;
	}

	public Integer getLastNum() {
		return lastNum;
	}

	public void setLastNum(Integer lastNum) {
		this.lastNum = lastNum;
	}

}
