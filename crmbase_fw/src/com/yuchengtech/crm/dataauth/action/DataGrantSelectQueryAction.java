package com.yuchengtech.crm.dataauth.action;

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
import com.yuchengtech.crm.dataauth.model.AuthSysFilter;
import com.yuchengtech.crm.dataauth.service.DataGrantSelectQueryService;

@Action("/datagrantselectquery")
@Results({
    @Result(name="success", type="redirectAction", params = {"actionName" , "datagrantfilterquery1"})
})
public class DataGrantSelectQueryAction  extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{

	private static final long serialVersionUID = 8550661616118279889L;
	private AuthSysFilter model = new AuthSysFilter();
	private String mapId;
	private HttpServletRequest request;
	private Collection<AuthSysFilter> list;
	@Autowired
	private DataGrantSelectQueryService dgsqService;
	
    public HttpHeaders show() {
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders index() {
        list = dgsqService.findAll(mapId);
        return new DefaultHttpHeaders("index")
            .disableCaching();
    }
    
    public String edit() {
        return "edit";
    }

    public String editNew() {
    	model = new AuthSysFilter();
        return "editNew";
    }

    public String deleteConfirm() {
        return "deleteConfirm";
    }
    
	public void findFilter() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("tempId");
		mapId = idStr;
		index();
	}

    public void validate() {

    }

    public Object getModel() {
        return (list != null ? list : model);
    }

}