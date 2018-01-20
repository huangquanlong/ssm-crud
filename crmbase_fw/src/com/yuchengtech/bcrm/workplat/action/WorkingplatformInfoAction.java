package com.yuchengtech.bcrm.workplat.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.workplat.model.WorkingplatformInfo;
import com.yuchengtech.bcrm.workplat.service.WorkingplatformInfoService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.common.DataType;
/**
 * 知识库发布处理
 * luyy
 * 2014-04-23
 */
@SuppressWarnings("serial")
@Action("/workingplatformInfo")
public class WorkingplatformInfoAction extends CommonAction {
	@Autowired
	private WorkingplatformInfoService service;
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	@Autowired
	public void init() {
		model = new WorkingplatformInfo();
		setCommonService(service);
		needLog = false;;
	}
	
	public void prepare() {
		StringBuilder sb = new StringBuilder(
				"select p.*,decode(SE.SECTION_NAME,'','全部目录',SE.SECTION_NAME) AS MESSAGE_TYPE_VALUE"
				+ " from ocrm_f_wp_info p  LEFT JOIN OCRM_F_WP_INFO_SECTION SE ON TO_CHAR(SE.SECTION_ID) = p.MESSAGE_TYPE where 1>0 "
			);
		/**-添加知识库发布 查看详情-start*/
		String msgId = request.getParameter("msgId");
		if(msgId !=null && !"".equals(msgId)){
			sb.append(" and p.message_Id = '" +msgId+ "' ");
		}
		//查询本栏目类别及其子类别的数据
		for(String key:this.getJson().keySet()){
            if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
                if((key.equals("MESSAGE_TYPE")||key.equals("MESSAGE_TYPE_VALUE"))&&!"root".equals(this.getJson().get(key))){
                	sb.append(" and p.MESSAGE_TYPE in " +
                			"(select to_char(section_id) from ocrm_f_wp_info_section where 1=1 " +
                			"start with SECTION_ID='"+this.getJson().get(key)+"' " +
                					"connect by parent_section= prior to_char(section_id) )");
                }
                if(key.equals("PUBLISH_DATE")){
                	sb.append(" and p.PUBLISH_DATE = to_date('"+this.getJson().get(key).toString().substring(0,10)+"','yyyy-mm-dd')");
                }
            }
        }
		/**-添加知识库发布 查看详情-end*/
		SQL = sb.toString();
		this.setPrimaryKey(" p.MESSAGE_ID desc");
		datasource = ds;
        configCondition("p.MESSAGE_TITLE","=","MESSAGE_TITLE",DataType.String);
        configCondition("p.MESSAGE_INTRODUCE","=","MESSAGE_INTRODUCE",DataType.String);
        configCondition("p.PUBLISH_USER","=","PUBLISH_USER",DataType.String);
        configCondition("p.PUBLISH_ORG","=","PUBLISH_ORG",DataType.String);
		
	}

	public HttpHeaders findWithType() throws Exception {
		try {
			StringBuilder sb = new StringBuilder(
					"select c from  WorkingplatformInfo c where 1=1 ");
			Map<String, Object> values = new HashMap<String, Object>();
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			if (request.getParameter("start") != null)
				start = new Integer(request.getParameter("start")).intValue();
			if (request.getParameter("limit") != null)
				limit = new Integer(request.getParameter("limit")).intValue();
			String messageType = request.getParameter("messageType");
			if (messageType != null&&messageType.equals("root")==false) {
				if (messageType.length() > 0) {
					sb.append("and c.messageType = :messageType");
					values.put("messageType", messageType);
				}
			} else {
				this.setJson(request.getParameter("ddddd"));
				for (String key : this.getJson().keySet()) {
					sb.append(" and c." + key + " = :" + key);
					values.put(key, this.getJson().get(key));
				}
			}
			
			/**-添加知识库发布 查看详情-start*/
			String msgId = request.getParameter("msgId");
			if(msgId !=null && !"".equals(msgId)){
				sb.append(" and c.messageId = '" +msgId+ "' ");
			}
			/**-添加知识库发布 查看详情-end*/

			return super.indexPageByJql(sb.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// 删除
	public String batchDestroy() {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			long idStr = Long.parseLong(request.getParameter("messageId"));
			String jql = "delete from WorkingplatformInfo c where c.messageId in ("
					+ idStr + ")";
			Map<String, Object> values = new HashMap<String, Object>();
			service.batchUpdateByName(jql, values);
			addActionMessage("batch removed successfully");
			return "success";
	}
	//修改栏目属性表
	public String update_productType() throws Exception {
		try {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx
			.get(ServletActionContext.HTTP_REQUEST);
			String messageType = request.getParameter("messageType");
			String productType = request.getParameter("productType");
			String jql = "update WorkingplatformInfo c set c.productType ='"+productType+"' where c.messageType = ("+ messageType + ")";
			Map<String, Object> values = new HashMap<String, Object>();
			service.batchUpdateByName(jql, values);
			addActionMessage("batch removed successfully");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
