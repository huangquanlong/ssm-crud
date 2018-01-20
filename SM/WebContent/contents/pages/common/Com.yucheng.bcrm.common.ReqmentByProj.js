Ext.ns('Com.yucheng.bcrm.common');
/**
 * 项目需求大镜
 * @author hujun
 * @since 2016-05-25
 * @modify 
 */
Com.yucheng.bcrm.common.ReqmentByProj = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.ux.form.SearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },
    onRender : function(ct, position){
		Com.yucheng.bcrm.common.ReqmentByProj.superclass.onRender.call(this, ct, position);
		if(this.hiddenName){
			var ownerForm = this;
			while(ownerForm.ownerCt && !Ext.instanceOf(ownerForm.ownerCt,'form')){				//根据条件查询放大镜控件的最外层容器
				ownerForm = ownerForm.ownerCt;
			};
			if(Ext.instanceOf(ownerForm.ownerCt,'form')){										//判断父容器是否为form类型
				ownerForm = ownerForm.ownerCt;
				if(ownerForm.getForm().findField(this.hiddenName)){								//如果已经创建隐藏域
					this.hiddenField = ownerForm.getForm().findField(this.hiddenName);
				}else {		//如果未创建隐藏域，则根据hiddenName属性创建隐藏域
					
					this.hiddenField = ownerForm.add({
						
						xtype : 'hidden',
						id:this.hiddenName,
						name: this.hiddenName
					});
				}
			}
		}
	},
	autoLoadFlag: false,
    singleSelected:false,//记录标志 true单选,false多选
    callback:false,
    projId:'',//项目ID 
    reqId :'',//需求ID
    reqDirId:'',//需求目录ID
    reqDirName:'',//需求目录名称
    reqName:'',//需求名称
    reqBaseLine:'',//需求基线
    reqVer:'',//需求版本
    potentialFlag:'',
    jobType:'',
    industType:'',
    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width:180,
    hasSearch : false,
    paramName : 'query',
    hiddenName:false, //用于存隐藏ID字段
    oCustomerQueryWindow : false,
    editable:false,
    allowTrigger:true,
    listeners:{//增加鼠标点击放大镜输入框触发onTrigger2Click事件
		focus:function(){
			if(!this.disabled&&this.allowTrigger){ //禁用的放大镜不允许弹出选择
				this.onTrigger2Click();
			}
		}
	},
    onTrigger2Click : function(){
    	//禁用的放大镜不允许弹出选择
		if(this.disabled||!this.allowTrigger){
			return ;
		}
    	var _this=this;
    	if(_this.oCustomerQueryWindow){
    		_this.oCustomerQueryWindow.show();
    		return;
    	}
    	var oThisSearchField=_this;
    	
    	var searchFunction = function(){
			var selectRe;
			var tempId;
			var idStr = '';
			if(reqMentLeftTreeForShow.getChecked()){
				for(var i=0;i<reqMentLeftTreeForShow.getChecked().length;i++){
					selectRe = reqMentLeftTreeForShow.getChecked()[i];
					tempId = selectRe.id;
					idStr += tempId;
					if( i != reqMentLeftTreeForShow.getChecked().length-1)
	                		idStr += ',';
				}
			}
			var parameters = _this.oCustomerQueryForm.getForm().getValues(false);
			_this.oCustomerQueryStore.removeAll();
			_this.oCustomerQueryStore.baseParams = {
					'condition':Ext.util.JSON.encode(parameters)
			};
			_this.oCustomerQueryStore.load({
					params:{
						'idStr':idStr,
						start:0,
						limit: parseInt(spagesize_combo.getValue())
					}
				});
			
		};
  
    	_this.boxstore = new Ext.data.Store({  
    		sortInfo: {
	    	    field: 'key',
	    	    direction: 'ASC' // or 'DESC' (case sensitive for local sorting)
	    	},
    		restful:true,   
    		autoLoad :true,
    		proxy : new Ext.data.HttpProxy({
    				url :basepath+'/lookup.json?name=REQ_STATE'
    		}),
    		reader : new Ext.data.JsonReader({
    			root : 'JSON'
    		}, [ 'key', 'value' ])
    	});
    	_this.boxstore.load();
    
    	var loader = new Com.yucheng.bcrm.ArrayTreeLoader({
			checkField : 'ASTRUE',//选择字段
			parentAttr : 'REQ_DIR_PARENT_NO',
			locateAttr : 'root',//UNITID
			rootValue :'0',
			textField : 'REQ_DIR_NAME',
			idProperties : 'REQ_DIR_ID',//节点点击事件句柄
			callback:function(){
				productLeftTreeForShow.expandAll();//默认展开树，解决前台取不到子节点，从而不能递归check问题
			}
		});
		var condition = {searchType:this.searchType};
		var filter = false;
		Ext.Ajax.request({//需求目录树数据加载
			url :basepath + '/ReqmentContetTreeInfoAction.json?projId='+this.projId,
			method:'GET',
			success:function(response){
				var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
				loader.nodeArray = nodeArra;
				var children = loader.loadAll();
				Ext.getCmp('productLeftTreeForShow').appendChild(children);
			}
		});
			
		var productLeftTreeForShow = new Com.yucheng.bcrm.TreePanel({//左侧显示的树
			id:'productLeftTreeForShow',
			height : document.body.clientHeight,
			width : 200,
			autoScroll:true,
			checkBox : false, //是否现实复选框：
			_hiddens : [],
			resloader:loader,//加载产品树
			region:'west',//布局位置设置
			split:true,
			root: new Ext.tree.AsyncTreeNode({//设置根节点
				id:'root',
				expanded:true,
				text:'需求目录树',
				autoScroll:true,
				children:[]
			}),
			clickFn:function(node){//单击事件，当单击树节点时触发并且获得这个节点的CATL_CODE
				if(node.attributes.REQ_DIR_ID == undefined){
					Ext.MessageBox.alert('提示', '不能选择根节点,请重新选择 !');
					return;
				}
				if(node.attributes.id!= ''){
					_this.nodeNow=node;
					_this.oCustomerQueryForm .getForm().findField('REQ_DIR_ID').
					setValue(node.attributes.REQ_DIR_ID);
					_this.oCustomerQueryStore.on('beforeload', function() {
						var conditionStr =  _this.oCustomerQueryForm.getForm().getValues(false);
						this.baseParams = {
								"condition":Ext.encode(conditionStr)
						};
					});
					_this.oCustomerQueryStore.load({//重新加载产品列表
						params:{
							limit:parseInt(_this.oCustomerQueryBbar.pageSize),
							start:0
						}
					});		
				}
			}
		}); 
    	_this.oCustomerQueryForm = new Ext.form.FormPanel({
			frame : true, //是否渲染表单面板背景色
			labelAlign : 'middle', // 标签对齐方式
			buttonAlign : 'center',
			region:'north',
			height : 110,
			width : 1000,
			items : [{
				layout : 'column',
				border : false,
				items : [{
					columnWidth : .5,
					layout : 'form',
					labelWidth : 80, // 标签宽度
					defaultType : 'textfield',
					border : false,
					items : [{
						fieldLabel : '需求名称',
						name : 'REQ_NAME',
						xtype : 'textfield', // 设置为数字输入框类型
						labelStyle: 'text-align:right;',
						anchor : '90%'
					}]
				},{
					columnWidth : .5,
					layout : 'form',
					labelWidth : 80, // 标签宽度
					defaultType : 'textfield',
					border : false,
					items : [{
						fieldLabel : '需求标识',
						name : 'REQ_NO',
						xtype : 'textfield', // 设置为数字输入框类型
						labelStyle: 'text-align:right;',
						anchor : '90%'
					}]
				}]
			}],
			buttons : [{
				text : '查询',
				handler : function() {
					_this.oCustomerQueryStore.removeAll();
					_this.oCustomerQueryStore.on('beforeload', function() {
						var conditionStr =  _this.oCustomerQueryForm.getForm().getValues(false);
						this.baseParams = {
								"condition":Ext.encode(conditionStr)
						};
					});
					_this.oCustomerQueryStore.reload({
						params : {
							start : 0,
							limit : _this.oCustomerQueryBbar.pageSize
						}
					});
				}
			},{
				text : '重置',
				handler : function() {
					_this.oCustomerQueryForm.getForm().reset();  
//					var childNodes = productLeftTreeForShow.getChecked();
//					if(productLeftTreeForShow.getChecked().length!=0){
//						for(var i = 0;i<childNodes.length;i++){
//							childNodes[i].getUI().toggleCheck(false);
//						}
//					}
				}
			}]
		});
    	_this.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:oThisSearchField.singleSelected});

    	// 定义自动当前页行号
    	_this.rownum = new Ext.grid.RowNumberer({
    		header : 'No.',
    		width : 28
    	});
	    
    	// 定义列模型
    	_this.cm = new Ext.grid.ColumnModel([_this.rownum,_this.sm, 
    	    {header : '需求ID',dataIndex : 'REQ_ID',sortable : true,width : 80,hidden:true},
    	    {header : '需求目录ID',dataIndex : 'REQ_DIR_ID',width : 150,sortable : true,hidden:true},
    	    {header : '需求名称',dataIndex : 'REQ_NAME',width : 140,sortable : true},
    	    {header : '需求标识',dataIndex : 'REQ_NO',width : 120,sortable : true},
    	    {header : '需求目录',dataIndex : 'REQ_DIR_NAME',width : 120,sortable : true},
    	    {header : '需求版本',dataIndex : 'REQ_VER',width : 80,sortable : true},
    	    {header : '需求状态',dataIndex : 'REQ_STATE',width : 80,sortable : true,hidden:true},
    	    {header : '需求状态',dataIndex : 'REQ_STATE_ORA',width : 80,sortable : true},
    	    {header : '创建人',dataIndex : 'CREATE_NAME',width : 80,sortable : true},
    	    {header : '创建时间',dataIndex : 'CREATE_DATE',width : 80,sortable : true}
    	]);
    	/**
    	 * 数据存储
    	 */
    	_this.oCustomerQueryStore = new Ext.data.Store({
    		restful:true,	
    		proxy : new Ext.data.HttpProxy({url:basepath+'/ReqmentBaseInfoQueryAction.json?projId='+this.projId}),
    		reader: new Ext.data.JsonReader({
    			totalProperty : 'json.count',
    			root:'json.data'
    		}, [{name: 'REQ_ID'},
    		    {name: 'PROJ_ID'},
    		    {name: 'REQ_DIR_ID'},
    		    {name: 'REQ_DIR_NAME'},
    		    {name: 'REQ_NO'},
    		    {name: 'REQ_NAME'},
    		    {name: 'REQ_BASELINE'},
    		    {name: 'REQ_VER'},
    		    {name: 'REQ_DESC'},
    		    {name: 'REQ_STATE'},
    		    {name: 'REQ_STATE_ORA'},
    		    {name: 'CREATE_NAME'},
    		    {name: 'CREATE_DATE'}
    		    ])
    	});

    	_this.oPagesizeCombo = new Ext.form.ComboBox({
    		name : 'pagesize',
    		triggerAction : 'all',
    		mode : 'local',
    		store : new Ext.data.ArrayStore({
    			fields : ['value', 'text'],
    			data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
    			         [ 100, '100条/页' ], [ 250, '250条/页' ],
    			         [ 500, '500条/页' ] ]
    		}),
    		valueField : 'value',
    		displayField : 'text',
    		value : '20',
    		editable : false,
    		width : 85
    	});
    	_this.number = parseInt(_this.oPagesizeCombo.getValue());
    	_this.oPagesizeCombo.on("select", function(comboBox) {
    		_this.oCustomerQueryBbar.pageSize = parseInt(_this.oPagesizeCombo.getValue()),
    		_this.oCustomerQueryStore.load({
    			params : {
    				start : 0,
    				limit : parseInt(_this.oPagesizeCombo.getValue())
    			}
    		});
    	});
    	_this.oCustomerQueryBbar = new Ext.PagingToolbar({
    		pageSize : _this.number,
    		store : _this.oCustomerQueryStore,
    		displayInfo : true,
    		displayMsg : '显示{0}条到{1}条,共{2}条',
    		emptyMsg : "没有符合条件的记录",
    		items : ['-', '&nbsp;&nbsp;', _this.oPagesizeCombo]
    	});
		// 表格实例
    	_this.oCustomerQueryGrid = new Ext.grid.GridPanel({
    		height : 275,
			width:1000,
			region:'center',
			frame : true,
			autoScroll : true,
			store : _this.oCustomerQueryStore, // 数据存储
			stripeRows : true, // 斑马线
			cm : _this.cm, // 列模型
			sm : _this.sm, // 复选框
			bbar:_this.oCustomerQueryBbar,
			viewConfig:{
    			forceFit:false,
    			autoScroll:true
    		},
    		loadMask : {
    			msg : '正在加载表格数据,请稍等...'
    		}
    	});

    	_this.oCustomerQueryWindow=new Ext.Window({
    		title : '项目需求查询',
    		closable : true,
    		resizable : true,
    		height:435,
    		width:1013,
    		draggable : true,
    		closeAction : 'hide',
    		modal : true, // 模态窗口 
    		border : true,
    		closable : true,
    		layout : 'border',
    		listeners : {
    			'show':function(){
					_this.oCustomerQueryForm.form.reset();
					_this.oCustomerQueryStore.removeAll();
					if(_this.autoLoadFlag)
						_this.oCustomerQueryStore.load({//如果自动加载，需要对数据进行分页
							params : {
								start : 0,
								limit : _this.oCustomerQueryBbar.pageSize
							}
						});
    			}    			
    		},
    		items:[productLeftTreeForShow,{
				region:'center',
				layout:'border',
				items:[_this.oCustomerQueryForm,{
					region:'center',
					layout:'fit',
					items:[_this.oCustomerQueryGrid]
				}]				
			}],
    		buttonAlign:'center',
    		buttons:[{
    			text:'确定',
    			handler:function(){
    				var sName='';
    				var checkedNodes = _this.oCustomerQueryGrid.getSelectionModel().selections.items;
    				if(!(_this.oCustomerQueryGrid.getSelectionModel().selections==null)){
    					if(oThisSearchField.hiddenField){
//    						checkedNodes = _this.oCustomerQueryGrid.getSelectionModel().selections.items;
						if(oThisSearchField.singleSelected&&checkedNodes.length>1){
							Ext.Msg.alert('提示', '您只能选择一个需求');
							return ;
						}
						for(var i=0;i<checkedNodes.length;i++){
							if(i==0){
								sName=checkedNodes[i].data.REQ_NAME;
								oThisSearchField.hiddenField.setValue(checkedNodes[i].data.REQ_ID);
							}else{
								sName=sName+','+checkedNodes[i].data.REQ_NAME;
								oThisSearchField.hiddenField.setValue(_this.hiddenField.value+','+checkedNodes[i].data.REQ_ID);
							}
						}
						oThisSearchField.setRawValue(sName);
						if(checkedNodes.length==1){//如果单选，则设置该客户相应的附属属性
							oThisSearchField.reqId=checkedNodes[0].data.REQ_ID;
							oThisSearchField.reqDirId=checkedNodes[0].data.REQ_DIR_ID;
							oThisSearchField.reqNo=checkedNodes[0].data.REQ_NO;
							oThisSearchField.reqName=checkedNodes[0].data.REQ_NAME;
							oThisSearchField.reqVer=checkedNodes[0].data.REQ_VER;
							oThisSearchField.reqDirName=checkedNodes[0].data.REQ_DIR_NAME;
						}
					}
    					/**
    					 * 如果未选择任何客户，清空隐藏域中的客户号 
    					 * by GuoChi
    					 * 2013-12-25
    					 */
    					if(_this.oCustomerQueryGrid.getSelectionModel().selections.length==0){
    						oThisSearchField.hiddenField.setValue("");
    					}	
    					
				}
				if (typeof oThisSearchField.callback == 'function') {
					oThisSearchField.callback(oThisSearchField,checkedNodes);
				}
				_this.oCustomerQueryWindow.hide();
			}
    	},{
    		text: '取消',
    		handler:function(){
    			oThisSearchField.setRawValue('');
				_this.oCustomerQueryWindow.hide();
    		}
    	}]	
    });
	_this.oCustomerQueryWindow.show();
    return;
    }
    
});
Ext.reg('reqmentByProj',Com.yucheng.bcrm.common.ReqmentByProj);