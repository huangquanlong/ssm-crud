package com.yuchengtech.crm.log;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * 线程池类
 */
public class ThreadPool {
	
	private ArrayList<PooledThread> threads;
	
	private String errorMessage;
	
	private Integer totalThreads;
	
	private static Logger logger;
	
	public ThreadPool(int poolSize, String className) {
		assert (poolSize > 0) : "The pool size should be greater than zero, unexpected parameter.";
		assert (className != null) : "The class name cannot be null, unexpected parameter.";
		totalThreads = 0;
		if (logger == null) {
    		logger = Logger.getLogger(this.getClass());
    	}
		try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getDeclaredConstructor(int.class);
			Object o = constructor.newInstance(0);
			if (o instanceof PooledThread) {
				threads = new ArrayList<PooledThread>();
				for (int i = 0; i < poolSize; i++, o = constructor.newInstance(i)) {
					((PooledThread) o).setThreadPool(this);
					Thread t = new Thread((PooledThread) o);
					t.start();
					threads.add((PooledThread) o);
					totalThreads++;
				}		
			} else {
				throw new Exception("Threads must inherit PooledThread, but it's: " + className);
			}
		} catch (Exception e) {
			logger.error("创建线程池时发生异常。", e);
			e.printStackTrace();
		} 
	}
	
	/**
	 * 销毁线程池中的所有线程对象
	 */
	public void releasePool() {
		if (threads == null) {
			errorMessage = "The thread pool had been released.";
		} else {
			for (PooledThread thread : threads) {
				thread.stop();
			}
			threads = null;
		}
	}

	/**
	 * 唤醒线程池中的所有线程对象
	 */
	public synchronized void dispatch() {
		if (threads != null) {
			for (PooledThread thread : threads) {
				if (!thread.isBusying()) {
					thread.resume();
				} 
			}
		}					
	}
	
	/**
	 * 等待线程池中的线程通知，供主线程调用
	 */
	public void waitAllThread() {
        try {
            synchronized (totalThreads) {
            	totalThreads.wait();
            }
        } catch (InterruptedException e) {
        	logger.error("挂起主线程时发生异常。", e);
            e.printStackTrace();
        }			
	}
	
	/**
	 * 线程池中的线程对象通知主线程
	 */
	public void threadNotify() {
    	synchronized (totalThreads) {
			totalThreads.notify();
    	}
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public synchronized int getBusyThreadCount() {
		int busyThreadCount = 0;
		if (threads != null) {
			for (PooledThread thread : threads) {
				if (thread.isBusying()) {
					busyThreadCount++;
				}
			}
		}					
		return busyThreadCount;
	}
	
}
