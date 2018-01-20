package com.yuchengtech.crm.dataauth.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;

/**
 * 日程服务
 * @author WillJoe
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class AuthSysFilterAuthService {
	
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em){
		this.em = em;
	}
	public List <AuthSysFilterAuth> query(String filterID,String roleID) { 
		 StringBuffer querysql=new StringBuffer();
		 querysql.append("select c from AuthSysFilterAuth c where  c.filterId=?1 and c.roleId=?2");
		 Query q = em.createQuery(querysql.toString()); 
		 q.setParameter(1, filterID);
		 q.setParameter(2, roleID);
	     q.setFirstResult(0);
		 q.setMaxResults(10);  
	     List<AuthSysFilterAuth> list= q.getResultList(); 
		 return list;
	} 
	/**
	 * 保存：包括新增和修改
	 * @param ws
	 */
	public void save(String filterID,String roleID,Long id,AuthSysFilterAuth ws){
		
		//List<AuthSysFilterAuth> list=query(filterID,roleID);
		//AuthSysFilterAuth ws2=	list.get(0);
		
		////system.out.printlnln(marketTeamName+"12312312******************************************");
		if(id==null){
			//AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//String currenUserId = auth.getUserId();
			//int tempID = Integer.parseInt(b);
			ws.setFilterId(filterID);
			ws.setRoleId(roleID);
			Date date = new Date();
			ws.setAuthDate(date);
			em.persist(ws);
		}else {
			//AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//String currenUserId = auth.getUserId();
			//int tempID = Integer.parseInt(b);
			ws.setFilterId(filterID);
			ws.setRoleId(roleID);
			ws.setId(id);
			Date date = new Date();
			ws.setAuthDate(date);
			em.merge(ws);
			};
	}
	
	/**
	 * 移除记录
	 * @param id
	 */
	public void remove(Long id){
//		em.remove(em.find(MarketTeam.class, id));
		AuthSysFilterAuth ws2 = em.find(AuthSysFilterAuth.class, id);
		em.remove(ws2);
//		if(ws2!=null){
////			ws2.setTeamstatus("注销");
//		}
	}
	public void delete(String filterID,String roleID){
//		em.remove(em.find(MarketTeam.class, id));
		AuthSysFilterAuth ws =new AuthSysFilterAuth();
		ws.setFilterId(filterID);
		ws.setRoleId(roleID);
		List<AuthSysFilterAuth> list=query(filterID,roleID);
		AuthSysFilterAuth ws2=	list.get(0);
		em.remove(ws2);
	}
	/**
	 * 查看记录
	 * @param id
	 * @return
	 */
	public AuthSysFilterAuth find(Long id){
		return em.find(AuthSysFilterAuth.class, id);
	}
	
	/**
	 * 查询所有记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthSysFilterAuth> findAll(){
		String wsFindAll = "select ws from MarketTeam ws";
		Query wsQuery = em.createQuery(wsFindAll);
		return wsQuery.getResultList();
	}
}
