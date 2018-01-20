package com.yuchengtech.bcrm.system.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.system.model.AdminLogInfo;

/**
 * 日志管理
 * @author weijl
 * @since 2012-09-24
 */
@Service
@Transactional(value="postgreTransactionManager")
public class AdminLogManagerService {
	
    private EntityManager em;//声明一个实体管理器
    
    /**
     * 为本service的实体管理器赋值
     * @param em 实体管理器
     */
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * 删除方法
     * @param idStr 日志id字符串
     */
    public void remove(String idStr) {
    	
        String[] strarray = idStr.split(",");//将id字符串拆分并拼接成String[]数组
        
        for (int i = 0; i < strarray.length; i++) {//循环遍历id数组，并删除相应日志
            long ID = Long.parseLong(strarray[i]);
            em.remove(em.find(AdminLogInfo.class, ID));
        }
    }
}


