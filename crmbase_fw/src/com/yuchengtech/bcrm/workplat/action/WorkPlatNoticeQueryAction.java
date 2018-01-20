package com.yuchengtech.bcrm.workplat.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bcrm.common.service.OrgSearchService;
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.common.DataType;
import com.yuchengtech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value="/noticequery", results={
    @Result(name="success", type="json"),
})
public class WorkPlatNoticeQueryAction extends CommonAction{
    
    private List<String> orgIds = new ArrayList<String>();
    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;
    @Autowired
    OrgSearchService oss;
    

    /**
     * @describe Create sql for search and export.
     */
    public void prepare(){
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId();
        String orgId = auth.getUnitId();
        
        ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
//		String dateStr = (String)request.getParameter("current");
		String readStatus = (String)request.getParameter("isRead");
		String ifImportant = (String)request.getParameter("ifImportant");
		String noticeDate = (String)request.getParameter("noticeDate");
		String search = (String)request.getParameter("search");
		String current = (String)request.getParameter("current");
		List orgsPath=new ArrayList();
		
		try {
			orgsPath =  (List) oss.searchPathInOrgTree(orgId).get("data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //inOrgs(orgId);
        StringBuilder sb = new StringBuilder("");
        sb.append("Select n.NOTICE_ID,n.NOTICE_TITLE,n.NOTICE_CONTENT,n.NOTICE_LEVEL,(SELECT u2.USERNAME FROM SYS_USERS u2 WHERE u2.USERID=n.PUBLISHER) AS PUBLISHER_NAME,n.PUBLISHER,(SELECT u1.UNITNAME FROM SYS_UNITS u1 WHERE u1.UNITID=n.PUBLISH_ORG) AS PUB_ORG_NAME, n.PUBLISH_ORG,n.IS_TOP,n.PUBLISH_TIME,n.MOD_TYPE,n.PUBLISHED,n.ACTIVE_DATE,n.TOP_ACTIVE_DATE,n.NOTICE_TYPE,(SELECT u2.USERNAME FROM SYS_USERS u2 WHERE u2.USERID=n.CREATOR) AS CREATOR_NAME,n.CREATOR,"
                +"(CASE WHEN EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r1 where r1.USER_ID='"+userId+"' and r1.NOTICE_ID=n.NOTICE_ID) THEN 'red001' ELSE 'red002' END) AS IS_READ, "
                +"(SELECT COUNT(1) FROM OCRM_F_WP_ANNEXE an WHERE an.RELATION_INFO=to_char(n.NOTICE_ID) AND an.RELATIOIN_MOD='notice') as ANN_COUNT "
                +" from ocrm_f_wp_notice n where (n.CREATOR='"+userId+"' ");
        sb.append(" OR (n.PUBLISHED='pub001' AND n.PUBLISH_ORG in ('"+orgId+"'");
        
        
        
        for(Object oId : orgsPath){
        	Map uniMap =(Map)oId;
        	String[] paths = ((String)uniMap.get("UNITSEQ")).split(",");
        	for(String o : paths)
        		sb.append(",'"+o+"'");
        }
        sb.append(")");
        sb.append(")");
        sb.append(")");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String dateStr = df1.format(System.currentTimeMillis()).substring(0, 10);
        if(!"".equals(dateStr) && dateStr != null){
        	//若当前日期不为空
//        	sb.append("and n.PUBLISH_TIME is not null ");
//        	sb.append("and n.PUBLISH_TIME <= to_date('");//for db2 9.7
////        	sb.append("and n.PUBLISH_TIME <= '");//for db2 9.5
//        	sb.append(dateStr);
//        	sb.append("','yyyy-mm-dd') ");//for db2 9.7
////        	sb.append("'");//for db2 9.5
//        	sb.append("and nvl(n.ACTIVE_DATE,to_date('2999-12-31','yyyy-mm-dd')) >= to_date('");//for db2 9.7
////        	sb.append("and nvl(n.ACTIVE_DATE,'2999-12-31') >= '");//for db2 9.5
//        	sb.append(dateStr);
//        	sb.append("','yyyy-mm-dd') ");//for db2 9.7
////        	sb.append("'");//for db2 9.5
        }
        if (!"".equals(readStatus) && readStatus != null && readStatus.equals("red001")){
            sb.append(" AND EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r WHERE n.notice_id=r.notice_id and r.user_id='"+userId+"')");

        }else if(!"".equals(readStatus) && readStatus != null && readStatus.equals("red002")){
            sb.append(" AND EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r WHERE n.notice_id=r.notice_id and r.user_id='"+userId+"')");

        }
//      if(key.equals("PUBLISH_TIME_START")){
////            String st = (String)this.getJson().get(key);
//            sb.append(" and n.PUBLISH_TIME>=to_date('"+st.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
////          sb.append(" and n.PUBLISH_TIME>= '"+st.substring(0, 10)+"'");//for db2 9.5
//        }
//      if(key.equals("PUBLISH_TIME_END")){
//            String et = (String)this.getJson().get(key);
//            sb.append(" and n.PUBLISH_TIME<=to_date('"+et.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
////            sb.append(" and n.PUBLISH_TIME<= '"+et.substring(0, 10)+"'");//for db2 9.5
//        }
        if (!"".equals(dateStr) && dateStr != null && dateStr.equals("1")){
//        	Date currentDate =new Date(limit);
//        	  sb.append(" and n.PUBLISH_TIME>=to_date('"+st.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7

        }
        if(!"".equals(readStatus) && readStatus != null && readStatus.equals("1")){
            sb.append(" AND EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r WHERE n.notice_id=r.notice_id and r.user_id='"+userId+"')");

        }
        if(!"".equals(ifImportant) && ifImportant != null && ifImportant.equals("1")){
            sb.append(" AND N.NOTICE_LEVEL = 'lev001'");

        }
        if(!"".equals(search) && search != null ){
            sb.append(" AND N.NOTICE_TITLE like '%"+search+"%' OR N.NOTICE_CONTENT like '%"+search+"%' ");

        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String str = df.format(System.currentTimeMillis()).substring(0, 10);
        if(!"".equals(current) && current != null ){
        	
        	sb.append(" AND N.PUBLISH_TIME >= to_date('"+str+"', 'yyyy-mm-dd')");

        }
        
        for(String key:this.getJson().keySet()){ 
            if(null!=this.getJson().get(key)&&!this.getJson().get(key).equals("")){
                if(!key.toUpperCase().equals("ISREAD")){
                    if(!key.equals("PUBLISH_TIME_START")&&!key.equals("PUBLISH_TIME_END")){
                        if(key.equals("NOTICE_TITLE")){
                            sb.append(" and n."+key+" like '%"+this.getJson().get(key)+"%' ");
                        }else if(key.equals("PUBLISHER")){
                        	String ss = (String) this.getJson().get(key);
                        	String bb = ss.replace(",", "','");
                            sb.append(" and n."+key+" in ('"+bb+"') ");
                        }
                        else if(key.equals("NOTICE_LEVEL_ORA")){
                            sb.append(" and n.NOTICE_LEVEL = '"+this.getJson().get(key)+"' ");
                        }
                        else if(key.equals("IS_TOP_ORA")){
                            sb.append(" and n.IS_TOP = '"+this.getJson().get(key)+"' ");
                        }
                        }
                        
                    else if(key.equals("PUBLISH_TIME_START")){
                        String st = (String)this.getJson().get(key);
                        sb.append(" and n.PUBLISH_TIME>=to_date('"+st.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
//                      sb.append(" and n.PUBLISH_TIME>= '"+st.substring(0, 10)+"'");//for db2 9.5
                    }
                    else if(key.equals("PUBLISH_TIME_END")){
                        String et = (String)this.getJson().get(key);
                        sb.append(" and n.PUBLISH_TIME<=to_date('"+et.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
//                        sb.append(" and n.PUBLISH_TIME<= '"+et.substring(0, 10)+"'");//for db2 9.5
                    }
                    else if(key.equals("PUBLISHED_ORA")){
                        sb.append(" and n.PUBLISHED='"+this.getJson().get(key)+"' ");
                    }
                    else if(key.equals("PUBLISH_TIME_END")){
                        String et = (String)this.getJson().get(key);
                        sb.append(" and n.PUBLISH_TIME<=to_date('"+et.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
//                        sb.append(" and n.PUBLISH_TIME<= '"+et.substring(0, 10)+"'");//for db2 9.5
                    }
                    else if(key.equals("PUBLISH_TIME_END")){
                        String et = (String)this.getJson().get(key);
                        sb.append(" and n.PUBLISH_TIME<=to_date('"+et.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
//                        sb.append(" and n.PUBLISH_TIME<= '"+et.substring(0, 10)+"'");//for db2 9.5
                    }
                    else if(key.equals("PUBLISH_TIME_END")){
                        String et = (String)this.getJson().get(key);
                        sb.append(" and n.PUBLISH_TIME<=to_date('"+et.substring(0, 10)+"','yyyy-MM-dd') ");//for db2 9.7
//                        sb.append(" and n.PUBLISH_TIME<= '"+et.substring(0, 10)+"'");//for db2 9.5
                    }
                  
                }else{
                    if(this.getJson().get(key).equals("red001"))
                        sb.append(" AND EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r WHERE n.notice_id=r.notice_id and r.user_id='"+userId+"')");
                    else if(this.getJson().get(key).equals("red002"))
                        sb.append(" AND NOT EXISTS (SELECT 1 FROM OCRM_F_WP_NOTICE_READ r WHERE n.notice_id=r.notice_id and r.user_id='"+userId+"')");
                    else continue;
                }
            }
        }
        SQL=sb.toString();
        setPrimaryKey("n.NOTICE_ID , n.PUBLISH_TIME ,n.ACTIVE_DATE DESC");
        addOracleLookup("NOTICE_LEVEL", "NOTICE_LEV");
        addOracleLookup("IS_READ", "READ_FLAG");
        addOracleLookup("IS_TOP", "IF_FLAG");
        addOracleLookup("PUBLISHED", "NOTICE_PUB");
        datasource = ds;
    	configCondition("n.NOTICE_LEVEL","=","NOTICE_LEVEL",DataType.String);
   	 	configCondition("n.PUBLISHED","=","PUBLISHED",DataType.String);
		configCondition("n.ACTIVE_DATE","<=","ACTIVE_DATE",DataType.Date);
		configCondition("n.IS_TOP","=","IS_TOP",DataType.String);
//		configCondition("n.PUBLISHER_NAME","=","PUBLISHER_NAME",DataType.String);

    }
}
