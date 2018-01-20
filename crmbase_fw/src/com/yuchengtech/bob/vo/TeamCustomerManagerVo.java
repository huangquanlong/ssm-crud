package com.yuchengtech.bob.vo;

import java.util.Date;

public class TeamCustomerManagerVo {

    private Long id;

    private Long marketTeamId;

    private Date joinDate;

    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarketTeamId() {
        return marketTeamId;
    }

    public void setMarketTeamId(Long marketTeamId) {
        this.marketTeamId = marketTeamId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
