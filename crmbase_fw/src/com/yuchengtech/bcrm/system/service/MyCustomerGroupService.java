package com.yuchengtech.bcrm.system.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yuchengtech.bcrm.system.model.OcrmFCmAccordionCust;
import com.yuchengtech.bcrm.system.model.OcrmFCmAccordionGroup;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;

/**
 * 客户经理快捷工作台 service
 * @author wz
 * @since 2012-11-7
 */
@Service
public class MyCustomerGroupService extends CommonService {

	/**
	 * 创建baseDAO
	 */
	public MyCustomerGroupService() {
		JPABaseDAO<OcrmFCmAccordionGroup, Long> baseDAO = new JPABaseDAO<OcrmFCmAccordionGroup, Long>(
				OcrmFCmAccordionGroup.class);
		super.setBaseDAO(baseDAO);
	}
	
	/**
	 * 保存群组名称
	 * @param model 操作对象
	 * @return success
	 */
	public Object save(Object model){
		
		OcrmFCmAccordionGroup afg = (OcrmFCmAccordionGroup)model;
		AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = auth.getUserId();
		afg.setUserId(userId);//登陆者用户ID
		OcrmFCmAccordionGroup ag = (OcrmFCmAccordionGroup)super.save(afg);
		String gId = ag.getId();
		if(gId != null){
			ag.setId("G"+ag.getId());//群组表主键id
			ag.setGroupId(gId);//群组id
		}
		return super.save(ag);
	}
	
	/**
	 * 保存的移动节点
	 * @param nodeId 移动的节点
	 * @param oldParentId 移动节点的 旧父节点
	 * @param newParentId 移动节点的 新父节点
	 * @return void
	 * */
	public void move(String nodeId, String oldParentId, String newParentId){
		
		Map<String,Object> values=new HashMap<String,Object>();
		JPABaseDAO<OcrmFCmAccordionCust, Long> moveBaseDAO = new JPABaseDAO<OcrmFCmAccordionCust, Long>(
				OcrmFCmAccordionCust.class);
		moveBaseDAO.setEntityManager(em);
		
		String updateJQL = "update OcrmFCmAccordionCust a set a.groupId ='"+newParentId.split("_")[0]+"' where a.id = '"+nodeId+"'";
		moveBaseDAO.batchExecuteWithNameParam(updateJQL, values);//执行修改节点的父节点操作
		
	}
	
	/**
	 * 保存的移动节点
	 * @param nodeId 移动的节点
	 * @param oldParentId 移动节点的 旧父节点
	 * @param newParentId 移动节点的 新父节点
	 * @return void
	 * */
	public void addNewGroup(String nodeId, String newParentId){
		
		OcrmFCmAccordionCust afac = new OcrmFCmAccordionCust();
		JPABaseDAO<OcrmFCmAccordionCust, Long> moveBaseDAO = new JPABaseDAO<OcrmFCmAccordionCust, Long>(
				OcrmFCmAccordionCust.class);
		moveBaseDAO.setEntityManager(em);
		afac.setCustId(nodeId);
		afac.setGroupId(newParentId.split("_")[0]);//newParentId格式为：G3_add
		moveBaseDAO.save(afac);
		
	}
}
