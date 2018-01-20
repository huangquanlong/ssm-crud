package com.yuchengtech.crm.homePage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.crm.homePage.model.OcrmFSysSheetHomepagecfg;
import com.yuchengtech.crm.homePage.service.SheetHomePageCfgService;

/**
 *  首页配置
 * @author CHANGZH@YUCHENGTECH.COM
 * @date 2013-10-31
 */

@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value = "/sheetHomePageAction")
public class SheetHomePageAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
	@Autowired
	private SheetHomePageCfgService sheetHomePageCfgService;

	@Autowired
	public void init() {
		model = new OcrmFSysSheetHomepagecfg();
		setCommonService(sheetHomePageCfgService);
	}
	/**
	 * 更新首页配置数据
	 */
	public String updateCfgInfo() {
		
		ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        List cfgList = new ArrayList();
        try {
        	/**取报表配置列表中的数据并转换成list*/
        	cfgList  = (List) JSONUtil.deserialize(request.getParameter("cfgInfo"));
        	sheetHomePageCfgService.updateCfgInfo(cfgList);
		} catch (JSONException e) {			
			e.printStackTrace();
			throw new BizException(1,2,"1002","更新首页配置数据异常。");
		} 
		
		return "success";		
	}
	
	/**
	 * 查询首页页签数据
	 */
	public String getCfgInfo() {
		// 用户首页数据
		Map<String, Object> tabCfg = new HashMap<String, Object>();
		// 用户页签数据
		Map<String, Object> modeCfg = new HashMap<String, Object>();
		try {
			// 查询用户页签信息
			StringBuilder querySQL = new StringBuilder("");
			querySQL.append(" SELECT * FROM OCRM_F_SYS_SHEETUSERTABPNEL T ");
			querySQL.append(" WHERE T.USER_ID= '" + this.getUserSession().getUserId() + "' ORDER BY ID ");
			QueryHelper queryHelper = null;
			queryHelper = new QueryHelper(querySQL.toString(),ds.getConnection());
			tabCfg = queryHelper.getJSON();
			
			// 查询模块配置信息
			querySQL = new StringBuilder("");
			querySQL.append("SELECT T.TAB_ID,P.TAB_NAME,T.MODEL_ID,T.MODEL_COLUMN,T.MODEL_ROWNUM,");
			querySQL.append(" M.MOD_NAME,M.MOD_TYPE,M.MOD_ACTION,M.MOD_CM,M.MOD_SWFFILE,M.MOD_ICON ");
			querySQL.append(" FROM OCRM_F_SYS_SHEETHOMEPAGECFG T LEFT JOIN OCRM_F_CI_MODILE M ON T.MODEL_ID = M.MOD_ID");
			querySQL.append(" LEFT JOIN OCRM_F_SYS_SHEETUSERTABPNEL P ON T.TAB_ID = P.ID");
			querySQL.append(" WHERE T.MODEL_ID IS NOT NULL");
			querySQL.append(" AND T.USER_ID = '"+ this.getUserSession().getUserId() +"'");
			querySQL.append(" ORDER BY T.TAB_ID, T.MODEL_COLUMN,T.MODEL_ROWNUM");
			queryHelper = new QueryHelper(querySQL.toString(),ds.getConnection());
			modeCfg = queryHelper.getJSON();
			if(json != null)
                json.clear();
            else
                json = new HashMap();
            json.put("tabCfg", tabCfg);
            json.put("modeCfg", modeCfg);
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new BizException(1,2,"1002","查询首页页签数据异常。");
		}
		//List<OcrmFSysSheetHomepagecfg> cfgList = sheetHomePageCfgService.getUserCfg();
		return "success";		
	}
	
}
