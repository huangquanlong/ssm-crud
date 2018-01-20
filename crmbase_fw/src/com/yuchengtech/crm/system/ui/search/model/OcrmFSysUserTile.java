package com.yuchengtech.crm.system.ui.search.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "OCRM_F_SYS_USER_TILE")
public class OcrmFSysUserTile {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "RES_ID")
	private Long resId;
	
	@Column(name = "MODULE_ID")
	private Long moduleId;
	
	@Column(name = "GROUP_SEQ")
	private Integer groupSeq;
	
	@Column(name = "TILE_SIZE")
	private String tileSize;
	
	@Column(name = "POS_X")
	private Integer posX;
	
	@Column(name = "POS_Y")
	private Integer posY;
	
	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.DATE)
	private Date updateDate;
	
	@Column(name = "TILE_ICON")
	private String tileIcon;
	
	@Column(name = "TILE_COLOR")
	private String tileColor;
	
	@Column(name = "SPARE_ONE")
	private String spareOne;
	
	@Column(name = "SPARE_TWO")
	private String spareTwo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getResId() {
		return resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getGroupSeq() {
		return groupSeq;
	}

	public void setGroupSeq(Integer groupSeq) {
		this.groupSeq = groupSeq;
	}

	public String getTileSize() {
		return tileSize;
	}

	public void setTileSize(String tileSize) {
		this.tileSize = tileSize;
	}

	public Integer getPosX() {
		return posX;
	}

	public void setPosX(Integer posX) {
		this.posX = posX;
	}

	public Integer getPosY() {
		return posY;
	}

	public void setPosY(Integer posY) {
		this.posY = posY;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getTileIcon() {
		return tileIcon;
	}

	public void setTileIcon(String tileIcon) {
		this.tileIcon = tileIcon;
	}

	public String getTileColor() {
		return tileColor;
	}

	public void setTileColor(String tileColor) {
		this.tileColor = tileColor;
	}

	public String getSpareOne() {
		return spareOne;
	}

	public void setSpareOne(String spareOne) {
		this.spareOne = spareOne;
	}

	public String getSpareTwo() {
		return spareTwo;
	}

	public void setSpareTwo(String spareTwo) {
		this.spareTwo = spareTwo;
	}

}
