package com.yuchengtech.bcrm.workplat.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bcrm.workplat.service.WorkingplatformAnnexeService;
import com.yuchengtech.bob.vo.WorkingplatformAnnexeVo;

/**
 * @describe 附件服务导航
 * @author WillJoe
 *
 */
@SuppressWarnings("serial")
@Action("workplatannexe")
@Results({
    @Result(name="success", type="redirectAction", params = {"actionName" , "workplatannexe"})
})
public class WorkingplatformAnnexeAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{

	
	private WorkingplatformAnnexeVo wa = new WorkingplatformAnnexeVo();
	private Collection<WorkingplatformAnnexeVo> wac;
	private Long id;
	 private HttpServletRequest request;
	
    @Autowired
    private WorkingplatformAnnexeService was;
	
    /**
     * 单条数据展示.
     * HTTP:GET方法
     * URL:/actionName/$id;
     */
    public HttpHeaders show() {
    	/**
    	 * setUserId,this needs session;
    	 */
    	
        return new DefaultHttpHeaders("show");
    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。
     *  HTTP:GET方法 
     *  URL:/actionName；
     */
    public HttpHeaders index() {
        if(wa.getRelationInfo()!=null&&!"".equals(wa.getRelationInfo())){
            String relaInfo = this.wa.getRelationInfo();
            wac = was.findByRe(relaInfo);
        }else wac = was.findAll();
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
    	was.remove(wa.getAnnexeId());
        return "success";
    }

    /**
     * 数据新增提交
     * HTTP:POST方法
     * URL:/actionName
     */
    public HttpHeaders create() {
    	was.save(wa);
        return new DefaultHttpHeaders("success")
            .setLocationId(wa.getAnnexeId());
    }

    /**
     * 数据修改提交
     * HTTP:PUT方法
     * URL:/WorkPlatNotice/$id
     */
    public String update() {
       //	was.save(wa);
        return "success";
    }
    
    /**
     * 扩展方法，通过关联模块记录ID查询附件列表
     * HTTP:POST方法
     * URL:/workplatannexe/findAna
     * @return
     */
    public HttpHeaders findAna(){
        String relaInfo = this.wa.getRelationInfo();
        if(null!=relaInfo&&!"".equals(relaInfo)){
            wac = was.findByRe(relaInfo);
        }
        return new DefaultHttpHeaders("index").disableCaching();
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
            //this.wa = was.find(id);
        }
        this.id = id;
    }
    
    /**
     * 
     */
    public Object getModel() {
        return (wac != null ? wac : wa);
    }
}
