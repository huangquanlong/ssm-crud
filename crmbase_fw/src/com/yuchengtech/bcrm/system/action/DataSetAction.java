package com.yuchengtech.bcrm.system.action;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.yuchengtech.bcrm.system.model.DataSet;
import com.yuchengtech.bcrm.system.service.DataSetService;
@ParentPackage("json-default")
@Action(value="/dataset", results={
    @Result(name="success", type="json")
})
public class DataSetAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable{
	private static final long serialVersionUID = -2010621122837504304L;
	private DataSet model = new DataSet();
    private Collection<Map<String, Object>> list;
    private Long id;
    @Autowired
    private DataSetService dataSetService=new DataSetService();
    private HttpServletRequest request;

    // GET /orders
    public HttpHeaders index() {

        return new DefaultHttpHeaders("index")
            .disableCaching();
    }
	
    // GET /orders/1
    public HttpHeaders show() {
        return new DefaultHttpHeaders("show");
    }

    // GET /orders/1/edit
    public String edit() {
        return "edit";
    }

    // GET /orders/new
    public String editNew() {
   
        return "editNew";
    }

    // GET /orders/1/deleteConfirm
    public String deleteConfirm() {
        return "deleteConfirm";
    }

    // DELETE /orders/1
    public String destroy() {
    	
    	
    	//customerBaseService.remove(id);
        return "success";
    }

    /**
     * 数据集管理
     * @param operate 
     * add 新增列配置
     * update 修改列配置
     * updatenew 清空以前配置，然后再新增列配置
     * delete 删除列配置
     * 
     * @return
     * @throws Exception
     */
    public HttpHeaders create() throws Exception {
    	ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        if(request.getParameter("operate").equals("add")){
		    String s1=request.getParameter("NAME");
		    String s2=request.getParameter("REMARKS");
		    String s3=request.getParameter("COLTYPE");
		    String s4=request.getParameter("LENGTH");
		    String s5=request.getParameter("NULLS");
		    String s6=request.getParameter("KEYSEQ");
		    String s7=request.getParameter("NOTES");
		    String s8=request.getParameter("COLNO");
		    String dataSetId=request.getParameter("dataSetId");
		    
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
		    //Long dataSetId= dataSetService.save(model);
		    dataSetService.batchSave(jarray1,jarray2,jarray3,jarray4,jarray5,jarray6,jarray7,jarray8,Long.valueOf(dataSetId));
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
		    //dataSetService.update(model);
		    dataSetService.batchUpdate(jarray2,jarray7,jarray9);
        }
        else if(request.getParameter("operate").equals("delete")){
  		    String s9=request.getParameter("ID");
  		    JSONObject jsonObject9 =JSONObject.fromObject(s9);
		    JSONArray jarray9 =  jsonObject9.getJSONArray("id");
		    dataSetService.removeAll(jarray9);
        }
        else if(request.getParameter("operate").equals("updatenew")){
           // dataSetService.update(model);
        	String pid=request.getParameter("pid");
            dataSetService.remove(Long.valueOf(pid));
        	
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
  		    dataSetService.batchSave(jarray1,jarray2,jarray3,jarray4,jarray5,jarray6,jarray7,jarray8,Long.valueOf(pid));
        }
        
        return new DefaultHttpHeaders("success").setLocationId(model.getId());
    }

    // PUT /orders/1
    public String update() throws Exception {
    	//customerBaseService.save(model);
        return "success";
    }
    //校验方法
    public void validate() {
    }
    
    public Long getId() { 
        return this.id; 
    } 

    public void setId(Long id) {
        this.id = id;
    }
    
    public Object getModel(){ 
        return (list != null ? list : model); 
    } 
}
