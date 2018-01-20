/**
 * @描述：模型查询放大镜
 * @author：hujun
 * @since：2016.05.25
 */
Ext.ns('Com.yucheng.crm.common');
Com.yucheng.crm.common.ModelQuery = Ext.extend(Ext.form.TwinTriggerField, {
	initComponent : function(){
		Com.yucheng.crm.common.ModelQuery.superclass.initComponent.call(this);
	},

	onRender : function(ct, position){
		Com.yucheng.crm.common.ModelQuery.superclass.onRender.call(this, ct, position);
		if(this.hiddenName){
			var ownerForm = this;
			while(ownerForm.ownerCt && !Ext.instanceOf(ownerForm.ownerCt,'form')){				//根据条件查询放大镜控件的最外层容器
				ownerForm = ownerForm.ownerCt;
			};
			if(Ext.instanceOf(ownerForm.ownerCt,'form')){										//判断父容器是否为form类型
				ownerForm = ownerForm.ownerCt;
				if(ownerForm.getForm().findField(this.hiddenName)){								//如果已经创建隐藏域
					this.hiddenField = ownerForm.getForm().findField(this.hiddenName);
				}else {																			//如果未创建隐藏域，则根据hiddenName属性创建隐藏域
					this.hiddenField = ownerForm.add({
						xtype : 'hidden',
						id:this.hiddenName,
						name: this.hiddenName
					});
				}
			}
		}
		
	},
	hiddenName:false, 
	singleSelect:'',
	callback:false,
	projId:'',//项目ID
	projName:'',//项目名称
	mId:'',
	mNameCn:'',
	mNameEn:'',
	getParaBeforeQry : false,// 查询前获取查询参数方法，在引用放大镜的地方进行覆盖
	validationEvent:false,
	validateOnBlur:false,
	trigger1Class:'x-form-clear-trigger',
	trigger2Class:'x-form-search-trigger',
	hideTrigger1:true,
	width:180,
	searchType:'SUBTREE',//默认查询辖内机构
	searchRoleType:'',//默认查询全部角色信息
	hasSearch : false,
	paramName : 'query',
	listeners:{//增加鼠标点击放大镜输入框触发onTrigger2Click事件
		focus:function(){
			if(!this.disabled){ //禁用的放大镜不允许弹出选择
				this.onTrigger2Click();
			}
		}
	},
	editable:false,
	onTrigger2Click : function(){
		if(this.disabled){ //禁用的放大镜不允许弹出选择
			return;
		}
		var _this = this;
		if (typeof _this.getParaBeforeQry == 'function') {
			_this.getParaBeforeQry();
			// 重新设置要传入的查询参数的值
			if (_this.modelFieldQueryStore) {
				_this.modelFieldQueryStore.baseParams = {
					'PROJ_ID' : _this.projId//项目ID
				};
			}
		}
		if (!_this.projId || _this.projId == null || _this.projId == "") {
			Ext.Msg.alert('提示', '没有项目，不能查询项目模型！');
			return false;
		}
		if(_this.orgUserManageWindow){
			_this.orgUserManageWindow.show();
			return;
		}
		var searchFunction = function(){
			var parameters = _this.orgUserSearchPanel.getForm().getFieldValues();
			_this.modelFieldQueryStore.removeAll();
			_this.modelFieldQueryStore.load({
				params:{
					'condition':Ext.util.JSON.encode(parameters),
					start:0,
					limit: parseInt(_this.pagesize_combo.getValue())
				}
			});
		};
		var searchField = _this;
		_this.orgUserSearchPanel = new Ext.form.FormPanel({//查询panel
			title:'项目：'+_this.projName,
			height:100,
			labelWidth:100,//label的宽度
			labelAlign:'right',
			frame:true,
			autoScroll : true,
			region:'north',
			split:true,
			items:[{
				layout:'column',
				items:[{
					columnWidth:.5,
					layout:'form',
					items:[{
						xtype:'textfield',
						name:'MODEL_NAME_CH',
						fieldLabel:'模型中文名',
						anchor:'90%'
					}]
				},{
					columnWidth:.5,
					layout:'form',
					items:[{
						xtype:'textfield',
						name:'MODEL_NAME_EN',
						fieldLabel:'模型英文名',
						anchor:'90%'
					}]
				}
				]
			}],
			buttonAlign:'center',
			buttons:[{
				text:'查询',
				handler:searchFunction
			},{
				text:'重置',
				handler:function(){
					_this.orgUserSearchPanel.getForm().reset();
					_this.modelFieldQueryStore.load({
						params:{
							start:0,
							limit: parseInt(_this.pagesize_combo.getValue())
						}
					});
				}
			}]
		});
		//复选框
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect:_this.singleSelect,
			listeners:{
				'rowselect':function(sm,rowIndex,record){
					_this.fieldStore.load( {
						params : {
							modelId : record.data.ID
						},
						callback:function(){
						}
					});
				}
				}
		});
		// 定义自动当前页行号
		_this.rownum = new Ext.grid.RowNumberer({
			header : 'No.',
			width : 28
		});
		
		
		//模型列表
		_this.orgUserInfoColumns = new Ext.grid.ColumnModel([//gridtable中的列定义
		    _this.rownum,sm,
		    {header:'ID',dataIndex:'ID',id:'id',width:100,sortable : true,hidden : true},
		    {header:'模型中文名',dataIndex:'MODEL_NAME_CH',id:"modelNameCh",width:180,sortable : true},
		    {header:'模型英文名',dataIndex:'MODEL_NAME_EN',id:'modelNameEn',width:180,sortable : true}
		]);
		_this.orgUserInfoRecord = new Ext.data.Record.create([
		    {name:'ID',mapping:'ID'},
		    {name:'MODEL_NAME_CH',mapping:'MODEL_NAME_CH'},
		    {name:'MODEL_NAME_EN',mapping:'MODEL_NAME_EN'}
		]);
		_this.orgUserInfoReader = new Ext.data.JsonReader({//读取json数据的panel
			totalProperty:'json.count',
			root:'json.data'
		}, _this.orgUserInfoRecord);
		
		_this.orgUserManageProxy = new Ext.data.HttpProxy({
			url:basepath+'/ModelDesign!findModelList.json'
		});
		
		_this.modelFieldQueryStore = new Ext.data.Store({
			restful : true,
			baseParams:{
				'PROJ_ID' : this.projId
			},
			proxy : _this.orgUserManageProxy,
			reader :_this.orgUserInfoReader,
			recordType: _this.orgUserInfoRecord
		});
		
		
		//查询前设置参数
		_this.modelFieldQueryStore.on('beforeload', function(store) {
			var parameters = _this.orgUserSearchPanel.getForm().getFieldValues();
			if (_this.projId && _this.projId != null && _this.projId != "") {
				_this.modelFieldQueryStore.baseParams = {
					'PROJ_ID' : _this.projId,
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
				fields : [ 'value', 'text' ],
				data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
				         [ 100, '100条/页' ], [ 250, '250条/页' ],
				         [ 500, '500条/页' ] ]
			}),
			valueField : 'value',
			displayField : 'text',
			value : '10',
			forceSelection : true,
			width : 75
		});
		
		var number = parseInt(_this.pagesize_combo.getValue());
		_this.pagesize_combo.on("select", function(comboBox) {
			_this.bbar.pageSize = parseInt(_this.pagesize_combo.getValue());
			_this.modelFieldQueryStore.load({
				params : {
					start : 0,
					limit : parseInt(_this.pagesize_combo.getValue())
				}
			});
		});
		_this.bbar = new Ext.PagingToolbar({
			pageSize : number,
			store : _this.modelFieldQueryStore,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : ['-', '&nbsp;&nbsp;', _this.pagesize_combo]
		});

		_this.orgUserManageGrid =  new Ext.grid.GridPanel({//产品列表数据grid
			title:'模型选择',
			frame:true,
			autoScroll : true,
			bbar:_this.bbar,
			//width : 450,
			height : 240,
			region:'center',
			stripeRows : true, // 斑马线
			store:_this.modelFieldQueryStore,
			loadMask:true,
			cm :_this.orgUserInfoColumns,
			sm :sm,
			viewConfig:{
				forceFit:true,
				autoScroll:true
			},
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			}
		});
		
		_this.orgUserManageWindow=new Ext.Window({
			title : '模型字段查询',
			closable : true,
			plain : true,
			resizable : false,
			collapsible : false,
			height:420,
			width:600,
			draggable : false,
			closeAction : 'hide',
			modal : true, // 模态窗口 
			border : false,
			autoScroll : true,
			closable : true,
			animateTarget : Ext.getBody(),
			constrain : true,
			layout:'border',
			buttonAlign:'center',
			items:[_this.orgUserSearchPanel,_this.orgUserManageGrid],
			buttons:[{ 
				text : '选定',
				handler : function() {
					var checkedNodes = _this.orgUserManageGrid.getSelectionModel().selections.items;
					var sName='';
					var json = '';
					if(checkedNodes){
						if(_this.singleSelect && checkedNodes.length > 0) {
							
							json = checkedNodes[0].data.ID;
							sName = checkedNodes[0].data.MODEL_NAME_CH;
							_this.mId = checkedNodes[0].data.ID;
							_this.mNameCn = checkedNodes[0].data.MODEL_NAME_CH;
							_this.mNameEn = checkedNodes[0].data.MODEL_NAME_EN;
							_this.setValue(sName);
							if(_this.hiddenField){
								_this.hiddenField.setValue(json);
							}
						}else{
							if(checkedNodes.length > 0){
									json = json + checkedNodes[0].data.ID;
									sName = sName + checkedNodes[0].data.MODEL_NAME_CH;
								}
							for(var i=1;i<checkedNodes.length;i++){
									json = json + ',' + checkedNodes[i].data.ID;
									sName = sName + ',' + checkedNodes[i].data.MODEL_NAME_CH;
							};
							_this.setValue(sName);
							if(_this.hiddenField){
								_this.hiddenField.setValue(json);
							}
						};
					}
					

					_this.orgUserManageWindow.hide();
					if (typeof searchField.callback == 'function') {
						searchField.callback(checkedNodes);
					}
				}
			},{
				text : '取消',
				handler : function() {//只关闭放大镜窗体，不做任何逻辑处理
					_this.orgUserManageWindow.hide();
				}
			}]
		}); 
		_this.orgUserManageWindow.on('hide',function(){
			_this.orgUserSearchPanel.getForm().reset();
		});
		_this.orgUserManageWindow.on('show',function(){
			searchFunction();
		});
		_this.orgUserManageWindow.show();
		return;
	}
});
Ext.reg('modelquery',Com.yucheng.crm.common.ModelQuery);