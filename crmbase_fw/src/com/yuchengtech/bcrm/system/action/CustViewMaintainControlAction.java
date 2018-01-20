package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.OcrmSysViewManagerControl;
import com.yuchengtech.bcrm.system.service.CustViewMaintainControlService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 客户视图项控制点维护
 * @author  chenmeng
 * @since 2014-12-19
 */
@SuppressWarnings("serial")
@Action("/CustViewMaintainControl-action")
public class CustViewMaintainControlAction  extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	
	@Autowired
	private CustViewMaintainControlService custViewMaintainControlService;
	
	@Autowired
	public void init() {
		model = new OcrmSysViewManagerControl();
		setCommonService(custViewMaintainControlService);
		// 新增修改删除记录是否记录日志,默认为false，不记录日志
		needLog = false;;
	}

	/**
	 * 查询控制点表格
	 * @return OCRM_SYS_VIEW_MANAGER_CONTROL
	 */
	public void prepare() {
		ActionContext ctx = ActionContext.getContext();
	    request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
	    String managerId = request.getParameter("managerId");
		StringBuilder sb = new StringBuilder("select * from OCRM_SYS_VIEW_MANAGER_CONTROL where MANAGER_ID='"+managerId+"'");		
		SQL = sb.toString(); //为父类SQL属性赋值（设置查询SQL）
		datasource = ds; //为父类数据源赋值
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
		custViewMaintainControlService.batchRemove(idStr);
		return "success";
	}
}
