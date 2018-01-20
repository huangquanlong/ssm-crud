package com.yuchengtech.sm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthAccountRole;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.exception.BizException;
import com.yuchengtech.sm.model.SmTypeAttrRel;
/**
 * 角色授权
 * @since 2017-02-23
 * 
 */
@Service
public class SmTypeAttrGrantService extends CommonService{
	
	public SmTypeAttrGrantService(){
		JPABaseDAO<SmTypeAttrRel, String>  baseDAO=new JPABaseDAO<SmTypeAttrRel, String>(SmTypeAttrRel.class);  
		super.setBaseDAO(baseDAO);
	}
    
	/***
	 * 取消属性
	 * @param idStr 已添加属性的私募类型ID
	 */
	@SuppressWarnings("unchecked")
	public void deleteMenu(String idStr){
		String jql="delete from SmTypeAttrRel s where s.id in ("+idStr+")";
		Map<String,Object> values=new HashMap<String,Object>();
		this.batchUpdateByName(jql, values);
		return ;
	}
	/**
	 * 新增属性
	 * @param typeId  私募类型ID
	 * @param id 属性定义主键 ID
	 */
	public void save(String attrId,String typeId){
		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String[] strArray = attrId.split(","); 
		String jql = "select s from SmTypeAttrRel swhere s.attrId IN ("+attrId+") and s.typeId = "+typeId+" ";
		Query q = em.createQuery(jql.toString());
		List<AdminAuthAccountRole> list = q.getResultList();
		if(list.size()==0){
			for(int i=0;i<strArray.length;i++){
				SmTypeAttrRel sr=new SmTypeAttrRel();
				sr.setAttrId(Integer.parseInt(strArray[i]));
				sr.setTypeId(Integer.parseInt(typeId));
				sr.setCrtBy(authUser.getUserId());
				sr.setCrtTime(new Date());
				this.em.persist(sr);
			}
			return;
		}else {
			throw new BizException(1,0,"100010","所选的用户存在已经被添加的用户，请重新选择！");
		}
	}
	
	
	
}
