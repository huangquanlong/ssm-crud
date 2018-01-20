package com.yuchengtech.bcrm.system.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.core.UserOnlineManager;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.exception.BizException;
/**
 * 在线用户查询
 * @author CHANGZH
 * @since 2013-02-26
 */

@SuppressWarnings("serial")
@Action("/userOnlineAction")
public class UserOnlineAction extends CommonAction{
	/***
	 * 
	 * 取得当前在线用户信息
	 * @return 用户信息列表
	 */
	@SuppressWarnings("unchecked")
	public String Query() {
		 ActionContext ctx = ActionContext.getContext();
		 request           = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		 this.setJson(request.getParameter("condition"));
		 String   userName = (String) this.getJson().get("cname");//用户名
		 String   unitName = (String) this.getJson().get("unitName");//用户机构名
		 
		 //20140625,XXX.startsWith(null) 会报空指针异常，故将null设置为"";
		 if(userName == null){
			 userName="";
		 }
		 if(unitName == null){
			 unitName="";
		 }
		try {
			Cache userOnlineCache = UserOnlineManager.getInstance().getUserOnlineCache();
	        Element element = userOnlineCache.get(UserOnlineManager.cacheKey);
            if(this.json!=null)
    			this.json.clear();
    		else 
    			this.json = new HashMap<String,Object>();  
            
            List<AuthUser> list = new ArrayList();
            List<AuthUser> queryList = new ArrayList();
            Map<String, Object> map = new HashMap<String,Object>();
    		if (element != null) {
    			list = (List<AuthUser>) element.getObjectValue();
    		}
    		//按输入条件过滤
    		for (int i = 0; i < list.size(); i++) {
        		AuthUser au = list.get(i);
        		if ((userName == null || "".equals(userName)) && (unitName == null || "".equals(unitName))) {
        			queryList = list;
        			break;
        		} else if ((unitName == null || "".equals(unitName)) && au.getCname().startsWith(userName)) {
        			queryList.add(list.get(i));
        		} else if ((userName == null || "".equals(userName)) && au.getUnitName().startsWith(unitName) ) {
        			queryList.add(list.get(i));
        		} else if (au.getUnitName().startsWith(unitName) && au.getCname().startsWith(userName)) {
        			queryList.add(list.get(i));
        		}
            }
    		
    		try {
    			map.put("data", queryList);
    			this.json.put("json",map);
    		} catch(Exception e) {
    			throw new BizException(1,2,"1006","userOnlineAction query exception!");
    		}
             
        }catch(Exception e){
        	e.printStackTrace();        	
        	throw new BizException(1,2,"1002",e.getMessage());
        }
        return "success";
	} 
	/****
	 * 
	 * 取得在线最大用户数
	 * 当前在线用户数
	 */
	@SuppressWarnings("unchecked")
	public String getOnlineMax() {
		try {
            if(this.json!=null)
    			this.json.clear();
    		else 
    			this.json = new HashMap<String,Object>();  
            
            Map<String, Object> map = new HashMap<String,Object>();
    		Cache userOnlineCache = UserOnlineManager.getInstance().getUserOnlineCache();
            Element element = userOnlineCache.get(UserOnlineManager.cacheKey);
            List<AuthUser> list = new ArrayList();
            if (element != null) {
    			list = (List<AuthUser>)element.getObjectValue();
            }
            int onlineNum = list.size();
            Element onlineElement = userOnlineCache.get(UserOnlineManager.onlineNumCacheKey);
            if (onlineElement != null) {
            	onlineNum = (Integer)onlineElement.getObjectValue();
            }
    		try {
    			map.put("onlineMax", onlineNum);
    			map.put("onlineNum", list.size());
    			this.json.put("json",map);
    		} catch(Exception e) {
    			throw new BizException(1,2,"1006","userOnlineAction onlineMax exception!");
    		}
             
        }catch(Exception e){
        	e.printStackTrace();        	
        	throw new BizException(1,2,"1002",e.getMessage());
        }
        return "success";
	}
	    
}