/**
 * 数据集权限过滤器配置
 * @author shiyang
 * @since 2014-04-29
 * @since 2014-07-20 helin3按最新的改造
 */
Ext.QuickTips.init();

var needCondition = false;

var url = basepath+'/dataGrantMapQueryAction.json';
var comitUrl = basepath+'/dataGrantMapAction.json';

var fields = [
    {name: 'ID',hidden : true},
    {name: 'FUNCTION_ID',hidden : true},                                   
    {name: 'CLASS_NAME',text:'查询类名称', resutlWidth:250,allowBlank:false},
    {name: 'CLASS_DESC',text:'查询类描述', resutlWidth:250,allowBlank:false}		   
];

var createView = true;
var editView = true;
var detailView = true;

		
/**新增、修改、详情设计**/
var formViewers = [{
	columnCount : 1 ,
	fields : ['CLASS_NAME','CLASS_DESC'],
	fn : function(CLASS_NAME,CLASS_DESC){
		return [CLASS_NAME,CLASS_DESC];
	}
}];

var detailFormViewer = [{
	columnCount : 1 ,
	fields : ['CLASS_NAME','CLASS_DESC'],
	fn : function(CLASS_NAME,CLASS_DESC){
		CLASS_NAME.disabled = true;
		CLASS_NAME.cls = "x-readOnly";
		CLASS_NAME.allowBlank = true;
		
		CLASS_DESC.disabled = true;
		CLASS_DESC.cls = "x-readOnly";
		CLASS_DESC.allowBlank = true;
		return [CLASS_NAME,CLASS_DESC];
	}
}];

var customerView =[{
	title : '过滤器配置',
	type : 'grid',
	url : basepath + '/dataGrantFilterQueryAction.json',
	pageable : true,
	fields: {
		fields : [
			{name: 'ID',hidden: true},
			{name: 'ROLE_ID',hidden: true},
			{name: 'METHOD_NAME',hidden: true},
			{name: 'CLASS_NAME',text: '类名称',width: 200},
			{name: 'SQL_STRING',text: '过滤SQL',width: 300},
			{name: 'DESCRIBETION',text: 'SQL描述',width: 200}
		]
	},
	gridButtons:[{
		/**
		 * 新增过滤器配置项
		 */
		text : '新增',
		fn : function(grid){
			showCustomerViewByTitle('新增过滤器配置项');
		}
	},{
		/**
		 * 修改过滤器配置项
		 */
		text : '修改',
		fn : function(grid){
			var selectLength = grid.getSelectionModel().getSelections().length;
            var selectRecord = grid.getSelectionModel().getSelections()[0];
            if(selectLength != 1){
                Ext.Msg.alert('提示','请选择一条数据进行操作!');
                return false;
            }
			showCustomerViewByTitle('修改过滤器配置项');
		}
	},{
		/**
		 * 删除过滤器配置项
		 */
		text : '删除',
		fn : function(grid){
			var selectLength = grid.getSelectionModel().getSelections().length;
            var selectRecords = grid.getSelectionModel().getSelections();
            if(selectLength < 1){
                Ext.Msg.alert('提示','请选择一条数据进行操作!');
                return false;
            }
            var idStr = '';
            for(var i=0; i < selectLength; i++){
                var selectRecord = selectRecords[i];
                idStr +=  selectRecord.data.ID;
                if( i != selectLength - 1){
                    idStr += ',';
                }
            }
            Ext.MessageBox.confirm('提示','你确定删除吗!',function(buttonId){
                if(buttonId.toLowerCase() == 'no'){
                    return false;
                }
                Ext.Ajax.request({
                    url : basepath+'/dataGrantFilterAction!delFun.json',
                    waitMsg: '正在删除，请稍等...',
					method : 'POST',
					params : {
						delId : idStr
					},
                    success: function(){
                        Ext.Msg.alert('提示','删除操作成功!');
                        grid.store.reload();
                    },
                    failure: function(){
                        Ext.Msg.alert('提示','删除操作失败!');
                    }
                });
            });
		}
	}]
},{
	/**
	 * 自定义新增过滤器配置项
	 */
	title:'新增过滤器配置项',
	type: 'form',
	hideTitle: true,
	groups:[{
		columnCount : 1,
		fields : [
			{name: 'ID',hidden: true},
			{name: 'ROLE_ID',hidden: true},
			{name: 'METHOD_NAME',hidden: true},
			{name: 'CLASS_NAME',text: '类名称',width:150,allowBlank: false,readOnly:true,cls:'x-readOnly'},
			{name: 'SQL_STRING',text: '过滤SQL',width:150,allowBlank: false},
			{name: 'DESCRIBETION',text: 'SQL描述',width:300,allowBlank: false}
		],
		/**
		 *新增过滤器配置项面板字段初始化处理
		 */
		fn : function(ID,ROLE_ID,METHOD_NAME,CLASS_NAME,SQL_STRING,DESCRIBETION){
			return [ID,ROLE_ID,METHOD_NAME,CLASS_NAME,SQL_STRING,DESCRIBETION];
		}
	}],
	formButtons:[{
		/**
		 * 新增过滤器配置项-保存按钮
		 */
		text : '保存',
		fn : function(contentPanel, baseform){
			if(!baseform.isValid()){
				Ext.Msg.alert('提示', '字段校验失败，请检查输入项!');
				return false;
			}
			var commintData = translateDataKey(baseform.getFieldValues(),_app.VIEWCOMMITTRANS);
            Ext.Ajax.request({
                url: basepath + '/dataGrantFilterAction.json',
                method: 'POST',
                params : commintData,
                success: function(response) {
                    Ext.Msg.alert('提示', '操作成功！');
                    showCustomerViewByTitle('过滤器配置');
                },
                failure: function(){
                	Ext.Msg.alert('提示', '操作失败！');
                }
            });
                    
		}
	},{
		/**
		 * 返回过滤器配置界面
		 */
		text: '返回',
		fn : function(contentPanel, baseform){
			showCustomerViewByTitle('过滤器配置');
		}
	}]
},{
	/**
	 * 自定义修改过滤器配置项面板
	 */
	title:'修改过滤器配置项',
	type: 'form',
	hideTitle: true,
	groups:[{
		columnCount : 1,
		fields : [
			{name: 'ID',hidden: true},
			{name: 'ROLE_ID',hidden: true},
			{name: 'METHOD_NAME',hidden: true},
			{name: 'CLASS_NAME',text: '类名称',allowBlank: false,readOnly:true,cls:'x-readOnly'},
			{name: 'SQL_STRING',text: '过滤SQL',allowBlank: false},
			{name: 'DESCRIBETION',text: 'SQL描述',allowBlank: false}
		],
		/**
		 *修改过滤器配置项面板 字段初始化处理
		 */
		fn : function(ID,ROLE_ID,METHOD_NAME,CLASS_NAME,SQL_STRING,DESCRIBETION){
			return [ID,ROLE_ID,METHOD_NAME,CLASS_NAME,SQL_STRING,DESCRIBETION];
		}
	}],
	formButtons:[{
		/**
		 * 修改过滤器配置项-保存按钮
		 */
		text : '保存',
		fn : function(contentPanel, baseform){
			if(!baseform.isValid()){
				Ext.Msg.alert('提示', '字段校验失败，请检查输入项!');
				return false;
			}
			var commintData = translateDataKey(baseform.getFieldValues(),_app.VIEWCOMMITTRANS);
            Ext.Ajax.request({
                url: basepath + '/dataGrantFilterAction.json',
                method: 'POST',
                params : commintData,
                success: function(response) {
                    Ext.Msg.alert('提示', '操作成功！');
                    showCustomerViewByTitle('过滤器配置');
                },
                failure: function(){
                	Ext.Msg.alert('提示', '操作失败！');
                }
            });
		}
	},{
		/**
		 * 返回过滤器配置界面
		 */
		text: '返回',
		fn : function(contentPanel, baseform){
			showCustomerViewByTitle('过滤器配置');
		}
	}]
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
					url : basepath+ '/dataGrantMapAction!delFun.json?delId='+ id,
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
		
var beforeviewshow = function(view){
	if(view._defaultTitle == '过滤器配置'){
		if(getSelectedData()){
			view.setParameters({
				selectName:getSelectedData().data.CLASS_NAME
			});
		    return true;
		}else{
			Ext.Msg.alert('提示','请选择一条数据进行操作！');
			return false;
		}
	}
	if(view._defaultTitle == '新增过滤器配置项'){
		var tempData = getSelectedData().data;
		view.contentPanel.getForm().reset();
		view.contentPanel.getForm().findField('CLASS_NAME').setValue(tempData.CLASS_NAME);
	}else if(view._defaultTitle == '修改过滤器配置项'){
		var tempGridView = getCustomerViewByTitle('过滤器配置');
		var tempRecord = tempGridView.grid.getSelectionModel().getSelections()[0];
		view.contentPanel.getForm().loadRecord(tempRecord);
	}
}
