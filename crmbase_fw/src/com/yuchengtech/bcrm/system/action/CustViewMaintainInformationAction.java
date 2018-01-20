package com.yuchengtech.bcrm.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.OcrmSysViewManager;
import com.yuchengtech.bcrm.system.service.CustViewMaintainService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.common.DataType;

/**
 * 客户视图项维护的新增修改和删除
 * @author zhangsxin chenmeng
 * @since 2014-12-19
 */

@SuppressWarnings("serial")
@Action("/CustViewMaintainInfo-action")
public class CustViewMaintainInformationAction  extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	@Autowired
	private CustViewMaintainService custViewMaintainService;
	
	@Autowired
	public void init() {
		model = new OcrmSysViewManager();
		setCommonService(custViewMaintainService);
	}

	/**
	 * 客户视图项维护的查询
	 */
	public void prepare() {
		StringBuilder sb = new StringBuilder("SELECT A.*,B.NAME AS PARENT_NAME FROM OCRM_SYS_VIEW_MANAGER A ");
		sb.append("	LEFT JOIN OCRM_SYS_VIEW_MANAGER B ON A.PARENTID=B.ID");
		sb.append("  WHERE 1=1");
		
		for (String key : this.getJson().keySet()) { //循环获取查询条件
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("NAME")){ //视图项名称
					sb.append(" AND A.NAME like '%" + this.getJson().get(key) + "%'");
				}else if(key.equals("VIEWTYPE")){//客户视图项类型
					sb.append(" AND A.VIEWTYPE like '%" + this.getJson().get(key) + "%'");
				}
			}
		}
		
		SQL = sb.toString(); //为父类SQL属性赋值（设置查询SQL）
		datasource = ds; //为父类数据源赋值
		
		configCondition("B.NAME", "like", "PARENT_NAME",DataType.String);
	}
	
	/**
     * 数据删除提交
     * HTTP:DELETE方法
     * URL:/actionName/$id
     */
	public String destroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr"); //获取需要删除的控制id
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {//删除控制点
			long id = Long.parseLong(strarray[i]);
			String jql = new String("delete  from OcrmSysViewManagerControl  where managerId='"+id+"'");
			Map<String,Object> values=new HashMap<String,Object>();
			custViewMaintainService.batchUpdateByName(jql, values);
			addActionMessage("batch removed successfully");
		}
		custViewMaintainService.batchRemove(idStr);//删除视图项
		return "success";   	
	}
}
   
