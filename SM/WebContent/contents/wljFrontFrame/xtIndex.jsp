<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page import="com.yuchengtech.bob.upload.FileTypeConstance" language="java" %>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="org.springframework.security.core.GrantedAuthority" language="java"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" language="java"%>
<%@ page import="com.yuchengtech.bob.vo.AuthUser" language="java"%>
<%@ page import="java.util.List" language="java" %>
<%@ page import="com.yuchengtech.bob.common.LogService" language="java" %>
<%@ page import="com.yuchengtech.crm.constance.SystemConstance" language="java" %>
<%@ page import="com.yuchengtech.bob.core.LookupManager" language="java" %>
<%@ page import="com.yuchengtech.crm.constance.OperateTypeConstant" language="java" %>
<%@ page import="org.springframework.context.ApplicationContext" language="java" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" language="java" %>
<%@ page import="java.util.Map" language="java" %>
<%@page import="org.springframework.web.context.ContextLoader"%>
<html>
<%--<script type="text/javascript">

//服务器地址及端口
<%	String serverIP = FileTypeConstance.getServerIP();
	out.print("var __serverIP = '"+serverIP+"';");%>
--</script>
<!-- 话媒设备 -->
<OBJECT id=PhoneOcx
     width="0" 
     height="0" 
     classid="clsid:780A40C6-C224-11DA-AD88-0080C75D8B26"
     codebase="../cabFile/YTEC-CRM.cab#version=1,0,0,0" >
</OBJECT> 
<!-- 爬山虎设备  -->
<OBJECT ID="Phonic_usb"
		width="0" 
     	height="0" 
   		CLASSID="CLSID:BB4780D9-391E-41B5-B366-434ADCFD7D10" 
   		codebase="../cabFile/YTEC-CRM.cab#version=1,0,0,0">
</OBJECT>
--%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/resource/ext3/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/resource/ext3/ext-all.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/resource/ext3/ux/ux-all.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/common/ViewContext.js"></script>
	<head>
	
	<script type="text/javascript">
		var intervalID; 
		var __a="<%=request.getContextPath()%>";
		var basepath = "/" + __a.substring(1, __a.length);
		<%
		if((SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)){
			//Session过期，重新登录
			out.print("top.location.href = basepath;");
		}else{
			AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        
			String userId = auth.getUserId();
			out.print("var __userId = '"+userId+"';");
			out.print("var __userName = '"+auth.getUsername()+"';");
			out.print("var __userCname = '"+auth.getCname()+"';");
			out.print("var __themeColorId = '"+auth.getAttribute("THEME_COLOR_ID")+"';");
			//当前用户角色ID串
			String role = "";
			//当前用户角色Code串
			String roleCode ="";
			for(int i=0;i<auth.getAuthorities().size();i++)
				role += auth.getAuthorities().get(i).getAuthority()+"$";
		
			for(int i=0;i<auth.getRolesInfo().size();i++){
			   Map roleMap=(Map)auth.getRolesInfo().get(i);
			   roleCode+=roleMap.get("ROLE_CODE")+"$";
			}
			//当前用户机构ID
			String orgId = auth.getUnitId();
			//公共JS变量
			out.print("var __roles = '"+role+"';");
			out.print("var __roleCodes = '"+roleCode+"';");
			out.print("var __units = '"+auth.getUnitId()+"';");
			out.print("var __grants = [];");
			String resId = request.getParameter("resId");
			out.print("var __resId = '"+resId+"';");
			out.print("var __unitname = '"+auth.getUnitName()+"';");
			out.print("var __unitlevel = '"+auth.getUnitlevel()+"';");
			out.print("var __appId = '"+SystemConstance.LOGIC_SYSTEM_APP_ID+"';");
			//登录类型（单角色或多角色）
			out.print("var __loginType = '"+auth.getLoginType()+"';");
			//security变量
			out.print("var __secMsgType = '';");
			out.print("var __secMsg = '';");
			if (auth.getCredentialInfo() != null) {
				out.print("__secMsgType = '"+auth.getCredentialInfo().getInfoType()+"';");
				out.print("__secMsg = '"+auth.getCredentialInfo().getMessage()+"';");
			}
			//判断如果是菜单URL请求，则做两件事  
			//1、将菜单下的控制点写入公共变量
			//2、记录菜单访问日志
			if(resId!=null && !"-1".equals(resId) && !"".equals(resId)){
				List<String> grants = auth.findGrantByRes(resId);
				if(grants!=null){
					for(int i=0;i<grants.size();i++){
						out.print("__grants.push('"+grants.get(i)+"');");
					}
				}
				//增加菜单日志访问记录
				//LogUtils lu=new LogUtils();
				/* LogService.loginfo.setLoginIp(request.getRemoteAddr());
				LogService.loginfo.setLogTypeId(Long.valueOf(OperateTypeConstant.VISIT_MENU+""));
				LogService.loginfo.setAfterValue(request.getServletPath());
				LogService.loginfo.setContent(OperateTypeConstant.getOperateText(OperateTypeConstant.VISIT_MENU)+":"+LookupManager.getInstance().getMenuName(resId));
				LogService.addLog();*/
			} 
			//获取异常描述信息
			ApplicationContext app = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()) ;
			//ApplicationContext app = ContextLoader.getCurrentWebApplicationContext();
			
			Map<String, String> errMsgMap = (Map<String, String>) app.getBean("getErrMsgMap");
			Map<String, String> errPageMap = (Map<String, String>) app.getBean("getErrPageMap");
			String defaultErrMsg = (String) app.getBean("getDefaultErrMsg");
			String defaultErrPage = (String) app.getBean("getDefaultErrPage");
			out.print("var __errMsgMap = [];");
			for(String key : errMsgMap.keySet()){
				out.print("__errMsgMap.push({code:'"+key+"',content:'"+errMsgMap.get(key)+"'});"); 
			}
		}
		//上传地址
		String upFile = FileTypeConstance.getUploadPath();
		out.print("var __upFile = '"+upFile+"';");%>
		JsContext.initContext();
	</script>
	<link  type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/contents/resource/ext3/resources/css/ext-all.css" />
	<link  type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/contents/css/comm.css" />
	<link  type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/contents/css/frame.css" />
	<link  type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/contents/css/elements.css" />
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/contents/wljFrontFrame/styles/sheet/sheetcss/common.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/contents/wljFrontFrame/styles/sheet/sheetcss/base_frame.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/contents/wljFrontFrame/styles/sheet/sheetthemes/theme1/css/themes_frame.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/wljFrontFrame/js/jquery-1.7.2.min.js"></script>

	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/common/Crm-Ext-Patch-1.000-v1.0.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/common/Crm-Ext-Extends-1.000-v1.0.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/wljFrontFrame/js/sheet/Com.yucheng.crm.index.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/common/Com.yucheng.crm.security.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/common/Com.yucheng.crm.information.js"></script> 

	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/systemManager/grantAuthortication/accordionTool.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/system/comfunctionset/commonfuntion.js"></script> 

	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/resource/ext3/locale/ext-lang-zh_CN.js"></script>
<%-- 
	 <!-- 电话处理页面 -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/customer/customerManager/mktPhone/phoneViewWindow.js"></script>
	<!-- 设备公用的处理 -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/customer/customerManager/mktPhone/Comhandler.js"></script>
	<!-- 爬山虎设备事件处理 -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/customer/customerManager/mktPhone/PSHhandler.js"></script>
	<!-- 话媒设备事件处理 -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/customer/customerManager/mktPhone/HMhandler.js"></script>
	
	<script type="text/javascript">
		//设备判断(在onload中调用)
		function getMType(){
 			if(PSHState==0){//接入了爬山虎的设备
 				MType = 1;
 			}else{
 				try{
 					HMState = PhoneOcx.IfConnectUSB();
 				}catch(e){
 				}
 				if(HMState==0){
 					MType = 2;
 					setInterval(getInLineInfo,1000);//接入了话媒，开始轮询
 				}
 					
 			}
 		}
	</script>
	
<!-- 爬山虎设备接入事件  如果接入修改设备状态-->	
<SCRIPT   LANGUAGE="javascript"  type = "text/javascript" FOR="Phonic_usb"   EVENT='PlugIn(uboxHandle)'  >  
	hdlID = uboxHandle;
	PSHState = 0;
</SCRIPT> 
 <!--挂机事件 -->
<SCRIPT   LANGUAGE="javascript"  type = "text/javascript"  FOR="Phonic_usb"   EVENT='HangUp(uboxHandle)' >  
   Ubox_hookon(uboxHandle);
</SCRIPT> 
 <!--来电事件 -->
<SCRIPT   LANGUAGE="javascript"  type = "text/javascript"  FOR="Phonic_usb"   EVENT='callId(uboxHandle,callerNumber,callerTime,callerName)'  >  
	ifCalling = true;
	Ubox_CallId(uboxHandle,callerNumber,callerTime,callerName);
</SCRIPT> 
 <!--摘机事件 -->
<SCRIPT   LANGUAGE="javascript"  type = "text/javascript"  FOR="Phonic_usb"   EVENT='HookOff(uboxHandle)'  >  
	 Ubox_hookoff(uboxHandle);
</SCRIPT> 
<!-- 页面加载完后执行，判断电脑接入的是话媒还是爬山虎的设备，然后调用相应的事件处理 -->	
<SCRIPT LANGUAGE="JavaScript" FOR="window" EVENT="onLoad()">
		try{
			Phonic_usb.CloseDevice();
		 	var uChannelNum=Phonic_usb.OpenDevice(0);//初始化爬山虎设备,以“录音模式”打开设备
		 	if(uChannelNum== 0)//成功
		 	{
		 	//因为设备打开成功由事件PLugIn通知的，所以此处添加一个延迟(等待PLugIn事件处理)后判断电脑是否接入了爬山虎设备
		 	setTimeout(getMType,10000);
		 	}else{
		 		getMType();
		 	}
		 }catch(e)
		 {
			 getMType();
			 //alert(e);
		 }
		 
</SCRIPT> 
<!-- 页面关闭或刷新时处理 -->	
<SCRIPT LANGUAGE="JavaScript" FOR="window" EVENT="onunLoad()">
	if(PSHState==0){
		Phonic_usb.CloseDevice();//关闭设备
	}	
	HMState = 1;//话媒设备状态
	PSHState = 1;//爬山虎设备状态
</SCRIPT> 
	--%>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/xtIndex/xtIndex.js"></script>
	<title>客户关系管理系统</title>
	</head>
	<body id ='body' style="overflow-x:hidden;overflow-y:hidden;">
	   <div class="in_head ">
		<div class="in_top" id="in_top">
    		<div class="in_logo" id='in_logo'></div>
    		<div id="invokeIcons" class="in_fun" align="right">
    		</div>
   		</div>
        <div id="nav" class="in_menu">   
      	    <ul id="rootMenus" class="in_menu_ul" style="_width:true;">
      	    </ul>
    	</div> 	
      </div>  
      <iframe id="indexPageFrame" style="border:0 solid #000;height:0px;width:100%;" src='' ></iframe>
      <div id="wholeContent" class="bg" style="display:none;"> 
      	<div id="hiddenLeftMenuDiv" class="frame_left">
          	<div id="left_menu" class="left_menu" style="display:''">
            	<div class="left_menu_tit" id="left_menu_tit">
             		<p class="left_menu_p" id="left_menu_p"><span id="firstTitleSpan" class="left_menu_w"></span></p>
            	</div>
          	</div>
         </div>   
  		 <div class="in_tab">
			<div id="rightTagConent" class="in_tab_div">
            	<ul id="fc_tab_ul" class="fc_tab_ul"></ul>
       	    </div>
  	    </div> 	 
	    <div id="frame_center_contents" class="frame_center_contents"></div>  
	  </div> 
	</body>
</html>