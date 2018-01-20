package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.OcrmFASsRelation;
import com.yuchengtech.bcrm.system.service.DataSetRelationService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 数据集关联关系管理（新增，修改，删除）
 * @author songxs
 * @since 2012-12-7
 *
 */
@SuppressWarnings("serial")
@Action("/dataSetRelation-action")
public class DataSetRelationAction  extends CommonAction{
    @Autowired
    private DataSetRelationService dataSetRelationService ;
    @Autowired
	public void init(){
	  	model = new OcrmFASsRelation(); 
		setCommonService(dataSetRelationService);
		//新增修改删除记录是否记录日志,默认为false，不记录日志
		needLog=true;
	}
  //（自定义）批量删除
    public String batchDestroy(){
    	   	ActionContext ctx = ActionContext.getContext();
	        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String idStr = request.getParameter("idStr");
			String jql="delete from OcrmFASsRelation c  where c.id in ("+idStr+")";
			Map<String,Object> values=new HashMap<String,Object>();
			dataSetRelationService.batchUpdateByName(jql, values);
			addActionMessage("batch removed successfully");
	        return "success";
    }

    
}