package com.yuchengtech.crm.sec.credentialstrategy;


import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.yuchengtech.bcrm.system.model.AdminAuthPasswordLog;
import com.yuchengtech.bcrm.system.service.PasswordChangeLogService;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.sec.common.SystemUserConstance;
/**
 * 强制修改密码策略类
 * @author changzh@yuchengtech.com
 * @date 2012-11-05
 * 
 **/
public class PswModifyStrategy extends CredentialStrategy {
	
	@Autowired
	private PasswordChangeLogService passwordChangeLogService;
	
	PswModifyStrategy () {
		CreStrategyID = SystemUserConstance.CS_PSW_MODIFY_ID;
	}
	
	public void setCreStrategyID (String ID) {
		CreStrategyID = ID;
	}
	
	public boolean doCredentialStrategy (AuthUser userDetails, boolean isAuthenticationChecked) {
		boolean isCredentialStrategy = false;
		if (isAuthenticationChecked) {
			//强制修改密码逻辑
			String searchSql = "select n from AdminAuthPasswordLog n where n.userId =?1 order by n.id desc";
			Query query = passwordChangeLogService.getEntityManager() .createQuery(searchSql);
			query.setParameter(1, userDetails.getUsername());
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<AdminAuthPasswordLog> result = (List<AdminAuthPasswordLog>)query.getResultList();
			Long modifyPeriod = Long.parseLong(this.CreStrategyDetail);
			Long dateNum = new Long(0);
			for(AdminAuthPasswordLog aapl : result){
				Date lastUpdateDate = aapl.getPswdUpTime();
				Date currentDate = new Date();
				dateNum = (currentDate.getTime() - lastUpdateDate.getTime())/(1000*3600*24);
			}
			
			if (dateNum > modifyPeriod) {
				userDetails.setTemp2("");//将密码修改标志设置为空,20141026
				doActionType(ActionType, "您已经"+ this.CreStrategyDetail +"天没有修改密码了，请注意及时更新密码。", userDetails);
				isCredentialStrategy = true;
			}
		}  

		return isCredentialStrategy;
		 
	}

}

