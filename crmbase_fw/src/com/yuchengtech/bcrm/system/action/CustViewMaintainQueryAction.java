package com.yuchengtech.bcrm.system.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

/**
 * 客户视图项维护查询
 * @author zhangsxin
 * @since 2012-11-30
 */
@SuppressWarnings("serial")
@Action("/custViewMaintainQuery")
public class CustViewMaintainQueryAction extends CommonAction {

	
	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;//数据源


	/**
	 * 客户视图项维护的查询
	 */
	public void prepare() {
		
		StringBuilder sb = new StringBuilder("SELECT A.*,B.NAME AS PARENT_NAME FROM OCRM_SYS_VIEW_MANAGER A ")
		.append("	LEFT JOIN OCRM_SYS_VIEW_MANAGER B ON A.PARENTID=B.ID")
		.append("  WHERE 1=1");
		
		for (String key : this.getJson().keySet()) { //循环获取查询条件
			if (null != this.getJson().get(key) && !this.getJson().get(key).equals("")) {
				if (key.equals("name")){ //视图项名称
					sb.append(" AND A.NAME like '%" + this.getJson().get(key) + "%'");
				} 
				else if(key.equals("viewtype")){//客户视图项类型
					sb.append(" AND A.VIEWTYPE like '%" + this.getJson().get(key) + "%'");
				}
			}
		}
		addOracleLookup("VIEWTYPE", "PAR0100021");//对照类型
		
		SQL = sb.toString(); //为父类SQL属性赋值（设置查询SQL）
		datasource = ds; //为父类数据源赋值
	}

}
