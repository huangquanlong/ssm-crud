package com.yuchengtech.bcrm.system.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.FwSysProp;
import com.yuchengtech.bcrm.system.service.CustOnwerParaService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.core.SysPublicParamManager;
/***
 * 客户归属参数设置
 */
@SuppressWarnings("serial")
@Action("/custOnwerPara")
public class CustOnwerParaAction extends CommonAction {
	@Autowired
	private CustOnwerParaService custOnwerParaService;
	@Autowired
	public void init(){
		model = new FwSysProp();
		setCommonService(custOnwerParaService);
	}
	/***
	 * 保存
	 * @return
	 */
	 public String saveData()
	 {
		 List<FwSysProp> belongParamsList = new ArrayList<FwSysProp>();
		 ActionContext ctx = ActionContext.getContext();
         request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
         String saveStr = request.getParameter("saveStr");
         belongParamsList = custOnwerParaService.saveData(saveStr);
         /**客户归属参数设置更新*/
         SysPublicParamManager.getInstance().loadSysPublicParams();
		 return "";
	 }
}