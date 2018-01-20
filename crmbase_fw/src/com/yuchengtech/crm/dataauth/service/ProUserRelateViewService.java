package com.yuchengtech.crm.dataauth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.dataauth.model.UserViewRelation;

/**
 * @描述: 项目视图授权数据处理Service
 * @author : wzy
 * @date : 2015-02-01
 */
@Service
@Transactional(value = "postgreTransactionManager")
public class ProUserRelateViewService {
	@PersistenceContext
	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	// 无查询条件
	public List<UserViewRelation> query(String roleId) {
		StringBuffer querysql = new StringBuffer();
		querysql.append("select c from UserViewRelation c where  c.roleId=?1");
		Query q = em.createQuery(querysql.toString());
		q.setParameter(1, roleId);
		q.setFirstResult(0);
		q.setMaxResults(100000);
		List<UserViewRelation> list = q.getResultList();
		return list;
	}

	// 项目视图授权数据保存逻辑处理
	public void batchSave(String[] menuAddCodeStr, String[] menuDelCodeStr,
			String userId, String projId) {
		if (menuAddCodeStr != null) {
			for (int i = 0; i < menuAddCodeStr.length; i++) {
				UserViewRelation model = new UserViewRelation();
				String[] str = menuAddCodeStr[i].split("_");
				model.setViewId(Long.parseLong(str[0]));
				model.setIfFlag(str[1]);
				// model.setViewId(Long.parseLong(menuAddCodeStr[i]));
				model.setRoleId("p_" + userId);// 项目角色ID（为了和系统角色ID区分，在项目角色ID前增加前缀“p_”）
				model.setProjId(Long.valueOf((projId == null || ""
						.equals(projId)) ? "0" : projId));// 项目ID
				em.persist(model);
			}
		}

		Map<String, Object> values = new HashMap<String, Object>();
		JPABaseDAO<UserViewRelation, Long> baseDAO1 = new JPABaseDAO<UserViewRelation, Long>(
				UserViewRelation.class);
		baseDAO1.setEntityManager(em);
		if (menuDelCodeStr != null) {
			for (int a = 0; a < menuDelCodeStr.length; a++) {
				String[] sto = menuDelCodeStr[a].split("_");
				Long viewId = Long.valueOf(sto[0]);
				// Long viewId =Long.valueOf(menuDelCodeStr[a]);
				String attrDataDelJQL = "delete from UserViewRelation a where a.roleId='p_"
						+ userId + "' and a.viewId = '" + viewId + "' and a.projId = "+projId;
				baseDAO1.batchExecuteWithNameParam(attrDataDelJQL, values);
			}
		}
	}

	// 根据项目ID及当前用户账户，查询当前用户具有哪些项目角色
	// 返回格式为：'code1','code2'...
	@SuppressWarnings("rawtypes")
	public String getProRoleByUserId(AuthUser auth, String projId) {
		String rsStr = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("select\n");
		sb.append("	t3.prole_code\n");
		sb.append("from\n");
		sb.append("	ppmp_project_member t1,\n");
		sb.append("	ppmp_project_rolememr_rel t2,\n");
		sb.append("	ppmp_project_role t3\n");
		sb.append("where\n");
		sb.append("	t2.mem_id = t1.mem_id\n");
		sb.append("and t2.prole_id = t3.prole_id\n");
		sb.append("and t1.account_id = '" + auth.getUserId() + "'\n");
		sb.append("and t1.proj_id = " + projId);
		List list = this.em.createNativeQuery(sb.toString()).getResultList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				rsStr += "'p_" + list.get(i) + "',";
			}
		}
		// 去掉最后的逗号
		if (!"".equals(rsStr)
				&& ",".equals(rsStr.substring(rsStr.length() - 1))) {
			rsStr = rsStr.substring(0, rsStr.length() - 1);
		}
		if ("".equals(rsStr)) {
			rsStr = "''";
		}
		return rsStr;
	}
}