package com.yuchengtech.bcrm.workplat.service;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.struts2.json.JSONUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.workplat.model.WorkingplatformSchedule;
import com.yuchengtech.bob.vo.AuthUser;

/**
 * 日程服务
 * 
 * @author WillJoe
 * 
 */
@Service
@Transactional(value="postgreTransactionManager")
public class WorkingplatformScheduleService {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * 保存：包括新增和修改
     * 
     * @param ws
     */
    public void save(WorkingplatformSchedule ws) {
        AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = auth.getUserId();
        String groupId = auth.getUnitId();
        if (ws.getScheduleId() == null) {
            ws.setCreator(userId);
            ws.setCreateOrganizer(groupId);
            ws.setCreateDate(new Date());
            em.persist(ws);
        } else {
            ws.setCreator(userId);
            ws.setCreateOrganizer(groupId);
            em.merge(ws);
        }
    }

    /**
     * 移除记录
     * 
     * @param idStr
     */
    public void remove(String idStr) {
        long id = Long.parseLong(idStr);
        WorkingplatformSchedule workingplatformSchedule = find(id);
        if (workingplatformSchedule != null) {
            em.remove(workingplatformSchedule);
        }
    }

    /**
     * 查看记录
     * 
     * @param id
     * @return
     */
    public WorkingplatformSchedule find(Long id) {
        return em.find(WorkingplatformSchedule.class, id);
    }

    /**
     * 查询所有记录
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<WorkingplatformSchedule> findAll() {
        String wsFindAll = "select ws from WorkingplatformSchedule ws";
        Query wsQuery = em.createQuery(wsFindAll);
        return wsQuery.getResultList();
    }

    /**
     * 日程操作入口
     * @param events：前台记录数据，json格式
     */
    @SuppressWarnings("unchecked")
	public void scheduleOptions(String events){
    	WorkingplatformSchedule ws;
    	try {
    		if(events.indexOf("{")>=0){//修改
    			Map<String, Object> eventsO;
    			eventsO = (Map<String, Object>) JSONUtil.deserialize(events);
    			String startDate=null,endDate=null,remindBelong=null,scheduleTitle=null,scheduleContent = null,customer = null;
    			Long isTeam = null;
    			Boolean all_day = true;
    			startDate = (String) eventsO.get("startDate");
				endDate = (String) eventsO.get("endDate");
				all_day =(Boolean) eventsO.get("all_day")==null? true:(Boolean) eventsO.get("all_day") ;
				remindBelong = (String) eventsO.get("remindBelong");
				scheduleTitle = (String) eventsO.get("scheduleTitle");
				scheduleContent = (String) eventsO.get("scheduleContent");
				customer = (String) eventsO.get("relationCust");
				if(eventsO.get("isTeam")!=null)
				{
				    isTeam = Long.valueOf((String) eventsO.get("isTeam"));
				}
    			if(events.indexOf("scheduleId")<0){//新增
    				ws = new WorkingplatformSchedule();
    			} else {//修改
    				ws = em.find(WorkingplatformSchedule.class, Long.valueOf((String) eventsO.get("scheduleId")));
    			}
    			
    			if(null!=startDate&&!"".equals(startDate)){
    				ws.setStartDate(DateFormat.getDateInstance().parse(startDate.substring(0, 10)));
    				ws.setStartTime(startDate.substring(11));
    			}
    			if(null!=endDate&&!"".equals(endDate)){
    				ws.setEndDate(DateFormat.getDateInstance().parse(endDate.substring(0, 10)));
    				ws.setEndTime(endDate.substring(11));
    			}
    			if(null!=remindBelong){
    				ws.setRemindBelong(remindBelong);
    			}
    			if(null!=scheduleTitle){
    				ws.setScheduleTitle(scheduleTitle);
    			}
    			if(null!=scheduleContent){
    				ws.setScheduleContent(scheduleContent);
    			}
    			if(null!=customer){
    				ws.setRelationCust(customer);
    			}
    			if(null!=isTeam){
    				ws.setIsTeam(isTeam);
    			}
    			ws.setIsProcess(all_day);
    			this.save(ws);
    			
    		}else{//删除，（）
    			this.remove(events.substring(1,events.length()-1));
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
