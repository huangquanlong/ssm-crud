// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CrmConstants.java

package com.yuchengtech.bcrm.util;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

public class CrmConstants
{

	public static final String REQSYSCD = "12";
	public static final String ROLE_MGRROLE = "mgrRole";
	public static final String ROLE_CSTDEPUTY = "cstDeputy";
	public static final String ROLE_PRODMGR = "zhcpjl";
	public static final String OPEN_YES = "1";
	public static final String OPEN_NO = "0";
	public static final String CUSTMSG_MAINTYPE = "1";
	public static final String CUSTMSG_OMAINTYPE = "2";
	public static final String AGRTYPE_DP = "DP";
	public static final String AGRTYPE_LN = "LN";
	public static final String AGRTYPE_NI = "NI";
	public static final String ESB_SERVICE_HOST;
	public static final int ESB_SERVICE_PORT;
	private static final String CONFIG = "esb";
	private static final Locale DEFAULT_LOCALE;
	private static ResourceBundleMessageSource bundle;
	public static final String ESB_SUCCESS = "SUCCESS";
	public static final String ESB_SUCCESS_CODE = "000000";
	public static final String ECIF_TXCODE_UO102720001 = "UO102720001";
	public static final String ECIF_TXCODE_AO101620101 = "AO101620101";
	public static final String ECIF_TXCODE_AC101820201 = "AC101820201";
	public static final String ECIF_TXCODE_AC101920201 = "AC101920201";
	public static final String ECIF_TXCODE_UO103620101 = "UO103620101";
	public static final String ECIF_TXCODE_UC103820201 = "UC103820201";
	public static final String ECIF_TXCODE_UC103920201 = "UC103920201";
	public static final String ECIF_TXCODE_UO103720101 = "UO103720101";
	public static final String ESB_TXCODE_OO100220001 = "OO100220001";
	public static final String ECIF_TXCODE_MC100420001 = "MC100420001";
	public static final String ECIF_TXCODE_MC100520001 = "MC100520001";
	public static final String ESB_EXCODE_S007001990UO1027 = "S007001990UO1027";
	public static final String ESB_EXCODE_S007001990AO1016 = "S007001990AO1016";
	public static final String ESB_EXCODE_S007001990AC1018 = "S007001990AC1018";
	public static final String ESB_EXCODE_S007001990AC1019 = "S007001990AC1019";
	public static final String ESB_EXCODE_S007001990UO1036 = "S007001990UO1036";
	public static final String ESB_EXCODE_S007001990UC1038 = "S007001990UC1038";
	public static final String ESB_EXCODE_S007001990UC1039 = "S007001990UC1039";
	public static final String ESB_EXCODE_S007001990UO1037 = "S007001990UO1037";
	public static final String ECIF_EXOCDE_S007001990OO1002 = "S007001990OO1002";
	public static final String ESB_EXCODE_S007001990MC1004 = "S007001990MC1004";
	public static final String ESB_EXCODE_S007001990MC1005 = "S007001990MC1005";
	public static final String IsUsingBelongLine = "1";

	public CrmConstants()
	{
	}

	static 
	{
		DEFAULT_LOCALE = Locale.CHINA;
		bundle = null;
		if (bundle == null)
		{
			bundle = new ResourceBundleMessageSource();
			bundle.setBasename("esb");
		}
		ESB_SERVICE_HOST = bundle.getMessage("esbServiceHost", null, DEFAULT_LOCALE);
		ESB_SERVICE_PORT = Integer.valueOf(bundle.getMessage("esbServicePort", null, DEFAULT_LOCALE)).intValue();
	}
}
