package com.yuchengtech.bcrm.workplat.action;

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
import com.yuchengtech.bcrm.workplat.model.WorkingplatformSchedule;
import com.yuchengtech.bcrm.workplat.service.WorkingplatformScheduleService;

/**
 * @describe 日程安排服务导航
 * @author WillJoe
 * 
 */
@SuppressWarnings("serial")
@Action("workformschedule")
@Results({ @Result(name = "success", type = "redirectAction", params = {
        "actionName", "workformschedule"}) })
public class WorkingplatformScheduleAction extends ValidationAwareSupport
        implements ModelDriven<Object>, Validateable {

    private WorkingplatformSchedule ws = new WorkingplatformSchedule();
    private Collection<WorkingplatformSchedule> wsc;
    private Long id;

    @Autowired
    private WorkingplatformScheduleService wss;

    public void excute(){
    	System.out.println("what a terrible thing!");
    }
    
    /**
     * 单条数据展示. HTTP:GET方法 URL:/actionName/$id;
     */
    public HttpHeaders show() {
        ws = wss.find(id);
        return new DefaultHttpHeaders("show");
    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。 HTTP:GET方法 URL:/actionName；
     */
    public HttpHeaders index() {
        wsc = wss.findAll();
        return new DefaultHttpHeaders("index").disableCaching();
    }

    /**
     * 请求数据编辑页面跳转。 HTTP:GET方法 URL:/actionName/$id/edit;
     */
    public String edit() {
        return "edit";
    }
    /**
     * 新增页面请求 HTTP:GET方法 URL:/actionName/new
     */
    public String editNew() {
        return "editNew";
    }

    /**
     * 请求删除页面 HTTP:GET方法 URL:/actionName/$id/deleteContirm
     */
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    /**
     * 数据删除提交 HTTP:DELETE方法 URL:/actionName/$id
     */
    public String destroy() {
        wss.remove(id+"");
        return "success";
    }

    /**
     * 数据新增提交 HTTP:POST方法 URL:/actionName
     */
    public HttpHeaders create() {
    	ActionContext ctx = ActionContext.getContext();
    	HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
    	//request.getParameter("json.data");
    	String eventJson = (String)request.getParameter("eventJson");
    	wss.scheduleOptions(eventJson);
        //wss.save(ws);
        return new DefaultHttpHeaders("success").setLocationId(ws
                .getScheduleId());
    }
    
    /**
     * 数据修改提交 HTTP:PUT方法 URL:/WorkPlatNotice/$id
     */
    public String update() {
        wss.save(ws);
        return "success";
    }

    /**
     * 数据验证方法
     */
    public void validate() {
        /**
         * TODO validate bussness logic.
         */
    }

    /**
     * ID参数获取方法
     * 
     * @param id
     */
    public void setId(Long id) {
        if (id != null) {
            this.ws = wss.find(id);
        }
        this.id = id;
    }

    public void setJsonData(String jsonData){
    	System.out.println(jsonData);
    }
    
    public void setJson(String json){
    	System.out.println(json);
    }
    
    /**
     * 
     */
    public Object getModel() {
        return (wsc != null ? wsc : ws);
    }
}
