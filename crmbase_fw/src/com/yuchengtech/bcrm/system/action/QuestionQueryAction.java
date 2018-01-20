package com.yuchengtech.bcrm.system.action;


import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;

@ParentPackage("json-default")
@Action(value = "/questionQuery", results = { @Result(name = "success", type = "json") })
@SuppressWarnings("unchecked")
public class QuestionQueryAction extends CommonAction {
	@Autowired
	private HttpServletRequest request;


	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	@Override
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String paperId = request.getParameter("paperId");
		StringBuilder sb = new StringBuilder("");
		sb.append("select f.* ,(case  when f.checked is null then  0 else  1 end) is_checked"+
		  " from (select t1.*, e.*  from ocrm_f_se_title t1  left join (select t2.question_id, t2.id, t2.paper_id as checked,t4.question_order "+
		  "  from ocrm_f_sm_papers_question_rel t2"+
		  "  left join ocrm_f_sm_papers t3 on t3.id = t2.paper_id"+
		  "  left join ocrm_f_sm_papers_question_rel t4 on t4.question_id=t2.question_id and t4.paper_id="+paperId+""+
		  " where t2.paper_id ="+paperId+") e on e.question_id = t1.title_id) f order by is_checked desc,question_order ");

		SQL = sb.toString();
		datasource = ds;

	}

}
