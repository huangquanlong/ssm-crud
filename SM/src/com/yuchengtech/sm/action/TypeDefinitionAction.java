package com.yuchengtech.sm.action;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bcrm.system.model.AdminAuthRole;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.sm.model.SmAttrClassify;
import com.yuchengtech.sm.model.SmTypeDefinition;
import com.yuchengtech.sm.service.TypeDefinitionService;

/**
 * @Title: TypeDefinitionAction 
* @Description: 类型定义service 
* @date 2017年2月20日 
 */
@SuppressWarnings("serial")
@Action("/typeDefinition")
@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "typeDefinition" }) })
public class TypeDefinitionAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable {

	private SmTypeDefinition model= new SmTypeDefinition();
	private HttpServletRequest request;
	private Long ID;
	private Collection<SmTypeDefinition> list;
	@Autowired
	private TypeDefinitionService typeDefinitionService;
		
	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}

	public HttpHeaders index() {
		list = typeDefinitionService.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public String edit() {
		return "edit";
	}

	public String editNew() {
		model = new SmTypeDefinition();
		return "editNew";
	}

	public String deleteConfirm() {
		return "deleteConfirm";
	}

	public DefaultHttpHeaders create() {
		boolean tempS = typeDefinitionService.save(model);
		if (tempS == true) {
			addActionMessage("New typeDefinition created successfully");
			return new DefaultHttpHeaders("success").setLocationId(model.getId());
		} else {
			addActionMessage("New typeDefinition created failure");
			return new DefaultHttpHeaders("failure").setLocationId(model.getId());
		}
	}

	public String update() {
		model.setUpdTime(new Date());
		typeDefinitionService.save(model);
		addActionMessage("typeDefinition updated successfully");
		return "success";
	}

	public String batchDestroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		typeDefinitionService.batchRemove(idStr);
		addActionMessage(" typeDefinition removed successfully");
		return "success";
	}

	public void setId(Long ID) {
		if (ID != null) {
			this.model = typeDefinitionService.find(ID);
		}
		this.ID = ID;
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

	@Override
	public void validate() {
		
	}


}