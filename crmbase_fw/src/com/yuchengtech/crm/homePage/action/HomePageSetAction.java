package com.yuchengtech.crm.homePage.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.homePage.model.HomePageSet;
import com.yuchengtech.crm.homePage.service.PanelService;

/**
 * 
 * 页签配置信息Action 根据页签号删除用户页签内的甩的模块
 * 
 * 添加模块到页签
 * 
 * @author lenovo
 * 
 */

@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/homePageSet")
public class HomePageSetAction extends CommonAction {
	private HttpServletRequest request;

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	// 获取当前用户号
	AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();



	// 2013 -10 -15 页签处理
	@Autowired
	private PanelService panelService;

	@Autowired
	public void init() {
		model = new HomePageSet();
		//setCommonService(ocrmFSysRuleService);
	}

	/**
	 * 根据tabid删除用户页签的模块
	 * 
	 * @return
	 */
	public String deleteTabPanelModeByTabID() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);

		String tabID = (String) request.getParameter("tabID");
		if (null != tabID && !"".equals(tabID)) {
			String jql = "delete from HomePageSet p where p.userID =:userID  and p.tabID = :tabID ";
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("userID", auth.getUserId());
			values.put("tabID", tabID);

			executeUpdate(jql, values);

			addActionMessage("batch removed successfully");
		}
		return "success";
	}

	/**
	 * 更新TabPanelMode信息
	 * 
	 * @return
	 */
	public DefaultHttpHeaders updateTabPanelMode() {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);

			String tabID = (String) request.getParameter("tabID");
			String modID = (String) request.getParameter("modID");
			String colNum = (String) request.getParameter("colNum");
			String rowNum = (String) request.getParameter("rowNum");

			// 页签内容配置对象
			HomePageSet homeMod = new HomePageSet();
			homeMod.setTabID(tabID);
			homeMod.setModID(modID);
			homeMod.setUserID(auth.getUserId());
			homeMod.setModRow(Integer.parseInt(rowNum));
			homeMod.setModCol(Integer.parseInt(colNum));

			// 删除已存在的对象
			// panelService.removePanelMode(homeMod);

			// 增加模块
			panelService.addPanelMode(homeMod);
			return new DefaultHttpHeaders("success").disableCaching();

		} catch (Exception e) {
			e.printStackTrace();
			return new DefaultHttpHeaders("failure").disableCaching();
		}

	}

}
