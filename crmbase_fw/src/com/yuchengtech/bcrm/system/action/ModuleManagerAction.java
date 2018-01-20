package com.yuchengtech.bcrm.system.action;

import java.text.ParseException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.system.model.FwModule;
import com.yuchengtech.bcrm.system.service.ModuleManagerService;
import com.yuchengtech.bob.common.CommonAction;

/**
 * 模块管理
 * @author GUOCHI
 * @since 2012-10-09
 */
@SuppressWarnings("serial")
@Action("/FwModule-action")
public class ModuleManagerAction extends CommonAction {

	private FwModule wi = new FwModule();
	private Collection<FwModule> wic;
	private HttpServletRequest request;
	@SuppressWarnings("unused")
    private Long id;

	@Autowired
	private ModuleManagerService wis;

	@Autowired
	public void init() {// 初始化module
		model = new FwModule();
		setCommonService(wis);
		needLog = false;// 新增修改删除记录是否记录日志,默认为false，不记录日志
	}

	/**
	 * 请求数据编辑页面跳转。 HTTP:GET方法 URL:/actionName/$id/edit;
	 */
	public String edit() {
		return "edit";
	}

	/**
	 * 新增页面请求 HTTP:GET方法 URL:/actionName/new
	 */
	public String editNew() {
		return "editNew";
	}

	/**
	 * 请求删除页面 HTTP:GET方法 URL:/actionName/$id/deleteContirm
	 */
	public String deleteConfirm() {
		return "deleteConfirm";
	}

	/**
	 * 数据删除提交 HTTP:DELETE方法 URL:/actionName/$id
	 */
	public String destroy() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String idStr = request.getParameter("idStr"); // 获取需要删除的记录id
		System.out.println(idStr);
		try {
			wis.remove(idStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 数据新增提交 HTTP:POST方法 URL:/actionName
	 */
	public DefaultHttpHeaders create() {
		wis.save(wi);
		return new DefaultHttpHeaders("success").setLocationId(wi.getId());
	}

	/**
	 * 数据修改提交 HTTP:PUT方法 URL:/WorkPlatNotice/$id
	 */
	public String update() {
		wis.save(wi);
		return "success";
	}

	/**
	 * ID参数获取方法
	 * @param id
	 */
	public void setId(Long id) {
		if (id != null) {
			this.wi = (FwModule) wis.find(id);
		}
		this.id = id;
	}

	public Object getModel() {
		return (wic != null ? wic : wi);
	}
}
