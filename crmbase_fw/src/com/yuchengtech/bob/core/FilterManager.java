package com.yuchengtech.bob.core;

import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class FilterManager {
    
    private static FilterManager instance;
    
    private static Logger log = Logger.getLogger(StartupPathListener.class);
    
    private DataSource dsOracle;
    
    private ConcurrentHashMap<String, String> filterItem;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> filters;

    private ApplicationContext applicationContext;
    private FilterManager() {
        // Exists only to defeat instantiation.       
        //applicationContext = new ClassPathXmlApplicationContext("applicationContext-dataSource.xml ");
        //dsOracle = (DataSource) applicationContext.getBean("dsOracle");
        filters = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
    }

    public static synchronized FilterManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new FilterManager();
        }
        return instance;
    }
    
    private int loadFilters() {
        String SQL = "SELECT CLASS_NAME, ROLE_ID, SQL_STRING FROM SYS_FILTER";
        int rowsCached = 0;
//        try {
//            ResultSet rs;
//            PreparedStatement pstmt = dsOracle.getConnection().prepareStatement(SQL);
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                addFilterItem(rs.getString("CLASS_NAME"), rs.getString("ROLE_ID"), rs.getString("SQL_STRING"));
//                rowsCached++;
//            }
//            rs.close();
//            rs = null;
//        } catch (SQLException e) {
//            log.error("加载Filters时发生异常:", e);
//            e.printStackTrace();
//        }
        return rowsCached;
    }

    private void addFilterItem(String className, String roleId, String SQL) {
        filterItem = filters.get(className);
        if (filterItem == null) {
            filterItem = new ConcurrentHashMap<String, String>();
            filters.put(className, filterItem);
        }
        filterItem.put(roleId, SQL);
    }
    
    public String getFilterSQL(String className, String roleId) {
        String SQL = null;
        filterItem = filters.get(className);
        if (filterItem != null) {
            SQL = filterItem.get(roleId);
        }
        return SQL;
    }
    
    public void initialize(ApplicationContext applicationContext) {
    	this.applicationContext = applicationContext;
        dsOracle = (DataSource) applicationContext.getBean("dsOracle");
        filters = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
        log.info("开始加载Filters……");
        int count = loadFilters();
        log.info("完成加载Filters，共计：" + count);
    }
        
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
