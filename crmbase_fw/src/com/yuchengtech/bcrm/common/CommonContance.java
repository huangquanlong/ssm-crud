package com.yuchengtech.bcrm.common;

import java.util.ArrayList;
import java.util.List;

public class CommonContance {
	
	/**
	 * 机构查询类型列表。
	 */
	public static List<String> ORG_SEAR = new ArrayList<String>();
	
	/**
	 * 自上往下，分别表示：
	 * 查询子机构树；
	 * 查询直接子机构；
	 * 查询父机构；
	 * 查询所有父、祖机构；
	 * 查询所有机构;
	 * 查询当前机构用户；
	 * 查询子机构用户；
	 * 查询子机构用户（含当前机构用户）；
	 * 查询全部机构用户;
	 * 查询机构名称。
	 */
	static{
		ORG_SEAR.add("SUBTREE");
		ORG_SEAR.add("SUBORGS");
		ORG_SEAR.add("PARENT");
		ORG_SEAR.add("PARPATH");
		ORG_SEAR.add("ALLORG");
		ORG_SEAR.add("ORGUSER");
		ORG_SEAR.add("SUBUSER");
		ORG_SEAR.add("SUBUSER_MAX");
		ORG_SEAR.add("ALLUSER");
		ORG_SEAR.add("ORGNAME");
		ORG_SEAR.add("SUBINDEXTREE");
	}
	
}
