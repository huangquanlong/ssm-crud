package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模块信息
 * @author zkl
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="OCRM_F_CI_MODILE")
public class PanelMode implements Serializable{
	
	@Id
	@Column(name="MOD_ID")
	private String modID;
	
	@Column(name="MOD_NAME")
	private String modName;
	
	@Column(name="MOD_ICON")
	private String modIcon;
	
	@Column(name="MOD_TYPE")
	private String modType;
	
	@Column(name="MOD_ACTION")
	private String modAction;
	
	@Column(name="MOD_SWFFILE")
	private String modSwfFile;
	
	@Column(name="MOD_CM")
	private String modCM;
	
	
	public PanelMode(){
		
	}


	public String getModID() {
		return modID;
	}


	public void setModID(String modID) {
		this.modID = modID;
	}


	public String getModName() {
		return modName;
	}


	public void setModName(String modName) {
		this.modName = modName;
	}


	public String getModIcon() {
		return modIcon;
	}


	public void setModIcon(String modIcon) {
		this.modIcon = modIcon;
	}


	public String getModType() {
		return modType;
	}


	public void setModType(String modType) {
		this.modType = modType;
	}


	public String getModAction() {
		return modAction;
	}


	public void setModAction(String modAction) {
		this.modAction = modAction;
	}


	public String getModSwfFile() {
		return modSwfFile;
	}


	public void setModSwfFile(String modSwfFile) {
		this.modSwfFile = modSwfFile;
	}


	public String getModCM() {
		return modCM;
	}


	public void setModCM(String modCM) {
		this.modCM = modCM;
	}

}
