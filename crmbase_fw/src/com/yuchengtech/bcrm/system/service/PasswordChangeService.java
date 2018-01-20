package com.yuchengtech.bcrm.system.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuchengtech.bcrm.system.model.AdminAuthAccount;
import com.yuchengtech.bcrm.system.model.AdminAuthPasswordLog;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.EndecryptUtils;
import com.yuchengtech.crm.exception.BizException;

/**
 * @describe 重置密码模块Service
 * @author wangwan
 * @since 2012.11.13
 *
 */
@Service
@Transactional(value="postgreTransactionManager")
public class PasswordChangeService extends CommonService{
	
	public PasswordChangeService(){
		JPABaseDAO<AdminAuthPasswordLog,Long> baseDao = new JPABaseDAO<AdminAuthPasswordLog,Long>(AdminAuthPasswordLog.class);
		
		super.setBaseDAO(baseDao);
	}
	private EntityManager em;
		
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	/*
	 * @param userId 用户名
	 * @param password 输入的新密码
	 * @param updateUser 更新人
	 * @param authEnableFlag 与历史密码重复校验策略启动标志
	 * @param historyPw 与历史密码重复次数
	 * @param oldPassword 修改人输入密码验证
	 * @param oldPassword2 当前用户密码
	 * @param id 所修改用户在用户表中数据主键ID
	 */
	@SuppressWarnings("unchecked")
	public void authPassword(String userId,String password,String updateUser,String authEnableFlag,String historyPw,String oldPassword,String oldPassword2) {
		AuthUser auth=this.getUserSession();   
		
		String message = "";//抛出异常信息
		
		//wzy,20140404,modify：修改下面一行代码，增加判空处理，避免报错
		// int historyPwLength =  Integer.parseInt(historyPw);
		int historyPwLength =  (historyPw == null || "".equals(historyPw))?-1:Integer.parseInt(historyPw);
		String passwordEncode=EndecryptUtils.encrypt(password);
		String oldPasswordEncoded =EndecryptUtils.encrypt(oldPassword);
		String oldPW = auth.getPassword();
		if(oldPassword2.equals("")){//若oldPassword2参数为空，则为重置密码模块
			//判断当前用户输入原密码是否正确
			if(authEnableFlag.equals("1")){//判断与历史密码重复校验策略是否启动
				String searchSql = "select n from AdminAuthPasswordLog n where n.userId =?1 order by n.id desc";
				Query query = em.createQuery(searchSql);
				query.setParameter(1, userId);
				query.setFirstResult(0);
				query.setMaxResults(historyPwLength);
				List<AdminAuthPasswordLog> result = (List<AdminAuthPasswordLog>)query.getResultList();
				
				for(AdminAuthPasswordLog aapl : result){//与历史密码循环比对
					if( passwordEncode.equals( aapl.getPswdUped() ) ){
						throw new BizException(1,0,"1000","输入密码与近期历史密码重复");
					}
				}
			}
			String searchSql = "select bo from AdminAuthAccount bo where 1=1 and bo.accountName = ?1";
			Query query = em.createQuery(searchSql);
			query.setParameter(1, userId);
			List<AdminAuthAccount> result = (List<AdminAuthAccount>)query.getResultList();
			for(AdminAuthAccount aapl : result){
				if(!aapl.getId().equals("")){
					AdminAuthPasswordLog ws = new AdminAuthPasswordLog();//保存密码至历史密码记录表
					ws.setUpdateUser(updateUser);
					ws.setUserId(userId);
					ws.setPswdUped(passwordEncode);
					ws.setPswdUpTime(new Date(System.currentTimeMillis()));
					this.em.persist(ws);
				
					AdminAuthAccount wm = em.find(AdminAuthAccount.class,Long.valueOf(aapl.getId()));//从用户表修改密码
					wm.setPassword(passwordEncode);
					wm.setTemp2("");	//设置密码修改标志为""必须修改
					this.em.merge(wm);
				}
			}
		
		}else{//若oldPassword2参数不为空，则为个人密码重置模块，需要对当前用户密码进行验证
			if(oldPasswordEncoded.equals(oldPW)){//判断当前用户输入原密码是否正确
				if(authEnableFlag.equals("1")){//判断与历史密码重复校验策略是否启动
					String searchSql = "select n from AdminAuthPasswordLog n where n.userId =?1 order by n.id desc";
					Query query = em.createQuery(searchSql);
					query.setParameter(1, userId);
					query.setFirstResult(0);
					query.setMaxResults(historyPwLength);
					List<AdminAuthPasswordLog> result = (List<AdminAuthPasswordLog>)query.getResultList();
					
					for(AdminAuthPasswordLog aapl : result){//与历史密码循环比对
						if( passwordEncode.equals( aapl.getPswdUped() ) ){
							throw new BizException(1,0,"1000","输入密码与近期历史密码重复");
						}
					}
				}
				String searchSql = "select bo from AdminAuthAccount bo where 1=1 and bo.accountName = ?1";
				Query query = em.createQuery(searchSql);
				query.setParameter(1, userId);
				List<AdminAuthAccount> result = (List<AdminAuthAccount>)query.getResultList();
				for(AdminAuthAccount aapl : result){
					if(!aapl.getId().equals("")){
						AdminAuthPasswordLog ws = new AdminAuthPasswordLog();//保存密码至历史密码记录表
						ws.setUpdateUser(updateUser);
						ws.setUserId(userId);
						ws.setPswdUped(passwordEncode);
						ws.setPswdUpTime(new Date(System.currentTimeMillis()));
						this.em.persist(ws);
					
						AdminAuthAccount wm = em.find(AdminAuthAccount.class,Long.valueOf(aapl.getId()));//从用户表修改密码
						wm.setPassword(passwordEncode);
						wm.setTemp2("1");	//设置密码修改标志为1已修改
						this.em.merge(wm);
					}
				}
			
			}else{//若用户输入原密码有误
				message = "原密码输入有误，请重新输入!"; 
				throw new BizException(1,0,"0001",message);
			}
		}
		
		this.getUserSession().setPassword(passwordEncode);

	}

	

}
