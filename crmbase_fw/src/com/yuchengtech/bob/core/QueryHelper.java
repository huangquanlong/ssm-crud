package com.yuchengtech.bob.core;


import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.yuchengtech.crm.constance.SystemConstance;

public class QueryHelper {
	private static Logger log = Logger.getLogger(QueryHelper.class);
    /** 主键字段 */
    private String primaryKey = "ID";
    /** 原始SQL */
    private String originSQL;
    /** db2 withSQL 需要在select前*/
    private String withSQL;
    /** 加上分页的SQL */
    private String pagingSQL;
    /** 数据库连接 */
    private Connection connection;
    /** 分页信息类 */
    private PagingInfo paging;
    /** Oracle字典映射 */
    private ConcurrentHashMap<String, String> oracleMapping = new ConcurrentHashMap<String, String>();
   
    LookupManager manager = LookupManager.getInstance();
    
    protected String prepareSQL(String SQL) {

        StringBuilder builder = new StringBuilder(SQL);
        if(paging != null) {
          	//builder.insert(0, "SELECT *  FROM (SELECT ROW_NUMBER () OVER (ORDER BY 1) AS RN, BUSINESS_QUERY.* FROM (");
          	//if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
          	//	builder.insert(0, withSQL);
          	//}
            builder.append(" limit " + paging.getBeginRowNumber());
            builder.append(" , " + paging.getPageSize());
            log.info("JDBC分页查询语句："+builder.toString());
        }
        return builder.toString();
    }
    
    protected void processLookup(HashMap<String, Object> map, String name, Object code) {
        if (oracleMapping.containsKey(name)) {
            String lookupName = oracleMapping.get(name);
            ConcurrentHashMap<String, String> lookup = manager.getOracleValues(lookupName);
            String key = code.toString();
            if (lookup != null && lookup.containsKey(key)) {
                map.put(name + "_ORA", lookup.get(key));
            } else {
                map.put(name + "_ORA", code);
            }                         
        }        
    }
    
    public void addOracleLookup(String columnName, String LookupName) {
        oracleMapping.put(columnName, LookupName);
    }
   
    public QueryHelper(String SQL, Connection connection) {
        this.originSQL = SQL;
        this.connection = connection;
    }
    
    public QueryHelper(String SQL, Connection connection, PagingInfo paging) {
        this.paging = paging;
        this.originSQL = SQL;
        this.connection = connection;
    }

    public QueryHelper(String SQL, String withSQL, Connection connection, PagingInfo paging) {
        this.paging = paging;
        this.originSQL = SQL;
        this.withSQL = withSQL;
        this.connection = connection;
    }

    public ResultSet executeQuery() throws SQLException {
    	pagingSQL = prepareSQL(originSQL);
    	Statement stmt = connection.createStatement();
	    ResultSet   rs = stmt.executeQuery(pagingSQL);
        return rs;
    }
    
    public Map<String, Object> getJSON() throws SQLException {
        pagingSQL = prepareSQL(originSQL);
        log.info("QueryHelper.getJSON:originSQL:"+originSQL);
        log.info("QueryHelper.getJSON:pagingSQL:"+pagingSQL);
        ResultSet rs = null;
        Statement stmt = null;
        Map<String, Object> result = new HashMap<String, Object>();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(pagingSQL);
	        ResultSetMetaData metaData = rs.getMetaData();
	        int columnCount = metaData.getColumnCount();
	       
	        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
	        while (rs.next()) {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            for (int i = 0; i < columnCount; i++) {
	                String columnName = metaData.getColumnLabel(i + 1);
	                columnName=columnName.toUpperCase();
	                if ("RN".equals(columnName.toUpperCase())) {
	                    continue;
	                }
	                if(rs.getObject(columnName) != null) {
	                    if (rs.getObject(columnName) instanceof Clob) {
	                    	Clob clob = rs.getClob(columnName);
	                    	String value = clob.getSubString((long) 1, (int) clob.length());
	                    	map.put(columnName, value); 
	                    } else if (rs.getObject(columnName) instanceof Blob) {
	                    	Blob blob = rs.getBlob(columnName);
	                    	long len = blob.length();
	                        byte [] data = blob.getBytes(1,(int) len);
	                    	map.put(columnName, data); 
	                    }  else {
	                    	String value = rs.getObject(columnName).toString();
	                    	map.put(columnName, value); 
	                    }
	                       
	                    processLookup(map, columnName, rs.getObject(columnName));
	                } else {
	                    map.put(columnName, "");
	                }
	            }
	            rowsList.add(map);
	        }
	        result.put("data", rowsList);
	        
	        if (paging != null) {
	            StringBuilder builder = new StringBuilder(originSQL);
	            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
	            if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
	          		builder.insert(0, withSQL);
	          	}
	            builder.append(") SUB_QUERY");
	            rs = stmt.executeQuery(builder.toString());
	            if(rs.next()) {
	                result.put("count", rs.getInt("TOTAL"));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if(rs!=null){
					rs.close();
				}if(stmt!=null){
					stmt.close();
				}if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				log.error("关闭数据连接异常:", e);
				e.printStackTrace();
			}
		}
        return result;       
    }
    
    public Map<String, Object> getJSON(boolean needTrans) throws SQLException {
    	if(needTrans){
    		ResultSet rs = null;
    		Statement stmt = null;
    		Map<String, Object> result = new HashMap<String, Object>();
    		try {
    			pagingSQL = prepareSQL(originSQL);
    	        stmt = connection.createStatement();
    	        rs = stmt.executeQuery(pagingSQL);
    	        ResultSetMetaData metaData = rs.getMetaData();
    	        int columnCount = metaData.getColumnCount();
    	        
    	        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
    	        while (rs.next()) {
    	            HashMap<String, Object> map = new HashMap<String, Object>();
    	            for (int i = 0; i < columnCount; i++) {
    	                String columnName = metaData.getColumnName(i + 1);
    	                if ("RN".equals(columnName.toUpperCase())) {
    	                    continue;
    	                }
    	                if(rs.getObject(columnName) != null) {
    	                	if (rs.getObject(columnName) instanceof Clob) {
    	                    	Clob clob = rs.getClob(columnName);
    	                    	String value = clob.getSubString((long) 1, (int) clob.length());
    	                    	map.put(ColumnNameUtil.getModelField(columnName), value); 
    	                    } else if (rs.getObject(columnName) instanceof Blob) {
    	                    	Blob blob = rs.getBlob(columnName);
    	                    	long len = blob.length();
    	                        byte [] data = blob.getBytes(1,(int) len);
    	                    	map.put(ColumnNameUtil.getModelField(columnName), data); 
    	                    }  else {
    	                    	String value = rs.getObject(columnName).toString();
    	                    	map.put(ColumnNameUtil.getModelField(columnName), value); 
    	                    }
    	                    processLookup(map, ColumnNameUtil.getModelField(columnName), rs.getObject(columnName));
    	                } else {
    	                    map.put(ColumnNameUtil.getModelField(columnName), "");
    	                }
    	            }
    	            rowsList.add(map);
    	        }
    	        result.put("data", rowsList);
    	        
    	        if (paging != null) {
    	            StringBuilder builder = new StringBuilder(originSQL);
    	            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
    	            if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
    	          		builder.insert(0, withSQL);
    	          	}
    	            builder.append(") SUB_QUERY");
    	            rs = stmt.executeQuery(builder.toString());
    	            if(rs.next()) {
    	                result.put("count", rs.getInt("TOTAL"));
    	            }
    	        }
	    	} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				try {
					if(rs!=null){
						rs.close();
					}if(stmt!=null){
						stmt.close();
					}if(connection!=null){
						connection.close();
					}
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
    		return result;  
    	}else{
    		return this.getJSON();
    	}
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

}
