package com.yuchengtech.crm.dataauth.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.crm.dataauth.model.AuthSysFilter;

/**
 * @describe 数据权限过滤器加载器，仅在服务器启动时候加载。
 * @author WILLJOE
 */
@Service
@Transactional(value="postgreTransactionManager")
public class FilterLoader {
	
	private EntityManager em ;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public List<AuthSysFilter> LoadFilters(){
		String JQL = "SELECT A FROM AuthSysFilter A";
		return (List<AuthSysFilter>)em.createQuery(JQL).getResultList();
	}
	
}
