package com.yuchengtech.bob.vo;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author WillJoe
 *
 */
public class WorkingplatformNoticeVo extends VoBasic{
    
    private Long noticeId;
    
    private List<WorkingplatformNoticeReadVo> noticeIdL;
 
    private List<WorkingplatformNoticeOrganizerVo> noticeIdO;

    private String noticeTitle;

    private String noticeContent;

    private String noticeLevel;

    private String publisher;

    private String publishOrganizer;

    private String isTop;

    private Date publishTime;

    private String moduleType;

    private String published;

    private Date activeDate;
    
    private Date topActiveDate;
    
    private String noticeType;
    
    private String isRead;

    private Date publishTimeEnd;
    
    private String creator;
    
    public Date getPublishTimeEnd() {
        return publishTimeEnd;
    }

    public void setPublishTimeEnd(Date publishTimeEnd) {
        this.publishTimeEnd = publishTimeEnd;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public List<WorkingplatformNoticeReadVo> getNoticeIdL() {
        return noticeIdL;
    }

    public void setNoticeIdL(List<WorkingplatformNoticeReadVo> noticeIdL) {
        this.noticeIdL = noticeIdL;
    }

    public List<WorkingplatformNoticeOrganizerVo> getNoticeIdO() {
        return noticeIdO;
    }

    public void setNoticeIdO(List<WorkingplatformNoticeOrganizerVo> noticeIdO) {
        this.noticeIdO = noticeIdO;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeLevel() {
        return noticeLevel;
    }

    public void setNoticeLevel(String noticeLevel) {
        this.noticeLevel = noticeLevel;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishOrganizer() {
        return publishOrganizer;
    }

    public void setPublishOrganizer(String publishOrganizer) {
        this.publishOrganizer = publishOrganizer;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public Date getTopActiveDate() {
        return topActiveDate;
    }

    public void setTopActiveDate(Date topActiveDate) {
        this.topActiveDate = topActiveDate;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    
}
