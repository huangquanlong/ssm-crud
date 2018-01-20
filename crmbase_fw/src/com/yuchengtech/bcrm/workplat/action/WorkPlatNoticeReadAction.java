package com.yuchengtech.bcrm.workplat.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bcrm.workplat.service.NoticeReadLoadService;
import com.yuchengtech.bob.common.BatObjectValidater;
import com.yuchengtech.bob.vo.WorkingplatformNoticeReadVo;


/**
 * 涉及到公告已阅和下载记录的action
 */
@SuppressWarnings("serial")
@Action("/workplatnoticeread")
@Results({
    @Result(name="success", type="redirectAction", params = {"actionName" , "workplatnoticeread"})
})
public class WorkPlatNoticeReadAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{
	
	private WorkingplatformNoticeReadVo wnr = new WorkingplatformNoticeReadVo();
	private Collection<WorkingplatformNoticeReadVo> wnrc;
	private Long id;
	
    @Autowired
    private NoticeReadLoadService nrs;
	
    /**
     * 单条数据展示.
     * HTTP:GET方法
     * URL:/actionName/$id;
     */
    public HttpHeaders show() {
    	//wnr = nrs.find(id);    	
    	/**
    	 * setUserId,this needs session;
    	 */
    	@SuppressWarnings("unused")
		String userId = "";
    	/**
    	 * need Service overload mothed:find.
    	 */
        return new DefaultHttpHeaders("show");
    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。
     *  HTTP:GET方法 
     *  URL:/actionName.
     */
    public HttpHeaders index() {
    	wnrc = nrs.findAll();
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
    	nrs.remove(id);
        return "success";
    }

    /**
     * 数据新增提交
     * HTTP:POST方法
     * URL:/actionName
     */
    public HttpHeaders create() {
        if(null!=wnrc&&wnrc.size()>0){
            for(WorkingplatformNoticeReadVo wnrv:wnrc){
                nrs.save(wnrv);
            }
        }
        else nrs.save(wnr);
        return new DefaultHttpHeaders("success")
            .setLocationId(wnr.getNoticeId());
    }

    /**
     * 数据修改提交
     * HTTP:PUT方法
     * URL:/WorkPlatNotice/$id
     */
    public String update() {
       	nrs.save(wnr);
        return "success";
    }

    /**
     * 数据验证方法
     */
    public void validate() {
    	/**
    	 * TODO validate bussness logic.
    	 */
        if(wnr.getIsBat()){
            this.wnrc = new ArrayList<WorkingplatformNoticeReadVo>();
            BatObjectValidater bov = new BatObjectValidater(this.wnr);
            List<Object> listO = bov.Validate();
            for(Object o: listO){
                wnrc.add((WorkingplatformNoticeReadVo)o);
            }
        }
    }

    /**
     * ID参数获取方法
     * @param id
     */
    public void setId(Long id) {
        if (id != null) {
            this.wnr = nrs.find(id);
        }
        this.id = id;
    }
    
    /**
     * 
     */
    public Object getModel() {
        return (wnrc != null ? wnrc : wnr);
    }
    
}
