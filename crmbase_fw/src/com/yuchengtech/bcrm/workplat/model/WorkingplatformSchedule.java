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
 * The persistent class for the OCRM_F_WP_SCHEDULE database table.
 * 
 * FDM(基础数据层)，归属于工作平台的第二主题，数据为日程安排信息
 */
@Entity
@Table(name = "OCRM_F_WP_SCHEDULE")
public class WorkingplatformSchedule implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6667122599737043805L;

    /** 日程id */
    @Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
    @Column(name = "SCHEDULE_ID", nullable = false)
    private Long scheduleId;

    /** 创建机构id */
    @Column(name = "CREATE_ORG", length = 32)
    private String createOrganizer;

    /** 创建人id */
    @Column(length = 30)
    private String creator;

    /** 是否处理 */
    @Column(name = "IS_PROCESS")
    private Boolean isProcess;

    /** 是否提醒 */
    @Column(name = "IS_REMIND")
    private Boolean isRemind;

    /** 是否重复提醒 */
    @Column(name = "IS_REPEAT")
    private Boolean isRepeat;

    /** 关联客户 */
    @Column(name = "RELATION_CUST", length = 30)
    private String relationCust;

    /** 提醒周期_分钟 */
    @Column(name = "REMIND_CYCLE")
    private Long remindCycle;

    /** 提醒结束时间 */
    @Temporal(TemporalType.DATE)
    @Column(name = "REMIND_END_TIME")
    private Date remindEndTime;

    /** 提醒开始时间 */
    @Temporal(TemporalType.DATE)
    @Column(name = "REMIND_STSRT_TIME")
    private Date remindStsrtTime;

    /** 提醒方式 */
    @Column(name = "REMIND_TYPE", length = 20)
    private String remindType;

    /** 工作事项内容 */
    @Column(name = "SCHEDULE_CONTENT", length = 2000)
    private String scheduleContent;

    /** 工作事项标题 */
    @Column(name = "SCHEDULE_TITLE", length = 200)
    private String scheduleTitle;

    /** 开始日期 */
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    /** 开始时间 */
    @Column(name = "START_TIME", length = 20)
    private String startTime;

    /** 结束时间 */
    @Column(name = "END_TIME", length = 20)
    private String endTime;

    /******** add by mxt *********/

    /** 提醒提前天数 */
    @Column(name = "BEFOREHEAD_DAY")
    private Integer beforeheadDay;

    /** 结束日期 */
    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    /** 提醒类别 */
    @Column(name = "REMIND_BELONG", length = 10)
    private String remindBelong;

    /** 创建日期 */
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATE")
    private Date createDate;
    
    /**是否团队日程*/
    @Column(name = "IS_TEAM", length = 1)
    private Long isTeam;
    
    /**团队ID*/
    @Column(name = "MKT_TEAM_ID", length = 20)
    private Long mktTeamId;
    
    
    public Long getMktTeamId() {
		return mktTeamId;
	}

	public void setMktTeamId(Long mktTeamId) {
		this.mktTeamId = mktTeamId;
	}

	public Long getIsTeam() {
		return isTeam;
	}

	public void setIsTeam(Long isTeam) {
		this.isTeam = isTeam;
	}

	public Date getEndDate() {
        return endDate;
    }

    public String getRemindBelong() {
        return remindBelong;
    }

    public void setRemindBelong(String remindBelong) {
        this.remindBelong = remindBelong;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getBeforeheadDay() {
        return beforeheadDay;
    }

    public void setBeforeheadDay(Integer beforeheadDay) {
        this.beforeheadDay = beforeheadDay;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getRelationCust() {
        return this.relationCust;
    }

    public void setRelationCust(String relationCust) {
        this.relationCust = relationCust;
    }

    public Date getRemindEndTime() {
        return this.remindEndTime;
    }

    public void setRemindEndTime(Date remindEndTime) {
        this.remindEndTime = remindEndTime;
    }

    public Date getRemindStsrtTime() {
        return this.remindStsrtTime;
    }

    public void setRemindStsrtTime(Date remindStsrtTime) {
        this.remindStsrtTime = remindStsrtTime;
    }

    public String getRemindType() {
        return this.remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public String getScheduleContent() {
        return this.scheduleContent;
    }

    public void setScheduleContent(String scheduleContent) {
        this.scheduleContent = scheduleContent;
    }

    public String getScheduleTitle() {
        return this.scheduleTitle;
    }

    public void setScheduleTitle(String scheduleTitle) {
        this.scheduleTitle = scheduleTitle;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setIsProcess(Boolean isProcess) {
        this.isProcess = isProcess;
    }

    public Boolean getIsProcess() {
        return isProcess;
    }

    public void setIsRemind(Boolean isRemind) {
        this.isRemind = isRemind;
    }

    public Boolean getIsRemind() {
        return isRemind;
    }

    public void setIsRepeat(Boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public Boolean getIsRepeat() {
        return isRepeat;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setCreateOrganizer(String createOrganizer) {
        this.createOrganizer = createOrganizer;
    }

    public String getCreateOrganizer() {
        return createOrganizer;
    }

    public void setRemindCycle(Long remindCycle) {
        this.remindCycle = remindCycle;
    }

    public Long getRemindCycle() {
        return remindCycle;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

}