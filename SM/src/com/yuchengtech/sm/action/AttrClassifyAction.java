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
import com.yuchengtech.sm.model.SmAttrClassify;
import com.yuchengtech.sm.service.AttrClassifyService;

@Action("/attrClassify")
@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "attrClassify" }) })
public class AttrClassifyAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable {

	private static final long serialVersionUID = -1674300042355548401L;
	private SmAttrClassify model = new SmAttrClassify();
	private HttpServletRequest request;
	private Long ID;
	private Collection<SmAttrClassify> list;
	@Autowired
	private AttrClassifyService attrClassifyService;

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}

	public HttpHeaders index() {
		list = attrClassifyService.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public String edit() {
		return "edit";
	}

	public String editNew() {
		model = new SmAttrClassify();
		return "editNew";
	}

	public String deleteConfirm() {
		return "deleteConfirm";
	}

	public String destroy() {
		attrClassifyService.remove(model);
		addActionMessage("attrClassify removed successfully");
		return "success";
	}

	public DefaultHttpHeaders create() {
		boolean tempS = attrClassifyService.save(model);
		if (tempS == true) {
			addActionMessage("New attrClassify created successfully");
			return new DefaultHttpHeaders("success").setLocationId(model.getId());
		} else {
			addActionMessage("New attrClassify created failure");
			return new DefaultHttpHeaders("failure").setLocationId(model.getId());
		}
	}

	public String update() {
		attrClassifyService.save(model);
		addActionMessage("attrClassify updated successfully");
		return "success";
	}

	public String batchDestroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		attrClassifyService.batchRemove(idStr);
		addActionMessage(" attrClassify removed successfully");
		return "success";
	}

	public void validate() {

	}

	public void setId(Long ID) {
		if (ID != null) {
			this.model = attrClassifyService.find(ID);
		}
		this.ID = ID;
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

}