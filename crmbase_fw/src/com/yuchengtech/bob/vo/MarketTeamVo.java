package com.yuchengtech.bob.vo;

import java.util.Date;

public class MarketTeamVo extends VoBasic{
	
    private Long marketTeamId;
	
	private Date reateDate;
	
	private String createUser;
	
	private Integer teamNumber;
	
	private Integer teamCustomerNumber;
	
	private String isSmall;
	
	private String marketTeamName;
	
	private String organizationId;
	
	private String teamLeader;

    public Long getMarketTeamId() {
        return marketTeamId;
    }

    public void setMarketTeamId(Long marketTeamId) {
        this.marketTeamId = marketTeamId;
    }

    public Date getReateDate() {
        return reateDate;
    }

    public void setReateDate(Date reateDate) {
        this.reateDate = reateDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getIsSmall() {
        return isSmall;
    }

    public void setIsSmall(String isSmall) {
        this.isSmall = isSmall;
    }

    public String getMarketTeamName() {
        return marketTeamName;
    }

    public void setMarketTeamName(String marketTeamName) {
        this.marketTeamName = marketTeamName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamCustomerNumber(Integer teamCustomerNumber) {
        this.teamCustomerNumber = teamCustomerNumber;
    }

    public Integer getTeamCustomerNumber() {
        return teamCustomerNumber;
    }
}
