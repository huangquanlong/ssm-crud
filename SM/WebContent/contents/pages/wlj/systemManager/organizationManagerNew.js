/**
 * @description 机构管理
 * @author helin
 * @since 2014-04-24
 */

imports([
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js'
]);

Ext.QuickTips.init();

//树配置查询类型
var treeCondition = {searchType:'SUBTREE'};
var treeCondition1 = {searchType:'ALLORG'};
/**
 * 树形结构的loader对象配置
 */
var treeLoaders = [{
	key : 'DATASETMANAGERLOADER',
	url : basepath + '/commsearch.json?condition='+Ext.encode(treeCondition),
	parentAttr : 'SUPERUNITID',
	locateAttr : 'UNITID',
	jsonRoot:'json.data',
	rootValue : JsContext._orgId,
	textField : 'UNITNAME',
	idProperties : 'UNITID'
}];
/**
 * 树形面板对象配置
 */
var treeCfgs = [{
	key : 'DATASETMANAGERTREE',
	loaderKey : 'DATASETMANAGERLOADER',
	autoScroll:true,
	rootCfg : {
		expanded:true,
		id:JsContext._orgId,
		text:JsContext._unitname,
		autoScroll:true,
		children:[]
	},clickFn : function(node){
		setSearchParams({
			TREE_ORG : node.id
		});
	}
},{
	key : 'DATASETMANAGERTREE1',
	loaderKey : 'DATASETMANAGERLOADER',
	autoScroll:true,
	rootCfg : {
		expanded:true,
		id:JsContext._orgId,
		text:JsContext._unitname,
		UNITID: JsContext._orgId,
		UNITNAME: JsContext._unitname,
		autoScroll:true,
		children:[]
	},clickFn : function(node){
	}
}];
//左边树
var leftTree = TreeManager.createTree('DATASETMANAGERTREE');

var url = basepath+'/systemUnit-query.json';
var comitUrl = basepath+'/systemUnit-query.json';		
		
var fields = [
    {name: 'ID',hidden : true},
    {name: 'APP_ID',hidden:true},
    {name: 'ORG_ID',text:'机构编号',searchField: true,editable:true,resutlWidth:100,allowBlank:false},
    {name: 'ORG_NAME',text: '机构名称',searchField: true,editable:true,allowBlank:false,vtype:'trim',maskRe:/^[^~!@#$%&*() _+=\-`{}\[\]\^:";'<>?,.\/]$/},
    {name: 'UP_ORG_ID',text:'上层机构',hidden:true,xtype: 'wcombotree',innerTree:'DATASETMANAGERTREE1',allowBlank:false,showField:'text',hideField:'UNITID',editable:false},
    {name: 'PARENT_ORG_NAME',text:'上层机构',readOnly:true},
    {name: 'ORG_LEVEL',text:'层次',resutlWidth:80,readOnly:true,searchField: false}, 
    {name: 'LAUNCH_DATE',text:'开办日期',resutlWidth:100,editable:false,dataType:'date',format:'Y-m-d'},
    {name: 'POST_CODE',text:'邮政编码',resutlWidth:100,vtype:'postcode'}, 
    {name: 'ORG_ADDR',text:'机构地址',resutlWidth:260},
    {name: 'QTIPS',text:'提示',xtype: 'displayfield',hidden:true,value:'机构编号填写规则为：6位数字，格式为：10-01-03，其中前两位默认为10，中间两位表示区域或分行，后续在目前最大数字上加1，最后两位表示支行。新增区域或分行时最后两位为00。'},
    {name: 'ACCOUNT_ID',hidden:true}
];

var createView = true;
var editView = true;
var detailView = true;

/**
 * 新增设计
 */
var createFormViewer = [{
	columnCount: 1,
	fields : ['ORG_ID','ORG_NAME','ORG_LEVEL','UP_ORG_ID','APP_ID','LAUNCH_DATE','POST_CODE','ORG_ADDR','QTIPS'],
	fn : function(ORG_ID,ORG_NAME,ORG_LEVEL,UP_ORG_ID,APP_ID,LAUNCH_DATE,POST_CODE,ORG_ADDR,QTIPS){
		APP_ID.value='62';	
		UP_ORG_ID.hidden=false;
		QTIPS.hidden = false;
		UP_ORG_ID.listeners={
			'select':function(a,b,c){
			}
		};
		return [ORG_ID,ORG_NAME,ORG_LEVEL,UP_ORG_ID,APP_ID,LAUNCH_DATE,POST_CODE,ORG_ADDR,QTIPS];
	}
}];

/**
 * 修改设计
 */
var editFormViewer = [{
	columnCount: 1,
	fields : ['ORG_ID','ORG_NAME','ORG_LEVEL','UP_ORG_ID','PARENT_ORG_NAME','APP_ID','LAUNCH_DATE','POST_CODE','ORG_ADDR','QTIPS'],
	fn : function(ORG_ID,ORG_NAME,ORG_LEVEL,UP_ORG_ID,PARENT_ORG_NAME,APP_ID,LAUNCH_DATE,POST_CODE,ORG_ADDR,QTIPS){
		APP_ID.value='62';	
		UP_ORG_ID.hidden=true;
		UP_ORG_ID.readOnly=false;
		PARENT_ORG_NAME.hidden=false,
		QTIPS.hidden = false;
		ORG_ID.readOnly=true;
		ORG_ID.allowBlank = true;
		return [ORG_ID,ORG_NAME,ORG_LEVEL,UP_ORG_ID,PARENT_ORG_NAME,APP_ID,LAUNCH_DATE,POST_CODE,ORG_ADDR,QTIPS];
	}
}];

/**
 * 详情设计
 */
detailFormViewer = [{
	columnCount: 1,
	fields : ['ORG_ID','ORG_NAME','ORG_LEVEL','UP_ORG_ID','PARENT_ORG_NAME','APP_ID','LAUNCH_DATE','POST_CODE','ORG_ADDR'],
	fn : function(ORG_ID,ORG_NAME,ORG_LEVEL,UP_ORG_ID,PARENT_ORG_NAME,APP_ID,LAUNCH_DATE,POST_CODE,ORG_ADDR){
		UP_ORG_ID.hidden=true;
		PARENT_ORG_NAME.hidden=false,
		ORG_NAME.allowBlank = true;
		ORG_ID.readOnly=true;
		ORG_ID.allowBlank = true;
		LAUNCH_DATE.readOnly = true;
		POST_CODE.readOnly = true;
		ORG_ADDR.readOnly = true;
		
		ORG_LEVEL.disabled = true;
		ORG_LEVEL.cls = "x-readOnly";
		PARENT_ORG_NAME.disabled = true;
		PARENT_ORG_NAME.cls = "x-readOnly";
		ORG_NAME.disabled = true;
		ORG_NAME.cls = "x-readOnly";
		ORG_ID.disabled = true;
		ORG_ID.cls = "x-readOnly";
		LAUNCH_DATE.disabled = true;
		LAUNCH_DATE.cls = "x-readOnly";
		POST_CODE.disabled = true;
		POST_CODE.cls = "x-readOnly";
		ORG_ADDR.disabled = true;
		ORG_ADDR.cls = "x-readOnly";
		return [ORG_ID,ORG_NAME,ORG_LEVEL,UP_ORG_ID,PARENT_ORG_NAME,APP_ID,LAUNCH_DATE,POST_CODE,ORG_ADDR];
	}
}];

/**
 * 数据联动，当数据发生变化，且失去焦点的时候，会调用该联动逻辑
 */
var linkages = {
	UP_ORG_ID : {
		fields : ['ORG_LEVEL'],
		fn : function(UP_ORG_ID,ORG_LEVEL){
			Ext.each(treeCfgs[1].resloader.nodeArray,function(a){
				if(a.id == UP_ORG_ID.value){
					ORG_LEVEL.setValue(Number(a.LEVELUNIT)+1);
				}
			});
		}
	}
};

/**
 * 自定义工具条上按钮
 * 注：批量选择未实现,目前只支持单条删除
 */
var tbar = [{
	/**
	 * 系统机构删除
	 */
	text : '删除',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
			var selectedData = getSelectedData().data;
			var id=selectedData.ID;
			Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
				if(buttonId.toLowerCase() == "no"){
					return;
				}
				Ext.Ajax.request({
					url: basepath + '/systemUnit-query!batchDestroy.json',
					waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
	                params: {
                        'idStr': id
                    },
					success : function() {
                        Ext.Msg.alert('提示', '删除成功');
                        var node = {};
						node.UNITID = selectedData.ORG_ID;
						node.UNITNAME = selectedData.ORG_NAME;
						node.LEVELUNIT =  selectedData.ORG_LEVEL;
						node.SUPERUNITID =selectedData.UP_ORG_ID;
						leftTree.deleteNode(node);
						reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '删除失败');
						reloadCurrentData();
					}
				});
			});
		}
	}
}];

/**
 * 边缘面板配置
 */
var edgeVies = {
	left : {
		/**
		 * 左边机构树展示
		 */
		width : 200,
		layout : 'form',
		items : [leftTree]
	}
};

/**
 * 结果域面板滑入前触发,系统提供listener事件方法
 * @param {} view
 * @return {Boolean}
 */
var beforeviewshow = function(view){
	if(view.baseType == 'createView'){
		//view.contentPanel.getForm().findField('UP_ORG_ID').setValue('');
	}else{
		if(!getSelectedData() || getAllSelects().length > 1){
			Ext.Msg.alert('提示信息','请选择一条数据后操作！');
			return false;
		}
	}
};

/**
 * 结果域面板滑入后触发,系统提供listener事件方法
 * @param {} view
 * @return {Boolean}
 */
var viewshow = function(view){
	if(view.baseType == 'editView'){
		var qtips = "机构编号填写规则为：6位数字，格式为：10-01-03，其中前两位默认为10，中间两位表示区域或分行，后续在目前最大数字上加1，最后两位表示支行。新增区域或分行时最后两位为00。";
		view.contentPanel.getForm().findField('QTIPS').setValue(qtips);
	}
};

/**
 * 数据提交之后触发
 * @param {} data
 * @param {} cUrl
 * @param {} result
 */
var afertcommit = function(data, cUrl, result){
	var tempView = getCurrentView();
	var tempUrl = cUrl.indexOf('!') > 0 ? cUrl.split('!')[0] : cUrl.split('\.')[0];
	if(result){
		if(tempView.baseType == 'createView'){
			Ext.Ajax.request({
				url : tempUrl + '!getPid.json',
				method : 'GET',
				success : function(response) {
					var nodeArra = Ext.util.JSON.decode(response.responseText);
					var node = {};
					node.ID = nodeArra.pid;
					node.id = nodeArra.pid;
		            node.APP_ID = data.APP_ID;
					node.UNITID = data.ORG_ID;
					node.UNITNAME = data.ORG_NAME;
					node.LEVELUNIT = data.ORG_LEVEL;
					node.SUPERUNITID = data.UP_ORG_ID;
					leftTree.resloader.addNode(node);
				}
			});
		}else if(tempView.baseType == 'editView'){
			var node = {};
			node.ID = data.ID;
            node.APP_ID = data.APP_ID;
			node.UNITID = data.ORG_ID;
			node.UNITNAME = data.ORG_NAME;
			node.LEVELUNIT = data.ORG_LEVEL;
			node.SUPERUNITID = data.UP_ORG_ID;
			leftTree.editNode(node);
		}
	}
};
