/***
 * 公共参数管理
 * @author CHANGZH
 * @date 2014-04-28
 */

var createView = true;
var editView = true;
var detailView = true;
		
var url = basepath+'/pubParamManagerAction.json';
var comitUrl = basepath+'/pubParamManagerAction!saveData.json';

var fields = [
  		    {name: 'ID',hidden : true},
  		    {name: 'APP_ID',hidden : true},
  		    {name: 'PROP_NAME', text : '参数名称',allowBlank: false, searchField: true},                                   
  		    {name: 'PROP_DESC',text:'参数说明', searchField: true, allowBlank: false, resutlWidth:250},                                   
  		    {name: 'PROP_VALUE',text:'参数值', searchField: false, allowBlank: false, resutlWidth:250},                                   
  		    {name: 'REMARK',text:'参数备注', searchField: false, allowBlank: true, resutlWidth:350}			   
  		];

/**新增、修改、详情设计**/
var formViewers = [{
	fields : ['APP_ID','PROP_NAME','PROP_DESC','PROP_VALUE'],
	fn : function(APP_ID,PROP_NAME,PROP_DESC,PROP_VALUE){
		return [PROP_NAME,PROP_DESC,PROP_VALUE];
	}
},{
	columnCount : 1,
	fields : ['REMARK'],
	fn : function(REMARK){
		REMARK.xtype = 'textarea';
		return [REMARK];
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
					url : basepath
							+ '/pubParamManagerAction!batchDestroy.json?idStr='
							+ id,
					waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
					success : function() {
					Ext.Msg.alert('提示', '操作成功!');
					  reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败!');
						  reloadCurrentData();
					}
				});

			});
		}
	}
}];
/**
 * 
 * @param view
 * @return
 */
var beforeviewshow = function(view){
	if(view._defaultTitle!='新增'){
		if(!getSelectedData()||getAllSelects().length>1){
			Ext.Msg.alert('提示信息','请选中一条参数记录！');  
			return false;
		}
	}
};