package com.yuchengtech.crm.homePage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_AUTH_ACCOUNT_CFG")
public class AdminAuthAccountCfg implements Serializable {
	
	@Id
	@SequenceGenerator(name="AdminAuthAccountCfg_GENERATOR", sequenceName="ID_SEQUENCE" ,allocationSize = 1)
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;

	@Column(name="APP_ID")
	private String appId;
	
	@Column(name="USER_ID")
	private String userId;

	@Column(name="THEME_ID")
	private String themeId;

	@Column(name="STYLE_ID")
	private String styleId;
	
	@Column(name="COLOR_ID")
	private String colorId;

	@Column(name="TEMP1")
	private String temp1;

	@Column(name="TEMP2")
	private String temp2;

	@Column(name="TEMP3")
	private String temp3;

	@Column(name="TEMP4")
	private String temp4;

	@Column(name="TEMP5")
	private String temp5;

	@Column(name="TEMP6")
	private String temp6;

	@Column(name="TEMP7")
	private String temp7;
	
	@Column(name="TEMP8")
	private String temp8;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getTemp3() {
		return temp3;
	}

	public void setTemp3(String temp3) {
		this.temp3 = temp3;
	}

	public String getTemp4() {
		return temp4;
	}

	public void setTemp4(String temp4) {
		this.temp4 = temp4;
	}

	public String getTemp5() {
		return temp5;
	}

	public void setTemp5(String temp5) {
		this.temp5 = temp5;
	}

	public String getTemp6() {
		return temp6;
	}

	public void setTemp6(String temp6) {
		this.temp6 = temp6;
	}

	public String getTemp7() {
		return temp7;
	}

	public void setTemp7(String temp7) {
		this.temp7 = temp7;
	}

	public String getTemp8() {
		return temp8;
	}

	public void setTemp8(String temp8) {
		this.temp8 = temp8;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	
}
