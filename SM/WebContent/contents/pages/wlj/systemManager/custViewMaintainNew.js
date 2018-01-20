/**
 * @description 客户视图项设置
 * @author helin chenmeng
 * @since 2014-12-19
 */
imports([
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js'
]);

Ext.QuickTips.init();
//功能点id
var managerId = '';
//本地数据字典定义
var localLookup = {
	'VIEW_TYPE' : [//视图类型
		{key : '1',value : '项目视图'},
	    {key : '2',value : '需求视图'}
	]
};

//下拉视图树
var treeLoaders = [{
	key : 'CUSTVIEWLOADER',
	url : basepath + '/parentidQuery.json',  //viewtype 不传表未后台接收的为null,这样可将所有的视图树查询出来,
	parentAttr : 'PARENTID',
	locateAttr : 'ID',
	jsonRoot:'json.data',
	rootValue : '0',
	textField : 'NAME',
	idProperties : 'ID'
}];
var treeCfgs = [{
	key : 'CUSTVIEWTREE',
	loaderKey : 'CUSTVIEWLOADER',
	autoScroll:true,
	rootVisible : false,//根结点不可见
	rootCfg : {
		expanded:true,
		id:'root',
		autoScroll:true,
		children:[]
	},
	clickFn : function(node){
	}
}];

var url = basepath+'/CustViewMaintainInfo-action.json';
var comitUrl = basepath+'/CustViewMaintainInfo-action.json';

var fields = [
    {name: 'ID',hidden : true},
    {name: 'NAME',text: '名称', searchField: true,allowBlank: false},
    {name: 'ADDR',text:'链接地址', resutlWidth:240},
    {name: 'PARENT_NAME',text:'父节点'},
    {name: 'PARENTID',text:'父节点',hidden:true,xtype: 'wcombotree',innerTree:'CUSTVIEWTREE',allowBlank:false,showField:'text',hideField:'ID',editable:false},
    {name: 'ORDERS',text:'顺序号', resutlWidth:100},
    {name: 'VIEWTYPE',text:'视图类型',translateType:'VIEW_TYPE',searchField: true,resutlWidth:100,allowBlank: false}
];

/**
 * 新增-控制点表格
 */
var controllerSm = new Ext.grid.CheckboxSelectionModel();
var controllerRowNumber = new Ext.grid.RowNumberer({
    header:'NO.',
    width:28
});
var controllerCm = new Ext.grid.ColumnModel([
    controllerSm,controllerRowNumber,
    {dataIndex:'id',header:'ID',width : 120,sortable : true, hidden:true},
    {dataIndex:'managerId',header:'视图项ID',width : 120,sortable : true, hidden:true},
    {dataIndex:'controlCode',header:'控制点Code',width : 120,sortable : true},
    {dataIndex:'controlName',header:'控制点名称',width : 120,sortable : true}
]);
// create the data record
var controllerRecord = new Ext.data.Record.create([
    {name:'id',mapping:'ID'},
    {name:'managerId',mapping:'MANAGER_ID'},
    {name:'controlCode',mapping:'CONTROL_CODE'},
    {name:'controlName',mapping:'CONTROL_NAME'}
]);
// create the data store
var controllerStore = new Ext.data.Store({
    restful:true,
    proxy: new Ext.data.HttpProxy({
        url: basepath + '/CustViewMaintainControl-action.json'
    }),
    reader: new Ext.data.JsonReader({
        root:'json.data',
        totalProperty:'json.count'
    },controllerRecord)
});
//create the toolbar
var controllerTbar = new Ext.Toolbar({
    items: [{
        text: '新增',
        handler: function(){ 
	    	if(maintainForm.getForm().getEl()){
	            maintainForm.getForm().getEl().dom.reset();
	        }
	    	var id = getCurrentView().contentPanel.getForm().findField('ID').getValue();
	    	if (id == '' || id == undefined){
	    		Ext.Msg.alert('提示','请先保存功能点信息');
	    		return false;
	    	}
	        maintainForm.getForm().findField('managerId').setValue(id);
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
            maintainForm1.getForm().loadRecord(selectRecord);
            maintainWindow1.show();
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
                    url: basepath + '/CustViewMaintainControl-action!destroy.json',
                    waitMsg: '正在删除，请稍等...',
                    params:{
                        idStr : ids
                    },
                    success: function(){
                    	controllerStore.reload();
                        Ext.Msg.alert('提示','操作成功!');
                    },
                    failure: function(){
                        Ext.Msg.alert('提示','操作失败!');
                    }
                });
            });
        }
    }]
});
/**
 * 修改-控制点表格
 */
var controllerSm1 = new Ext.grid.CheckboxSelectionModel();
var controllerRowNumber1 = new Ext.grid.RowNumberer({
    header:'NO.',
    width:28
});
var controllerCm1 = new Ext.grid.ColumnModel([
    controllerSm1,controllerRowNumber1,
    {dataIndex:'id',header:'ID',width : 120,sortable : true,hidden:true},
    {dataIndex:'managerId',header:'视图项ID',width : 120,sortable : true, hidden:true},
    {dataIndex:'controlCode',header:'控制点Code',width : 120,sortable : true},
    {dataIndex:'controlName',header:'控制点名称',width : 120,sortable : true}
]);
// create the data record
var controllerRecord1 = new Ext.data.Record.create([
    {name:'id',mapping:'ID'},
    {name:'managerId',mapping:'MANAGER_ID'},
    {name:'controlCode',mapping:'CONTROL_CODE'},
    {name:'controlName',mapping:'CONTROL_NAME'}
]);
// create the data store
var controllerStore1 = new Ext.data.Store({
    restful:true,
    proxy: new Ext.data.HttpProxy({
        url: basepath + '/CustViewMaintainControl-action.json'
    }),
    reader: new Ext.data.JsonReader({
        root:'json.data',
        totalProperty:'json.count'
    },controllerRecord1)
});
//create the toolbar
var controllerTbar1 = new Ext.Toolbar({
    items: [{
        text: '新增',
        handler: function(){      	
	        if(maintainForm.getForm().getEl()){
	            maintainForm.getForm().getEl().dom.reset();
	        }   
	        var ids=getSelectedData().data.ID;
	        maintainForm.getForm().findField('managerId').setValue(ids);
            maintainWindow.show();
        }
    },{
        text: '修改',
        handler: function(){
            var selectLength = controllerPanel1.getSelectionModel().getSelections().length;
            var selectRecord = controllerPanel1.getSelectionModel().getSelections()[0];
            if(selectLength != 1){
                Ext.Msg.alert('提示','请选择一条记录!');
                return false;
            }
            maintainForm1.getForm().loadRecord(selectRecord);
            maintainWindow1.show();
        }
    },{
        text: '删除',
        handler: function(){
            var selectLength = controllerPanel1.getSelectionModel().getSelections().length;
            if(selectLength < 1){
                Ext.Msg.alert('提示','请选择你要删除的记录!');
                return false;
            }
            var selectRecords = controllerPanel1.getSelectionModel().getSelections();
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
                    url: basepath + '/CustViewMaintainControl-action!destroy.json',
                    waitMsg: '正在删除，请稍等...',
                    params:{
                        idStr : ids
                    },
                    success: function(){
                    	controllerStore1.reload();
                        Ext.Msg.alert('提示','操作成功!');                       
                    },
                    failure: function(){
                        Ext.Msg.alert('提示','操作失败!');
                    }
                });
            });
        }
    }]
});

/**
 * 详情-控制点表格
 */
var controllerSm2 = new Ext.grid.CheckboxSelectionModel();
var controllerRowNumber2 = new Ext.grid.RowNumberer({
    header:'NO.',
    width:28
});
var controllerCm2 = new Ext.grid.ColumnModel([
    controllerSm2,controllerRowNumber2,
    {dataIndex:'id',header:'ID',width : 120,sortable : true, hidden:true},
    {dataIndex:'managerId',header:'视图项ID',width : 120,sortable : true, hidden:true},
    {dataIndex:'controlCode',header:'控制点Code',width : 120,sortable : true},
    {dataIndex:'controlName',header:'控制点名称',width : 120,sortable : true}
]);
// create the data record
var controllerRecord2 = new Ext.data.Record.create([
    {name:'id',mapping:'ID'},
    {name:'managerId',mapping:'MANAGER_ID'},
    {name:'controlCode',mapping:'CONTROL_CODE'},
    {name:'controlName',mapping:'CONTROL_NAME'}
]);
// create the data store
var controllerStore2 = new Ext.data.Store({
    restful:true,
    proxy: new Ext.data.HttpProxy({
        url: basepath + '/CustViewMaintainControl-action.json'
    }),
    reader: new Ext.data.JsonReader({
        root:'json.data',
        totalProperty:'json.count'
    },controllerRecord2)
});

/**
 * 控制点表格
 */
//create the maintain formpanel创建控制表格新增窗口
var maintainForm = new Ext.FormPanel({
    frame: true,
    width: 400,
    height: 240,
    autoScroll : true,
    split : true,
    items: [
        {xtype:'textfield',name:'id',fieldLabel:'ID',labelStyle:'text-align:right;',anchor:'95%',hidden:true},
        {xtype:'textfield',name:'managerId',fieldLabel:'视图项ID<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false,readOnly:true},
        {xtype:'textfield',name:'controlCode',fieldLabel:'控制点Code<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false},
        {xtype:'textfield',name:'controlName',fieldLabel:'控制点名称<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false}
    ]
});
//create the maintain formpanel创建控制表格新增窗口
var maintainForm1 = new Ext.FormPanel({
    frame: true,
    width: 400,
    height: 240,
    autoScroll : true,
    split : true,
    items: [
        {xtype:'textfield',name:'id',fieldLabel:'ID',labelStyle:'text-align:right;',anchor:'95%',hidden:true},
        {xtype:'textfield',name:'managerId',fieldLabel:'视图项ID<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false,readOnly:true},
        {xtype:'textfield',name:'controlCode',fieldLabel:'控制点Code<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false},
        {xtype:'textfield',name:'controlName',fieldLabel:'控制点名称<font color="red">*</font>',labelStyle:'text-align:right;',anchor:'95%',allowBlank:false}
    ]
});

//create the maintain window
var maintainWindow = new Ext.Window({
    title:'新增',
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
            var data=maintainForm.getForm().getFieldValues();
            Ext.Ajax.request({
                url: basepath + '/CustViewMaintainControl-action.json',
                method: 'POST',
                params :data,
                waitMsg: '正在保存数据，请稍等...',
                success: function(response){
            		controllerStore1.reload();  
                    Ext.Msg.alert('提示','操作成功！');
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

var maintainWindow1 = new Ext.Window({
    title:'修改',
    width: 400,
    height: 240,
    layout: 'fit',
    closable: true,
    closeAction: 'hide',
    buttonAlign: 'center',
    model: true,
    items : [maintainForm1],
    buttons:[{
        text:'保存',
        handler:function(){   
            if (!maintainForm1.getForm().isValid()) {
                Ext.Msg.alert("提示", "输入有误或存在漏输项，请重新输入！");
                return false;
            }          
            var data=maintainForm1.getForm().getFieldValues();
            Ext.Ajax.request({
                url: basepath + '/CustViewMaintainControl-action.json',
                method: 'POST',
                params :data,
                waitMsg: '正在保存数据，请稍等...',
                success: function(response){
            		controllerStore1.reload();  
                    Ext.Msg.alert('提示','操作成功！');
                    maintainWindow1.hide();                   
                },
                failure:function(){
                    Ext.Msg.alert('提示','操作失败！');
                }
            });
        }
    },{
        text: '关闭',
        handler: function(){
            maintainWindow1.hide();
        }
    }]
});

//create the gridPanel新增表格
var controllerPanel = new Ext.grid.GridPanel({
	height : 260,
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

//修改表格
var controllerPanel1 = new Ext.grid.GridPanel({
	height : 260,
    frame: true,
    region: 'center',
    autoScroll: true,
    stripeRows: true,
    store: controllerStore1,
    cm : controllerCm1,
    sm : controllerSm1,
    tbar: controllerTbar1,
    viewConfig:{},
    loadMask: {
        msg: '正在加载表格数据,请稍等...'
    }
});

//详情表格
var controllerPanel2 = new Ext.grid.GridPanel({
	height : 260,
    frame: true,
    region: 'center',
    autoScroll: true,
    stripeRows: true,
    store: controllerStore2,
    cm : controllerCm2,
    sm : controllerSm2,
    viewConfig:{},
    loadMask: {
        msg: '正在加载表格数据,请稍等...'
    }
});

//保存调用的方法
function save(formPanel){
	if (!formPanel.getForm().isValid()) {
		Ext.MessageBox.alert('系统提示','请正确输入各项必要信息！');
		return false;
	}
	var commintData =translateDataKey(formPanel.getForm().getFieldValues(),_app.VIEWCOMMITTRANS);
	Ext.Ajax.request({
 		url:basepath+'/CustViewMaintainInfo-action.json',
 		method:'POST',
 		params :commintData,
 		success:function(response){
 			Ext.Msg.alert('提示','保存成功！');
 			reloadCurrentData();
 			
		},
 		failure:function(){
 			Ext.Msg.alert('提示','保存失败！');
 		}
	});
}
/**
 * 
*新增面板
 */
var customerView =  [{
	title : '新增',
	type : 'form',
	autoLoadSeleted : false,
	groups : [{
		fields : ['ID','VIEWTYPE','NAME','ORDERS','PARENTID']
	},{
		columnCount : 1 ,
		fields : ['ADDR'],
		fn : function(ADDR){
			ADDR.anchor = '95%';
			return [ADDR];
		}
	},{
		columnCount : 1 ,
		fields : ['ADDR'],
		fn : function(ADDR){
			return [controllerPanel];
		}
	}],
	formButtons : [{
		text : '保存',
		fn : function(formPanel,basicForm) {
			save(formPanel);
		}
	}]
},{
	title : '修改',
	type : 'form',
	autoLoadSeleted : true,
	groups : [{
		fields : ['ID','VIEWTYPE','NAME','ORDERS','PARENTID']
	},{
		columnCount : 1 ,
		fields : ['ADDR'],
		fn : function(ADDR){
			ADDR.anchor = '95%';
			return [ADDR];
		}
	},{
		columnCount : 1 ,
		fields : ['ADDR'],
		fn : function(ADDR){
			return [controllerPanel1];
		}
	}],
	formButtons : [{
		text : '保存',
		fn : function(formPanel,basicForm) {
	    	save(formPanel);
		}
	}]
},{
	title : '详情',
	type : 'form',
	autoLoadSeleted : true,
	groups : [{
		fields : ['ID','VIEWTYPE','NAME','ORDERS','PARENTID'],
		fn : function(ID,NAME,VIEWTYPE,PARENTID,ORDERS){
			NAME.cls = 'x-readOnly';
			NAME.readOnly = true;
			VIEWTYPE.cls = 'x-readOnly';
			VIEWTYPE.readOnly = true;
			ORDERS.cls = 'x-readOnly';
			ORDERS.readOnly = true;
			PARENTID.hidden = false;
			return [ID,NAME,VIEWTYPE,PARENTID,ORDERS];
		}
	},{
		columnCount : 1 ,
		fields : ['ADDR'],
		fn : function(ADDR){
			ADDR.cls = 'x-readOnly';
			ADDR.readOnly = true;
			ADDR.anchor = '95%';
			return [ADDR];
		}
	},{
		columnCount : 1 ,
		fields : ['ADDR'],
		fn : function(ADDR){
			return [controllerPanel2];
		}
	}]
}];

var tbar =[{
	/**
	 * 视图项删除
	 */
	text : '删除',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}
		var ids = '';
		var selectRecords = getAllSelects();
		for(var i=0;i<selectRecords.length;i++){
			if(i == 0){
				ids += selectRecords[i].data.ID;
			}else{
				ids += ',' + selectRecords[i].data.ID;
			}
		}
		Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
			if(buttonId.toLowerCase() == "no"){
				return;
			}
			Ext.Ajax.request({
				url: basepath+'/CustViewMaintainInfo-action!destroy.json',
				waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
                params: {
                    'idStr': ids
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
}];

/**
 * 结果域面板滑入前触发,系统提供listener事件方法
 * @param {} view
 * @return {Boolean}
 */
var beforeviewshow = function(view){
	if (view._defaultTitle != '新增'){
		if (getSelectedData()==false){
			Ext.Msg.alert('提示','请选择一条数据进行操作');
			return false;
		}
		controllerStore2.load( {
			params : {
				managerId : getSelectedData().data.ID
			}
		});
		controllerStore1.load( {
			params : {
				managerId : getSelectedData().data.ID
			}
		});
	}
};
