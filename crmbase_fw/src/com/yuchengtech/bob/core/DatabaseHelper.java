package com.yuchengtech.bob.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import com.yuchengtech.crm.constance.SystemConstance;

public class DatabaseHelper {
	
	private String databaseProductName;
	
	private DataSource dataSource;
	
	public DatabaseHelper(DataSource dataSource) throws Exception {
		this.dataSource = dataSource;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData meta =	conn.getMetaData();
			databaseProductName = meta.getDatabaseProductName();
		} finally {
			conn.close();
		}
	}
	
	private int getValue(String sequenceName) throws Exception {
		int value = 0;
		StringBuilder builder=null;
		if("DB2".equals(SystemConstance.DB_TYPE))
			builder = new StringBuilder("SELECT  nextval for id_sequence from SYSIBM.SYSDUMMY1"); //db2
		else if("ORACLE".equals(SystemConstance.DB_TYPE))
			builder = new StringBuilder("SELECT  id_sequence.NEXTVAL FROM DUAL"); //oracle
		
		else if ("MYSQL".equals(SystemConstance.DB_TYPE)){
			builder = new StringBuilder("SELECT nextval('id_sequence') ");
		}
			
//		if ("Oracle".equals(databaseProductName)) {
//			builder.append(sequenceName);
//			builder.append(".NEXTVAL FROM DUAL");
//		} else if ("PostgreSQL".equals(databaseProductName)) {
//			builder.append("NEXTVAL('");
//			builder.append(sequenceName);
//			builder.append("')");
//		}
//		else if("DB2/LINUXX8664".equals(databaseProductName)){
//			
//			builder.append("NEXTVAL FOR ");
//			builder.append(sequenceName);
//			builder.append(" FROM DUAL");
//		}
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(builder.toString());
			if (rs.next()) {
				value = rs.getInt(1);
			}
		} finally {
			conn.close();
		}
		return value;
	}
	
	public String getProductName() {
		return databaseProductName;
	}
	
	public int getNextValue(String sequenceName) {
		int value = 0;
		try {
			value = getValue(sequenceName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
