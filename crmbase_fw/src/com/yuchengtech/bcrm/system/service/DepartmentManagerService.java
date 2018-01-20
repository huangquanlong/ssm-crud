package com.yuchengtech.bcrm.system.service;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthDpt;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.exception.BizException;
/**
 * 部门管理
 * @author changzh@yuchengtech.com
 * @since 2012-11-19
 */
@Service
public class DepartmentManagerService extends CommonService{
	
	 public DepartmentManagerService(){
		   JPABaseDAO<AdminAuthDpt, Long>  baseDAO = new JPABaseDAO<AdminAuthDpt, Long>(AdminAuthDpt.class);  
		   super.setBaseDAO(baseDAO);
	 }
	 
	 public Object saveData(Object model){
		 try {
			 AdminAuthDpt d=(AdminAuthDpt)model;
			 StringBuffer searchSql = new StringBuffer("select d from AdminAuthDpt d where d.dptId =?1 and d.belongOrgId =?2 ");
			 if (d.getId() != null) {
				 searchSql.append(" and d.id <>" + d.getId());
			 }
			 	
			Query query = em.createQuery(searchSql.toString());
			query.setParameter(1, d.getDptId());
			query.setParameter(2, d.getBelongOrgId());
			query.setFirstResult(0);
				
			List<AdminAuthDpt> result = (List<AdminAuthDpt>) query.getResultList();
			if (result != null)	 {
				/**部门编号是否重复*/
				if (result.size() > 0) {
					throw new BizException(1,0,"0001", "部门编号重复,请重新输入");
				} 
			}
				 
			return baseDAO.save(d);
			 
		 } catch (Exception e) {			 
			 throw new BizException(1,0,"0001",e.getMessage());
		 }		 
	 }
}
