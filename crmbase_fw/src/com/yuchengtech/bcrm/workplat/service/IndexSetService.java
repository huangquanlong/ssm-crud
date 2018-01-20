package com.yuchengtech.bcrm.workplat.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.model.WorkspaceUserLayout;
import com.yuchengtech.bob.model.WorkspaceUserModule;
import com.yuchengtech.bob.vo.AuthUser;

@Service
@Transactional(value="postgreTransactionManager")
public class IndexSetService {
    
    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
   
    
    
    @SuppressWarnings("unchecked")
    public void commitUserSet(JSONArray ja,String layoutId){
        
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId();    
        
        /**
         * TODO Commit the layout id setting.
         */
        String checkSql = "SELECT wul from WorkspaceUserLayout wul where wul.USER_ID = ?1";
        Query checkQuery = em.createQuery(checkSql);
        checkQuery.setParameter(1, userId);
        WorkspaceUserLayout wul;
        int hasSet = checkQuery.getResultList().size();
        if(hasSet>0){
            wul = (WorkspaceUserLayout) checkQuery.getSingleResult();
            wul.setLAYOUT_ID(layoutId);
            em.merge(wul);
           // em.getTransaction().commit();
        }else {
            wul = new WorkspaceUserLayout();
            wul.setUSER_ID(userId);
            wul.setLAYOUT_ID(layoutId);
            em.persist(wul);
        }
        
        /**
         * TODO Commit the module's setting.
         */
        if(hasSet>0){
            String clearSql = "SELECT wum FROM WorkspaceUserModule wum where wum.USER_ID = ?1";
            Query clearQuery  = em.createQuery(clearSql);
            clearQuery.setParameter(1, userId);
            List<WorkspaceUserModule> wuml = clearQuery.getResultList();
            for(WorkspaceUserModule wum:wuml){
                em.remove(wum);
            }
        }
        ListIterator li = ja.listIterator();
        while(li.hasNext()){
            JSONObject jb = (JSONObject) li.next();
            WorkspaceUserModule tmpWum = new WorkspaceUserModule();
            tmpWum.setUSER_ID(userId);
            tmpWum.setLAYOUT_ID(layoutId);
            tmpWum.setCOLUMN_SEQ(BigDecimal.valueOf((Integer)jb.get("COLUMN_SEQ")));
            tmpWum.setMODULE_SEQ(BigDecimal.valueOf((Integer)jb.get("MODULE_SEQ")));
            tmpWum.setMODULE_ID((String)jb.get("MODULE_ID"));
            em.persist(tmpWum);
        }
    }    
}
