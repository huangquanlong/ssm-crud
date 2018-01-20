package com.yuchengtech.sm.action;

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
import com.yuchengtech.sm.model.SmTypeAttrRel;
import com.yuchengtech.sm.service.SmTypeAttrGrantService;

/**
 * 私募类型添加或者删除属性
 * @since 2017-2-23
 */
@SuppressWarnings("serial")
@Action("/SmTypeAtrrGrant-action")
public class SmTypeAtrrGrantAction extends CommonAction {
	
	//角色授权操作service
	@Autowired
	private  SmTypeAttrGrantService smTypeAttrGrantService;
	
	@Autowired
	public void init() {
		model = new SmTypeAttrRel();
		setCommonService(smTypeAttrGrantService);
	}
	
	/**
	 * 取消属性（批量删除方法）
	 * @return 返回成功标志
	 */
	public String batchDestroy(){
		try{
			ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("idStr");
			smTypeAttrGrantService.deleteMenu(idStr);
			addActionMessage("batch removed successfully");
			}catch(Exception e){
				e.printStackTrace();
				throw new BizException(1,2,"1002",e.getMessage());
			}
			return "success";
	}
	
	/**
	 * 私募类型新增属性 
	 */
	 public DefaultHttpHeaders create() {
			ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String typeId = request.getParameter("typeId"); 
		    String attrId = request.getParameter("attrId"); 
		    smTypeAttrGrantService.save(attrId,typeId);
	    	return new DefaultHttpHeaders("success");
		 
		 }
	 }
