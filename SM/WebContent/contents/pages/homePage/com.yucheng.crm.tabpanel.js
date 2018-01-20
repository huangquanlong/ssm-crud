// 1、引入命名空间
Ext.namespace("Com.yucheng.crm");// 相当于java中包的作用

var PANEL_WINDOW_WIDTH;
var PANEL_WINDOW_HEIGHT;

/**
 * 模块信息类 panelType : '', // panel类型 iframe/grid/panel/fusionChart/ 不建意使用iframe类型
 * --未实现的功能 list/ js tabid : '', // 页签ID modid : '', // 模块ID action : '', //
 * action地址/获取json数据/页面地址 /panelc对象 modCM : '', // grid 列头 swfFile:'', //swf文件
 * 
 */
Com.yucheng.crm.ModePortal = Ext.extend(Ext.Panel, {
	title : '',
	border : true,
	panelType : '', // panel类型 iframe/grid/panel/fusionChart/ js
	// --未实现的功能 list
	tabid : '', // 页签ID
	modid : '', // 模块ID
	action : '', // action地址/获取json数据/页面地址 /panelc对象
	modCM : '', // grid 列头
	swfFile : '', // swf文件
	draggable : true,
	editMode :false,
	tools : [ {
		id : 'close',
		hidden : true,
		handler : function(e,target,panel) {
			panel.ownerCt.remove(panel,true);
		}
	} ],
	// 构建panel展示内容
	doLayoutPanel : function() {
		portMode = this;
		switch (portMode.panelType) {
		case 'js': //js文件
			Ext.ScriptLoader.loadScript({
				scripts : [ basepath + portMode.action ],
				finalCallback : function() {
					portMode.add(CRM_HOMEPAGE_ADDPANEL_OBJECT);
					portMode.doLayout();
				}
			});
			break;
		case 'fusionChart': // 图形
			fusionChartPanel = new Com.yucheng.crm.common.FusionChartPanel({
				height : PANEL_WINDOW_HEIGHT,
				swfFile : basepath + portMode.swfFile,
				dataUrl : basepath + portMode.action
			});
			portMode.add(fusionChartPanel);
			portMode.doLayout();
			break;
		case 'panel':
			portMode.add(portMode.action);
			portMode.doLayout();
			break;
		case 'iframe':
			frameShowPanel = new Ext.Panel({
				title : portMode.name,
				height : PANEL_WINDOW_HEIGHT,
				html : '<iframe src=' + basepath + portMode.action
						+ '  height="95%" width="100%" ></iframe>'
			});
			portMode.add(frameShowPanel);
			portMode.doLayout();
			break;
		case 'grid':
			// 列表头 CM
			var fieldsArray = [];
			var columnModArray = [];

			Ext.each(panelModDate, function(modDate) {
				if (modDate.cmid == portMode.modCM) {
					columnModArray = modDate.dataNames;
				}
			});// end panelModDate

			for ( var i = 0; i < columnModArray.length; i++) {
				var tmpF = {};
				tmpF.name = columnModArray[i].dataIndex;
				fieldsArray.push(tmpF);
			}

			var tmpStore = new Ext.data.Store({
				reader : new Ext.data.JsonReader({
					successProperty : 'success',
					messageProperty : 'message',
					fields : fieldsArray
				})
			});
			// 展示grid对象
			_panelGrid = new Ext.grid.GridPanel({
				autoScroll : true,
				stripeRows : true,
				store : tmpStore,
				style : "text-align:left;",
				height : PANEL_WINDOW_HEIGHT,
				viewConfig : {
					autoFill : true,
					foreceFit : true
				},
				cm : new Ext.grid.ColumnModel(columnModArray)
			});
			// 获取grid列表数据
			Ext.Ajax.request({
				url : basepath + portMode.action,
				method : 'GET',
				params : {
					'start' : '0',
					'limit' : '8'
				},
				success : function(response) {
					modulesdata = Ext.util.JSON.decode(response.responseText);
					var inData = [];
					inData = eval("modulesdata.json.data");
					tmpStore.loadData(inData);
				},
				failure : function() {

				}
			}); // END Ext.Ajax.request
			portMode.add(_panelGrid);
			portMode.doLayout();
			break;
		} // END switch case
	}
});
Ext.reg('modePortal', Com.yucheng.crm.ModePortal);// 第一个参数为自定义控件的xtype

/**
 * 设为编辑模式
 */
function editMode(e, target, panel){
	alert('editMode');
	//防止重复点击
//	if(panel.editMode){
//		Ext.Msg.alert('提示', '当前已经是编辑模式,请不要重复点击!');
//		return;
//	}
	//设置所有页签可以删除
	Ext.each(panel.ownerCt.items.items,function(tabPanel){
		// 设置页签为不可编辑模式标志
		tabPanel.editMode = true;
		//页签可编辑
		Ext.fly(tabPanel.tabEl).addClass('x-tab-strip-closable');
	});
		
	//当前页签模块可编辑 关闭
	Ext.each(panel.items.items, function(itemTas) {
		itemTas.items.each(function(portlet,index) {
			//打开关闭按钮
			portlet.getTool('close').show();
		});
	});
	
	// 打开模块列表
	if(!selectModeWin.isVisible())
		selectModeWin.show();
		
	// 增加添加面签的功能
	panel.ownerCt.add({
		title : '&nbsp;', //点击新增页签
		id:'newAddPanel',
		tabTip:'点击新增页签',
		layout : 'fit',
		listeners : {
			'activate' : function() {
				var index = tabThis.items.length;
				var tab = new Com.yucheng.crm.ModePage({
					title : "新增页签",
					closable : true, // 允许关闭
					tabid : '',
					editMode : true // 设置页签为可编辑模式标志,
				});
				Ext.Ajax.request({
					url : basepath
							+ '/tabPanel!addTabPanel.json?tabPanName='
							+ encodeURI(encodeURI(tab.title)),
					params : {
						tabNum : index - 1,
						layoutID : 'L00001' // 默认样式ID
					},
					method : "GET",
					success : function(response) {
						restInfo = Ext.util.JSON.decode(response.responseText).newTabPanID;
						tab.tabid = restInfo;
						tabThis.insert(index - 1,tab);
						tabThis.setActiveTab(index - 1); // 设置当前tab页	
						selectModeWin.show();
					},
					failure : function(response) {
						var resultArray = Ext.util.JSON.decode(response.status);
						if (resultArray == 403) {
							Ext.Msg.alert('提示',response.responseText);
						} else {
							Ext.Msg.alert('提示','操作失败,失败原因:'+ response.responseText);
						}
					}
				});

			}// 激动页签 功能
		}
	});	
		
		
}

/**
 * 保存模式
 */
function saveMode(e, target, panel){
	if (panel.editMode) {
		// 关闭模块列表
		if(selectModeWin.isVisible())
			selectModeWin.hide();
		
		//设置所有页签不可以删除
		Ext.each(panel.ownerCt.items.items,function(tabPanel){
			// 设置页签为不可编辑模式标志
			tabPanel.editMode = false;
			//页签不可编辑
			Ext.fly(tabPanel.tabEl).removeClass('x-tab-strip-closable');
		});
		/**
		 * 更新数据库信息 一条更新 还是所有全部更新
		 */
		//先删除页签的内容，在添加
		Ext.Ajax.request({
			url : basepath+ '/homePageSet!deleteTabPanelModeByTabID.json',
			params : {
				tabID : panel.tabid
			},
			method : "GET",
			waitMsg : '正在保存数据,请等待...',// 显示读盘的动画效果，执行完成后效果消失
			success : function(response) {

			},
			failure : function(response) {
				Ext.Msg.alert('提示','操作失败,失败原因:'+ response.responseText);
			}
		}); //Ext.Ajax.request
		for ( var i = 0; i < panel.items.items.length; i++) {
			itemTas = panel.items.items[i];
			itemTas.items.each(function(portlet,index) {
				Ext.Ajax.request({
							url : basepath+ '/homePageSet!updateTabPanelMode.json',
							params : {
								tabID : portlet.tabid,
								modID : portlet.modid,
								colNum : i,
								rowNum : index
							},
							method : "GET",
							waitMsg : '正在保存数据,请等待...',// 显示读盘的动画效果，执行完成后效果消失
							success : function(response) {
								Ext.Msg.alert('提示','保存成功！');
							},
							failure : function(response) {
								Ext.Msg.alert('提示','操作失败,失败原因:'+ response.responseText);
							}
						}); //Ext.Ajax.request
				//隐藏到关闭按钮
				portlet.getTool('close').hide();
			});//each
		}//end for  
		var ctPanel = panel.ownerCt.items.items[panel.ownerCt.items.length - 1];
		panel.ownerCt.remove(ctPanel,true);
	} else {
		Ext.Msg.alert('提示', "["+panel.title+ ']页签不是编辑模式,请在在编辑模式下保存!');
		return;
	}
}


/**
 * 页面布局控件
 */
Com.yucheng.crm.ModePage = Ext.extend(Ext.ux.Portal,{
		title : '',
		editMode : false,
		border : false,
		region : 'center',
		// frame : true,
		headerAsText : false,
		colnum : 2,
		userModleData : [], // 用户模块信息列表
		cls : 'x-portal',
		autoScroll : true,
		ddGroup : 'mygroup',
		tools : [{
					id : 'gear',
					qtip : '编辑模式',
					handler : editMode
				},{
					id : 'save',
					qtip : '保存结果',
					handler : saveMode
				} ],
		listeners : {
			render : function(tabPanEL) {
				/**
				 * ********************Drag and Drop zone**************************
				 */
				new Ext.dd.DropTarget(tabPanEL.body.dom,{
							ddGroup : 'previewmodel',
							notifyDrop : function(ddSource, e, data) {
								if (data.selections.length > 1) {
									Ext.Msg.alert('提示','只能选择一个模块!');
									return;
								}
								/**
								 * 查询数据库用户是否已存在该模块
								 */
								if (Ext.getCmp(data.selections[0].data.modID)) {
									Ext.Msg.alert('提示','该模块已经存在，请您选择其他模块！');
									return;
								}

								// 获取模块数量
								var modNum1 = tabPanEL.items.get(0).items.length;
								var modNum2 = tabPanEL.items.get(1).items.length;
								if (modNum1 + modNum2 >= 4) {
									Ext.Msg.alert('提示','添加模块失败,已超出该页签的模块数量！');
									return;
								}
								// 选择的模块信息
								var mod = data.selections[0].json;
								// 构建模块
								add_userPanelMode = new Com.yucheng.crm.ModePortal({
									id : mod.MOD_ID,
									title : mod.MOD_NAME,
									modid : mod.MOD_ID,
									tabid : tabPanEL.tabid,
									iconCls : mod.MOD_ICON,
									panelType : mod.MOD_TYPE,
									action : mod.MOD_ACTION,
									modCM : mod.MOD_CM,
									swfFile : mod.MOD_SWFFILE,
									collapsible : true,
									height : PANEL_WINDOW_HEIGHT,
									width : PANEL_WINDOW_WIDTH,
									tools : [ {
										id : 'close',
										handler : function(e, target,panel) {
											panel.ownerCt.remove(panel,true);
										}
									} ]
								});//end add_userPanelMode
								// 渲染模块
								add_userPanelMode.doLayoutPanel();

								// 判断模块的位置
								if ((modNum1 + modNum2) % 2 == 0)
									tabPanEL.items.items[0].add(add_userPanelMode);
								else
									tabPanEL.items.items[1].add(add_userPanelMode);

								// 渲染页签
								tabPanEL.doLayout();
							}
						});

			}, //beforeclose
			close :function(tab){ 
				var addPan = Ext.getCmp('newAddPanel');
				tab.ownerCt.stack.remove(addPan);
				Ext.Ajax.request({
					url : basepath + '/tabPanel!deleteTabPanByID.json?tabID='+tab.tabid,
					method : "GET",
					success : function(response) {
						
					}
				});
			}
			
			//加双击事件方法
			
		},

		onRender : function(ct, position) {
			Com.yucheng.crm.ModePage.superclass.onRender.call(this,ct, position);
			PANEL_WINDOW_WIDTH = ct.dom.clientWidth / 2 - 25;
			PANEL_WINDOW_HEIGHT = ct.dom.clientHeight / 2 - 25;
			modeThis = this;
			for ( var i = 0; i < modeThis.colnum; i++) {
				modeThis.add({
					columnWidth : .5,
					style : 'padding: 10px 10px 10px 10px'// 设置样式
				});
			}
			Ext.each(modeThis.userModleData,function(mod) {
				if (modeThis.tabid == mod.TABS_ID) {
					// 设置modepanel属性值
					var _userPanelMode = new Com.yucheng.crm.ModePortal({
								id : mod.MODILD_ID,
								title : mod.MOD_NAME,
								modid : mod.MODILD_ID,
								tabid : mod.TABS_ID,
								iconCls : mod.MOD_ICON,
								panelType : mod.MOD_TYPE,
								action : mod.MOD_ACTION,
								modCM : mod.MOD_CM,
								swfFile : mod.MOD_SWFFILE,
								collapsible : true,
								height : PANEL_WINDOW_HEIGHT,
								width : PANEL_WINDOW_WIDTH
							});
					_userPanelMode.doLayoutPanel();
					modeThis.items.items[mod.MODILD_COLUMN].add(_userPanelMode);
				} // end if
			}); // end Ext.each
		}// end initComponent
	});
// 注册控件
Ext.reg('modePage', Com.yucheng.crm.ModePage);


//页签控件
Com.yucheng.crm.TabPage = Ext.extend(Ext.TabPanel,{
					activeTab : 0, // 默认激活第一个tab页
					tabPosition : 'top', // 控制tab页签显示的位置（顶部：top；底部：bottom）
					height : document.documentElement.clientHeight,
					frame : true,
					border : false,
					enableTabScroll : true,// tab标签超宽时自动出现滚动按钮
					plain : false, // True表示tab候选栏上没有背景图片（默认为false）
					width : '100%',
					dateJsonUrl : '', // 获取数据的URL,
					editFun : //editMode,
					function () {
						alert('editFun');

						alert('editMode');
						//防止重复点击
//						if(panel.editMode){
//							Ext.Msg.alert('提示', '当前已经是编辑模式,请不要重复点击!');
//							return;
//						}
						//设置所有页签可以删除
						Ext.each(panel.ownerCt.items.items,function(tabPanel){
							// 设置页签为不可编辑模式标志
							tabPanel.editMode = true;
							//页签可编辑
							Ext.fly(tabPanel.tabEl).addClass('x-tab-strip-closable');
						});
							
						//当前页签模块可编辑 关闭
						Ext.each(panel.items.items, function(itemTas) {
							itemTas.items.each(function(portlet,index) {
								//打开关闭按钮
								portlet.getTool('close').show();
							});
						});
						
						// 打开模块列表
						if(!selectModeWin.isVisible())
							selectModeWin.show();
							
						// 增加添加面签的功能
						panel.ownerCt.add({
							title : '&nbsp;', //点击新增页签
							id:'newAddPanel',
							tabTip:'点击新增页签',
							layout : 'fit',
							listeners : {
								'activate' : function() {
									var index = tabThis.items.length;
									var tab = new Com.yucheng.crm.ModePage({
										title : "新增页签",
										closable : true, // 允许关闭
										tabid : '',
										editMode : true // 设置页签为可编辑模式标志,
									});
									Ext.Ajax.request({
										url : basepath
												+ '/tabPanel!addTabPanel.json?tabPanName='
												+ encodeURI(encodeURI(tab.title)),
										params : {
											tabNum : index - 1,
											layoutID : 'L00001' // 默认样式ID
										},
										method : "GET",
										success : function(response) {
											restInfo = Ext.util.JSON.decode(response.responseText).newTabPanID;
											tab.tabid = restInfo;
											tabThis.insert(index - 1,tab);
											tabThis.setActiveTab(index - 1); // 设置当前tab页	
											selectModeWin.show();
										},
										failure : function(response) {
											var resultArray = Ext.util.JSON.decode(response.status);
											if (resultArray == 403) {
												Ext.Msg.alert('提示',response.responseText);
											} else {
												Ext.Msg.alert('提示','操作失败,失败原因:'+ response.responseText);
											}
										}
									});

								}// 激动页签 功能
							}
						});	
							
							

					},
					saveFun : saveMode,
//					function () {
//						alert('saveFun');
//					},
					initComponent : function() {
						Com.yucheng.crm.TabPage.superclass.initComponent.call(this);
						tabThis = this;
						tabThis.removeAll();
						/**
						 * DOTO GET TABPANEL DATE
						 */
						Ext.Ajax.request({
							url : basepath + tabThis.dateJsonUrl,
							method : "GET",
							success : function(response) {
								userSetting = Ext.util.JSON.decode(response.responseText);
								var userPanel = userSetting.userTabPan.data;
								var userModle = userSetting.userMode.data;
								//无数据初始化时增加一个空页签
								if (userPanel == null || userPanel.length == 0) {
									// 增加添加面签的功能
									tabThis.add({
										title : '&nbsp;', //点击新增页签
										id:'newAddPanel',
										tabTip:'点击新增页签',
										layout : 'fit',
										listeners : {
											'activate' : function() {
												var index = tabThis.items.length;
												var tab = new Com.yucheng.crm.ModePage({
													title : "新增页签",
													closable : true, // 允许关闭
													tabid : '',
													editMode : true // 设置页签为可编辑模式标志,
												});
												Ext.Ajax.request({
													url : basepath
															+ '/tabPanel!addTabPanel.json?tabPanName='
															+ encodeURI(encodeURI(tab.title)),
													params : {
														tabNum : index - 1,
														layoutID : 'L00001' // 默认样式ID
													},
													method : "GET",
													success : function(response) {
														restInfo = Ext.util.JSON.decode(response.responseText).newTabPanID;
														tab.tabid = restInfo;
														tabThis.insert(index - 1,tab);
														tabThis.setActiveTab(index - 1); // 设置当前tab页	
														selectModeWin.show();
													},
													failure : function(response) {
														var resultArray = Ext.util.JSON.decode(response.status);
														if (resultArray == 403) {
															Ext.Msg.alert('提示',response.responseText);
														} else {
															Ext.Msg.alert('提示','操作失败,失败原因:'+ response.responseText);
														}
													}
												});

											}// 激动页签 功能
										}
									});	
								}			
								
								Ext.each(userPanel,function(tabPanel) {
									var _ModePage = new Com.yucheng.crm.ModePage({
												title : tabPanel.TABS_NAME,
												tabid : tabPanel.TABS_ID,
												userModleData : userModle,
												colnum : tabPanel.LAYOUT_COLNUM
											});// _ModePage
									
									//根据模式设置模块是否可编辑		
									_ModePage.on('activate',function(tab) {		
										if(tab.editMode){
											//当前页签模块可编辑 
											Ext.each(tab.items.items, function(itemTas) {
												itemTas.items.each(function(portlet,index) {
													//打开关闭按钮
													portlet.getTool('close').show();
												});
											});
										}else{
											//当前页签模块可编辑 关闭
											Ext.each(tab.items.items, function(itemTas) {
												itemTas.items.each(function(portlet,index) {
													//打开关闭按钮
													portlet.getTool('close').hide();
												});
											});
										}
									});
									
									tabThis.add(_ModePage); // end addtab
								});// end each
								tabThis.setActiveTab(0);
							},
							failure : function(action, form) {
								Ext.Msg.alert('提示', '您尚未做过首页设置！');
							}
						});// end Ext.Ajax.request

					},// end initComponent
					initEvents : function() {
						Ext.TabPanel.superclass.initEvents.call(this);
						this.mon(this.strip, 'mousedown',this.onStripMouseDown, this);
						this.mon(this.strip, 'contextmenu',this.onStripContextMenu, this);
						//monitor title dbclick
						this.mon(this.strip, 'dblclick', this.onTitleDbClick,this);
					},

					onTitleDbClick : function(e, target, o) { // 双击事件执行方法
						 tabObj = this.activeTab;
						 Ext.MessageBox.show({ 
							  title : '更改页签标题', 
							  msg : '请输入页签标题：',
							  modal : true, 
							  prompt : true, 
							  multiline : false, 
							  closable :false, 
							  buttons : Ext.Msg.OKCANCEL, 
							  icon : Ext.Msg.INFO, 
							  fn :function(id, title){
							 		 if ('ok' == id) { // 更改数据中页签的标题
							 			 Ext.Ajax.request({
							 				 url : basepath + '/tabPanel!updateTabPanName.json?tabPanName=' + encodeURI(encodeURI(title)), 
							 				 params : { 
							 					 tabID :tabObj.tabid 
							 				 }, 
							 				 method : "GET", 
							 				 waitMsg :'正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
							 				 success :function(response) {
							 					 tabObj.setTitle(title);  
							 				 },
							 				 failure :function(response) {
							 					 Ext.Msg.alert('提示','操作失败,失败原因:'+ response.responseText); 
							 				 } 
							 			 });// end Ext.Ajax.request
							 		 } // end OK
							 		 
							 }// end editTitle 
						 }); // 如果单击是，则更新TabPanel Title 
					}

				});

// 注册控件
Ext.reg('tabPage', Com.yucheng.crm.TabPage);

Com.yucheng.crm.ModListPanel = Ext.extend(Ext.grid.GridPanel, {
	width : '100%',
	height : 50,
	columns : [ {
		dataIndex : "modID",
		hidden : true,
		id : 'modID'
	}, {
		dataIndex : 'modName',
		sortable : true,
		id : 'modName'
	} ,{
		dataIndex : "modIcon",
		hidden : true,
		id : 'modIcon'
	},{
		dataIndex : "modType",
		hidden : true,
		id : 'modType'
	},{
		dataIndex : "modAction",
		hidden : true,
		id : 'modAction'
	},{
		dataIndex : "modSwfFile",
		hidden : true,
		id : 'modSwfFile'
	},{
		dataIndex : "modCM",
		hidden : true,
		id : 'modCM'
	}],
	autoExpandColumn : 'modName'
});
Ext.reg('modlist', Com.yucheng.crm.ModListPanel);
