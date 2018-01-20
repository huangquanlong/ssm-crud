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
import com.yuchengtech.sm.model.SmAttrDefinition;

/**
 * @Title: AttrDefibitionService
 * @Description: 属性定义Service
 * @date 2017年2月16日
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class AttrDefinitionService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// 查询定义类别列表
	@SuppressWarnings("unchecked")
	public List<SmAttrDefinition> findAll() {
		Query query = em.createQuery("select sd FROM SmAttrDefinition sd");
		return query.getResultList();
	}

	// 根据ID是否为空进行新增或者修改属性定义
	public boolean save(SmAttrDefinition attrDefinition) {
		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<SmAttrDefinition> list = getByNameOrIdentify(attrDefinition);
		if (attrDefinition.getId() != null) {
			SmAttrDefinition sc = find(attrDefinition.getId());
			if (list.size() > 0) {
				for (SmAttrDefinition li : list) {
					if (!li.getName().equals(sc.getName()) || !li.getIdentify().equals(sc.getIdentify())) {
						if (li.getName().equals(attrDefinition.getName())
								&& !li.getIdentify().equals(attrDefinition.getIdentify())) {
							throw new BizException(1, 0, "000001", "属性名称重复");
						} else if (!li.getName().equals(attrDefinition.getName())
								&& li.getIdentify().equals(attrDefinition.getIdentify())) {
							throw new BizException(1, 0, "000001", "逻辑标识重复");
						}
						throw new BizException(1, 0, "000001", "属性名称与逻辑标识都重复");
					}
				}
			}
			attrDefinition.setCrtTime(sc.getCrtTime());
			attrDefinition.setCrtBy(sc.getCrtBy());
			attrDefinition.setUpdBy(authUser.getUserId());
			attrDefinition.setUpdTime(new Date());
			em.merge(attrDefinition);
			return false;
		}
		if (list != null & list.size() > 0) {
			if (list.get(0).getName().equals(attrDefinition.getName())) {
				throw new BizException(1, 0, "000001", "属性名称重复");
			} else {
				throw new BizException(1, 0, "000001", "逻辑标识重复");
			}

		}
		attrDefinition.setCrtBy(authUser.getUserId());
		attrDefinition.setCrtTime(new Date());
		em.persist(attrDefinition);
		return true;
	}

	// 删除属性定义
	public void remove(SmAttrDefinition sd) {
		StringBuffer searchSql = new StringBuffer("SELECT sv.`value` " + "FROM sm_attr_definition sd ,sm_attr_value sv "
				+ "WHERE sd.`id`=sv.`attr_def_id` AND sd.`id`=?1");
		Query query = em.createQuery(searchSql.toString());
		query.setParameter(1, sd.getId());
		query.setFirstResult(0);
		List<SmAttrDefinition> result = (List<SmAttrDefinition>) query.getResultList();
		if (result != null && result.size() > 1) {
			// **该属性是否存有值*//*
			throw new BizException(1, 0, "000001", "该属性存有值，不能删除");
		}
		SmAttrDefinition smAttrDefinition = find(sd.getId());
		if (smAttrDefinition != null) {
			em.remove(smAttrDefinition);
		}
	}

	// 批量删除科目
	public void batchRemove(String idStr) {
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			SmAttrDefinition smAttrDefinition = find(id);
			if (smAttrDefinition != null) {
				em.remove(smAttrDefinition);
			}
		}
	}

	// 按name查找对象
	public List<SmAttrDefinition> getByNameOrIdentify(SmAttrDefinition sc) {
		StringBuffer searchSql = new StringBuffer(
				"select sd from SmAttrDefinition sd where sd.name=?1 or sd.identify=?2 ");
		Query query = em.createQuery(searchSql.toString());
		query.setParameter(1, sc.getName());
		query.setParameter(2, sc.getIdentify());
		query.setFirstResult(0);
		return (List<SmAttrDefinition>) query.getResultList();
	}

	public SmAttrDefinition find(long id) {
		return em.find(SmAttrDefinition.class, id);
	}

	private EntityManager getEntityManager() {
		return em;
	}
}
