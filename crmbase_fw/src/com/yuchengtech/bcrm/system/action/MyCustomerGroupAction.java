package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.OcrmFCmAccordionGroup;
import com.yuchengtech.bcrm.system.service.MyCustomerGroupService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 客户经理快捷工作台 action
 * @author wz
 * @since 2012-11.7
 */
@SuppressWarnings("serial")
@Action("/myCustomerGroupAction")
public class MyCustomerGroupAction extends CommonAction {
	
	@Autowired
	private MyCustomerGroupService myCustomerGroupService;//保存群组名称service
	
	private HttpServletRequest request;
	
	@Autowired
	public void init(){
		model = new OcrmFCmAccordionGroup();
		setCommonService(myCustomerGroupService);
	}

	/**
	 * 修改选中群组名称
	 */
	public void modNode() {
		
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		Map<String,Object> values=new HashMap<String,Object>();
		String nodeId = request.getParameter("nodeId");
		String nodeName = request.getParameter("nodeName");
		String modJQL = "update OcrmFCmAccordionGroup g set g.groupName = '"+nodeName+"' where g.id = '"+nodeId+"'";
		myCustomerGroupService.batchUpdateByName(modJQL, values);
	}
	
	/**
	 * 删除选中群组名称
	 */
	public void delNode() {
		
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String nodeId = request.getParameter("nodeId");
		myCustomerGroupService.remove(nodeId);
	}
	
	/**
	 * 保存的移动节点
	 * */
	public void moveNodeSave() {
		
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String nodeId = request.getParameter("nodeId");//移动的节点
		String oldParentId = request.getParameter("oldParent");//移动节点的 旧父节点
		String newParentId = request.getParameter("newParent");//移动节点的 新父节点
		myCustomerGroupService.move(nodeId, oldParentId, newParentId);
	}
	
	/**
	 * 保存的加入其他群组的节点
	 * */
	public void addNodeSave() {
		
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String nodeId = request.getParameter("nodeId");//选择的节点
		String newParentId = request.getParameter("newParent");//选择节点的 新父节点
		myCustomerGroupService.addNewGroup(nodeId, newParentId);
	}
}