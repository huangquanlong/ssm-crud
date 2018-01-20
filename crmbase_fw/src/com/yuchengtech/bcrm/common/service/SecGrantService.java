package com.yuchengtech.bcrm.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.system.service.UserManagerService;
import com.yuchengtech.bob.common.LogService;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.bob.vo.IAuser;
import com.yuchengtech.crm.constance.OperateTypeConstant;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
import com.yuchengtech.crm.sec.credentialstrategy.CredentialStrategy;

/**
 * 认证管理服务类
 * 
 * 功能： 取当前用户所拥有的菜单及功能点等 		 
 * @author changzh@yuchengtech.com
 * @date   2012-11-01
 */
@Service
@Transactional
public class SecGrantService {
	
	private static Logger log = Logger.getLogger(SecGrantService.class);
	
	@Autowired
    @Qualifier("dsOracle")
    private DataSource dsOracle;
	
	@Autowired
	private UserManagerService userManagerService;
	
	@SuppressWarnings("unchecked")
	public Map getGrantMenus(String resName, AuthUser userDetails) {
		StringBuffer sb 	 = new StringBuffer();
		StringBuffer roleIDs = new StringBuffer();
		StringBuffer roleCon = new StringBuffer();
		
		if (userDetails.getRolesInfo() == null)
			return null;
		/**取角色ID拼接成in条件串*/
		for (int i = 0; i < userDetails.getRolesInfo().size(); i ++ ) {
			Map tempMap = (HashMap)userDetails.getRolesInfo().get(i);
			if (roleIDs.length() == 0 ) {
				roleIDs.append("'" + tempMap.get("ID") + "'");
			} else {
				roleIDs.append(",'" + tempMap.get("ID") + "'");
			}
		}
		if (roleIDs.length() > 0) {
			roleCon.append("M.ATTR_CODE IN ("+ roleIDs +") AND ");
		}
		//逻辑系统管理员判断
		boolean isLogicSysManger = isLogicSysManager(userDetails);
	    if (isLogicSysManger) {
	    	/**逻辑系统管理员部分 ：db2中 行转行处理功能点标识串*/
	    	if("DB2".equals(SystemConstance.DB_TYPE)){
	    		sb.insert(0, "WITH T1(MENUID,OPCODE,NUM) AS ");  
		    	sb.append("( SELECT MENUID,OPCODE,ROW_NUMBER() OVER(PARTITION BY MENUID ORDER BY MENUID) FROM ");  
		    	sb.append("   ( SELECT DISTINCT(AC.CON_CODE) AS OPCODE,CM.ID AS MENUID FROM AUTH_RES_CONTROLLERS AC  ");   
		    	sb.append("			LEFT JOIN FW_FUNCTION FW ON AC.FW_FUN_ID=FW.ID   ");  
		    	sb.append("			LEFT JOIN CNT_MENU CM ON FW.ID = CM.MOD_FUNC_ID  ");  
		    	sb.append("			WHERE CM.ID IN ( SELECT C.ID FROM  CNT_MENU C   ");   
		    	sb.append("			                     WHERE C.APP_ID IN ('" + SystemConstance.LOGIC_SYSTEM_APP_ID + "') ) ");   
		    	sb.append("	  ) T ");  
		    	sb.append("),");  
		    	sb.append("T2(RMENUID,ROPCODE,NUM) AS ");  
		    	sb.append("( SELECT MENUID,CLOB(OPCODE,10000),NUM FROM T1 WHERE NUM = 1 ");  
		    	sb.append("  UNION ALL    ");  
		    	sb.append("  SELECT T2.RMENUID,RTRIM(T2.ROPCODE)||','||T1.OPCODE,T1.NUM  ");  
		    	sb.append("  	FROM T1 , T2  ");  
		    	sb.append("		WHERE T1.NUM = T2.NUM + 1 AND T1.MENUID = T2.RMENUID    ");  
		    	sb.append(") ");  
		    	sb.append("SELECT C.ID,TT.ROPCODE FROM CNT_MENU C ");  	
		    	sb.append("LEFT JOIN  ");  
		    	sb.append("( SELECT RMENUID,ROPCODE FROM T2     ");  
		    	sb.append("  	WHERE  NUM = ( SELECT MAX(NUM) FROM T2 TEMP WHERE TEMP.RMENUID = T2.RMENUID)   ");  
		    	sb.append(" ) TT ON  C.ID =TT.RMENUID ");  
		    	sb.append(" WHERE C.APP_ID IN ('" + SystemConstance.LOGIC_SYSTEM_APP_ID + "')  ");  
	    	}else if("ORACLE".equals(SystemConstance.DB_TYPE)){sb.insert(0, "SELECT WM_CONCAT(OPCODE) AS ROPCODE,MENUID  AS ID FROM ( SELECT DISTINCT(AC.CON_CODE) AS OPCODE,CM.ID AS MENUID" +
	    			" FROM AUTH_RES_CONTROLLERS AC " +
	    			" RIGHT JOIN  FW_FUNCTION FW ON AC.FW_FUN_ID=FW.ID RIGHT JOIN  CNT_MENU CM ON FW.ID = CM.MOD_FUNC_ID " +
	    			" WHERE CM.ID IN (SELECT C.ID FROM  CNT_MENU C " +
	    			" WHERE C.APP_ID IN ('" + SystemConstance.LOGIC_SYSTEM_APP_ID + "') )) group by menuID");  
	    	}else if("MYSQL".equals(SystemConstance.DB_TYPE)){	    	
	    		sb.insert(0, "SELECT GROUP_CONCAT(C.OPCODE) AS ROPCODE,C.MENUID  AS ID FROM ( SELECT DISTINCT(AC.CON_CODE) AS OPCODE,CM.ID AS MENUID" +
		    			" FROM AUTH_RES_CONTROLLERS AC " +
		    			" RIGHT JOIN  FW_FUNCTION FW ON AC.FW_FUN_ID=FW.ID RIGHT JOIN  CNT_MENU CM ON FW.ID = CM.MOD_FUNC_ID " +
		    			" WHERE CM.ID IN (SELECT C.ID FROM  CNT_MENU C " +
		    			" WHERE C.APP_ID IN ('" + SystemConstance.LOGIC_SYSTEM_APP_ID + "') )) AS C group by C.MENUID");  
		    	  
				}
	    } else {
    		sb.append(" SELECT M.RES_CODE AS ID, C.OPERATE_KEY as ROPCODE FROM AUTH_RES_ATTR_DATA M "+
    				  "LEFT JOIN auth_res_control_attr_data C ON C.ATTR_CODE=M.ATTR_CODE AND C.RES_CODE=M.RES_CODE" +
    				  " WHERE 1=1 AND M.APP_ID = " + SystemConstance.LOGIC_SYSTEM_APP_ID  +
    				  " AND " + roleCon +"  1=1 ");
	    }
	    
		QueryHelper qh;
		List names;
		try {
			qh = new QueryHelper(sb.toString(), dsOracle.getConnection());
			names = (List) qh.getJSON().get("data");
			if (names != null && names.size() > 0) {
				Map res = new HashMap();
				for (int i = 0; i < names.size(); i ++ ) {
					String menuID = (String)((Map) names.get(i)).get("ID");
					res.put(menuID, getAuthList((List)res.get(menuID), menuID, (String)((Map) names.get(i)).get("ROPCODE"), isLogicSysManger));
				}
				return res;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	 }

	 /**
	  * 获取权限信息串放入MenuID匹配的list中
	  **/
	@SuppressWarnings("unchecked")
	private List getAuthList(List authList, String MenuID, String AuthStr, boolean isLogicSysManger) {
		if (authList == null) {
			authList = new ArrayList();
		}
		if (AuthStr != null && !"".equals(AuthStr)) {			 
			if (isLogicSysManger) {
				/**用sql将 AuthStr ：拼接为add,del,modify 
				 * 由下面方法转换为去重单个串放入list中*/
				String[] authTem = AuthStr.split(",");
				for (int i = 0; i < authTem.length; i ++) {  
					if (!authList.contains(authTem[i]) && (null != authTem[i] || "".equals(authTem[i]))) {
						authList.add(authTem[i]);
					}
				}
			} else {
				/**db中 AuthStr ： ["add","del"] 转换为去重单个串放入list中*/
				JSONArray jsonArray = JSONArray.fromObject(AuthStr);
				int iSize = jsonArray.size();   
				List jsonList = jsonArray.subList(0, iSize);				
				for (int j = 0; j < iSize; j ++) {
					if (!JSONNull.getInstance().equals(jsonList.get(j))) {
						String jsonObj = (String) jsonList.get(j);   
						if (!authList.contains(jsonObj) && (null != jsonObj || "".equals(jsonObj))) {
							authList.add(jsonObj);						
						}
					}
				}
			}			
		}
		
		return authList;
	}
	
	 /**
	  * 逻辑系统管理判断
	  * @param AuthUser
	  * @return boolean
	  **/
	@SuppressWarnings("unchecked")
	private boolean isLogicSysManager(AuthUser userDetails) {
		
		boolean isLogicSysManagerFlag = false;
		for (int i = 0; i < userDetails.getRolesInfo().size(); i ++ ) {
			Map tempMap = (HashMap) userDetails.getRolesInfo().get(i);
			String roleCode = (String) tempMap.get("ROLE_CODE");
			//TODO 是否合理
			if ( SystemUserConstance.LOGIC_SYSTEM_USER_ID.equals(roleCode) ||
				 SystemUserConstance.SUPER_SYSTEM_USER_ID.equals(roleCode) ||
				 SystemUserConstance.SYSTEM_ADMIN_ID.equals(roleCode)) {
				isLogicSysManagerFlag = true;
				break;
			}
		}
		
		return isLogicSysManagerFlag;
	}
	/**
	  * 初始化策略类
	  * @param credentialStrategy
	  **/
	@SuppressWarnings("unchecked")
	public void initCreStrategy(List<CredentialStrategy>  credentialStrategy) {
		if (credentialStrategy != null) {
			StringBuffer sb 	  = new StringBuffer();
			StringBuffer temp 	  = new StringBuffer();
			StringBuffer whereSQL = new StringBuffer();
			
			/**取策略类ID拼接成in条件串*/
			for (int i = 0; i < credentialStrategy.size(); i ++ ) {
				CredentialStrategy cs = credentialStrategy.get(i);
				if (temp.length() == 0 ) {
					temp.append("'" + cs.CreStrategyID + "'");
				} else {
					temp.append(",'" + cs.CreStrategyID + "'");
				}
			}
			if (temp.length() > 0) {
				whereSQL.append(" WHERE  ID IN ("+ temp +") ");
			}
			sb.insert(0, "SELECT ID, NAME, ENABLE_FLAG, DETAIL, ACTIONTYPE FROM OCRM_F_SYS_CREDENTIAL_STRATEGY CS ");
			sb.append(whereSQL);
			QueryHelper qh;
			List names;
			try {
				qh = new QueryHelper(sb.toString(), dsOracle.getConnection());
				names = (List) qh.getJSON().get("data");
				if (names != null && names.size() > 0) {
					Map res = new HashMap();
					for (int i = 0; i < names.size(); i ++ ) {
						res = (Map) names.get(i);
						for (int j = 0; j < credentialStrategy.size(); j ++ ) {
							CredentialStrategy cs = credentialStrategy.get(j);
							if (cs.CreStrategyID.equals(res.get("ID"))) {
								cs.CreStrategyDetail = (String)res.get("DETAIL");
								cs.ActionType        = (String)res.get("ACTIONTYPE");
								cs.CreStrategyName   = (String)res.get("NAME");
								cs.enable = false;
								if (((String)res.get("ENABLE_FLAG")).equals("1")) {
									cs.enable = true;
								}
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/** 冻结用户 */
	public void freezingUser(AuthUser userDetails) {
		if (userDetails.getUsername() != null ) {
			StringBuilder jql = new StringBuilder("UPDATE AdminAuthAccount a SET a.userState=0 WHERE a.accountName='" + userDetails.getUsername() + "'");
			Map<String,Object> values = new HashMap<String,Object>();
			userManagerService.batchUpdateByName(jql.toString(), values);
		}
	}
	
	/** 冻结用户 */
	public void freezingUser(String userName) {
		if (userName != null ) {
			StringBuilder jql = new StringBuilder("UPDATE AdminAuthAccount a SET a.userState=0 WHERE a.accountName='" + userName + "'");
			Map<String,Object> values = new HashMap<String,Object>();
			userManagerService.batchUpdateByName(jql.toString(), values);
		}
	}
	
	/** 更新用户登录时间 */
	public void refreshLoginTime(AuthUser userDetails) {
		if (userDetails.getUsername() != null ) {
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String currentTime =  dateTimeFormat.format(new Date());
	        StringBuilder jql = new StringBuilder("UPDATE AdminAuthAccount a SET a.lastLoginTime='"+currentTime+"' WHERE a.accountName='" + userDetails.getUserId() + "'");
			Map<String,Object> values = new HashMap<String,Object>();
			userManagerService.batchUpdateByName(jql.toString(), values);
		}
	}
	
	/** 更新用户登录登出日志 */
	public void addLoginLogInfo(AuthUser userDetails, int opType) {
			LogService.loginfo.setLoginIp(((IAuser)userDetails).getCurrentIP() );
			LogService.loginfo.setLogTypeId(Long.valueOf(opType+""));
			LogService.loginfo.setContent(OperateTypeConstant.getOperateText(opType)+":"+OperateTypeConstant.getOperateText(opType)+"成功");
			LogService.loginfo.setAfterValue("");
			LogService.addLog(userDetails);
	}
	
	/** 登录系统异常信息日志 */
	public void addLoginExceptionInfo(AuthUser userDetails, AuthenticationException authException) {
			LogService.loginfo.setLoginIp(((IAuser)userDetails).getCurrentIP() );
			LogService.loginfo.setLogTypeId(Long.valueOf(OperateTypeConstant.LOGIN_SYS+""));
			LogService.loginfo.setContent(OperateTypeConstant.getOperateText(OperateTypeConstant.LOGIN_SYS)+":["+userDetails.getUsername()+"]"+authException.getMessage());
			LogService.loginfo.setAfterValue("");
			LogService.addLog(userDetails);
	}
	/***查询用户有权限的角色*/
	@SuppressWarnings("unchecked")
	public Map getRolesInfo(String sql) {
		
		QueryHelper qh;
		Map names = new HashMap();
		try {
			qh = new QueryHelper(sql, dsOracle.getConnection());
			names.put("json", qh.getJSON());
			if (names != null && names.size() > 0) {
				 
				return names;
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 通过角色ID，查询角色信息
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings({"unchecked" })
	public List getRoleInfo(String roleId) {
		if (roleId != null && roleId.length() > 0) {
			StringBuffer sql = new StringBuffer(
					"SELECT * FROM admin_auth_role A WHERE A.Id IN ("+ roleId +")");
			QueryHelper qh;
			List roles;
			try {
				qh = new QueryHelper(sql.toString(), dsOracle.getConnection());
				roles = (List) qh.getJSON().get("data");
				if (roles != null && roles.size() > 0) {
					return roles;
				} else
					return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}
	
}
