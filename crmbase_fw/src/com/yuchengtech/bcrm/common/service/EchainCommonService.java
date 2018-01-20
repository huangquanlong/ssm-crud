package com.yuchengtech.bcrm.common.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecc.echain.workflow.engine.EVO;
import com.ecc.echain.workflow.engine.WfStatistic;
import com.ecc.echain.workflow.engine.WorkFlowClient;
import com.ecc.echain.workflow.engine.WorkList;
import com.ecc.echain.workflow.exception.WFException;
import com.yuchengtech.bcrm.model.WfInstanceWholeProperty;
import com.yuchengtech.bob.common.CommonService;
import com.yuchengtech.bob.common.JPABaseDAO;
import com.yuchengtech.bob.vo.AuthUser;
import com.yuchengtech.crm.exception.BizException;

/**
 * echain公共service
 * 
 * @author km 20130411
 * @update 20140923,helin,增加处理工作流提交时获取动态调用类的错误信息并返回至前台
 */
@Service
public class EchainCommonService extends CommonService {

	public EchainCommonService() {
		JPABaseDAO<WfInstanceWholeProperty, String> baseDao = new JPABaseDAO<WfInstanceWholeProperty, String>(
				WfInstanceWholeProperty.class);
		super.setBaseDAO(baseDao);
	}

//	@Autowired
//	@Qualifier("dsOracle")
//	private DataSource ds;

	/**
	 * 将当前用户编号\所属机构号\数据库连接传入EVO对象
	 * 
	 * @param vo
	 * @return vo
	 * @throws SQLException
	 */
	protected EVO setSessionInfo(EVO vo) throws SQLException {
		AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		vo.setCurrentUserID(auth.getUserId());// 当前用户
		vo.setOrgid(auth.getUnitId());// 当前机构号
		vo.setSysid("echaindefault"); //20131125,默认sysid
//		vo.setConnection(ds.getConnection()); // 调用统一的connection
		if (vo.getCommentVO() != null) {// 如果意见信息不为空,则将session信息等也放入commentVO中
			vo.getCommentVO().setInstanceID(vo.getInstanceID()); // 实例号
			vo.getCommentVO().setOrgid(vo.getOrgid()); // 当前机构号
			vo.getCommentVO().setUserID(vo.getCurrentUserID()); // 当前用户
			vo.getCommentVO().setNodeID(vo.getNodeID()); // 当前节点id
		}
		return vo;
	}

	/**
	 * 取得用户全部待办列表
	 * 
	 * @param limit
	 * @param start
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * 
	 * @throws SQLException
	 */
	public Vector getUserAllTodoWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserAllTodoWorkList(vo);

		return vect;
	}

	/**
	 * 完成当前节点办理，提交工作任务
	 * 
	 * @return EVO * Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;Tip ：结果提示信息
	 *         Nextnodeid ：下一节点id; Nextnodename ：下一节点名称; Nextnodeuser ：下一办理人;
	 *         SendSMSSign ：通知方式：0.不通知;1.消息通知;2.邮件通知;3.短信通知;4.所有方式通知
	 * 
	 * @throws SQLException
	 */
	public EVO wfCompleteJob(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		WorkFlowClient wfc = WorkFlowClient.getInstance();
		try {
			vo = wfc.wfCompleteJob(vo);
		} catch (Exception e) {
			/**
			 * helin 于20140923增加工作流调用将其错误信息返回至前台界面,以用于展示出来
			 */
			if(e.getCause() instanceof InvocationTargetException){
				InvocationTargetException ite = (InvocationTargetException) e.getCause();
				if(ite.getTargetException() != null){
					if(ite.getTargetException() instanceof BizException){
						throw (BizException)ite.getTargetException();
					}else{
						throw new BizException(1,2,"1002",ite.getTargetException().getMessage());
					}
				}
			}else{
				throw new BizException(1,2,"1002",e.getMessage());
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 调用流程提交API接口
		return vo;
	}

	/**
	 * 保存当前实例
	 * 
	 * @param EVO
	 *            vo里放：InstanceID,CurrentUserID,jobname(可选）,FormFlow(可选）,AppSign(
	 *            可选),paramMap(可选，存放表单数据)
	 * @param UserObject
	 *            (可选，Vector类型，存放应用需要执行的sql语句)
	 * @param SuggestContent
	 *            (可选,意见内容）
	 * @param SuggestControl
	 *            （obj.getTip()）:意见查看权限（可选,"1"放开查看意见权限,其他的为加密）
	 * @param connection
	 *            （可选）
	 * @return EVO,包含：sign,tip,wfsign,instanceid,formid
	 * @throws SQLException
	 */
	public EVO wfSaveJob(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		WorkFlowClient wfc = WorkFlowClient.getInstance();
		try {
			vo = wfc.wfSaveJob(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 调用流程提交API接口
		return vo;
	}

	/**
	 * @return echain 版本信息
	 */
	public String getWorkFlowVersion() {
		String version = WorkFlowClient.getInstance().getWorkFlowVersion();
		return version;
	}

	/**
	 * 获取可用流程列表
	 * 
	 * @param vo
	 * @return vector 包括字段:*WFID 工作流ID号 WFName 工作流名称 WFSign 工作流标识 WFDescription
	 *         工作流描述 AppID 应用模块ID AppName 应用模块名称 WFMainForm 流程主表单ID WFAdmin
	 *         流程管理员ID WFReaders 流程阅读者 Author 流程制订者 Tip 流程版本
	 * @throws SQLException
	 * @throws WFException
	 */
	public Vector getWFNameList(EVO vo) throws SQLException, WFException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vector = WorkFlowClient.getInstance().getWFNameList(vo);
		return vector;
	}

	/**
	 * 根据流程id获取所有的流程实例
	 * 
	 * @param vo
	 * @return Vector，里面放置EVO对象，属性包括 WFID 工作流ID号 WFName 工作流名称 WFSign 工作流标识
	 *         WFDescription 工作流描述 AppID 应用模块ID AppName 应用模块名称 WFMainForm
	 *         流程主表单ID WFAdmin 流程管理员ID WFReaders 流程阅读者 Author 流程制订者 Tip 流程版本
	 * @throws SQLException
	 * @throws WFException
	 */
	public Vector getInstanceList(EVO vo) throws SQLException, WFException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vector = WorkFlowClient.getInstance().getInstanceList(vo);
		return vector;
	}

	/**
	 * 启动流程
	 * 
	 * @param vo
	 * @return EVO对象，属性包括 InstanceID 工作流实例号 NodeID 节点ID Formid 表单号
	 * @throws Exception
	 */
	public EVO initializeWFWholeDocUNID(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().initializeWFWholeDocUNID(vo);
		return vo;
	}

	/**
	 * 删除流程实例
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO wfDelInstance(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfDelInstance(vo);
		return vo;
	}

	/**
	 * 重办
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */

	public EVO wfTakeBack(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfTakeBack(vo);
		return vo;
	}

	/**
	 * 打回
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */

	public EVO wfCallBack(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfCallBack(vo);
		return vo;
	}

	/**
	 * 撤办
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO wfCancel(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfCancel(vo);
		return vo;
	}

	/**
	 * 退回
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO wfReturnBack(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfReturnBack(vo);
		return vo;
	}

	/**
	 * 手工催办
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO wfUrge(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfUrge(vo);
		return vo;
	}

	/**
	 * 流程挂起
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO wfHang(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfHang(vo);
		return vo;
	}

	/**
	 * 流程唤醒
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO wfWake(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfWake(vo);
		return vo;
	}

	/**
	 * 设置流程审批状态
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO setSPStatus(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().setSPStatus(vo);
		return vo;
	}

	/**
	 * 获取下一节点列表
	 * 
	 * @param vo
	 * @return EVO对象，属性包括 nodeRouterType:节点流向类型“0”一般处理“1”单选处理“2”多选处理“3”条件处理.
	 *         paramMap,HashMap,哈希表里面对应节点id、节点Map,Map里面放. nodeid nodename
	 *         nodetype routename ifselectuser：1是，其他否. 节点类型nodetype： S：“起始”
	 *         E：“结束” A：“活动” C：“控制”
	 * 
	 * @throws Exception
	 */
	public EVO getNextNodeList(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().getNextNodeList(vo);
		return vo;
	}

	/**
	 * 获取打回节点列表
	 * 
	 * @return List 打回节点列表
	 * @throws SQLException
	 * @throws WFException
	 */
	public List getWFTreatedNodeList(EVO vo) throws SQLException, WFException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		List list = WorkFlowClient.getInstance().getWFTreatedNodeList(vo);
		return list;
	}

	/**
	 * 获取节点办理用户列表
	 * 
	 * @param vo
	 * @return EVO对象，属性包括 : multeitFlag:节点单人或是多人标志1：“单人办理”n：“多人办理”;
	 *         paramMap,HashMap,哈希表里面对应人员id（格式为U.XXX）、名称;
	 * 
	 * @throws Exception
	 */
	public EVO getNodeUserList(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().getNodeUserList(vo);
		return vo;
	}

	/**
	 * 删除所有流程实例
	 * 
	 * @return
	 */
	public boolean ClearAllWFInstance() {
		return WorkFlowClient.getInstance().ClearAllWFInstance();
	}

	/**
	 * 删除流程以及相关实例
	 * 
	 * @param wfid
	 * @return
	 */
	public boolean DelWF(String wfid) {
		return WorkFlowClient.getInstance().DelWF(wfid);
	}

	/**
	 * 删除所有流程以及实例
	 * 
	 * @return
	 */
	public boolean ClearAllWF() {
		return WorkFlowClient.getInstance().ClearAllWF();
	}

	/**
	 * 填写意见提交
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public boolean setComment(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		return WorkFlowClient.getInstance().setComment(vo);

	}

	/**
	 * 获取节点用户输入的意见
	 * 
	 * @param vo
	 * @return EVO对象，属性包括 : commentVO,意见信息VO
	 * 
	 * @throws Exception
	 */
	public EVO getUserComment(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo.setCommentVO(WorkFlowClient.getInstance().getUserComment(vo));
		return vo;
	}

	/**
	 * 获取当前实例所有的意见列表
	 * 
	 * @param vo
	 * @return Vector，里面放置EVO对象，属性包括 ; NodeName 节点名称 ; NodeID 节点id ; UserName
	 *         用户名 ; SuggestTime 填写时间 ; SuggestContent 意见内容
	 * 
	 * @throws Exception
	 */
	public Vector getAllComments(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		return WorkFlowClient.getInstance().getAllComments(vo);
	}

	/**
	 * 流程跳转
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO wfJump(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfJump(vo);
		return vo;
	}

	/**
	 * 获取流程节点列表
	 * 
	 * @return HashMap,哈希表里面对应节点id、节点名称
	 * @throws Exception
	 * 
	 */
	public HashMap getWFNodeList(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		return WorkFlowClient.getInstance().getWFNodeList(vo);
	}

	/**
	 * 获取文本列表流程跟踪信息
	 * 
	 * @param vo
	 * @return Vector，里面放置EVO对象，属性包括 ;NodeName 节点名称 ;NextNodeUser 下一节点用户
	 *         ;UserName 用户名 ;SuggestTime 填写时间 ;SuggestContent 意见内容
	 * 
	 * @throws Exception
	 */
	public Vector getWorkFlowHistory(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		return WorkFlowClient.getInstance().getWorkFlowHistory(vo);
	}

	/**
	 * 转办人员列表
	 * 
	 * @param vo
	 * @return EVO对象，属性包括 : multeitFlag:节点单人或是多人标志1：“单人办理”n：“多人办理”;
	 *         paramMap,HashMap,哈希表里面对应人员id（格式为U.XXX）、名称; Sign：处理结果标识
	 *         0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知; Tip：结果提示信息;
	 * 
	 * @throws Exception
	 */
	public EVO wfChange(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().wfChange(vo);
		return vo;
	}

	/**
	 * 获取可调用子流列表
	 * 
	 * @param vo
	 * @return Vector对象，属性包括 ; WFID 流程id ; WFName 流程名称 ; WFDescription 流程描述 ;
	 *         AppID 模块id ; AppName 模块名 ; WFMainForm 应用主表单
	 * 
	 * @throws Exception
	 */
	public Vector getSubFlow(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		return WorkFlowClient.getInstance().getSubFlow(vo);
	}

	/**
	 * 子流设置完毕
	 * 
	 * @return EVO对象 ，属性包括 ; Sign：处理结果标识 0：成功；1：失败；2：成功但有异常；3：部分成功；4：未知 ;
	 *         Tip：结果提示信息 ; Nextnodeid：下一节点id ; Nextnodename：下一节点名称 ;
	 *         Nextnodeuser：下一办理人
	 * @throws Exception
	 * 
	 */
	public EVO SubFlowSetSubmit(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().SubFlowSetSubmit(vo);
		return vo;
	}

	/**
	 * 取得实例相关属性
	 * 
	 * @param vo
	 * @return EVO.paramMap(HashMap),哈希表里面放 "getNextNodeList",EVO
	 *         "getNodeControlFormField",HashMap
	 *         "getNodeControlFormAction",HashMap "getNodeFormData",HashMap
	 *         "getAllComments",Vector
	 * 
	 * 
	 * @throws Exception
	 */
	public EVO getInstanceInfo(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().getInstanceInfo(vo);
		return vo;
	}

	/**
	 * 取得用户待办工作列表
	 * 
	 * @param limit
	 * @param start
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * 
	 * @throws SQLException
	 */
	public Vector getUserTodoWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserTodoWorkList(vo);

		return vect;
	}

	/**
	 * 取得用户已办工作列表
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getUserDoneWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserDoneWorkList(vo);
		return vect;
	}

	/**
	 * 取得用户全部已办工作列表
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getUserAllDoneWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserAllDoneWorkList(vo);
		return vect;
	}

	/**
	 * 取得用户有权限的文件列表（按模块）
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getUserAvailableWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserAvailableWorkList(vo);
		return vect;
	}

	/**
	 * 取得用户所有有权限的文件列表
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getUserAllAvailableWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserAllAvailableWorkList(vo);
		return vect;
	}

	/**
	 * 取得用户全部抄送列表
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getUserAllAnnounceWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserAllAnnounceWorkList(vo);
		return vect;
	}

	/**
	 * 取得异常状态文档列表
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getExceptionWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getExceptionWorkList(vo);
		return vect;
	}

	/**
	 * 取得已办结文件视图
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getWFStatusEndWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getWFStatusEndWorkList(vo);
		return vect;
	}

	/**
	 * 取得用户工作列表
	 * 
	 * @param EVO
	 *            vo里放：CurrentUserID,formid(可选),WFSign(可选),NodeStatus(可选),fromRow
	 *            (可选),toRow(可选),bDraft(可选)
	 * @param NodeID
	 *            (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap
	 *            (可选，存放查询条件，如:name=ljy;key=2)
	 * @return VECTOR对象，属性包括 ;InstanceID：流程实例号 ;WFID：流程ID ;WFName：流程名称
	 *         ;WFSign：流程标识 ;AppID：应用模块pk ;AppName：应用模块名称 ;JobName：工作任务名称
	 *         ;Appsign：应用数据标识 ;PreNodeID：前一节点ID ;PreNodeName：前一节点名称
	 *         ;NodeID：节点ID ;NodeName：节点名称（流程当前所处状态） ;WFStatus：流程状态 0：流转中 1：流程结束
	 *         2：流程挂起 3：流程异常中止 ;NodeStatus：节点状态 0：正常办理 1：催办 2：办理结束
	 *         ;CurrentNodeUser：当前节点办理人 ;Originaluser：原始办理人 ;Author：流程启动者
	 *         ;PreUser：上一节点办理人 ;NodePlanEndTime：节点办理时限 ;NodeStartTime：待办产生时间
	 *         ;WFArchiveEndTime：归档时间 ;Tip：流程阅读者 ;totalRow：总记录数
	 * @throws SQLException
	 */
	public Vector getUserWorkList(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		Vector vect = WorkList.getInstance().getUserWorkList(vo);
		return vect;
	}

	/**
	 * 个人待办任务统计
	 * 
	 * @throws SQLException
	 */
	public Map StatUserTodo(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		WfStatistic wf = new WfStatistic();
		HashMap map = wf.StatUserTodo(vo);
		return map;
	}

	/**
	 * 个人已办任务统计
	 * 
	 * @throws SQLException
	 */
	public List StatUserDone(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		WfStatistic wf = new WfStatistic();
		List list = wf.StatUserDone(vo);
		return list;
	}

	/**
	 * 过期任务统计
	 * 
	 * @throws SQLException
	 */
	public Map StatOverdue(EVO vo) throws SQLException {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		WfStatistic wf = new WfStatistic();
		HashMap map = wf.StatOverdue(vo);
		return map;
	}

	/**
	 * 实例签收
	 * @param InstanceID
	 *            实例号
	 * @return EVO,包含：sign,tip
	 * @throws Exception
	 */
	public EVO instanceSignIn(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().instanceSignIn(vo);
		return vo;
	}
	
	/**
	 * 撤销签收
	 * @param InstanceID
	 *            实例号
	 * @return EVO,包含：sign,tip
	 * @throws Exception
	 */
	public EVO instanceSignOff(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		vo = WorkFlowClient.getInstance().instanceSignOff(vo);
		WorkFlowClient.getInstance().taskSignOff(vo);
		return vo;
	}
	
	/**
	 * 撤销任务认领
	 * @param InstanceID
	 *            实例号
	 * @return EVO,包含：sign,tip
	 * @throws Exception
	 */
	public EVO taskSignOff(EVO vo) throws Exception {
		setSessionInfo(vo); // 将当前用户编号\所属机构号\数据库连接传入EVO对象
		WorkFlowClient.getInstance().taskSignOff(vo);
		return vo;
	}
}
