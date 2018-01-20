package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.service.PasswordChangeService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 密码校验策略
 * 重置密码模块action
 * @author wangwan
 * @since 2012.11.08
 */
@SuppressWarnings("serial")
@Action("passwordChangeAction")
@Results({
    @Result(name="success",type="redirectAction", params = {"actionName" , "workplatnotice", "success", "false"})
})
public class PasswordChangeAction extends CommonAction {
	@Autowired
	private PasswordChangeService PasswordChangeService;
	@Autowired
	public void init(){
		setCommonService(PasswordChangeService);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}

	/*
	 * 重置密码模块修改方法
	 * 
	 */
	 public String authPassword()
	 {
		 ActionContext ctx = ActionContext.getContext();
         request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
         String oldPassword = request.getParameter("oldPassword");  //获取密码
         String oldPassword2 = request.getParameter("oldPassword2");  //获取密码
         String userId = request.getParameter("userId");
         String password = request.getParameter("password");
         String updateUser = request.getParameter("updateUser");
         String id = request.getParameter("id");
         String authEnableFlag = request.getParameter("authEnableFlag");
         String historyPw = request.getParameter("historyPw");
         PasswordChangeService.authPassword(userId, password, updateUser,authEnableFlag,historyPw,oldPassword,oldPassword2);
		
		 return "";
	 }
}