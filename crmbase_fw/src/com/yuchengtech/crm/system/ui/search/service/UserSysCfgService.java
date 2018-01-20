package com.yuchengtech.crm.system.ui.search.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.system.ui.search.model.OcrmFSysUserCfg;

/**
 * @describtion: 用户首页个性化设置
 *
 * @author : lhqheli (email: lhqheli@gmail.com)
 * @date : 2014-08-13 11:24:20
 */
@Service
public class UserSysCfgService extends CommonService {
    public UserSysCfgService(){
        JPABaseDAO<OcrmFSysUserCfg, Long> baseDao = new JPABaseDAO<OcrmFSysUserCfg, Long>(OcrmFSysUserCfg.class);
        super.setBaseDAO(baseDao);
    }
    
    /**
     * 用户首页个性化设置保存
     */
    public Object save(Object obj) {
    	AuthUser auth = (AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OcrmFSysUserCfg userSysCfg = (OcrmFSysUserCfg)obj;
        userSysCfg.setUserId(auth.getUserId());
        
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("userId", auth.getUserId());
        super.batchUpdateByName("DELETE FROM OcrmFSysUserCfg t WHERE t.userId =:userId", values);
        auth.putAttribute("crm.front.BG", userSysCfg.getBgIcon());
        auth.putAttribute("crm.front.TH", userSysCfg.getThemeCss());
        auth.putAttribute("crm.front.WS", userSysCfg.getWordSize());
        return super.save(userSysCfg);
    }
    
    @SuppressWarnings("unchecked")
	public OcrmFSysUserCfg findByUser(){
    	String findSql = "SELECT t from OcrmFSysUserCfg t WHERE t.userId = ?1";
    	AuthUser auth = (AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String userId = auth.getUserId();
    	Query query = this.em.createQuery(findSql);
    	query.setParameter(1, userId);
    	List<OcrmFSysUserCfg> lofsuc = (List<OcrmFSysUserCfg>)query.getResultList();
    	if(lofsuc.size()>0){
    		return lofsuc.get(0);
    	}else return null;
    }
    
}
