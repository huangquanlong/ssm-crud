package com.yuchengtech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmFSysCredentialStrategy;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
/***
 * 客户归属参数设置
 * @author zhangmin
 *
 */
@Service
public class UserApproveTacticsService extends CommonService {
	
	public UserApproveTacticsService(){
		JPABaseDAO<OcrmFSysCredentialStrategy,Long> baseDao = new JPABaseDAO<OcrmFSysCredentialStrategy,Long>(OcrmFSysCredentialStrategy.class);
		super.setBaseDAO(baseDao);
	}
	/***
	 * 保存客户归属参数
	 * @param DataStr 参数
	 * @return
	 */
	public String saveData(String DataStr){
		
		String[] tacticsArr = DataStr.split(";");
		for (int i = 0; i < tacticsArr.length; i++) {
			String[] itemsArr = tacticsArr[i].split(",");
			//for (int j = 0; j < itemsArr.length; j++) {
				OcrmFSysCredentialStrategy o = (OcrmFSysCredentialStrategy)em.find(OcrmFSysCredentialStrategy.class, Integer.parseInt(itemsArr[0]));
				if(itemsArr[3].equals("1"))
				{
					o.setDetail(itemsArr[1]);
					o.setActiontype(itemsArr[2]);
					o.setEnableFlag(Integer.parseInt(itemsArr[3]));
					em.merge(o);
				}
				else
				{
					o.setDetail("");
					o.setActiontype("");
					o.setEnableFlag(Integer.parseInt(itemsArr[3]));
					em.merge(o);
				}
			//}
		}
		return "success";
	}
}

