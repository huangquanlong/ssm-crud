package com.yuchengtech.bcrm.system.service;

import java.text.ParseException;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AuthResController;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.crm.exception.BizException;

/**
 * @describe 功能点按钮控制管理
 * @author GUOCHI
 * @since 2012-10-15
 */
@Service
public class ControllersManagerService extends CommonService {
	public ControllersManagerService() {
		JPABaseDAO<AuthResController, Long> baseDAO = new JPABaseDAO<AuthResController, Long>(AuthResController.class);
		super.setBaseDAO(baseDAO);
	}
	/**
	 * 保存：包括新增和修改
	 * @param ws
	 */
	@SuppressWarnings("unchecked")
    public void save(AuthResController ws)throws ParseException{		
		if(ws.getId()==null){		    
		    String sql="select c from AuthResController c where  c.fwFunId='"+ws.getFwFunId()+"'"+" and c.conCode= '"+ws.getConCode()+"'";
	        Query q = em.createQuery(sql.toString());   
	        List<AuthResController> list= q.getResultList(); 
	        if(list.size()==0){
	            ws.setAppId("0");
	            ws.setVersion(Long.valueOf(0));
	            em.persist(ws);
	            em.merge(ws);
	            return ;
	        }
	        else{
	            throw new BizException(1, 0, "0001", "该功能点下有相同的控制代码，新增失败！");
	        }
		}else em.merge(ws);
	}
	 /**
     * 删除方法
     * @param idStr 控制id字符串
     */
    public void remove(String idStr) {
        String[] strarray = idStr.split(",");//将id字符串拆分并拼接成String[]数组
        for (int i = 0; i < strarray.length; i++) {//循环遍历id数组，并删除相应日志
            long ID = Long.parseLong(strarray[i]);
            em.remove(em.find(AuthResController.class, ID));
        }
    }
}
