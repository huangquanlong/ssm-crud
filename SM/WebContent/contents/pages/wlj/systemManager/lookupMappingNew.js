/**
 * 公共参数管理-数据字典管理
 * @author ZHANGHAO
 * @since 2014-4-29
 */	
var createView = true;
var editView = true;
var detailView = true;
var url = basepath+'/lookupMappingQuery.json';
var comitUrl = basepath+'/lookup-mapping.json';
var fields = [
    {name: 'ID',mapping:'F_ID',text:'字典ID'},
    {name: 'NAME',mapping:'F_NAME', text : '字典名称', resutlWidth:250,allowBlank:false,searchField: true},                                   
    {name: 'COMMENT',mapping:'F_COMMENT',text:'中文备注', searchField: true,allowBlank:false, resutlWidth:350}		   
];

var formViewers = [{
	columnCount: 1,
	fields : ['ID','NAME','COMMENT'],
	fn : function(ID,NAME,COMMENT){
			ID.xtype = 'hidden';
		return [ID,NAME,COMMENT];
	}
}];
var detailFormViewer = [{
	columnCount: 1,
	fields : ['ID','NAME','COMMENT'],
	fn : function(ID,NAME,COMMENT){
		ID.xtype = 'hidden';
		NAME.allowBlank = true;
		NAME.disabled = true;
		NAME.cls = 'x-readOnly';
		COMMENT.allowBlank = true;
		COMMENT.disabled = true;
		COMMENT.cls = 'x-readOnly';
		return [ID,NAME,COMMENT];
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
					url : basepath+ '/lookup-mapping/'+ id+ '/batchDestroy.json?idStr='+ id,
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
	if(params.NAME){//替换查询条件传的参数名
		params.F_NAME = params.NAME;
		delete params.NAME;
	}
	if(params.COMMENT){
		params.F_COMMENT = params.COMMENT;
		delete params.COMMENT;
	}
};