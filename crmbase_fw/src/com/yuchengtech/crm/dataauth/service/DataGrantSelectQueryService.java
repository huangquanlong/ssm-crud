package com.yuchengtech.crm.dataauth.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.crm.dataauth.model.AuthSysFilter;

@Service
@Transactional(value="postgreTransactionManager")
public class DataGrantSelectQueryService{
	
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public List<AuthSysFilter> findAll(String b) {
		String a = "select mp FROM AuthSysFilter mp where mp.className=:tt";
		Query query = getEntityManager().createQuery(a);
		query.setParameter("tt", b);
        return query.getResultList();
	}
	
	private EntityManager getEntityManager() {
		return em;
	}
	
	public AuthSysFilter find(long id) {
		return em.find(AuthSysFilter.class, id);
	}

}
