package com.yuchengtech.bob.core;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.vo.AuthUser;

public abstract class BackgroundTask implements Runnable {

    protected static Logger log = Logger.getLogger(BackgroundTask.class);

	private Object lock;
	
	private DataSource datasource;
	
	private int taskID;
	
	protected String Message;
	
	protected int total;
	
	protected int current;
	
	protected boolean running;
	
	private AuthUser aUser = null;
	
    public void stop() {
        running = false;
        lock.notify();
    }

    public void pause() {
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("挂起后台任务线程出错:", e);
        }
    }

    public void resume() {
        lock.notify();
    }

	public abstract void run();

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public int getTotal() {
		return total;
	}

	public int getCurrent() {
		return current;
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

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public AuthUser getaUser() {
		return aUser;
	}

	public void setaUser(AuthUser aUser) {
		this.aUser = aUser;
	}

}
