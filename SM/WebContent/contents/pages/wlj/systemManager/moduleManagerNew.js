/**
 * @description 模块管理
 * @author helin
 * @since 2014-06-27
 */
imports([
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js'
]);

//初始化提示框
Ext.QuickTips.init();

//本地数据字典定义
var localLookup = {
	'IS_OUNTER' : [  //是否外部系统
		{key : '1',value : '是'},
	    {key : '0',value : '否'}
	]
	,'IS_RIDE' : [  //什么用户
		{key : '1',value : 'RIDE用户登录'},
	    {key : '2',value : '配置用户登录'}
	]
};

//查看的模块ID
var moduleId = '-1';
//功能点id
var nodeId = ''; 
//是否新增功能点 0表示否，1表示是
var ifAdd=0;
var treeLoaders = [{
	key : 'FWFUNCTIONTREELOADER',
	url : basepath	+ '/fwFunctionTree-action.json?mdulId='	+ moduleId,
	parentAttr : 'PARENT_ID',
	locateAttr : 'ID',
	rootValue : '0',
	textField : 'AMOUNT_NAME',
	idProperties : 'ID',
	jsonRoot:'json.data'
}];
var treeCfgs = [{
	key : 'FWFUNCTIONTREE',
	loaderKey : 'FWFUNCTIONTREELOADER',
	autoScroll:true,
	rootCfg : {
		expanded:true,
		id:'root',
		text:'模块功能点列表',
		autoScroll:true,
		children:[]
	},
	tbar : [{ 
	    text : '新增',
	    handler : function() {// 调用新增方法
	        functionForm.getForm().reset();
	        functionForm.getForm().findField('moduleId').setValue(moduleId);
	        Ext.getCmp('funcManagerButt').setDisabled(false);
	        ifAdd=1;
	    }
	},{
	    text : '删除',
	    handler : function() { // 调用删除方法
	    	Ext.MessageBox.confirm('提示','你确定删除吗!',function(buttonId){
                if(buttonId == 'no'){
                    return false;
                }
    	        if(nodeId == "root") {
    	            Ext.Msg.alert('提示', '只能对子功能点进行删除操作!');
    	            return false;
    	        }
    	        Ext.Ajax.request({
	            	url : basepath + '/Function-action!destroy.json?idStr=' + nodeId,
	               	waitMsg : '正在删除数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
                    success : function() {
                    	Ext.Msg.alert('提示', '操作成功');
                        Ext.Ajax.request({// 左侧模块功能树的Ajax请求事件
							url : basepath	+ '/fwFunctionTree-action.json?mdulId='	+ moduleId,
							method : 'GET',
							success : function(response) {
								var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
								leftFunctionTree.resloader.nodeArray = nodeArra;//重新获取后台数据
								leftFunctionTree.resloader.refreshCache(); // 刷新缓存
								var children = leftFunctionTree.resloader.loadAll(); //得到相应的树数据
								leftFunctionTree.root.removeAll(true); // 清掉以前的数据
								leftFunctionTree.appendChild(children);// 把数据重新填充
							}
						});
						controllerStore.reload();
                        functionForm.getForm().reset();
                        Ext.getCmp('funcManagerButt').setDisabled(true);
                    },
                    failure : function() {
                        Ext.Msg.alert('提示', '操作失败!');
                    }
	            });
    	    });
	    }
	}],
	clickFn : function(node){
		nodeId = node.attributes.id;
		if(nodeId == 'root'){
			return false;
		}
		controllerStore.load({ //刷新控制点列表
            params:{
                'fwFunId' : nodeId
            }
        });
        
        Ext.getCmp('funcManagerButt').setDisabled(false);
        functionForm.getForm().reset();
        functionForm.getForm().findField('funcName').setValue(node.attributes.FUNC_NAME);
        functionForm.getForm().findField('funcDesc').setValue(node.attributes.FUNC_DESC);
        functionForm.getForm().findField('action').setValue(node.attributes.ACTION);
        functionForm.getForm().findField('moduleId').setValue(node.attributes.MODULE_ID);
        functionForm.getForm().findField('id').setValue(nodeId);
        
        Ext.Ajax.request({// 左侧模块功能树的Ajax请求事件
			url : basepath	+ '/fwFunctionExtTree-action.json',
			params: {
				id:nodeId
			},
			method : 'GET',
			success : function(response) {
				var nodeArray = Ext.util.JSON.decode(response.responseText).json.data;
				if(nodeArray.length != 0){
					Ext.getCmp('tubiao').setValue(nodeArray[0].TILE_LOGO);
					Ext.getCmp('tileColor').setValue(nodeArray[0].TILE_COLOR);
					var supportSizeUrl=nodeArray[0].SUPPORT_SIZE_URL;
					var defaultSize=nodeArray[0].DEFAULT_SIZE;
					var b=defaultSize.split("_0");
					Ext.getCmp('defaultSize').setValue(b[1]);
					var a=supportSizeUrl.split(",");
					if(a.length!=0 && a!=""){
						for(var i=0;i<a.length;i++){
							var b=a[i].split("_0");
							var c=b[1].split("|");
							Ext.getCmp('size'+c[0]).setValue(c[1]);
						}
						Ext.getCmp('TacticsFlag0').expand();
					}else{
						Ext.getCmp('TacticsFlag0').collapse();
					}
				}
			}
		});
	}
}];

//查询url与提交url
var url = basepath+'/moduleManagerQuery.json';
var comitUrl = basepath+'/FwModule-action.json';

var fields = [
    {name: 'ID',hidden : true},
    {name: 'MDUL_NAME', text : '模块名称', searchField: true,resutlWidth: 200,allowBlank: false}, 
    {name: 'MDUL_DESC',text:'模块描述', resutlWidth:200,xtype:'textarea'},
    {name: 'AMOUNT',text:'功能点数量', resutlWidth:80},
    {name: 'CRT_DATE',text:'创建日期', resutlWidth:100,dataType:'date',format:'Y-m-d',hidden:true},
    {name: 'IS_OUTER',text:'是否外部系统',translateType : 'IS_OUNTER', resutlWidth:100,hidden:true,allowBlank: false}, 
    {name: 'USER_NAME',text:'USER_NAME',hidden:true},
    {name: 'USER_KEY',text:'USER_KEY',hidden:true},
    {name: 'URL',text:'URL',hidden:true},
    {name: 'PWD_KEY',text:'PWD_KEY',hidden:true},
    {name: 'PASSWORD',text:'PASSWORD',hidden:true},
    {name: 'IS_RIDE',text:'是否RIDE',translateType : 'IS_RIDE',hidden:true}
];

var createView = true;
var editView = true;
var detailView = true;

var formViewers = [{
	columnCount : 1,
	fields : ['ID','MDUL_NAME','MDUL_DESC','IS_OUTER','CRT_DATE','IS_RIDE','URL','USER_KEY','PWD_KEY','USER_NAME','PASSWORD'],
	fn : function(ID,MDUL_NAME,MDUL_DESC,IS_OUTER,CRT_DATE,IS_RIDE,URL,USER_KEY,PWD_KEY,USER_NAME,PASSWORD){
		IS_OUTER.hidden = false;
		return [ID,MDUL_NAME,MDUL_DESC,IS_OUTER,CRT_DATE,IS_RIDE,URL,USER_KEY,PWD_KEY,USER_NAME,PASSWORD];
	}
}];

/**
 * 功能模块树
 */
var leftFunctionTree = TreeManager.createTree('FWFUNCTIONTREE');

/**
 * 右侧磁贴图标
 */
var iconStore = new Ext.data.ArrayStore({//右侧图片选择时展示所用的store
	proxy : new Ext.data.MemoryProxy(),
	fields : ['id'],
	sortInfo: {
		field : 'id',
		direction: 'ASC'
	}
});

var iconData = [	//id--图标的路径
//	['/contents/wljFrontFrame/styles/search/searchpics/fun5.png'],
//	['/contents/wljFrontFrame/styles/search/searchpics/fun4.png'],
//	['/contents/wljFrontFrame/styles/search/searchpics/fun3.png'],
//	['/contents/wljFrontFrame/styles/search/searchpics/fun2.png'],
//	['/contents/wljFrontFrame/styles/search/searchpics/fun1.png']
];
for(var i=1;i<=100;i++){
	iconData.push(['ico-t-'+i]);
}
iconStore.loadData(iconData);//右侧图标展示时加载静态数据
/**
 * 设置选择的图标显示
 * @param {} id
 */
function setIcon(id){
	Ext.getCmp('tubiao').setValue(id);
} 
var dataView = new Ext.DataView({//右侧图标展示的样式(包括图标的排版)
	store: iconStore,
	id: 'iconss',
	tpl  : new Ext.XTemplate(
		'<ul>',
		'<tpl for=".">',
		'<li class="icon {[values.id]}" onclick="setIcon(\'{[values.id]}\')" style="width:60px;height:60px;float:left;margin-left:10px;background-color:#1b96d1;margin-top:10px;">',
//		'<img src= "'+basepath+'/{[values.id]}" />',
		'</li>',
		'</tpl>',
		'</ul>'
	),
	itemSelector: 'li.icon',
	overClass   : 'phone-hover',
	singleSelect: true,
	autoScroll  : true
});
var iconView = new Ext.Panel({//右侧图标展示的Panel
	title: '磁贴图标',
	id :'iconView',
	ddGroup  : 'gridDDGroups1',
	enableDD  : true,
	layout: 'fit',
	items : dataView
});

/**
 * 右侧磁贴颜色
 */
var tileColorStore = new Ext.data.ArrayStore({
	proxy : new Ext.data.MemoryProxy(),
	fields : ['id'],
	sortInfo: {
		field : 'id',
		direction: 'ASC'
	}
});
var tileColorData = [];
for(var i=1;i<=10;i++){
	tileColorData.push(['tile_c'+i]);
}
tileColorStore.loadData(tileColorData);
/**
 * 设置选择的颜色显示
 * @param {} id
 */
function setTileColor(id){
	Ext.getCmp('tileColor').setValue(id);
} 
var tileColorDataView = new Ext.DataView({//右侧图标展示的样式(包括图标的排版)
	store: tileColorStore,
	id: 'tileColorss',
	tpl  : new Ext.XTemplate(
		'<ul>',
		'<tpl for=".">',
		'<li class="icon {[values.id]}" onclick="setTileColor(\'{[values.id]}\')" style="width:60px;height:60px;float:left;margin-left:10px;margin-top:10px;">',
		'</li>',
		'</tpl>',
		'</ul>'
	),
	itemSelector: 'li.icon',
	overClass   : 'phone-hover',
	singleSelect: true,
	autoScroll  : true
});
var tileColorView = new Ext.Panel({//右侧图标展示的Panel
	title: '磁贴颜色',
	id :'tileColorView',
	ddGroup  : 'gridDDGroups2',
	enableDD  : true,
	layout: 'fit',
	items : tileColorDataView
});

/**
 * 控制点表格
 */
var controllerSm = new Ext.grid.CheckboxSelectionModel();
var controllerRowNumber = new Ext.grid.RowNumberer({
    header:'NO.',
    width:28
});
var controllerCm = new Ext.grid.ColumnModel([
    controllerSm,controllerRowNumber,
    {dataIndex:'id',header:'ID',width : 120,sortable : true, hidden:true},
    {dataIndex:'name',header:'控制名称',width : 120,sortable : true},
    {dataIndex:'conCode',header:'控制代码',width : 120,sortable : true},
    {dataIndex:'remark',header:'备注',width : 120,sortable : true}
]);
// create the data record
var controllerRecord = new Ext.data.Record.create([
    {name:'id',mapping:'ID'},
    {name:'name',mapping:'NAME'},
    {name:'conCode',mapping:'CON_CODE'},
    {name:'remark',mapping:'REMARK'},
    {name:'fwFunId',mapping:'FW_FUN_ID'}
]);
// create the data store
var controllerStore = new Ext.data.Store({
    restful:true,
    proxy: new Ext.data.HttpProxy({
        url: basepath + '/controllerManagerQuery.json'
    }),
    reader: new Ext.data.JsonReader({
        root:'json.data',
        totalProperty:'json.count'
    },controllerRecord)
});
// create the maintain formpanel
var maintainForm = new Ext.FormPanel({
    frame: true,
    width: 400,
    height: 240,
    autoScroll : true,
    split : true,
    items: [
        {xtype:'textfield',name:'id',fieldLabel:'ID',labelStyle:'text-align:right;',anchor:'95%',hidden:true},
        {xtype:'textfield',name:'fwFunId',fieldLabel:'功能ID',labelStyle:'text-align:right;',anchor:'95%',hidden:true},
        {xtype:'textfield',name:'name',fieldLabel:'控制点名称<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false},
        {xtype:'textfield',name:'conCode',fieldLabel:'控制点代码<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false},
        {xtype:'textarea',name:'remark',fieldLabel:'备注',labelStyle:'text-align:right;',anchor:'95%'}
    ]
});

// create the maintain window
var maintainWindow = new Ext.Window({
    title:'新增OR修改',
    width: 400,
    height: 240,
    layout: 'fit',
    closable: true,
    closeAction: 'hide',
    buttonAlign: 'center',
    model: true,
    items : [maintainForm],
    buttons:[{
        text:'保存',
        handler:function(){    
            if (!maintainForm.getForm().isValid()) {
                Ext.Msg.alert("提示", "输入有误或存在漏输项，请重新输入！");
                return false;
            }
            maintainForm.getForm().findField('fwFunId').setValue(nodeId);
            Ext.Ajax.request({
                url: basepath + '/Controllers-action.json',
                method: 'POST',
                form: maintainForm.getForm().id,
                waitMsg: '正在保存数据，请稍等...',
                success: function(response){
                    Ext.Msg.alert('提示','操作成功！')
                    controllerStore.load({
                        params:{
                            'fwFunId' : nodeId
                        }
                    });
                    maintainWindow.hide();
                },
                failure:function(){
                    Ext.Msg.alert('提示','操作失败！');
                }
            });
        }
    },{
        text: '关闭',
        handler: function(){
            maintainWindow.hide();
        }
    }]
});
//create the toolbar
var controllerTbar = new Ext.Toolbar({
    items: [{
        text: '新增',
        handler: function(){
        	if(nodeId == '' || nodeId == 'root'){
        		Ext.Msg.alert('提示','请先选择一个功能点!');
        		return false;
        	}
            if(maintainForm.getForm().getEl()){
                maintainForm.getForm().getEl().dom.reset();
            }
            maintainWindow.setTitle('新增');
            maintainWindow.show();
        }
    },{
        text: '修改',
        handler: function(){
            var selectLength = controllerPanel.getSelectionModel().getSelections().length;
            var selectRecord = controllerPanel.getSelectionModel().getSelections()[0];
            if(selectLength != 1){
                Ext.Msg.alert('提示','请选择一条记录!');
                return false;
            }
            maintainWindow.setTitle('修改');
            maintainForm.getForm().loadRecord(selectRecord);
            maintainWindow.show();
        }
    },{
        text: '删除',
        handler: function(){
            var selectLength = controllerPanel.getSelectionModel().getSelections().length;
            if(selectLength < 1){
                Ext.Msg.alert('提示','请选择你要删除的记录!');
                return false;
            }
            var selectRecords = controllerPanel.getSelectionModel().getSelections();
            var ids = '';
            for(var i=0; i < selectLength; i++){
                var selectRecord = selectRecords[i];
                ids +=  selectRecord.data.id;
                if( i != selectLength - 1){
                    ids += ',';
                }
            }
            Ext.MessageBox.confirm('提示','你确定删除吗!',function(buttonId){
                if(buttonId == 'no'){
                    return false;
                }
                Ext.Ajax.request({
                    url: basepath + '/Controllers-action!destroy.json',
                    waitMsg: '正在删除，请稍等...',
                    params:{
                        idStr : ids
                    },
                    success: function(){
                        Ext.Msg.alert('提示','操作成功!');
                        controllerStore.load({
	                        params:{
	                            'fwFunId' : nodeId
	                        }
	                    });
                    },
                    failure: function(){
                        Ext.Msg.alert('提示','操作失败!');
                    }
                });
            });
        }
    }]
});
// create the gridPanel
var controllerPanel = new Ext.grid.GridPanel({
    frame: true,
    region: 'center',
    autoScroll: true,
    stripeRows: true,
    store: controllerStore,
    cm : controllerCm,
    sm : controllerSm,
    tbar: controllerTbar,
    viewConfig:{},
    loadMask: {
        msg: '正在加载表格数据,请稍等...'
    }
});

/**
 * 功能点详情维护面板
 */
var functionForm = new Ext.form.FormPanel({
	title : '功能点详情',
	frame : true,
	autoScroll : true,
	split : true,
	items : [{
	    layout : 'form',
	    items : [
	    	{id : 'fid',name : 'id',xtype : 'numberfield',hidden : true,anchor : '90%'},
	    	{id : 'moduleId',name : 'moduleId',xtype : 'textfield',hidden : true,anchor : '90%'},
	    	{id : 'funcName',name : 'funcName',xtype : 'textfield',fieldLabel : '功能点名称<font color=red>*</font>',allowBlank : false,anchor : '90%'},
	    	{id : 'funcDesc',name : 'funcDesc',xtype : 'textfield',fieldLabel : '功能点描述',anchor : '90%'},
	    	{id : 'action',name : 'action',xtype : 'textfield',emptyText : '例：/contents/pages/admin/...或http://192.168.1.1/...',
	        	allowBlank : false,fieldLabel : '功能点链接<font color=red>*</font>',anchor : '90%'},
			{id : 'tubiao',name : 'tubiao',xtype : 'textfield',fieldLabel : '磁贴图标',width : 250,emptyText:'请选择图标',readOnly:true,cls:'x-readOnly',anchor : '90%'
				,listeners:{//监控事件，当鼠标点击到该field的时候自动打开右侧相应的页签
					focus:function(a){
						Ext.getCmp('rightLayout').layout.setActiveItem(iconView);
					}
				}
			},
			{id : 'tileColor',name : 'tileColor',xtype : 'textfield',fieldLabel : '磁贴颜色',width : 250,emptyText:'请选择颜色',readOnly:true,cls:'x-readOnly',anchor : '90%'
				,listeners:{//监控事件，当鼠标点击到该field的时候自动打开右侧相应的页签
					focus:function(a){
						Ext.getCmp('rightLayout').layout.setActiveItem(tileColorView);
					}
				}
			},
	        {
		    	title: '是否使用动态磁贴',
		    	id:'TacticsFlag0',
		    	xtype: 'fieldset',
		    	checkboxToggle: true,
		    	animCollapse :true,
		    	height: 285,
		    	items:[new Ext.form.ComboBox({
						fieldLabel : '默认尺寸',
						store : new Ext.data.SimpleStore({
							fields: ['value','key'],
							data : [['尺寸3x3','1'],['尺寸2x3','2'],['尺寸3x2','3'],['尺寸1x3','4'],['尺寸3x1','5'],['尺寸2x2','6'],['尺寸1x2','7'],['尺寸2x1','8']]
						}),
						id:'defaultSize',mode: 'local',labelStyle: 'text-align:right;',triggerAction : 'all',name:'REPORT_STATUS',hiddenName:'REPORT_STATUS',displayField : 'value',valueField : 'key',
						width : 270,forceSelection : true,emptyText:'请选择',resizable : true,anchor : '90%'
					}),
		        	{fieldLabel : '尺寸3x3',xtype : 'textfield',width : 270,id:'size1',anchor : '90%'},
		        	{fieldLabel : '尺寸2x3',xtype : 'textfield',	width : 270,id:'size2',anchor : '90%'},
		        	{fieldLabel : '尺寸3x2',xtype : 'textfield',	width : 270,id:'size3',anchor : '90%'},
		        	{fieldLabel : '尺寸1x3',xtype : 'textfield',width : 270,id:'size4',anchor : '90%'},
		        	{fieldLabel : '尺寸3x1',xtype : 'textfield',	 width : 270,id:'size5',anchor : '90%'},
		        	{fieldLabel : '尺寸2x2',xtype : 'textfield',	width : 270,id:'size6',anchor : '90%'},
		        	{fieldLabel : '尺寸1x2',xtype : 'textfield',	id:'size7',width : 270,anchor : '90%'},
		        	{fieldLabel : '尺寸2x1',xtype : 'textfield',	width : 270,id:'size8',anchor : '90%'}
		    	]
			}
		]
	}],
    buttonAlign : 'center',
    buttons : [{
    	id : 'funcManagerButt',
        text : ' 保  存 ',
        disabled : true,
        handler : function() {
            if (!functionForm.getForm().isValid()) {
                Ext.MessageBox.alert('提示', '请正确输入各项必要信息！');
                return false;
            }
            var ifHas=0;
        	var data = functionForm.getForm().getFieldValues();
        	data.ifHas = ifHas;
        	data.ifAdd = ifAdd;
        	
        	if(Ext.getCmp('tubiao').getValue()!=''){
        		data.tubiao=Ext.getCmp('tubiao').getValue();
        	}else{
        		data.tubiao='';
        	}
        	
        	if(Ext.getCmp('tileColor').getValue()!=''){
        		data.tileColor=Ext.getCmp('tileColor').getValue();
        	}else{
        		data.tileColor='';
        	}
        	
            if(!Ext.getCmp('TacticsFlag0').collapsed){
            	var ids='';
            	var limit;
        		if(Ext.getCmp('defaultSize').getValue()!=''){
        			var index=Ext.getCmp('defaultSize').getValue();
        			if(Ext.getCmp('size'+index).getValue()==''){
        				Ext.MessageBox.alert('提示', '请输入默认尺寸对应的页面！');
	                	return false;
        			}
        		}
            	
            	for(var i=1;i<9;i++){
            		if(Ext.getCmp('size'+i).getValue()!='')
            			limit=i;
            	}
            	var defaultUrl=Ext.getCmp('tubiao').getValue();
            	
            	for(i=1;i<9;i++){
        			if((i+'')==( Ext.getCmp('defaultSize').getValue())){
        				defaultUrl=Ext.getCmp('size'+i).getValue();
        			}
            	}
            	
            	for(var i=1;i<limit+1;i++){
            		var sizeNumber=Ext.getCmp('size'+i).getValue();
            		if(Ext.getCmp('size'+i).getValue()!=''){ 
            			ids+= 'TS_0'+i+'|'+Ext.getCmp('size'+i).getValue();
						if( i != limit){
							ids += ',';
						}
            		}
            	}
            	var tubiao = Ext.getCmp('tubiao').getValue();

            	ifHas=1;
            	data.ifHas = ifHas;
        		data.ids = ids;
        		data.tubiao=tubiao;
        		data.idd=nodeId;
        		if(Ext.getCmp('defaultSize').getValue()!=''){
        			data.defaultSize=Ext.getCmp('defaultSize').getValue();
        		}
        		data.defaultUrl=defaultUrl;
            }
                
            Ext.Ajax.request({
                url : basepath + '/Function-action.json',
                method : 'POST',
                params :data,
                waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
                success : function() {
                    Ext.Msg.alert('提示', '操作成功');
                    Ext.Ajax.request({// 左侧模块功能树的Ajax请求事件
                        url : basepath + '/fwFunctionTree-action.json?mdulId=' + moduleId,
                        method : 'GET',
                        success : function(response) {
                    	    ifAdd=0;
                            var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
							leftFunctionTree.resloader.nodeArray = nodeArra;//重新获取后台数据
							leftFunctionTree.resloader.refreshCache(); // 刷新缓存
							var children = leftFunctionTree.resloader.loadAll(); //得到相应的树数据
							leftFunctionTree.root.removeAll(true); // 清掉以前的数据
							leftFunctionTree.appendChild(children);// 把数据重新填充
                        }
                    });
                    functionForm.getForm().reset();
                    controllerStore.load({
                        params:{
                            'fwFunId' : nodeId
                        }
                    });
                },
                failure : function(response) {
                    var resultArray = Ext.util.JSON.decode(response.status);
                    if (resultArray == 403) {
                        Ext.Msg.alert('提示', response.responseText);
                    } else {
                        Ext.Msg.alert('提示', '操作失败,失败原因:' + response.responseText);
					}
                }
            });
            Ext.getCmp('funcManagerButt').setDisabled(true);
        }
    }]
});

//结果域扩展功能面板
var customerView = [{
	/**
	 * 自定义功能点维护面板
	 */
	title:'功能点维护',
	suspendWidth:860,
	items:[{
		layout: 'border',
		items:[{
			region: 'west',
			layout:'fit',
			width: 240,
			title: '模块功能',
			items:[leftFunctionTree]
		},{
			region:'center',
			layout: 'border',
			items:[{
				region : 'north',
				height : 280,
				layout: 'fit',
				items:[functionForm]
			},{
				region: 'center',
				layout: 'fit',
				items:[controllerPanel]
			}]
		},{ 
			region:'east',
			layout:'accordion',
			id:'rightLayout',
			width: 180,
			items:[iconView,tileColorView]
		}]
	}]
}];

/**
 * 自定义工具条上按钮
 * 注：批量选择未实现,目前只支持单条删除、启用、停用
 */
var tbar = [{
	/**
	 * 模块删除
	 */
	text : '删除',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
			var id=getSelectedData().data.ID;
			Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
				if(buttonId.toLowerCase() == "no"){
					return;
				}  
				Ext.Ajax.request({
					url: basepath + '/FwModule-action!destroy.json',
					waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
	                params: {
                        'idStr': id
                    },
					success : function() {
                        Ext.Msg.alert('提示', '删除成功' );
						reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '删除失败' );
						reloadCurrentData();
					}
				});
			});
		}
	}
}];


/**
 * 结果域面板滑入前触发,系统提供listener事件方法
 * @param {} view
 * @return {Boolean}
 */
var beforeviewshow = function(view){
	if(view.baseType != 'createView'){
		if(!getSelectedData()||getAllSelects().length>1){ //注：beforeviewshow事件不包含进入列表，因此可以此调用
			Ext.Msg.alert('提示','请选择一条数据进行操作！');
			return false;
		}
		if(view._defaultTitle == '功能点维护'){
			moduleId = getSelectedData().data.ID;
			Ext.Ajax.request({// 左侧模块功能树的Ajax请求事件
				url : basepath	+ '/fwFunctionTree-action.json?mdulId='	+ moduleId,
				method : 'GET',
				success : function(response) {
					var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
					leftFunctionTree.resloader.nodeArray = nodeArra;//重新获取后台数据
					leftFunctionTree.resloader.refreshCache(); // 刷新缓存
					var children = leftFunctionTree.resloader.loadAll(); //得到相应的树数据
					leftFunctionTree.root.removeAll(true); // 清掉以前的数据
					leftFunctionTree.appendChild(children);// 把数据重新填充
				}
			});
		}
	}
};