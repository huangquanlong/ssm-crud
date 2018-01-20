package com.yuchengtech.crm.sec.common;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext {
	
	/***单例对象*/
    private static SecurityContext instance; 
   
    /***构造方法*/
    private SecurityContext() {
    	
    }
    /***获取单例对象*/
    public static synchronized SecurityContext getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new SecurityContext();
        }
        return instance;
    }
    /***获取用户session*/
    public Object getUserSession() {
    	
    	return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
}
