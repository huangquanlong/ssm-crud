package com.yuchengtech.bob.common;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import com.democreater.JsonCreater;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bob.core.DatabaseHelper;
import com.yuchengtech.bob.core.PagingInfo;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.download.DownloadThread;
import com.yuchengtech.bob.download.DownloadThreadManager;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.OperateTypeConstant;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.constance.fusioncharts.FusionChartBasicObject;
import com.yuchengtech.crm.exception.BizException;

  
public class CommonAction  extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{

	private static final long serialVersionUID = 8556373616118279765L;
	private static Logger log = Logger.getLogger(CommonAction.class);
	//MOde类定义，需要在业务Action中指定具体业务Model类
	protected Object model  ;
	//Request请求对象，从中可以手工获取请求参数
	protected HttpServletRequest request;
	//Model主键
	protected Object ID;
	//JPA查询返回集合对象
	protected Collection<?> list;
	//分页起始记录
	protected int start = 0;
	//每页记录数
	protected int limit = 100;
	//分页查询查询条件
    protected String condition;
    //Get请求返回对象
    protected Map<String, Object> json ; 
    //导出文件列映射
    private Map<String, String> fieldMap = new HashMap<String, String>();
    //分页查询排序字段
    protected String primaryKey = "ID";
    //分页查询、导出数据字典映射
    protected ConcurrentHashMap<String, String> oracleMapping = new ConcurrentHashMap<String, String>();
    //数据分组字段 GROUP BY 条件字段
    private String groupByFields = "";
    //分页查询SQL
    protected String SQL;
    //db2 withSQL
    protected String withSQL;
    //JDBC查询数据源，在业务Action中注入
    protected DataSource datasource;
    //数据权限机构过滤字段
    protected String branchFileldName;
    //数据权限用户过滤字段
    protected String userFileldName;
   	//新增修改删除记录是否记录日志,默认为false，不记录日志
	protected boolean needLog=false;
	//是否需要讲JDBC结果集转换为驼峰命名规范
	protected boolean transNames = false;
	
	protected FusionChartBasicObject chart = null;
	
	//公共业务服务类的定义
	private CommonService commonService;

	/*
	 * JDBC 分页查询查询语句准备
	 * 在业务Action中需重载实现
	 */
	public  void prepare(){
		
	}
    /*
     * 基于JDBC分页查询入口，默认进入index方法
     */
    public String index()  {
    	try {
	    	ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
	   		this.setJson(request.getParameter("condition"));
	    	prepare();
	        processSQL();
	        log.info("QUERY SQL: "+SQL);
	        if(request.getParameter("start")!=null)
	        	start=Integer.parseInt(request.getParameter("start"));
	        if(request.getParameter("limit")!=null)
	        	limit=Integer.parseInt(request.getParameter("limit"));
	        int currentPage = start / limit + 1;
	        PagingInfo pi=null;
	        if(request.getParameter("start")!=null&&request.getParameter("limit")!=null){
	        	pi = new PagingInfo(limit, currentPage);
	        }
	        Connection conn = datasource.getConnection();
	        QueryHelper query;
	        if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
	        	query = new QueryHelper(SQL, withSQL, conn, pi);
	        } else {
	        	query = new QueryHelper(SQL, conn, pi);
	        }
            if (!"ID".equals(primaryKey)) {
                query.setPrimaryKey(primaryKey);
            }
            for (Entry<String, String> entry : oracleMapping.entrySet()) {
                query.addOracleLookup(entry.getKey(), entry.getValue());
            }
            	if(this.json!=null)
            		this.json.clear();
            	else 
            		this.json = new HashMap<String,Object>(); 
            	this.json.put("json",query.getJSON(this.transNames));
            	if(null!=request.getParameter("createJson")){
            		String url = this.getClass().getAnnotation(org.apache.struts2.convention.annotation.Action.class).value();
            		if(url.startsWith("/")){
            			url = url.substring(1);
            		}
            		JsonCreater jc = new JsonCreater(this.json, url);
            		jc.createJson();
            	}
            	createChartData();
        }catch(Exception e){
        	e.printStackTrace();
        	/*
 			  异常抛出的详细接口说明    
 			      参数一：direct：0是输出到错误页，1是协议输出
			      参数二：level：  0信息，1警告，2错误
			      参数三：code:自定义代码，四位格式，自定义代码省掉为标准事件，如404错误不需要再有自定代码,
			      	目前已定义 的码值在extendpoint-ytec-bi-exception.xml中定义,如下：
		          	<entry key="404" value="文件{0}不存在"></entry>
					<entry key="500-1000" value="环境配置错误"></entry>
					<entry key="500-1001" value="系统启动参数配置错误"></entry>
					<entry key="500-1002" value="数据库错误"></entry>
					<entry key="500-1003" value="访问接口错误"></entry>
					<entry key="500-1004" value="方法的参数错误"></entry>
					<entry key="500-1005" value="文件访问错误"></entry>
					<entry key="500-1006" value="空指针错误"></entry>
					<entry key="500-1007" value="数组下标越界"></entry>
					<entry key="500-1008" value="访问Servlet错误"></entry>
			      参数四：msg:错误原因或业务约束校验提示内容
        	 */
        	throw new BizException(1,2,"1002",e.getMessage());
        }
        return "success";
    }
//    /*
//     * 后台JDBC导出
//     */
//    public String export() { 
//        try{
//	    	prepare();
//	        processSQL();
//	        ActionContext ctx = ActionContext.getContext();
//	        TaskExportCSV task = (TaskExportCSV) ctx.getSession().get("BACKGROUND_EXPORT_CSV_TASK");
//	        if (task == null) {
//	            DatabaseHelper dh = new DatabaseHelper(datasource);
//	            int taskId = dh.getNextValue("ID_BACKGROUND_TASK");
//	            ExportImportManager eim = ExportImportManager.getInstance();            
//	            task = eim.addExportTask(taskId, SQL, datasource);
//	            if (task == null) {
//	                //throw new Exception("当前下载人数过多，请稍后重试。");
//	                throw new BizException(1,0,"2001","当前下载人数过多，请稍后重试。");
//	            } else {
//	                json.put("taskID", task.getTaskID()); 
//	                task.setFieldLabel(fieldMap);
//	                task.setOracleMapping(oracleMapping);
//	                ctx.getSession().put("BACKGROUND_EXPORT_CSV_TASK", task);                
//	            }
//	        } else {
//	            json.put("taskID", task.getTaskID());
//	            //throw new Exception("请等待当前下载任务完成。");
//	            throw new BizException(1,0,"2002","请等待当前下载任务完成。");
//	        }
//        }catch(Exception e){
//        	e.printStackTrace();
//        	throw new BizException(1,2,"1002",e.getMessage());
//        }
//        return "success";    
//    }
//    /*
//     * 导出任务刷新
//     */
//    public String refresh()  {
//    	try{
//	        ActionContext ctx = ActionContext.getContext();
//	        TaskExportCSV task = (TaskExportCSV) ctx.getSession().get("BACKGROUND_EXPORT_CSV_TASK");
//	        String fileType = FileTypeConstance.getSystemProperty("EXP_FILE_TYPE")==null?"CSV":FileTypeConstance.getSystemProperty("EXP_FILE_TYPE");
//	        if (task != null) {
//	            if (task.isRunning()) {
//	                json.put("total", task.getTotal());
//	                json.put("expRecNum", task.getExpRecNum());
//	                json.put("current", task.getCurrent());
//	            } else {
//	            	json.put("total", task.getTotal());
//	                json.put("expRecNum", task.getExpRecNum());
//	                json.put("filename", task.getTaskID() + "."+fileType.toLowerCase());
//	                task = null;
//	                ctx.getSession().put("BACKGROUND_EXPORT_CSV_TASK", null);                
//	            }
//	        }
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    	throw new BizException(1,2,"1002",e.getMessage());
//	    }
//        return "success";
//    }
    /*
     * 后台JDBC导出
     */
    @SuppressWarnings("unchecked")
	public String export() { 
        try{
        	ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
	   		this.setJson(request.getParameter("condition"));
	   		Map<String, String> downloadInfo = new HashMap<String, String>();
	   		downloadInfo.put("menuId", request.getParameter("menuId"));
	   		downloadInfo.put("queryCon", request.getParameter("condition"));
	    	prepare();
	        processSQL();
	        
	        //添加导出列字典映射字段
	        Map<String, String> translateMap = new HashMap<String, String>();
			translateMap = (Map<String, String>) JSONUtil.deserialize(request.getParameter("translateMap"));
	   		for(String key : translateMap.keySet()){
	    		if(null!=translateMap.get(key)&&!"".equals(translateMap.get(key))){	
	    			this.addOracleLookup(key, translateMap.get(key).toString());
	    		}
	   		}
	        
	        DownloadThread thread = (DownloadThread) ctx.getSession().get("BACKGROUND_EXPORT_CSV_TASK");
	        if (thread == null || thread.status.equals(DownloadThread.status_completed)) {
	            DatabaseHelper dh = new DatabaseHelper(datasource);
	            int taskId = dh.getNextValue("ID_BACKGROUND_TASK");
	            DownloadThreadManager dtm = DownloadThreadManager.getInstance();            
	            thread = dtm.addDownloadThread(taskId, SQL, datasource, downloadInfo);
	            if (thread == null || DownloadThread.status_wating.equals(thread.status)) {
	                throw new Exception("当前下载人数过多，请稍后重试。");
	                //throw new BizException(1,0,"2001","当前下载人数过多，下载进程已放入队列，请不要重复点击下载。");
	            } else {
	                json.put("taskID", thread.getThreadID()); 
	                thread.setFieldLabel(fieldMap);
	                thread.setOracleMapping(oracleMapping);
	                ctx.getSession().put("BACKGROUND_EXPORT_CSV_TASK", thread);                
	            }
	        } else {
	            json.put("taskID", thread.getThreadID());
	            //throw new Exception("请等待当前下载任务完成。");
	            throw new BizException(1,0,"2002","请等待当前下载任务完成。");
	        }
        }catch(JSONException e){
			e.printStackTrace();
        	throw new BizException(1,0,"1002","导出列字典映射字段转换出错。");
		}catch(BizException e){
        	throw e;
        }catch(Exception e){
        	e.printStackTrace();
        	throw new BizException(1,2,"1002",e.getMessage());
        }
        return "success";    
    }
    /*
     * 导出任务刷新
     */
    public String refresh()  {
    	try{
	        ActionContext ctx = ActionContext.getContext();
	        DownloadThread thread = (DownloadThread) ctx.getSession().get("BACKGROUND_EXPORT_CSV_TASK");
	        String fileType = FileTypeConstance.getSystemProperty("EXP_FILE_TYPE")==null?"CSV":FileTypeConstance.getSystemProperty("EXP_FILE_TYPE");
	        if (thread != null) {
	        	if (json == null) {
	        		json = new HashMap<String, Object>();
	        	} else {
	        		json.clear();
	        	}
	        	Map<String, Object> temp = new HashMap<String, Object>();
	            if (thread.getStatus().equals(DownloadThread.status_downloading) || thread.getStatus().equals(DownloadThread.status_wating)) {
	            	temp.put("total", thread.getTotal());
	            	temp.put("expRecNum", thread.getExpRecNum());
	            	temp.put("current", thread.getCurrent());
	                json.put("json", temp);
	            } else if (thread.getStatus().equals(DownloadThread.status_completed)) {
	            	temp.put("total", thread.getTotal());
	            	temp.put("expRecNum", thread.getExpRecNum());
	                temp.put("filename", thread.getThreadID() + "."+fileType.toLowerCase());
	                json.put("json", temp);
	                DownloadThreadManager.getInstance().removeDownloadThreadById(thread.getThreadID());
	                thread = null;
	                ctx.getSession().put("BACKGROUND_EXPORT_CSV_TASK", null);                
	            }
	        }
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new BizException(1,2,"1002",e.getMessage());
	    }
        return "success";
    }
    /*
     * 构造最终的查询语句，包括数据权限
     */
	public void processSQL() {
        StringBuilder builder = new StringBuilder(SQL);
        boolean hasWhere = SQL.toUpperCase().indexOf(" WHERE ") > 0;
        AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (null!=auth.getUnitInfo() && branchFileldName != null && !"".equals(branchFileldName)) {
            if (hasWhere) {
                builder.append(" AND ");
            } else {
                builder.append(" WHERE ");
                hasWhere = true;
            }
            builder.append(branchFileldName);
            /**builder.append(" IN (SELECT UNITID FROM SYS_UNITS START WITH ID = '");
            builder.append(authUnits);
            builder.append("' CONNECT BY PRIOR ID = SUPERUNITID )");**/
            builder.append(" IN (");
//            if(authOrgList!=null){
//            	for(int i=0;i<authOrgList.size();i++){
//            		Map m = (Map)authOrgList.get(i);
//            		if(i==0){
//            			 builder.append("'");
//            		}else{
//            			 builder.append(",'");
//            		}
//            		builder.append(m.get("UNITID"));
//            		builder.append("'");
//            	}
//            }
            builder.append("SELECT SYSUNIT.UNITID FROM SYS_UNITS SYSUNIT WHERE SYSUNIT.UNITSEQ LIKE '"+auth.getUnitInfo().get("UNITSEQ")+"%'");
            builder.append(")");
        }
        
 //       boolean filterAdded = false;
//        StringBuilder filterBuilder = new StringBuilder();
        /**
         * TODO 增加数据权限过滤中的SQL过滤体条件
         */
        String filterSql = auth.getFilterString(getClass().getSimpleName(), AuthUser.METHOD_SELECT);
        if(null!=filterSql&&!"".equals(filterSql)){
        	  if (hasWhere) {
			    	builder.append(" AND ");
			    } else {
			    	builder.append(" WHERE ");
			    }
        	builder.append(filterSql);
        }
        
        if (!"".equals(groupByFields)){
            builder.append(groupByFields);
        }
        if (!"ID".equals(primaryKey)) {
            builder.append(" ORDER BY ");
            builder.append(primaryKey);
        }
        this.SQL = builder.toString();
       
    }

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}
	public Map<String, Object> getJson() { 
		return json;
	} 
	
	public void setJson(Map<String, Object> json) {
        this.json = json;
    }
	/*
	 * 查询条件字符串转成Json对象
	 */
    @SuppressWarnings("unchecked")
	public void setJson(String condition){        
        try {
        	if(condition!=null&&!"".equals(condition))
        		this.json = (Map<String, Object>) JSONUtil.deserialize(condition);
        	else
        		this.json = new HashMap<String, Object>();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
     * 直接设置condition条件
     */
    public void setCondition(String condition) {
        try {
            this.json = (Map<String, Object>) JSONUtil.deserialize(condition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
     * 设置导出字段
     */
    @SuppressWarnings("unchecked")
	public void setFieldMap(String fieldMap){
        try {
//            this.fieldMap = (Map<String, String>) JSONUtil.deserialize(fieldMap);
        	this.fieldMap = JSONObject.fromObject(fieldMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 设置默认的排序
     * @param primaryKey 
     * 可以是多个字段的组合，例如："field1, field2 desc, field3"
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    /*
     * 内部方法，添加数据字典映射
     * columnName：列名
     * LookupName：字典类型名称
     */
    public void addOracleLookup(String columnName, String LookupName) {
        oracleMapping.put(columnName, LookupName);
    }
    
    //JPA查询所有记录
	//调用：GET /actionName
    public HttpHeaders indexAll() {
        list = commonService.findAll();
        this.json=null;
        return new DefaultHttpHeaders("index")
            .disableCaching();
    }
    //JPA删除一条记录
	//调用：DELETE /actionName/ID
    public String destroy() {
    	commonService.remove(ID);
        addActionMessage("Record removed successfully");
        return "success";
    }
    //删除确认
    // GET /actionName/ID/deleteConfirm
    public String deleteConfirm() {
        return "deleteConfirm";
    }
    // JPA新增一条记录
    // POST /actionName
    public DefaultHttpHeaders create() {
    	try{
    		model=commonService.save(model);
    		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
    		auth.setPid(metadataUtil.getId(model).toString());
    		if(needLog){
	    	   	ActionContext ctx = ActionContext.getContext();
		        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
				LogService.loginfo.setLoginIp(request.getRemoteAddr());
				LogService.loginfo.setLogTypeId((long)OperateTypeConstant.LOG_RECORD);
				LogService.loginfo.setContent("新增或修改记录:"+model.getClass().getName());
				LogService.loginfo.setAfterValue(LogService.ObjectToJSON(model));
				LogService.addLog();
    		}
	        
    	}catch(BizException e){
    		throw e;
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new BizException(1,2,"1002",e.getMessage());
    	}
    	return new DefaultHttpHeaders("success");
        
    }
    // 更新一条记录（暂不使用）
    // PUT /actionName/ID
    public String update() {
    	//commonService.save(model);
        //addActionMessage("Record updated successfully");
        return "success";
    }
    //（自定义）批量删除
    public String batchDestroy(){
    	try{
    	   	ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("idStr");
	        commonService.batchRemove(idStr);
	        if(needLog){
	        	LogService.loginfo.setLoginIp(request.getRemoteAddr());
		        LogService.loginfo.setLogTypeId((long)OperateTypeConstant.LOG_RECORD);
				LogService.loginfo.setContent("删除记录:"+model.getClass().getName());
				LogService.loginfo.setAfterValue("主键："+idStr);
				LogService.addLog();
	        }
	        
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new BizException(1,2,"1002",e.getMessage());
    	}
    	return "success";
    }
    
    /**
	 * 执行JQL进行批量修改/删除操作.
	 * 
	 * @param jql 更新或删除语句
	 * For Example : 
	 * UPDATE PollOption p SET p.optionItem = :value WHERE p.optionId = :optionId
	 * values.put("value","aaa");
	 * values.put("optionId",1);
	 * UPDATE PollOption p SET p.optionItem = :value WHERE p.optionId in (1,2,3)
	 * DELETE FROM PollOption p WHERE p.optionId = :optionId 
	 * DELETE FROM PollOption p WHERE p.optionId in (1,2,3,4)
	 * @param values  map(参数、参数值)
	 *     
	 * @return 更新记录数.*/
	public int executeUpdate(String jql,Map<String,Object>values) {
		try{
			return commonService.batchUpdateByName(jql, values);
		}catch(Exception e){
    		e.printStackTrace();
    		throw new BizException(1,2,"1002",e.getMessage());
    	}
 	}
    //进入详情页面
    // GET /actionName/ID
	public HttpHeaders show() {
        return new DefaultHttpHeaders("show");
    }
	//进入编辑页面
    // GET /actionName/ID/edit
    public String edit() {
        return "success";
    }
    //进入新增页面
    // GET /actionName/new
    public String editNew() {

        return "editNew";
    }
    public void validate() {

    }
    //接收传入的ID参数
//    public void setId(Long ID) {
//        if (ID != null) {
//            this.model = commonService.find(ID);
//        }
//        this.ID = ID;
//    }
    /*非常重要的接口，基于CommonActon的类在调用开始第一个入口函数
     * Get请求返回时的出口
     * 获取输入对象和返回对象，实现ModelDriven后只从getModel返回对象到客户端(non-Javadoc)
     * @see com.opensymphony.xwork2.ModelDriven#getModel()
     */
    public Object getModel() {
    	if(json!=null) 
    		return json;
    	else if(list!=null)
    		return list;
    	else return model;
        //return (list != null ? list : model);
    }
  //单Model查询，在业务Action中需要构造JQL
    public HttpHeaders indexByJql(String jql,Map<String,Object> values) {
    	list=commonService.findByJql(jql, values);
	    //getModel 返回List,因此将json设为null
    	this.json=null;
	    return new DefaultHttpHeaders("success").disableCaching();
	}
    //JPA分页查询，在业务Action中需要构造JQL
    public HttpHeaders indexPageByJql(String jql,Map<String,Object> values) {

    	//数据权限过滤（按机构层级）
	    try{	
    		AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (null != auth.getUnitInfo() && branchFileldName != null && !"".equals(branchFileldName)) {
			    if (jql.toUpperCase().indexOf(" WHERE ")!=-1) {
			    	jql=jql+(" AND ");
			    } else {
			    	jql=jql+(" WHERE ");
			    }
			    jql=jql+(branchFileldName);
			    jql=jql+(" IN (");
//			    if(authOrgList!=null){
//			    	for(int i=0;i<authOrgList.size();i++){
//			    		Map m = (Map)authOrgList.get(i);
//			    		if(i==0){
//			    			jql=jql+("'");
//			    		}else{
//			    			jql=jql+(",'");
//			    		}
//			    		jql=jql+(m.get("UNITID"));
//			    		jql=jql+("'");
//			    	}
//			    }
			    jql = jql + "SELECT C.unitid FROM SysUnits C WHERE C.unitseq LIKE '"+auth.getUnitInfo().get("UNITSEQ")+"%'";
			    jql=jql+(")");
			}
			if (userFileldName != null && !"".equals(userFileldName)) {
			    if (jql.toUpperCase().indexOf(" WHERE ")!=-1) {
			    	jql=jql+(" AND ");
			    } else {
			    	jql=jql+(" WHERE ");
			    }
			    jql=jql+(userFileldName);
			    jql=jql+("=:userFiledName");
			    values.put("userFileldName",auth.getUserId());
			}
			
			/**
			 * TODO 增加数据权限过滤中的JQL过滤体条件
			 */
			String tmpFilter = auth.getFilterString(getClass().getSimpleName(), AuthUser.METHOD_SELECT);
	    	if(null!=tmpFilter&&!"".equals(tmpFilter)){
	    		if(jql.toUpperCase().indexOf(" WHERE ")!=-1)
	    			jql = jql + " AND ";
	    		else
	    			jql = jql + " WHERE ";
	    		jql = jql + tmpFilter;
	    	}
			
    		Map<String, Object> resultMap= commonService.findPageByJql(start, limit, jql, values);
    		if(this.json!=null)
    			this.json.clear();
    		else 
    			this.json = new HashMap<String,Object>();  
    		this.json.put("json",resultMap); 
    		createChartData();
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw new BizException(1,2,"1002",e.getMessage());
	    }
    	return new DefaultHttpHeaders("success").disableCaching();
    	
	}
    //获取新增或修改记录的主键
    public HttpHeaders getPid(){
    	try{
    		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//          //system.out.printlnln(auth2.getPid());
    		String pid=auth.getPid();
    		if(this.json!=null)
    			this.json.clear();
    		else 
    			this.json = new HashMap<String,Object>();  
    		this.json.put("pid",pid);
    		auth.setPid(null);
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new BizException(1,2,"1002",e.getMessage());
     	}
    	return new DefaultHttpHeaders("success").disableCaching();
	}
    /**
     * 设置业务机构的字段名称
     * @param branchFileldName
     * 只能是一个字段，可以加表前缀，例如："t1.unitId"
     * 该字段存储的是5位机构代码
     */
    public void setBranchFileldName(String branchFileldName){
    	this.branchFileldName=branchFileldName;
    }
    /**
     * 设置数据过滤的用户ID
     * @param userFileldName
     * 只能是一个字段，可以加表前缀，例如："t1.userId"
     * 该字段存储的是5位机构代码
     */
    public void setUserFileldName(String UserFileldName){
    	this.userFileldName=UserFileldName;
    }
    /**
     * 设置分组字段
     * @param gbfs
     * 请按照SQL中字段分组来设置。例如：'t1.update_date,t1.create_date'
     * 另：请注意分组统计时的SQL标准。
     */
    public void setGroupByFields(String gbfs){
        this.groupByFields = gbfs;
    }
    /**
     * 获取当前用户session
     */
    public AuthUser getUserSession() {
    	return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    private void createChartData(){
    	if(chart!=null){
    		chart.setJsonData(JSONObject.fromObject(this.json));
    		this.json = chart.getJson();
    	}
    }
    /***
	 *  拼接条件项 <br>
	 *  <p>向条件串中增加条件项(默认逻辑符为 and)<br>
	 *  @param conditionName 列名<br>
	 *  @param compareChar 比较符<br>
	 *  @param conditionValue 条件值<br>
	 *  @param dataType 数据类型<br>
	 **/
    public void configCondition(String conditionName, String compareChar, String conditionValue, int dataType) {
    	ConditionManager conditionManager = new ConditionManager(this.getJson());
    	conditionManager.addItem(conditionName, compareChar, conditionValue, dataType);
    	if (conditionManager.getWhereSQL().length() > 0) {
    		boolean hasWhere = SQL.toUpperCase().indexOf(" WHERE ") > 0;
       	 	if (hasWhere) {
       			SQL += " AND ";
            } else {
           	 	SQL +=" WHERE ";
            }
       	 	SQL += conditionManager.getWhereSQL();
    	}
    	log.debug("whereSQL：["+conditionName +" " +compareChar+" " +conditionManager.getWhereSQL()+"]");
	}
    /***
     *  拼接条件项 <br>
     *  <p>向条件串中增加条件项(默认逻辑符为 and)<br>
     *  @param conditionName 列名<br>
     *  @param compareChar 比较符<br>
     *  @param conditionValue 条件值<br>
     **/
    public void configCondition(String conditionName, String compareChar, String conditionValue) {
    	configCondition(conditionName, compareChar, conditionValue, -1);
    }
    
    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
}