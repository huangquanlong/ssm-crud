/**
 * @description 银行选择器
 * @author wangmk1
 * @since 2015-6-9
 */
Ext.ns('Com.yucheng.bcrm.common');
Com.yucheng.bcrm.common.BankQuery = Ext.extend(Ext.form.TwinTriggerField, {
	initComponent : function(){
		Com.yucheng.bcrm.common.BankQuery.superclass.initComponent.call(this);
	},
	onRender : function(ct, position){
		Com.yucheng.bcrm.common.BankQuery.superclass.onRender.call(this, ct, position);
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
						name: this.hiddenName
					});
				}
			}
		}
	},
	hiddenName:false, 
	singleSelect:true,
	callback:false,
	validationEvent:false,
	validateOnBlur:false,
	trigger1Class:'x-form-clear-trigger',
	trigger2Class:'x-form-search-trigger',
	hideTrigger1:true,
	width:180,
	editable:false,
	listeners:{//增加鼠标点击放大镜输入框触发onTrigger2Click事件
		focus:function(){
			if(!this.disabled&&!this.readOnly){ //禁用的放大镜不允许弹出选择
				this.onTrigger2Click();
			}
		}
	},
	onTrigger2Click : function(){
		//禁用的放大镜不允许弹出选择
		if(this.disabled||this.readOnly){
			return ;
		}
		var _this= this;
		if(_this.targetWindow){
			_this.targetWindow.show();
			return;
		}
		/**
		 * 查询方法定义
		 */
		var searchFunction = function(){
			var parameters = _this.targetPanel.getForm().getFieldValues(false);
			_this.targetStore.removeAll();
			_this.targetStore.load({
				params:{
					'condition':Ext.encode(parameters),
					start:0,
					limit: parseInt(_this.pagesize_combo.getValue())
				}
			});
		};
		var searchField = _this;
		/**
		 * 银行查询面板
		 */
		_this.targetPanel = new Ext.form.FormPanel({//查询panel
			height:90,
			labelWidth:100,//label的宽度
			labelAlign:'right',
			frame:true,
			autoScroll : true,
			region:'north',
			split:true,
			buttonAlign:'center',
			items:[{
				layout:'column',
				items:[{
					columnWidth:.5,
					layout:'form',
					items:[
						{xtype:'textfield',name:'BANK_NO',fieldLabel:'银行编号',anchor:'90%'}
					]
				},{
					columnWidth:.5,
					layout:'form',
					items:[
						{xtype:'textfield',name:'BANK_NAME',fieldLabel:'银行名称',anchor:'90%'}
					]
				}]
			}],
			buttons:[{
				text:'查询',
				handler:searchFunction
			},{
				text:'重置',
				handler:function(){
					_this.targetPanel.getForm().reset();
					searchFunction();
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
			width : 32
		});
		_this.targetCm = new Ext.grid.ColumnModel([
		    _this.rownum,sm,
		    {header:'银行编号',dataIndex:'BANK_NO',width:100,sortable : true},
		    {header:'银行名称',dataIndex:'BANK_NAME',width:150,sortable : true},
		    {header:'英文简称',dataIndex:'BANK_EN_ABBREVIATION',width:100,sortable : true},
		    {header:'银行类型',dataIndex:'BANK_TYPE_ORA',width:100,sortable : true},	
		    {header:'银行级别',dataIndex:'BANK_LEVEL_ORA',width:150,sortable : true}
		]);
		_this.targetRecord = new Ext.data.Record.create([
		    {name:'BANK_NO'},
		    {name:'BANK_NAME'},
		    {name:'BANK_EN_ABBREVIATION'},
		    {name:'BANK_TYPE_ORA'},
		    {name:'BANK_LEVEL_ORA'}
		]);
		_this.targetReader = new Ext.data.JsonReader({
			totalProperty:'json.count',
			root:'json.data'
		}, _this.targetRecord);
		
		_this.targetProxy = new Ext.data.HttpProxy({
			url:basepath+'/bankInfo.json?type=query'
		});
		_this.targetStore = new Ext.data.Store({
			restful : true,
			proxy : _this.targetProxy,
			reader :_this.targetReader,
			recordType: _this.targetRecord
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
		//当前分页条数
		var number = parseInt(_this.pagesize_combo.getValue());
		/**
		 * 监听分页下拉框选择事件
		 */
		_this.pagesize_combo.on("select", function(comboBox) {
			_this.bbar.pageSize = parseInt(_this.pagesize_combo.getValue()),
			searchFunction();
		});
		//分页工具条定义
		_this.bbar = new Ext.PagingToolbar({
			pageSize : number,
			store : _this.targetStore,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : ['-', '&nbsp;&nbsp;', _this.pagesize_combo]
		});
		/**
		 * 银行选择grid定义
		 */
		_this.targetGrid =  new Ext.grid.GridPanel({//产品列表数据grid
			frame:true,
			autoScroll : true,
			bbar:_this.bbar,
			stripeRows : true, // 斑马线
			store:_this.targetStore,
			loadMask:true,
			cm :_this.targetCm,
			sm :sm,
			region:'center',
			viewConfig:{
				forceFit:false,
				autoScroll:true
			},
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			}
		});
		
		/**
		 * 银行选择window定义
		 */
		_this.targetWindow=new Ext.Window({
			title : '银行选择',
			closable : true,
			plain : true,
			resizable : false,
			collapsible : false,
			height:400,
			width:760,
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
			items:[_this.targetPanel,_this.targetGrid],
			buttons:[{
				/**
				 * 选定银行操作
				 */
				text : '选定',
				handler : function() {
					var checkedNodes = _this.targetGrid.getSelectionModel().selections.items;
					if(_this.singleSelect && checkedNodes.length > 0) {
						_this.setValue(checkedNodes[0].data.BANK_NAME);
						if(_this.hiddenField){
							_this.hiddenField.setValue(checkedNodes[0].data.BANK_NO);
						}
					}else{
						var sName='';
						var json = '';
						if(checkedNodes.length > 0){
							json = json + checkedNodes[0].data.BANK_NO;
							sName = sName + checkedNodes[0].data.BANK_NAME;
						}
						for(var i=1;i<checkedNodes.length;i++){
							json = json + ',' + checkedNodes[i].data.BANK_NO;
							sName = sName + ',' + checkedNodes[i].data.BANK_NAME;
						}
						_this.setValue(sName);
						if(_this.hiddenField){
							_this.hiddenField.setValue(json);
						}
					};
					_this.targetWindow.hide();
					if(typeof searchField.callback == 'function') {
						searchField.callback(checkedNodes);
					}
				}
			},{
				text : '取消',
				handler : function() {
					searchField.setRawValue('');
					if(searchField.hiddenField){
						searchField.hiddenField.setValue('');
					}
					_this.targetWindow.hide();
				}
			}]
		});
		/**
		 * 添加事件监听,在隐藏银行面板时移除面板数据
		 */
		_this.targetWindow.on('hide',function(){
			_this.targetPanel.getForm().reset();
			_this.targetStore.removeAll();
		});
		_this.targetWindow.on('show',function(){
			searchFunction();
		});
		_this.targetWindow.show();
		return;
	}
});
Ext.reg('bankquery',Com.yucheng.bcrm.common.BankQuery);