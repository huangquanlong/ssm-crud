package com.yuchengtech.bcrm.system.action;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.OcrmFSmPaper;
import com.yuchengtech.bcrm.system.service.PaperManageService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.crm.exception.BizException;

/**
 * 问卷管理Action
 * @author luyy
 * @since 2014-06-13
 */

@SuppressWarnings("serial")
@Action("/paperManage")
public class PaperManageAction extends CommonAction {
	
	@Autowired
	private PaperManageService service;//定义UserManagerService属性
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//定义数据源属性
	
	@Autowired
	public void init() {
		model = new OcrmFSmPaper();
		setCommonService(service);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	/**
	 * 用户查询拼装SQL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		StringBuffer sb = new StringBuffer("");
		if(!"".equals(id)&&id != null){
			sb.append(" select * from OCRM_F_SM_PAPERS where id ='"+id+"'");
		}else{
			sb.append("SELECT t.*,decode(r.count ,0,'no',null,'no','yes') as HASQ from OCRM_F_SM_PAPERS t left join " +
					"(select count(paper_id) as count,paper_id as id from OCRM_F_SM_PAPERS_QUESTION_REL group by paper_id) r  " +
					"on t.id = r.id where 1=1 ");
			if("3".equals(type)){//客户满意度调查问卷
				sb.append(" and t.OPTION_TYPE = '3' and t.AVAILABLE='4' ");
			}
		}
		
		for(String key : this.getJson().keySet()){
			if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
				if (key.equals("CREATE_DATE")||key.equals("PUBLISH_DATE")){
					sb.append("   AND t."+key+" =to_char("+" '"+this.getJson().get(key).toString().substring(0, 10)+"','YYYY-MM-dd')");
				}else{
					sb.append("  AND t."+key+" like"+" '%"+this.getJson().get(key)+"%'");
				}
			}
		}
        SQL=sb.toString();
        datasource = ds;
        try{
        	json=new QueryHelper(SQL, ds.getConnection()).getJSON();
        }catch(Exception e){
        	e.printStackTrace();
			throw new BizException(1,2,"1002","查询问卷信息出错");
        }
	}	

	 
	 public DefaultHttpHeaders saveQuestion(){
		 
		 try{
			 ActionContext ctx = ActionContext.getContext();
			 this.request = ((HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest"));
			 
			 String s3 = this.request.getParameter("addArray");//
			 String s4 = this.request.getParameter("deleteArray");//
			 if(!(s3.equals("[]")))
			 {
				 JSONArray jarray = JSONArray.fromObject(s3);
				 this.service.saveQ(jarray);
			 } 
			 if(!(s4.equals("[]"))){
		  	    	JSONArray jarray2 = JSONArray.fromObject(s4);
		  	    	this.service.removeQ(jarray2);
			 }
		 }catch(Exception e){
			 e.printStackTrace();
			 throw new BizException(1,2,"1002",e.getMessage());
		 }
		 return new DefaultHttpHeaders("success");
		   
	 }
		/**
		 * 修改停启用状态方法
		 * @return
		 * @throws Exception
		 */
	 public String updateState() throws Exception{
			
		 try{
			 ActionContext ctx = ActionContext.getContext();
			 request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			 long idStr = Long.parseLong((request.getParameter("id")));//获取用户信息主键ID
			 String available = request.getParameter("available");//获取用户密码
			 String jql = "update  OcrmFSmPaper a set a.available ='"+available+"'" +
			 " where a.id in ("+idStr+")";
			 Map<String,Object> values = new HashMap<String,Object>();
			 super.executeUpdate(jql, values);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return "success";
	 }
	 
	 public void batchDel(){
		 ActionContext ctx = ActionContext.getContext();
		 request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		 String s[] = request.getParameter("idStr").split(",");
			for(int i=0;i<s.length;i++){
			 String jql = "delete from OcrmFSmPapersQuestionRel where paperId = "+BigDecimal.valueOf(Long.valueOf(s[i]))+"";
			 Map<String,Object> values = new HashMap<String,Object>();
			 super.executeUpdate(jql, values);
			 
			 String jql1 = "delete from OcrmFSmPaper where id = "+Long.valueOf(s[i])+"";
			 Map<String,Object> values1 = new HashMap<String,Object>();
			 super.executeUpdate(jql1, values1);
		 }
		
		 
	 }
	 /**
		 * 发起工作流
		 * */
		public void initFlow() throws Exception{
		  	ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String requestId =  request.getParameter("instanceid");
			String name =  request.getParameter("name");
			String instanceid = "WJ_"+requestId;//此处为组装流程实例号，通过自定义标识加上业务主键id组装，在流程办理时候可以通过截取业务id查询业务信息
			  String jobName = "问卷复核_"+name;//自定义流程名称
			  service.initWorkflowByWfidAndInstanceid("16", jobName, null, instanceid);//调用CommonService中的该方法发起工作流，第三个参数可以自定义一些变量，用于路由器条件等
			
			  Map<String,Object> map=new HashMap<String,Object>();
				map.put("instanceid", instanceid);
			    map.put("currNode", "16_a3");
			    map.put("nextNode",  "16_a4");
			    this.setJson(map);
		
		}
		
		   /**
		 * 流程提交
		 * */
		public void initFlowJob() throws Exception{
//		  	ActionContext ctx = ActionContext.getContext();
//			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
//			String instanceid = "WJ_"+request.getParameter("instanceid");
//			service.wfCompleteJob(instanceid, "16_a3", "16_a4", null, null);
		}

		public void publish(){
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			String id = request.getParameter("id");
			String type = request.getParameter("type");
			Map map = new HashMap();
			if("1".equals(type)){//风险评测问卷    原已发布的变成未发布
				service.batchUpdateByName(" update OcrmFSmPaper p set p.available='3' where p.available='4' and p.optionType='4'", map);
				map.put("date", new Date());
				service.batchUpdateByName(" update OcrmFSmPaper p set p.available='4',p.publishDate=:date where p.id='"+Long.parseLong(id)+"'", map);
			}else{
				map.put("date", new Date());
				service.batchUpdateByName(" update OcrmFSmPaper p set p.available='4',p.publishDate=:date where p.id='"+Long.parseLong(id)+"'", map);
			}
		}
	 
	  
}


