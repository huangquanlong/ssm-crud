package com.yuchengtech.bcrm.system.service;

import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.AdminAuthOrg;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;

/**
 * 机构管理-查询、维护、新增
 * 
 * @author lixb
 * @since 2012-09-20
 */
@Service
public class SystemOrganizationService extends CommonService {

	public SystemOrganizationService() {
		JPABaseDAO<AdminAuthOrg, Long> baseDAO = new JPABaseDAO<AdminAuthOrg, Long>(
				AdminAuthOrg.class);
		super.setBaseDAO(baseDAO);
	}

	/**
	 * 按照机构ID:orgId查询对象
	 * 
	 * @param orgId
	 * @param value
	 * @return
	 */
	public Object findUniqueByOrgId(String orgId, Object value) {
		return super.baseDAO.findUniqueByProperty(orgId, value);
	}

	/**
	 * 按照机构Name:orgName查询对象
	 * 
	 * @param orgName
	 * @param value
	 * @return
	 */
	public Object findUniqueByOrgName(String orgName, Object value) {
		return super.baseDAO.findUniqueByProperty(orgName, value);
	}

}
