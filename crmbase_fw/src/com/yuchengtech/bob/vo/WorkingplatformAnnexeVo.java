package com.yuchengtech.bob.vo;

import java.math.BigDecimal;
import java.util.Date;

public class WorkingplatformAnnexeVo extends VoBasic{
    
    private Long annexeId;

    private String annexeName;

    private String annexeServerName;

    private BigDecimal annexeSize;

    private String annexeType;

    private String clientName;

    private Date createTime;
    
    private Date lastLoadTime;

    private String lastLoader;

    private Long loadCount;

    private String physicalAddress;

    private String relationInfo;

    private String relationMod;
    
    public String getRelationMod() {
        return relationMod;
    }

    public void setRelationMod(String relationMod) {
        this.relationMod = relationMod;
    }

    public Long getAnnexeId() {
        return annexeId;
    }

    public void setAnnexeId(Long annexeId) {
        this.annexeId = annexeId;
    }

    public String getAnnexeName() {
        return annexeName;
    }

    public void setAnnexeName(String annexeName) {
        this.annexeName = annexeName;
    }

    public String getAnnexeServerName() {
        return annexeServerName;
    }

    public void setAnnexeServerName(String annexeServerName) {
        this.annexeServerName = annexeServerName;
    }

    public BigDecimal getAnnexeSize() {
        return annexeSize;
    }

    public void setAnnexeSize(BigDecimal annexeSize) {
        this.annexeSize = annexeSize;
    }

    public String getAnnexeType() {
        return annexeType;
    }

    public void setAnnexeType(String annexeType) {
        this.annexeType = annexeType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoadTime() {
        return lastLoadTime;
    }

    public void setLastLoadTime(Date lastLoadTime) {
        this.lastLoadTime = lastLoadTime;
    }

    public String getLastLoader() {
        return lastLoader;
    }

    public void setLastLoader(String lastLoader) {
        this.lastLoader = lastLoader;
    }

    public Long getLoadCount() {
        return loadCount;
    }

    public void setLoadCount(Long loadCount) {
        this.loadCount = loadCount;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getRelationInfo() {
        return relationInfo;
    }

    public void setRelationInfo(String relationInfo) {
        this.relationInfo = relationInfo;
    }
    
    
}
