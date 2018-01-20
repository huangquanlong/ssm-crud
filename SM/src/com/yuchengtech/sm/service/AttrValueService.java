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
import com.yuchengtech.sm.model.SmAttrValue;

/**
 * @Title: AttrValueService
 * @Description: 属性值Service
 * @date 2017年2月27日
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class AttrValueService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// 查询属性类别列表
	@SuppressWarnings("unchecked")
	public List<SmAttrValue> findAll() {
		Query query = getEntityManager().createQuery("select sv FROM SmAttrValue sv");
		return query.getResultList();
	}

	// 根据name查询进行新增或者修改
	public boolean save(SmAttrValue attrValue) {
		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (attrValue.getId() != null) {
			SmAttrValue sc = find(attrValue.getId());
			attrValue.setCrtTime(sc.getCrtTime());
			attrValue.setCrtBy(sc.getCrtBy());
			attrValue.setUpdBy(authUser.getUserId());
			attrValue.setUpdTime(new Date());
			em.merge(attrValue);
			return false;
		}
		attrValue.setCrtBy(authUser.getUserId());
		attrValue.setCrtTime(new Date());
		em.persist(attrValue);
		return true;
	}



	// 批量删除科目
	public void batchRemove(String idStr) {
/*		StringBuffer searchSql = new StringBuffer("SELECT sd.`classify` FROM sm_attr_classify sc ,sm_attr_definition sd"
											+ " WHERE sc.`id`=sd.`classify` AND sc.`id`= "+idStr);
		Query query = em.createQuery(searchSql.toString());
		List<Object> list=query.getResultList();
		if(list!=null &&list.size()>0){
			throw new BizException(1, 0, "000001", "该类别有属性定义，不能删除");
		}*/
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			SmAttrValue attrValue = find(id);
			if (attrValue != null) {
				em.remove(attrValue);
			}
		}
	}

	/*public List<SmAttrClassify> getByName(SmAttrValue sc) {
		StringBuffer searchSql = new StringBuffer("select s from SmAttrValue s where s.name=?1 ");
		Query query = em.createQuery(searchSql.toString());
		query.setParameter(1, sc.getName());
		query.setFirstResult(0);
		return (List<SmAttrClassify>) query.getResultList();
	}
*/
	private EntityManager getEntityManager() {
		return em;
	}

	public SmAttrValue find(long id) {
		return em.find(SmAttrValue.class, id);
	}

}
