package com.yuchengtech.crm.system.ui.search.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.system.ui.search.model.OcrmFSysUserTile;

@Service
@Transactional(value="postgreTransactionManager")
public class UserTileService {
	
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public void saveBat(List<Map> tiles){
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = auth.getUserId();
		Date date = new Date();
		
		String deleteJql = "DELETE FROM OcrmFSysUserTile n WHERE n.userId=?1";
		Query delete  = em.createQuery(deleteJql);
		delete.setParameter(1, userId);
		delete.executeUpdate();
		for(Map o : tiles){
			OcrmFSysUserTile ofsut = new OcrmFSysUserTile();
			ofsut.setUpdateDate(date);
			ofsut.setUserId(userId);
			try {
				BeanUtils.populate(ofsut, o);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.em.persist(ofsut);
				continue;
			}
		}
	}
}
