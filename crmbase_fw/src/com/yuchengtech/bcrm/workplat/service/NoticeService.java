package com.yuchengtech.bcrm.workplat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.common.EntityToVoTrans;
import com.yuchengtech.bob.model.WorkingplatformNotice;
import com.yuchengtech.bob.model.WorkingplatformNoticeRead;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.bob.vo.WorkingplatformNoticeReadVo;
import com.yuchengtech.bob.vo.WorkingplatformNoticeVo;

/**
 * @describe 公告记录服务
 * @author WillJoe
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class NoticeService {
	
	private EntityManager em;
		
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	/**
	 * @param notice
	 * @return
	 */
	public void save(WorkingplatformNoticeVo notice){
	    WorkingplatformNotice wn;
	    AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId(); 
	    if(notice.getNoticeId()==null){
	        wn = new WorkingplatformNotice();
	        wn.setNoticeLevel(notice.getNoticeLevel());
            wn.setIsTop(notice.getIsTop());//是否置顶 
            wn.setTopActiveDate(notice.getTopActiveDate());//置顶时间
            wn.setPublished("pub002");
            wn.setPublishOrganizer("");
            wn.setNoticeTitle(notice.getNoticeTitle());
            wn.setActiveDate(notice.getActiveDate());
            wn.setNoticeContent(notice.getNoticeContent());
            wn.setCreator(userId);
	        em.persist(wn);
	        auth.setPid(wn.getNoticeId().toString());
	        return;
	    }else {
	        wn = em.find(WorkingplatformNotice.class, notice.getNoticeId());	
	        if("publish".equals(notice.getMethodNs())){
	            wn.setPublished("pub001");
	            wn.setPublishTime(new Date());
	            wn.setPublisher(userId);
	            wn.setPublishOrganizer(auth.getUnitId());
	        }else{
	        	wn.setIsTop(notice.getIsTop());//是否置顶 
	            wn.setTopActiveDate(notice.getTopActiveDate());//置顶时间
	            wn.setNoticeLevel(notice.getNoticeLevel());
	            wn.setNoticeTitle(notice.getNoticeTitle());
	            wn.setNoticeContent(notice.getNoticeContent());
	            wn.setActiveDate(notice.getActiveDate());
	        }
	        em.merge(wn);
	        return;
		}
	}
	
	public void save(WorkingplatformNoticeVo notice, String corp){
	    AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId(); 
	    if(corp.equals("publish")){
	        WorkingplatformNotice wn = em.find(WorkingplatformNotice.class, notice.getNoticeId());
	        wn.setPublished("pub001");
            wn.setPublishTime(new Date());
            wn.setPublisher(userId);
            wn.setPublishOrganizer(auth.getUnitId());
            em.merge(wn);
	    }
	    return;
	}
	
	
	/**
	 * @param first
	 * @param last
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<WorkingplatformNoticeVo> findAll(int first,int last){		
        
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId();        
        
		String searchSql = "select n from WorkingplatformNotice n";
		Query query = em.createQuery(searchSql);		
		query.setFirstResult(first);
		query.setMaxResults(last);
		List<WorkingplatformNotice> result = (List<WorkingplatformNotice>)query.getResultList();
		List<WorkingplatformNoticeVo> resultVo = new ArrayList<WorkingplatformNoticeVo>();
		EntityToVoTrans etv = new EntityToVoTrans();		   
		for(WorkingplatformNotice wn:result){
		    WorkingplatformNoticeVo wpnv = (WorkingplatformNoticeVo) etv.trans(wn);		
		    List<WorkingplatformNoticeReadVo> wnrvl = new ArrayList<WorkingplatformNoticeReadVo>();
		    for(WorkingplatformNoticeReadVo wnrv:wpnv.getNoticeIdL()){
		        if(null!=userId&&!userId.equals(wnrv.getUserId())){
		            continue;
		        }else wnrvl.add(wnrv);
		    }
		    wpnv.setNoticeIdL(wnrvl);
		    if(wnrvl.size()==0){
		        wpnv.setIsRead("未阅");		        
		    }else wpnv.setIsRead("已阅");		    
            resultVo.add(wpnv);
		}
		return resultVo;
	}
    
    @SuppressWarnings("unchecked")
    public List<WorkingplatformNoticeVo> findAll(int first,int last,WorkingplatformNoticeVo wn){
        
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId();
        //String noticeTitle = wn.getNoticeTitle();
        //String searchSql = "select n from WorkingplatformNotice n where n.noticeTitle = ?1";
        StringBuilder sb = new StringBuilder("select n from WorkingplatformNotice n JOIN n.noticeIdL l where 1>0 ");
        
//        /**是否阅读*/
//        if(wn.getIsRead()!=null&&!"".equals(wn.getIsRead())){
//            if(wn.getIsRead().equals("已阅")){
//                sb.append(" and l.userId = ?1");
//            }else sb.append(" and l.userId <> ?1");
//        }
        
        /**公告标题*/
        if(wn.getNoticeTitle()!=null&&!"".equals(wn.getNoticeTitle())){
            sb.append(" and n.noticeTitle = ?2");
        }
        
        /**重要程度*/
        if(wn.getNoticeLevel()!=null&&!"".equals(wn.getNoticeLevel())){
            sb.append(" and n.noticeLevel = ?3");
        }
        
        /**发布机构*/
        if(wn.getPublishOrganizer()!=null&&!"".equals(wn.getPublishOrganizer())){
            sb.append(" and n.publishOrganizer = ?4");
        }        
        
        /**发布人*/
        if(wn.getPublisher()!=null&&!"".equals(wn.getPublisher())){
            sb.append(" and n.publisher = ?5");
        }
        
        /**发布时间区间*/
        if(wn.getPublishTime()!=null&&!"".equals(wn.getPublishTime())){
            if(wn.getPublishTimeEnd()!=null&&!"".equals(wn.getPublishTimeEnd())){
                sb.append(" and n.publishTime > ?6 and n.publishTime < ?7");
            }else sb.append(" and n.publishTime > ?8");
        }else{
            if(wn.getPublishTimeEnd()!=null&&!"".equals(wn.getPublishTimeEnd())){
                sb.append(" and n.publishTime< ?9");
            }
        }
        
        Query q = em.createQuery(sb.toString());
        
        if(wn.getNoticeTitle()!=null&&!"".equals(wn.getNoticeTitle())){
            q.setParameter(2, wn.getNoticeTitle());
        }
        
        if(wn.getNoticeLevel()!=null&&!"".equals(wn.getNoticeLevel())){
            q.setParameter(3, wn.getNoticeLevel());
        }
        
        if(wn.getPublishOrganizer()!=null&&!"".equals(wn.getPublishOrganizer())){
            q.setParameter(4, wn.getPublishOrganizer());
        }
        
        if(wn.getPublisher()!=null&&!"".equals(wn.getPublisher())){
            q.setParameter(5, wn.getPublisher());
        }
        
        if(wn.getPublishTime()!=null&&!"".equals(wn.getPublishTime())){
            if(wn.getPublishTimeEnd()!=null&&!"".equals(wn.getPublishTimeEnd())){
               q.setParameter(6, wn.getPublishTime());
               q.setParameter(7, wn.getPublishTimeEnd());
            }else q.setParameter(8, wn.getPublishTime());
        }else{
            if(wn.getPublishTimeEnd()!=null&&!"".equals(wn.getPublishTimeEnd())){
                q.setParameter(9, wn.getPublishTime());
            }
        }
        
        List<WorkingplatformNotice> result = ( List<WorkingplatformNotice>)q.getResultList();
        q.setFirstResult(first);
        q.setMaxResults(last);
        List<WorkingplatformNoticeVo> resultVo = new ArrayList<WorkingplatformNoticeVo>();
        EntityToVoTrans etv = new EntityToVoTrans();
        for(WorkingplatformNotice wn1:result){
            WorkingplatformNoticeVo wpnv = (WorkingplatformNoticeVo) etv.trans(wn1);
            List<WorkingplatformNoticeReadVo> wnrvl = new ArrayList<WorkingplatformNoticeReadVo>();
            for(WorkingplatformNoticeReadVo wnrv:wpnv.getNoticeIdL()){
                if(null!=userId&&!userId.equals(wnrv.getUserId())){
                    continue;
                }else wnrvl.add(wnrv);
            }
            wpnv.setNoticeIdL(wnrvl);
            if(wnrvl.size()==0){
                wpnv.setIsRead("未阅");               
            }else wpnv.setIsRead("已阅");
            resultVo.add(wpnv);                        
        }
        return resultVo;
    }
    
	/**
	 * 
	 * @param id
	 * @return
	 */
	public WorkingplatformNoticeVo findById(Long id){
	    
		WorkingplatformNotice notice = em.find(WorkingplatformNotice.class, id);
		WorkingplatformNoticeVo wpnv = new WorkingplatformNoticeVo();
        wpnv.setActiveDate(notice.getActiveDate());
        wpnv.setIsTop(notice.getIsTop());
        wpnv.setModuleType(notice.getModuleType());
        wpnv.setNoticeTitle(notice.getNoticeTitle());
        wpnv.setNoticeId(notice.getNoticeId());
		return wpnv;
	}
	
	/**
	 * 
	 * @param notice
	 * @return
	 */
	public void remove(WorkingplatformNotice notice){
		em.remove(notice);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public void remove(Long id){
	    
		WorkingplatformNotice notice = em.find(WorkingplatformNotice.class, id);
		em.remove(notice);	
	}	
    /**
     * @describe find by obj.
     * @return
     */
    @SuppressWarnings("unchecked")
    public void setNoticeRead(WorkingplatformNotice notice,String userId){
        
    	String checkRead = "select wnr from WorkingplatformNoticeRead wnr where wnr.noticeId = ?1 and wnr.userId = ?2";
    	Query wnrQuery = em.createQuery(checkRead);
    	wnrQuery.setParameter(1, notice.getNoticeId());
    	wnrQuery.setParameter(2, userId);
    	List<WorkingplatformNoticeRead> result = wnrQuery.getResultList();
    	WorkingplatformNoticeRead wnrAdd;
    	if(null==result||result.size()==0){
    		wnrAdd = new WorkingplatformNoticeRead();
    		wnrAdd.setIsLoad(true);
    		wnrAdd.setUserId(userId);
    		wnrAdd.setReadTime(new Date());
    		em.persist(wnrAdd);
    	}
    }

}
