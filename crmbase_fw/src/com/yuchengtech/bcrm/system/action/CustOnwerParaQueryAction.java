package com.yuchengtech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;
@ParentPackage("json-default")
@Action(value="/custOnwerParaQuery", results={
    @Result(name="success", type="json"),
})
/***
 * 客户归属参数设置-查询
 */
public class CustOnwerParaQueryAction extends CommonAction{

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	/***
	 * 保存参数设置
	 */
    public void prepare() {
    	StringBuilder sb = new StringBuilder("SELECT T.ID, T.VERSION, T.APP_ID, T.PROP_NAME, T.PROP_DESC, T.PROP_VALUE, T.REMARK FROM FW_SYS_PROP T WHERE T.PROP_NAME LIKE 'CustOnwerPara%'");
        setPrimaryKey("T.ID");
        SQL=sb.toString();
        datasource = ds;
    }
}
