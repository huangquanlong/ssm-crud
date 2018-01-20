
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
import com.yuchengtech.crm.constance.SystemConstance;
/**
 * 产品放大镜查询
 * @author songxs
 * @since 2012-12-21
 * @modify luyy
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value="comProductTree-action", results={@Result(name="success", type="json")})
public class ComProductTreeAction  extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")	
	private DataSource ds;  
	
	public void prepare(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStrs = request.getParameter("idStr");	
		String fitCust = request.getParameter("fitCust");	
		StringBuffer sb  = new StringBuffer("select a.*,b.catl_name from ocrm_f_pd_prod_info a inner join ocrm_f_pd_prod_catl_view b on a.catl_code=b.catl_code where 1=1 ");
		if(null!=idStrs&&!idStrs.equals("")){
			String[] strArray = idStrs.split(","); 
			sb.append(" and ( ");
			for(int i=0;i<strArray.length;i++){
				if("DB2".equals(SystemConstance.DB_TYPE)){
					sb.append(" b.catlseq in ( select c.catlseq from ocrm_f_pd_prod_catl_view d ,ocrm_f_pd_prod_catl_view c  where d.catl_code  in "+strArray[i]+" and locate(d.catlseq,c.catlseq)>0)");
				}else{
					sb.append(" b.catlseq like (select catlseq from ocrm_f_pd_prod_catl_view where catl_code in "+strArray[i]+")||'%'");
				}
				if(i<strArray.length-1){
					sb.append(" or ");
				}
			}
			sb.append(" ) ");
		}
		if(null!=fitCust&&!fitCust.equals("")){
			if("perOnly".equals(fitCust))
				sb.append(" and TYPE_FIT_CUST like '%1%' ");
			else if("comOnly".equals(fitCust))
				sb.append(" and TYPE_FIT_CUST like '%2%' ");
			
		}
		for(String key : this.getJson().keySet()){
			if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){	
				if(null!=key&&key.equals("CATL_CODE")){
					if("DB2".equals(SystemConstance.DB_TYPE)){
						sb.append(" and b.catlseq in ( select c.catlseq from ocrm_f_pd_prod_catl_view d ,ocrm_f_pd_prod_catl_view c  where d.catl_code  = "+this.getJson().get(key)+" and locate(d.catlseq,c.catlseq)>0)");
					}else{
						sb.append(" and  b.catlseq like (select catlseq from ocrm_f_pd_prod_catl_view where catl_code = "+this.getJson().get(key)+")||'%'");
					}
				}else if(null!=key&&key.equals("PRODUCT_ID")){
					sb.append("  and a.PRODUCT_ID like '%"+this.getJson().get(key)+"%'  ");
				}else if(null!=key&&key.equals("PROD_NAME")){
					sb.append("  and a.PROD_NAME like '%"+this.getJson().get(key)+"%'  ");
				}else if (key.equals("PROD_STATE")){
					sb.append("  AND a.PROD_STATE in ( "+this.getJson().get(key)+")");
				}else if (key.equals("RISK_LEVEL")){
					sb.append("  AND a.RISK_LEVEL in ( "+this.getJson().get(key)+")");
				}	
			}
		}
		
		addOracleLookup("PROD_STATE", "PROD_STATE");
		addOracleLookup("RISK_LEVEL", "PROD_RISK_LEVEL");
		SQL=sb.toString();
		datasource = ds;
	}

}

