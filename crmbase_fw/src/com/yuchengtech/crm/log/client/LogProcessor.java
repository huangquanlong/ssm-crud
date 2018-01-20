package com.yuchengtech.crm.log.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.web.context.ContextLoader;

import com.yuchengtech.bob.model.BIPLogInfo;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.log.PooledThread;

/***
 * 
 * 日志处理类
 * @author CHANGZH
 * @date 2014-07-29
 */
public class LogProcessor extends PooledThread {

	public LogProcessor(int threadId) {
		super(threadId);
	}
	/**
	 * 处理日志线程是否需要运行
	 */
	public boolean needProcess() {
		if(LogCollectorManager.getInstance().getLogInfoSize() > 0) {
			return true;
		} else {
			return false;
		}
	} 
	/**
	 * 处理日志方法
	 */
	public void process() {
		//处理当前线程未处理日志
		Object obj =  LogCollectorManager.getInstance().getLastLogInfo();
		if (obj != null) {
			logger.debug("start log threadId=" + this.getThreadId());
			logger.debug("loginfo=" + obj);
			 
			if (obj instanceof CommonLogInfo) {
				BIPLogInfo logInfo = (BIPLogInfo)((CommonLogInfo)obj).getLogInfo();
				AuthUser authUser = (AuthUser)((CommonLogInfo)obj).getAuthUser();
				addLog(logInfo, authUser);
			}
			logger.debug("end log threadId=" + this.getThreadId());
		}
		
	}
	/**
	 * 日志保存方法
	 * @param logInfo 日志信息
	 * @param authUser 用户信息
	 */
	private void addLog(BIPLogInfo logInfo, AuthUser authUser) {
		DataSource dsOracle = (DataSource) ContextLoader.getCurrentWebApplicationContext().getBean("dsOracle"); 
		Connection     conn = null;
		Statement      stmt = null;
		PreparedStatement pstmt = null;
	    SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currenUserId = authUser.getUserId();
        String currenOrgId = authUser.getUnitId();
        logInfo.setVersion((long)0);
        logInfo.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
        logInfo.setOperFlag((long)1);
        logInfo.setUserId(currenUserId);
        logInfo.setOrgId(currenOrgId);
        logInfo.setOperTime(new Date());
        String operTime = "'"+sf.format(logInfo.getOperTime())+"'";
        operTime = "to_date("+operTime+",'yyyy-mm-dd hh24:mi:ss')";
        String logSql = "insert into ADMIN_LOG_INFO (VERSION, APP_ID, USER_ID, OPER_TIME, OPER_OBJ_ID, BEFORE_VALUE, AFTER_VALUE, OPER_FLAG, LOG_TYPE_ID, CONTENT, ORG_ID, LOGIN_IP) values(" ;
        logSql = logSql+"0,?, ?, ?,?,?,?,?,?,?,?,?)";
		//+logInfo.getOperObjId()+",'"+logInfo.getBeforeValue()+"','"+ logInfo.getAfterValue()+"', "+logInfo.getOperFlag()+","
		//+logInfo.getLogTypeId()+",'"+logInfo.getContent()+"','"+logInfo.getOrgId()+"','"+logInfo.getLoginIp()+"')";
        logger.info(logSql);
        try {
        	conn  = dsOracle.getConnection();
            stmt  = conn.createStatement();
            pstmt = conn.prepareStatement(logSql);
            pstmt.setString(1, logInfo.getAppId());
            pstmt.setString(2, logInfo.getUserId());
            pstmt.setString(3, sf.format(logInfo.getOperTime()));
            if(null != logInfo.getOperObjId()) {
            	pstmt.setLong(4, logInfo.getOperObjId());
            } else {
            	pstmt.setNull(4,Types.DOUBLE);
            }
            pstmt.setString(5, logInfo.getBeforeValue() + "");
            pstmt.setString(6, logInfo.getAfterValue() + "");
            pstmt.setString(7, logInfo.getOperFlag() + "");
            pstmt.setLong(8, logInfo.getLogTypeId());
            pstmt.setString(9, logInfo.getContent() + "");
            pstmt.setString(10, logInfo.getOrgId());
            pstmt.setString(11, logInfo.getLoginIp());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	try {
				if(stmt != null) {
					stmt.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.info("记录日志关闭数据库连接时异常！");
				e.printStackTrace();
			}
        }
	}
}
