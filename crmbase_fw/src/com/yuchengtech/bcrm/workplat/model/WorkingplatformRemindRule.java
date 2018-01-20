package com.yuchengtech.bcrm.workplat.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the OCRM_F_WP_REMIND_RULE database table.
 * 
 * FDM(基础数据层)，归属于工作平台的第二主题，数据为自动提醒规则信息
 */
@Entity
@Table(name = "OCRM_F_WP_REMIND_RULE")
public class WorkingplatformRemindRule implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
	@Column(name="RULE_ID")
	private Long RULE_ID;
	
	

    /** 提醒提前天数/睡眠天数等 */
    @Column(name = "BEFOREHEAD_DAY")
    private Integer BEFOREHEAD_DAY;

    /** 创建机构id */
    @Column(name = "CREATE_ORG", length = 32)
    private String CREATE_ORG;

    /** 创建机构名称 */
    @Column(name = "CREATE_ORG_NAME", length = 100)
    private String CREATE_ORG_NAME;

    /** 创建时间 */
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME")
    private Date CREATE_TIME;

    /** 创建人id */
    @Column(name = "CREATOR",length = 30)
    private String CREATOR;

    /** 变动余额 */
    @Column(name = "CHANGE_AMOUNT", precision = 24, scale = 6)
    private BigDecimal CHANGE_AMOUNT;

    /** 创建人名称 */
    @Column(name = "CREATOR_NAME", length = 200)
    private String CREATOR_NAME;

    /** 提醒频率-默认：每天 */
    @Column(name = "CYCLE_TIME")
    private Long CYCLE_TIME;

    /** 提醒方式 */
    @Column(name = "REMIND_MODE", length = 20)
    private String REMIND_MODE;

    /** 生效截止时间（暂未使用） */
    @Temporal(TemporalType.DATE)
    @Column(name = "REMIND_TIME")
    private Date REMIND_TIME;

    /** 提醒类别（暂未使用） */
    @Column(name = "REMIND_TYPE", length = 20)
    private String REMIND_TYPE;

    /** 规则名称（暂未使用）*/
    @Column(name = "RULE_NAME", length = 100)
    private String RULE_NAME;

    /** 事件类型 */
    @Column(name = "SECTION_TYPE", length = 20)
    private String SECTION_TYPE;

    /** 提醒持续天数*/
    @Column(name = "THRESHHOLD", precision = 24, scale = 6)
    private Integer THRESHHOLD;
    
    /** 是否客户经理 */
    @Column(name = "IS_CUST_MGR",length=20)
    private String IS_CUST_MGR;

    /** 提醒角色 */
    @Column(name = "RULE_ROLE",length=20)
    private String RULE_ROLE;
    
    /** 是否启用 */
    @Column(name = "IS_VALID",length=20)
    private String IS_VALID;
    
    /** 基线客户层级 */
    @Column(name = "BASE_CUS_LEVEL",length=20)
    private String BASE_CUS_LEVEL;
    
    
    

    public long getRULE_ID() {
		return RULE_ID;
	}




	public void setRULE_ID(long rULE_ID) {
		this.RULE_ID = rULE_ID;
	}




	public Integer getBEFOREHEAD_DAY() {
		return BEFOREHEAD_DAY;
	}




	public void setBEFOREHEAD_DAY(Integer bEFOREHEAD_DAY) {
		this.BEFOREHEAD_DAY = bEFOREHEAD_DAY;
	}




	public String getCREATE_ORG() {
		return CREATE_ORG;
	}




	public void setCREATE_ORG(String cREATE_ORG) {
		this.CREATE_ORG = cREATE_ORG;
	}




	public String getCREATE_ORG_NAME() {
		return CREATE_ORG_NAME;
	}




	public void setCREATE_ORG_NAME(String cREATE_ORG_NAME) {
		this.CREATE_ORG_NAME = cREATE_ORG_NAME;
	}




	public Date getCREATE_TIME() {
		return CREATE_TIME;
	}




	public void setCREATE_TIME(Date cREATE_TIME) {
		this.CREATE_TIME = cREATE_TIME;
	}




	public String getCREATOR() {
		return CREATOR;
	}




	public void setCREATOR(String cREATOR) {
		this.CREATOR = cREATOR;
	}




	public BigDecimal getCHANGE_AMOUNT() {
		return CHANGE_AMOUNT;
	}




	public void setCHANGE_AMOUNT(BigDecimal cHANGE_AMOUNT) {
		this.CHANGE_AMOUNT = cHANGE_AMOUNT;
	}




	public String getCREATOR_NAME() {
		return CREATOR_NAME;
	}




	public void setCREATOR_NAME(String cREATOR_NAME) {
		this.CREATOR_NAME = cREATOR_NAME;
	}




	public Long getCYCLE_TIME() {
		return CYCLE_TIME;
	}




	public void setCYCLE_TIME(Long cYCLE_TIME) {
		this.CYCLE_TIME = cYCLE_TIME;
	}




	public String getREMIND_MODE() {
		return REMIND_MODE;
	}




	public void setREMIND_MODE(String rEMIND_MODE) {
		this.REMIND_MODE = rEMIND_MODE;
	}




	public Date getREMIND_TIME() {
		return REMIND_TIME;
	}




	public void setREMIND_TIME(Date rEMIND_TIME) {
		this.REMIND_TIME = rEMIND_TIME;
	}




	public String getREMIND_TYPE() {
		return REMIND_TYPE;
	}




	public void setREMIND_TYPE(String rEMIND_TYPE) {
		this.REMIND_TYPE = rEMIND_TYPE;
	}




	public String getRULE_NAME() {
		return RULE_NAME;
	}




	public void setRULE_NAME(String rULE_NAME) {
		this.RULE_NAME = rULE_NAME;
	}




	public String getSECTION_TYPE() {
		return SECTION_TYPE;
	}




	public void setSECTION_TYPE(String sECTION_TYPE) {
		this.SECTION_TYPE = sECTION_TYPE;
	}




	public Integer getTHRESHHOLD() {
		return THRESHHOLD;
	}




	public void setTHRESHHOLD(Integer tHRESHHOLD) {
		this.THRESHHOLD = tHRESHHOLD;
	}




	public String getIS_CUST_MGR() {
		return IS_CUST_MGR;
	}




	public void setIS_CUST_MGR(String iS_CUST_MGR) {
		this.IS_CUST_MGR = iS_CUST_MGR;
	}




	public String getRULE_ROLE() {
		return RULE_ROLE;
	}




	public void setRULE_ROLE(String rULE_ROLE) {
		this.RULE_ROLE = rULE_ROLE;
	}




	public String getIS_VALID() {
		return IS_VALID;
	}




	public void setIS_VALID(String iS_VALID) {
		this.IS_VALID = iS_VALID;
	}




	public String getBASE_CUS_LEVEL() {
		return BASE_CUS_LEVEL;
	}




	public void setBASE_CUS_LEVEL(String bASE_CUS_LEVEL) {
		this.BASE_CUS_LEVEL = bASE_CUS_LEVEL;
	}




	public static long getSerialversionuid() {
        return serialVersionUID;
    }

}