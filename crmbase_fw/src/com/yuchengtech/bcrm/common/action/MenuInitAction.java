package com.yuchengtech.bcrm.common.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;
import com.yuchengtech.crm.constance.SystemConstance;

/**
 * 
 * @author songxs
 * @since 2012-9-23
 */
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Action(value="/menuInit", results={
    @Result(name="success", type="json"),
})
public class MenuInitAction extends CommonAction{
	
	//数据源
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;
	
	/**
	 * 
	 */
	public void prepare(){
		StringBuffer sb = new StringBuffer();
		sb.insert(0, "SELECT c.*,f.*,(select name from cnt_menu where id = c.parent_id)parent_name FROM CNT_MENU C LEFT JOIN FW_FUNCTION F ON C.MOD_FUNC_ID = F.ID WHERE ");
		sb.append(" c.APP_ID = '"+SystemConstance.LOGIC_SYSTEM_APP_ID+"' ORDER BY C.ORDER_ ASC ");
		SQL=sb.toString();
		datasource = ds;
	}
}	







