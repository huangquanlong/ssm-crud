package com.yuchengtech.bob.action;

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
import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.bob.vo.AuthUser;

@ParentPackage("json-default")
@Action(value = "/queryremindlist", results = { @Result(name = "success", type = "json") })
public class QueryRemindListAction extends CommonAction {

    @Autowired
    @Qualifier("dsOracle")
    private DataSource ds;

    private HttpServletRequest request;

    public void prepare() {
        ActionContext ctx = ActionContext.getContext();
        request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        String msgTyp = request.getParameter("MSG_TYP");
        AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
        String userId = auth.getUserId(); 
        StringBuilder sb = new StringBuilder(
                "select t.* from ocrm_f_wp_remind t where t.USER_NO = '" +
                userId+"' and to_date(t.MSG_END_DATE,'YYYY-MM-DD') >= SYSDATE "); //for oracle\DB2
        		//userId+"' and to_date(t.MSG_END_DATE,'yyyy-MM-dd') >= SYSDATE "); //for db2 9.7
        if (msgTyp!=null&&!msgTyp.equals("")) {
            sb.append(" and t.msg_typ = '"+msgTyp+"' ");
        }
        for (String key : this.getJson().keySet()) {
            if (null != this.getJson().get(key)
                    && !this.getJson().get(key).equals("")) {
                if (key.equals("MSG_TYP")||key.equals("MSG_STS"))
                    sb.append(" and t." + key + " = '"
                            + this.getJson().get(key) + "'");
                else if (key.equals("MSG_END_DATE")||key.equals("MSG_LAST_DATE"))
                    sb.append(" and t.MSG_END_DATE = '"
                            + this.getJson().get(key).toString().substring(0,10) + "' ");
            }
        }

        SQL = sb.toString();
        setPrimaryKey("t.ID");
        addOracleLookup("MSG_TYP", "REMIND_TYPE");
        datasource = ds;
    }

}
