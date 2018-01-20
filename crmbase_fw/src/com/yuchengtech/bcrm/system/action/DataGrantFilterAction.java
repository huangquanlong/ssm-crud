package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.service.DataGrantFilterService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.dataauth.model.AuthSysFilter;

/**
 * 数据权限过滤去FILTER模块 增删改
 * 
 * @author wz
 * @since 2012-11-16
 */
@SuppressWarnings("serial")
@Action("/dataGrantFilterAction")
public class DataGrantFilterAction extends CommonAction {

	@Autowired
	private DataGrantFilterService dataGrantFilterService;

	/**
	 * 初始化model
	 * @author wz
	 * */
	@Autowired
	public void init() {// 初始化module
		model = new AuthSysFilter();
		setCommonService(dataGrantFilterService);
		// 新增修改删除记录是否记录日志,默认为false，不记录日志
		needLog = false;
	}
	
	/**
	 * 新增MAP表数据用
	 * */
    public DefaultHttpHeaders create() {
    	dataGrantFilterService.save(model);
    	return new DefaultHttpHeaders("success");
        
    }
	/**
	 * 删除操作
	 * @author wz
	 * @return 操作结果标志
	 * */
	public String delFun() {
		try{
    	   	ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("delId");
			String jql="delete from AuthSysFilter c where c.id in ("+idStr+")";
			Map<String,Object> values=new HashMap<String,Object>();
			dataGrantFilterService.batchUpdateByName(jql, values);
			addActionMessage("batch removed successfully");
	        return "success";
    	}catch(Exception e){
    		e.printStackTrace();
    		return "failure";
    	}
	}
}
