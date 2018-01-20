<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="com.yuchengtech.bob.common.GrantProxyProjView" language="java" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>YC.CRM</title>
	<meta name="keywords" content="YC.CRM,YC.CRM" />
	<meta name="description" content="YC.CRM,YC.CRM" />
	<meta name="Author" content="YuchengTech" />
	<link rel="shortcut icon" href="favicon.ico" />
	<%@ include file="/contents/pages/common/includes.jsp"%>
	<script type="text/javascript">
	<%
		String projId = request.getParameter("projId");//项目ID
		String projNo = request.getParameter("projNo")==null?"":request.getParameter("projNo");//项目标识
		projNo = URLDecoder.decode(projNo,"UTF-8");//项目名称(转码)
		String projName = request.getParameter("projName")==null?"":request.getParameter("projName");//项目名称
		projName = URLDecoder.decode(projName,"UTF-8");//项目名称(转码)
		String reqId = request.getParameter("reqId");//需求ID
		String reqNo = request.getParameter("reqNo")==null?"":request.getParameter("reqNo");//需求标识
		reqNo = URLDecoder.decode(reqNo,"UTF-8");//需求标识(转码)
		String reqName = request.getParameter("reqName")==null?"":request.getParameter("reqName");//需求名称
		reqName = URLDecoder.decode(reqName,"UTF-8");//需求名称(转码)
		String busiId = request.getParameter("busiId");
		String viewId = request.getParameter("viewId");
		String viewType = request.getParameter("viewType");
		
		out.print("var _projId = '"+projId+"';");
		out.print("var _projNo = '"+projNo+"';");
		out.print("var _projName = '"+projName+"';");
		out.print("var _reqId = '"+reqId+"';");
		out.print("var _reqNo = '"+reqNo+"';");
		out.print("var _reqName = '"+reqName+"';");
		out.print("var _busiId = '"+busiId+"';");
		out.print("var _viewType = '"+viewType+"';");
		
		
		if(!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)){
			AuthUser auth=(AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//1、重新将项目视图下的控制点写入公共变量（包括项目视图和需求视图菜单中的控制点）,用于功能按钮权限控制
			if(viewId!=null && !"-1".equals(viewId) && !"".equals(viewId)){
				List<String> grants = new GrantProxyProjView().getControlByRoleIdAndViewId(auth,viewId,projId);
				if(grants!=null){
					for(int i=0;i<grants.size();i++){
						out.print("__grants.push('"+grants.get(i)+"');");
					}
				}
			}
		}
	%>
		JsContext.initContext();
	</script>
	<version:frameLink  type="text/css" rel="stylesheet" href="/contents/css/LovCombo.css" />
	<version:frameScript type="text/javascript" src="/contents/pages/common/LovCombo.js"/>
	<version:frameLink type="/contents/wljFrontFrame/styles/search/searchcss/common.css"/>
	<version:frameLink type="/contents/wljFrontFrame/styles/search/searchcss/base_frame.css"/>
	<version:frameLink type="/contents/wljFrontFrame/styles/search/searchthemes/blue/frame.css"/>
	<version:frameLink type="/contents/resource/ext3/resources/css/debug.css"/>
	<version:frameLink type="/contents/wljFrontFrame/styles/search/searchthemes/blue/main.css"/>
	
	<version:frameScript type="text/javascript" src="/contents/frameControllers/Wlj-SyncAjax.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/Wlj-frame-base.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/Wlj-memorise-base.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/search/tiles.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/app/Wlj-frame-function-error.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/debug.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/app/Wlj-frame-function-app-cfg.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/app/Wlj-frame-function-widgets.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/app/Wlj.frame.function.widgets/Wlj.frame.functions.app.widgets.View.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/app/Wlj-frame-function-app.js"/>
    
    <version:frameScript type="text/javascript" src="/contents/frameControllers/view/Wlj-view-function-builder.js"/>
    
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/app/Wlj-frame-function-header.js"/>
    <version:frameScript type="text/javascript" src="/contents/frameControllers/widgets/app/Wlj-frame-function-api.js"/>
	
</head>
<body>
</body>
</html>