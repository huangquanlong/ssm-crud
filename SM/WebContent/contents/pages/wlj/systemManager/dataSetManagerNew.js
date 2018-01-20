/**
 * 数据集管理
 * @author shiyang
 * @since 2014-04-29
 */

imports([
	'/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js',
	'/contents/pages/common/Com.yucheng.bcrm.common.Annacommit.js'
]);


//定义左侧数据集树
var treeLoaders = [{
	key : 'DATASETMANAGERLOADER',
	url : basepath + '/datasetmanagerquery.json',
	parentAttr : 'PARENT_ID',
	locateAttr : 'ID',
	jsonRoot:'JSON.data',
	rootValue : '0',
	textField : 'NAME',
	idProperties : 'ID'
}];
var treeCfgs = [{
	key : 'DATASETMANAGERTREE',
	loaderKey : 'DATASETMANAGERLOADER',
	//title : '资讯文档树',
	autoScroll:true,
	rootCfg : {
		expanded:true,
		text:'全部目录',
		autoScroll:true,
		children:[]
	},
	clickFn : function(node){
		/**
		 * 调用windows域API；
		 */
		setSearchParams({
			dataSetType : isNaN(Number(node.id))?"":node.id  //解决掉根结节不是数字类型问题
		});
	}
}];
		
var lookupTypes = [{
	TYPE : 'PARENT_ID',
	url : '/datasetmanagerquery.json',
	key : 'ID',
	value : 'NAME',
	root : 'JSON.data'
},{
	TYPE : 'TABLE_NAME',
	url : '/datasetmanagerquery!queryDataSetSolution.json',
	key : 'VALUE',
	value : 'VALUE',
	root : 'JSON.data'
}];
var localLookup = {
	'TYPE' : [
	   {key : '1',value : '数据库表'},
	   {key : '2',value : '标准SQL'}
	]
};

var url = basepath+'/datasetquery.json';
var comitUrl = basepath+'/dataSetNewAction.json';

var fields = [
    {name: 'ID',hidden : true},
    {name: 'APP_ID',hidden : true},
    {name: 'NAME', text : '数据集名称', searchField: true,allowBlank:false},                                   
    {name: 'PARENT_ID',text:'所属目录',hidden:true,allowBlank:false,translateType : 'PARENT_ID',listeners:{
    	'select':function(a){}
    }},  
    {name: 'TYPE', text :'类型',resutlWidth:450,translateType : 'TYPE',allowBlank:false},
    {name: 'VALUE', text:'表名',resutlWidth:400,hidden:true,translateType : 'TABLE_NAME',allowBlank:false},
    {name: 'NOTES', text:'描述',resutlWidth:400,allowBlank:false} 
];

var createView = true;
var editView = true;
var detailView = true;

  		
/**新增、修改、详情设计**/
var formViewers = [{
	fields : ['APP_ID','NAME','PARENT_ID','TYPE','VALUE'],
	fn : function(APP_ID,NAME,PARENT_ID,TYPE,VALUE){
		APP_ID.value='62';
		PARENT_ID.hidden=false;
		VALUE.hidden=false;	
		return [NAME,PARENT_ID,TYPE,VALUE];
	}
},{
	columnCount : 1 ,
	fields : ['NOTES'],
	fn : function(NOTES){
		NOTES.xtype='textarea';
		return [NOTES];
	}
}];

var detailFormViewer = [{
	fields : ['APP_ID','NAME','PARENT_ID','TYPE','VALUE'],
	fn : function(APP_ID,NAME,PARENT_ID,TYPE,VALUE){
		APP_ID.value='62';
		PARENT_ID.hidden=false;
		VALUE.hidden=false;	
		APP_ID.disabled = true;
		NAME.disabled = true;
		PARENT_ID.disabled = true;
		TYPE.disabled = true;
		VALUE.disabled = true;
		return [NAME,PARENT_ID,TYPE,VALUE];
	}
},{
	columnCount : 1 ,
	fields : ['NOTES'],
	fn : function(NOTES){
		NOTES.xtype='textarea';
		NOTES.disabled = true;
		return [NOTES];
	}
}];


//删除按钮，删除一个数据集方法
var tbar = [{
	text : '删除',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
			var idStr=getSelectedData().data.ID;
			var temp={'id':[]};
			temp.id.push(idStr);
			Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
				if(buttonId.toLowerCase() == "no"){
					return false;
				} 
			    Ext.Ajax.request({
	                url: basepath+'/dataset.json',                                
	                method : 'POST',
	                params : {
			    		operate : 'delete',
	                    isBat : true ,
	                    batString : idStr,
	                    ID:Ext.encode(temp)
	                },
	                success : function(){
	                    Ext.Msg.alert('提示', '删除成功');
	                    reloadCurrentData();
	                },
	                failure : function(){
	                    Ext.Msg.alert('提示', '删除失败');
	                    reloadCurrentData();
	                }
	            });
			});
		}
	}
}];

var edgeVies = {
	left : {
		width : 200,
		layout : 'form',
		items : [TreeManager.createTree('DATASETMANAGERTREE')]
	}
};

/**
 * *
 * @param data
 * @param cUrl
 * @param result
 * 提交保存之后的ajax请求
 * @author shiyang
 * @since 2014-04-30
 */
var afertcommit = function(data, cUrl, result){
	var tempView = getCurrentView();
	if(tempView.baseType == 'createView'){
		tbname=data.VALUE;
		Ext.Ajax.request({
			url : cUrl.split('.')[0]+'!getPid.json',
			method : 'GET',
			success : function(response) {
				var nodeArra = Ext.util.JSON.decode(response.responseText);
				tempPid=nodeArra.pid;
				lockGrid();
				showCustomerViewByTitle('数据集方案字段设置');
			}
		});
	}
};
var beforeviewshow = function(view){
	if(view._defaultTitle!='新增'){
		if(view._defaultTitle=='数据集方案字段设置'){
			if(getSelectedData()){
				operate='1';//表示修改，0表示新增
				//updateTyep 0表示不清空已配置的字段进行修改，1表示清空以前字段，重新配置修改
				Ext.MessageBox.confirm('提示','是否重新配置字段?',function(buttonId){
					if(buttonId.toLowerCase() == "no"){
						updateTyep='0';
					}else{
						updateTyep='1';
					}
					columnStore.load({
						params:{
		                  "tbname":getSelectedData().data.VALUE,
		                  "operate":operate,
		                  "rowId":getSelectedData().data.ID,
		                  "updateTyep":updateTyep
						}
					});
				});
				return true;
			}else if(tempPid!=''&&tbname!=''){
				operate='0';
				columnStore.load({
					params:{
	                  "tbname":getSelectedData().data.VALUE,
	                  "operate":'0',
	                  "rowId":'',
	                  "updateTyep":updateTyep
					}
				});
				return true;
			}else{
				Ext.Msg.alert('提示','请选择一条数据进行操作！');
				return false;
			}
		}
	}
	if(view._defaultTitle!='修改'||view._defaultTitle!='详情'){
		if(view == getEditView()){
			if(getSelectedData()==false || getAllSelects().length > 1){
				Ext.Msg.alert('提示','请选择一条数据!');
				return false;
			}
		}
		if(view == getDetailView()){
			if(getSelectedData()==false || getAllSelects().length > 1){
				Ext.Msg.alert('提示','请选择一条数据!');
				return false;
			}
		}
	}
};

//全局标量
var tempPid='';
var tbname='';

//备注（数据字典类型）store
var lookupIdStore = new Ext.data.JsonStore({
	id : lookupIdStore,
	restful : true,
	proxy : new Ext.data.HttpProxy({
		url : basepath + '/lookup-mapping.json'
	}),
	fields : [ 'name','comment' ],
	reader : new Ext.data.JsonReader({
		totalProperty : 'list'
	}, [ {
		name : 'name',
		mapping : 'name'
	},{
		name : 'comment',
		mapping : 'comment'
	} ])
});

lookupIdStore.load({
	callback:function(){
		var newcreate = Ext.data.Record.create(['name', 'comment']);
		var recordadd = new newcreate({name:'',comment:'无'});
		lookupIdStore.insert(0,recordadd);
	}
});
var numberField = new Ext.form.NumberField({allowBlank : false,minValue:0.01,maxValue:100.00});  
var textField = new Ext.form.TextField({allowBlank : false,minValue:0}); 
var columnsm = new Ext.grid.CheckboxSelectionModel({
	checkOnly: true
});
var columnrownum = new Ext.grid.RowNumberer({
	header : 'No.',
	width : 28
});   
//数据集方案字段设置CM
var cmodel = new Ext.grid.ColumnModel([
    columnrownum,columnsm, 
    {header :'ID',dataIndex:'ID',width:130,sortable : true,hidden:true},
    {header :'字段名',dataIndex:'NAME',width:130,sortable : true},
    {header :'中文名称 ',dataIndex:'REMARKS',width:150,sortable : true,editor:textField},
    {header :'字段类型',dataIndex:'COLTYPE',width:130,sortable:true},
    {header :'长度',dataIndex:'LENGTH',width:130,sortable:true},
    {header :'是否主键 ',dataIndex:'NULLS',width:100,sortable : true},
    {header :'是否为空 ',dataIndex:'KEYSEQ',width:100,sortable : true},
    {header :'排序 ',dataIndex:'COLNO',width:100,sortable : true,hidden:true},
    {header :'备注 (数据字典类型)',dataIndex : 'NOTES',width:150,sortable : true
    	,editor :{
        	xtype:'combo',
        	store : lookupIdStore,
        	mode : 'local',
        	triggerAction : 'all',
        	valueField : 'name',
        	displayField : 'comment',
        	forceSelection:true,
			resizable:true,
			typeAhead : true,
			emptyText : '请选择',
        	listeners:{
	        	select:function(){
	        		var valuefind = this.value;
	        		this.fireEvent('blur',this);
	        	}
        	}
    	},
    	renderer:function(val){
        	if(val!=''){
        		var stolength = lookupIdStore.data.items;
        		var i=0;
        		for(i=0;i< stolength.length;i++){
        			if(stolength[i].data.name==val){
        				return stolength[i].data.comment;
        			}
        		}
        	}
        	return val;	
        }
    }
    ]);
    
//数据集方案字段设置store
var columnStore = new Ext.data.Store({
	restful : true,
	proxy : new Ext.data.HttpProxy({
		url : basepath + '/datasetmanagerquery!queryDataSetColumn.json'
	}),
	reader : new Ext.data.JsonReader( {
		root : 'JSON.data'
	}, [ 
	{name : 'ID'},
	{name : 'NAME'},
	{name : 'REMARKS'},
	{name : 'COLTYPE'},
	{name : 'LENGTH'},
	{name : 'NULLS'},
	{name : 'KEYSEQ'},
	{name : 'COLNO'},
	{name : 'NOTES'}
	])
});
		 	
/***
 * 解决不能保存多条的问题 auther:zm 20130114
 */
columnStore.on('update', function(store,records,operation) {
	if(changeDataList.length > 0){
		var count = 0;
		for ( var i = 0; i < changeDataList.length; i++) {// 遍历改变数据集，若不为重复记录，则添加至数据集
			if(changeDataList[i].data.ID == records.data.ID){
				count++;
			}
		}
		if(count==0){
			changeDataList.push(records);
		}
	}else{
		changeDataList.push(records);
	}
});

//数据集方案字段设置Grid
var columnGrid = new Ext.grid.EditorGridPanel( {
	frame : true,
	clicksToEdit : 1,
	id : 'assignInfoGrid',				
	height : 400,
	store : columnStore,
	loadMask : true,
	cm : cmodel,
	sm : columnsm,
	buttonAlign : 'center',
	buttons : [{
		text : '保存',
		id : 'save',
		handler : function() {
			if (operate == '0') {
				saveall();
			} else if (operate == '1') {
				if (updateTyep == '0') {
					updateall();
				} else if (updateTyep == '1') {
					updatenew();
				}
			}
		}
	}]
});

var customerView = [{
	title : '数据集方案字段设置',
	xtype : 'panel',
	frame : true,
	layout : 'fit',
	items:[columnGrid],
	recordView : true
}];

var changeDataList = new Array();

/**
 *数据集方案字段设置保存 方法（新增）
 *@author shiyang
 * @since 2014-04-30
 */
var saveall=function(){
	var checkedNodes = columnGrid.getSelectionModel().selections.items;
	if (checkedNodes.length == 0) {
		Ext.Msg.alert('提示', '未选择任何记录');
		return;
	};
	var json1={'NAME':[]};
 	var json2={'REMARKS':[]};
 	var json3={'COLTYPE':[]};
 	var json4={'LENGTH':[]};
 	var json5={'NULLS':[]};
	var json6={'KEYSEQ':[]};
	var json7={'NOTES':[]};
	var json8={'COLNO':[]};
	for(var i=0;i<checkedNodes.length;i++){
		json1.NAME.push(checkedNodes[i].data.NAME);
		json2.REMARKS.push(checkedNodes[i].data.REMARKS);
		json3.COLTYPE.push(checkedNodes[i].data.COLTYPE);
		json4.LENGTH.push(checkedNodes[i].data.LENGTH);
		json5.NULLS.push(checkedNodes[i].data.NULLS);
		json6.KEYSEQ.push(checkedNodes[i].data.KEYSEQ);
		json7.NOTES.push(checkedNodes[i].data.NOTES);
		json8.COLNO.push(checkedNodes[i].data.COLNO);
	}
	Ext.Ajax.request({
		url:basepath+'/dataset!create.json',
	    //form:tabForm.getForm().id,
        method: 'POST',
		params : {
			'NAME':Ext.encode(json1),
			'REMARKS': Ext.encode(json2),
			'COLTYPE': Ext.encode(json3),
			'LENGTH': Ext.encode(json4),
			'NULLS': Ext.encode(json5),
			'KEYSEQ': Ext.encode(json6),
			'NOTES': Ext.encode(json7),
			'COLNO':Ext.encode(json8),
			'operate': 'add',
			dataSetId: tempPid
		},
		success : function(response) {
			Ext.Msg.alert('提示', '新增成功');
		},
		failure : function(response) {
		  	var resultArray = Ext.util.JSON.decode(response.status);
		   	if(resultArray == 403) {
		      	Ext.Msg.alert('提示','您没有此权限!');
		   	} else {
			  	Ext.Msg.alert('提示','加入失败!');
		   	}
		}
	});
};

/**
 * 数据集方案字段设置保存 方法（修改）
 *@author shiyang
 * @since 2014-04-30
 */
var updateall=function (){
	if (changeDataList.length == 0) {
		Ext.Msg.alert('提示', '未作任何数据变动');
		return;
	}
 	var json2={'REMARKS':[]};
	var json7={'NOTES':[]};
	var json9={'ID':[]};
	for(var i=0;i<changeDataList.length;i++){
		json2.REMARKS.push(changeDataList[i].data.REMARKS);
		json7.NOTES.push(changeDataList[i].data.NOTES);
		json9.ID.push(changeDataList[i].data.ID);
	}
	changeDataList = new Array();
	Ext.Ajax.request({
		url:basepath+'/dataset.json',
        method: 'POST',
		params : {
			'REMARKS': Ext.encode(json2),
			'NOTES': Ext.encode(json7),
			'ID':Ext.encode(json9),
			'operate': 'update'
		},
		success : function(response) {
			Ext.Msg.alert('提示', '修改成功');
			reloadCurrentData();
		},
		failure : function(response) {
		   var resultArray = Ext.util.JSON.decode(response.status);
		   if(resultArray == 403) {
		      Ext.Msg.alert('提示','您没有此权限!');
		   } else {
			  Ext.Msg.alert('提示','加入失败!');
		   }
		}
	});
};

/**
 * 数据集方案字段设置修改新增的字段
 *@author shiyang
 * @since 2014-04-30
 */
var updatenew=function (){
	var checkedNodes = columnGrid.getSelectionModel().selections.items;
	if (checkedNodes.length == 0) {
		Ext.Msg.alert('提示', '未选择任何记录');
		return;
	};
	var json1={'NAME':[]};
 	var json2={'REMARKS':[]};
 	var json3={'COLTYPE':[]};
 	var json4={'LENGTH':[]};
 	var json5={'NULLS':[]};
	var json6={'KEYSEQ':[]};
	var json7={'NOTES':[]};
	var json8={'COLNO':[]};
	for(var i=0;i<checkedNodes.length;i++){
		json1.NAME.push(checkedNodes[i].data.NAME);
		json2.REMARKS.push(checkedNodes[i].data.REMARKS);
		json3.COLTYPE.push(checkedNodes[i].data.COLTYPE);
		json4.LENGTH.push(checkedNodes[i].data.LENGTH);
		json5.NULLS.push(checkedNodes[i].data.NULLS);
		json6.KEYSEQ.push(checkedNodes[i].data.KEYSEQ);
		json7.NOTES.push(checkedNodes[i].data.NOTES);
		json8.COLNO.push(checkedNodes[i].data.COLNO);
	}
	Ext.Ajax.request({
		url:basepath+'/dataset.json',
        method: 'POST',
		params : {
			'pid':getSelectedData().data.ID,
			'NAME':Ext.encode(json1),
			'REMARKS': Ext.encode(json2),
			'COLTYPE': Ext.encode(json3),
			'LENGTH': Ext.encode(json4),
			'NULLS': Ext.encode(json5),
			'KEYSEQ': Ext.encode(json6),
			'NOTES': Ext.encode(json7),
			'COLNO':Ext.encode(json8),
			'operate': 'updatenew'
		},
		success : function(response) {
			Ext.Msg.alert('提示', '修改成功');
			reloadCurrentData();
		},
		failure : function(response) {
		    var resultArray = Ext.util.JSON.decode(response.status);
		    if(resultArray == 403) {
		        Ext.Msg.alert('提示','您没有此权限!');
		    } else {
			    Ext.Msg.alert('提示','加入失败!');
		    }
		}
	});
};