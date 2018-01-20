package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.service.QuestionManageService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value = "/quessionManage", results = { @Result(name = "success", type = "json") })
@SuppressWarnings("unchecked")
public class QuessionManageAction extends CommonAction {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private QuestionManageService questionManageService;

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	@Override
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);

		StringBuilder sb = new StringBuilder("");

		sb.append(" select O.TITLE_NAME,O.TITLE_TYPE,O.TITLE_SORT,O.AVAILABLE,A.USER_NAME,O.UPDATE_DATE,O.TITLE_ID");
		sb.append(" from OCRM_F_SE_TITLE O");
		sb.append(" LEFT OUTER JOIN ADMIN_AUTH_ACCOUNT A ON O.UPDATOR = A.ACCOUNT_NAME");
		sb.append(" where 1=1");

//		if (request.getParameter("start") != null)
//			start = new Integer(request.getParameter("start")).intValue();
//		if (request.getParameter("limit") != null)
//			limit = new Integer(request.getParameter("limit")).intValue();

		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key)
					&& !this.getJson().get(key).equals("")) {
				if (key.equals("TITLE_TYPE")) {
					sb.append(" and O.TITLE_TYPE='" + this.getJson().get(key)
							+ "'");
				} else if (key.equals("TITLE_NAME")) {
					sb.append(" and O.TITLE_NAME like '%"
							+ this.getJson().get(key) + "%'");
				} else if (key.equals("AVAILABLE")) {
					sb.append(" and O.AVAILABLE= '" + this.getJson().get(key)
							+ "'");
				}
			}
		}

		sb.append(" ORDER BY O.TITLE_SORT");
		SQL = sb.toString();
		datasource = ds;

	}
	
	public String createQuession() {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		this.json.put("UPDATOR", auth.getUserId());
		questionManageService.createQuession(this.json);
		return "sucess";
	}
//	public void save(){
//		 ActionContext ctx = ActionContext.getContext();
//         request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
//         String titleName = request.getParameter("titleName");
//         String titleType = request.getParameter("titleType");
//         String available = request.getParameter("available");
//         String updator = request.getParameter("updator");
//         
//         String s2 = this.request.getParameter("condition");
//         String s = this.request.getParameter("resultInfo");//
//		    if((!(s.equals("[]")))&&(!(s2.equals("[]"))))
//		    	{
//		    	JSONArray jarray = JSONArray.fromObject(s);
//		    	JSONArray jarray2 = JSONArray.fromObject(s2);
////		    	 this.questionManageService.save(jarray);
//		    	 questionManageService.save(titleName,titleType,available,updator,jarray,jarray2);
//		    } 
//        
//	}



}
