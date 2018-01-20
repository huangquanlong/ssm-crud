Ext.onReady(function() {
	var dataJsonUrl = '/sheetHomePageAction!getCfgInfo.json';
	var sheetHome = new Com.yucheng.crm.sheetHomePage.SheetHomePageBooter(dataJsonUrl);
	// 页面布局
	new Ext.Viewport({// 页面展示
		layout : 'fit',
		// frame : true,
		border : false,
		items : [sheetHome.homePage]
	});
});
