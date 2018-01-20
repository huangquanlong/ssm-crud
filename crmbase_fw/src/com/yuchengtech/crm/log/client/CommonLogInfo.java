package com.yuchengtech.crm.log.client;

import net.sf.json.JSONObject;

import com.yuchengtech.bob.vo.AuthUser;

/**
 * 日志信息
 * @author CHANGZH
 * @since 2014-07-29
 */
public class CommonLogInfo {

	/** 日志信息 */
	private Object logInfo;

	/** 用户信息 */
	private AuthUser authUser;

	/**
	 * 将LogInfo对象转为json格式字符串输出 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		JSONObject jo = JSONObject.fromObject(this);
		return jo.toString();
	}

	public void setLogInfo(Object logInfo) {
		this.logInfo = logInfo;
	}

	public Object getLogInfo() {
		return logInfo;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

}
