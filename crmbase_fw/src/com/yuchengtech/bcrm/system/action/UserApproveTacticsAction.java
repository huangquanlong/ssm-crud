package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.OcrmFSysCredentialStrategy;
import com.yuchengtech.bcrm.system.service.UserApproveTacticsService;
import com.yuchengtech.bob.common.CommonAction;
/***
 * 用户认证策略
 * @author zhangmin
 *
 */
@SuppressWarnings("serial")
@Action("/userApproveTactics")
public class UserApproveTacticsAction extends CommonAction {
	@Autowired
	private UserApproveTacticsService userApproveTacticsService;
	@Autowired
	public void init(){
		model = new OcrmFSysCredentialStrategy();
		setCommonService(userApproveTacticsService);
	}
	/***
	 * 保存用户认证策略
	 * @return
	 */
	 public String saveData()
	 {
		 ActionContext ctx = ActionContext.getContext();
         request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
         String saveStr = request.getParameter("saveStr");
         userApproveTacticsService.saveData(saveStr);
		 return "";
	 }
}