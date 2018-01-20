package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.DataSet;
import com.yuchengtech.bcrm.system.service.DataSetService;
import com.yuchengtech.bob.common.CommonAction;


@SuppressWarnings("serial")
@Action("/dataSetNewAction")
public class DataSetNewAction extends CommonAction {

	private HttpServletRequest request;
	@Autowired
	private DataSetService dataSetService;
	@Autowired
	public void init() {
		model = new DataSet();
		setCommonService(dataSetService);
		needLog = true;//新增修改删除记录是否记录日志,默认为false，不记录日志
	}
    // POST /orders
    public String saveColInfo() throws Exception {
    	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        Long dataSetId =null;
        if(request.getParameter("operate").equals("add")){
		    String s1=request.getParameter("NAME");
		    String s2=request.getParameter("REMARKS");
		    String s3=request.getParameter("COLTYPE");
		    String s4=request.getParameter("LENGTH");
		    String s5=request.getParameter("NULLS");
		    String s6=request.getParameter("KEYSEQ");
		    String s7=request.getParameter("NOTES");
		    String s8=request.getParameter("COLNO");
		    
		    JSONObject jsonObject1 =JSONObject.fromObject(s1);
		    JSONArray jarray1=  jsonObject1.getJSONArray("NAME");
		    JSONObject jsonObject2 =JSONObject.fromObject(s2);
		    JSONArray jarray2 =  jsonObject2.getJSONArray("REMARKS");
		    JSONObject jsonObject3 =JSONObject.fromObject(s3);
		    JSONArray jarray3 =  jsonObject3.getJSONArray("COLTYPE");
		    JSONObject jsonObject4 =JSONObject.fromObject(s4);
		    JSONArray jarray4 =  jsonObject4.getJSONArray("LENGTH");
		    JSONObject jsonObject5 =JSONObject.fromObject(s5);
		    JSONArray jarray5 =  jsonObject5.getJSONArray("NULLS");
		    JSONObject jsonObject6 =JSONObject.fromObject(s6);
		    JSONArray jarray6 =  jsonObject6.getJSONArray("KEYSEQ");
		    JSONObject jsonObject7 =JSONObject.fromObject(s7);
		    JSONArray jarray7 =  jsonObject7.getJSONArray("NOTES");
		    JSONObject jsonObject8 =JSONObject.fromObject(s8);
		    JSONArray jarray8 =  jsonObject8.getJSONArray("COLNO");
//		    Long dataSetId= dataSetService.save(model);
		    dataSetService.batchSave(jarray1,jarray2,jarray3,jarray4,jarray5,jarray6,jarray7,jarray8,dataSetId);
		  // customerRelateCustomerBaseService.batchSave(jarray,jarray1,jarray2,x);
        }
        else if(request.getParameter("operate").equals("update")){
  		    String s2=request.getParameter("REMARKS");
  		    String s7=request.getParameter("NOTES");
  		    String s9=request.getParameter("ID");
  		    JSONObject jsonObject2 =JSONObject.fromObject(s2);
  		    JSONArray jarray2 =  jsonObject2.getJSONArray("REMARKS");
  		    JSONObject jsonObject7 =JSONObject.fromObject(s7);
  		    JSONArray jarray7 =  jsonObject7.getJSONArray("NOTES");
  		    JSONObject jsonObject9 =JSONObject.fromObject(s9);
		    JSONArray jarray9 =  jsonObject9.getJSONArray("ID");
//		    dataSetService.update(model);
		    dataSetService.batchUpdate(jarray2,jarray7,jarray9);
        }
        else if(request.getParameter("operate").equals("delete")){
  		    String s9=request.getParameter("ID");
  		    JSONObject jsonObject9 =JSONObject.fromObject(s9);
		    JSONArray jarray9 =  jsonObject9.getJSONArray("id");
		    dataSetService.removeAll(jarray9);
        }
        else if(request.getParameter("operate").equals("updatenew")){
//            dataSetService.update(model);
//           dataSetService.remove(model.getId());
        	
        	String s1=request.getParameter("NAME");
  		    String s2=request.getParameter("REMARKS");
  		    String s3=request.getParameter("COLTYPE");
  		    String s4=request.getParameter("LENGTH");
  		    String s5=request.getParameter("NULLS");
  		    String s6=request.getParameter("KEYSEQ");
  		    String s7=request.getParameter("NOTES");
  		    String s8=request.getParameter("COLNO");
  		    
  		    JSONObject jsonObject1 =JSONObject.fromObject(s1);
  		    JSONArray jarray1=  jsonObject1.getJSONArray("NAME");
  		    JSONObject jsonObject2 =JSONObject.fromObject(s2);
  		    JSONArray jarray2 =  jsonObject2.getJSONArray("REMARKS");
  		    JSONObject jsonObject3 =JSONObject.fromObject(s3);
  		    JSONArray jarray3 =  jsonObject3.getJSONArray("COLTYPE");
  		    JSONObject jsonObject4 =JSONObject.fromObject(s4);
  		    JSONArray jarray4 =  jsonObject4.getJSONArray("LENGTH");
  		    JSONObject jsonObject5 =JSONObject.fromObject(s5);
  		    JSONArray jarray5 =  jsonObject5.getJSONArray("NULLS");
  		    JSONObject jsonObject6 =JSONObject.fromObject(s6);
  		    JSONArray jarray6 =  jsonObject6.getJSONArray("KEYSEQ");
  		    JSONObject jsonObject7 =JSONObject.fromObject(s7);
  		    JSONArray jarray7 =  jsonObject7.getJSONArray("NOTES");
  		    JSONObject jsonObject8 =JSONObject.fromObject(s8);
  		    JSONArray jarray8 =  jsonObject8.getJSONArray("COLNO");
//  		    dataSetService.batchSave(jarray1,jarray2,jarray3,jarray4,jarray5,jarray6,jarray7,jarray8,model.getId());
        }
        
        return "success";
    }

}