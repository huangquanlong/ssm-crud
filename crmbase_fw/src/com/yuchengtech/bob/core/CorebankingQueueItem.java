package com.yuchengtech.bob.core;

public class CorebankingQueueItem {
    
    private Long id;
    
    private String account;
    
    private String userId;
    
    private String authUnits;
    
    public CorebankingQueueItem(Long id, String account, String userId,
            String authUnits) {
        this.id = id;
        this.account = account;
        this.userId = userId;
        this.authUnits = authUnits;
    }

    public Long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthUnits() {
        return authUnits;
    }

}
