package com.yuchengtech.bcrm.workplat.action;

import java.util.ArrayList;
import java.util.List;

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
import com.yuchengtech.bcrm.workplat.service.NoticeService;
import com.yuchengtech.bob.common.BatObjectValidater;
import com.yuchengtech.bob.vo.WorkingplatformNoticeVo;

/**
 * 公告查看Aciton
 * @author WillJoe
 *
 */
@SuppressWarnings("serial")
@Action("workplatnotice")
@Results({
    @Result(name="success",type="redirectAction", params = {"actionName" , "workplatnotice", "success", "false"})
})
public class WorkPlatNoticeAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{
 	    
	private WorkingplatformNoticeVo wn = new WorkingplatformNoticeVo();
	private List<WorkingplatformNoticeVo> wnc;
	private Long id;
	
    @Autowired
    private NoticeService ns;
    private HttpServletRequest request;
	
    /**
     * 单条数据展示.
     * HTTP:GET方法
     * URL:/actionName/$id;
     */
    public HttpHeaders show() {
    	wn = ns.findById(id);
    	return new DefaultHttpHeaders("show");
    }

    /**
     * 数据列表查询包括全部数据，或者待条件查询。
     *  HTTP:GET方法 
     *  URL:/actionName；
     */
    public HttpHeaders index() {
        if(wn.getNoticeTitle()==null){
    	    wnc = ns.findAll(1,10);
        }else 
            wnc = ns.findAll(1, 10, wn);
        
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
    	//ns.remove(id);
        return "deleteConfirm";
    }

    /**
     * 数据删除提交
     * HTTP:DELETE方法
     * URL:/actionName/$id
     */
    public String destroy() {
        
        ns.remove(this.id);
        return "success";
    }

    /**
     * 数据新增提交
     * HTTP:POST方法
     * URL:/actionName
     */
    public HttpHeaders create() {
    	  ActionContext ctx = ActionContext.getContext();
  		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
       String methodNs = wn.getMethodNs();
       if(null!=wnc&&wnc.size()>0){
            for(WorkingplatformNoticeVo wnv:wnc){
                if(methodNs.toLowerCase().equals("delete")){
                    ns.remove(wnv.getNoticeId());
                }else if(methodNs.toLowerCase().equals("publish")){
                    ns.save(wnv,"publish");
                }else ns.save(wnv);
            }
        }else if(null!=wn){
        	if(methodNs!=null){
        		if(methodNs.toLowerCase().equals("delete")){
        			long noticeId = Long.parseLong((request.getParameter("noticeId")));
          		  ns.remove(noticeId);
        		}else if(methodNs.toLowerCase().equals("publish")){
        			   ns.save(wn,"publish");
        		}
        	}else {
        		  ns.save(wn);
        	}
        }else{
        	ns.save(wn);
        } 
        return new DefaultHttpHeaders("success").setLocationId(wn.getNoticeId());
    }

    /**
     * 数据修改提交
     * HTTP:PUT方法
     * URL:/WorkPlatNotice/$id
     */
    public String update() {
       	ns.save(wn);
        return "success";
    }

    /**
     * 数据验证方法
     */
    public void validate() {
    	/**
    	 * TODO validate bissness logic.
    	 */ 
    	 if(wn.getIsBat()){
    	     this.wnc = new ArrayList<WorkingplatformNoticeVo>();
    	     BatObjectValidater bov = new BatObjectValidater(this.wn);
    	     List<Object> listO = bov.Validate();
    	     for(Object o: listO){
    	         wnc.add((WorkingplatformNoticeVo)o);
    	     }
    	 }
    }

    /**
     * ID参数获取方法
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 
     */
    public Object getModel() {
        return (wnc != null ? wnc : wn);
    }
    

}
