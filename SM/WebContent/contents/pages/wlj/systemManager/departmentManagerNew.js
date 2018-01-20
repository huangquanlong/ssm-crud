/**
 * 部门管理模块
 * 
 * @author sniper
 * @since 2014-4-29
 */
imports( [
		'/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js',
		/**
		 * 未引用公共机构放大镜的原因：连动时事件setValue（）时强制触发，公共机构放大镜中是setRawValue（），无法触发,故在Com.yucheng.bcrm.common.OrgFieldx.js
		 * 中奖第300行setRawValue（）改为setValue()
		 * 具体参见：Crm-Ext-Patch-1.000-v1.0.js中410行（补丁）
		 * **/
		'/contents/pages/common/Com.yucheng.bcrm.common.OrgFieldx.js' // 机构放大镜
]);

var url = basepath + '/departmentManagerAction.json'; // 数据查询url
var comitUrl = basepath + '/departmentManagerAction!saveData.json';// 新增修改url

var createView = true;  // 是否需要构建新建面板
var editView = true;    // 是否需要构建修改面板
var detailView = true;  // 是否需要构建详情面板

/** 自定义变量区* */
// 机构树加载条件
var condition = {
	searchType : 'SUBTREE' // 查询子机构
};

/**
 * 上级部门数据
 */
var departParentStore = new Ext.data.Store( {
	autoLoad : false,
	restful : true,
	proxy : new Ext.data.HttpProxy( {
		url : basepath + '/departmentManagerAction!getDptParents.json'
	}),
	reader : new Ext.data.JsonReader( {
		root : 'json.data'
	}, [ {
		name : 'dptId'
	}, {
		name : 'dptName'
	} ])
});
/**
 * 下辖机构数据加载器
 * 
 * @author sniper
 * @since 2014-4-29
 */
var includeOrgStore = new Ext.data.SimpleStore( {
	autoLoad : false,
	restful : true,
	proxy : new Ext.data.HttpProxy( {
		url : basepath + '/commsearch.json?condition=' + Ext.encode(condition)
	}),
	reader : new Ext.data.JsonReader( {
		root : 'json.data'
	}, [ {
		name : 'key',
		mapping : 'UNITID'
	}, {
		name : 'value',
		mapping : 'UNITNAME'
	} ])
});


/**
 * 本地数据字典 ：业务条线
 */
var localLookup = {
	'DPTTYPE' : [ 
		{key : '',value : '无'},
		{key : '01',value : '零售'}, 
		{key : '02',value : '对公'}
	]
};

/**
 * 部门信息元数据
 * 
 * @author sniper
 * @since 2014-4-29
 */
var fields = [ 
	{name : 'ID',hidden : true},
	{name : 'DPT_ID',text : '部门编号',resultWidth:100,allowBlank : false},
	{name : 'DPT_NAME',text : '部门名称',searchField : true,allowBlank : false},
	{name : 'DPT_PARENT_ID',text : '上级部门',hidden : true,xtype : 'combo',store : departParentStore,forceSelection : true,
		editable : false,ComboBox : true,mode : 'local',triggerAction : 'all',displayField : 'dptName',valueField : 'dptId',width : 500,anchor : '100%'},
	{name : 'DPT_PARENT_NAME',text : '上级部门名称'},
	{name : 'BELONG_ORG_ID',text : '所属机构编号',hidden:true,dataType:'string'},
	{name : 'ORG_NAME',text : '所属机构',searchField : true,resutlWidth : 150,xtype : 'wcombotree',innerTree : 'DATASETMANAGERTREE1',
		showField : 'UNITNAME',hideField : 'UNITID',hiddenName : 'BELONG_ORG_ID',allowBlank : false},
//	{name : 'INCLUDE_ORG_IDS',text : '所辖机构',hidden : true,xtype : 'lovcombo',displayField : 'value',valueField : 'key',
//		width : 500,store : includeOrgStore,editable : false,triggerAction : 'all',anchor : '100%',mode : 'local'},
	{name : 'COUNT_INCLUDE_ORGS',text : '所辖机构数',hidden:true},
	{name : 'DPT_TYPE',text : '业务条线',translateType : 'DPTTYPE'},
	{name : 'REMARK',text : '备注'},
	{name: 'QTIPS',text:'提示',xtype: 'displayfield',hidden:true,value:'部门编号填写规则为：前面6位数字为机构号，后面部分格式为：01或01-02或01-02-01，其中前两位01，表示该所属机构的直属部门编号，中间两位表示01部门的下级部门02，再后面两位表示子部门02下的下级部门。若部门下无子部门。'},
	{name : 'APP_ID',text : 'APP_ID',hidden : true}
];

/**
 * 左侧机构树数据加载器
 * 
 * @author sniper
 * @since 2014-4-29
 */
var treeLoaders = [ {
	key : 'DATASETMANAGERLOADER',
	url : basepath + '/commsearch.json?condition=' + Ext.encode(condition),
	parentAttr : 'SUPERUNITID',
	locateAttr : 'UNITID',
	jsonRoot : 'json.data',
	rootValue : JsContext._orgId,
	textField : 'UNITNAME',
	idProperties : 'UNITID'
} ];

/**
 * 左侧机构树配置
 * 
 * @author sniper
 * @since 2014-4-29
 */
var treeCfgs = [ {
	key : 'DATASETMANAGERTREE',
	loaderKey : 'DATASETMANAGERLOADER',
	autoScroll : true,
	rootCfg : {
		expanded:true,
		id:JsContext._orgId,
		text:JsContext._unitname,
		autoScroll:true,
		children:[]
	},
	clickFn : function(node) {
		if(node.isRoot==true){//如果用户点击根节点，默认加载所有数据
			setSearchParams( {
			});
		}else{//如果用户点击非根节点，加载改机构下的数据
			setSearchParams( {
				BELONG_ORG_ID : node.id
			});
		}
	}
}, {
	key : 'DATASETMANAGERTREE1',
	loaderKey : 'DATASETMANAGERLOADER',
	autoScroll : true,
	rootCfg : {
		expanded:true,
		id:JsContext._orgId,
		text:JsContext._unitname,
		autoScroll:true,
		children:[],
		UNITID:JsContext._orgId,
		UNITNAME:JsContext._unitname
	}
} ];

/**
 * 机构树布局
 * 
 * @author sniper
 * @since 2014-4-29
 */
var edgeVies = {
	left : {
		width : 200,
		layout : 'form',
		items : [ TreeManager.createTree('DATASETMANAGERTREE') ]
	}
};

/**
 * 新增面板配置
 * 
 * @author sniper
 * @since 2014-4-29
 */
var createFormViewer = [{
	fields : [ 'DPT_ID', 'DPT_NAME', 'DPT_PARENT_ID',
		'DPT_PARENT_NAME', 'ORG_NAME',	/*'INCLUDE_ORG_IDS',*/'DPT_TYPE', 'APP_ID' ],
	fn : function(DPT_ID, DPT_NAME, DPT_PARENT_ID, DPT_PARENT_NAME,
		 ORG_NAME, /*INCLUDE_ORG_IDS,*/DPT_TYPE, APP_ID) {
		APP_ID.value = '62';
		//INCLUDE_ORG_IDS.hidden = true;//下拉多选框显示样式和修改、详情赋值存在问题，联动不存在问题，暂时隐藏
		//INCLUDE_ORG_IDS.value = '100000';//暂时设置默认值(华一银行总行)由于数据表中改字段非空，当下拉复选框西施样式和赋值无问题时放开
		DPT_PARENT_ID.hidden = false;
		DPT_PARENT_NAME.hidden = true;
		return [ DPT_ID, DPT_NAME,  ORG_NAME, DPT_TYPE,
			DPT_PARENT_ID, DPT_PARENT_NAME,/* INCLUDE_ORG_IDS,*/APP_ID ];
	}
}, {
	columnCount : 1,
	fields : [ 'REMARK' ,'QTIPS'],
	fn : function(REMARK,QTIPS) {
		REMARK.xtype = 'textarea';
		QTIPS.hidden = false;
		return [ REMARK ,QTIPS];
	}
} ];

/**
 * 修改面板配置
 * 
 * @author sniper
 * @since 2014-4-29
 */
var editFormViewer = [{
	fields : [ 'DPT_ID', 'DPT_NAME', 'DPT_PARENT_ID',
			'DPT_PARENT_NAME', 'ORG_NAME',/*'INCLUDE_ORG_IDS',*/'DPT_TYPE', 'APP_ID' ],
	fn : function(DPT_ID, DPT_NAME, DPT_PARENT_ID, DPT_PARENT_NAME,
			 ORG_NAME, /*INCLUDE_ORG_IDS, */DPT_TYPE, APP_ID) {
		DPT_ID.hidden = true;
		DPT_PARENT_ID.hidden = false;
		DPT_PARENT_NAME.hidden = true;
		//INCLUDE_ORG_IDS.hidden = true;//下拉多选框显示样式和修改、详情赋值存在问题，联动不存在问题，暂时隐藏
		//INCLUDE_ORG_IDS.value = '100000';//暂时设置默认值(华一银行总行)由于数据表中改字段非空，当下拉复选框西施样式和赋值无问题时放开
		return [ DPT_ID, DPT_NAME,  ORG_NAME, DPT_TYPE,
				DPT_PARENT_ID, DPT_PARENT_NAME,/* INCLUDE_ORG_IDS,*/APP_ID ];
	}
}, {
	columnCount : 1,
	fields : [ 'REMARK' ,'QTIPS'],
	fn : function(REMARK,QTIPS) {
		REMARK.xtype = 'textarea';
		QTIPS.hidden = false;
		return [ REMARK ,QTIPS];
	}
} ];

/**
 * 详情面板配置
 * 
 * @author sniper
 * @since 2014-4-29
 */
var detailFormViewer = [{
	fields : [ 'DPT_ID', 'DPT_NAME', 'DPT_PARENT_ID','DPT_PARENT_NAME', 'ORG_NAME',/*'INCLUDE_ORG_IDS',*/'DPT_TYPE', 'APP_ID' ],
	fn : function(DPT_ID, DPT_NAME, DPT_PARENT_ID, DPT_PARENT_NAME,
			 ORG_NAME,/* INCLUDE_ORG_IDS,*/DPT_TYPE, APP_ID) {
		//INCLUDE_ORG_IDS.hidden = true;//下拉多选框显示样式和修改、详情赋值存在问题，联动不存在问题，暂时隐藏
		//INCLUDE_ORG_IDS.value = '100000';//暂时设置默认值(华一银行总行)由于数据表中改字段非空，当下拉复选框西施样式和赋值无问题时放开
		DPT_ID.disabled = true;
		DPT_NAME.disabled = true;
		ORG_NAME.disabled = true;
		DPT_PARENT_NAME.disabled = true;
		DPT_TYPE.disabled = true;
		DPT_ID.cls = "x-readOnly";
		DPT_NAME.cls = "x-readOnly";
		ORG_NAME.cls = "x-readOnly";
		DPT_PARENT_NAME.cls = "x-readOnly";
		DPT_TYPE.cls = "x-readOnly";
		return [ DPT_ID, DPT_NAME,  ORG_NAME, DPT_TYPE,
						DPT_PARENT_ID, DPT_PARENT_NAME,/* INCLUDE_ORG_IDS,*/APP_ID ];
	}
}, {
	columnCount : 1,
	fields : [ 'REMARK' ],
	fn : function(REMARK) {
		REMARK.xtype = 'textarea';
		REMARK.disabled = true;
		REMARK.cls = "x-readOnly";
		return [ REMARK ];
	}
}];

/**
 * 删除按钮配置
 * 
 * @author sniper
 * @since 2014-4-29
 */
var tbar = [ {
	text : '删除',
	handler : function() {
		if (getSelectedData() == false) {
			Ext.Msg.alert('提示', '请选择一条数据！');
			return false;
		} else {
			var id = getSelectedData().data.ID;
			Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId) {
				if (buttonId.toLowerCase() == "no") {
					return;
				}
				var selectRe;
				var tempId;
				var tempCount;
				Ext.Ajax.request( {
					url : basepath+ '/departmentManagerAction!batchDestroy.json?idStr='+ id,
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

/**
 * 数据联动（当新增、修改时选择所属机构动态加载上级部门和所辖机构）
 * 
 * @author sniper
 * @since 2014-4-29
 */
var linkages = {
	ORG_NAME : {
		fields : [ 'DPT_PARENT_ID', /*'INCLUDE_ORG_IDS',*/ 'DPT_ID' ],
		fn : function(ORG_NAME, DPT_PARENT_ID, /*INCLUDE_ORG_IDS,*/ DPT_ID) {
			// 加载上级部门store
			DPT_PARENT_ID.store.load( {
				params : {
					dptId : DPT_ID.getValue(),
					belongOrgId : ORG_NAME.getValue()
				}
			});
			
			/*INCLUDE_ORG_IDS.store.removeAll();
			var params = {
				searchType : 'SUBORGS',
				orgId : ORG_NAME.getValue()
			};
			// 加载所辖直接子机构数据
			Ext.Ajax.request( {
				url : basepath + '/commsearch.json?condition='+ Ext.encode(params),
				method : 'GET',
				success : function(response) {
					var nodeArra = Ext.util.JSON
							.decode(response.responseText).json.data;
					if (0 < nodeArra.length) {// 如果存在直接子机构，则将直接子机构添加入所辖（INCLUDE_ORG_IDS）的stroe
						for ( var i = 0; i < nodeArra.length; i++) {
							var newRecord = new Ext.data.Record( {
								key : nodeArra[i].UNITID,
								value : nodeArra[i].UNITNAME
							});
							INCLUDE_ORG_IDS.store.add(newRecord);
						}
					}
				},
				failure : function() {
					Ext.Msg.alert('提示信息', '加载下辖机构数据失败');
				}
			});*/
		}
	}
};

/**
 * 用户点击修改或详情时需先选择一条记录
 * 
 * @author sniper
 * @since 2014-4-29
 */
var beforeviewshow = function(view) {
	if (view._defaultTitle != '新增') {
		if (!getSelectedData() || getAllSelects().length>1) {
			Ext.Msg.alert('提示信息', '请选择一条数据后操作！');
			return false;
		}
		// 加载上级部门store
		departParentStore.load( {
			params : {
				dptId : getSelectedData().data.DPT_ID,
				belongOrgId : getSelectedData().data.BELONG_ORG_ID
			},
			callback:function(){
				var dptParentId = view.contentPanel.getForm().findField('DPT_PARENT_ID').getValue();
				view.contentPanel.getForm().findField('DPT_PARENT_ID').setValue(dptParentId);
			}
		});
	}
};
/**
 * 结果域面板滑入后触发,系统提供listener事件方法
 * @param {} view
 * @return {Boolean}
 */
var viewshow = function(view){
	if(view.baseType == 'editView'){
		var qtips = "部门编号填写规则为：前面6位数字为机构号，后面部分格式为：01或01-02或01-02-01，其中前两位01，表示该所属机构直属部门编号，中间两位表示01部门的下级部门02，再后面两位表示子部门02下的下级部门。若部门下无子部门。";
		view.contentPanel.getForm().findField('QTIPS').setValue(qtips);
	}
};


/**
 * 修改时加载所辖机构数据
 * 修改表单滑入，加载当前选择数据之后触发；
 * params ：
 *   view：修改表单； 
 *   record ：当前选择的数据；
 */
var aftereditload = function(view, record) {
	/*// 清空所辖机构store
	includeOrgStore.removeAll();
	// 加载所辖直接子机构数据
	var params = {
		searchType : 'SUBORGS',
		orgId : record.data.BELONG_ORG_ID
	};
	Ext.Ajax.request( {
		url : basepath + '/commsearch.json?condition='+ Ext.encode(params),
		method : 'GET',
		success : function(response) {
			var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
			if (0 < nodeArra.length) {// 如果存在直接子机构，则将直接子机构添加入所辖（INCLUDE_ORG_IDS）的stroe
				for ( var i = 0; i < nodeArra.length; i++) {
					var newRecord = new Ext.data.Record( {
						key : nodeArra[i].UNITID,
						value : nodeArra[i].UNITNAME
					});
					includeOrgStore.add(newRecord);
				}
			}
		},
		failure : function() {
			Ext.Msg.alert('提示信息', '加载下辖机构数据失败');
		}
	});*/
};

