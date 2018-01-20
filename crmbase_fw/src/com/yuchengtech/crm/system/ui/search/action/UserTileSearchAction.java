package com.yuchengtech.crm.system.ui.search.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.system.ui.search.service.UserTileService;

@Action("usertile")
public class UserTileSearchAction extends CommonAction {

	private static final long serialVersionUID = 1L;
	@Autowired
	private DataSource dsOracle;
	@Autowired
	private UserTileService userTileService;
	
	@Override
	public void prepare() {
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userTileSql = "SELECT * FROM OCRM_F_SYS_USER_TILE WHERE USER_ID='"+auth.getUserId()+"' ORDER BY POS_X,POS_Y";
		
		SQL = userTileSql;
		datasource = dsOracle;
	}
	
	public DefaultHttpHeaders create() {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			List<Map> tiles = (List<Map>) JSONUtil.deserialize(request.getParameter("condition"));
			userTileService.saveBat(tiles);
			System.out.println("tiles.tile");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultHttpHeaders("success");
	}
}
