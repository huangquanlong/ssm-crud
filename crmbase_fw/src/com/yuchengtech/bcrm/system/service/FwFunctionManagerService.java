package com.yuchengtech.bcrm.system.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AuthResController;
import com.yuchengtech.bcrm.system.model.FwFunction;
import com.yuchengtech.bcrm.system.model.FwFunctionExt;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * @describe 模块管理服务
 * @author GUOCHI
 * @since 2012-10-12
 */
@Service
public class FwFunctionManagerService extends CommonService {
	public FwFunctionManagerService() {
		JPABaseDAO<FwFunction, Long> baseDAO = new JPABaseDAO<FwFunction, Long>(
				FwFunction.class);
		super.setBaseDAO(baseDAO);

	}
	    @Autowired
	    private FwFunctionExtManagerService wis;
	// 删除一条记录及其连带按钮控制

	@SuppressWarnings("unchecked")
	public void remove(Object id) {
		Object obj = find(id);
		if (obj != null) {
			baseDAO.remove(obj);
			JPABaseDAO<AuthResController, Long> baseDAO2 = new JPABaseDAO<AuthResController, Long>(
					AuthResController.class);
			baseDAO2.setEntityManager(em);
			if (id != null && id != "") {
				Map<String, Object> values = new HashMap<String, Object>();
				String attrDataDelJQL = "delete from AuthResController a where a.fwFunId='"
						+ id + "'";
				baseDAO2.batchExecuteWithNameParam(attrDataDelJQL, values);
			}
			em.createNativeQuery("DELETE FROM OCRM_F_SYS_FW_FUNCTION_EXT WHERE MODULE_ID = '"+id+"'").executeUpdate();
		}
	}
    
    
	/**s
	 * 保存：包括新增和修改
	 * @param ws
	 */
	public void saveWithIconView(FwFunction ws,FwFunctionExt wi1,String ifAdd) {
//		if(Integer.parseInt(ifAdd)!=1 ){
//			wis.deleteWithIconView(ws.getId(),wi1);
//		}
		if (ws.getId() == null) {
			ws.setVersion(0);
			em.persist(ws);
			em.merge(ws);
		} else {
			ws.setVersion(0);
			em.merge(ws);
		}
		wis.deleteWithIconView(ws.getId(),wi1,ifAdd);

	}

	/**s
	 * 保存：包括新增和修改
	 * @param ws
	 */
	public void saveAlone(FwFunction ws) {
		wis.delete(ws.getId());
		if (ws.getId() == null) {
			ws.setVersion(0);
			em.persist(ws);
			em.merge(ws);
		} else {
			ws.setVersion(0);
			em.merge(ws);
		}
	}
	/**s
	 * 保存：包括新增和修改
	 * @param ws
	 */
	public void save(FwFunction ws,FwFunctionExt ws1) {
		if (ws.getId() == null) {
			ws.setVersion(0);
			em.persist(ws);
			em.merge(ws);
			ws1.setModuleId(ws.getId());
		} else {
			ws.setVersion(0);
			em.merge(ws);
		}
		
		wis.save(ws1);
	}
}
