package com.yuchengtech.bob.vo;

import java.util.Date;

public class WorkingplatformNoticeReadVo extends VoBasic{

    private Long id;

  //  private WorkPlatNoticeVo noticeId;
    private Long noticeId;

    public Long getNoticeId() {
        return noticeId;
    }
 
    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    private String userId;
    
    private String functionType;

    private Boolean isLoad;

    private Date readTime;

    private Date loadTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public WorkPlatNoticeVo getNoticeId() {
//        return noticeId;
//    }
//
//    public void setNoticeId(WorkPlatNoticeVo noticeId) {
//        this.noticeId = noticeId;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public Boolean getIsLoad() {
        return isLoad;
    }

    public void setIsLoad(Boolean isLoad) {
        this.isLoad = isLoad;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }
    
    
}
