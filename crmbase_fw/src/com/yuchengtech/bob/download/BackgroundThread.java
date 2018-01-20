package com.yuchengtech.bob.download;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.vo.AuthUser;
/**
 * 后台进程虚类
 * @author CHANGZH
 * @date 2013-07-03
 */
public abstract class BackgroundThread extends Thread {

    protected static Logger log = Logger.getLogger(BackgroundThread.class);

	private DataSource datasource;
	
	private int threadID;
	
	protected int total;
	
	protected int current;
	
	protected String Message;
	
	protected boolean running;
	
	private AuthUser aUser = null;
	
	public abstract void run();

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public boolean isRunning() {
		return running;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public int getThreadID() {
		return threadID;
	}

	public void setThreadID(int threadID) {
		this.threadID = threadID;
	}

	public AuthUser getaUser() {
		return aUser;
	}

	public void setaUser(AuthUser aUser) {
		this.aUser = aUser;
	}

	public int getTotal() {
		return total;
	}

	public int getCurrent() {
		return current;
	}
}
