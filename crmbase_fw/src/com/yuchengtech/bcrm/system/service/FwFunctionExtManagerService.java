package com.yuchengtech.bcrm.system.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.FwFunctionExt;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * @describe 模块管理服务
 * @author GUOCHI
 * @since 2012-10-12
 */
@Service
public class FwFunctionExtManagerService extends CommonService {
	public FwFunctionExtManagerService() {
		JPABaseDAO<FwFunctionExt, Long> baseDAO = new JPABaseDAO<FwFunctionExt, Long>(
				FwFunctionExt.class);
		super.setBaseDAO(baseDAO);

	}
	
//	  @Autowired
//	    private FwFunctionExtManagerService wis;
	// 删除一条记录及其连带按钮控制

    @SuppressWarnings("unchecked")
	public void remove(Object id) {
		Object obj = find(id);
		if (obj != null) {
			baseDAO.remove(obj);
			baseDAO.setEntityManager(em);
			if (id != null && id != "") {
				Map<String, Object> values = new HashMap<String, Object>();
				String attrDataDelJQL = "delete from AuthResController a where a.fwFunId='"
						+ id + "'";
				baseDAO.batchExecuteWithNameParam(attrDataDelJQL, values);
			}
		}
	}
    
    @SuppressWarnings("unchecked")
	public void deleteWithIconView(long id,FwFunctionExt wi1,String ifAdd){
    	Map<String,Object> values=new HashMap<String,Object>();
		String jql1 = "select a  from FwFunctionExt a where 1=1 and a.moduleId="+id+"";
		
		if(Integer.parseInt(ifAdd)==1){
			wi1.setModuleId(id);
			wi1.setHasDynTiyle("0");
			em.persist(wi1);
			em.merge(wi1);
		}
		else if(baseDAO.findWithNameParm(jql1,values).size()>0){
			String jql = "update FwFunctionExt a set a.hasDynTiyle='0',a.tileLogo='"+wi1.getTileLogo()+"',a.tileColor='"+wi1.getTileColor()+"'  ,a.defaultSize='', a.defaultUrl='' , a.supportSizeUrl=''   where 1=1 and a.moduleId="+id+"";
			baseDAO.batchExecuteWithNameParam(jql, values);
		}
		else{
			wi1.setModuleId(id);
			wi1.setHasDynTiyle("0");
			em.persist(wi1);
			em.merge(wi1);
		}
    	
    }
    
    @SuppressWarnings("unchecked")
	public void delete(long id){
    	Map<String,Object> values=new HashMap<String,Object>();
//		String jql = "delete  from FwFunctionExt a where 1=1 and a.moduleId="+id+"";
//		baseDAO.batchExecuteWithNameParam(jql, values);
		String jql = "update FwFunctionExt a set a.hasDynTiyle='0'  ,a.defaultSize='', a.defaultUrl='' , a.supportSizeUrl=''   where 1=1 and a.moduleId="+id+"";
		baseDAO.batchExecuteWithNameParam(jql, values);
    	
    }
    

    
	/**s
	 * 保存：包括新增和修改
	 * @param ws
	 */
	@SuppressWarnings("unchecked")
	public void save(FwFunctionExt ws1) {

		Map<String, Object> values = new HashMap<String, Object>();

		String jql = "delete  from FwFunctionExt a where 1=1 and a.moduleId="
				+ ws1.getModuleId() + "";
		// if(ws1.getModuleId()!=null){
		baseDAO.batchExecuteWithNameParam(jql, values);
		// }
		// baseDAO.findWithNameParm(jql, values);
		//
		// Map<String,Object> values1=new HashMap<String,Object>();
		// String jql1 =
		// "update FwFunctionExt a set a.defaultSize='"+ws1.getDefaultSize()+"' , a.tileLogo='"+ws1.getTileLogo()+"' , a.defaultUrl='"+ws1.getDefaultUrl()+"' , a.supportSizeUrl='"+ws1.getSupportSizeUrl()+"'   where 1=1 and a.moduleId="+ws1.getModuleId()+"";
		// baseDAO.batchExecuteWithNameParam(jql1, values1);

		em.persist(ws1);
		em.merge(ws1);

	}
}
