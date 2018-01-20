package com.yuchengtech.bob.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.model.LookupMapping;


@Service
@Transactional(value="postgreTransactionManager")
public class LookupMappingService{
	
	private EntityManager em;	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// 查询数据字典列表
	@SuppressWarnings("unchecked")
	public List<LookupMapping> findAll() {
		Query query = getEntityManager().createQuery("select lm FROM LookupMapping lm");
        return query.getResultList();
	}
	
	// 根据ID是否为空进行新增或者修改数据字典
	public boolean save(LookupMapping lookupMapping) {
		
		Long ID = lookupMapping.getId();
		if (ID == null) {
			//新增
			em.persist(lookupMapping);
			return true;
		} else {
			//修改
			em.merge(lookupMapping);
			return true;
		}
	}

	// 删除数据字典
    public void remove(long id) {
    		LookupMapping lookupMapping = find(id);
        	if (lookupMapping != null) {
            em.remove(lookupMapping);
		}
    }
    
	//批量删除科目
	public void batchRemove(String idStr) {
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			LookupMapping lookupMapping = find(id);
			if (lookupMapping != null) {
				em.remove(lookupMapping);
			}
		}
	}
    
	private EntityManager getEntityManager() {
		return em;
	}

	public LookupMapping find(long id) {
		return em.find(LookupMapping.class, id);
	}

}
