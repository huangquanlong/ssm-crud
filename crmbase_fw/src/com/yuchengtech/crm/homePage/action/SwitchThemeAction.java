package com.yuchengtech.crm.homePage.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.homePage.model.AdminAuthAccountCfg;
import com.yuchengtech.crm.homePage.service.UserConfigService;

/**
 *  首页切换
 * @author CHANGZH@YUCHENGTECH.COM
 * @date 2013-10-28
 */

@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/switchThemeAction")
public class SwitchThemeAction extends CommonAction {

	@Autowired
	private UserConfigService userConfigService;

	@Autowired
	public void init() {
		model = new AdminAuthAccountCfg();
		setCommonService(userConfigService);
	}
	
	public String updateUserCfg() {
		
		ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
        String themeId = request.getParameter("themeId");
        String colorId = request.getParameter("colorId");
		if (themeId != null) {
			userConfigService.updateUserCfg(themeId);
		}
		if (colorId != null) {
			userConfigService.updateUserCfgColor(colorId);
			this.getUserSession().putAttribute("THEME_COLOR_ID",colorId);
		}
		return "success";		
	}
	
	
	public String getUserCfg() {
		String themeId = userConfigService.getUserCfg();
		if(this.json!=null)
    		this.json.clear();
    	else 
    		this.json = new HashMap<String,Object>(); 
    	this.json.put("themeId", themeId);
		return "success";		
	}
	
}
