package com.yuchengtech.bob.common;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.model.BIPLogInfo;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.log.client.CommonLogInfo;
import com.yuchengtech.crm.log.client.ILogCollector;
import com.yuchengtech.crm.log.client.LogCollector;

public class LogService {
	private static Logger log = Logger.getLogger(LogService.class);
	public static DataSource dsOracle; 
	//private static Connection conn = null ;
	//private static Statement stmt = null ;
    //private static SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static BIPLogInfo loginfo =new BIPLogInfo();
	public static void addLog(){
		//private EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("BOB_JPA_ORACLE");
		//private EntityManager entityManager = emf.createEntityManager();
		
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/*
        String currenUserId = auth.getUserId();
        String currenOrgId = auth.getUnitId();
        loginfo.setVersion((long)0);
        loginfo.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
        //loginfo.setLoginIp("");
        loginfo.setOperFlag((long)1);
        //loginfo.setLogTypeId((long)3);
        //loginfo.setOperTime(new Date());
        loginfo.setUserId(currenUserId);
        loginfo.setOrgId(currenOrgId);
        loginfo.setOperTime(new Date());
        String operTime = "'"+sf.format(loginfo.getOperTime())+"'";
        operTime = "to_date("+operTime+",'yyyy-mm-dd hh24:mi:ss')";
        String logSql="insert into ADMIN_LOG_INFO (ID, VERSION, APP_ID, USER_ID, OPER_TIME, OPER_OBJ_ID, BEFORE_VALUE, AFTER_VALUE, OPER_FLAG, LOG_TYPE_ID, CONTENT, ORG_ID, LOGIN_IP) values(" ;
        logSql=logSql+"log_sequence.nextval, 0, '"+loginfo.getAppId()+"', '"+ loginfo.getUserId()+"',"+operTime+","
		+loginfo.getOperObjId()+",'"+loginfo.getBeforeValue()+"','"+ loginfo.getAfterValue()+"', "+loginfo.getOperFlag()+","
		+loginfo.getLogTypeId()+",'"+loginfo.getContent()+"','"+loginfo.getOrgId()+"','"+loginfo.getLoginIp()+"')";
		log.info(logSql);
        try {
        	conn=dsOracle.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(logSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
        	try {
				if(stmt != null) {
					stmt.close() ;
				}
				if(conn != null) {
					conn.close() ;
				}
			} catch (SQLException e) {
				//system.out.printlnln("close SQLException!");
				e.printStackTrace();
			}

        }
        */
        ILogCollector logCollector = new LogCollector();
        CommonLogInfo commonLogInfo = new CommonLogInfo();
        commonLogInfo.setLogInfo(loginfo);
        commonLogInfo.setAuthUser(auth);
        logCollector.collectLog(commonLogInfo);
	}
	/**
	 * 登出时使用(也可用于其它情况，只需要把AuthUser取到)
	 * 退出后SecurityContextHolder.getContext().getAuthentication().getPrincipal()是取不到的
	 **/
	public static void addLog(AuthUser auth){
		/* 		
        String currenUserId = auth.getUserId();
        String currenOrgId = auth.getUnitId();
        loginfo.setVersion((long)0);
        loginfo.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
        //loginfo.setLoginIp("");
        loginfo.setOperFlag((long)1);
        //loginfo.setLogTypeId((long)3);
        //loginfo.setOperTime(new Date());
        loginfo.setUserId(currenUserId);
        loginfo.setOrgId(currenOrgId);
        loginfo.setOperTime(new Date());
        String operTime = "'"+sf.format(loginfo.getOperTime())+"'";
        operTime = "to_date("+operTime+",'yyyy-mm-dd hh24:mi:ss')";
        String logSql="insert into ADMIN_LOG_INFO (ID, VERSION, APP_ID, USER_ID, OPER_TIME, OPER_OBJ_ID, BEFORE_VALUE, AFTER_VALUE, OPER_FLAG, LOG_TYPE_ID, CONTENT, ORG_ID, LOGIN_IP) values(" ;
        logSql=logSql+"log_sequence.nextval, 0, '"+loginfo.getAppId()+"', '"+ loginfo.getUserId()+"',"+operTime+","
		+loginfo.getOperObjId()+",'"+loginfo.getBeforeValue()+"','"+ loginfo.getAfterValue()+"', "+loginfo.getOperFlag()+","
		+loginfo.getLogTypeId()+",'"+loginfo.getContent()+"','"+loginfo.getOrgId()+"','"+loginfo.getLoginIp()+"')";
		log.info(logSql);
        try {
        	conn=dsOracle.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(logSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
        	try {
				if(stmt != null) {
					stmt.close() ;
				}
				if(conn != null) {
					conn.close() ;
				}
			} catch (SQLException e) {
				//system.out.printlnln("close SQLException!");
				e.printStackTrace();
			}

        }
        */
        ILogCollector logCollector = new LogCollector();
        CommonLogInfo commonLogInfo = new CommonLogInfo();
        commonLogInfo.setLogInfo(loginfo);
        commonLogInfo.setAuthUser(auth);
        logCollector.collectLog(commonLogInfo);
	}
	public static String ObjectToJSON(Object t){    
        java.lang.reflect.Field[] fs = t.getClass().getDeclaredFields();   
        HashMap<String,String> jsonMap =new HashMap<String,String>();
        String json = new String();    
        try{
	        for (java.lang.reflect.Field field : fs) {    
	            String propertyName = field.getName(); 
	            String methodName = "get"   
	                + propertyName.substring(0, 1).toUpperCase()    
	                + propertyName.substring(1); 
	            Method m = t.getClass().getMethod(methodName);
	            if(m!=null){
	            	Object o = m.invoke(t); 
	            	if(o!=null)
	            		//jsonMap.put(field.getName(),o.toString());
	            		json=json+"|"+o.toString();
	            }
	        }    
	        //json=JSONUtil.serialize(jsonMap);
        }catch(Exception e){
        	
        }
        return json;    
    } 
	
}
