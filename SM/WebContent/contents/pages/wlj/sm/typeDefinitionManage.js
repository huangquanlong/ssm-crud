/**
 * 配置管理-属性定义管理
 * 
 * @author DINGLEIXING
 * @since 2017-2-15
 */
// 引入公共js文件
imports([ '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js',
		'/contents/pages/common/Com.yucheng.crm.common.OrgUserManage.js' ]);

var createView = true;
var editView = true;
var detailView = true;
var localLookup = {
	'TYPE_STATE' : [ {
		key : '0',
		value : '启用'
	}, {
		key : '1',
		value : '删除'
	} ]
};
var url = basepath + '/typeDefinitionQuery.json';
var comitUrl = basepath + '/typeDefinition.json';
var fields = [ {
	name : 'ID',
	text : '私募类型ID'
}, {
	name : 'NAME',
	text : '私募类型名称',
	resutlWidth : 150,
	allowBlank : false,
	searchField : true
}, {
	name : 'COMM',
	text : '私募类型描述',
	searchField : false,
	allowBlank : false,
	resutlWidth : 150
}, {
	name : 'DISPLAY_NAME',
	text : '显示名称',
	searchField : true,
	allowBlank : false,
	resutlWidth : 150
}, {
	name : 'STATE',
	text : '类型状态',
	searchField : true,
	allowBlank : false,
	resutlWidth : 150,
	translateType : 'TYPE_STATE'
}, {
	name : 'IDENTIFY',
	text : '逻辑标识',
	allowBlank : false,
	resutlWidth : 100
} ];

var formViewers = [ {
	columnCount : 1,
	fields : [ 'ID', 'NAME', 'COMM', 'DISPLAY_NAME', 'STATE', 'IDENTIFY' ],
	fn : function(ID, NAME, COMM, DISPLAY_NAME, STATE, IDENTIFY) {
		ID.xtype = 'hidden';
		return [ ID, NAME, COMM, DISPLAY_NAME, STATE, IDENTIFY ];
	}
} ];
var detailFormViewer = [ {
	columnCount : 1,
	fields : [ 'ID', 'NAME', 'COMM', 'DISPLAY_NAME', 'STATE', 'IDENTIFY' ],
	fn : function(ID, NAME, COMM, DISPLAYNAME, STATE, IDENTIFY) {
		ID.xtype = 'hidden';
		NAME.allowBlank = true;
		NAME.disabled = true;
		NAME.cls = 'x-readOnly';
		DISPLAYNAME.allowBlank = true;
		DISPLAYNAME.disabled = true;
		DISPLAYNAME.cls = 'x-readOnly';
		COMM.allowBlank = true;
		COMM.disabled = true;
		COMM.cls = 'x-readOnly';
		STATE.allowBlank = true;
		STATE.disabled = true;
		STATE.cls = 'x-readOnly';
		IDENTIFY.allowBlank = true;
		IDENTIFY.disabled = true;
		IDENTIFY.cls = 'x-readOnly';
		return [ ID, NAME, COMM, DISPLAYNAME, STATE, IDENTIFY ];
	}
} ];

var tbar = [ {
	text : '删除',
	handler : function() {
		if (getSelectedData() == false) {
			Ext.Msg.alert('提示', '请选择一条数据！');
			return false;
		} else {
			var id = getSelectedData().data.ID;
			Ext.MessageBox.confirm('提示', '确定删除吗?', function(buttonId) {
				if (buttonId.toLowerCase() == "no") {
					return;
				}
				var selectRe;
				var tempId;
				var tempCount;
				var idStr = '';
				Ext.Ajax.request({
					url : basepath + '/typeDefinition/' + id
							+ '/batchDestroy.json?idStr=' + id,
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
} ];
/*******************************************************************************
 * 自定义滑动面板
 */
var customerView = [{
	title : '扩展属性',
	url : basepath + '/typeWarrantDefinitionQuery.json',
	type : 'grid',
	pageable : true,
	frame : true,
	buttonAlign: 'center',
	fields : {
		fields : [
		        {name : 'ID'},
		        {name : 'NAME',text:'属性名称'},
				{name : 'COMM',text : '属性描述'}],
				fn : function(ID, NAME,COMM){
					return [ID, NAME,COMM];
			}
			},
	gridButtons : [{
		text:'新增属性',
		fn : function(grid){
				showCustomerViewByTitle('新增属性面板');	
			}
		},{
			text:'取消属性',
			fn : function(grid){
				if (grid.selModel.hasSelection()) {//取消授权
					Ext.MessageBox.confirm('系统提示信息','确定要对所选的用户取消属性么?',
						function(buttonobj) {
							if (buttonobj == 'yes') {
								var records = grid.selModel.getSelections();// 得到被选择的行的数组
								var selectLength = records.length;// 得到行数组的长度
								var idStr = '';//用于存储将删除角色的id
								var tempId;
								for ( var i = 0; i < selectLength; i++) {
									selectRe = records[i].data.ID;
									tempId = selectRe;
									idStr += tempId;
									if (i != selectLength - 1){
										idStr += ',';
									}
								};	
								Ext.Ajax.request({
									url :basepath+'/SmTypeAtrrGrant-action!batchDestroy.json',//删除请求url
									params : {
										idStr : idStr//传递参数，将删除角色id
									},
									waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
									method : 'POST',
									scope : this,
									success : function() {
										Ext.Msg.alert('提示', '操作成功');
										grid.store.load({
											params : {
							 					roleId : roleIdgrant
							 				}
							 			});
									}
								});
							}
						}
					);
				} else {
					Ext.Msg.alert("提示", "请先选择要取消授权的用户的行!");
					}
				}
			}]
},{
	title : '新增属性',
	type : 'form',
	hideTitle : true,
	autoLoadSeleted : true,
	groups : [{
		columnCount : 1 ,
		fields : [{name : 'NAME',text : '属性 ',xtype:'userchoose',		
					callback:function(a){
									istr = '';
									var tempid = '';
										for ( var i = 0; i < a.length; i++) {
											seletId = a[i].data.id;
											tempid = seletId;
											istr += tempid;
											if (i != a.length - 1){
												istr += ',';
											}
										};	
								}
					}],
		fn : function(NAME){
			return [NAME];
		}
	}],
	formButtons : [{
		text:'确定',
		fn : function(formPanel,basicForm){
			if(!basicForm.isValid()){ //输入项检查
				Ext.MessageBox.alert('提示','输入有误或存在漏输项,请检查！');
				return false;
			}
			var str = istr.split(","); 
			for(i=0;i<str.length;i++){
				for(j=1;j<str.length-1;j++){
					if(str[i]== str[j]){
						Ext.MessageBox.alert('提示','您所选择的用户重复,请检查！');
						return false;
					}
					break;
				}
		}
			Ext.Ajax.request( {
				url : basepath + '/SmTypeAtrrGrant-action!create.json',
				method : 'POST',
				params : {
					'typeId' : roleIdgrant,
					'AttrIds':istr
			},
				success : checkResult,
				failure: checkResult
			});
			function checkResult(response) {
				var resultArray = Ext.util.JSON.decode(response.status);
				var resultError = response.responseText;
				if ((resultArray == 200 ||resultArray == 201)&&resultError=='') {
					Ext.Msg.alert('提示', '操作成功');
					showCustomerViewByTitle('授权用户查看');	
				} else {
					if(resultArray == 403){
						Ext.Msg.alert('提示', response.responseText);
					}else{
						Ext.Msg.alert('提示', '操作失败,失败原因:' + resultError);
					}
				};
		}				
}
		},{
		text : '取消',
		fn : function(formPanel,basicForm){
		 		showCustomerViewByTitle('授权用户查看');
		}
	}]	
} ];
/*******************************************************************************
 * 方法：在面板滑入前进行逻辑操作
 * 
 * @param view
 * @return
 */
var beforeviewshow = function(view) {
	if (view._defaultTitle != '新增') {
		if (!getSelectedData() || getAllSelects().length > 1) {// 判断是否选择一条数据
			Ext.Msg.alert('提示信息', '请选择一条数据后操作！');
			return false;
		} else if (view._defaultTitle == '扩展属性') {
			typeIdgrant = getSelectedData().data.ID;// 获得选中记录的id
			if (typeIdgrant != '') {
				view.setParameters({
					typeId : getSelectedData().data.ID
				});
			}
		}
	}
};

/*******************************************************************************
 * 方法：在查询条件提交前进行名称的替换以便与model对应
 * 
 * @param params
 * @param forceLoad
 * @param add
 * @param transType
 * @return
 */
var beforesetsearchparams = function(params, forceLoad, add, transType) {
	/*
	 * if(params.NAME){//替换查询条件传的参数名 params.F_NAME = params.NAME; delete
	 * params.NAME; } if(params.COMMENT){ params.F_COMMENT = params.COMMENT;
	 * delete params.COMMENT; }
	 */
};