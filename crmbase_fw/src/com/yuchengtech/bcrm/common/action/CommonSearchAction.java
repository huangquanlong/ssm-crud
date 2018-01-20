package com.yuchengtech.bcrm.common.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.common.CommonContance;
import com.yuchengtech.bcrm.common.service.OrgSearchService;
import com.yuchengtech.bob.action.BaseAction;
import com.yuchengtech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value = "/commsearch", results = { @Result(name = "success", type = "json"), })
public class CommonSearchAction extends BaseAction{
	
	@Autowired
	OrgSearchService oss;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String index() throws Exception{
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgId = auth.getUnitId();
		String flag = (String)this.getJson().get("searchType");
		String condiOrg = (String)this.getJson().get("orgId");
		if(condiOrg!=null){
			orgId = condiOrg;
		}
		String condiRole = (String)this.getJson().get("roleId");
		switch(CommonContance.ORG_SEAR.indexOf(flag)){
			case 0 : setJson(oss.searchSubOrgTree(orgId)); break;
			case 1 : setJson(oss.searchSubOrgs(orgId)); break;
			case 2 : setJson(oss.searchParentOrg(orgId)); break;
			case 3 : setJson(oss.searchPathInOrgTree(orgId)); break;
			case 4 : setJson(oss.searchAllOrgs()); break;
			case 5 : 
				if(condiRole!=null)
					setJson(oss.SearchOrgUsers(orgId,condiRole));
				else
					setJson(oss.SearchOrgUsers(orgId,this.getJson()));
				break;
			case 6 : 
				if(condiRole!=null)
					setJson(oss.SearchSubOrgUsers(orgId, false,condiRole));
				else
					setJson(oss.SearchSubOrgUsers(orgId, false)); 
				break;
			case 7 : 
				if(condiRole!=null)
					setJson(oss.SearchSubOrgUsers(orgId, true,condiRole));
				else	
					setJson(oss.SearchSubOrgUsers(orgId, true)); 
				break;
			case 8 : 
				if(condiRole!=null)
					setJson(oss.searchAllUsers(condiRole));
				else
					setJson(oss.searchAllUsers()); 
				break;
			case 9 : 
				if(condiOrg!=null){
					Map tmp = new HashMap<String,Object>();
					tmp.put("ORGNAME", oss.orgName(condiOrg));
					setJson(tmp);
				} 
				break;
			//指标类型树查询
			//case 10 : setJson(oss.searchSubIndexTypeTree()); break;
			default : setJson(new HashMap<String,Object>());
		}
		return "success";
	}
	//客户经理主协办类型查询
	@SuppressWarnings("unchecked")
	public String isMainType(){
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
		//HttpServletResponse response = (HttpServletResponse)ctx.get(ServletActionContext.HTTP_RESPONSE);
	//	AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//	String roleCode="";
	//	for (int j = 0; j < auth.getRolesInfo().size(); j++) {
		//	Map role = (Map)auth.getRolesInfo().get(j);
		//	roleCode = role.get("ROLE_CODE").toString();
			//if("zhkhjingli".equals(roleCode)||"zhgdkhjl".equals(roleCode)||"zhlckhjl".equals(roleCode)||"zhdtjl".equals(roleCode)){
				String mgrId = request.getParameter("mgrId");
				String custId = request.getParameter("custId");
				Map mainType = oss.isMainType(mgrId, custId);
				String resStr ="[{mainType:"+mainType+"}]";
				this.setJson(mainType);
				//break;
		//	}
	//	}
		return "success";
	}
	
	
}
