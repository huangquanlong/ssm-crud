package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.FwSysProp;
import com.yuchengtech.bcrm.system.service.DownloadRecordManagerService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.download.DownloadThreadManager;
import com.yuchengtech.crm.exception.BizException;

/**
 * 报表下载管理
 * @author CHANGZH@YUCHENGTECH.COM
 * @since 2013-06-17
 */

@SuppressWarnings("serial")
@Action("/DownloadRecordAction")
public class DownloadRecordAction  extends CommonAction{
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds; //声明数据源
	
    @Autowired
    private DownloadRecordManagerService downloadRecordManagerService;
    
    @Autowired
	public void init(){
	  	model = new FwSysProp(); 
		setCommonService(downloadRecordManagerService);
		//新增修改删除记录是否记录日志,默认为false，不记录日志
		//needLog=true;
	}
    
    /**
	 * 设置查询SQL并为父类相关属性赋值
	 */
	public void prepare() {

		String sortStr = "R.ID DESC"; //设置默认排序
		String userCondtion = "USER_ID = '"+ this.getUserSession().getUserId() +"' AND";
		if (downloadRecordManagerService.isLogicSysManager()) {
			userCondtion = "";
		}
		StringBuilder queryStr = new StringBuilder("SELECT R.ID,USER_ID,TO_CHAR(R.FINISH_TIME, 'yyyy-mm-dd hh24:mi:ss') as FINISH_TIME," +
				" TO_CHAR(R.START_TIME, 'yyyy-mm-dd hh24:mi:ss') as  START_TIME,THREAD_ID,FILENAME," +
				" THREAD_STATUS,M.NAME AS MENU_NAME,A.USER_NAME " +
				" FROM OCRM_F_SYS_DOWNLOAD_RECORD R LEFT JOIN CNT_MENU M ON M.ID= R.MENU_ID " +
				"  LEFT JOIN ADMIN_AUTH_ACCOUNT A ON A.ACCOUNT_NAME= R.USER_ID " +
				" WHERE "+userCondtion+" 1=1 ");

		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key)
					&& !this.getJson().get(key).equals("")) {
				if (key.equals("THREAD_STATUS")) {
					queryStr.append(" AND R.THREAD_STATUS = '" + this.getJson().get(key) + "'");
				} else if (key.equals("START_TIME1")) {// 开始时间从
					queryStr.append(" AND TO_CHAR(R.START_TIME, 'yyyy-mm-dd hh24:mi:ss') >='" + this.getJson().get(key) + "'");
				} else if (key.equals("START_TIME2")) {// 开始时间至
					queryStr.append(" AND TO_CHAR(R.START_TIME, 'yyyy-mm-dd hh24:mi:ss') <='" + this.getJson().get(key) + "'");
				} else if (key.equals("END_TIME1")) {// 结束时间从
					queryStr.append(" AND TO_CHAR(R.FINISH_TIME, 'yyyy-mm-dd hh24:mi:ss') >='" + this.getJson().get(key) + "'");
				} else if (key.equals("END_TIME2")) {// 结束时间至
					queryStr.append(" AND TO_CHAR(R.FINISH_TIME, 'yyyy-mm-dd hh24:mi:ss') <='" + this.getJson().get(key) + "'");
				}
				
			}
		}
		
		SQL = queryStr.toString();
		setPrimaryKey(sortStr);
		datasource = ds;
	}
	/***
	 * 删除下载任务
	 * @return
	 */
	public String delFile() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String id  = request.getParameter("id");
		downloadRecordManagerService.removeDownloadFile(id);
		return "success";
	}
	/**
	 * 结束下载进程
	 * @return 成功标识
	 */
	public String stopDownloadThread() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		int threadId  = Integer.parseInt(request.getParameter("threadId"));
		DownloadThreadManager.getInstance().removeDownloadThreadById(threadId);
		return "success";
	}
	
	/**
	 * 导出文件
	 * @return 成功标识
	 */
	public String exportFile() { 
        try{
	        ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);	       
	        String fileName = request.getParameter("fileName");			
			if(this.json!=null)
        		this.json.clear();
        	else 
        		this.json = new HashMap<String,Object>(); 
			 
			this.json.put("filename", fileName);
			
        } catch(Exception e){
        	throw new BizException(1,2,"1002",e.getMessage());
        }
        return "success";    
    }
	
	public String isLogicSysManager() {
		if(this.json!=null)
    		this.json.clear();
    	else 
    		this.json = new HashMap<String,Object>(); 	
		if (downloadRecordManagerService.isLogicSysManager()) {			
			this.json.put("isLogicSysManager", "1");
		} else {
			this.json.put("isLogicSysManager", "0");
		}
		return "success";
	}
    
}