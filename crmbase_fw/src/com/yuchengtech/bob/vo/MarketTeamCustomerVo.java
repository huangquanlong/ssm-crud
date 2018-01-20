package com.yuchengtech.bob.vo;

import java.util.Date;

public class MarketTeamCustomerVo {

    private Long id;

    private Long marketTeamId;

    private String custId;

    private Date joinDate;

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

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

}
