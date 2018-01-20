package com.yuchengtech.crm.dataauth.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.FwFunction;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;
import com.yuchengtech.crm.exception.BizException;
/**
 * @describe 模块管理服务
 * @author GUOCHI
 * @since 2012-10-09
 */
@Service
public class DataGrantManagerService extends CommonService {
	public DataGrantManagerService() {
		JPABaseDAO<AuthSysFilterAuth, Long> baseDAO = new JPABaseDAO<AuthSysFilterAuth, Long>(
		        AuthSysFilterAuth.class);
		super.setBaseDAO(baseDAO);
	}
	
	/**
     * @param roleId   :   角色ID
     * @param jarray   :   过滤器ID
     */
    @SuppressWarnings("unchecked")
    public void batchSave(JSONArray jarray, String roleId ) {
        if(roleId != null&&roleId!=""){
            Map<String,Object> values=new HashMap<String,Object>();
            String attrDataDelJQL = "delete from AuthSysFilterAuth a where a.roleId='"+roleId+"'";
            baseDAO.batchExecuteWithNameParam(attrDataDelJQL, values);
        }
        for (int i = 0; i < jarray.size(); i++) {
            AuthSysFilterAuth asfa = new AuthSysFilterAuth();
//            if (jarray.get(i).toString().equals("")) {//ID为空，新增
//              asfa.setID(Long.parseLong(jarray.get(i).toString())); //ID
                asfa.setRoleId(roleId);  //角色ID
                asfa.setFilterId(jarray.get(i).toString());   //  过滤器ID
                asfa.setAuthDate(new Date());
                em.persist(asfa);//新增执行
                em.merge(asfa);
//            }else{
//                asfa = em.find(AuthSysFilterAuth.class,Long.parseLong(jarray.get(i).toString()));
//                    asfa.setRoleId(jarray2.get(i).toString());    //角色ID
//                    asfa.setFilterId(Long.parseLong(jarray3.get(i).toString()));   //  过滤器ID
//                    asfa.setAuthDate(new Date());
//                    em.merge(asfa);//修改执行
//            }
        }
    }


	/**
	 * 保存：包括新增和修改
	 */
	public void save(AuthSysFilterAuth ws) {
	    ws.setAuthDate(new Date());
		if (ws.getId() == null) {
			em.persist(ws);
			em.merge(ws);
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
			em.remove(em.find(AuthSysFilterAuth.class, Long.valueOf(idStr)));
			return;
		} else {
			throw new BizException(1, 0, "0001", "该模块下有有效的子功能点，不能删除！");
		}
	}
}
