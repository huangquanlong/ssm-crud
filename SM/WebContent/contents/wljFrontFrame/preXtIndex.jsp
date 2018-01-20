<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/contents/resource/ext3/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/contents/resource/ext3/ext-all.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/contents/resource/ext3/ux/ux-all.js"></script> --%>
	</head>
	<body></body>
	<script>
		var a="<%=request.getContextPath()%>";
		var basepath = "/" + a.substring(1, a.length);	
		//获取用户配置信息
		/* Ext.Ajax.request({
			url:basepath+'/switchThemeAction!getUserCfg.json',
			mothed: 'GET',
			success : function(response) {
				var res = Ext.util.JSON.decode(response.responseText);
				if (res.themeId == '1') {
					window.location.href = basepath + '/contents/wljFrontFrame/xtIndex.jsp';
				} else if (res.themeId == '2'){
					window.location.href = basepath + '/contents/wljFrontFrame/JSsearch_index.jsp';
				} else {
					window.location.href = basepath + '/contents/wljFrontFrame/xtIndex.jsp';
				}
			},
			failure : function(response) {
				window.location.href = basepath + '/contents/wljFrontFrame/xtIndex.jsp';
			}
		}); */
		//华一银行只保留win8版
		window.location.href = basepath + '/contents/wljFrontFrame/JSsearch_index.jsp';
    </script>
</html>