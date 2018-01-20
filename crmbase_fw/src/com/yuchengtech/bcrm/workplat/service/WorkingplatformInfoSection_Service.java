package com.yuchengtech.bcrm.workplat.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.workplat.model.WorkingplatformInfoSection;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;

@Service
public class WorkingplatformInfoSection_Service extends CommonService {
	
   public WorkingplatformInfoSection_Service(){
		JPABaseDAO<WorkingplatformInfoSection, Long>  baseDAO=new JPABaseDAO<WorkingplatformInfoSection, Long>(WorkingplatformInfoSection.class);  
		   super.setBaseDAO(baseDAO);
	}
   /*得到当前的登录用户 ,和系统的登录时间**/
	public Object save(Object model){
		WorkingplatformInfoSection workingplatformInfoSection = (WorkingplatformInfoSection) model;
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String name = auth.getUsername();
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		//得到新建
		if(request.getParameter("sectionCategory").equals("全部目录")||request.getParameter("sectionCategory").length()==0){
			workingplatformInfoSection.setParentSection("root");
		}else{
			workingplatformInfoSection.setParentSection(request.getParameter("parentSection")); 	
		}
		workingplatformInfoSection.setSectionCategory(request.getParameter("sectionCategory"));
		workingplatformInfoSection.setSectionSummary(request.getParameter("sectionSummary"));
		workingplatformInfoSection.setSectionName(request.getParameter("sectionName"));
		if(workingplatformInfoSection.getSectionId()==null){//新增
			workingplatformInfoSection.setCreatorName(name);
			workingplatformInfoSection.setCreateTime(new Date());
		}else{//更新
			workingplatformInfoSection.setCreatorName(name);
			workingplatformInfoSection.setCreateTime(new Date());
		}
		return super.save(workingplatformInfoSection);
	}
	
}
