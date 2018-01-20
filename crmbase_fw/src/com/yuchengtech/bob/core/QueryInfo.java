package com.yuchengtech.bob.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryInfo {
    
    String querySql = "";
    
    String primaryKey = "ID";
    
    List<Map<String,String>> mappingField = new ArrayList<Map<String,String>>();
    
    public QueryInfo(){}
    
    public QueryInfo(String sql){
        this.querySql = sql;
    }
    
    public QueryInfo(String sql, String primaryKey){
        this.querySql = sql;
        this.primaryKey = primaryKey;
    }
    
    public void addOracleLookup(String columnName, String mappingType){
        Map<String,String> tmpMap  = new HashMap<String,String>();
        tmpMap.put("columnName", columnName);
        tmpMap.put("mappingType", mappingType);
        mappingField.add(tmpMap);
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<Map<String, String>> getMappingField() {
        return mappingField;
    }

    public void setMappingField(List<Map<String, String>> mappingField) {
        this.mappingField = mappingField;
    }
    
    
    public boolean equals(QueryInfo qi){
        if(querySql.equalsIgnoreCase(qi.querySql)){
            return true;
        }else return false;
    }
    
}
