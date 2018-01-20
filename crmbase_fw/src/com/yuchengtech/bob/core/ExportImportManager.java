package com.yuchengtech.bob.core;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.yuchengtech.bob.upload.ImportTradeBean;

/**
 * @describe This object will maintain the import or export queue.
 *           You add, remove the task of import or export.
 *           And the maximum size of the queue is 100.
 * @author WillJoe
 */
public class ExportImportManager {
	
    private static ExportImportManager instance;
    
    private static int impBatch=0;
    
    private List<BackgroundTask> runningTask;
    
    private ExportImportManager() {
        
    	runningTask = new ArrayList<BackgroundTask>();
    }
    
    public static synchronized ExportImportManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new ExportImportManager();
        }
        return instance;
    }

    /**
     * @describe Add a export task.
     * @param taskID
     * @param SQL
     * @param ds
     * @return
     */
    public synchronized TaskExportCSV addExportTask(int taskID, String SQL, DataSource ds) {
    	if (runningTask.size() > 100) {
    		return null;
    	} else {
    		TaskExportCSV task = new TaskExportCSV();
    		task.setMessage(SQL);
    		task.setDatasource(ds);
    		task.setTaskID(taskID);
    		runningTask.add(task);
    		Thread t = new Thread(task);
    		t.setName("Export_" + taskID);
    		t.start();
    		return task;
    	}
    }
    
    public synchronized void removeExportTask(TaskExportCSV task) {
    	runningTask.remove(task);
    }
    
    /**
     * @describe Add a import task.
     * @param taskId
     * @param ds
     * @param importTradeBean
     * @param fileName
     * @return
     */
    public synchronized TaskImportXLS addImportTask(int taskId,DataSource ds,ImportTradeBean importTradeBean,String fileName){
        if(runningTask.size() > 100) {
            return null;
        } else {
            TaskImportXLS task = new TaskImportXLS();
            task.setImportTradeBean(importTradeBean);
            task.setTaskID(taskId);
            task.setDatasource(ds);
            task.setFileName(fileName);
            task.setPKHead("IMP_"+impBatch);
            runningTask.add(task);
            Thread t = new Thread(task);
            t.setName("Import_" + taskId);
            t.start();
            impBatch++;
            return task;
        }
    }
    
    public synchronized void removeImportTask(TaskImportXLS task){
        runningTask.remove(task);
    }
    
}
