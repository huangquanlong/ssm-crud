package com.yuchengtech.bcrm.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthAccountRole;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;
/**
 * 类型添加属性
 * @since 2017-2-23
 * 
 */
@Service
public class RoleAccountGrantService extends CommonService{
	
	public RoleAccountGrantService(){
		JPABaseDAO<AdminAuthAccountRole, String>  baseDAO=new JPABaseDAO<AdminAuthAccountRole, String>(AdminAuthAccountRole.class);  
		super.setBaseDAO(baseDAO);
	}
    
	/***
	 * 取消属性
	 * @param idStr 已添加的属性ID
	 */
	@SuppressWarnings("unchecked")
	public void deleteMenu(String idStr){
		JSONArray jarray = JSONArray.fromObject(idStr);
		   String idstr1="" ;
		   for (int i = 0; i < jarray.size(); i++){
			   idstr1 = idstr1 + String.valueOf(jarray.get(i));
			   if(i != jarray.size()-1){
				   idstr1 += ",";
			   }
		   }
		   
		String jql="delete from AdminAuthAccountRole c where c.id in ("+idstr1+")";
		Map<String,Object> values=new HashMap<String,Object>();
		this.batchUpdateByName(jql, values);
		return ;
	}
	/**
	 * 新增角色授权
	 * @param roleId  角色ID
	 * @param accountIds 用户主键 ID
	 */
	public void save(String roleId,String accountIds){
	     
		String[] strArray = accountIds.split(","); 
		String jql = "select c from AdminAuthAccountRole c where c.accountId IN ("+accountIds+") and c.roleId = "+roleId+" ";
		Query q = em.createQuery(jql.toString());
		List<AdminAuthAccountRole> list = q.getResultList();
		if(list.size()==0){
			for(int i=0;i<strArray.length;i++){
				AdminAuthAccountRole ws = new AdminAuthAccountRole();
				ws.setRoleId(Long.valueOf(roleId));
				ws.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
				ws.setAccountId(Long.valueOf(strArray[i]));
				this.em.persist(ws);
			}
			return;
		}else {
			throw new BizException(1,0,"100010","所选的用户存在已经被添加的用户，请重新选择！");
		}
	}
	
	
	
}
