/**
 * @description 数据集关联设置
 * @author helin
 * @since 2014-06-26
 */

Ext.QuickTips.init();

//本地数据字典定义
var localLookup = {
	'JOIN_TYPE' : [
		{key : 'inner',value : '内关联'},
	    {key : 'left',value : '左关联'},
	    {key : 'right',value : '右关联'}
	]
};

//远程数据字典
var lookupTypes = [{
	//左关联表
	TYPE : 'JOIN_LEFT_TABLE',
	url : '/queryJoinLeftTable.json?type=1',
	key : 'VALUE',
	value : 'NAME',
	root : 'json.data'
},{
	//右关联表
	TYPE : 'JOIN_RIGHT_TABLE',
	url : '/queryJoinLeftTable.json?type=2',
	key : 'VALUE',
	value : 'NAME',
	root : 'json.data'
},{
	//左关联列-属于级联数据字典,根据左关联表动态加载
	TYPE : 'JOIN_LEFT_COL',
	url : '/queryJoinLeftCol.json',
	key : 'VALUE',
	value : 'NAME',
	root : 'json.data'
},{
	//右关联列-属于级联数据字典,根据右关联表动态加载
	TYPE : 'JOIN_RIGHT_COL',
	url : '/queryJoinLeftCol.json',
	key : 'VALUE',
	value : 'NAME',
	root : 'json.data'
}];

var url = basepath+'/setDateRelationQuery-action.json';
var comitUrl = basepath+'/dataSetRelation-action.json';

var fields = [
    {name: 'ID',hidden : true},
    {name: 'JOIN_LEFT_TABLE',text: '关联左表表名',translateType:'JOIN_LEFT_TABLE',hidden : true,resizable : true,allowBlank:false
		,listeners:{
			select:function(combo,record){
				getCurrentView().contentPanel.getForm().findField('JOIN_LEFT_COL').setValue('');
				reloadLookup('JOIN_LEFT_COL',{
					params:{
						joinLeftCols:record.data.key
					},
					callback:function(){
						getCurrentView().contentPanel.getForm().findField('JOIN_LEFT_COL').bindStore(findLookupByType('JOIN_LEFT_COL'));
					}
				});
			}
		}
	},
    {name: 'JOIN_LEFT_NAME',text:'关联左表表名', searchField: true,allowBlank:false},
    {name: 'JOIN_LEFT_TABLE_NAME',text:'左表备注',resutlWidth:150},
    {name: 'JOIN_RIGHT_TABLE',text:'关联右表表名',translateType:'JOIN_RIGHT_TABLE',hidden : true,resizable : true,allowBlank:false
		,listeners:{
			select:function(combo,record){
				getCurrentView().contentPanel.getForm().findField('JOIN_RIGHT_COL').setValue('');
				reloadLookup('JOIN_RIGHT_COL',{
					params:{
						joinLeftCols:record.data.key
					},
					callback:function(){
						getCurrentView().contentPanel.getForm().findField('JOIN_RIGHT_COL').bindStore(findLookupByType('JOIN_RIGHT_COL'));
					}
				});
			}
		}
	},
    {name: 'JOIN_RIGHT_NAME',text:'关联右表表名', searchField: true,allowBlank:false},
    {name: 'JOIN_RIGHT_TABLE_NAME',text:'右表备注',resutlWidth:150},
    {name: 'SS_COL_LEFT',text:'关联方式',resutlWidth:80,allowBlank:false},
    {name: 'JOIN_LEFT_ALIAS',text:'左表别名',resutlWidth:80,allowBlank:false},
    {name: 'JOIN_RIGHT_ALIAS',text:'右表别名',resutlWidth:80,hidden:true},
    {name: 'JOIN_LEFT_COL',text:'左表关联字段',translateType:'JOIN_LEFT_COL',hidden : true,resizable : true,allowBlank:false},
    {name: 'JOIN_LEFT_COL_NAME',text:'左表关联字段',resutlWidth:100,allowBlank:false},
    {name: 'JOIN_LEFT_COL_REMARK',text:'左表关联字段备注'},
    {name: 'JOIN_RIGHT_COL',text:'右表关联字段',translateType:'JOIN_RIGHT_COL',hidden : true,editable:true,resizable : true,allowBlank:false},
    {name: 'JOIN_RIGHT_COL_NAME',text:'右表关联字段',resutlWidth:100,allowBlank:false},
    {name: 'JOIN_RIGHT_COL_REMARK',text:'右表关联字段备注'}
];

var createView = true;
var editView = true;
var detailView = true;

/**
 * 新增、修改、详情设计
 * @type 
 */
var formViewers = [{
	columnCount : 2,
	fields : ['ID','JOIN_LEFT_TABLE','JOIN_RIGHT_TABLE','JOIN_LEFT_COL','JOIN_RIGHT_COL','JOIN_LEFT_ALIAS','SS_COL_LEFT'],
	fn : function(ID,JOIN_LEFT_TABLE,JOIN_RIGHT_TABLE,JOIN_LEFT_COL,JOIN_RIGHT_COL,JOIN_LEFT_ALIAS,SS_COL_LEFT){
		JOIN_LEFT_TABLE.hidden = false;
		JOIN_RIGHT_TABLE.hidden = false;
		JOIN_LEFT_COL.hidden = false;
		JOIN_RIGHT_COL.hidden = false;
		return [ID,JOIN_LEFT_TABLE,JOIN_RIGHT_TABLE,JOIN_LEFT_COL,JOIN_RIGHT_COL,JOIN_LEFT_ALIAS,SS_COL_LEFT];
	}
}];

var detailFormViewer = [{
	columnCount : 2,
	fields : ['ID','JOIN_LEFT_TABLE','JOIN_RIGHT_TABLE','JOIN_LEFT_COL','JOIN_RIGHT_COL','JOIN_LEFT_ALIAS','SS_COL_LEFT'],
	fn : function(ID,JOIN_LEFT_TABLE,JOIN_RIGHT_TABLE,JOIN_LEFT_COL,JOIN_RIGHT_COL,JOIN_LEFT_ALIAS,SS_COL_LEFT){
		JOIN_LEFT_TABLE.hidden = false;
		JOIN_LEFT_TABLE.allowBlank = true;
		JOIN_LEFT_TABLE.disabled = true;
		JOIN_LEFT_TABLE.cls = "x-readOnly";
		
		JOIN_RIGHT_TABLE.hidden = false;
		JOIN_RIGHT_TABLE.allowBlank = true;
		JOIN_RIGHT_TABLE.disabled = true;
		JOIN_RIGHT_TABLE.cls = "x-readOnly";
		
		JOIN_LEFT_COL.hidden = false;
		JOIN_LEFT_COL.allowBlank = true;
		JOIN_LEFT_COL.disabled = true;
		JOIN_LEFT_COL.cls = "x-readOnly";
		
		JOIN_RIGHT_COL.hidden = false;
		JOIN_RIGHT_COL.allowBlank = true;
		JOIN_RIGHT_COL.disabled = true;
		JOIN_RIGHT_COL.cls = "x-readOnly";
		
		JOIN_LEFT_ALIAS.allowBlank = true;
		JOIN_LEFT_ALIAS.disabled = true;
		JOIN_LEFT_ALIAS.cls = "x-readOnly";
		
		SS_COL_LEFT.allowBlank = true;
		SS_COL_LEFT.disabled = true;
		SS_COL_LEFT.cls = "x-readOnly";
		
		return [ID,JOIN_LEFT_TABLE,JOIN_RIGHT_TABLE,JOIN_LEFT_COL,JOIN_RIGHT_COL,JOIN_LEFT_ALIAS,SS_COL_LEFT];
	}
}];

/**
 * 数据联动，当数据发生变化，且失去焦点的时候，会调用该联动逻辑
 * 由于下拉框级联联动会有问题，故改用select事件
 */
//var linkages = {
//	JOIN_LEFT_TABLE : {
//		fields : ['JOIN_LEFT_COL'],
//		fn : function(JOIN_LEFT_TABLE,JOIN_LEFT_COL){
//			//helin,20140626,由于目前联动时暂不能获取到hiddenName对应的值，故暂时作一处理
//			var record = JOIN_LEFT_TABLE.findRecord(JOIN_LEFT_TABLE.displayField, JOIN_LEFT_TABLE.value); 
//			var tableId = record ? record.get(JOIN_LEFT_TABLE.valueField) : -1;
//			reloadLookup('JOIN_LEFT_COL',{
//				params:{
//					joinLeftCols:tableId
//				},
//				callback:function(){
//					theview.contentPanel.getForm().findField('JOIN_LEFT_COL').setValue(JOIN_LEFT_COL.value);
//				}
//			});
//		}
//	},
//	JOIN_RIGHT_TABLE : {
//		fields : ['JOIN_RIGHT_COL'],
//		fn : function(JOIN_RIGHT_TABLE,JOIN_RIGHT_COL){
//			//helin,20140626,由于目前联动时暂不能获取到hiddenName对应的值，故暂时作一处理
//			var record = JOIN_RIGHT_TABLE.findRecord(JOIN_RIGHT_TABLE.displayField, JOIN_RIGHT_TABLE.value); 
//			var tableId = record ? record.get(JOIN_RIGHT_TABLE.valueField) : -1;
//			reloadLookup('JOIN_RIGHT_COL',{
//				params:{
//					joinLeftCols:tableId
//				},
//				callback:function(){
//					theview.contentPanel.getForm().findField('JOIN_RIGHT_COL').setValue(JOIN_RIGHT_COL.value);
//				}
//			});
//		}
//	}
//};

var tbar =[{
	/**
	 * 数据集关联删除
	 */
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
				Ext.Ajax.request({
					url: basepath+'/dataSetRelation-action!batchDestroy.json',
					waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
	                params: {
                        'idStr': id
                    },
					success : function() {
                        Ext.Msg.alert('提示', '删除成功' );
						reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '删除失败' );
						reloadCurrentData();
					}
				});
			});
		}
	}
}];


/**
 * 结果域面板滑入前触发：
 * params：theview : 当前滑入面板；
 * return： false，阻止面板滑入操作；默认为true；
 */
var beforeviewshow = function(theview){
	if(theview.baseType !== 'createView'){
		if(getSelectedData()){
			/**
			 * 点击修改或详情时，reload一下相应的列数据字典
			 */
			var tempData = getSelectedData().data;
			reloadLookup('JOIN_LEFT_COL',{
				params:{
					joinLeftCols: tempData.JOIN_LEFT_TABLE
				},
				callback:function(){
					theview.contentPanel.getForm().findField('JOIN_LEFT_COL').setValue(tempData.JOIN_LEFT_COL);
				}
			});
			reloadLookup('JOIN_RIGHT_COL',{
				params:{
					joinLeftCols: tempData.JOIN_RIGHT_TABLE
				},
				callback:function(){
					theview.contentPanel.getForm().findField('JOIN_RIGHT_COL').setValue(tempData.JOIN_RIGHT_COL);
				}
			});
		}
	}
};