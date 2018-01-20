Ext.ns('Wlj.search.App');

Wlj.search.App.UTIL = {
	needSearchArea : false,				//是否需要点击开始后的主查询页面，该配置项会影响到的开始、返回主功能图标的展示
	menuInWindow : true,
	headerFunctions : 'HEADBUTTONS', 	//首页主功能导航区展示内容：menuRoot，或者某个菜单下的子菜单：res：XXX，或者HEADBUTTONS;
	menuHeaderCount : 6,				//如首页展示菜单（一级菜单，或者某子菜单）时，主功能区的展示的菜单个数。
	needStartOBack : false,				//如首页展示菜单（一级菜单，或者某子菜单）时，是否始终需要一个开始返回按钮
	
	autoCreateGroup : true,				//如果瓷贴不在当前已有瓷贴个数内，是否创建新的组：true，创建新的group，并渲染瓷贴；false：舍弃该瓷贴；
	defaultTileGroupCount : 3,			//默认瓷贴组个数
	defaultTileGroupOwnerH : 3,			//瓷贴组纵向默认瓷贴单元数 
	defaultTileGroupOwnerW : 9,			//瓷贴组横向默认瓷贴单元数
	defaultIndexTileWidthUnit : 128,	//单元瓷贴宽度
	defaultIndexTileHeightUnit : 128,	//单元瓷贴高度
	defaultIndexTileMarginUnit : 4,		//1/2单元瓷贴间距
	
	groupMargin : 27,					//瓷贴组间距
	
//	containerTop : 79,					//瓷贴容器顶边距属性
	containerLeft : 0,					//瓷贴容器左边距属性
	
	headerHeight : 80,					//首页主功能导航区高度，暂不支持修改
	headerTopMargin : 56,				//主功能导航区顶部间隔
	headerButtomMargin : 23,			//瓷贴组及查询区域等，与主功能导航区域的间隔
	
	taskbarHeight : 40,					//任务栏高度，暂不支持修改
	//由此，瓷贴区域，以及搜索区域，高度，则可根据以上配置计算得出：
	//	瓷贴区域以及搜索区域高度：screenHeight - headerHeight - headerTopMargin - headerButtomMargin - taskbarHeigth
	//	瓷贴区域以及搜友区域距离顶部的距离： headerHeight + headerTopMargin + headerButtomMargin
	
	viewAdjust : true					//有限范围内，界面展示优化逻辑开关.默认启用。
	
};
APPUTIL = Wlj.search.App.UTIL;
Wlj.search.App.HEADERBUTTONS = [{
	id : 'backFunction',
	name:'返回',
	acls : 'lv1',
	iconcls : 'icon0',
	wordcls : 'word',
	cssText : 'display:none;',
	cls : '',
	handler:function(p,f,dom){
		p.appObject.HideSearchArea();
	}
},{
	id:'startFunction',
	name:'开始',
	cssText : !APPUTIL.needSearchArea?'display:none;':'',
	acls : 'lv1',
	iconcls : 'icon1',
	wordcls : 'word',
	cls : '',
	handler:function(p,f,dom){
		p.appObject.ShowSearchArea();
	}
},{
	id:'menuFunction',
	name:'菜单',
	acls:'lv1',
	iconcls : 'icon2',
	wordcls : 'word',
	cssText : '',
	cls : '',
	menuRoot : false,
	functionName : 'Wlj.widgets.search.header.MenuFunction'
},{
//	name:'配置',
//	acls:'lv1',
//	iconcls : 'icon3',
//	wordcls : 'word',
//	cssText : '',
//	cls : '',
//	handler : function(p,f,dom){
//		p.appObject.saveIndexTiles();
//	}
//},{
	name:'主题',
	acls:'lv1',
	iconcls : 'icon4',
	wordcls : 'word',
	cssText : '',
	cls : '',
	handler : function(parent,_thisObj,dom){
		if(!_thisObj.controll){
			//主要是用于用户默认项目是否还存在校验
			var projects = parent.appObject.projects;
			if (projects && projects.length > 0) {
				var iscontain = false;
				for (var proj in projects) {
					if (proj.key == __defaultProject) {
						iscontain = true;
						break;
					}
				}
				__defaultProject = iscontain ? __defaultProject : projects[0].key;
			} else {
				__defaultProject = "";//修复项目删除时，而该用户默认项目和该项目之间的绑定
			}
			var cfg = {
				themeData : parent.appObject.themeData,
				themes : Wlj.search.App.THEME,
				backgrounds : Wlj.search.App.BACKGROUND,
				wordsizes : Wlj.search.App.WORDSIZE,
				BG_ICON : __background,
				THEME_CSS : __theme,
				WORD_SIZE : __wordsize,
				SPARE_ONE : __defaultProject,
				projects : projects,
				renderTo :  _thisObj.el,
				hidden : true
			};
			_thisObj.controll = new Wlj.widgets.search.header.Customize(cfg);
			
		}
		if(_thisObj.controll && _thisObj.controll.hidden){
			_thisObj.controll.show();
		}
		//wzy,2015-02-10,modify:修改索引序号，避免JS错误，原因是，和“主题”平级的功能模块，去掉了2个，索引同时减去2
//		if(parent.headerFun.get(4).plugin && !parent.headerFun.get(4).plugin.hidden){
//			parent.headerFun.get(4).plugin.hide();
//		}
		if(parent.headerFun.get(2).plugin && !parent.headerFun.get(2).plugin.hidden){
			parent.headerFun.get(2).plugin.hide();
		}
	},
	outHandler : function(p,_thisObj,dom){
		if(_thisObj.controll && !_thisObj.controll.hidden && !_thisObj.controll.defaultProjSelect){
			_thisObj.controll.hide();
			_thisObj.controll.defaultProjSelect = false;
		}
	}
}//,
//{
//	name:'图表',
//	acls:'lv1',
//	iconcls : 'icon7',
//	wordcls : 'word',
//	cssText : '',
//	cls : '',
//	handler : function(parent,_thisObj,dom){
//		if(!_thisObj.plugin){
//			_thisObj.plugin = new indexTileExtra({
//				renderTo :  _thisObj.el,
//				appObject : parent.appObject,
//				hidden : true
//			});
//		}
//		if(_thisObj.plugin.hidden){
//			_thisObj.plugin.show();
//		}else{
//			_thisObj.plugin.hide();
//		}
//		if(parent.headerFun.get(3).controll && !parent.headerFun.get(3).controll.hidden){
//			parent.headerFun.get(3).controll.hide();
//		}
//	},
//	outHandler : function(p,_thisObj,dom){
//		if(_thisObj.plugin && !_thisObj.plugin.hidden){
//			_thisObj.plugin.hide();
//		}
//	}
//},
//{//根据业务需求，需要屏蔽模式模块，mender: luoyd
//	name:'模式',
//	acls:'lv1',
//	iconcls : 'icon5',
//	wordcls : 'word',
//	cssText : '',
//	cls : '',
//	handler:function(p,f,dom) {
//		window.location.href = basepath + '/contents/wljFrontFrame/xtIndex.jsp';
//		//记录切换失败
//		Ext.Ajax.request({
//			url:basepath+'/switchThemeAction!updateUserCfg.json?themeId=1',
//			method: 'POST',
//			success : function(response) {//记录切换成功，不提示			
//			},
//			failure : function(response) {//记录切换失败
//				Ext.Msg.alert('提示', '模式切换失败!');
//			}
//		});
//	}
//},{
//	name:'任务',
//	acls:'lv1',
//	iconcls : 'icon6',
//	wordcls : 'word',
//	cssText : '',
//	cls : '',
//	handler:function(p,f,dom) {
//		Wlj.TaskMgr.showTasks(dom);
//	}
//},{
//	name:'服务',
//	acls:'lv1',
//	iconcls : 'icon7',
//	wordcls : 'word',
//	cssText : '',
//	cls : '',
//	handler:function(p,f,dom) {
//		Wlj.ServiceMgr.showServiceWindow();
//	}
//},{//根据业务需求，需要屏蔽盖模块，mender: luoyd
//	name:'GETDEMOJSONS',
//	acls:'lv1',
//	iconcls : 'icon7',
//	wordcls : 'word',
//	cssText : '',
//	cls : '',
//	handler:function(p,f,dom) {
////		Ext.Ajax.request({
////			url : basepath+'/lookup.json',
////			method : 'GET',
////			success : function(a,b,c){debugger;}
////			
////		});
//		
//		var theUrls = ['/loanfproduct.json',
//		               '/usermanagequery.json',
//		               '/systemUnit-query.json',
//		               '/loanfcontract.json',
//		               '/loanfcontractbelong.json',
//		               '/loanfhkplan.json',
//		               '/loanfskrecord.json',
//		               '/loanfznjreduce.json',
//		               '/loanfblack.json',
//		               '/loanfcustomer.json'
//		               ];
//		
//		createDemoJson(theUrls);
//		
//	}
//	
//}
];

//function getProjectList(){
//	Ext.Ajax.request({
//		url : basepath+'/projectManageAction!getProjectList.json',
//		method:'GET',
//		success : function(respose){
//			debugger;
//			var projectS =  Ext.util.JSON.decode(respose.responseText).json.data;
//			var resultData = [];
//			for (var index = 0; index < projectS.length; index++) {
//				var project = {};
//				project.key = projectS[index].PROJ_ID;
//				project.value = projectS[index].PROJ_NAME;
//				resultData.push(project);
//			}
//			
//			return Ext.encode(resultData);
//		}
//	});
//	return [];
//}
function createDemoJson(urls){
	if(urls.length == 0){
		return;
	}else {
		Ext.Ajax.request({
			url : basepath+urls.pop(),
			method:'GET',
			params : {
				createJson : 'true'
			},
			success : function(){
				createDemoJson(urls);
			}
		});
	}
	
	
}


Wlj.search.App.SEARCHTYPES = [{
	name : '客户',
	searchType : 'CUSTOMER',
	searchUrl : '/contents/pages/testtiles/searchResult.jsp'
},{
	name : '产品',
	searchType : 'PRODUCT',
	searchUrl : '/contents/pages/testtiles/noticeWidthBuilder.js'
},{
	name : '功能',
	searchType : 'FUNCTION',
	searchUrl : '/contents/pages/testtiles/oprationGuard.jsp'
},{
	name : '知识库',
	searchType : 'KNOWLEGE',
	searchUrl : '/contents/pages/oprations/opratingFlows.jsp'
}];

/**
 * 瓷贴颜色
 */
Wlj.search.App.TILECOLOR ={
	red : {
		bColor : '#FF0000',
		fColor : '#1f373d'
	},
	blue : {
		bColor : '#1b96d1'
	},
	pink : {
		bColor:'#FF1493'
	},
	yellow : {
		bColor:'#FFFF00'
	},
	green : {
		bColor:'#9ACD32'
	},
	gray : {
		bColor:'#808080'
	},
	lightblue : {
		bColor:'#BBFFEE'
	},
	zise : {
		bColor:'#FF3EFF'
	},
	color9 : {
		bColor:'#CCBBFF'
	},
	color10 : {
		bColor:'#66009D'
	}
};
/**
 * 皮肤设置
 * @type 
 */
Wlj.search.App.THEME = [{
	preBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/themes_pic_01.jpg',
	themeName : 'blue'
},{
	preBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/themes_pic_02.jpg',
	themeName : 'darkblue'
},{
	preBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/themes_pic_02.jpg',
	themeName : 'orange'
}];
/**
 * 背景图片
 */
Wlj.search.App.BACKGROUND = [{
	preBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_01_small.gif',
	reaBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_01.jpg'
},{
	preBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_02_small.gif',
	reaBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_02.jpg'
},{
	preBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_03_small.gif',
	reaBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_03.jpg'
},{
	preBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_04_small.gif',
	reaBG : '/contents/wljFrontFrame/styles/search/searchthemes/blue/pics/index/index_bg_04.jpg'
}];
/**
 * 字号设置
 * @type 
 */
Wlj.search.App.WORDSIZE = [{
	size : 'ra_normal',
	text : '正常(默认)'
},{
	size : 'ra_max',
	text : '大号'
}];
/**
 * 后台数据服务地址列表
 */
Wlj.search.App.DATA_SERVICE = {
	menuService : {
		url : '/indexinit.json',
		root : 'json.data'
	},
	userTileService : {
		url : '/usertile.json',
		root : 'json.data'
	},
	comunicatFunctionService : {
		url : '/comfunctionset.json',
		root : 'returns.data'
	}
};

/**
 * 右键菜单配置方案，
 * 下列某对象右键列表配置必须存在，且为数组对象，如不需要某项列表，可将该数据置为空数组，如[];
 * 每个右键菜单本身，必须至少有text和fn属性。且，fn属性为function。另，可添加Ext.menu.MenuItem的配置项。
 * 其他对象右键配置对象将逐步扩展。
 */
Wlj.search.App.CONTEXT_MENU = {
	//任务栏右键列表，作用域：任务栏对象；Wlj.widgets.search.window.TaskBar
	TASK_BAR : [{
		text : '返回首页',
		fn : function(){
			Wlj.TaskMgr.tasks.each(function(task){
				task.minisize();
			});
		}
	},{
		text : '关闭所有',
		fn : function(){
			this.closeAll();
		}
	},{
		text : '关闭非当前',
		fn : function(){
			this.closeWithOutCurrent();
		}
	}],
	//任务对象右键列表，会向上冒泡至任务栏。作用域：任务对象：Wlj.widgets.search.window.TaskItem
	TASK_ITEM : [{
		text : '关闭窗口',
		fn : function(){
			this.close();
		}
	},{
		text : '关闭其他',
		fn : function(){
			this.closeOthers();
		}
	},{
		text : '最大化',
		fn : function(){
			this.maxisize();
		}
	},{
		text : '最小化',
		fn : function(){
			this.minisize();
		}
	}],
	//首页瓷贴右键列表，会向上冒泡至瓷贴组对象；作用域：Wlj.widgets.search.tile.IndexTile
	INDEX_TILE : [{
		text : '打开',
		fn : function(){
			this.clickFire();
		}
	}],
	//首页瓷贴组右键列表，会向上冒泡至瓷贴容器对象；作用域：Wlj.widgets.search.tile.TileGroup
	TILE_GROUP : [],
	//首页瓷贴容器右键列表；作用域：Wlj.widgets.search.tile.TileContainer
	TILE_CONTAINER : [{
		text :'保存配置',
		fn : function(){
			_APP.saveIndexTiles();
		}
	}]
};

Wlj.search.App.FUNCTIONRECOMMEND = [{
	name : '菜单',
	cls : 'dddd',
	type : 'menus'
}];