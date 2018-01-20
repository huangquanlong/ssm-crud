package com.yuchengtech.bcrm.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthRole;
import com.yuchengtech.bcrm.system.model.AuthResAttrData;
import com.yuchengtech.bcrm.system.model.AuthResControlAttrData;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;
import com.yuchengtech.crm.exception.BizException;

/**
 * 角色管理Service
 * @author weilh
 * @since 2012-09-25 
 */
@Service
public class RoleManagerService extends CommonService {
	
   public RoleManagerService(){
	   JPABaseDAO<AdminAuthRole, Long>  baseDAO=new JPABaseDAO<AdminAuthRole, Long>(AdminAuthRole.class);  
		super.setBaseDAO(baseDAO);
	   
	}
   
   /**
    * 按照:orgId查询对象
    * @param orgId
    * @param value
    * @return
    */
   public Object findUniqueByRoleCode(String roleCode,Object value){
	  return super.baseDAO.findUniqueByProperty(roleCode,value );
   } 
   
   /**
    * 按照机构Name:orgName查询对象
    * @param orgName
    * @param value
    * @return
    */
   public Object findUniqueByRoleName(String orgName,Object value){
		  return super.baseDAO.findUniqueByProperty(orgName,value );
	   }
   
   /***
    * 保存复制的角色对象，并复制原角色的所有授权菜单和功能控制点
    * @param oldRoleCode 角色复制源
    * @param newRoleCode 新角色编码
    * @param newRoleName 新角色名称
    * @return
    */
   public String copyNewRole(String oldRoleCode,String newRoleCode,String newRoleName) {
	   
	   String jql = "select a from AdminAuthRole a where a.roleCode='"+newRoleCode+"' or a.roleName='"+newRoleName+"'";
		Map<String,Object> values = new HashMap<String,Object>();
		List<AdminAuthRole> attrDataList = baseDAO.findWithNameParm(jql, values);
	   if(attrDataList.size()>0)
	   {
		   throw new BizException(1,0,"100010","所复制的角色名称或角色编码已存在，请重新重新输入！");
	   }
	   AdminAuthRole aarOld = (AdminAuthRole)super.find(Long.parseLong(oldRoleCode));
	   AdminAuthRole aarNew = new AdminAuthRole();
	   aarNew.setRoleCode(newRoleCode);
	   aarNew.setRoleName(newRoleName);
	   aarNew.setAccountId(aarOld.getAccountId());
	   aarNew.setRoleLevel(aarOld.getRoleLevel());
	   aarNew.setRoleType(aarOld.getRoleType());
	   String newRoldId = ((AdminAuthRole)super.save(aarNew)).getId().toString(); // 复制角色获得新角色ID
	   
	   copyOptionMenuSave(oldRoleCode,newRoldId); // 复制原角色菜单授权到新角色
	   copyOptionMenuControlSave(oldRoleCode,newRoldId); // 复制原角色功能点授权到新角色
	   copyRoleFilterAuth(oldRoleCode,newRoldId); // 复制原角色的数据权限到新角色
	   return "success";
   }
   
   /***
    * 保存复制的角色对象，并返回ID
    * @param oldRoleCode 角色复制源
    * @param toRoleCodes 复制到角色IDS
    * @return
    */
   public String copyRoleToRole(String oldRoleCode,String toRoleCodes) {
	   String toRolecodesArr[] = toRoleCodes.split(",");
	   for (int i = 0; i < toRolecodesArr.length; i++) {
		   removeMenu(toRolecodesArr[i]);// 删除角色原有授权菜单信息
		   removeMenuControl(toRolecodesArr[i]);// 删除角色原有控制点信息
		   removeRoleFilterAuth(toRolecodesArr[i]);// 删除角色原有数据授权信息
		   
		   copyOptionMenuSave(oldRoleCode,toRolecodesArr[i]); // 复制原角色菜单授权到新角色
		   copyOptionMenuControlSave(oldRoleCode,toRolecodesArr[i]); // 复制原角色功能点授权到新角色
		   copyRoleFilterAuth(oldRoleCode,toRolecodesArr[i]); // 复制原角色的数据权限到新角色
	   }
	   return "success";
   }
   
   /***
    * 设置角色及功能点授权
    * @param oldRoleCode 旧角色ID
    * @param newRoldId 新角色ID
    */
	public void copyOptionMenuSave(String oldRoleCode,String newRoldId){
		JPABaseDAO<AuthResAttrData, Long> saveOpSetDAO = new JPABaseDAO<AuthResAttrData, Long>(AuthResAttrData.class);
		saveOpSetDAO.setEntityManager(em);
		// 查询角色的功能点授权信息
		String ctlAttrDataQueryJQL = "select a from AuthResAttrData a where a.attrCode='"+oldRoleCode+"'";
		Map<String,Object> values = new HashMap<String,Object>();
		List<AuthResAttrData> attrDataList = saveOpSetDAO.findWithNameParm(ctlAttrDataQueryJQL, values);
		
		// 新增角色功能点授权
		for (int i = 0; i < attrDataList.size(); i++) {
			AuthResAttrData oldAttr = (AuthResAttrData)attrDataList.get(i);	
			AuthResAttrData newAttr = new AuthResAttrData();
			newAttr.setAppId(oldAttr.getAppId());
			newAttr.setAttrId(oldAttr.getAttrId());
			newAttr.setAttrCode(newRoldId);// 新角色ID
			newAttr.setOperateKey(oldAttr.getOperateKey());
			newAttr.setResCode(oldAttr.getResCode());
			newAttr.setResId(oldAttr.getResId());
			newAttr.setType(oldAttr.getType());
			newAttr.setVersion(oldAttr.getVersion());
			saveOpSetDAO.persist(newAttr);
		}
	}
	
   /***
    * 设置角色控制点授权
    * @param oldRoleCode 旧角色ID
    * @param newRoldId 新角色ID
    */
	public void copyOptionMenuControlSave(String oldRoleCode,String newRoldId){
		JPABaseDAO<AuthResControlAttrData, Long> saveOpSetDAO = new JPABaseDAO<AuthResControlAttrData, Long>(AuthResControlAttrData.class);
		saveOpSetDAO.setEntityManager(em);
		// 查询角色的控制点信息
		String ctlAttrDataQueryJQL = "select a from AuthResControlAttrData a where a.attrCode='"+oldRoleCode+"'";
		Map<String,Object> values = new HashMap<String,Object>();
		List<AuthResControlAttrData> attrDataList = saveOpSetDAO.findWithNameParm(ctlAttrDataQueryJQL, values);
		
		// 新增控制点授权
		for (int i = 0; i < attrDataList.size(); i++) {
			AuthResControlAttrData oldAttr = (AuthResControlAttrData)attrDataList.get(i);	
			AuthResControlAttrData newAttr = new AuthResControlAttrData();
			newAttr.setAppId(oldAttr.getAppId());
			newAttr.setAttrId(oldAttr.getAttrId());
			newAttr.setAttrCode(newRoldId);// 新角色ID
			newAttr.setOperateKey(oldAttr.getOperateKey());
			newAttr.setResCode(oldAttr.getResCode());
			newAttr.setResId(oldAttr.getResId());
			newAttr.setType(oldAttr.getType());
			newAttr.setVersion(oldAttr.getVersion());
			saveOpSetDAO.persist(newAttr);
		}
	}
	
	/***
    * 复制角色的数据授权
    * @param oldRoleCode 旧角色ID
    * @param newRoldId 新角色ID
    */
	public void copyRoleFilterAuth(String oldRoleCode,String newRoldId){
		JPABaseDAO<AuthSysFilterAuth, Long> saveOpSetDAO = new JPABaseDAO<AuthSysFilterAuth, Long>(AuthSysFilterAuth.class);
		saveOpSetDAO.setEntityManager(em);
		// 查询角色的控制点信息
		String ctlAttrDataQueryJQL = "select a from AuthSysFilterAuth a where a.roleId='"+oldRoleCode+"'";
		Map<String,Object> values = new HashMap<String,Object>();
		List<AuthSysFilterAuth> attrDataList = saveOpSetDAO.findWithNameParm(ctlAttrDataQueryJQL, values);
		
		// 新增控制点授权
		for (int i = 0; i < attrDataList.size(); i++) {
			AuthSysFilterAuth oldFilterAuth = (AuthSysFilterAuth)attrDataList.get(i);	
			AuthSysFilterAuth newFilterAuth = new AuthSysFilterAuth();
			newFilterAuth.setAuthDate(oldFilterAuth.getAuthDate());
			newFilterAuth.setFilterId(oldFilterAuth.getFilterId());
			newFilterAuth.setRoleId(newRoldId);
			saveOpSetDAO.persist(newFilterAuth);
		}
	}
	
	/***
    * 删除角色的授权菜单信息
    * @param RoleCode 角色ID
    */
	public void removeMenu(String RoleCode){
		JPABaseDAO<AuthResAttrData, Long> saveOpSetDAO = new JPABaseDAO<AuthResAttrData, Long>(AuthResAttrData.class);
		saveOpSetDAO.setEntityManager(em);
		// 查询角色的功能点授权信息
		String ctlAttrDataQueryJQL = "select a from AuthResAttrData a where a.attrCode='"+RoleCode+"'";
		Map<String,Object> values = new HashMap<String,Object>();
		List<AuthResAttrData> attrDataList = saveOpSetDAO.findWithNameParm(ctlAttrDataQueryJQL, values);
		
		// 删除角色的授权菜单信息
		for (int i = 0; i < attrDataList.size(); i++) {
			AuthResAttrData attr = (AuthResAttrData)attrDataList.get(i);	
			saveOpSetDAO.remove(attr);
		}
	}
	
	/***
    * 删除角色的控制点信息
    * @param RoleCode 角色ID
    */
	public void removeMenuControl(String RoleCode){
		JPABaseDAO<AuthResControlAttrData, Long> saveOpSetDAO = new JPABaseDAO<AuthResControlAttrData, Long>(AuthResControlAttrData.class);
		saveOpSetDAO.setEntityManager(em);
		// 查询角色的控制点信息
		String ctlAttrDataQueryJQL = "select a from AuthResControlAttrData a where a.attrCode='"+RoleCode+"'";
		Map<String,Object> values = new HashMap<String,Object>();
		List<AuthResControlAttrData> controlAttrList = saveOpSetDAO.findWithNameParm(ctlAttrDataQueryJQL, values);
		
		// 删除角色的控制点信息
		for (int i = 0; i < controlAttrList.size(); i++) {
			AuthResControlAttrData controlAttr = (AuthResControlAttrData)controlAttrList.get(i);	
			saveOpSetDAO.remove(controlAttr);
		}
	}
	
	/***
    * 删除角色的数据授权
    * @param RoleCode 角色ID
    */
	public void removeRoleFilterAuth(String RoleCode){
		JPABaseDAO<AuthSysFilterAuth, Long> saveOpSetDAO = new JPABaseDAO<AuthSysFilterAuth, Long>(AuthSysFilterAuth.class);
		saveOpSetDAO.setEntityManager(em);
		// 查询角色的控制点信息
		String ctlAttrDataQueryJQL = "select a from AuthSysFilterAuth a where a.roleId='"+RoleCode+"'";
		Map<String,Object> values = new HashMap<String,Object>();
		List<AuthSysFilterAuth> controlAttrList = saveOpSetDAO.findWithNameParm(ctlAttrDataQueryJQL, values);
		
		// 删除角色的控制点信息
		for (int i = 0; i < controlAttrList.size(); i++) {
			AuthSysFilterAuth controlAttr = (AuthSysFilterAuth)controlAttrList.get(i);	
			saveOpSetDAO.remove(controlAttr);
		}
	}
}
