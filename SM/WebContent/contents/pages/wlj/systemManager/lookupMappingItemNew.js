/**
 * 公共参数管理-数据字典管理
 * @author ZHANGHAO
 * @since 2014-4-29
 */
var createView = true;
var editView = true;
var detailView = true;
var lookupTypes = [{//自定义数据字典种类ID下拉框
	TYPE : 'LOOKUP_ID',
	url : '/lookupMappingQuery.json',
	key : 'F_NAME',
	value : 'F_NAME',
	jsonRoot : 'json.data'
}];
var url = basepath+'/lookupMappingItemQuery.json';
var comitUrl = basepath+'/lookupMapping-item.json';
var fields = [
    {name: 'ID',mapping:'F_ID',text : '属性ID'},
    {name: 'LOOKUP',mapping:'F_LOOKUP_ID',text:'字典类别', searchField: true,translateType:'LOOKUP_ID',editable:true, allowBlank:false,resutlWidth:150},
    {name: 'CODE',mapping:'F_CODE',text:'属性编码', searchField: true,allowBlank:false, resutlWidth:100}, 
    {name: 'VALUE', mapping:'F_VALUE',text : '属性名称', searchField: true,allowBlank:false,resutlWidth:200},  
    {name: 'COMMENT',mapping:'F_COMMENT',text:'属性说明',  resutlWidth:350,allowBlank:false}                              
    			   
];
var formViewers = [{
	columnCount: 1,
	fields : ['ID','LOOKUP','CODE','VALUE','COMMENT'],
	fn : function(ID,LOOKUP,CODE,VALUE,COMMENT){
		ID.hidden = true;
		return [ID,LOOKUP,CODE,VALUE,COMMENT];
	}
}];
var detailFormViewer = [{
	columnCount: 1,
	fields : ['ID','LOOKUP','CODE','VALUE','COMMENT'],
	fn : function(ID,LOOKUP,CODE,VALUE,COMMENT){
		ID.hidden = true;
		LOOKUP.allowBlank = true;
		CODE.allowBlank = true;
		VALUE.allowBlank = true;
		COMMENT.allowBlank = true;
		
		LOOKUP.disabled = true;
		CODE.disabled = true;
		VALUE.disabled = true;
		COMMENT.disabled = true;
		
		LOOKUP.cls = "x-readOnly";
		CODE.cls = "x-readOnly";
		VALUE.cls = "x-readOnly";
		COMMENT.cls = "x-readOnly";
		return [ID,LOOKUP,CODE,VALUE,COMMENT];
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
					url : basepath+ '/lookupMapping-item/'+ id+ '/batchDestroy.json?idStr='+ id,
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
		if(!getSelectedData()||getAllSelects().length>1){
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
	if(params.ID){//替换查询条件传的参数名
		params.F_ID = params.ID;
		delete params.ID;
	}
	if(params.VALUE){
		params.F_VALUE = params.VALUE;
		delete params.VALUE;
	}
	if(params.CODE){
		params.F_CODE = params.CODE;
		delete params.CODE;
	}
	if(params.COMMENT){
		params.F_COMMENT = params.COMMENT;
		delete params.COMMENT;
	}
	if(params.LOOKUP){
		params.F_LOOKUP_ID = params.LOOKUP;
		delete params.LOOKUP;
	}
};