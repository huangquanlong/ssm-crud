package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthAccountRole;
import com.yuchengtech.bcrm.system.service.RoleAccountGrantService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.exception.BizException;

/**
 * 角色授权
 * @author songxs
 * @since 2012-11-23
 */
@SuppressWarnings("serial")
@Action("/roleAccountGrant-action")
public class RoleAccountGrantAction extends CommonAction {
	
	//角色授权操作service
	@Autowired
	private  RoleAccountGrantService  roleAccountGrantMenuService;
	
	@Autowired
	public void init() {
		model = new AdminAuthAccountRole();
		setCommonService(roleAccountGrantMenuService);
	}
	
	/**
	 * 取消授权（批量删除方法）
	 * @return 返回成功标志
	 */
	public String batchDestroy(){
		try{
			ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("idStr");
			String jql="delete from AdminAuthAccountRole c where c.id in ("+idStr+")";
			Map<String,Object> values=new HashMap<String,Object>();
			roleAccountGrantMenuService.batchUpdateByName(jql, values);
			//roleAccountGrantMenuService.deleteMenu(idStr);
			addActionMessage("batch removed successfully");
			}catch(Exception e){
				e.printStackTrace();
				throw new BizException(1,2,"1002",e.getMessage());
			}
			return "success";
	}
	
	/**
	 * 角色授权新增 
	 */
	 public DefaultHttpHeaders create() {
			ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String roleId = request.getParameter("roleId"); 
		    String accountIds = request.getParameter("accountIds"); 
		    roleAccountGrantMenuService.save(roleId,accountIds);
	    	return new DefaultHttpHeaders("success");
		 
		 }
	 }
