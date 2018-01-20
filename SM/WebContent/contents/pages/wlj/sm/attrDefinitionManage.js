/**
 * 配置管理-属性定义管理
 * @author DINGLEIXING
 * @since 2017-2-15
 */	
      //引入公共js文件
		imports([
		        '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js',
		        '/contents/pages/common/Com.yucheng.crm.common.OrgUserManage.js'
		        ]);	

var createView = true;
var editView = true;
var detailView = true;
var lookupTypes = [{//自定义属性类型种类ID下拉框
	TYPE : 'ATTR_CLASSIFY_ID',
	url : '/attrClassifyMappingAction.json',
	key : 'ID',
	value : 'NAME',
	jsonRoot : 'json.data'
}];
var localLookup = {
		'ATTR_CLASSIFY_TYPE' : [
			{key : '0',value : '数字'},
			{key : '1',value : '字符串'},
			{key : '2',value : '布尔'},
			{key : '3',value : '日期'},
		    {key : '4',value : '枚举'}
		]
	};
var url = basepath+'/attrDefinitionQueryAction.json';
var comitUrl = basepath+'/attrDefiniton.json';
var fields = [
    {name: 'ID',text:'属性定义ID'},
    {name: 'NAME', text :'属性名称', resutlWidth:150,allowBlank:false,searchField: true},                                   
    {name: 'COMM',text:'属性定义描述', searchField: false,allowBlank:true, resutlWidth:150},		   
    {name: 'DISPLAY_NAME',text:'显示名称', searchField: true,allowBlank:false, resutlWidth:150},	
    {name: 'ATTR_TYPE',text:'属性类型', allowBlank:false, resutlWidth:150,translateType:'ATTR_CLASSIFY_TYPE'},
    {name: 'CLASSIFY',text:'属性类别', searchField: true,allowBlank:false, resutlWidth:150,translateType:'ATTR_CLASSIFY_ID'},		   
    {name: 'TYPE_LENGTH',text:'类型长度',allowBlank:true, resutlWidth:100},		   
    {name: 'IDENTIFY',text:'逻辑标识',allowBlank:false, resutlWidth:100,gridField:false}		   
];

var formViewers = [{
	columnCount: 1,
	fields : ['ID','NAME','COMM','DISPLAY_NAME','ATTR_TYPE','CLASSIFY','TYPE_LENGTH','IDENTIFY'],
	fn : function(ID,NAME,COMM,DISPLAY_NAME,ATTR_TYPE,CLASSIFY,TYPE_LENGTH,IDENTIFY){
			ID.xtype = 'hidden';
			
		return [ID,NAME,COMM,DISPLAY_NAME,ATTR_TYPE,CLASSIFY,TYPE_LENGTH,IDENTIFY];
	}
}];
var detailFormViewer = [{
	columnCount: 1,
	fields : ['ID','NAME','COMM','DISPLAY_NAME','ATTR_TYPE','CLASSIFY','TYPE_LENGTH','IDENTIFY'],
	fn : function(ID,NAME,COMM,DISPLAYNAME,ATTR_TYPE,CLASSIFY,TYPE_LENGTH,IDENTIFY){
		ID.xtype = 'hidden';
		NAME.allowBlank = true;
		NAME.disabled = true;
		NAME.cls = 'x-readOnly';
		DISPLAYNAME.allowBlank = true;
		DISPLAYNAME.disabled = true;
		DISPLAYNAME.cls = 'x-readOnly';
		CLASSIFY.allowBlank = true;
		CLASSIFY.disabled = true;
		CLASSIFY.cls = 'x-readOnly';
		COMM.allowBlank = true;
		COMM.disabled = true;
		COMM.cls = 'x-readOnly';
		ATTR_TYPE.allowBlank = true;
		ATTR_TYPE.disabled = true;
		ATTR_TYPE.cls = 'x-readOnly';
		TYPE_LENGTH.allowBlank = true;
		TYPE_LENGTH.disabled = true;
		TYPE_LENGTH.cls = 'x-readOnly';
		IDENTIFY.allowBlank = true;
		IDENTIFY.disabled = true;
		IDENTIFY.cls = 'x-readOnly';
		return [ID,NAME,COMM,DISPLAYNAME,ATTR_TYPE,CLASSIFY,TYPE_LENGTH,IDENTIFY];
	}
}];

var tbar = [{
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
				var selectRe;
				var tempId;
				var tempCount;
				var idStr = '';
				Ext.Ajax.request({
					url : basepath+ '/attrDefiniton/'+ id+ '/batchDestroy.json?idStr='+ id,
					waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
					success : function() {
						Ext.Msg.alert('提示', '操作成功!' );
						reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败!' );
						reloadCurrentData();
					}
				});
			});
		}
	}
}];
/***
 * 方法：在面板滑入前进行逻辑操作
 * @param view
 * @return
 */
var beforeviewshow = function(view){
	if(view._defaultTitle!='新增'){//判断是否选择一条数据
		if(!getSelectedData()){
			Ext.Msg.alert('提示信息','请选择一条数据后操作！');  
			return false;
		}
	}
};
/***
 * 方法：在查询条件提交前进行名称的替换以便与model对应
 * @param params
 * @param forceLoad
 * @param add
 * @param transType
 * @return
 */
var beforesetsearchparams = function(params,forceLoad,add,transType){
	/*if(params.NAME){//替换查询条件传的参数名
		params.F_NAME = params.NAME;
		delete params.NAME;
	}
	if(params.COMMENT){
		params.F_COMMENT = params.COMMENT;
		delete params.COMMENT;
	}*/
};