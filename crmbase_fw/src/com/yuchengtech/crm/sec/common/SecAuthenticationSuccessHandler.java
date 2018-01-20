package com.yuchengtech.crm.sec.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.yuchengtech.bcrm.common.service.OrgSearchService;
import com.yuchengtech.bcrm.common.service.SecGrantService;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.OperateTypeConstant;
import com.yuchengtech.crm.dataauth.service.DataAuthInfo;
/**
 * 登录成功处理类
 * @author changzh@yuchengtech.com
 * @date 2012-11-08
 * */
public class SecAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private static Logger log = Logger.getLogger(SecAuthenticationSuccessHandler.class);
	
	@Autowired
	private SecGrantService secGrantService;
	
	@Autowired
	private OrgSearchService orgSearchService;
	
	@Autowired
	private DataAuthInfo dataAuthInfo;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  /**默认跳转策略类*/

	private String defaultSuccessUrl;  /**登录成功跳转URL*/
	
	private String defaultFailureUrl;  /**登录失败跳转URL*/
	
	public SecAuthenticationSuccessHandler() {
		 
	}
	/**登录成功处理方法*/
	@SuppressWarnings("unchecked")
	public void onAuthenticationSuccess(HttpServletRequest request,  
	            HttpServletResponse response, Authentication authentication)  
	            throws ServletException, IOException { 
		HttpSession session = request.getSession();  
		AuthUser auth = (AuthUser) authentication.getPrincipal();
        Map forbiddenUserMap = (Map) session.getAttribute("forbiddenUserMap"); 
        /**用户密码连续错误达到上限map是否包含当前用户*/
        boolean isIncludFlag = false;
        if (forbiddenUserMap != null) {
        	if (auth.getUserId().equals(forbiddenUserMap.get(auth.getUserId()))) {
        		isIncludFlag = true;
        	}
        }
        String deviceId = request.getParameter("deviceId");
        if (deviceId != null) {
        	auth.putAttribute("deviceId", deviceId);
        	
        	//TODO 真正的设备ID
			auth.putAttribute("mobileFlag", "ipad");
			//auth.setRolesInfo(orgSearchService.getRoleInfo((List) auth.getAttribute("mobileRoles")));
			//auth.setAuthorities((List) auth.getAttribute("mobileRoles"));
			//auth.setGrant(secGrantService.getGrantMenus("MENU_RES",auth));
			//auth.setAuthInfos(dataAuthInfo.LoadAuthInfo(auth));
			//auth.setDataAuthInfo(DataAuthManager.getInstance().getDataAuthInfo(auth));
			/** 用户动态设置session级别信息加载*/
			
        	try {
				//response.setContentType("text/html;charset=UTF-8");   
				response.setContentType("application/json;charset=UTF-8");   
				response.setHeader("Cache-Control", "no-cache"); 
				response.setCharacterEncoding("UTF-8");
				Map<String, Object> loginInfo = new HashMap<String, Object>();
				loginInfo.put("status", "00");
				//loginInfo.put("errors", "登录成功!");
				loginInfo.put("datas", auth);
				JSONObject jsonObj  = JSONObject.fromObject(loginInfo);
				response.getWriter().print(jsonObj);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
				log.info("mobile user login exception!");
			}
        } else {
	        /**是否禁止用户登录*/
	        if (isIncludFlag) {
	        	redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
	        } else {
	        	doLoginSuccess(request);
	    		redirectStrategy.sendRedirect(request, response, defaultSuccessUrl);
	        }
	    }
		
	}
	public String getDefaultSuccessUrl() {
		return defaultSuccessUrl;
	}
	public void setDefaultSuccessUrl(String defaultSuccessUrl) {
		this.defaultSuccessUrl = defaultSuccessUrl;
	} 
	/**登录成功业务处理*/
	public void doLoginSuccess(HttpServletRequest request) {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/**更新登录时间*/
		secGrantService.refreshLoginTime(auth);
		/**登录成功更新日志信息*/
		secGrantService.addLoginLogInfo(auth, OperateTypeConstant.LOGIN_SYS);
		if (SystemUserConstance.MULTI_ROLE_LOGIN.equals(auth.getLoginType())) {
			SecLoaderManager.getInstance().initialize();
		}
//		/***登录成功更新在线用户信息*/
//        ServletContext servletContext =  request.getSession().getServletContext();
//        Cache userOnlineCache = (Cache) servletContext.getAttribute(UserOnlineManager.cacheName);
//        Element element = userOnlineCache.get(UserOnlineManager.cacheKey);
//        List<AuthUser> list = new ArrayList();
//        
//        if (element == null) {
//        	element = new Element(UserOnlineManager.cacheKey, list);
//        } else {
//			list = (List<AuthUser>)element.getObjectValue();
//        }
//        list.add((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        userOnlineCache.put(element);
//        servletContext.setAttribute(UserOnlineManager.cacheName, userOnlineCache);
	}
	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}
	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

}
