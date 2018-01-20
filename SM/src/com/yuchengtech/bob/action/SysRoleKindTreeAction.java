/**
 * 
 */
package com.yuchengtech.bob.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.yuchengtech.bob.service.SysRoleKindTreeService;

/**
 * @author yaoliang
 *
 */
@Action("/sysRole-kind-tree")
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "sysRole-kind-tree" }) })
		
public class SysRoleKindTreeAction extends BaseAction implements
ModelDriven<Object>, Validateable{
	
	private List kindChild = new ArrayList(); 
	@Autowired
	SysRoleKindTreeService sysRoleKindTreeService;
	
	/**
     *	角色功能授权
	 * 获取所有普通用户角色
	 * @see 修改获取角色的过滤条件。
	 * @author wangxq;
	 * @date 2014-11-7
	 * @return
	 */
	public String index(){
		
//		StringBuffer sb = new StringBuffer("select t.* from ADMIN_AUTH_ROLE t where t.APP_ID = '62' and t.ROLE_TYPE = '"+ SystemUserConstance.NORMAL_MANAGER_ROLE + "' order by t.role_type desc,t.role_code asc");
		StringBuffer sb = new StringBuffer("select t.* from ADMIN_AUTH_ROLE t ");
		sb.append(" where   ");
		sb.append(" t.APP_ID = '62'  and trim(role_code) not in('admin','logicSystemManager')  ");
		sb.append(" order by t.role_type desc,t.role_code asc");
		Map map = sysRoleKindTreeService.productListKinds(sb.toString());
		transToTreeNode((List)map.get("data"));
		return "success";
	}
	
	public void transToTreeNode(List list){
		kindChild = list;
		for(int i=0;i<kindChild.size();i++){			
			Map map = (Map)list.get(i);
			map.put("id", map.get("ID"));
			map.put("text", map.get("ROLE_NAME"));
			map.put("leaf", "true");
		}
		
	}
	
	
	public List getKindChild() {
		return kindChild;
	}

	public void setKindChild(List kindChild) {
		this.kindChild = kindChild;
	}

	public void validate() {
		// TODO Auto-generated method stub
		
	}

	public Object getModel() {
		return kindChild;
		// TODO Auto-generated method stub
	//	return null;
	}
}
