package com.yuchengtech.crm.sec.credentialstrategy;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
/**
 * 密码连续错误策略类
 * @author CHANGZH@YUCHENGTECH.COM
 * @date 2012-11-05
 * 
 **/
public class PswWrongStrategy extends CredentialStrategy implements AuthenticationFailureHandler {
	
	private static Logger log = Logger.getLogger(PswWrongStrategy.class);
	
	PswWrongStrategy () {
		CreStrategyID = SystemUserConstance.CS_PSW_WRONG_ID;
	}
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  

	private String defaultFailureUrl;  

	private int maxTryCount;
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		//连续密码错误时是用AuthenticationFailureHandler监听的因此这里返回false
		boolean isCredentialStrategy = false;
		isCredentialStrategy = false;
		return isCredentialStrategy;
	}

	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authenticationexception)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		String userName =  request.getParameter("j_username");
		session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new BadCredentialsException(authenticationexception.getMessage()));
		if (enable) {	
			String currentLoginName = (String) session.getAttribute("currentLoginName");
	        Integer tryCount = (Integer) session.getAttribute("maxTryCount"); 
	        /**最近登录用户及连续错误次数判断*/
			if(!userName.equals(currentLoginName) && !CreStrategyDetail.equals("1")) {  
	            session.setAttribute("maxTryCount", 1);//增加失败次数  
	            session.setAttribute("currentLoginName", userName);
	            session.removeAttribute("maxTryFlag");
	        } else {
	        	/**是否超过连续错误次数*/
	            if(tryCount > getMaxTryCount() - 1) {  
	            	doActionTypeByUserName(ActionType, this.CreStrategyName, userName, session);
	            	Map forbiddenUserMap = new HashMap();
	            	if (session.getAttribute("forbiddenUserMap") != null) {
	            		forbiddenUserMap = (Map) session.getAttribute("forbiddenUserMap");
	            	} 
	            	forbiddenUserMap.put(currentLoginName, userName);
	            	session.setAttribute("forbiddenUserMap", forbiddenUserMap);
	            }
	            session.setAttribute("maxTryCount", tryCount + 1);
	        }
		} 
		String deviceId = request.getParameter("deviceId");
		if (deviceId != null) {
        	try {
				//response.setContentType("text/html;charset=UTF-8");   
				response.setContentType("application/json;charset=UTF-8");   
				response.setHeader("Cache-Control", "no-cache"); 
				response.setCharacterEncoding("UTF-8");
				Map<String, Object> loginInfo = new HashMap<String, Object>();
				loginInfo.put("status", "99");
				loginInfo.put("errors", authenticationexception.getMessage());
				JSONObject jsonObj  = JSONObject.fromObject(loginInfo);
				response.getWriter().print(jsonObj);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
				log.info("mobile user login exception!");
			}
        } else {
        	redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
        }
		
	}
	
	/**策略执行动作*/
	public void doActionTypeByUserName (String ActionType, String message, String userName, HttpSession session) {
		if (ICredentialStrategy.ACTIONTYPE_WARN.equals(ActionType)) {
			session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new BadCredentialsException("[" + CreStrategyName + "]:" + "超过最大登录尝试次数"+getMaxTryCount()+",用户禁止登录"));
		} else if (ICredentialStrategy.ACTIONTYPE_FREEZING.equals(ActionType)) {
			getSecGrantService().freezingUser(userName);
			session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,new BadCredentialsException("[" + CreStrategyName + "]:" + "超过最大登录尝试次数"+getMaxTryCount()+",用户已被冻结,请联系管理员", null));
		} else if (ICredentialStrategy.ACTIONTYPE_FORBIDDEN.equals(ActionType)) {
			session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new BadCredentialsException("[" + CreStrategyName + "]:" + "超过最大登录尝试次数"+getMaxTryCount()+",用户已被禁止登录,请联系管理员", null));
		}
	}

	public void setMaxTryCount(int maxTryCount) {
		this.maxTryCount = maxTryCount;
	}

	public int getMaxTryCount() {
		if (CreStrategyDetail == null || "".equals(CreStrategyDetail)) {
			return 0;
		} else {
			return Integer.parseInt(CreStrategyDetail);
		}
		
	}
	
	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}
}

