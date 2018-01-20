package com.yuchengtech.bob.action;

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
import com.yuchengtech.bob.model.LookupMapping;
import com.yuchengtech.bob.service.LookupMappingService;

@Action("/lookup-mapping")
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "lookup-mapping" }) })
public class LookupMappingAction extends ValidationAwareSupport implements
		ModelDriven<Object>, Validateable {

	private static final long serialVersionUID = 8550661616118279765L;
	private LookupMapping model = new LookupMapping();
	private HttpServletRequest request;
	private Long ID;
	private Collection<LookupMapping> list;
	@Autowired
	private LookupMappingService lookupMappingService;

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}

	public HttpHeaders index() {
		list = lookupMappingService.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public String edit() {
		return "edit";
	}

	public String editNew() {
		model = new LookupMapping();
		return "editNew";
	}

	public String deleteConfirm() {
		return "deleteConfirm";
	}

	public String destroy() {
		lookupMappingService.remove(ID);
		addActionMessage("lookupMapping removed successfully");
		return "success";
	}

	public DefaultHttpHeaders create() {
		boolean tempS = lookupMappingService.save(model);
		if(tempS==true){
		addActionMessage("New lookupMapping created successfully");
		return new DefaultHttpHeaders("success").setLocationId(model.getId());
		}else{
			addActionMessage("New lookupMapping created failure");
			return new DefaultHttpHeaders("failure").setLocationId(model.getId());
		}
	}

	public String update() {
		lookupMappingService.save(model);
		addActionMessage("lookupMapping updated successfully");
		return "success";
	}

	public String batchDestroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		lookupMappingService.batchRemove(idStr);
		addActionMessage(" lookupMapping removed successfully");
		return "success";
	}

	public void validate() {

	}

	public void setId(Long ID) {
		if (ID != null) {
			this.model = lookupMappingService.find(ID);
		}
		this.ID = ID;
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

}