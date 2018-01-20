/**
 * @description 银行信息管理
 * @author wangmk1
 * @since 2015-6-9
 */
 
Ext.QuickTips.init();//提示信息
var lookupTypes=[
	'BANK_LEVEL',
	'BANK_TYPE'
];
var url = basepath + '/bankInfo.json';
var createView = true;
var editView = true;
var detailView = true;
var comitUrl = basepath + '/bankInfo.json';
var fields = [
 	{name: 'ID',hidden: true},
    {name: 'BANK_NO',text: '银行编号',  searchField: true, allowBlank: false},
	{name: 'BANK_NAME', text: '银行名称', searchField: true, allowBlank: false},
	{name: 'BANK_EN_ABBREVIATION', text: '英文简称', searchField: false, allowBlank: true},
	{name: 'BANK_TYPE', text: '银行类型',translateType:'BANK_TYPE', searchField: false, allowBlank: true},
	{name: 'BANK_LEVEL',text: '银行级别',translateType:'BANK_LEVEL',  searchField: false, allowBlank: true},
	{name: 'HEAD_OFFICE_POSITION', text: '总行位置', searchField: false, width:200,allowBlank: true}
];
//新增面板
var createFormViewer = [{
	fields : ['BANK_NO','BANK_NAME','BANK_EN_ABBREVIATION','BANK_TYPE','BANK_LEVEL'],
	fn : function(BANK_NO,BANK_NAME,BANK_EN_ABBREVIATION,BANK_TYPE,BANK_LEVEL){
			return [BANK_NO,BANK_NAME,BANK_EN_ABBREVIATION,BANK_TYPE,BANK_LEVEL];
	}
},{/**总行地址信息**/
	columnCount:1,
	fields:['HEAD_OFFICE_POSITION'],
	fn:function(HEAD_OFFICE_POSITION){
		return [HEAD_OFFICE_POSITION];
	}
}];
//修改面板
var editFormViewer = [{
	fields : ['BANK_NO','BANK_NAME','BANK_EN_ABBREVIATION','BANK_TYPE','BANK_LEVEL'],
	fn : function(BANK_NO,BANK_NAME,BANK_EN_ABBREVIATION,BANK_TYPE,BANK_LEVEL){
			return [BANK_NO,BANK_NAME,BANK_EN_ABBREVIATION,BANK_TYPE,BANK_LEVEL];
	}
},{/**总行地址信息**/
	columnCount:1,
	fields:['HEAD_OFFICE_POSITION'],
	fn:function(HEAD_OFFICE_POSITION){
		return [HEAD_OFFICE_POSITION];
	}
}];
//详情面板
var detailFormViewer = [{
	fields : ['BANK_NO','BANK_NAME','BANK_EN_ABBREVIATION','BANK_TYPE','BANK_LEVEL'],
	fn : function(BANK_NO,BANK_NAME,BANK_EN_ABBREVIATION,BANK_TYPE,BANK_LEVEL){
			for(var i in arguments){
				arguments[i].readOnly=true;
				arguments[i].cls = 'x-readOnly';
			}
			return [BANK_NO,BANK_NAME,BANK_EN_ABBREVIATION,BANK_TYPE,BANK_LEVEL];
	}
},{/**总行地址信息**/
	columnCount:1,
	fields:['HEAD_OFFICE_POSITION'],
	fn:function(HEAD_OFFICE_POSITION){
		HEAD_OFFICE_POSITION.readOnly=true;
		HEAD_OFFICE_POSITION.cls='x-readOnly';
		return [HEAD_OFFICE_POSITION];
	}
}];
var tbar =[{
	text:'删除',
	handler:function(){
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
					url: basepath + '/bankInfo!batchDelete.json',
					waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
	                params: {
                        'idStr': id
                    },
					success : function() {
                        Ext.Msg.alert('提示', '删除成功');
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
}]
var beforeviewshow=function(view){
	if(view == getDetailView()||view == getEditView()){
		if(getSelectedData() == false || getAllSelects().length > 1){
			Ext.Msg.alert('提示','请选择一条数据');
			return false;
		}
	}
}