package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.service.RoleMenuOptionService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 资源权限配置 保存设置按钮 action
 * @author wz
 * @since 2012-10.17
 */
@SuppressWarnings("serial")
@Action("/roleMenuOptionAction")
public class RoleMenuOptionAction extends CommonAction {
	
	@Autowired
	private RoleMenuOptionService roleMenuOptionService;//资源权限设置service

	/**
	 * 保存资源权限设置
	 */
	public void saveOptionSet() {
		
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String[] menuAddCodeStr = null;
		String[] menuDelCodeStr = null;
		String[] optionCodeStr = null;
		String[] addOpCodeStr = null;
		String[] delOpCodeStr = null;
		
		String optionCode = request.getParameter("optionCode");//所选全部功能按钮编码
		String roleCodeGloble = request.getParameter("roleCodeGloble");//选择的角色编码
		String menuCodeGloble = request.getParameter("menuCodeGloble");//选择的菜单编码
		String addStr = request.getParameter("addStr");//需要新增的菜单编码
		String delStr = request.getParameter("delStr");//需要删除的菜单编码
		String addGrantsStr = request.getParameter("addGrantsStr");//需要新增的控制点信息
		String delGrantsStr = request.getParameter("delGrantsStr");//需要删除的控制点信息
		
		if(optionCode != null && !"".equals(optionCode)){
			optionCodeStr = optionCode.split(",");
		}
		
		if(addStr != null && !"".equals(addStr)){
			menuAddCodeStr = addStr.split(",");
		}
		if(delStr != null && !"".equals(delStr)){
			menuDelCodeStr = delStr.split(",");
		}
		if(addGrantsStr != null && !"".equals(addGrantsStr)){
			addOpCodeStr = addGrantsStr.split(",");
		}
		if(delGrantsStr != null && !"".equals(delGrantsStr)){
			delOpCodeStr = delGrantsStr.split(",");
		}
		
		roleMenuOptionService.optionMenuSave(menuAddCodeStr,menuDelCodeStr,optionCodeStr,roleCodeGloble,menuCodeGloble,addOpCodeStr,delOpCodeStr);
	}

}