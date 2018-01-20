package com.yuchengtech.bcrm.workplat.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bob.model.WorkplatRemindList;

/**
 * 
 * @author km
 * 
 */
@Service
@Transactional(value="postgreTransactionManager")
public class WorkplatRemindListService {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

 // 获取系统当前时间
    public Date getCurrentDate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        String date = format.format(new java.util.Date()).toString();
        return (Date) format.parse(date);

    }
    
    /**
     * 设置已读
     * 
     * @param id
     */
    public void readed(String idStr) {
        String[] strarray = idStr.split(",");
        for (int i = 0; i < strarray.length; i++) {
            Long id = Long.parseLong(strarray[i]);
            WorkplatRemindList wrl =em.find(WorkplatRemindList.class, id);
            wrl.setMsgSts("1");
            Date date;
            try {
                date = getCurrentDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateString=format.format(date);
                wrl.setReadDate(dateString);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            em.merge(wrl);
        }
    }

}
