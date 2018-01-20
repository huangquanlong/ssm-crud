package com.yuchengtech.sm.action;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bcrm.common.DateUtils;
import com.yuchengtech.sm.model.SmAttrDefinition;
import com.yuchengtech.sm.service.AttrDefinitionService;

@SuppressWarnings("serial")
@Action("/attrDefiniton")
@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "attrDefiniton" }) })
public class AttrDefinitionAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable {

	private SmAttrDefinition model = new SmAttrDefinition();
	private HttpServletRequest request;
	private Long ID;
	private Collection<SmAttrDefinition> list;
	@Autowired
	private AttrDefinitionService attrDefinitionService;

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}

	public HttpHeaders index() {
		list = attrDefinitionService.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public String edit() {
		return "edit";
	}

	public String editNew() {
		model = new SmAttrDefinition();
		return "editNew";
	}

	public String deleteConfirm() {
		return "deleteConfirm";
	}

	public String destroy() {
		attrDefinitionService.remove(model);
		addActionMessage("attrDefiniton removed successfully");
		return "success";
	}

	public DefaultHttpHeaders create() {
		boolean tempS = attrDefinitionService.save(model);
		if (tempS == true) {
			addActionMessage("New attrDefiniton created successfully");
			return new DefaultHttpHeaders("success").setLocationId(model.getId());
		} else {
			addActionMessage("New attrDefiniton created failure");
			return new DefaultHttpHeaders("failure").setLocationId(model.getId());
		}
	}

	public String update() {
		model.setUpdTime(new Date());
		attrDefinitionService.save(model);
		addActionMessage("attrDefiniton updated successfully");
		return "success";
	}

	public String batchDestroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		attrDefinitionService.batchRemove(idStr);
		addActionMessage(" attrDefiniton removed successfully");
		return "success";
	}

	public void validate() {

	}

	public void setId(Long ID) {
		if (ID != null) {
			this.model = attrDefinitionService.find(ID);
		}
		this.ID = ID;
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

}