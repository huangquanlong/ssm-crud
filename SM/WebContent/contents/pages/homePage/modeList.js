/**
 * 客户查询结果数据存储
 */

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

// 模块数据信息
var store = new Ext.data.Store({
			restful : true,
			proxy : new Ext.data.HttpProxy({
						url : basepath + '/panelMode!getAllPanelMode.json'
					}),
			reader : new Ext.data.JsonReader({
						totalProperty : 'json.count',
						root : 'json.data'
					}, [{
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
							}])
		});

store.load();


var selectModeWin = new Ext.Window({
			title : '功能列表',
			resizable : false,
			collapsible : false,
			draggable : true,
			closeAction : 'hide',
			animCollapse : true,
			border : false,
			closable : true,
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
				// previewPan : 'centerPanel',
				hideHeaders : true
				/*
				tools : [{
							id : 'minus',
							qtip : '删除',
							handler : function(e, target, panel) {
								var onGrid = panel.getSelectionModel();
								if (onGrid.getSelections()) {
									var records = onGrid.getSelections();// 选择行的个数
									var recordsLen = records.length;// 得到行数组的长度
									if (recordsLen < 1) {
										Ext.Msg.alert("系统提示信息", "请选择其中一条记录！");
									} else {
										// 删除操作

										Ext.Msg.alert("系统提示信息", "删除成功！");
									}
								}
							}
						}, {
							id : 'plus',
							qtip : '增加',
							handler : function() {
								addeModePanel.getForm().reset();
								addModWin.show();
							}
						}, {
							id : 'save',
							qtip : '更新',
							handler : function(e, target, panel) {
								var selectLength = panel.getSelectionModel()
										.getSelections().length;
								var selectRe = panel.getSelectionModel()
										.getSelections()[0];
								if (selectLength != 1) {
									Ext.Msg.alert("系统提示信息", "请选择其中一条记录！");
								} else {
									addeModePanel.getForm()
											.loadRecord(selectRe);
									addModWin.show();
								}
							}
						}]*/
			}]
			
			

		});