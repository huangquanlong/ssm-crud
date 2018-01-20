package com.yuchengtech.bcrm.workplat.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.workplat.model.WorkingplatformNoticeOrganizer;

/**
 * @describe 公告接收机构服务
 * @author WillJoe
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class NoticeOrganizetionService {
	
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	/**
	 * 保存：包括新增和修改
	 * @param wno
	 */
	public void save(WorkingplatformNoticeOrganizer wno){
		if(wno.getReceiveId()==null){
			em.persist(wno);
		}else em.merge(wno);
	}
	
	/**
	 * 移除记录
	 * @param id
	 */
	public void remove(Long id){
		em.remove(em.find(WorkingplatformNoticeOrganizer.class, id));
	}
	
	/**
	 * 查看记录
	 * @param id
	 * @return
	 */
	public WorkingplatformNoticeOrganizer find(Long id){
		return em.find(WorkingplatformNoticeOrganizer.class, id);
	}
	
	/**
	 * 查询所有记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WorkingplatformNoticeOrganizer> findAll(){
		String findWno = "select wno from WorkingplatformNoticeOrganizer wno";
		Query wnoQuery = em.createQuery(findWno);
		return wnoQuery.getResultList();
	}
}
