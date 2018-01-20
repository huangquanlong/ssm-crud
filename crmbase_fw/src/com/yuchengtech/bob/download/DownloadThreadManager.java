package com.yuchengtech.bob.download;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoaderListener;

import com.yuchengtech.bcrm.system.service.DownloadRecordManagerService;
import com.yuchengtech.bob.vo.AuthUser;

/**
 * @author CHANGZH
 * @date 2013-06-18
 */
public class DownloadThreadManager {
	
    private static DownloadThreadManager instance;
    /**下载进程list*/
    private List<DownloadThread> downloadThread;
    /**下载中队列最大数进程*/
    private static int maxDownloadThreadNum = 100;
    /**下载队列最大数进程*/
    private static int maxThreadNum = 200;

    private DownloadRecordManagerService downloadRecordManagerService;
    
    private Timer timer;
    
	private static String preName = "Download_";
    
    public static synchronized DownloadThreadManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
        	instance = new DownloadThreadManager();
            instance.downloadThread = new ArrayList<DownloadThread>();
        }
        return instance;
    }

    /**
     * @describe Add a DOWNLOADTHREAD.
     * @param threadID
     * @param DownloadManagerService
     * @param DataSource
     * @return thread
     */
    public synchronized DownloadThread addDownloadThread(int threadID, String SQL, DataSource ds, Map<String, String> downloadInfo) {
    	if (downloadThread.size() > maxThreadNum) {
    		return null;
    	}
    	DownloadThread thread = new DownloadThread();
    	thread.setDatasource(ds);
    	thread.setSQL(SQL);
    	thread.setThreadID(threadID);
    	thread.setDownloadInfo(downloadInfo);
		thread.setaUser((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		downloadRecordManagerService = (DownloadRecordManagerService) ContextLoaderListener.getCurrentWebApplicationContext().getBean("downloadRecordManagerService");
		thread.setService(downloadRecordManagerService);
		addThread(thread);
    	if (downloadThread.size() > maxDownloadThreadNum) {
    		thread.status = "2";
    		//TODO 增加等待进程处理
    		startTimer();
    		return thread;
    	} else {
    		thread.status = "1";
    		thread.setName(preName + thread.getThreadID());
    		thread.start();
    		return thread;
    	}
    }
    
    private void addThread(DownloadThread thread) {
    	downloadThread.add(thread);   
    	for (int i= 0; i < downloadThread.size(); i ++) {
    		if (downloadThread.get(i).status.equals(DownloadThread.status_completed)) {
    			DownloadThreadManager.getInstance().removeDownloadThread(downloadThread.get(i));
    			i--;
    		}
    	}  
	}

	/***
     * 
     * 删除进程
     * @param threadID
     **/
    public synchronized void removeDownloadThreadById(int threadID) {
    	
    	for (DownloadThread thread : downloadThread) {
    		if (thread.getThreadID() == threadID) {
    			thread.interrupt();
    			downloadThread.remove(thread);
    			break;
    		}
    	}
    }
    /***
     * 
     * 删除进程
     * @param thread
     **/
    public synchronized void removeDownloadThread(DownloadThread thread) {
    	thread.interrupt();
    	downloadThread.remove(thread);
    }
    /**
     * 开启定时器
     * 当进程数大于100时运行，小于等于100时停止
     **/
    private void startTimer() {
    	getTimer().schedule(new startTask(), 1000, 60000);//在1秒后执行此任务,每次间隔60秒,如果传递一个Date参数,就可以在某个固定的时间执行这个任务.
        while(true){//停止此任务判断
            try { 
                if(downloadThread.size() == 0){ 
                	getTimer().cancel();//使用这个方法退出任务
                }
            } catch (Exception e) { 
                e.printStackTrace();
            }

        }
	}
    
    class startTask extends TimerTask{ 
		public void run() { 
			for (int i= 0; i < downloadThread.size(); i ++) {
        		if (downloadThread.get(i).status.equals("2") && downloadThread.size() <= maxDownloadThreadNum) {
        			downloadThread.get(i).setName(preName + downloadThread.get(i).getThreadID());
        			downloadThread.get(i).start();
        		} else if (downloadThread.get(i).status.equals(DownloadThread.status_completed)) {
        			DownloadThreadManager.getInstance().removeDownloadThread(downloadThread.get(i));
        			i--;
        		}
        	}             
    	}
	} 
	
	public DownloadThread getTheadCurrentInfo(Long threadID) {
		return null;
	}

	public int getTheadCurrentPercent(Long threadID) {
		return 50;
	}
	
	/****
	 *  取得定时器
	 **/
	public Timer getTimer() {
    	if (timer == null) {
    		timer = new Timer();
    	}
		return timer;
	}

    
}
