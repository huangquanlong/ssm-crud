package com.yuchengtech.bcrm.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**

 */
@Entity
@Table(name="OCRM_F_SYS_FW_FUNCTION_EXT")
public class FwFunctionExt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	private Long id;//模块ID同时也是主键


	@Column(name="HAS_DYNTILE")
	private String hasDynTiyle;//

	@Column(name="TILE_LOGO")
	private String tileLogo;//图标class样式名
	
	@Column(name="TILE_COLOR")
	private String tileColor;

	@Column(name="MODULE_ID")
	private Long moduleId;//模块ID

	@Column(name="DEFAULT_URL")
	private String defaultUrl;//父节点ID

	@Column(name="DEFAULT_SIZE")
	private String defaultSize;//父节点ID
	
	@Column(name="SUPPORT_SIZE_URL")
	private String supportSizeUrl;//父节点ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHasDynTiyle() {
		return hasDynTiyle;
	}

	public void setHasDynTiyle(String hasDynTiyle) {
		this.hasDynTiyle = hasDynTiyle;
	}

	public String getTileLogo() {
		return tileLogo;
	}

	public void setTileLogo(String tileLogo) {
		this.tileLogo = tileLogo;
	}

	public String getTileColor() {
		return tileColor;
	}

	public void setTileColor(String tileColor) {
		this.tileColor = tileColor;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public String getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(String defaultSize) {
		this.defaultSize = defaultSize;
	}

	public String getSupportSizeUrl() {
		return supportSizeUrl;
	}

	public void setSupportSizeUrl(String supportSizeUrl) {
		this.supportSizeUrl = supportSizeUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
   
}