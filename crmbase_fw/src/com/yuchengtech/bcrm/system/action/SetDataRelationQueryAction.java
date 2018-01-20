package com.yuchengtech.bcrm.system.action;


import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

/**
 * 数据集关联关系查询
 * @author songxs
 * @since 2012-12-5
 */
@ParentPackage("json-default")
@Action(value="/setDateRelationQuery-action", results={
    @Result(name="success", type="json")
})
public class SetDataRelationQueryAction extends CommonAction{
    
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
		
	

    @Override
    public void prepare() {
        StringBuffer builder = new StringBuffer("select c.*," +
        		" t.value as join_left_name,t.name as join_left_table_name," +
        		" t1.value as join_right_name,t1.name as join_right_table_name," +
        		" t2.col_name_e as join_left_col_name,t2.col_name_c as join_left_col_remark," +
        		" t3.col_name_e as join_right_col_name,t3.col_name_c as join_right_col_remark " +
        		" from OCRM_F_A_SS_RELATION c " +
        		" left join MTOOL_DBTABLE t on t.id = c.join_left_table " +
        		" left join MTOOL_DBTABLE t1 on t1.id = c.join_right_table " +
        		" left join MTOOL_DBCOL t2 on t2.id = c.join_left_col " +
        		" left join MTOOL_DBCOL t3 on t3.id = c.join_right_col " +
        		" where 1=1 ");

        for (String key : getJson().keySet()){
            String value = getJson().get(key).toString();
            if (! "".equals(value)) {
                if("JOIN_LEFT_TABLE".equals(key))
                    builder.append(" and c." + key + " like " + "'%" + value + "%'");
                else if("JOIN_RIGHT_TABLE".equals(key))
                    builder.append(" and c." + key + " like " + "'%" + value +"%'");
        /*        else if("START_DATE_FROM".equals(key))
                    builder.append(" and c.START_DATE >= to_date('" + value + "', 'YYYY-MM-DD') ");  
                else if("END_DATE_TO".equals(key))
                    builder.append(" and c.START_DATE <= to_date('" + value + "', 'YYYY-MM-DD') ");    */
            }
        }
        setPrimaryKey("c.id");
        // 设置父类中的信息
        SQL = builder.toString();
        datasource = ds;
    }
    
}
