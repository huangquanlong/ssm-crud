package com.yuchengtech.crm.sec.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
/**
 * 登出过滤类
 * @author changzh@yuchengtech.com
 * @date 2012-11-12
 * */
public class SecLogoutFilter extends LogoutFilter{
	
	private SecLogoutSuccessHandler secLogoutSuccessHandler;
	
	private List<LogoutHandler> sechandlers;
	
	public SecLogoutFilter(SecLogoutSuccessHandler logoutSuccessHandler,
			LogoutHandler[] handlers) {
		
		super(logoutSuccessHandler, handlers);
		setSecLogoutSuccessHandler(logoutSuccessHandler);
		this.sechandlers = Arrays.asList(handlers);
		
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException
	{
	    HttpServletRequest request = (HttpServletRequest)req;
	    HttpServletResponse response = (HttpServletResponse)res;
	    if(requiresLogout(request, response))
	    {
	        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if(logger.isDebugEnabled())
	            logger.debug((new StringBuilder()).append("Logging out user '").append(auth).append("' and transferring to logout destination").toString());
	        LogoutHandler handler;
	        for(Iterator<LogoutHandler> it = this.sechandlers.iterator(); it.hasNext(); handler.logout(request, response, auth))
	            handler = (LogoutHandler)it.next();
	
	        secLogoutSuccessHandler.onLogoutSuccess(request, response, auth);
	        return;
	    } else
	    {
	        chain.doFilter(request, response);
	        return;
	    }
	}

	public void setSecLogoutSuccessHandler(SecLogoutSuccessHandler secLogoutSuccessHandler) {
		this.secLogoutSuccessHandler = secLogoutSuccessHandler;
	}

	public SecLogoutSuccessHandler getSecLogoutSuccessHandler() {
		return secLogoutSuccessHandler;
	}

}
