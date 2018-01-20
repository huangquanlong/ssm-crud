package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.OcrmFSeCustRiskInfoList;
import com.yuchengtech.bcrm.system.service.PaperAnswerService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.core.QueryHelper;
@SuppressWarnings("serial")
@Action("/PaperAnswer")
public class PaperAnswerAction  extends CommonAction{

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	@Autowired
    private PaperAnswerService PaperAnswerService ;
    @Autowired
	public void init(){
	  	model = new OcrmFSeCustRiskInfoList(); 
		setCommonService(PaperAnswerService);
	}
	/**
	 * 用户查询拼装SQL
	 */
    

	
	public void prepare() {
	    ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		
		this.setJson(request.getParameter("condition"));
		String paper_id = request.getParameter("paper_id");
		
    	StringBuffer sb = new StringBuffer("select t.cust_q_id,t.evaluate_name,t.evaluate_date,t.papers_id ,m.paper_name " +
    			"from ocrm_f_se_cust_risk_info_list t" +
    			" left join ocrm_f_sm_papers m on m.id=t.papers_id where 1=1 and ( t.papers_id = "+paper_id+")");
//    	sb.append(" and  where t.paper_id = '"+paper_id+"'");
//    	if(!(paper_id.equals(""))){
//    		sb.append(" and  where t.paper_id = '"+paper_id+"'");
//    	}
    SQL=sb.toString();
   	datasource = ds;
   	try{
   		json=new QueryHelper(SQL, ds.getConnection()).getJSON();
   	}catch(Exception e){}
}	
    
    public String addCustRiskEvaluation() throws Exception{
    	 ActionContext ctx = ActionContext.getContext();
         request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
         String paperId = request.getParameter("paperId");
         String user = request.getParameter("user");
         String title_result = request.getParameter("title_result");
    	OcrmFSeCustRiskInfoList crl = (OcrmFSeCustRiskInfoList)model;
    	PaperAnswerService.addCustRiskEvaluation(crl,paperId,user,title_result);
    	return "success";
    }
    
    public String updateCustRiskEvaluation() throws Exception{
    	OcrmFSeCustRiskInfoList crl = (OcrmFSeCustRiskInfoList)model;
    	PaperAnswerService.save(crl);
    	return "success";
    }
    public String loadCustRiskQa() throws Exception{
    	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String custqId = request.getParameter("custqId");
    	if(custqId!=null &&!"".equals(custqId)){
    		json = PaperAnswerService.loadCustRiskQa(new Long(custqId));
    	}
    	return "success";
    }
 


}