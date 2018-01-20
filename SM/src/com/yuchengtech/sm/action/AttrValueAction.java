package com.yuchengtech.sm.action;

import java.util.Collection;

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
import com.yuchengtech.sm.model.SmAttrValue;
import com.yuchengtech.sm.service.AttrValueService;

@Action("/attrValue")
@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "attrValue" }) })
public class AttrValueAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable {

	private static final long serialVersionUID = -1674300042355548401L;
	private SmAttrValue model = new SmAttrValue();
	private HttpServletRequest request;
	private Long ID;
	private Collection<SmAttrValue> list;
	@Autowired
	private AttrValueService attrValueService;

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}

	public HttpHeaders index() {
		list = attrValueService.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public String edit() {
		return "edit";
	}

	public String editNew() {
		model = new SmAttrValue();
		return "editNew";
	}

	public String deleteConfirm() {
		return "deleteConfirm";
	}

/*	public String destroy() {
		attrValueService.remove(model);
		addActionMessage("attrClassify removed successfully");
		return "success";
	}*/

	public DefaultHttpHeaders create() {
		boolean tempS = attrValueService.save(model);
		if (tempS == true) {
			addActionMessage("New attrValue created successfully");
			return new DefaultHttpHeaders("success").setLocationId(model.getId());
		} else {
			addActionMessage("New attrValue created failure");
			return new DefaultHttpHeaders("failure").setLocationId(model.getId());
		}
	}

	public String update() {
		attrValueService.save(model);
		addActionMessage("attrValue updated successfully");
		return "success";
	}

	public String batchDestroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		attrValueService.batchRemove(idStr);
		addActionMessage(" attrValue removed successfully");
		return "success";
	}

	public void validate() {

	}

	public void setId(Long ID) {
		if (ID != null) {
			this.model = attrValueService.find(ID);
		}
		this.ID = ID;
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

}