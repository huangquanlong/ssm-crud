/**
 * @description 角色数据权限授权
 * @author helin
 * @since 2014-07-01
 */
imports([
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js'
]);

Ext.QuickTips.init();

//数据字典定义
var lookupTypes = [{
	TYPE : 'DATA_ROLE',//自定义数据权限选项数据字典
	url : '/datagrantstorequery!getFilters.json',
	key : 'ID',
	value : 'DESCRIBETION',
	jsonRoot : 'json.data'
}];

//树配置查询类型
var treeLoaders = [{
	key : 'ROLE_TREE_LOADER_KEY',	//角色树loader
	url : basepath + '/sysRole-kind-tree.json',
	parentAttr : 'APP_ID',//此处因为要加载所有角色,又不想去多添加一个字段,故而把父节结设置为：APP_ID
	locateAttr : 'ID',
	rootValue : '62',
	textField : 'ROLE_NAME',
	idProperties : 'ID',
	jsonRoot:'json.data'
}];
var treeCfgs = [{
	key : 'ROLE_TREE_KEY',
	loaderKey : 'ROLE_TREE_LOADER_KEY',
	autoScroll:true,
	rootVisible: false,
	title : '银行角色树',
	rootCfg : {
		expanded:true,
		id:'root',
		text:'所有角色',
		autoScroll:true,
		children:[]
	},
	clickFn:function(node){
		roleId = node.id;
		getCurrentView().setParameters({
			roleId:roleId
		});
	}
}];

var needGrid = false;
WLJUTIL.suspendViews=false;  //自定义面板是否浮动

var roleId='';//查看的角色
var roleDataCombo= new Ext.form.ComboBox({
	editable: false,
	mode : 'local',
	store: new Ext.data.Store(),
	triggerAction : 'all',
	displayField:'value',
	valueField:'key'
});
//自定义下拉框 反显数据
Ext.util.Format.comboRenderer = function(combo){
	return function(value, metaData, record){
		var record11 = combo.findRecord(combo.valueField, value);
		return record11 ? record11.get(combo.displayField) : (record.data.DESCRIBETION?record.data.DESCRIBETION:value);
	}
};

var fields = [
	{name: 'TEST',text:'此文件fields必须要有一个无用字段', resutlWidth:80}
];

//左则角色树
var roleTree = TreeManager.createTree('ROLE_TREE_KEY');

//边缘面板配置
var edgeVies = {
	left : {
		width : 300,
		layout : 'fit',
		collapsible : false,
		items : [roleTree]
	}
};

//结果域扩展功能面板
var customerView = [{
	/**
	 * 自定义数据权限设置面板
	 */
	title:'数据权限设置',
	hideTitle: true,
	type : 'grid',
	url : basepath + '/datagrantquery.json',
	pageable : false,
	grideditable : true,
	fields: {
		fields : [
			{name: 'AID',text: 'AID',hidden: true},
			{name: 'DESCRIBETION',text: 'DESCRIBETION',hidden: true,editor: new Ext.form.TextField(),readOnly:true},
			{name: 'CLASS_DESC', text: '查询名称',width: 200},
			{name: 'CLASS_NAME', text: '查询Action',width: 200},
			{name: 'FID', text: '数据权限',translateType : 'DATA_ROLE',width: 200
				,editor: roleDataCombo,renderer :Ext.util.Format.comboRenderer(roleDataCombo)}
		]
	},
	gridButtons:[{
		/**
		 * 数据权限设置
		 */
		text : '保存',
		fn : function(grid){
			if(roleId == ''){
				Ext.Msg.alert('提示', '提示请选择一个角色');
				return false;
			}
			Ext.Msg.wait('正在保存数据,请等待...','提示');
            var json0 = {'FILTER_ID':[]};
            var store = grid.getStore();
            for(var i=0;i<store.getCount();i++){
                var temp=store.getAt(i);
                if(temp.data.FID !== undefined && temp.data.FID !== ''){
                    json0.FILTER_ID.push(temp.data.FID);
                }
            }
            var temp0 = Ext.encode(json0).toString();
            Ext.Ajax.request({
                url : basepath + '/DataGrantManager-action!saveAllot.json',
                method : 'POST',
                waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
                params:{
                    'roleId':roleId,
                    'filterId':temp0
                },
                success : function() {
                    Ext.Msg.alert('提示', '操作成功');
                },
                failure : function(response) {
                    Ext.Msg.alert('提示', '操作失败,失败原因!');
                }
            });
        }
	},{
		/**
		 * 数据权限重置
		 */
		text : '重置',
		fn : function(grid){
			getCurrentView().setParameters({
				roleId:roleId
			});
		}
	}]
}];

/**
 * 结果域面板滑入前触发,系统提供listener事件方法
 * @param {} view
 * @return {Boolean}
 */
var beforeviewshow = function(view){
	if(view._defaultTitle == '数据权限设置'){
		view.setParameters({
			roleId:roleId
		});
		view.grid.on('rowclick',function(grid,rowIndex){//点击某一行，加载该行下拉框内待选项的数据
        	var _record = grid.getSelectionModel().getSelections()[0];
	        if(_record){
	        	var _store = findLookupByType('DATA_ROLE');
	        	_store.load({
	                params : {
	                    'className' :_record.data.CLASS_NAME 
	                }
	            });
	            roleDataCombo.bindStore(_store);
	        }
	    });
	}
};