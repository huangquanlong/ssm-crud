package com.yuchengtech.bcrm.system.action;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AdminAuthAccountRole;
import com.yuchengtech.bcrm.system.service.UserManagerForAuthService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.exception.BizException;

/*
 * 用户管理Action,维护用户授权机构信息
 * @author wangwan
 * @since 2012-10-08
 */

@SuppressWarnings("serial")
@Action("/userManagerForAuth")
public class UserManagerForAuthAction extends CommonAction {
	
	@Autowired
	private UserManagerForAuthService service;//定义UserManagerForAuthService属性
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	@Autowired
	public void init() {
		model = new AdminAuthAccountRole();
		setCommonService(service);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	/*
	 * 
	 * 用户授权角色信息维护，包括新增和修改
	 * @return 成功标示
	 */
	  public DefaultHttpHeaders create(){
	 
	  try{
	    ActionContext ctx = ActionContext.getContext();
	    this.request = ((HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest"));
	    
	    String s3 = this.request.getParameter("addArray");//需要新增的授权角色信息集合
	    String s4 = this.request.getParameter("deleteArray");//需要删除的授权角色信息集合
	    if(!(s3.equals("[]")))
	    	{
	    	JSONArray jarray = JSONArray.fromObject(s3);
	    	 this.service.save(jarray);
	 
	    } 
	   	 if(!(s4.equals("[]"))){
	  	    	JSONArray jarray2 = JSONArray.fromObject(s4);
	  	    	   this.service.remove(jarray2);
	  	    }
	  }catch(Exception e){
  		e.printStackTrace();
		throw new BizException(1,2,"1002",e.getMessage());
	}
	   return new DefaultHttpHeaders("success");
	   
	  }
	 /**
	  * （自定义）批量删除，根据前台传递的idStr删除相应用户信息
	  * @return 成功标识
	  */
    public String batchDestroy(){
	   	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr");
		String jql="DELETE FROM AdminAuthAccount C WHERE C.id IN ("+idStr+")";
		Map<String,Object> values=new HashMap<String,Object>();
		service.batchUpdateByName(jql, values);
		addActionMessage("batch removed successfully");
		
        return "success";
    }
}


