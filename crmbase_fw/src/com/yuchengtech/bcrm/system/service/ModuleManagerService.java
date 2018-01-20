package com.yuchengtech.bcrm.system.service;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.FwFunction;
import com.yuchengtech.bcrm.system.model.FwModule;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.exception.BizException;

/**
 * @describe 模块管理服务
 * @author GUOCHI
 * @since 2012-10-09
 */
@Service
public class ModuleManagerService extends CommonService {
	public ModuleManagerService() {
		JPABaseDAO<FwModule, Long> baseDAO = new JPABaseDAO<FwModule, Long>(
				FwModule.class);
		super.setBaseDAO(baseDAO);
	}

	/**
	 * 保存：包括新增和修改
	 */
	public void save(FwModule ws) {
	    ws.setVersion(0);
	    ws.setCrtDate(new Date());
		if (ws.getId() == null) {
			em.persist(ws);
		} else
			em.merge(ws);
	}

	/**
	 * 删除方法
	 * @param idStr FW_FUNCTION表中功能点的ID
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void remove(String idStr) throws ParseException {
		String sql = "select c from FwFunction c where  c.moduleId='" + idStr + "'";
		Query q = em.createQuery(sql.toString());
		List<FwFunction> list = q.getResultList();
		if (list.size() == 0) {
			em.remove(em.find(FwModule.class, Long.valueOf(idStr)));
			return;
		} else {
			throw new BizException(1, 0, "0001", "该模块下有有效的子功能点，不能删除！");
		}
	}
}
