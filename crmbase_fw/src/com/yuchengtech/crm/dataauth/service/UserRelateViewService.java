package com.yuchengtech.crm.dataauth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.dataauth.model.UserViewRelation;
/**
 * 客户与客户群关系
 * @author Administrator
 */
@Service
@Transactional(value="postgreTransactionManager")
public class UserRelateViewService {
	@PersistenceContext
	private EntityManager em;

	
	public void setEntityManager(EntityManager em) {
	        this.em = em;
	    }
	
	@SuppressWarnings("unchecked")
	//无查询条件
	public List <UserViewRelation> query(String roleId) { 
		 StringBuffer querysql=new StringBuffer();
		 querysql.append("select c from UserViewRelation c where  c.roleId=?1");
		 Query q = em.createQuery(querysql.toString()); 
		 q.setParameter(1, roleId);
	     q.setFirstResult(0);
		 q.setMaxResults(100000);  
	     List<UserViewRelation> list= q.getResultList(); 
		 return list;
	} 
	
	public void batchSave(String[] menuAddCodeStr,String[] menuDelCodeStr,String userId){
		
		if(menuAddCodeStr!=null){
			for(int i =0;i<menuAddCodeStr.length;i++){
				UserViewRelation model=new UserViewRelation();
	     	    model.setRoleId(userId);
	     	    model.setViewId(Long.parseLong(menuAddCodeStr[i]));
			    em.persist(model);
			    }
		}

		Map<String,Object> values=new HashMap<String,Object>();
		JPABaseDAO<UserViewRelation, Long> baseDAO1 = new JPABaseDAO<UserViewRelation, Long>(UserViewRelation.class);
		baseDAO1.setEntityManager(em);
		if(menuDelCodeStr!=null){
			for(int a=0; a<menuDelCodeStr.length;a++){
				Long viewId =Long.valueOf(menuDelCodeStr[a]);
				String attrDataDelJQL = "delete from UserViewRelation a where a.roleId='"+userId+"' and a.viewId = '"+viewId+"'";
				baseDAO1.batchExecuteWithNameParam(attrDataDelJQL, values);
			}
		}
		
	
		}
		


}