package com.yuchengtech.crm.sec.ctxsession;

import org.apache.log4j.Logger;

import com.yuchengtech.bob.vo.AuthUser;
/**
 * 用户动态设置session级别信息管理器实现类
 * 请根据业务需要重写此类方法   或者 重新实现ICtxSessionManager接口
 * @author CHANGZH
 * @date 2013-01-05
 * 
 **/
public class CtxSessionManager implements ICtxSessionManager {

	/**日志信息*/
	private static Logger log = Logger.getLogger(CtxSessionManager.class);
	
	/**增加参数方法*/
	public void addCtxSessionParam(AuthUser authUser) {
		/**参数名称*/
		String paramName = "SESSIONEXTENDSPARAM";
		/**
		 * 参数值，参数类型为object
		 **/
		String paramValue = "例子：用户动态设置session级别信息，如：导入、导出线程句柄。";
		log.info("[paramName:"+ paramName + " add to ctxSessionMap]");
		/**增加参数方法 AuthUser*/
		authUser.putAttribute(paramName, paramValue);
		/**取得参数方法 AuthUser*/
		log.info("[paramName:"+ paramName + "][paramValue:"
				+ authUser.getAttribute(paramName) + "]");
		
	}
	
}
