package com.yuchengtech.bcrm.workplat.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.workplat.model.WorkingplatformRemindInfo;

/**
 * @describe 自动提醒记录服务
 * @author WillJoe
 *
 */
@Service
@Transactional
public class WorkingplatformRemindInfoService {
	
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em){
		this.em = em;
	}

	/**
	 * 保存：包括新增和修改
	 * @param wri
	 */
	public void save(WorkingplatformRemindInfo wri){
		if(wri.getRemindId()==null){
			em.persist(wri);
		}else {
		    
		    wri.setIsRead(true);
		    
		    em.merge(wri);};
	}
	
	/**
	 * 移除记录
	 * @param id
	 */
	public void remove(Long id){
		em.remove(em.find(WorkingplatformRemindInfo.class, id));
	}
	
	/**
	 * 查看记录
	 * @param id
	 * @return
	 */
	public WorkingplatformRemindInfo find(Long id){
		return em.find(WorkingplatformRemindInfo.class, id);
	}
	
	/**
	 * 查询所有记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WorkingplatformRemindInfo> findAll(){
		String wriFindAll = "Select wri from WorkingplatformRemindInfo wri";
		Query wriQuery = em.createQuery(wriFindAll);
		return wriQuery.getResultList();
	}
}
