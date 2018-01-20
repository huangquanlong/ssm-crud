package com.yuchengtech.bcrm.workplat.action;

import javax.sql.DataSource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yuchengtech.bob.common.CommonAction;

@ParentPackage("json-default")
@Action(value = "/workingplatformInfoQuery", results = { @Result(name = "success", type = "json"), })
public class WorkingplatformInfoQueryAction extends CommonAction {

	@Autowired
	@Qualifier("dsOracle")
	private DataSource ds;

	public void prepare() {


		StringBuilder sb = new StringBuilder(
			"select p.*,u.UNITNAME,SE.SECTION_NAME AS MESSAGE_TYPE_ORA"
			+ " from ocrm_f_wp_info p left join sys_units u on p.PUBLISH_ORG = u.UNITID left join sys_users u1 on p.PUBLISH_USER = u1.USERNAME LEFT JOIN OCRM_F_WP_INFO_SECTION SE ON TO_CHAR(SE.SECTION_ID) = p.MESSAGE_TYPE where 1>0 "
		);

		for (String key : this.getJson().keySet()) {
			if (null != this.getJson().get(key)&& !this.getJson().get(key).equals("")) {
				if (key.equals("RETRIEVAL_INFORMATION")) {
							sb.append(" and (MESSAGE_TITLE like '%"+ this.getJson().get(key) + "%'"
									+" or MESSAGE_INTRODUCE like '%"+ this.getJson().get(key) + "%')");
				} else if (key.equals("MESSAGE_TYPE"))
					sb.append(" and MESSAGE_TYPE ='"
							+ this.getJson().get(key) + "'");

				else if (key.equals("PUBLISH_USER"))
					sb.append(" and PUBLISH_USER like '%"
							+ this.getJson().get(key) + "%'");

				else if (key.equals("PUBLISH_ORG"))
					sb.append(" and PUBLISH_ORG like '%"
							+ this.getJson().get(key) + "%'");

				else if (key.equals("PUBLISH_DATES"))
					sb.append(" and PUBLISH_DATE >= to_date('"
							+ this.getJson().get(key) + "','yyyy-MM-dd')");

				else if (key.equals("PUBLISH_DATEE"))
					sb.append(" and PUBLISH_DATE <= to_date('"
							+ this.getJson().get(key) + "','yyyy-MM-dd')");
			}
		}

		setPrimaryKey("p.PUBLISH_DATE desc,p.MESSAGE_ID");
		//addOracleLookup("MESSAGE_TYPE", "MESSAGE_TYPE");

		SQL = sb.toString();
		datasource = ds;
	}
}
