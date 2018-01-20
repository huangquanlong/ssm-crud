package com.yuchengtech.bob.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

/**
 * @describe The basic class of Action.
 * @author WillJoe
 */
public class BaseAction {
    
    private Map<String,Object> json = new HashMap<String,Object>(); 
    private int start = 0;
    private int limit = 100;
    private String condition;
    
    public Map<String, Object> getJson() {
        return json;
    }

    public void setJson(Map<String, Object> json) {
        this.json = json;
    }

    @SuppressWarnings("unchecked")
    public void setJson(String jSON){        
        try {
            this.json = (Map<String, Object>) JSONUtil.deserialize(jSON);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
    
    public void setStart(String start){
        this.start = Integer.valueOf(start);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public void setLimit(String limit){
        this.limit = Integer.valueOf(limit);
    }

    public String getCondition() {
        return condition;
    }

    @SuppressWarnings("unchecked")
    public void setCondition(String condition) {
        try {
            this.json = (Map<String, Object>) JSONUtil.deserialize(condition);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.condition = condition;
    }
    
}
