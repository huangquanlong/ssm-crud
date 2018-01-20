/**
 * @描述：项目查询放大镜
 * @author:wzy
 * @since:2015.02.03
 */
Ext.ns('Com.yucheng.crm.common');
Com.yucheng.crm.common.ProjectQuery = Ext.extend(Ext.form.TwinTriggerField, {
	initComponent : function() {
		Com.yucheng.crm.common.ProjectQuery.superclass.initComponent.call(this);
	},
	onRender : function(ct, position) {
		Com.yucheng.crm.common.ProjectQuery.superclass.onRender.call(this, ct,
				position);
		if (this.hiddenName) {
			var ownerForm = this;
			while (ownerForm.ownerCt
					&& !Ext.instanceOf(ownerForm.ownerCt, 'form')) { // 根据条件查询放大镜控件的最外层容器
				ownerForm = ownerForm.ownerCt;
			};
			if (Ext.instanceOf(ownerForm.ownerCt, 'form')) { // 判断父容器是否为form类型
				ownerForm = ownerForm.ownerCt;
				if (ownerForm.getForm().findField(this.hiddenName)) { // 如果已经创建隐藏域
					this.hiddenField = ownerForm.getForm()
							.findField(this.hiddenName);
				} else { // 如果未创建隐藏域，则根据hiddenName属性创建隐藏域
					this.hiddenField = ownerForm.add({
								xtype : 'hidden',
								id : this.hiddenName,
								name : this.hiddenName
							});
				}
			}
		}
	},
	hiddenName : false,
	singleSelect : '',
	callback : false,
	isNeedBaseLine : false,// 是否需要“项目基线类型”做为查询参数：true需要、false不需要，在引用放大镜的地方进行覆盖
	proBaseLineType : '',// “项目基线类型”参数值
	getParaBeforeQry : false,// 查询前获取查询参数方法，在引用放大镜的地方进行覆盖
	validationEvent : false,
	validateOnBlur : false,
	trigger1Class : 'x-form-clear-trigger',
	trigger2Class : 'x-form-search-trigger',
	hideTrigger1 : true,
	width : 180,
	listeners : {// 增加鼠标点击放大镜输入框触发onTrigger2Click事件
		focus : function() {
			if (!this.disabled) { // 禁用的放大镜不允许弹出选择
				this.onTrigger2Click();
			}
		}
	},
	onTrigger2Click : function() {
		if (this.disabled) { // 禁用的放大镜不允许弹出选择
			return;
		}
		var _this = this;
		if (typeof _this.getParaBeforeQry == 'function') {
			_this.getParaBeforeQry();
			// 重新设置要传入的查询参数的值
			if (_this.orgUserManageInfoStore) {
				_this.orgUserManageInfoStore.baseParams = {
					'proBaseLineType' : this.proBaseLineType
				};
			}
		}
		if (this.isNeedBaseLine
				&& (!this.proBaseLineType || this.proBaseLineType == null || this.proBaseLineType == "")) {
			Ext.Msg.alert('提示', '请先选择基线类型！');
			return false;
		}
		if (_this.projectQueryWindow) {
			_this.projectQueryWindow.show();
			return;
		}
		var searchFunction = function() {
			var parameters = _this.orgUserSearchPanel.getForm()
					.getFieldValues();
			_this.orgUserManageInfoStore.removeAll();
			_this.orgUserManageInfoStore.load({
						params : {
							'condition' : Ext.util.JSON.encode(parameters),
							start : 0,
							limit : parseInt(_this.pagesize_combo.getValue())
						}
					});
		};
		var searchField = _this;
		// 实施大区-下拉框数据源定义
		_this.areaStore = new Ext.data.Store({
					restful : true,
					autoLoad : true,
					proxy : new Ext.data.HttpProxy({
								url : basepath + '/lookup.json?name=APPLY_AREA'
							}),
					reader : new Ext.data.JsonReader({
								root : 'JSON'
							}, ['key', 'value'])
				});
		// 项目类型-下拉框数据源定义
		_this.pTypeStore = new Ext.data.Store({
					restful : true,
					autoLoad : true,
					proxy : new Ext.data.HttpProxy({
								url : basepath + '/lookup.json?name=PROJ_TYPE'
							}),
					reader : new Ext.data.JsonReader({
								root : 'JSON'
							}, ['key', 'value'])
				});
		// 项目状态-下拉框数据源定义
		_this.pStatusStore = new Ext.data.Store({
					restful : true,
					autoLoad : true,
					proxy : new Ext.data.HttpProxy({
								url : basepath
										+ '/lookup.json?name=PROJ_STATUS'
							}),
					reader : new Ext.data.JsonReader({
								root : 'JSON'
							}, ['key', 'value'])
				});
		// 查询panel
		_this.orgUserSearchPanel = new Ext.form.FormPanel({
			title : '项目查询',
			height : 140,
			labelWidth : 100,// label的宽度
			labelAlign : 'right',
			frame : true,
			autoScroll : true,
			region : 'north',
			split : true,
			items : [{
						layout : 'column',
						items : [{
									columnWidth : .33,
									layout : 'form',
									items : [{
												xtype : 'textfield',
												name : 'PROJ_NO',
												fieldLabel : '项目编号',
												anchor : '90%'
											}]
								}, {
									columnWidth : .33,
									layout : 'form',
									items : [{
												xtype : 'textfield',
												name : 'PROJ_NAME',
												fieldLabel : '项目名称',
												anchor : '90%'
											}]
								}, {
									columnWidth : .33,
									layout : 'form',
									items : [{
												name : 'PROJ_MANAGER_NAME',
												fieldLabel : '项目经理',
												xtype : 'userchoose',
												hiddenName : 'PROJ_MANAGER',
												singleSelect : true,
												anchor : '90%'
											}]
								}]
					}, {
						layout : 'column',
						items : [{
									columnWidth : .33,
									layout : 'form',
									items : [{
												store : _this.areaStore,
												xtype : 'combo',
												name : 'APPLY_AREA',
												hiddenName : 'APPLY_AREA',
												labelStyle : 'text-align:right;',
												fieldLabel : '实施大区',
												displayField : 'value',
												valueField : 'key',
												mode : 'local',
												editable : false,
												typeAhead : true,
												forceSelection : true,
												triggerAction : 'all',
												emptyText : '请选择',
												anchor : '90%'
											}]

								}, {
									columnWidth : .33,
									layout : 'form',
									items : [{
												store : _this.pTypeStore,
												xtype : 'combo',
												name : 'PROJ_TYPE',
												hiddenName : 'PROJ_TYPE',
												labelStyle : 'text-align:right;',
												fieldLabel : '项目类型',
												displayField : 'value',
												valueField : 'key',
												mode : 'local',
												editable : false,
												typeAhead : true,
												forceSelection : true,
												triggerAction : 'all',
												emptyText : '请选择',
												anchor : '90%'
											}]

								}, {
									columnWidth : .33,
									layout : 'form',
									items : [{
												store : _this.pStatusStore,
												xtype : 'combo',
												name : 'PROJ_STATUS',
												hiddenName : 'PROJ_STATUS',
												labelStyle : 'text-align:right;',
												fieldLabel : '项目状态',
												displayField : 'value',
												valueField : 'key',
												mode : 'local',
												editable : false,
												typeAhead : true,
												forceSelection : true,
												triggerAction : 'all',
												emptyText : '请选择',
												anchor : '90%'
											}]

								}]
					}],
			buttonAlign : 'center',
			buttons : [{
						text : '查询',
						handler : searchFunction
					}, {
						text : '重置',
						handler : function() {
							_this.orgUserSearchPanel.getForm().reset();
							_this.orgUserManageInfoStore.load({
										params : {
											start : 0,
											limit : parseInt(_this.pagesize_combo
													.getValue())
										}
									});
						}
					}]
		});
		// 复选框
		var sm = new Ext.grid.CheckboxSelectionModel({
					singleSelect : _this.singleSelect
				});
		// 定义自动当前页行号
		_this.rownum = new Ext.grid.RowNumberer({
					header : 'No.',
					width : 28
				});
		// gridtable中的列定义
		_this.orgUserInfoColumns = new Ext.grid.ColumnModel([_this.rownum, sm,
				{
					header : '项目ID',
					dataIndex : 'PROJ_ID',
					sortable : true,
					hidden : true
				}, {
					header : '项目编号',
					dataIndex : 'PROJ_NO',
					width : 140,
					sortable : true
				}, {
					header : '项目名称',
					dataIndex : 'PROJ_NAME',
					width : 170,
					sortable : true
				}, {
					header : '银行名称',
					dataIndex : 'BANK_NANE',
					width : 150,
					sortable : true,
					hidden : false
				}, {
					header : '项目经理',
					dataIndex : 'PROJ_MANAGER_NAME',
					width : 80,
					sortable : true,
					hidden : false
				}, {
					header : '实施大区',
					dataIndex : 'APPLY_AREA',
					width : 100,
					sortable : true,
					hidden : false
				}, {
					header : '项目类型',
					dataIndex : 'PROJ_TYPE',
					width : 100,
					sortable : true,
					hidden : false
				}, {
					header : '项目状态',
					dataIndex : 'PROJ_STATUS',
					width : 100,
					sortable : true,
					hidden : false
				}]);
		_this.orgUserInfoRecord = new Ext.data.Record.create([{
					name : 'PROJ_ID',
					mapping : 'PROJ_ID'
				}, {
					name : 'PROJ_NO',
					mapping : 'PROJ_NO'
				}, {
					name : 'PROJ_NAME',
					mapping : 'PROJ_NAME'
				}, {
					name : 'BANK_NANE',
					mapping : 'BANK_NANE'
				}, {
					name : 'PROJ_MANAGER_NAME',
					mapping : 'PROJ_MANAGER_NAME'
				}, {
					name : 'APPLY_AREA',
					mapping : 'APPLY_AREA_ORA'
				}, {
					name : 'PROJ_TYPE',
					mapping : 'PROJ_TYPE_ORA'
				}, {
					name : 'PROJ_STATUS',
					mapping : 'PROJ_STATUS_ORA'
				}]);
		// 读取json数据的panel
		_this.orgUserInfoReader = new Ext.data.JsonReader({
					totalProperty : 'json.count',
					root : 'json.data'
				}, _this.orgUserInfoRecord);

		_this.orgUserManageProxy = new Ext.data.HttpProxy({
					url : basepath + '/projectQueryAction.json'
				});

		_this.orgUserManageInfoStore = new Ext.data.Store({
					restful : true,
					baseParams : {
						'proBaseLineType' : this.proBaseLineType
					},
					proxy : _this.orgUserManageProxy,
					reader : _this.orgUserInfoReader,
					recordType : _this.orgUserInfoRecord
				});

		//查询前设置参数
		_this.orgUserManageInfoStore.on('beforeload', function(store) {
			var parameters = _this.orgUserSearchPanel.getForm().getFieldValues();
			if (_this.proBaseLineType && _this.proBaseLineType != null && _this.proBaseLineType != "") {
				_this.orgUserManageInfoStore.baseParams = {
					'proBaseLineType' : _this.proBaseLineType,
					'condition' : Ext.util.JSON.encode(parameters)
				};
			}
		}); 
		// 每页显示条数下拉选择框
		_this.pagesize_combo = new Ext.form.ComboBox({
					name : 'pagesize',
					triggerAction : 'all',
					mode : 'local',
					store : new Ext.data.ArrayStore({
								fields : ['value', 'text'],
								data : [[10, '10条/页'], [20, '20条/页'],
										[50, '50条/页'], [100, '100条/页'],
										[250, '250条/页'], [500, '500条/页']]
							}),
					valueField : 'value',
					displayField : 'text',
					value : '10',
					forceSelection : true,
					width : 85
				});

		var number = parseInt(_this.pagesize_combo.getValue());
		_this.pagesize_combo.on("select", function(comboBox) {
			_this.bbar.pageSize = parseInt(_this.pagesize_combo.getValue()), _this.orgUserManageInfoStore
					.load({
								params : {
									start : 0,
									limit : parseInt(_this.pagesize_combo
											.getValue())
								}
							});
		});
		_this.bbar = new Ext.PagingToolbar({
					pageSize : number,
					store : _this.orgUserManageInfoStore,
					displayInfo : true,
					displayMsg : '显示{0}条到{1}条,共{2}条',
					emptyMsg : "没有符合条件的记录",
					items : ['-', '&nbsp;&nbsp;', _this.pagesize_combo]
				});
		// 项目列表数据grid
		_this.orgUserManageGrid = new Ext.grid.GridPanel({
					frame : true,
					autoScroll : true,
					bbar : _this.bbar,
					stripeRows : true, // 斑马线
					store : _this.orgUserManageInfoStore,
					loadMask : true,
					cm : _this.orgUserInfoColumns,
					sm : sm,
					viewConfig : {
						forceFit : false,
						autoScroll : true
					},
					loadMask : {
						msg : '正在加载表格数据,请稍等...'
					}
				});

		_this.projectQueryWindow = new Ext.Window({
					title : '项目放大镜',
					closable : true,
					plain : true,
					resizable : false,
					collapsible : false,
					height : 450,
					width : 900,
					draggable : false,
					closeAction : 'hide',
					modal : true, // 模态窗口
					border : false,
					autoScroll : true,
					closable : true,
					animateTarget : Ext.getBody(),
					constrain : true,
					layout : 'border',
					buttonAlign : 'center',
					items : [{
								region : 'center',
								layout : 'border',
								items : [_this.orgUserSearchPanel, {
											region : 'center',
											layout : 'fit',
											items : [_this.orgUserManageGrid]
										}]
							}],
					buttons : [{
						text : '选定',
						handler : function() {
							var checkedNodes = _this.orgUserManageGrid
									.getSelectionModel().selections.items;
							if (_this.singleSelect && checkedNodes.length > 0) {
								_this.setValue(checkedNodes[0].data.PROJ_NAME);
								if (_this.hiddenField) {
									_this.hiddenField
											.setValue(checkedNodes[0].data.PROJ_ID);
								}
							} else {
								var sName = '';
								var json = '';
								if (checkedNodes.length > 0) {
									json = json + checkedNodes[0].data.PROJ_ID;
									sName = sName
											+ checkedNodes[0].data.PROJ_NAME;
								}
								for (var i = 1; i < checkedNodes.length; i++) {
									json = json + ','
											+ checkedNodes[i].data.PROJ_ID;
									sName = sName + ','
											+ checkedNodes[i].data.PROJ_NAME;
								};
								_this.setValue(sName);
								if (_this.hiddenField) {
									_this.hiddenField.setValue(json);
								}
							};
							_this.projectQueryWindow.hide();
							if (typeof searchField.callback == 'function') {
								searchField.callback(checkedNodes);
							}
						}
					}, {
						text : '取消',
						handler : function() {
							searchField.setRawValue('');// 清空文本框的值
							searchField.hiddenField.setRawValue('');// 清空隐藏字段的值
							_this.projectQueryWindow.hide();
						}
					}]
				});
		_this.projectQueryWindow.on('hide', function() {
					_this.orgUserSearchPanel.getForm().reset();
					_this.orgUserManageInfoStore.removeAll();
				});
		_this.projectQueryWindow.on('show', function() {
					searchFunction();
				});
		_this.projectQueryWindow.show();
		return;
	}
});
Ext.reg('projectquery', Com.yucheng.crm.common.ProjectQuery);