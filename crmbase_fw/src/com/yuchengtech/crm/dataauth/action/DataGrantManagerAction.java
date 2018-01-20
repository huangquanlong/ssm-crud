package com.yuchengtech.crm.dataauth.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;
import com.yuchengtech.crm.dataauth.service.DataGrantManagerService;
/**
 * 模块管理
 * @author GUOCHI
 * @since 2012-11-19
 */
@SuppressWarnings("serial")
@Action("/DataGrantManager-action")
public class DataGrantManagerAction extends CommonAction {

	private AuthSysFilterAuth wi = new AuthSysFilterAuth();
	private Collection<AuthSysFilterAuth> wic;
	private HttpServletRequest request;
	@SuppressWarnings("unused")
    private Long id;

	@Autowired
	private DataGrantManagerService wis;

	@Autowired
	public void init() {// 初始化module
		model = new AuthSysFilterAuth();
		setCommonService(wis);
		needLog = false;// 新增修改删除记录是否记录日志,默认为false，不记录日志
	}
	public DefaultHttpHeaders saveAllot(){
        ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        String ROLE_ID = request.getParameter("roleId");
        String FILTER_ID = request.getParameter("filterId");
        JSONObject jsonObject =JSONObject.fromObject(FILTER_ID);
        JSONArray jarray =  jsonObject.getJSONArray("FILTER_ID");
         
        wis.batchSave(jarray,ROLE_ID);
         
        return new DefaultHttpHeaders("success");
    }

	/**
	 * ID参数获取方法
	 * @param id
	 */
	public void setId(Long id) {
		if (id != null) {
			this.wi = (AuthSysFilterAuth) wis.find(id);
		}
		this.id = id;
	}

	public Object getModel() {
		return (wic != null ? wic : wi);
	}
}
