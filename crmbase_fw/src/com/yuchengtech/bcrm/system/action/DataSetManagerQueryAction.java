package com.yuchengtech.bcrm.system.action;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.yuchengtech.bob.core.QueryHelper;
import com.yuchengtech.crm.constance.SystemConstance;
/**
 * 数据集查询
 * @author Sena
 */
@ParentPackage("json-default")
@Action(value = "/datasetmanagerquery", results = { @Result(name = "success", type = "json"), })
public class DataSetManagerQueryAction {
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	private HttpServletRequest request;

	
	private Map<String, Object> JSON;

	public Map<String, Object> getJSON() {
		return JSON;
	}

	public void setJSON(Map<String, Object> jSON) {
		JSON = jSON;
	}

	public String index() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		try {
			StringBuilder sb = new StringBuilder("select * from MTOOL_DBTABLE where PARENT_ID='0'");
			setJSON(new QueryHelper(sb.toString(), ds.getConnection()).getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	public String queryDataSetSolution()  {
		try {
			StringBuilder sb = new StringBuilder("");
			if("DB2".equals(SystemConstance.DB_TYPE)){
				sb.append("SELECT ROW_NUMBER () OVER (ORDER BY 1) AS key,t1.tabname AS value from SYSCAT.TABLES t1 where TABSCHEMA='CRM'");
							}
			else if("ORACLE".equals(SystemConstance.DB_TYPE)){
				
				sb.append("select rownum AS key,tb AS value from(select TABLE_NAME AS tb from user_tables  union select VIEW_NAME as tb from user_views  )");

			}
			setJSON(new QueryHelper(sb.toString(), ds.getConnection())
					.getJSON());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
	}
	//db2版本的
	public String queryDataSetColumn() {
			ActionContext ctx = ActionContext.getContext();
			request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
			if("0".equals(request.getParameter("operate").toString())){
			try {
				StringBuilder sb = new StringBuilder("");
				if("DB2".equals(SystemConstance.DB_TYPE)){
					sb.append("	select t2.NAME,t2.REMARKS,t2.COLTYPE,t2.LENGTH,t2.COLNO,case when t2.NULLS='Y' then '否' else '是' end AS NULLS,case when t2.KEYSEQ IS NULL then '否' else '是'end AS KEYSEQ from (select t1.NAME,t1.REMARKS,t1.COLTYPE,t1.LENGTH,t1.NULLS,t1.COLNO,t1.KEYSEQ from SYSIBM.SYSCOLUMNS t1 where TBCREATOR='CRM' and tbname='"+request.getParameter("tbname")+"' order by t1.colno) t2");
				}
				else if("ORACLE".equals(SystemConstance.DB_TYPE)){
				
					sb.append("SELECT t4.column_name AS NAME,"+
						" t4.COMMENTS  AS REMARKS,"+
						" t4.DATA_TYPE  AS COLTYPE,"+
						" t4.DATA_LENGTH  AS LENGTH, "+
						" CASE "+
						"   WHEN t4.NULLABLE ='Y' "+
						"   THEN '否'    "+
						"   ELSE '是'  "+
						" END AS NULLS ,  "+
						" CASE   "+
						"   WHEN t4.constraint_type='P'  "+
						"   THEN '是'  "+
						"   ELSE '否'  "+
						" END AS KEYSEQ, "+
						" t4.COLUMN_ID AS COLNO "+
				        " FROM   "+
				        " (SELECT t1.comments,"+
				        "   t2.*,   "+
				        "   t3.CONSTRAINT_TYPE "+
				        " FROM user_col_comments t1 "+
				        " INNER JOIN user_tab_columns t2 "+
				        " ON t1.COLUMN_NAME = t2.COLUMN_NAME  "+
				        " LEFT JOIN  "+
				        "   (SELECT DISTINCT coll.column_name,"+
				        "     con.constraint_type  "+
				        "   FROM user_constraints con, "+
				        "     user_cons_columns coll "+
				        "   WHERE con.constraint_name = coll.constraint_name"+
				        "   AND con.constraint_type  IN('U' ,'P')"+
				        "   ) t3 "+
				        " ON t3.COLUMN_NAME  =t2.column_name"+
				        " WHERE t2.table_name='"+request.getParameter("tbname")+"'"+
				        " AND t1.table_name  ='"+request.getParameter("tbname")+"'"+
				        " )t4  ");
				
				}
				setJSON(new QueryHelper(sb.toString(), ds.getConnection())
						.getJSON());
			} catch (SQLException e) {
				e.printStackTrace();
			}}
			else if("1".equals(request.getParameter("operate").toString())){
				if("0".equals(request.getParameter("updateTyep").toString())){
				try {
					StringBuilder sb = new StringBuilder(
							"	select t1.ID,t1.COL_NAME_E as NAME ,t1.COL_NAME_C as REMARKS,t1.COL_TYPE AS COLTYPE,t1.COL_SIZE AS LENGTH,t1.PRIMARY_KEY_FLAG AS KEYSEQ,t1.IS_NULL AS NULLS ,t1.NOTES,t1.COL_SORT AS COLNO from  MTOOL_DBCOL t1 where t1.DBTABLE_ID='"+request.getParameter("rowId")+"'");
					
					setJSON(new QueryHelper(sb.toString(), ds.getConnection())
							.getJSON());
				} catch (SQLException e) {
					e.printStackTrace();
				}}
				else if("1".equals(request.getParameter("updateTyep").toString())){
					try {
						StringBuilder sb = new StringBuilder("");
						if("DB2".equals(SystemConstance.DB_TYPE)){
							sb.append("	select t2.NAME,t2.REMARKS,t2.COLTYPE,t2.LENGTH,t2.COLNO,case when t2.NULLS='Y' then '否' else '是' end AS NULLS,case when t2.KEYSEQ IS NULL then '否' else '是'end AS KEYSEQ from (select t1.NAME,t1.REMARKS,t1.COLTYPE,t1.LENGTH,t1.NULLS,t1.COLNO,t1.KEYSEQ from SYSIBM.SYSCOLUMNS t1 where TBCREATOR='CRM' and tbname='"+request.getParameter("tbname")+"' order by t1.colno) t2");
						}
						else if("ORACLE".equals(SystemConstance.DB_TYPE)){
						
							sb.append("SELECT t4.column_name AS NAME,"+
								" t4.COMMENTS  AS REMARKS,"+
								" t4.DATA_TYPE  AS COLTYPE,"+
								" t4.DATA_LENGTH  AS LENGTH, "+
								" CASE "+
								"   WHEN t4.NULLABLE ='Y' "+
								"   THEN '否'    "+
								"   ELSE '是'  "+
								" END AS NULLS ,  "+
								" CASE   "+
								"   WHEN t4.constraint_type='P'  "+
								"   THEN '是'  "+
								"   ELSE '否'  "+
								" END AS KEYSEQ, "+
								" t4.COLUMN_ID AS COLNO "+
						        " FROM   "+
						        " (SELECT t1.comments,"+
						        "   t2.*,   "+
						        "   t3.CONSTRAINT_TYPE "+
						        " FROM user_col_comments t1 "+
						        " INNER JOIN user_tab_columns t2 "+
						        " ON t1.COLUMN_NAME = t2.COLUMN_NAME  "+
						        " LEFT JOIN  "+
						        "   (SELECT DISTINCT coll.column_name,"+
						        "     con.constraint_type  "+
						        "   FROM user_constraints con, "+
						        "     user_cons_columns coll "+
						        "   WHERE con.constraint_name = coll.constraint_name"+
						        "   AND con.constraint_type  IN('U' ,'P')"+
						        "   ) t3 "+
						        " ON t3.COLUMN_NAME  =t2.column_name"+
						        " WHERE t2.table_name='"+request.getParameter("tbname")+"'"+
						        " AND t1.table_name  ='"+request.getParameter("tbname")+"'"+
						        " )t4  ");
						
						}
						setJSON(new QueryHelper(sb.toString(), ds.getConnection())
								.getJSON());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			return "success";
		}

}
