package com.yuchengtech.bcrm.system.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bcrm.system.model.AdminLogInfo;
import com.yuchengtech.bcrm.system.service.AdminLogManagerService;

/**
 * 日志增删改
 * @author weijl
 * @since 2012-09-24
 */
@SuppressWarnings("serial")
@Action("/AdminLogManagerAction")
@Results({ @Result(name = "success", type = "redirectAction", params = {
        "actionName", "AdminLogManagerAction" }) })
public class AdminLogManagerAction extends ValidationAwareSupport
        implements ModelDriven<Object>, Validateable {

    private AdminLogInfo ali = new AdminLogInfo();//创建日志model实例
    private Collection<AdminLogInfo> alic;//声明日志model集合
    private HttpServletRequest request;

    @Autowired
    private AdminLogManagerService alis;//声明日志增删改service实例

    /**
     * 日志删除方法
     */
    public String destroy() {
    	
        ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        String idStr = request.getParameter("idStr"); //获取需要删除的日志id
        alis.remove(idStr);
        return "success";
    }

    /**
     * 数据验证方法
     */
    public void validate() {}

    /**
     * 获取日志model方法
     */
    public Object getModel() {
        return (alic != null ? alic : ali);
    }
}


