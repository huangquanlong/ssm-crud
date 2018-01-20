/**
 * 配置管理-属性类别管理
 * @author DINGLEIXING
 * @since 2017-2-14
 */	
var createView = true;
var editView = true;
var detailView = true;
var url = basepath+'/attrClassifyQuery.json';
var comitUrl = basepath+'/attrClassify.json';
var fields = [
    {name: 'ID',mapping:'ID',text:'属性类别ID'},
    {name: 'NAME',mapping:'NAME', text : '属性类别名称', resutlWidth:250,allowBlank:false,searchField: true},                                   
    {name: 'COMM',mapping:'COMM',text:'属性类别描述', searchField: true,allowBlank:false, resutlWidth:350}		   
];

var formViewers = [{
	columnCount: 1,
	fields : ['ID','NAME','COMM'],
	fn : function(ID,NAME,COMM){
			ID.xtype = 'hidden';
		return [ID,NAME,COMM];
	}
}];
var detailFormViewer = [{
	columnCount: 1,
	fields : ['ID','NAME','COMM'],
	fn : function(ID,NAME,COMM){
		ID.xtype = 'hidden';
		NAME.allowBlank = true;
		NAME.disabled = true;
		NAME.cls = 'x-readOnly';
		COMM.allowBlank = true;
		COMM.disabled = true;
		COMM.cls = 'x-readOnly';
		return [ID,NAME,COMM];
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
					url : basepath+ '/attrClassify/'+ id+ '/batchDestroy.json?idStr='+ id,
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