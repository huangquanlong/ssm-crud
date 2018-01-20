package com.yuchengtech.crm.homePage.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.vo.AuthUser;

/**
 * 首页个性配置的查询
 * 
 * @author Administrator
 * 
 */

@ParentPackage("json-default")
@Action(value = "/homepage", results = { @Result(name = "success", type = "json") })
public class HomePageAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
//	private HttpServletRequest request;

	// 用户首页数据
	Map<String, Object> userMode = new HashMap<String, Object>();
	// 用户页签数据
	Map<String, Object> userTabPan = new HashMap<String, Object>();
	
    
	// 获取当前用户号
	AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	String userId = auth.getUserId();

	public String index() {
		try {

			// 查询用户页签/样式信息
			StringBuilder tabPanSQL = new StringBuilder("select upan.tabs_num,tabp.tabs_id,tabp.tabs_name, ");
			tabPanSQL.append(" layt.layout_id,layt.layout_name,layt.layout_colnum,layt.layout_rownum ");
			tabPanSQL.append(" from OCRM_F_CI_USERTABPNEL upan ,OCRM_F_CI_TABPNEL tabp, OCRM_F_CI_LAYOUT layt ");
			tabPanSQL.append(" where upan.tabs_id = tabp.tabs_id and upan.layout_id = layt.layout_id ");
			tabPanSQL.append(" and upan.User_Id = '" + userId + "' ");
			tabPanSQL.append(" order by upan.tabs_num asc ");
			
			setUserTabPan(new QueryHelper(tabPanSQL.toString(),ds.getConnection()).getJSON());

			// 查询配置信息
			 StringBuilder userPanModSQL = new
					 StringBuilder(" select homePg.Tabs_Id,homePg.Modild_Id,homePg.Modild_Column,homePg.Modild_Rownum,");
					 userPanModSQL.append(" mol.mod_name,mol.mod_type,mol.mod_action,mol.mod_cm,mol.mod_swffile,mol.MOD_ICON ");
					 userPanModSQL.append(" from OCRM_F_CI_HOMEPAGESET homePg , OCRM_F_CI_MODILE mol ");
					 userPanModSQL.append(" where homePg.Modild_Id = mol.mod_id ");
					 userPanModSQL.append(" and homePg.User_Id = '"+userId + "' ");
					 userPanModSQL.append(" order by homePg.Tabs_Id,homePg.Modild_Rownum,Modild_Column ");
			
			 setUserMode(new QueryHelper(userPanModSQL.toString(),ds.getConnection()).getJSON());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "success";
	}

	public Map<String, Object> getUserMode() {
		return userMode;
	}

	public void setUserMode(Map<String, Object> userMode) {
		this.userMode = userMode;
	}

	public Map<String, Object> getUserTabPan() {
		return userTabPan;
	}

	public void setUserTabPan(Map<String, Object> userTabPan) {
		this.userTabPan = userTabPan;
	}

}
