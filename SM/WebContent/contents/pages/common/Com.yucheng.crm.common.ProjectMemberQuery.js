/**
 * @描述：项目成员查询放大镜
 * @author：wzy
 * @since：2015.02.07
 */
Ext.ns('Com.yucheng.crm.common');
Com.yucheng.crm.common.ProjectMemberQuery = Ext.extend(Ext.form.TwinTriggerField, {
	initComponent : function(){
		Com.yucheng.crm.common.ProjectMemberQuery.superclass.initComponent.call(this);
	},

	onRender : function(ct, position){
		Com.yucheng.crm.common.ProjectMemberQuery.superclass.onRender.call(this, ct, position);
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
	userId:'',
	orgId:'', 
	projId:'',//项目ID
	projName:'',//项目名称
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
			if (_this.projectMemberQueryStore) {
				_this.projectMemberQueryStore.baseParams = {
					'proj_id' : _this.projId//项目ID
				};
			}
		}
		if (!_this.projId || _this.projId == null || _this.projId == "") {
			Ext.Msg.alert('提示', '没有项目，不能查询项目成员！');
			return false;
		}
		if(_this.orgUserManageWindow){
			_this.orgUserManageWindow.show();
			return;
		}
		var searchFunction = function(){
			var parameters = _this.orgUserSearchPanel.getForm().getFieldValues();
			_this.projectMemberQueryStore.removeAll();
			_this.projectMemberQueryStore.load({
				params:{
					'condition':Ext.util.JSON.encode(parameters),
					start:0,
					limit: parseInt(_this.pagesize_combo.getValue())
				}
			});
		};
		var searchField = _this;
		_this.proleCodeStore = new Ext.data.Store({
			restful:true,   
			autoLoad :true,
			proxy : new Ext.data.HttpProxy({
				url :basepath+'/lookup.json?name=PROJ_ROLE_CODE'
			}),
			reader : new Ext.data.JsonReader({
				root : 'JSON'
			}, [ 'key', 'value' ])
		}); 
		_this.orgUserSearchPanel = new Ext.form.FormPanel({//查询panel
			title:'项目：'+_this.projName,
			height:105,
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
						name:'USER_NAME',
						fieldLabel:'成员账号/名称',
						anchor:'90%'
					}]
				},{
					columnWidth:.5,
					layout:'form',
					items:[{
						store : _this.proleCodeStore,
						xtype : 'combo',
						name : 'PROLE_CODE',
						hiddenName : 'PROLE_CODE',
						labelStyle: 'text-align:right;',
						fieldLabel : '成员角色',
						valueField : 'key',
						displayField : 'value',
						mode : 'local',
						editable:false,
						typeAhead : true,
						forceSelection : true,
						triggerAction : 'all',
						emptyText : '请选择',
						anchor : '90%'
					}]
				}]
			}],
			buttonAlign:'center',
			buttons:[{
				text:'查询',
				handler:searchFunction
			},{
				text:'重置',
				handler:function(){
					_this.orgUserSearchPanel.getForm().reset();
					_this.projectMemberQueryStore.load({
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
			singleSelect:_this.singleSelect
		});
		// 定义自动当前页行号
		_this.rownum = new Ext.grid.RowNumberer({
			header : 'No.',
			width : 28
		});
		_this.orgUserInfoColumns = new Ext.grid.ColumnModel([//gridtable中的列定义
		    _this.rownum,sm,
		    {header:'ID',dataIndex:'id',id:'id',width:100,sortable : true,hidden : true},
		    {header:'成员账号',dataIndex:'userId',id:"userId",width:100,sortable : true},
		    {header:'成员姓名',dataIndex:'userName',id:'userName',width:100,sortable : true},
		    {header:'成员角色',dataIndex:'PROJ_ROLE_CODE',id:'PROJ_ROLE_CODE',width:150,sortable : true},
		    {header:'所属机构号',dataIndex:'orgId',id:'orgId',width:150,sortable : true,hidden:false},	
		    {header:'所属机构名称',dataIndex:'orgName',id:'orgName',width:200,sortable : true,hidden:false}
		]);
		_this.orgUserInfoRecord = new Ext.data.Record.create([
		    {name:'id',mapping:'ID'},
		    {name:'userId',mapping:'ACCOUNT_NAME'},
		    {name:'userName',mapping:'USER_NAME'},
		    {name:'orgId',mapping:'ORG_ID'},
		    {name:'PROJ_ROLE_CODE',mapping:'PROJ_ROLE_CODE_ORA'},
		    {name:'orgName',mapping:'ORG_NAME'}
		]);
		_this.orgUserInfoReader = new Ext.data.JsonReader({//读取json数据的panel
			totalProperty:'json.count',
			root:'json.data'
		}, _this.orgUserInfoRecord);
		
		_this.orgUserManageProxy = new Ext.data.HttpProxy({
			url:basepath+'/projectMemberQueryAction.json'
		});
		_this.projectMemberQueryStore = new Ext.data.Store({
			restful : true,
			baseParams:{
				'proj_id' : this.projId
			},
			proxy : _this.orgUserManageProxy,
			reader :_this.orgUserInfoReader,
			recordType: _this.orgUserInfoRecord
		});
		//查询前设置参数
		_this.projectMemberQueryStore.on('beforeload', function(store) {
			var parameters = _this.orgUserSearchPanel.getForm().getFieldValues();
			if (_this.projId && _this.projId != null && _this.projId != "") {
				_this.projectMemberQueryStore.baseParams = {
					'proj_id' : _this.projId,
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
			width : 85
		});
		
		var number = parseInt(_this.pagesize_combo.getValue());
		_this.pagesize_combo.on("select", function(comboBox) {
			_this.bbar.pageSize = parseInt(_this.pagesize_combo.getValue());
			_this.projectMemberQueryStore.load({
				params : {
					start : 0,
					limit : parseInt(_this.pagesize_combo.getValue())
				}
			});
		});
		_this.bbar = new Ext.PagingToolbar({
			pageSize : number,
			store : _this.projectMemberQueryStore,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : ['-', '&nbsp;&nbsp;', _this.pagesize_combo]
		});
		_this.orgUserManageGrid =  new Ext.grid.GridPanel({//产品列表数据grid
			frame:true,
			autoScroll : true,
			bbar:_this.bbar,
			stripeRows : true, // 斑马线
			store:_this.projectMemberQueryStore,
			loadMask:true,
			cm :_this.orgUserInfoColumns,
			sm :sm,
			viewConfig:{
				forceFit:false,
				autoScroll:true
			},
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			}
		});
			
		_this.orgUserManageWindow=new Ext.Window({
			title : '项目成员查询',
			closable : true,
			plain : true,
			resizable : false,
			collapsible : false,
			height:450,
			width:800,
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
			items:[
			    {
					region:'center',
					layout:'border',
					items:[_this.orgUserSearchPanel,
					    {
							region:'center',
							layout:'fit',
							items:[_this.orgUserManageGrid]
					    }]				
			    }],
			buttons:[{ 
				text : '选定',
				handler : function() {
					var checkedNodes = _this.orgUserManageGrid.getSelectionModel().selections.items;
					if(checkedNodes){
						if(_this.singleSelect && checkedNodes.length > 0) {
							_this.setValue(checkedNodes[0].data.userName);
							if(_this.hiddenField){
								_this.hiddenField.setValue(checkedNodes[0].data.userId);
							}
						}else{
							var sName='';
							var json = '';
							if(checkedNodes.length > 0){
									json = json + checkedNodes[0].data.userId;
									sName = sName + checkedNodes[0].data.userName;
								}
							for(var i=1;i<checkedNodes.length;i++){
									json = json + ',' + checkedNodes[i].data.userId;
									sName = sName + ',' + checkedNodes[i].data.userName;
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
			_this.projectMemberQueryStore.removeAll();
		});
		_this.orgUserManageWindow.on('show',function(){
			searchFunction();
		});
		_this.orgUserManageWindow.show();
		return;
	}
});
Ext.reg('projectmemberquery',Com.yucheng.crm.common.ProjectMemberQuery);