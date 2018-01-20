package com.yuchengtech.crm.sec.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
/**
 * 登录失败处理类
 * @author changzh@yuchengtech.com
 * @date 2012-11-08
 * */
public class SecAuthenticationFailureHandler implements AuthenticationFailureHandler{

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  /**默认跳转策略类*/

	private String defaultFailureUrl;  /**失败后跳转URL*/

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	/**登录失败处理方法*/
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authenticationexception)
			throws IOException, ServletException {
		doLoginFailure();
		redirectStrategy.sendRedirect(request, response, defaultFailureUrl);  
	}

	/**登录失败处理类*/
	private void doLoginFailure() {
		 
	}

}
