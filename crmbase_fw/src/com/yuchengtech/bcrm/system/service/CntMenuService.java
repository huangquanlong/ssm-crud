package com.yuchengtech.bcrm.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.CntMenu;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.constance.SystemConstance;


/**
 * 菜单项管理,20140630,由于级联删除该菜单下的子孙菜单有问题，目前暂时处理为oracle版本解决掉此问题
 * DB2版本会有问题
 * 
 * @author songxs
 * @since 2012-9-23
 * 
 */
@Service
public class CntMenuService extends CommonService {

	public CntMenuService() {
		JPABaseDAO<CntMenu, String> baseDAO = new JPABaseDAO<CntMenu, String>(
				CntMenu.class);
		super.setBaseDAO(baseDAO);
	}

	/**
	 * TODO 未添加逻辑系统 管理员授权信息
	 */
	@SuppressWarnings("unchecked")
	public void deleteMenu(String idStr) {// 菜单项管理删除方法同时删掉菜单表，授权表和常用功能表里面的记录
		if("DB2".equals(SystemConstance.DB_TYPE)) {
			String sql = "delete from OCRM_F_WP_MODULE c where c.module_id in ";
			String sql1 = "delete from AUTH_RES_ATTR_DATA t where t.res_code in ";
			String sql2 = "delete from CNT_MENU where id in ";
			String condition = "";
			String withSQL = "with rpl (id,parent_id) as (" +
								   " select id,parent_id   from cnt_menu   where id = '"+ idStr + "' " +
								   	" union all  " +
								   	" select  child.id,child.parent_id from rpl parent, cnt_menu child where child.parent_id=parent.id" +
								   	" ) select id from rpl";
			List<Object> list = this.em.createNativeQuery(withSQL).getResultList();
			if(list != null) {
				for( Object menuId : list) {
					if (condition.length() ==0) {
						condition += menuId;
					} else {
						condition += "," + menuId;
					}
				}
			}
			sql = sql + "(" + condition + ")";
			sql1 = sql1 + "(" + condition + ")";
			sql2 = sql2 + "(" + condition + ")";
			this.em.createNativeQuery(sql).executeUpdate();
			this.em.createNativeQuery(sql1).executeUpdate();
			this.em.createNativeQuery(sql2).executeUpdate();
			 
		}  else if("ORACLE".equals(SystemConstance.DB_TYPE)){
			String sql = "delete from OCRM_F_WP_MODULE c where c.module_id in (select id from CNT_MENU start with id = '"
				+ idStr + "' connect by prior id = parent_id)";
			String sql1 = "delete from AUTH_RES_ATTR_DATA t where t.res_code in (select id from CNT_MENU start with id = '"
				+ idStr + "' connect by prior id = parent_id)";
			String sql2 = "delete from CNT_MENU where id in (select id from CNT_MENU start with id = '" + idStr
			+ "' connect by prior id = parent_id )";
			this.em.createNativeQuery(sql).executeUpdate();
			this.em.createNativeQuery(sql1).executeUpdate();
			this.em.createNativeQuery(sql2).executeUpdate();
		}else if("MYSQL".equals(SystemConstance.DB_TYPE)){
			String sql = "delete from OCRM_F_WP_MODULE where module_id in ";
			String sql1 = "delete from AUTH_RES_ATTR_DATA where res_code in ";
			String sql2 = "delete from CNT_MENU where id in ";
			idStr.replaceAll(",", "','");
			sql = sql + "('" + idStr + "')";
			sql1 = sql1 + "('" + idStr + "')";
			sql2 = sql2 + "('" + idStr + "')";
			this.em.createNativeQuery(sql).executeUpdate();
			this.em.createNativeQuery(sql1).executeUpdate();
			this.em.createNativeQuery(sql2).executeUpdate();
			 
		}
	}
}
