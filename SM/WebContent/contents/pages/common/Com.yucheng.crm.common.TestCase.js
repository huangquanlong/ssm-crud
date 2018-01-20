/**
 * 放大镜查询机构用户
 * @author:wangwan
 * @since:2012.11.08
 */
Ext.ns('Com.yucheng.crm.common');
Com.yucheng.crm.common.TestCase = Ext.extend(Ext.form.TwinTriggerField, {
	initComponent : function(){
		Com.yucheng.crm.common.TestCase.superclass.initComponent.call(this);
	},

	onRender : function(ct, position){
		Com.yucheng.crm.common.TestCase.superclass.onRender.call(this, ct, position);
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
		var _this= this;
		if (typeof _this.getParaBeforeQry == 'function') {
			_this.getParaBeforeQry();
			// 重新设置要传入的查询参数的值
			if (_this.testCaseInfoStore) {
				_this.testCaseInfoStore.baseParams = {
					'proj_id' : _this.projId//项目ID
				};
			}
		}
		if (!_this.projId || _this.projId == null || _this.projId == "") {
			Ext.Msg.alert('提示', '没有项目，不能查询项目测试用例！');
			return false;
		}
		if(_this.testCaseWindow){
			_this.testCaseWindow.show();
			return;
		}
		var searchFunction = function(){
			var parameters = _this.testCaseSearchPanel.getForm().getFieldValues();
			_this.testCaseInfoStore.removeAll();
			_this.testCaseInfoStore.load({
				params:{
					'condition':Ext.util.JSON.encode(parameters),
					start:0,
					limit: parseInt(_this.pagesize_combo.getValue())
				}
			});
		};
		var searchField = _this;
		_this.loader = new Com.yucheng.bcrm.ArrayTreeLoader({
			parentAttr : 'REQ_DIR_PARENT_NO',
			locateAttr : 'REQ_DIR_ID',
			rootValue : _this.projId,
			textField : 'REQ_DIR_NAME',
			idProperties : 'REQ_DIR_ID'
		});
		var filter = false;
		/***********************机构树********************************/
		Ext.Ajax.request({
			url : basepath + '/TestCaseTree.json?projId='+_this.projId,
			method:'GET',
			success:function(response){
				var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
				_this.loader.nodeArray = nodeArra;
				var children = _this.loader.loadAll();
				_this.testCaseTreeForShow.appendChild(children);
			},failure:function(a,b,c){}
		});
		_this.testCaseTreeForShow = new Com.yucheng.bcrm.TreePanel({
			width:'25%',
			heigth: 400,
			/**虚拟树形根节点*/
			root: new Ext.tree.AsyncTreeNode({
				id :_projId,
				text:_projName,
				expanded:true,
				autoScroll:true,
				children:[]
			}),
			resloader:_this.loader,
			region:'west',
			split:true,
			listeners:{
				'click':function(node){
					_this.testCaseInfoStore.removeAll();
					var id = node.id;
					Ext.getCmp('treenode').setValue(id);//
					searchFunction();
				}		
			}	
		});

		_this.reqPanel = new Ext.form.FormPanel({//查询panel
			width:'25%',
			height:150,
			frame:true,
			autoScroll : true,
			region:'west',
			split:true,
			items:[_this.testCaseTreeForShow]
		});
		var condition_role ={searchForRoleType:this.searchRoleType};
			    		
		_this.testCaseSearchPanel = new Ext.form.FormPanel({//查询panel
			title:'用例查询',
			height:110,
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
						name:'CASE_NAME',
						fieldLabel:'测试用例名称',
						anchor:'90%'
					}]
				},{
					columnWidth:.5,
					layout:'form',
					items:[{
						xtype:'textfield',
						name:'REQ_ID',
						id:'treenode',
						fieldLabel:'需求ID',
						anchor:'90%',
						hidden:true,
						value:_this.projId
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
					_this.testCaseSearchPanel.getForm().reset();
					_this.testCaseInfoStore.load({
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
		_this.caseInfoColumns = new Ext.grid.ColumnModel([//gridtable中的列定义
		    _this.rownum,sm,
		    {header:'需求ID',dataIndex:'reqId',id:'reqId',sortable : true,hidden : true},
		    {header:'需求名称',dataIndex:'reqName',id:"reqName",sortable : true},
		    {header:'用例ID',dataIndex:'caseId',id:'caseId',sortable : true,hidden : true},
		    {header:'用例名称',dataIndex:'caseName',id:'caseName',sortable : true}
		]);
		_this.caseInfoRecord = new Ext.data.Record.create([
		    {name:'reqId',mapping:'REQ_ID'},
		    {name:'reqName',mapping:'REQ_NAME'},
		    {name:'caseId',mapping:'CASE_ID'},
		    {name:'caseName',mapping:'CASE_NAME'}
		]);
		_this.caseInfoReader = new Ext.data.JsonReader({//读取json数据的panel
			totalProperty:'json.count',
			root:'json.data'
		}, _this.caseInfoRecord);
		
		_this.testCaseProxy = new Ext.data.HttpProxy({
			url:basepath+'/testCaseInfo.json?proj_Id='+_this.projId
		});
		_this.testCaseInfoStore = new Ext.data.Store({
			restful : true,
			proxy : _this.testCaseProxy,
			reader :_this.caseInfoReader,
			recordType: _this.caseInfoRecord
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
			value : '20',
			forceSelection : true,
			width : 85
		});
			
		var number = parseInt(_this.pagesize_combo.getValue());
		_this.pagesize_combo.on("select", function(comboBox) {
			_this.bbar.pageSize = parseInt(_this.pagesize_combo.getValue()),
			_this.testCaseInfoStore.load({
				params : {
					start : 0,
					limit : parseInt(_this.pagesize_combo.getValue())
				}
			});
		});
		_this.bbar = new Ext.PagingToolbar({
			pageSize : number,
			store : _this.testCaseInfoStore,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : ['-', '&nbsp;&nbsp;', _this.pagesize_combo]
		});
		_this.testCaseGrid =  new Ext.grid.GridPanel({//产品列表数据grid
			frame:true,
			autoScroll : true,
			bbar:_this.bbar,
			stripeRows : true, // 斑马线
			store:_this.testCaseInfoStore,
			loadMask:true,
			cm :_this.caseInfoColumns,
			sm :sm,
			viewConfig:{
				forceFit:false,
				autoScroll:true
			},
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			}
		});
			
		_this.testCaseWindow=new Ext.Window({
			title : '需求结构',
			closable : true,
			plain : true,
			resizable : false,
			collapsible : false,
			height:400,
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
			items:[_this.reqPanel,
			    {
					region:'center',
					layout:'border',
					items:[_this.testCaseSearchPanel,
					    {
							region:'center',
							layout:'fit',
							items:[_this.testCaseGrid]
					    }]				
			    }],
			buttons:[{ 
				text : '选定',
				handler : function() {
					var checkedNodes = _this.testCaseGrid.getSelectionModel().selections.items;
					if(_this.singleSelect && checkedNodes.length > 0) {
						_this.setValue(checkedNodes[0].data.caseName);
						if(_this.hiddenField){
							_this.hiddenField.setValue(checkedNodes[0].data.caseId);
						}
						_this.orgId=checkedNodes[0].data.orgId;
					}else{
						var sName='';
						var json = '';
						if(checkedNodes.length > 0){
								json = json + checkedNodes[0].data.caseId;
								sName = sName + checkedNodes[0].data.caseName;
							}
						for(var i=1;i<checkedNodes.length;i++){
								json = json + ',' + checkedNodes[i].data.caseId;
								sName = sName + ',' + checkedNodes[i].data.caseName;
						};
						_this.setValue(sName);
						if(_this.hiddenField){
							_this.hiddenField.setValue(json);
						}
					};
					_this.testCaseWindow.hide();
					if (typeof searchField.callback == 'function') {
						searchField.callback(checkedNodes);
					}
				}
			},{
				text : '取消',
				handler : function() {
					searchField.setRawValue('');
					_this.testCaseWindow.hide();
				}
			}]
		}); 
		_this.testCaseWindow.on('hide',function(){
			_this.testCaseSearchPanel.getForm().reset();
			_this.testCaseInfoStore.removeAll();
		});
		_this.testCaseWindow.on('show',function(){
			searchFunction(); 
		});
		_this.testCaseWindow.show();
		return;
	}
});
Ext.reg('testcase',Com.yucheng.crm.common.TestCase);