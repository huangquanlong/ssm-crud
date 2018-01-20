package com.yuchengtech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmFASsRelation;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;


/**
 * 数据集关联关系管理（新增，修改，删除）
 * @author songxs
 * @since 2012-12-7
 *
 */
@Service
public class DataSetRelationService extends CommonService{
	
	   public DataSetRelationService(){
		   JPABaseDAO<OcrmFASsRelation, String>  baseDAO=new JPABaseDAO<OcrmFASsRelation, String>(OcrmFASsRelation.class);  
		   super.setBaseDAO(baseDAO);
	   }


	
}
