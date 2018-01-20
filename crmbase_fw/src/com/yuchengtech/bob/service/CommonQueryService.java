package com.yuchengtech.bob.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yuchengtech.bob.core.PagingInfo;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.crm.constance.JdbcUtil;

/**
 * @describe Generic query service
 * @author WillJoe 
 */
@Service
public class CommonQueryService {
    
	@Autowired
	@Qualifier("dsOracle")
    private DataSource dsOracle;

    /** Oracle字典映射 */
    private HashMap<String, String> oracleMapping = new HashMap<String, String>();

    private String primaryKey = null;
    
    public Map<String, Object> excuteQuery(String sql, int start, int limit) {
        Connection conn = null;
        Map<String, Object> results = null;
        try {
            int currentPage = start / limit + 1;
            PagingInfo pi = new PagingInfo(limit, currentPage);
            conn = dsOracle.getConnection();
            QueryHelper qh = new QueryHelper(sql, conn, pi);
            if (primaryKey != null) {
                qh.setPrimaryKey(primaryKey);
            }
            if (!oracleMapping.isEmpty()) {
                for(Entry<String, String> item : oracleMapping.entrySet()) {
                    qh.addOracleLookup(item.getKey(), item.getValue());
                }
            }
            results = qh.getJSON();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                	if(!conn.isClosed())
                		conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }       
        return results;
    }
        
    public void addOracleLookup(String columnName, String LookupName) {
        oracleMapping.put(columnName, LookupName);
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    //根据当前登录机构，获取用于控制的机构号：针对非1.2级的机构其他取之上的二级机构
    public Map<String, Object> getUpOrg(String orgId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, Object> results = new HashMap<String, Object>();
        try {
			conn = JdbcUtil.getConnection();
			stmt = conn.createStatement();
			String sql = " select org_id,org_level,org_name from admin_auth_org where org_id in (select up_org_id from admin_auth_org where org_id = "+orgId+" )";
			stmt.executeQuery(sql);
			 rs = stmt.executeQuery(sql);
			 while(rs.next()){
				 if("1".equals(rs.getString("org_level"))||"2".equals(rs.getString("org_level"))){
					 results.put("ID", rs.getString("org_id"));
					 results.put("ORG_NAME", rs.getString("org_name"));
				 }else
					 getUpOrg(rs.getString("org_id"));
			 }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if(conn!=null)
					conn.close();
				
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
        return results;
    }

}
