package com.yuchengtech.sm.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.sm.model.SmAttrClassify;
import com.yuchengtech.sm.model.SmAttrDefinition;

/**
 * @Title: AttrClassifyService
 * @Description: 属性类别Service
 * @date 2017年2月16日
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class AttrClassifyService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// 查询属性类别列表
	@SuppressWarnings("unchecked")
	public List<SmAttrClassify> findAll() {
		Query query = getEntityManager().createQuery("select ac FROM SmAttrClassify ac");
		return query.getResultList();
	}

	// 根据name查询进行新增或者修改
	public boolean save(SmAttrClassify attrClassify) {
		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<SmAttrClassify> list = getByName(attrClassify);
		if (attrClassify.getId() != null) {
			SmAttrClassify sc = find(attrClassify.getId());
			if (list.size() > 0) {
					if (!list.get(0).getName().equals(sc.getName())) {
						throw new BizException(1, 0, "000001", "类别名称重复");
				}
			}
			attrClassify.setCrtTime(sc.getCrtTime());
			attrClassify.setCrtBy(sc.getCrtBy());
			attrClassify.setUpdBy(authUser.getUserId());
			attrClassify.setUpdTime(new Date());
			em.merge(attrClassify);
			return false;
		}
		if (list != null & list.size() > 0) {
			throw new BizException(1, 0, "000001", "类别名称重复");
		}
		attrClassify.setCrtBy(authUser.getUserId());
		attrClassify.setCrtTime(new Date());
		em.persist(attrClassify);
		return true;
	}

	// 删除属性类别
	public void remove(SmAttrClassify ac) {
		StringBuffer searchSql = new StringBuffer("SELECT sd.`classify` FROM"
				+ " sm_attr_classify sc ,sm_attr_definition sd WHERE sc.`id`=sd.`classify`" + " AND sc.`id`=?1");
		Query query = em.createQuery(searchSql.toString());
		query.setParameter(1, ac.getId());
		List<Object> result = query.getResultList();
		if (result != null && result.size() != 0) {
			/** 类别是否有属性定义 */
			throw new BizException(1, 0, "000001", "该类别有属性定义，不能删除");
		}
		em.remove(ac);
	}

	// 批量删除科目
	public void batchRemove(String idStr) {
		StringBuffer searchSql = new StringBuffer("SELECT sd.`classify` FROM sm_attr_classify sc ,sm_attr_definition sd"
											+ " WHERE sc.`id`=sd.`classify` AND sc.`id`= "+idStr);
		Query query = em.createQuery(searchSql.toString());
		System.out.println(searchSql.toString());
		System.out.println(query.getResultList().get(0));
		List<Object> list=query.getResultList();
		if(list!=null &&list.size()>0){
			throw new BizException(1, 0, "000001", "该类别有属性定义，不能删除");
		}
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			SmAttrClassify attrClassify = find(id);
			if (attrClassify != null) {
				em.remove(attrClassify);
			}
		}
	}

	public List<SmAttrClassify> getByName(SmAttrClassify sc) {
		StringBuffer searchSql = new StringBuffer("select s from SmAttrClassify s where s.name=?1 ");
		Query query = em.createQuery(searchSql.toString());
		query.setParameter(1, sc.getName());
		query.setFirstResult(0);
		return (List<SmAttrClassify>) query.getResultList();
	}

	private EntityManager getEntityManager() {
		return em;
	}

	public SmAttrClassify find(long id) {
		return em.find(SmAttrClassify.class, id);
	}

}
