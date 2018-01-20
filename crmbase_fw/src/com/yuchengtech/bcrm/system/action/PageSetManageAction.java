package com.yuchengtech.bcrm.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.service.PageSetManageService;
import com.yuchengtech.bob.common.CommonAction;

/***
 * 页面操作设置的Action
 * @author songxs
 * @since 2013-3-5
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value="/pageSetManageAction", results={
    @Result(name="success", type="json"),
})
public class PageSetManageAction extends CommonAction{

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源
	@Autowired//注入service
	private PageSetManageService pageSetManageService;

	/**
	 * 查询方法
	 */
    public void prepare() {
    	StringBuilder sb = new StringBuilder("SELECT T.ID, T.VERSION, T.APP_ID, T.PROP_NAME, T.PROP_DESC, T.PROP_VALUE, T.REMARK FROM FW_SYS_PROP T WHERE T.PROP_NAME LIKE 'indexCfg%'");
        setPrimaryKey("T.ID");
        SQL=sb.toString();
        datasource = ds;
    }
    /**
     * 保存参数设置
     * @return
     */
    public String updatePageSet(){
    	ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String models = request.getParameter("models");
		JSONArray jarray = null;
		if(!models.equals("[]")){
			jarray = JSONArray.fromObject(models);
			pageSetManageService.updateData(jarray);			
		}
    	return "success";
    }
}