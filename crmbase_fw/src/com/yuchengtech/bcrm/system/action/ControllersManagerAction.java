package com.yuchengtech.bcrm.system.action;

import java.text.ParseException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.AuthResController;
import com.yuchengtech.bcrm.system.service.ControllersManagerService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 功能点按钮控制管理
 * @author GUOCHI
 * @since 2012-10-15
 */
@SuppressWarnings("serial")
@Action("/Controllers-action")
public class ControllersManagerAction extends CommonAction{
	private AuthResController wi = new AuthResController();
	private Collection<AuthResController> wic;
	@SuppressWarnings("unused")
    private Long id;
	
    @Autowired
    private ControllersManagerService wis;
    @Autowired
	public void init() {//初始化module
		model = new AuthResController();
		setCommonService(wis);
		// 新增修改删除记录是否记录日志,默认为false，不记录日志
		needLog = false;;
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
        ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        String idStr = request.getParameter("idStr"); //获取需要删除的控制id
        wis.remove(idStr);
        return "success";
    }

    /**
     * 数据新增提交
     * HTTP:POST方法
     * URL:/actionName
     */
    public DefaultHttpHeaders create() {
    	try {
            wis.save(wi);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success")
            .setLocationId(wi.getId());
    }

    /**
     * 数据修改提交
     * HTTP:PUT方法
     * URL:/WorkPlatNotice/$id
     */
    public String update() {
       	try {
            wis.save(wi);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * ID参数获取方法
     * @param id
     */
    public void setId(Long id) {
        if (id != null) {
            this.wi = (AuthResController) wis.find(id);
        }
        this.id = id;
    }
    
    public Object getModel() {
        return (wic != null ? wic : wi);
    }
}
