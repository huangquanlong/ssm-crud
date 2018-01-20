package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.CntMenu;
import com.yuchengtech.bcrm.system.service.CntMenuService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.exception.BizException;

/**
 * 菜单项管理
 * @author songxs
 * @since 2012-9-23
 */
@SuppressWarnings("serial")
@Action("/CntMenu-action")
public class CntMenuAction extends CommonAction {
	
	//菜单业务操作service
	@Autowired
	private CntMenuService cntMenuService;
	
	@Autowired
	public void init() {
		model = new CntMenu();
		setCommonService(cntMenuService);
	}
	
	/**
	 * 批量删除方法
	 * @return 返回成功标志
	 */
	public String batchDestroy() {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("idStr");
			cntMenuService.deleteMenu(idStr);
			addActionMessage("batch removed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(1, 2, "1002", e.getMessage());
		}
		return "success";
	}
	
	/**
	 * 菜单项更新方法
	 * @return
	 * @throws Exception
	 */
	public String updateNew() throws Exception{
		
		try{
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			long idStr = Long.parseLong((request.getParameter("id")));
			String name = request.getParameter("name");
			String parentId = request.getParameter("parentId");
			String icon = request.getParameter("icon");
			int order = Integer.parseInt(request.getParameter("order"));
			long modFuncId = Long.parseLong(request.getParameter("modFuncId"));
			int issamewin = Integer.parseInt((request.getParameter("issamewin")));
			String jql = "update  CntMenu c  set c.name ='"+name+"' ,c.parentId='"+parentId+"'," +
					" c.icon = '"+icon+"', c.order = '"+order+"',c.modFuncId = '" + modFuncId + 
					"',c.issamewin = '"+issamewin+"' where c.id in ("+idStr+")";
			Map<String,Object> values = new HashMap<String,Object>();
			super.executeUpdate(jql, values);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
}
