package com.yuchengtech.crm.dataauth.action;

import java.text.ParseException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.crm.dataauth.model.UserViewRelation;
import com.yuchengtech.crm.dataauth.service.UserRelateViewService;
@ParentPackage("json-default")
@Action(value="/userviewrelation", results={
    @Result(name="success", type="json")
})
public class UserViewRelationAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{
	private static final long serialVersionUID = -2010621122837504304L;
	private UserViewRelation model = new UserViewRelation();
    private Collection<UserViewRelation> list;
    private Long id;
    @Autowired
    private UserRelateViewService UserRelateViewService ;
	private HttpServletRequest request;

    // GET /orders
    public HttpHeaders index() {
    	//list=customerRelateCustomerBaseService.query(0, 10);
        return new DefaultHttpHeaders("index")
            .disableCaching();
    }
	
    // GET /orders/1
    public HttpHeaders show() {
        return new DefaultHttpHeaders("show");
    }


    
    // GET /orders/1/edit
    public String edit() {
        return "edit";
    }

    // GET /orders/new
    public String editNew() {
   
        return "editNew";
    }

    // GET /orders/1/deleteConfirm
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    // DELETE /orders/1
    public String destroy() {
        return "success";
    }

    // POST /orders
    public HttpHeaders create() throws ParseException {
    	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        
	    	String[] menuAddCodeStr = null;
			String[] menuDelCodeStr = null;
		    String addStr=  request.getParameter("addStr");
		    String delStr=  request.getParameter("delStr");
		    
			if(!addStr.equals("")){
				menuAddCodeStr = addStr.split(",");
			}
			if(!delStr.equals("")){
				menuDelCodeStr = delStr.split(",");
			}
		    String userId =request.getParameter("user_id");
		    UserRelateViewService.batchSave(menuAddCodeStr,menuDelCodeStr,userId);
        return new DefaultHttpHeaders("success")
            .setLocationId(model.getId());
    }

    // PUT /orders/1
    public String update() {
        return "success";
    }
    //校验方法
    public void validate() {

    }
    public Long getId() 
    { 
        return this.id; 
    } 

    public void setId(Long id) {
 
        this.id = id;
    }
    
    public Object getModel() 
    { 
        return (list != null ? list : model); 
    } 

    
}
