package com.yuchengtech.crm.homePage.action;

import java.util.HashMap;
import java.util.Map;

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
import com.yuchengtech.bob.service.CommonQueryService;
import com.yuchengtech.crm.homePage.model.PanelMode;
import com.yuchengtech.crm.homePage.service.ModeService;
import com.yuchengtech.crm.homePage.service.PreFixService;

/**
 * 模块功能方法
 * 
 * @author zkl
 * 
 */
@ParentPackage("json-default")
@Action(value = "/panelMode", results = { @Result(name = "success", type = "json"), })
public class PanelModeAction extends CommonAction {
	private HttpServletRequest request;

	@Autowired
	private CommonQueryService cqs;
	
	@Autowired
	private PreFixService preFixService;
	
	@Autowired
	private ModeService modService;
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource dsOracle;

	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 获取所有模块列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public void prepare() {
		StringBuilder sb = new StringBuilder(
				"select mod.mod_id,mod.mod_name,mod.mod_type,mod.mod_action,mod.mod_cm,mod.mod_swffile,mod.mod_icon ");
		sb.append(" from OCRM_F_CI_MODILE  mod ");
		SQL = sb.toString();
		datasource = dsOracle;
	}

	/**
	 * 获取所有模块信息
	 * @return
	 */
	public String getAllPanelMode() {
		StringBuilder sb = new StringBuilder(
				"select mod.mod_id,mod.mod_name,mod.mod_type,mod.mod_action,mod.mod_cm,mod.mod_swffile,mod.mod_icon ");
		sb.append(" from OCRM_F_CI_MODILE  mod ");
		map = cqs.excuteQuery(sb.toString(), 0, 100);
		this.json = map;
		return "success";
	}
	
	/**
	 * 添加模块信息
	 */
	public String addMode(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		
		PanelMode panelMode = new PanelMode();
		
		panelMode.setModAction((String)request.getParameter("modAction"));
		panelMode.setModCM((String)request.getParameter("modCM"));
		panelMode.setModIcon((String)request.getParameter("modIcon"));
		panelMode.setModID(preFixService.getPreFixID("MD"));
		panelMode.setModName((String)request.getParameter("modAction"));
		panelMode.setModSwfFile((String)request.getParameter("modSwfFile"));
		panelMode.setModType((String)request.getParameter("modType"));
		
		modService.addMode(panelMode);
		
		return "success";
	}
	
	/**
	 * 更新Mode信息
	 * @return
	 */
	public String updateMode()
	{
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		
		PanelMode panelMode = new PanelMode();
		
		panelMode.setModAction((String)request.getParameter("modAction"));
		panelMode.setModCM((String)request.getParameter("modCM"));
		panelMode.setModIcon((String)request.getParameter("modIcon"));
		panelMode.setModID(preFixService.getPreFixID("MD"));
		panelMode.setModName((String)request.getParameter("modAction"));
		panelMode.setModSwfFile((String)request.getParameter("modSwfFile"));
		panelMode.setModType((String)request.getParameter("modType"));
		
		modService.updateMode(panelMode);
		
		return "success";
	}
	
	
	/**
	 * 根据ID删除MODE
	 * @return
	 */
	public String removeMode(){
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String modID = request.getParameter("modID");
		if(null != modID && !"".equals(modID))
			modService.removeMode(modID);
		
		return "success";
	}
	
	

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}
