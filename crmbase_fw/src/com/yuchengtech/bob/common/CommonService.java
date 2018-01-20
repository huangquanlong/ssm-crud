package com.yuchengtech.bob.common;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.ecc.echain.workflow.engine.EVO;
import com.ecc.echain.workflow.engine.WFI;
import com.ecc.echain.workflow.engine.WfEngine;
import com.ecc.echain.workflow.engine.WorkFlowClient;
import com.yuchengtech.bob.vo.AuthUser;

@Transactional(value="postgreTransactionManager")
public class CommonService{
	
	protected EntityManager em ;
	public EntityManager getEntityManager(){
		return this.em;
	}
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
        this.baseDAO.setEntityManager(em);		
		this.em = em;
	}

	
	@SuppressWarnings("unchecked")
	protected JPABaseDAO baseDAO = null;
	@SuppressWarnings("unchecked")
	public JPABaseDAO getBaseDAO() {
		return baseDAO;
	}

	@SuppressWarnings("unchecked")
	public void setBaseDAO(JPABaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	// 查询所有记录
	public List<?> findAll() {
		return baseDAO.getAll();
	}

	// 按照JqL进行查询
	@SuppressWarnings("unchecked")
	public List findByJql(String jql,Map<String,Object>values) {
		return baseDAO.findWithNameParm(jql, values);
	}
	// 按照JqL进行分页查询
	@SuppressWarnings("unchecked")
	public Map<String, Object> findPageByJql(int firstResult,int maxResult,String jql,Map<String,Object>values) {
		Map<String, Object> results = new HashMap<String, Object>();
		SearchResult searchResult=baseDAO.findPageWithNameParam(firstResult, maxResult, jql, values);
		results.put("data",searchResult.getResult());
		results.put("count",searchResult.getTotalCount());
		return results;
	}
	
	 //根据ID是否为空进行新增或者修改
	@SuppressWarnings("unchecked")
	public Object save(Object obj) {
		return baseDAO.save(obj);
	}

	// 删除一条记录
    @SuppressWarnings("unchecked")
	public void remove(Object id) {
    	 Object obj = find(id);
        	if (obj != null) {
        		baseDAO.remove(obj);
            
		}
    }
    
	//批量删除
	@SuppressWarnings("unchecked")
	public void batchRemove(String idStr) {
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			Object obj = find(id);
			if (obj != null) {
				baseDAO.remove(obj);

			}
		}
	}
	/**
	 * 执行JQL进行批量修改/删除操作.
	 * 
	 * @param jql 更新或删除语句
	 * For Example : 
	 * UPDATE PollOption p SET p.optionItem = :value WHERE p.optionId = :optionId
	 * values.put("value","aaa");
	 * values.put("optionId",1);
	 * UPDATE PollOption p SET p.optionItem = :value WHERE p.optionId in (1,2,3)
	 * DELETE FROM PollOption p WHERE p.optionId = :optionId 
	 * DELETE FROM PollOption p WHERE p.optionId in (1,2,3,4)
	 * @param values  map(参数、参数值)
	 *     
	 * @return 更新记录数.*/
	@SuppressWarnings("unchecked")
	public int batchUpdateByName(String jql,Map<String,Object>values) {
		return baseDAO.batchExecuteWithNameParam(jql, values);
	}
    //获取唯一对象
	@SuppressWarnings("unchecked")
	public  Object find(Object id) {
		return baseDAO.get((Serializable) id);
	}
	/**
     * 获取当前用户session
     */
    public AuthUser getUserSession() {
    	return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    /**
	 * 通过wfid发起流程,实例号自动生成,创建echain工作流实例
	 * 
	 * @param wfId
	 *            流程标识id
	 * @param jobName
	 *            任务名称
	 * @param paramMap
	 *            存放流程变量
	 * @return EVO
	 * @throws Exception
	 */
	public EVO initWorkflowByWfid(String wfId, String jobName, Map paramMap)
			throws Exception {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		WfEngine we = WfEngine.getInstance();
		EVO vo = new EVO();
		try {
			vo.setWFID(wfId);
			vo.setCurrentUserID(auth.getUserId());
			vo.setOrgid(auth.getUnitId());
			if ((jobName != null) && (jobName.length() > 0))
				vo.setJobName(jobName);
			if (paramMap != null)
				vo.paramMap.putAll(paramMap);
			vo = we.initializeWFWholeDocUNID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	/**
	 * 通过wfid发起流程,实例号用传参的方式获取,创建echain工作流实例
	 * 
	 * @param wfId
	 *            流程标识id
	 * @param jobName
	 *            任务名称
	 * @param paramMap
	 *            存放流程变量
	 * @param instanceId
	 *            实例ID
	 * @return EVO
	 * @throws Exception
	 */
	public EVO initWorkflowByWfidAndInstanceid(String wfId, String jobName,
			Map paramMap, String instanceId) throws Exception {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		WfEngine we = WfEngine.getInstance();
		EVO vo = new EVO();
		try {
			vo.setWFID(wfId);
			vo.setCurrentUserID(auth.getUserId());
			vo.setOrgid(auth.getUnitId());
			if ((instanceId != null) && (instanceId.length() > 0))
				vo.setInstanceID(instanceId);
			// vo.setBizseqno(bizseqno);
			if ((jobName != null) && (jobName.length() > 0))
				vo.setJobName(jobName);
			if (paramMap != null)
				vo.paramMap.putAll(paramMap);
			vo = we.initializeWFWholeDocUNID(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	/**
	 * 通过wfsign发起流程,实例号自动生成,创建echain工作流实例
	 * 
	 * @param ds
	 *            数据源
	 * @param wfsign
	 *            任务标识
	 * @param jobName
	 *            任务名称
	 * @param paramMap
	 *            存放流程变量
	 * @return hm key:instanceid 返回的流程实例号;nodeid 返回的节点ID;formid 返回的表单ID
	 * @throws Exception
	 */
	public Map initWorkflowByWfsign(String wfsign, String jobName, Map paramMap)
			throws Exception {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Map hm = null;
		WFI wfi = new WFI();// 工作流统一调用接口
		try {
			// hm key:instanceid 返回的流程实例号;nodeid 返回的节点ID;formid 返回的表单ID
			hm = wfi.initWFBySign(wfsign, auth.getUserId(), null, null,
					jobName, auth.getUnitId(), paramMap, null);
		} catch (Exception e) {
			System.out.println("调用工作流引擎接口initWFBySign()异常，信息如下：");
			e.printStackTrace();
		}
		return hm;
	}

	/**
	 * 通过wfsign发起流程,实例号用传参的方式获取,创建echain工作流实例
	 * 
	 * @param wfsign
	 *            任务标识
	 * @param jobName
	 *            任务名称
	 * @param paramMap
	 *            存放流程变量
	 * @param instanceId
	 *            实例ID
	 * @return hm key:instanceid 返回的流程实例号;nodeid 返回的节点ID;formid 返回的表单ID
	 * @throws Exception
	 */
	public Map initWorkflowByWfsignAndInstanceid(String wfsign, String jobName,
			Map paramMap, String instanceId) throws Exception {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		WFI wfi = new WFI();// 工作流统一调用接口
		Map hm = null;
		try {
			// hm key:instanceid 返回的流程实例号;nodeid 返回的节点ID;formid 返回的表单ID
			hm = wfi.initWFBySign(wfsign, auth.getUserId(), instanceId, null,
					jobName, auth.getUnitId(), paramMap, null);
		} catch (Exception e) {
			System.out.println("调用工作流引擎接口initWFBySign()异常，信息如下：");
			e.printStackTrace();
		}
		return hm;

	}

	/**
	 * 流程提交
	 * 
	 * @param instanceId
	 *            ：流程实例号
	 * @param nodeId
	 *            节点ID（可选）
	 * @param nextNodeId
	 *            ：下一节点ID（可选）
	 * @param nextNodeUser
	 *            ：下一节点办理人（可选）形如 : U.admin
	 * @param paramMap
	 *            ：流程参数（可选,_emp_context,SuggestContent,SuggestControl）
	 * @return EVO
	 * @throws Exception
	 */
	public EVO wfCompleteJob(String instanceId, String nodeId,
			String nextNodeId, String nextNodeUser, Map paramMap)
			throws Exception {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		WorkFlowClient wfc = WorkFlowClient.getInstance();
		EVO vo = new EVO();
		vo.setInstanceID(instanceId);
		if ((nodeId != null) && (nodeId.length() > 0))
			vo.setNodeID(nodeId);
		vo.setCurrentUserID(auth.getUserId());
		vo.setOrgid(auth.getUnitId());
		if ((nextNodeId != null) && (nextNodeId.length() > 0))
			vo.setNextNodeID(nextNodeId);
		if ((nextNodeUser != null) && (nextNodeUser.length() > 0))
			vo.setNextNodeUser(nextNodeUser);
		if (paramMap != null)
			vo.paramMap.putAll(paramMap);
		if ((paramMap != null) && (!(paramMap.isEmpty()))
				&& (paramMap.get("AnnounceUser") != null)) {
			vo.setAnnounceUser((String) paramMap.get("AnnounceUser"));
		}
		vo = wfc.wfCompleteJob(vo);
		return vo;
	}
	
}
