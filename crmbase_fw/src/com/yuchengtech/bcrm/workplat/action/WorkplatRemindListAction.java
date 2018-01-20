package com.yuchengtech.bcrm.workplat.action;

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
import com.yuchengtech.bcrm.workplat.service.WorkplatRemindListService;

/**
 * @author km
 * 
 */
@SuppressWarnings("serial")
@Action("/workplatremindlist")
@Results({ @Result(name = "success", type = "redirectAction", params = {
        "actionName", "workplatremindlist" }) })
public class WorkplatRemindListAction extends ValidationAwareSupport implements
        ModelDriven<Object>, Validateable {

    private HttpServletRequest request;

    @Autowired
    private WorkplatRemindListService wrls;

    /**
     * 数据删除提交 HTTP:DELETE方法 URL:/actionName/$id
     */
    public String read() {
        /******************/
        ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        String idStr = request.getParameter("idStr");
        /******************/
        wrls.readed(idStr);
        return "success";
    }

    public void validate() {
        // TODO Auto-generated method stub

    }

    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

}
