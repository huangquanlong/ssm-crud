package com.yuchengtech.crm.sec.common;

/**
 * CRM系统级别静态变量
 * @author CHANGZH
 * @date 2012-10-24
 */
public class SystemUserConstance {
	
	/**多角色登录*/
	public static final String MULTI_ROLE_LOGIN = "0";
	/**单角色登录*/
	public static final String SINGLE_ROLE_LOGIN = "1";
	/**逻辑系统管理员ID*/
	public static final String LOGIC_SYSTEM_USER_ID = "logicSystemManager";
	/**超级系统管理ID*/
	public static final String SUPER_SYSTEM_USER_ID = "BIPSuperRole";
	/**系统管理员角色*/
	public static final String SYSTEM_MANAGER_ROLE = "usermanager";
	/**普通管理员角色*/
	public static final String NORMAL_MANAGER_ROLE = "normaluser";
	/**admin ID*/
	public static final String SYSTEM_ADMIN_ID = "admin";
	/**首次登陆策略 ID*/
	public static final String CS_FIRST_LOGIN_ID = "1";
	/**强制修改口令策略 ID*/
	public static final String CS_PSW_MODIFY_ID = "2";
	/**登陆IP策略 ID*/
	public static final String CS_LOGIN_IP_ID = "3";
	/**口令错误策略 ID*/
	public static final String CS_PSW_WRONG_ID = "4";
	/**登录时间段策略 ID*/
	public static final String CS_LOGIN_TIME_ID = "5";
	/**在线用户策略 ID*/
	public static final String CS_USERONLINE_ID = "11";
	/**用户状态 启用*/
	public static final String USER_STATE_ENABLE = "1";
	/**用户状态冻结*/
	public static final String USER_STATE_FREEZING = "0";
	
}
