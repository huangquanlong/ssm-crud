package com.yuchengtech.crm.constance;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <pre>
 * Title:定义一些操作类型对应的常数
 * Description: 定义一些操作类型对应的常数，用于记载日志的时候使用
 * </pre>
 * 
 * @author gongguanyuan gonggy@yuchengtech.com
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public class OperateTypeConstant {

	/**
	 * 登录系统
	 */
	public final static int LOGIN_SYS = 1;

	/**
	 * 登出系统
	 */
	public final static int LOGOUT_SYS = 2;

	/**
	 * 访问菜单
	 */
	public final static int VISIT_MENU = 3;

	/**
	 * 发布报表
	 */
	public final static int ISSUE_REPORT = 4;

	/**
	 * 访问报表
	 */
	public final static int VISIT_REPORT = 5;

	/**
	 * 移动报表
	 */
	public final static int MOVE_REPORT = 6;

	/**
	 * 删除报表
	 */
	public final static int DELETE_REPORT = 7;

	/**
	 * 重命名报表
	 */
	public final static int RENAME_REPORT = 8;

	/**
	 * 增加目录
	 */
	public final static int ADD_FOLDER = 9;

	/**
	 * 删除目录
	 */
	public final static int DELETE_FOLDER = 10;

	/**
	 * 移动目录
	 */
	public final static int MOVE_FOLDER = 11;

	/**
	 * 重命名目录
	 */
	public final static int RENAME_FOLDER = 12;
	
	/**
	 * 用户的登录IP地址
	 */
	//public final static int LONGIN_IP = 13;
	
	/**
	 * 授权日志
	 */
	public final static int AUTHORIZE_LOG = 14;
	
	/**
	 * 报表导出
	 */
	public final static int REPORT_EXPORT = 15;
	
	/**
	 * 操作用户
	 */
	public final static int OPERATE_ACCOUNT = 16;
	/*
	 * 记录增删改行级日志
	 */
	public static final int LOG_RECORD = 99;
	/*
	 * 查询业务数据日志
	 */
	public static final int LOG_DATAQUERY = 17;
	/*
	 * 按钮操作日志
	 */
	public static final int LOG_BUTTON = 18;

	/**
	 * 操作类型Map
	 */
	public final static Map<Integer, String> OPERATE_TYPE_MAP = new HashMap<Integer, String>();

	static {
		OPERATE_TYPE_MAP.put(LOGIN_SYS, "登录系统");
		OPERATE_TYPE_MAP.put(LOGOUT_SYS, "登出系统");
		OPERATE_TYPE_MAP.put(VISIT_MENU, "访问菜单");
		//OPERATE_TYPE_MAP.put(ISSUE_REPORT, "发布报表");
		OPERATE_TYPE_MAP.put(VISIT_REPORT, "访问报表");
		//OPERATE_TYPE_MAP.put(MOVE_REPORT, "移动报表");
		//OPERATE_TYPE_MAP.put(DELETE_REPORT, "删除报表");
		//OPERATE_TYPE_MAP.put(RENAME_REPORT, "重命名报表");
		//OPERATE_TYPE_MAP.put(ADD_FOLDER, "增加目录");
		//OPERATE_TYPE_MAP.put(DELETE_FOLDER, "删除目录");
		//OPERATE_TYPE_MAP.put(MOVE_FOLDER, "移动目录");
		//OPERATE_TYPE_MAP.put(RENAME_FOLDER, "重命名目录");
		//OPERATE_TYPE_MAP.put(LONGIN_IP, "登录IP地址");
		OPERATE_TYPE_MAP.put(AUTHORIZE_LOG, "授权日志");
		OPERATE_TYPE_MAP.put(REPORT_EXPORT, "报表导出");
		OPERATE_TYPE_MAP.put(OPERATE_ACCOUNT, "操作用户");
		OPERATE_TYPE_MAP.put(LOG_RECORD,"记录操作日志");
		OPERATE_TYPE_MAP.put(LOG_DATAQUERY,"查询数据日志");
		OPERATE_TYPE_MAP.put(LOG_BUTTON,"按钮操作日志");

	}

	public static String getOperateText(int key) {
		return OPERATE_TYPE_MAP.get(key);
	}

}
