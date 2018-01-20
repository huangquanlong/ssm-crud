package com.yuchengtech.crm.constance;

import com.yuchengtech.bob.upload.FileTypeConstance;

/**
 * CRM系统级别静态变量
 * @author WILLJOE
 * @date 2012-9-20
 */
public class SystemConstance {
	
	/**数据库类型*/
	public static final String DB_TYPE = FileTypeConstance.getBipProperty("dbType");
	/**逻辑系统ID*/
	public static final String LOGIC_SYSTEM_APP_ID = "62";
	
	/**授权资源ID*/
	public static final String LOGIC_SYSTEM_RES_ID = "62";
	
	/**授权属性ID*/
	public static final String LOGIC_SYSTEM_ATTR_ID = "64";
}
