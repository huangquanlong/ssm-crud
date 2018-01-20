package com.yuchengtech.bcrm.workplat.action;

import java.util.Collection;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bcrm.workplat.model.WorkingplatformRemindInfo;
import com.yuchengtech.bcrm.workplat.service.WorkingplatformRemindInfoService;

/**
 * @describe 信息提醒服务导航
 * @author WillJoe
 *
 */
@SuppressWarnings("serial")
@Action("/workplatremind")
@Results({
    @Result(name="success", type="redirectAction", params = {"actionName" , "workplatremind"})
})
public class WorkingplatformRemindInfoAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{

	
	private WorkingplatformRemindInfo wri = new WorkingplatformRemindInfo();
	private Collection<WorkingplatformRemindInfo> wric;
	private Long id;
	
    @Autowired
    private WorkingplatformRemindInfoService wris;
	
    /**
     * 单条数据展示.
     * HTTP:GET方法
     * URL:/actionName/$id;
     */
    public HttpHeaders show() {
    	wri = wris.find(id);    	
        return new DefaultHttpHeaders("show");
    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。
     *  HTTP:GET方法 
     *  URL:/actionName；
     */
    public HttpHeaders index() {
    	wric = wris.findAll();
        return new DefaultHttpHeaders("index")
            .disableCaching();
    }
    
    /**
     * 请求数据编辑页面跳转。
     * HTTP:GET方法
     * URL:/actionName/$id/edit;
     */
    public String edit() {
        return "edit";
    }

    /**
     * 新增页面请求
     * HTTP:GET方法
     * URL:/actionName/new
     */
    public String editNew() {
        return "editNew";
    }

    /**
     * 请求删除页面
     * HTTP:GET方法
     * URL:/actionName/$id/deleteContirm
     */
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    /**
     * 数据删除提交
     * HTTP:DELETE方法
     * URL:/actionName/$id
     */
    public String destroy() {
    	wris.remove(id);
        return "success";
    }

    /**
     * 数据新增提交
     * HTTP:POST方法
     * URL:/actionName
     */
    public HttpHeaders create() {
    	wris.save(wri);
        return new DefaultHttpHeaders("success")
            .setLocationId(wri.getRemindId());
    }

    /**
     * 数据修改提交
     * HTTP:PUT方法
     * URL:/WorkPlatNotice/$id
     */
    public String update() {
       	wris.save(wri);
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
     * @param id
     */
    public void setId(Long id) {
        if (id != null) {
            this.wri = wris.find(id);
        }
        this.id = id;
    }
    
    /**
     * 
     */
    public Object getModel() {
        return (wric != null ? wric : wri);
    }

}
