package com.yuchengtech.crm.sec.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.yuchengtech.bcrm.common.service.SecGrantService;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.OperateTypeConstant;
/**
 * 登出成功处理类
 * @author changzh@yuchengtech.com
 * @date 2012-11-12
 * */
public class SecLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	private static Logger log = Logger.getLogger(SecLogoutSuccessHandler.class);
	
	@Autowired
	private SecGrantService secGrantService;
	
	public SecLogoutSuccessHandler()
    {
    }
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  

	private String defaultLogoutUrl; 

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException
    {
        /**登出成功更新日志信息*/
    	doLogoutSuccess(authentication);
       
        String deviceId = request.getParameter("deviceId");
        if (deviceId != null) {
        	try {
				//response.setContentType("text/html;charset=UTF-8");   
				response.setContentType("application/json;charset=UTF-8");   
				response.setHeader("Cache-Control", "no-cache"); 
				response.setCharacterEncoding("UTF-8");
				Map<String, Object> logoutInfo = new HashMap<String, Object>();
				logoutInfo.put("status", "00");
				logoutInfo.put("datas", deviceId+"logout success!");
				JSONObject jsonObj  = JSONObject.fromObject(logoutInfo);
				response.getWriter().print(jsonObj);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
				log.info("mobile user login exception!");
			}
        } else {
        	redirectStrategy.sendRedirect(request, response, defaultLogoutUrl);
        }
    }
    
    /**登出成功业务处理*/
	public void doLogoutSuccess(Authentication authentication) {
		//是否正常退出
		if (authentication != null) {
			 AuthUser auth = (AuthUser) authentication.getPrincipal();
			 secGrantService.addLoginLogInfo(auth, OperateTypeConstant.LOGOUT_SYS);
		}
	}

	public String getDefaultLogoutUrl() {
		return defaultLogoutUrl;
	}

	public void setDefaultLogoutUrl(String defaultLogoutUrl) {
		this.defaultLogoutUrl = defaultLogoutUrl;
	}

}
