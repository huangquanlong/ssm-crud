package com.yuchengtech.bcrm.common.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.action.BaseAction;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="/indexinit", results={
    @Result(name="success", type="json"),
})
public class IndexInitAction extends BaseAction{
	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource dsOracle;

	public String index() throws Exception{
		
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
		Map<String,Object> menus = auth.getGrant();
		
		StringBuffer sb = new StringBuffer();
		if(menus == null) {
			sb.append(" 1=2 ");
		} else {
			for(String key : menus.keySet()){
				if(sb.toString().equals("")){
					sb.append(key);
				}else{
					sb.append(","+key);
				}
			}
			sb.insert(0," C.ID IN (");
			sb.append(" )");
			//移动平台设备类型条件
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
			if(null != request.getParameter("deviceType")) {
				sb.append(" AND C.ISMOBILE=1");
			}
		}
		sb.insert(0, "SELECT C.*,F.*,E.*,CF.COMSITS FROM CNT_MENU C " +
				"LEFT JOIN FW_FUNCTION F ON C.MOD_FUNC_ID = F.ID " +
				"LEFT JOIN OCRM_F_SYS_FW_FUNCTION_EXT E ON C.MOD_FUNC_ID = E.MODULE_ID "+
				"LEFT JOIN OCRM_COMPOSIT_FUNCTION CF ON C.ID = CF.MENU_ID "+
				"WHERE ");
		sb.append(" ORDER BY C.ORDER_ ASC ");
		
		QueryHelper indexInit = new QueryHelper(sb.toString(), dsOracle.getConnection());
		this.setJson(indexInit.getJSON());
		return "success";
	}
	
	
}
