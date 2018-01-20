/**
 * @description 放大镜
 * @author wangmk1
 * @since 2016-3-23
 */
Ext.ns('Com.yucheng.bcrm.common');
Com.yucheng.bcrm.common.GlassTrigger = Ext.extend(Ext.ux.form.SearchField, {
	singleSelect:false,
	readOnly:false,
	winWidth:760,
	winHeight:400,
	winTitle:'模型放大镜',
	baseParams:{projId:_projId},
	url :basepath + '/reqmentModelInfo!findResult.json',
	gridRoot:'json.data',
	searchPanel:new Ext.Panel({
				layout:'column',
				items:[{
					columnWidth:.5,
					layout:'form',
					items:[
						{xtype:'textfield',name:'FIELD_NAME_CH',fieldLabel:'字段中文名',anchor:'90%'}
					]
				},{
					columnWidth:.5,
					layout:'form',
					items:[
						{xtype:'textfield',name:'FIELD_NAME_EN',fieldLabel:'字段英文名',anchor:'90%'}
					]
				}]
			}),
	columnConfig: [
			{header:'模型中文名',dataIndex:'MODEL_NAME_CH',width:100,hidden:false,sortable : true},
			{header:'模型英文名',dataIndex:'MODEL_NAME_EN',width:100,hidden:false,sortable : true},
			{header:'字段中文名',dataIndex:'FIELD_NAME_CH',width:100,hidden:false,sortable : true},
		    {header:'字段英文名',dataIndex:'FIELD_NAME_EN',width:100,hidden:false,sortable : true},
		    {header:'字段类型',dataIndex:'TYPE',width:100,sortable : true,mappingType:'FIELD_TYPE',
				    renderer:function(value){
						var val = translateLookupByKey("FIELD_TYPE",value);
						return val?val:"";
					}
			},
		    {header:'字段长度',dataIndex:'LENGTH',width:100,sortable : true},
		    {header:'字段默认值',dataIndex:'DEFAULT_VAL',width:150,sortable : true,hidden:false},	
		    {header:'是否为空',dataIndex:'IS_NULL',width:150,sortable : true,mappingType:'IF_NULL',
		    	renderer:function(value){
					var val = translateLookupByKey("IF_NULL",value);
					return val?val:"";
				}
		    },
		    {header:'是否主键',dataIndex:'IS_PRI_KEY',width:100,sortable : true,mappingType:'IF_PRIMARY',
		    	renderer:function(value){
					var val = translateLookupByKey("IF_PRIMARY",value);
					return val?val:"";
				}
		    },
		    {header:'字段说明',dataIndex:'REMARK',hidden:false,width:150,sortable : true}],
	record : new Ext.data.Record.create([
		    {name:'ID'},
		    {name:'MODEL_NAME_CH'},
		    {name:'MODEL_NAME_EN'},
		    {name:'FIELD_NAME_CH'},
		    {name:'FIELD_NAME_EN'},
		    {name:'TYPE'},
		    {name:'LENGTH'},
		    {name:'DEFAULT_VAL'},
		    {name:'IS_NULL'},
		    {name:'IS_PRI_KEY'},
		    {name:'REMARK'}
		]),
	initComponent : function(){
		Com.yucheng.bcrm.common.GlassTrigger.superclass.initComponent.call(this);
	},
	listeners:{
		focus:function(){
			if(!this.disabled){ //禁用的放大镜不允许弹出选择
				this.onTrigger2Click();
			}
		}
	},
	onTrigger2Click : function(){
		//禁用的放大镜不允许弹出选择
		if(this.disabled||this.readOnly){
			return ;
		}
		this.initWin();
	},
	callback:null,
	search:function(){
		
	},
	initWin: function(){
		var me=this;
		var loader = new Com.yucheng.bcrm.ArrayTreeLoader({
				url : basepath + '/reqmentModelInfo.json?projId='+_projId,
				parentAttr : 'MODEL_PARENT',
				locateAttr : 'MODEL_NAME_EN',
				jsonRoot : 'json.data',
				rootValue : '0',
				textField : 'MODEL_NAME_CH',
				idProperties : 'MODEL_NAME_EN'});
		this.tree=new Com.yucheng.bcrm.TreePanel({
			height : document.body.clientHeight,
			width : 200,
			autoScroll:true,
			checkBox : false, 
			resloader:loader,
			region:'west',
			split:true,
			root: new Ext.tree.AsyncTreeNode({
				id:'0',
				expanded:true,
				text:'模型树',
				autoScroll:true,
				children:[]
			}),
			clickFn:function(node){//单击事件
				if(node.attributes.id!= ''){
					me.nodeNow=node;
					me.search();
				}
			}
		}); 
		
		
		Ext.Ajax.request({//目录树数据加载
			url :loader.url,
			method:'GET',
			success:function(response){
				var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
				loader.nodeArray = nodeArra;
				var children = loader.loadAll();
				me.tree.appendChild(children);
				me.tree.expandAll();
			}
		});
		
		this.panel=new Ext.form.FormPanel({
			height:90,
			labelWidth:100,//label的宽度
			labelAlign:'right',
			frame:true,
			autoScroll : true,
			region:'north',
			split:true,
			buttonAlign:'center',
			items:[me.searchPanel],
			buttons:[{
				text:'查询',
				handler:function(){
					me.search();
				}
			},{
				text:'重置',
				handler:function(){
					me.panel.getForm().reset();
					//默认取消选中
					me.tree.getSelectionModel().clearSelections();
					me.nodeNow=null;
					me.search();
				}
			}]
		});
		
		//复选框
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect:me.singleSelect
		});
		// 定义自动当前页行号
		var rownum = new Ext.grid.RowNumberer({
			header : 'No.',
			width : 32
		});
		var cmarr=[rownum,sm];
		for(var i=0;i<me.columnConfig.length;i++){
			cmarr.push(me.columnConfig[i]);
		}
		
		var cm = new Ext.grid.ColumnModel(cmarr);
		var reader = new Ext.data.JsonReader({
			totalProperty:'json.count',
			root:me.gridRoot
		}, me.record);
		
		var proxy = new Ext.data.HttpProxy({
			url:me.url
		});
		
		me.gridStore = new Ext.data.Store({
			restful : true,
			proxy : proxy,
			reader :reader,
			baseParams:me.baseParams,
			recordType: me.record
		});
		me.gridStore.on('beforeload', function() {
						this.baseParams=me.baseParams;
						if(me.nodeNow!=null){
							this.baseParams.modelId=me.nodeNow.attributes.ID;
						}else{
							this.baseParams.modelId=null;
						}
					});
		me.search=function(){
			var parameters = me.panel.getForm().getFieldValues(false);
			me.gridStore.removeAll();
			me.gridStore.load({
				params:{
					'condition':Ext.encode(parameters),
					start:0,
					limit: parseInt(pagesize_combo.getValue())
				}
			});
		};
		
		// 每页显示条数下拉选择框
		var pagesize_combo = new Ext.form.ComboBox({
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
		var number = parseInt(pagesize_combo.getValue());
		/**
		 * 监听分页下拉框选择事件
		 */
		pagesize_combo.on("select", function(comboBox) {
			bbar.pageSize = parseInt(pagesize_combo.getValue());
			me.search();
		});
		//分页工具条定义
		var bbar = new Ext.PagingToolbar({
			pageSize : number,
			store : me.gridStore,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : ['-', '&nbsp;&nbsp;', pagesize_combo]
		});
		
		this.grid=new Ext.grid.GridPanel({
			frame:true,
			autoScroll : true,
			bbar:bbar,
			stripeRows : true,
			store:me.gridStore,
			loadMask:true,
			cm :cm,
			sm :sm,
			region:'center',
			viewConfig:{
				forceFit:false,
				autoScroll:true
			},
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			},
			buttonAlign:'center',
			buttons:[{
				text : '选定',
				handler : function() {
					var checkedNodes = me.grid.getSelectionModel().selections.items;
					if(checkedNodes.length > 0){
						me.setValue(checkedNodes[0].get("MODEL_NAME_CH"));
						if(typeof me.callback == 'function') {
							me.callback(checkedNodes);
						}
						me.window.hide();
					}else{
						Ext.Msg.alert("提示","未选择任何数据！");
					}
				}
			},{
				text : '取消',
				handler : function() {
					me.window.hide();
				}
			}]
		});
		
		this.window=new Ext.Window({
				title : me.winTitle,
				closable : true,
				resizable : true,
				collapsible : false,
				height:me.winHeight,
				width:me.winWidth,
				closeAction : 'hide',
				modal : true, // 模态窗口 
				autoScroll : true,
				closable : true,
				layout:'border',
				items:[this.tree,{
					region:'center',
					layout:'border',
					items:[this.panel,this.grid]
				}]
		});
		
		this.window.on('hide',function(){
			me.panel.getForm().reset();
			me.grid.getStore().removeAll();
		});
		this.window.on('show',function(){
			me.search();
		});
		
		this.window.show();
		this.window.setZIndex(90000);
	}
});
Ext.reg('glasstrigger',Com.yucheng.bcrm.common.GlassTrigger);