package com.yuchengtech.crm.dataauth.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.dataauth.model.AuthSysFilterAuth;

/**
 * @describe 数据授权信息查询服务，仅在用户登陆的时候调用
 * @author WILLJOE
 */
@Service
@Transactional(value="postgreTransactionManager")
public class DataAuthInfo {
	
	private EntityManager em ;
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	private List<AuthSysFilterAuth> asfa;
	
		public List<AuthSysFilterAuth> LoadAuthInfo(AuthUser ia){
		List authList = ia.getAuthorities();
		if(authList.size()>0){
			StringBuffer JQL = new StringBuffer("SELECT A FROM AuthSysFilterAuth A WHERE A.roleId IN (");
			for(int i=0;i<authList.size();i++){
				if(i==0){
					JQL.append("'"+((GrantedAuthority)authList.get(i)).getAuthority()+"'");
				}else
					JQL.append(",'"+((GrantedAuthority)authList.get(i)).getAuthority()+"'");
			}
			JQL.append(" )");
//			asfa=em.createQuery("SELECT A FROM AuthSysFilterAuth A").getResultList();
//			for(AuthSysFilterAuth tmpFilter : asfa){
//				if(em.contains(tmpFilter)){
//					em.refresh(tmpFilter);
//				}
//			}
			return (List<AuthSysFilterAuth>)em.createQuery(JQL.toString()).getResultList();
		}else return null;
	}
}
