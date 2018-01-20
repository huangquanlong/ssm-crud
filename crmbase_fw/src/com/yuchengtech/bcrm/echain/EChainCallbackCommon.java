package com.yuchengtech.bcrm.echain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.log4j.Logger;

import com.ecc.echain.workflow.engine.EVO;

/*
 * 修改记录：wzy，2014-01-02，修改：手动提交事务，出现异常后，事务回滚
 * update record：helin,20140923,将异常信息抛出
 */
public class EChainCallbackCommon {

	private static Logger log = Logger.getLogger(EChainCallbackCommon.class);
	
	protected String SQL;
	
	protected List<String> SQLS = new ArrayList<String>();

	/**
	 * @deprecated
	 * @return
	 */
//	public String execteSQL() {
//		Connection conn = null;
//		Statement stmt = null;
//		try {
//			conn = JdbcUtil.getConnection();
//			stmt = conn.createStatement();
//			stmt.execute(SQL);
//			System.out.println("执行SQL:"+SQL);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			JdbcUtil.close(null, stmt, conn);
//		}
//		return "success";
//	}

	/**
	 * @param vo
	 * @return
	 * @throws SQLException
	 */
	public String execteSQL(EVO vo) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = vo.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			log.info("执行SQL:"+SQL);
			stmt.execute(SQL);
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
//			if (conn != null) {//conn在工作流中调用并关闭,这里不做关闭处理
//				conn.close();
//			}
		}
		return "success";
	}
	
	public Result querySQL(EVO vo) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Result result = null;
		try {
			conn = vo.getConnection();
			stmt = conn.createStatement();
			log.info("执行SQL:"+SQL);
			rs = stmt.executeQuery(SQL);
		    result = ResultSupport.toResult(rs); 
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
//			if (conn != null) {//conn在工作流中调用并关闭,这里不做关闭处理
//				conn.close();
//			}
		}
		return result;
	}
	
	public String executeBatch(EVO vo) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = vo.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (String sql : SQLS) {
				log.info("执行SQL队列:"+sql);
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
//			if (conn != null) {//conn在工作流中调用并关闭,这里不做关闭处理
//				conn.close();
//			}
		}
		return "success";
	}
}
