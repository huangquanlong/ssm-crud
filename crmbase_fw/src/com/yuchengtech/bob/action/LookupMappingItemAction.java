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
import com.yuchengtech.bob.core.LookupManager;
import com.yuchengtech.bob.model.LookupMappingItem;
import com.yuchengtech.bob.service.LookupMappingItemService;

@Action("/lookupMapping-item")
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "lookupMapping-item" }) })
public class LookupMappingItemAction extends ValidationAwareSupport implements
		ModelDriven<Object>, Validateable {

	private static final long serialVersionUID = 8550661616118279376L;
	private LookupMappingItem model = new LookupMappingItem();
	private HttpServletRequest request;
	private Long ID;
	private Collection<LookupMappingItem> list;
	@Autowired
	private LookupMappingItemService lookupMappingItemService;

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}

	public HttpHeaders index() {
		list = lookupMappingItemService.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public String edit() {
		return "edit";
	}

	public String editNew() {
		model = new LookupMappingItem();
		return "editNew";
	}

	public String deleteConfirm() {
		return "deleteConfirm";
	}

	public String destroy() {
		lookupMappingItemService.remove(ID);
		addActionMessage("lookupMappingItem removed successfully");
		return "success";
	}

	public DefaultHttpHeaders create() {
		lookupMappingItemService.save(model);
		//重新加载数据字典  add by zj 2012-06-12
		LookupManager.getInstance().reloadOracle(model.getLookup());
		addActionMessage("New lookupMappingItem created successfully");
		return new DefaultHttpHeaders("success").setLocationId(model.getId());

	}

	public String update() {
		lookupMappingItemService.save(model);
		LookupManager.getInstance().reloadOracle(model.getLookup());
		addActionMessage("lookupMappingItem updated successfully");
		return "success";
	}
	
	public String batchDestroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			LookupMappingItem lookupMappingItem = lookupMappingItemService.find(id);
			if (lookupMappingItem != null) {
				lookupMappingItemService.remove(id);
				LookupManager.getInstance().reloadOracle(lookupMappingItem.getLookup());
			}
		}
		
		//lookupMappingItemService.batchRemove(idStr);
		
		addActionMessage(" lookupMappingItem removed successfully");
		return "success";
	}

	public void validate() {

	}

	public void setId(Long ID) {
		if (ID != null) {
			this.model = lookupMappingItemService.find(ID);
		}
		this.ID = ID;
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

}