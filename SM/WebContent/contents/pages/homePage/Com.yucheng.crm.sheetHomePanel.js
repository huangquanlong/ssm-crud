// 1、引入命名空间
Ext.namespace("Com.yucheng.crm.sheetHomePage");// 相当于java中包的作用

var PANEL_WINDOW_WIDTH;
var PANEL_WINDOW_HEIGHT;
/**
 * 
 * 传统版首页管理器
 * 
 * 
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter = function(dataUrl){
	
	var _this = this;
	_this.dataUrl = dataUrl;
	/**初始化*/
	_this.init();
};
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.dataUrl = null; //查询配置数据URL
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.editState = false;//当前编辑状态
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.homePage = null;//首页panel
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.homeTabPage = null;//首页tabPanel
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.defaultItem = null;//加号的那个 tabPanelItem
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.mainMask = new Ext.LoadMask(Ext.getBody(), {msg:'首页加载中...'});
/**
 * 初始化UI
 * @return
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.init = function() {
	var _this = this;
	if (_this.homePage == null) {
		_this.homePage = new Ext.Panel({
			title : '',
			layout : 'form',
			//tbar  : ['->',_this.getEditButton(), _this.getSaveButton()],
			items : [_this.getHomeTabPage()]
		});
	}
	Ext.Ajax.request({
		url : basepath + _this.dataUrl,
		method : "GET",
		success : function(response) {
			var cfgInfo   = Ext.util.JSON.decode(response.responseText);
			var userPanel = cfgInfo.tabCfg.data;
			var modeInfo  = cfgInfo.modeCfg.data;
			var itemSize = 0;
			Ext.each(userPanel,function(tabPanel) {
				var addModeInfo = new Array();
				Ext.each(modeInfo,function(curMode) {
					if (tabPanel.ID == curMode.TAB_ID) {
						addModeInfo.push(curMode);
					}
				});
				var _ModePage = new Com.yucheng.crm.ModePage({
					title : tabPanel.TAB_NAME,
					tabid : tabPanel.ID,
					userModleData : addModeInfo,
					colnum : 2,
					titleTemplate : new Ext.XTemplate('<input type="text" onfocus=this.select() name="titleInput" style=display:none;/>'),
					titleInput : new Ext.XTemplate('<div />'),
					listeners : {
						afterrender : function(tabPanEL) {
							this.titleInput = this.titleTemplate.append(this.tabEl);
							this.titleInput.value = this.title;
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
											var isExists = false;
											Ext.each(tabPanEL.items.items[0].items.items, function(modInfo) {
												if (modInfo.modid == data.selections[0].data.modID) {
													isExists = true;
													return;
												}
											});
											Ext.each(tabPanEL.items.items[1].items.items, function(modInfo) {
												if (modInfo.modid == data.selections[0].data.modID) {
													isExists = true;
													return;
												}
											});
											if (isExists) {
												Ext.Msg.alert('提示','该模块已经存在，请您选择其他模块！');
												return;
											}
											// 获取模块数量
											var modNum1 = tabPanEL.items.get(0).items.length;
											var modNum2 = tabPanEL.items.get(1).items.length;
											if (modNum1 + modNum2 >= 4) {
												//Ext.Msg.alert('提示','添加模块失败,已超出该页签的模块数量！');
												//return;
											}
											// 选择的模块信息
											var mod = data.selections[0].json;
											// 构建模块
											add_userPanelMode = new Com.yucheng.crm.ModePortal({
												//id : mod.MODEL_ID,
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
						}
					}
				});// _ModePage
				_ModePage.on('activate',function(tab) {		
					_this.setTabEdit();
				});
				_this.getHomeTabPage().add(_ModePage); // end addtab
				_this.getHomeTabPage().doLayout();
				_this.homeTabPage.setActiveTab(itemSize);
				itemSize++;
			});// end each
			_this.getHomeTabPage().setActiveTab(0);
		},
		failure : function(action, form) {
			Ext.Msg.alert('提示', '您尚未做过首页设置！');
		}
	});
};
/**
 * 增加页签
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.addTabItem = function() {
	var _this = this;
	var tab = new Com.yucheng.crm.ModePage({
		title : '新增页签',
		titleTemplate : new Ext.XTemplate('<input type="text" onfocus=this.select() name="titleInput" style=display:none;width:80; />'),
		closable : true, // 允许关闭
		titleInput : new Ext.XTemplate('<div />'),
		tabId : '', //页签ID
		listeners : {
			afterrender : function() {
				this.titleInput = this.titleTemplate.append(this.tabEl);
				this.titleInput.value = this.title;
			},
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
								var isExists = false;
								Ext.each(tabPanEL.items.items[0].items.items, function(modInfo) {
									if (modInfo.modid == data.selections[0].data.modID) {
										isExists = true;
										return;
									}
								});
								Ext.each(tabPanEL.items.items[1].items.items, function(modInfo) {
									if (modInfo.modid == data.selections[0].data.modID) {
										isExists = true;
										return;
									}
								});
								if (isExists) {
									Ext.Msg.alert('提示','该模块已经存在，请您选择其他模块！');
									return;
								}
	
								// 获取模块数量
								var modNum1 = tabPanEL.items.get(0).items.length;
								var modNum2 = tabPanEL.items.get(1).items.length;
								if (modNum1 + modNum2 >= 4) {
									//Ext.Msg.alert('提示','添加模块失败,已超出该页签的模块数量！');
									//return;
								}
								// 选择的模块信息
								var mod = data.selections[0].json;
								// 构建模块
								add_userPanelMode = new Com.yucheng.crm.ModePortal({
									//id : mod.MODEL_ID,
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
			}, //beforeClose
			close :function(a,b,tab){}
		}
	});
	var index = _this.homeTabPage.items.length;
	_this.homeTabPage.insert(index - 1,tab);
	_this.homeTabPage.setActiveTab(index - 1); 
};
/**
 * 取首页tabPanel
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.itemsResize = function() {
	var _this = this;
	Ext.each(_this.getTabItems(),function(tabPanel){
		if (tabPanel.id != 'defaultTabPanel' && tabPanel.items != null) {
			for ( var i = 0; i < tabPanel.items.items.length; i++) {
				itemTas = tabPanel.items.items[i];
				itemTas.items.each(function(portlet,index) {
					portlet.setWidth(_this.homeTabPage.getWidth()/2 - 25);
				});//each
			}//end for 
		}
	});	
};
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.getHomeTabPage = function() {
	var _this = this;
	if ( _this.homeTabPage == null) {
		_this.homeTabPage = new Ext.TabPanel({
			activeTab : 0, // 默认激活第一个tab页
			tabPosition : 'top', // 控制tab页签显示的位置（顶部：top；底部：bottom）
			height : document.documentElement.clientHeight,
			frame : true,
			title : 'homeTabPage',
			enableTabScroll : true,
			border : false,
			minTabWidth : 200,
			enableTabScroll : true,// tab标签超宽时自动出现滚动按钮
			plain : false, // True表示tab候选栏上没有背景图片（默认为false）
			width : '100%',
			initEvents : function() {
				Ext.TabPanel.superclass.initEvents.call(this);
				this.mon(this.strip, 'mousedown',this.onStripMouseDown, this);
				this.mon(this.strip, 'contextmenu',this.onStripContextMenu, this);
				this.mon(this.strip, 'dblclick', this.onTitleDbClick,this);
			},
			onTitleDbClick : function(e, target, o) {
				if (_this.editState) {
					var tabPanel = _this.getHomeTabPage().getActiveTab();
					var width = tabPanel.tabEl.clientWidth;
					var height = tabPanel.tabEl.clientHeight;
					tabPanel.titleInput.value = tabPanel.title;
					tabPanel.titleInput.className = 'titleInput';
					tabPanel.titleInput.style.display = '';
					tabPanel.titleInput.focus();
					Ext.fly(tabPanel.titleInput).setWidth(width-20);
					Ext.fly(tabPanel.titleInput).setHeight(height-4);
					Ext.fly(tabPanel.titleInput).on('blur',function(){
						tabPanel.setTitle(tabPanel.titleInput.value);
						tabPanel.titleInput.style.display = 'none';
					});
				}
			},
			onScrollRight : function(){
		        var sw = this.getScrollWidth()-this.getScrollArea(),
		            pos = this.getScrollPos(),
		            s = Math.min(sw, pos + this.getScrollIncrement());
		        s = s + 36;
		        if(s != pos){
		            this.scrollTo(s, this.animScroll);
		        }
		    },
		    
		    onScrollLeft : function(){
		        var pos = this.getScrollPos(),
		            s = Math.max(0, pos - this.getScrollIncrement());
		        if(s != pos){
		            this.scrollTo(s, this.animScroll);
		        }
		    },
			listeners : {
				'beforetabchange' : function(e,tab) {
						return true;
				},
				'resize' : function(){
					_this.itemsResize();
				},
				afterrender : function() {
					var setBtn = this.pos.insertFirst({
				            cls:'setCfg',
				            title : '编辑'
				        });
					var saveBtn = this.pos.insertFirst({
						cls:'saveCfg-disabled',
						title : ''
					});
					Ext.fly(setBtn).on('click',function(key, btn){
						if (_this.editState) {
							Ext.fly(setBtn).removeClass('cancelCfg');
							Ext.fly(setBtn).addClass('setCfg');
							Ext.fly(setBtn).dom.title = '编辑';
							Ext.fly(saveBtn).removeClass('saveCfg');
							Ext.fly(saveBtn).addClass('saveCfg-disabled');
							Ext.fly(saveBtn).dom.title = '';
							_this.editState = false;
						} else {
							Ext.fly(setBtn).removeClass('setCfg');
							Ext.fly(setBtn).addClass('cancelCfg');
							Ext.fly(setBtn).dom.title = '取消编辑';
							Ext.fly(saveBtn).removeClass('saveCfg-disabled');
							Ext.fly(saveBtn).addClass('saveCfg');
							Ext.fly(saveBtn).dom.title = '保存';
							_this.editState = true;
						}
						_this.editModelSwitch();
					});
					setBtn.setHeight(17);
					saveBtn.setHeight(17);
					Ext.fly(saveBtn).on('click',function(key, btn){
						if (_this.editState) {
							Ext.fly(setBtn).removeClass('cancelCfg');
							Ext.fly(setBtn).addClass('setCfg');
							Ext.fly(saveBtn).addClass('saveCfg-disabled');
							_this.saveFun();
						} 						
					});
				}
			}
		});
		_this.defaultItem = {
				title : '+',
				id:'defaultTabPanel',
				tabTip:'点击新增页签',
				layout : 'fit',
				listeners : {
					'activate' : function() {
						if (_this.editState) {
							if(_this.getTabItems().length > 1) {
								_this.homeTabPage.setActiveTab(_this.getTabItems().length - 1);
							} 
						} 
					},
					'afterrender' : function (tabPanel) {
						//增加click事件
						Ext.fly(tabPanel.tabEl).on('click',function(){
							 _this.addTabItem();
						});
					}
				}
			};
		
	}
	return _this.homeTabPage;
};
/**
 * 取页签Items
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.getTabItems = function() {
	var _this = this;
	return _this.homeTabPage.ownerCt.items.items[0].items.items;
};

/**
 * 开启页签编辑状态
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.setTabEdit = function() {
	var _this = this;
	if (_this.editState) {
		//设置所有页签可以辑模
		Ext.each(_this.getTabItems(),function(tabPanel){
			if (tabPanel.id != 'defaultTabPanel') {
				//页签可编辑
				Ext.fly(tabPanel.tabEl).addClass('x-tab-strip-closable');
				if (tabPanel.items != null) {
					Ext.each(tabPanel.items.items, function(itemTas) {
						itemTas.items.each(function(portlet,index) {
							//打开关闭按钮
							portlet.getTool('close').show();
						});
					});
				}
			}
		});
	} else {
		//设置所有页签不可以删除
		Ext.each(_this.getTabItems(),function(tabPanel){
			if (tabPanel.id != 'defaultTabPanel') {
				//页签不可编辑
				Ext.fly(tabPanel.tabEl).removeClass('x-tab-strip-closable');
				//当前页签模块可编辑 关闭
				if (tabPanel.items != null) {
					Ext.each(tabPanel.items.items, function(itemTas) {
						itemTas.items.each(function(portlet,index) {
							//打开关闭按钮
							portlet.getTool('close').hide();
						});
					});
				}
			}
		});
	}
};
/**
 * 
 * 编辑首页
 *  
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.addDefaultItem = function() {
	var _this = this;
	if (_this.homeTabPage.getItem('defaultTabPanel') == null) {
		_this.homeTabPage.add(_this.defaultItem);
	}
	_this.homeTabPage.doLayout();
};
/**
 * 
 * 编辑首页
 *  
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.editModelSwitch = function() {
	var _this = this;
	_this.setTabEdit();
	if (_this.editState) {
		//增加默认方块
		_this.addDefaultItem();
		// 打开模块列表
		if(!_this.getSelectModeWin().isVisible())
			_this.getSelectModeWin().show();
	} else {
		if(_this.getSelectModeWin().isVisible())
			_this.getSelectModeWin().hide();
		_this.homeTabPage.remove(_this.homeTabPage.getItem('defaultTabPanel'));
	}
};
/**
 * 
 * 保存首页
 *  
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.saveFun = function() {
	var _this = this;
	if (_this.editState) {
		// 关闭模块列表
		if(_this.getSelectModeWin().isVisible())
			_this.getSelectModeWin().hide();
		_this.setTabEdit();
		var cfgInfoArr = new Array();
		Ext.each(_this.getTabItems(),function(tabPanel){
			if (tabPanel.id != 'defaultTabPanel' && tabPanel.items != null) {
				for ( var i = 0; i < tabPanel.items.items.length; i++) {
					itemTas = tabPanel.items.items[i];
					itemTas.items.each(function(portlet,index) {
						var cfgInfoObj = {};
						cfgInfoObj.tabCmpId  = tabPanel.id;
						cfgInfoObj.tabTitle  = tabPanel.title;
						cfgInfoObj.tabId  = portlet.tabid;
						cfgInfoObj.modId  = portlet.modid;
						cfgInfoObj.colNum =  i;
						cfgInfoObj.rowNum = index;
						cfgInfoArr.push(cfgInfoObj);
						//隐藏到关闭按钮
						portlet.getTool('close').hide();
					});//each
				}//end for 
			}
		});
		Ext.Ajax.request({
			url : basepath+ '/sheetHomePageAction!updateCfgInfo.json',
			params : {
				cfgInfo : Ext.encode(cfgInfoArr)
			},
			method : "GET",
			//waitMsg : '正在保存数据,请等待...',// 显示读盘的动画效果，执行完成后效果消失
			success : function(response) {
				cfgInfoArr = new Array();
				Ext.Msg.alert('提示','保存成功！');
			},
			failure : function(response) {
				Ext.Msg.alert('提示','操作失败,失败原因:'+ response.responseText);
			}
		});
		
		_this.editState = false;
		_this.editModelSwitch();
		//_this.getEditButton().setText('编辑');
		//_this.getSaveButton().setDisabled(true);
	} else {
		Ext.Msg.alert('提示', '不是编辑模式,请在在编辑模式下保存!');
		return;
	}
};
/**
 * 模块面板win
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.selectModeWin = null;
/**
 * 模块面板win
 * @return selectModeWin
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.getSelectModeWin = function(){
	var _this = this;
	if (_this.selectModeWin != null) {
		return _this.selectModeWin;
	}

	// 模块类型
	var modeTypeDate = new Ext.data.ArrayStore({
				fields : ['key', 'value'],
				data : [[1, 'panel'], [2, 'js'], [3, 'grid'], [4, 'fusionChart'],
						[5, 'iframe']]
			});

	// 新增模块面板
	var addeModePanel = new Ext.form.FormPanel({
				height : 200,
				width : 350,
				labelWidth : 80,
				labelAlign : "right",
				frame : true,
				defaults : {
					xtype : "textfield",
					anchor : '90%'
				},
				items : [{
							xtype : "hidden",
							name : "modID",
							fieldLabel : "模块ID"
						}, {
							xtype : "textfield",
							name : "modName",
							fieldLabel : "模块标题"
						}, {
							xtype : "textfield",
							name : "modIcon",
							fieldLabel : "模块图标"
						}, {
							xtype : "combo",
							name : "modType",
							fieldLabel : "模块类型",
							store : modeTypeDate,
							displayField : 'value',
							valueField : 'key',
							mode : 'local',
							emptyText : '请选择 '
						}, {
							xtype : "textfield",
							name : "modAction",
							fieldLabel : "模块URL"
						}, {
							xtype : "textfield",
							name : "modSwfFile",
							fieldLabel : "模块SWF文件"
						}, {
							xtype : "textfield",
							name : "modCM",
							fieldLabel : "模块列名"
						}],
				buttons : [{
					text : "保存",
					handler : function(e, target, panel) {
						Ext.Ajax.request({
							url : basepath + '/panelMode!addMode.json',
							params:{
								modID  :addeModePanel.modID,
								modName:addeModePanel.modName,
								modIcon:addeModePanel.modIcon,
								modType:addeModePanel.modType,
								modAction:addeModePanel.modAction,
								modSwfFile:addeModePanel.modSwfFile,
								modCM:addeModePanel.modCM
							},
							waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
							success : function() {
								Ext.Msg.alert('提示', '操作成功', function() {store.reload();});
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
					}
				}, {
					text : "取消",
					handler : function() {
						addModWin.hide();
					}
				}]
			});

	// 新增模块
	var addModWin = new Ext.Window({
				resizable : false,
				collapsible : false,
				draggable : true,
				closeAction : 'hide',
				modal : true, // 模态窗口
				animCollapse : false,
				border : false,
				loadMask : true,
				closable : true,
				constrain : true,
				layout : 'fit',
				width : 350,
				height : 260,
				buttonAlign : "center",
				title : '模块信息',
				items : [addeModePanel]
			});

	
	
	
	//加载首页设置功能模块列表
	var listArray = [];
	Ext.Ajax.request({
		url : basepath + '/panelMode!getAllPanelMode.json',
		success : function(response) {
		var res = Ext.util.JSON.decode(response.responseText);
				for(var i=0;i<res.json.data.length;i++){
					if((JsContext.checkGrant(res.json.data[i].MOD_ID)==false)||(res.json.data[i].MOD_ID=='sysStatusMonitor')){
						listArray.push(res.json.data[i]);

					}
				}

				 store.loadData(listArray);
		},
		failure : function() {}
	});
	
	// 模块数据信息
	   var store = new Ext.data.Store({
	        reader:new Ext.data.JsonReader({
	            successProperty: 'success',
	            messageProperty: 'message',
	            fields:[
	                {
									name : 'modID',
									mapping : 'MOD_ID'
								}, {
									name : 'modName',
									mapping : 'MOD_NAME'
								}, {
									name : 'modIcon',
									mapping : 'MOD_ICON'
								}, {
									name : 'modType',
									mapping : 'MOD_TYPE'
								}, {
									name : 'modAction',
									mapping : 'MOD_ACTION'
								}, {
									name : 'modSwfFile',
									mapping : 'MOD_SWFFILE'
								}, {
									name : 'modCM',
									mapping : 'MOD_CM'
								}
	            ]
	        })
	    });

	
	_this.selectModeWin = new Ext.Window({
				title : '功能列表',
				resizable : false,
				collapsible : false,
				draggable : true,
				closeAction : 'hide',
				animCollapse : true,
				border : false,
				closable : false,
				collapsible : true,// 是否可收缩,
				constrain : false,
				layout : 'fit',
				width : 200,
				height : 420,
				floating : true,
				buttonAlign : "center",
				items : [{
					xtype : 'modlist',
					store : store,
					enableDD : true,
					ddGroup : 'previewmodel',
					enableDragDrop : true,
					hideHeaders : true
				}]
			});
	return _this.selectModeWin;
};
/**
 * edit按钮
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.editButton = null;
/**
 * 保存按钮
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.saveButton = null;
/**
 * 获取edit按钮
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.getEditButton = function() {
	var _this = this;
	if(_this.editButton == null) {
		_this.editButton = new Ext.Button({
								id : 'editBtn',
								text : '编辑',
								handler : function(btn, keyCode){
									if (_this.editState) {
										btn.setText('编辑');
										_this.editState = false;
										_this.getSaveButton().setDisabled(true);
									} else {
										btn.setText('取消编辑');
										_this.editState = true;
										_this.getSaveButton().setDisabled(false);
									}
									_this.editModelSwitch();
								}
						});
	}
	return _this.editButton;
};
/**
 * 获取保存按钮
 */
Com.yucheng.crm.sheetHomePage.SheetHomePageBooter.prototype.getSaveButton = function() {
	var _this = this;
	if(_this.saveButton == null) {
		_this.saveButton = new Ext.Button({
			id : 'saveBtn',
			text : '保存',
			disabled : true,
			handler : function() {
				_this.saveFun();
			}
		});
	}
	return _this.saveButton;
};
/**
 * ModePortal
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
 * 页面布局控件
 */
Com.yucheng.crm.ModePage = Ext.extend(Ext.ux.Portal,{
		title : '',
		border : false,
		region : 'center',
		// frame : true,
		titleTemplate : new Ext.XTemplate('<input type="text" onfocus=this.select() name="titleInput" style=display:none;/>'),
		titleInput : new Ext.XTemplate('<div />'),
		headerAsText : false,
		colnum : 2,
		userModleData : [], // 用户模块信息列表
		cls : 'x-portal',
		autoScroll : true,
		ddGroup : 'mygroup',
		tools : [],
		afterrender : function(){
			this.titleInput = this.titleTemplate.append(this.tabEl);
			this.titleInput.value = this.title;
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
				if (modeThis.tabid == mod.TAB_ID) {
					// 设置modepanel属性值
					var _userPanelMode = new Com.yucheng.crm.ModePortal({
								//id : mod.MODEL_ID,
								title : mod.MOD_NAME,
								modid : mod.MODEL_ID,
								tabid : mod.TAB_ID,
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
					modeThis.items.items[mod.MODEL_COLUMN].add(_userPanelMode);
				} // end if
			}); // end Ext.each
		}// end initComponent
	});
// 注册控件
Ext.reg('modePage', Com.yucheng.crm.ModePage);
/**
 * ModListPanel
 */
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
