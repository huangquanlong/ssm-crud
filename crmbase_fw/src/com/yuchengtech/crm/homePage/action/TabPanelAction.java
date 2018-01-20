/**
 * 
 */
package com.yuchengtech.crm.homePage.action;

import java.util.HashMap;

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
import com.yuchengtech.crm.homePage.model.TabPanel;
import com.yuchengtech.crm.homePage.model.UserPanMode;
import com.yuchengtech.crm.homePage.service.PanelService;
import com.yuchengtech.crm.homePage.service.PreFixService;
import com.yuchengtech.crm.homePage.service.UserPanelService;

/**
 * @author zkl
 * 
 *         TabPanel 处理类，添加Tab ,更新tab名字，删除tab等
 * 
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/tabPanel")
public class TabPanelAction extends CommonAction {
	private HttpServletRequest request;

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	// 获取当前用户号
	AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();


	@Autowired
	private PreFixService preFixService;

	@Autowired
	private UserPanelService userPanelService;

	// 2013 -10 -15 页签处理
	@Autowired
	private PanelService panelService;

	@Autowired
	public void init() {
		model = new TabPanel();
		setCommonService(panelService);
	}

	// 查询方法
	public void prepare() {

	}

	/**
	 * 添加 tabpanel
	 * 
	 * @return
	 */
	public DefaultHttpHeaders addTabPanel() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);

		TabPanel tabPanel = new TabPanel();
		String tabPanName = (String) request.getParameter("tabPanName");

		try {
			// 处理中文文件名的问题
			tabPanName = java.net.URLDecoder.decode(tabPanName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		tabPanel.setTabName(tabPanName);
		// 调用ID生成方法
		String _newID = preFixService.getPreFixID("TP");
		tabPanel.setTabID(_newID);

		// 调用增加方法
		panelService.addTabPanel(tabPanel);

		// 添加到 UserTabPanel 中
		String tabNum = (String) request.getParameter("tabNum");
		String layoutID = (String) request.getParameter("layoutID");

		UserPanMode userPanMode = new UserPanMode();
		// 设置内容
		userPanMode.setTabID(_newID);
		userPanMode.setUserID(auth.getUserId());
		userPanMode.setTabNum(Integer.parseInt(tabNum));
		userPanMode.setLayID(layoutID);
		// 添加到表中
		userPanelService.addUserPanelMode(userPanMode);

		if (this.json != null)
			this.json.clear();
		else
			this.json = new HashMap<String, Object>();

		// 返回界面新的TabID号
		this.json.put("newTabPanID", _newID);

		return new DefaultHttpHeaders("success").disableCaching();
	}

	/**
	 * 更新TabPanelName的方法
	 * 
	 * @return
	 */
	public DefaultHttpHeaders updateTabPanName() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		try {
			TabPanel tabPanel = new TabPanel();
			String tabPanName = (String) request.getParameter("tabPanName");
			String tabID = (String) request.getParameter("tabID");

			// 处理中文文件名的问题
			tabPanName = java.net.URLDecoder.decode(tabPanName, "UTF-8");

			tabPanel.setTabName(tabPanName);
			tabPanel.setTabID(tabID);

			// 调用更改TabPanelName的方法
			panelService.updateTabpanelName(tabPanel);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new DefaultHttpHeaders("success").disableCaching();
	}

	/**
	 * 根据tabid 删除tabpanel
	 * 
	 * @return
	 */
	public DefaultHttpHeaders deleteTabPanByID() {

		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		try {
			String tabID = (String) request.getParameter("tabID");

			// 调用删除页签操作
			if (null != tabID && !"".equals(tabID))
				panelService.removeTabpanel(tabID);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new DefaultHttpHeaders("success").disableCaching();

	}

	

	
}
