package com.yuchengtech.crm.constance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.core.LookupManager;
import com.yuchengtech.crm.exception.BipExceptionController;

public final class JdbcUtil {
	private static Logger log = Logger.getLogger(BipExceptionController.class);
	private static DataSource ds =LookupManager.getInstance().getDsOracle();

	public static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			log.error("从连接池获取链接失败."+e);
			return null;
		}
	}

	public static void close(ResultSet rs, Statement ps, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("ResultSet关闭错误"+e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				log.error("Statement关闭错误"+e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error("Connection关闭错误"+ e);
			}
		}
	}
}
