package com.yuchengtech.bob.action;

import java.sql.Connection;
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
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.core.DatabaseHelper;
import com.yuchengtech.bob.core.PagingInfo;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.download.DownloadThread;
import com.yuchengtech.bob.download.DownloadThreadManager;
import com.yuchengtech.bob.upload.FileTypeConstance;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.exception.BizException;

public abstract class BaseQueryAction {
	
	private static Logger log = Logger.getLogger(BaseQueryAction.class);
	protected Map<String, Object> json = new HashMap<String, Object>(); 
    private Map<String, String> fieldMap = new HashMap<String, String>();
  //Request请求对象，从中可以手工获取请求参数
	protected HttpServletRequest request;
    protected int start = 0;
    protected int limit = 100;
    protected String branchFileldName;
    protected String primaryKey = "ID";
    protected ConcurrentHashMap<String, String> oracleMapping = new ConcurrentHashMap<String, String>();
    private String groupByFields = "";
	//是否需要讲JDBC结果集转换为驼峰命名规范
	protected boolean transNames = false;
	
    protected String SQL;
    protected DataSource datasource;
    
    public abstract void prepare();
    
    public String index() throws Exception {
        
        prepare();
        processSQL();
        log.info("QUERY SQL: "+SQL);
        int currentPage = start / limit + 1;
        PagingInfo pi=null;
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        if(request.getParameter("start")!=null&&request.getParameter("limit")!=null){
        	pi = new PagingInfo(limit, currentPage);
        }
        Connection conn = datasource.getConnection();
        QueryHelper query;
        try {
            query = new QueryHelper(SQL, conn, pi);
            if (!"ID".equals(primaryKey)) {
                query.setPrimaryKey(primaryKey);
            }
            for (Entry<String, String> entry : oracleMapping.entrySet()) {
                query.addOracleLookup(entry.getKey(), entry.getValue());
            }
            json = query.getJSON(this.transNames);
        
        } 
        catch(Exception e){
        	e.printStackTrace();
        	throw(e);
        }
        finally {
            if(!conn.isClosed())
            	conn.close();
        }
        return "success";
    }

    @SuppressWarnings("unchecked")
	public String export() throws Exception { 
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
    
    public String refresh() throws Exception {
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
            builder.append(" IN (");
            builder.append("SELECT SYSUNIT.UNITID FROM SYS_UNITS SYSUNIT WHERE SYSUNIT.UNITSEQ LIKE '"+auth.getUnitInfo().get("UNITSEQ")+"%'");
            builder.append(")");
        }
        
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
        
//        if (auth.getAuthorities().size() > 0) {
//            filterBuilder.append(" AND (");
//            for(GrantedAuthority ga : auth.getAuthorities() ) {
//                String SQL = FilterManager.getInstance().getFilterSQL(getClass().getSimpleName(), ga.getAuthority());
//                if (SQL != null) {
//                    if(filterAdded){
//                        filterBuilder.append(" OR ");
//                    }
//                    SQL = SQL.replaceAll("@USER_ID","'"+auth.getUserId()+"'");
//                    
//                    filterBuilder.append(SQL);
//                    filterAdded = true;
//                }
//            }
//            filterBuilder.append(") ");
//        }
//        if (filterAdded) {
//            builder.append(filterBuilder);
//        }
        if (!"".equals(groupByFields)){
            builder.append(groupByFields);
        }
        if (!"ID".equals(primaryKey)) {
            builder.append(" ORDER BY ");
            builder.append(primaryKey);
        }
        this.SQL = builder.toString();
       
    }
    
    public void addOracleLookup(String columnName, String LookupName) {
        oracleMapping.put(columnName, LookupName);
    }

    public Map<String, Object> getJson() {
        return json;
    }

    public void setJson(Map<String, Object> json) {
        this.json = json;
    }

    @SuppressWarnings("unchecked")
    public void setCondition(String condition) {
        try {
            this.json = (Map<String, Object>) JSONUtil.deserialize(condition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    @SuppressWarnings("unchecked")
    public void setFieldMap(String fieldMap){
        try {
            //this.fieldMap = (Map<String, String>) JSONUtil.deserialize(fieldMap);
            this.fieldMap = (Map<String, String>) JSONObject.fromObject(fieldMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 设置默认的排序
     * @param primaryKey 
     * 可以是多个字段的组合，例如："field1, field2 desc, field3"
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    /**
     * 设置业务机构的字段名称
     * @param branchFileldName
     * 只能是一个字段，可以加表前缀，例如："t1.unitId"
     * 该字段存储的是5位机构代码
     */
    public void setBranchFileldName(String branchFileldName) {
        this.branchFileldName = branchFileldName;
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
}