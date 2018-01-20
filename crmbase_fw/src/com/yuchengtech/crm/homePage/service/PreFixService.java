package com.yuchengtech.crm.homePage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.homePage.model.PreFix;

/**
 * 
 * 主键生成的Service接口类
 * PreFixService 接口
 * @author lenovo
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class PreFixService extends CommonService{

	public PreFixService(){
		   JPABaseDAO<PreFix, Long>  baseDAO=new JPABaseDAO<PreFix, Long>(PreFix.class);  
		   super.setBaseDAO(baseDAO);
	 }
	
	/**
	 * 更新前缀的值
	 * @param id
	 */
	public void updateLastNum(String id){
		PreFix preFix = em.find(PreFix.class, id);
		//最后一位数加1
		int lastNum = preFix.getLastNum();
		lastNum = lastNum + 1;
		preFix.setLastNum(lastNum);
		if(null != preFix){
			em.persist(preFix);
		}
	}
	
	/**
	 * 获取生成ID的值
	 * @param id
	 * @return
	 */
	public String getPreFixID(String id){
		PreFix preFix = em.find(PreFix.class, id);
		//上次最后的大小
		Integer lastNum = preFix.getLastNum();
		//长度
		Integer legth = preFix.getExtent();
		
		String preFixID = preFix.getPreFix();
		for(int i=0 ; i< legth - (lastNum+"").length() ; i++){
			preFixID += "0";
		}
		//获取过后自增一位
		this.updateLastNum(id);
		//最后结果
		return preFixID + preFix.getLastNum();
	}
}
