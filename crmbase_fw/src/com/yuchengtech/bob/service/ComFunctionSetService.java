package com.yuchengtech.bob.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.model.OcrmFWpModule;
import com.yuchengtech.bob.vo.AuthUser;

@Service
@Transactional(value="postgreTransactionManager")
public class ComFunctionSetService {
    
    private EntityManager em;
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
     @SuppressWarnings("unchecked")
    public void commitUserSet(JSONArray ja){
        
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId();    
        /**
         * TODO Commit the layout id setting.
         */
        String checkSql = "SELECT ofw from OcrmFWpModule ofw where ofw.userId = ?1";
        Query checkQuery = em.createQuery(checkSql);
        checkQuery.setParameter(1, userId);
        List<OcrmFWpModule> ofwl = checkQuery.getResultList();
        
        if (ofwl.size()>0){
        	for(OcrmFWpModule ofwt:ofwl){
        		em.remove(ofwt);
        	}
        }
        
        if(ja.size()>0){
			for(int i=0;i<ja.size();i++){
				 OcrmFWpModule gm = new OcrmFWpModule();
				gm.setUserId(userId);
				gm.setModuleSeq(new BigDecimal(((JSONObject)ja.get(i)).getString("MODULE_SEQ").toString()));
				gm.setModuleId(((JSONObject)ja.get(i)).getString("MODULE_ID").toString());
				gm.setIconurl(((JSONObject)ja.get(i)).getString("ICON_URL").toString());
				em.persist(gm);
	        	}
	        }
        
       }
    
    
}
    



