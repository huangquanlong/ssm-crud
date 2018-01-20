/***
 * 日志管理
 * @author CHANGZH
 * @date 2014-04-28
 */
/**引入公共JS文件*/
imports(['/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js',
         '/contents/pages/common/Com.yucheng.crm.common.ImpExpNew.js'
]);
var detailView = false;
var url = basepath+'/AdminLogQuery.json';
//远程数据字典定义
var lookupTypes = [{
	TYPE : 'LOG_TYPE',
	url : '/AdminLogType.json',
	key : 'key',
	value : 'value',
	root : 'JSON'
}];
//本地数据字典定义
var localLookup = {
	'TIME_SORT' : [
		{key : '1',value : '时间顺序'},
	    {key : '2',value : '时间逆序'}
	]
};
var fields = [
	  {name: 'ID',hidden : true},
	  {name: 'LOG_TYPE', text : '日志类型', searchField: true, translateType:'LOG_TYPE', 
	   viewFn : function(data){return '<b>'+data+'</b>';}
	  },
	  {name: 'APP_ID',hidden : true},
	  {name: 'USERNAME', text : '操作用户', searchField: true},                                   
	  {name: 'CONTENT',text:'操作信息', searchField: false,  resutlWidth:200},                                   
	  {name: 'AFTER_VALUE',text:'参数', searchField: false, resutlWidth:250},                                   
	  {name: 'LOGIN_IP',text:'登陆IP地址', searchField: false, resutlWidth:150},			   
	  {name: 'OPER_TIME',text:'操作时间', xtype:'datefield', format:'Y-m-d', resutlWidth:120}
	  
	  //{name: 'START_TIME',text:'开始时间', searchField: true,hidden:true, xtype:'datefield', format:'Y-m-d', resutlWidth:120},
	 //{name: 'END_TIME',text:'结束时间', searchField: true,hidden:true, xtype:'datefield', format:'Y-m-d', resutlWidth:120}
	];
/**
 * 导出按钮
 * 删除按钮
 */
var tbar = [
	new Com.yucheng.crm.common.NewExpButton({ //导出按钮
	    formPanel : 'searchCondition',
	    url : basepath + '/AdminLogQuery.json'
	})
,'-', {
	/**
	 * 日志删除
	 */
	text : '删除',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选中要删除的日志记录！');
			return false;
		}else{
			var logId = getSelectedData().data.ID;
			// 支持批量删除
			var allIds = "";
			var allSec = getAllSelects();
			for (var i = 0; i < allSec.length; i++) {
				var id = allSec[i].data.ID;// 日志记录ID
				allIds += (id + ",");
			}
			if (allIds != ""
					&& allIds.substring(allIds.length - 1) == ",") {
				allIds = allIds.substring(0, allIds.length - 1);
			}
			Ext.MessageBox.confirm('提示','确定删除吗该日志吗?',function(buttonId){
				if(buttonId.toLowerCase() == "no"){
					return;
				}  
        		Ext.Ajax.request({
	    			url : basepath+'/AdminLogManagerAction/'+logId+'.json?idStr='+allIds,
	    			method : 'DELETE',        
	    			waitMsg : '正在删除数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
	    			success : function() {
	    				Ext.Msg.alert('提示', '操作成功!');
	    				reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败!');
					}
        		});
			});
		}
	}
}];
