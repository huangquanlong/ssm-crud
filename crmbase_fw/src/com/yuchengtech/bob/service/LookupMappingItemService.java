package com.yuchengtech.bob.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.model.LookupMappingItem;


@Service
@Transactional(value="postgreTransactionManager")
public class LookupMappingItemService{
	
	private EntityManager em;	
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// 查询数据字典列表
	@SuppressWarnings("unchecked")
	public List<LookupMappingItem> findAll() {
		Query query = getEntityManager().createQuery("select lmi FROM LookupMappingItem lmi");
        return query.getResultList();
	}

	// 根据ID是否为空进行新增或者修改数据字典类型
	public void save(LookupMappingItem lookupMappingItem) {
		
		Long ID = lookupMappingItem.getId();
		
		if (ID == null) {
			//新增
			em.persist(lookupMappingItem);
		} else {
			//修改
			em.merge(lookupMappingItem);
		}
	}

	// 删除数据字典类型
    public void remove(long id) {
    		LookupMappingItem lookupMappingItem = find(id);
        	if (lookupMappingItem != null) {
            em.remove(lookupMappingItem);
		}
    }
    
	//批量删除科目
	public void batchRemove(String idStr) {
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			LookupMappingItem lookupMappingItem = find(id);
			if (lookupMappingItem != null) {
				em.remove(lookupMappingItem);
			}
		}
	}
    
	private EntityManager getEntityManager() {
		return em;
	}

	public LookupMappingItem find(long id) {
		return em.find(LookupMappingItem.class, id);
	}

}
