<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>客户视图</title>
<%@ include file="/contents/pages/common/includes.jsp"%>
<version:frameLink  type="text/css" rel="stylesheet" href="/contents/wljFrontFrame/styles/search/searchcss/base_frame.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/Wlj-memorise-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/Wlj-search-APP.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/widgets/search/tiles.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/widgets/views/index/grid/grid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/plugin/customerView/Wlj-commonView.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/plugin/customerView/customerView.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/pages/common/Com.yucheng.crm.common.FusionChartPanel.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/plugin/customerView/customerViewData.js"></script>
<script type="text/javascript">
	var viewId   = "<%=request.getParameter("viewId")%>";
	var viewType = "<%=request.getParameter("viewType")%>";
	var _custId = "<%=request.getParameter("viewId")%>";
	var _prodId = "<%=request.getParameter("viewId")%>";
	var _roleId = "<%=request.getParameter("viewId")%>";
</script>
</head>
<body>
	
</body>
</html>