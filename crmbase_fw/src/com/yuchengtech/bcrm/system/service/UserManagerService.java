package com.yuchengtech.bcrm.system.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthAccount;
import com.yuchengtech.bcrm.system.model.AdminAuthPasswordLog;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.constance.EndecryptUtils;
import com.yuchengtech.crm.constance.SystemConstance;
import com.yuchengtech.crm.exception.BizException;

/**
 * 用户管理Service
 * @author wangwan
 * @since 2012-10-08 
 */
@Service
public class UserManagerService extends CommonService {
   
	public UserManagerService(){
		JPABaseDAO<AdminAuthAccount, Long>  baseDAO = new JPABaseDAO<AdminAuthAccount, Long>(AdminAuthAccount.class);  
		super.setBaseDAO(baseDAO);
	}


   /*
    * 保存用户基本信息方法
    * @return
    * @throws Exception
    */
	public AdminAuthAccount saveBaseInfo(AdminAuthAccount addAcount) throws ParseException {
		AuthUser auth = (AuthUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("accountName", addAcount.getAccountName());
		List<?> list = super.findByJql("select t from AdminAuthAccount t where t.accountName=:accountName", values);
		if(list != null && list.size() > 0){
			throw new BizException(1,0,"","登录名已存在!");
		}
		if(!"".equals(addAcount.getPassword())){//若已为新增用户设置密码，则将新密码保存至密码修改历史表
			String passwordEncode=EndecryptUtils.encrypt(addAcount.getPassword());
			AdminAuthPasswordLog ws = new AdminAuthPasswordLog();//保存密码至历史密码记录表
			ws.setUpdateUser(auth.getUserId());
			ws.setUserId(addAcount.getAccountName());
			ws.setPswdUped(passwordEncode);
			ws.setPswdUpTime(new Date(System.currentTimeMillis()));
			super.save(ws);
		
			addAcount.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			addAcount.setPassword(passwordEncode);
			return (AdminAuthAccount) super.save(addAcount);
		}else{
			addAcount.setAppId(SystemConstance.LOGIC_SYSTEM_APP_ID);
			return (AdminAuthAccount) super.save(addAcount);
		}
	}
}
