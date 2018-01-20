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

import com.yuchengtech.bob.model.WorkingplatformNotice;
import com.yuchengtech.bob.model.WorkingplatformNoticeRead;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.bob.vo.WorkingplatformNoticeReadVo;

/**
 * @describe 公告阅读下载记录服务
 * @author WillJoe
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class NoticeReadLoadService {
	
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	/**
	 * 保存：包括插入和修改
	 * @param wnr
	 */
	public void save(WorkingplatformNoticeReadVo wnrv) {
	    
	    Long noticeId = wnrv.getNoticeId();
	    AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = auth.getUserId();
        
        WorkingplatformNotice wn = em.find(WorkingplatformNotice.class, noticeId);	    
        Boolean hasRead = false;
        /**Check current user if has read it.*/
        for(WorkingplatformNoticeRead wnr:wn.getNoticeIdL()){
            if(userId.equals(wnr.getUserId())){
                hasRead = true;
                break;
            }else continue;
        }
        
        if(!hasRead){
            WorkingplatformNoticeRead wnr = new WorkingplatformNoticeRead();
            wnr.setReadTime(new Date());
            wnr.setUserId(userId);	    
            wnr.setNoticeId(wn);	    
            wn.getNoticeIdL().add(wnr);
            em.merge(wn);
        }
	    return;	    	    
	}
	
	/**
	 * 移除数据
	 * @param id
	 */
	public void remove(Long id){
		WorkingplatformNoticeRead wnr = em.find(WorkingplatformNoticeRead.class, id);
		em.remove(wnr);
	}
	
	
	/**
	 * 查看数据
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unused")
    public WorkingplatformNoticeReadVo find(Long id){
	    WorkingplatformNoticeRead wnr =  em.find(WorkingplatformNoticeRead.class, id);
	    return new WorkingplatformNoticeReadVo();
	}
	
	/**
	 * 查询所有数据
	 * @return
	 */
	@SuppressWarnings("unused")
    public List<WorkingplatformNoticeReadVo> findAll(){
		String findAll = "select wnr from WorkingplatformNoticeRead wnr";
		Query qfindAll = em.createQuery(findAll);
		List<WorkingplatformNoticeReadVo> wnrvc = new ArrayList<WorkingplatformNoticeReadVo>();
		return wnrvc;
	}
}
