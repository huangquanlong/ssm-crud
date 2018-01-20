package com.yuchengtech.crm.sec.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.Assert;
/**
 * 登出handler
 * @author changzh@yuchengtech.com
 * @date 2012-11-08
 * */
public class SecLogoutHandler extends SecurityContextLogoutHandler{
	
	 public SecLogoutHandler()
    {
        invalidateHttpSession = true;
    }
	
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        Assert.notNull(request, "HttpServletRequest required");
        if(invalidateHttpSession)
        {
            HttpSession session = request.getSession(false);
            if(session != null)
                session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }
	
	public boolean isInvalidateHttpSession()
    {
        return invalidateHttpSession;
    }

    public void setInvalidateHttpSession(boolean invalidateHttpSession)
    {
        this.invalidateHttpSession = invalidateHttpSession;
    }

    private boolean invalidateHttpSession;

}
