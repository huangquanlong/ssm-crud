package com.yuchengtech.bcrm.system.service;

import java.util.List;

import javax.persistence.Query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.system.model.FwSysProp;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * 页面操作设置的service
 * @author songxs
 * @since 2013-3-5
 */
@Service
@Transactional(value="postgreTransactionManager")
public class PageSetManageService extends CommonService {
	public PageSetManageService() {
		JPABaseDAO<FwSysProp, Long> baseDAO = new JPABaseDAO<FwSysProp, Long>(
				FwSysProp.class);
		super.setBaseDAO(baseDAO);
	}
	/**
	 * 保存方法updateData的实现
	 * @param jarray    ------ 由Action传过来的参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String updateData(JSONArray jarray){
		String jql = "select c from FwSysProp c where c.propName like ?1 ";//查询出表中所有的页面操作参数
		Query sql = em.createQuery(jql.toString());
		sql.setParameter(1, "%"+"indexCfg"+"%");//赋值，查询propName中包含'indexCfg'的数据
		List<FwSysProp> list = sql.getResultList();
		for(int i = 0;i<jarray.size();i++){//循环检索List,根据前台传来的数组的indexCfgName的值，然后进行数据修改
			JSONObject wa = (JSONObject)jarray.get(i);
			for(FwSysProp fsp : list){
				if(fsp.getPropName().equals((String)wa.get("indexCfgName"))){
					fsp.setPropValue(wa.get("value").toString());
					this.em.merge(fsp);
				}
			}
		}		
		return "success";
	}
}