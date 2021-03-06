package com.yuchengtech.sm.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.sm.model.SmTypeDefinition;

/**
 * @Title: AttrDefibitionService
 * @Description: 私募类型定义Service
 * @date 2017年2月16日
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class TypeDefinitionService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// 查询定义类别列表
	@SuppressWarnings("unchecked")
	public List<SmTypeDefinition> findAll() {
		Query query = em.createQuery("select sd FROM SmTypeDefinition sd");
		return query.getResultList();
	}

	// 根据ID是否为空进行新增或者修改属性定义
	public boolean save(SmTypeDefinition typeDefinition) {
		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<SmTypeDefinition> list = getByName(typeDefinition);
		if (typeDefinition.getId() != null) {
			SmTypeDefinition sc = find(typeDefinition.getId());
			if (list.size() > 0) {
				for (SmTypeDefinition li : list) {
					if (!li.getName().equals(sc.getName()) || !li.getIdentify().equals(sc.getIdentify())) {
						if (li.getName().equals(typeDefinition.getName())
								&& !li.getIdentify().equals(typeDefinition.getIdentify())) {
							throw new BizException(1, 0, "000001", "属性名称重复");
						} else if (!li.getName().equals(typeDefinition.getName())
								&& li.getIdentify().equals(typeDefinition.getIdentify())) {
							throw new BizException(1, 0, "000001", "逻辑标识重复");
						}
						throw new BizException(1, 0, "000001", "属性名称与逻辑标识都重复");
					}
				}
			}
			typeDefinition.setCrtTime(sc.getCrtTime());
			typeDefinition.setCrtBy(sc.getCrtBy());
			typeDefinition.setUpdBy(authUser.getUserId());
			typeDefinition.setUpdTime(new Date());
			em.merge(typeDefinition);
			return false;
		}
		if (list != null & list.size() > 0) {
			if (list.get(0).getName().equals(typeDefinition.getName())) {
				throw new BizException(1, 0, "000001", "属性名称重复");
			} else {
				throw new BizException(1, 0, "000001", "逻辑标识重复");
			}
		}
		typeDefinition.setCrtBy(authUser.getUserId());
		typeDefinition.setCrtTime(new Date());
		em.persist(typeDefinition);
		return true;
	}

	// 批量删除科目
	public void batchRemove(String idStr) {
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			SmTypeDefinition SmTypeDefinition = find(id);
			if (SmTypeDefinition != null) {
				em.remove(SmTypeDefinition);
			}
		}
	}

	// 按name查找对象
	public List<SmTypeDefinition> getByName(SmTypeDefinition sc) {
		StringBuffer searchSql = new StringBuffer(
				"select sd from SmTypeDefinition sd where sd.name=?1 or sd.identify=?2 ");
		Query query = em.createQuery(searchSql.toString());
		query.setParameter(1, sc.getName());
		query.setParameter(2, sc.getIdentify());
		query.setFirstResult(0);
		return (List<SmTypeDefinition>) query.getResultList();
	}

	public SmTypeDefinition find(long id) {
		return em.find(SmTypeDefinition.class, id);
	}

}
