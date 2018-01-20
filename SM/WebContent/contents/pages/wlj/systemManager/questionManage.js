/**
 * 试题管理页面
 * @author luyy
 * @since 2014-06-13
 */
Ext.QuickTips.init();
var teamstore = new Ext.data.Store({
			restful : true,
			proxy : new Ext.data.HttpProxy( {
				url : basepath + '/RiskQuession!findResult.json'
			}),
			reader : new Ext.data.JsonReader( {
				totalProperty : 'json.count',
				root : 'json.data'
			}, [ {
				name : 'result',
				mapping : 'RESULT'
			}, {
				name : 'resultScoring',
				mapping : 'RESULT_SCORING'
			}, {
				name : 'resultSort',
				mapping : 'RESULT_SORT'
			} ])
		});

		var teamrownum = new Ext.grid.RowNumberer( {
			header : 'No.',
			width : 28
		});
		var teamsm = new Ext.grid.CheckboxSelectionModel();
		var teamcm = new Ext.grid.ColumnModel( [ teamrownum, {
			header : '选项内容',
			dataIndex : 'result',
			width : 300,
			editor : new Ext.form.TextField( {
				id : 'CREATE_RESULT'
			}),
			sortable : true,
			renderer : function(value, meta, record) {
				meta.attr = 'style="white-space:normal;"';
				return value;
			}
		}, {
			header : '选项分值',
			dataIndex : 'resultScoring',
			editor : new Ext.form.TextField( {
				id : 'CREATE_RESULT_SCORING',
				regex :/^[-]?\d*[.]?(\d{0})?$/,
				maxLength : 3,			
				minLengthText : '该字段不可为空'
			}),
			width : 165,
			sortable : true
		}, {
			header : '选项顺序',
			dataIndex : 'resultSort',
			editor : new Ext.form.NumberField( {
				id : 'CREATE_RESULT_SORT',
				decimalPrecision : 0,
				allowNegative : false,
				maxLength : 3,
				minLength : 1,
				minLengthText : '该字段不可为空'
			}),
			width : 165,
			sortable : true
		} ]);
		
		var tbar0 = new Ext.Toolbar( {
			items : [ {
				text : '新增',
				handler : function() {
					teamstore.add(new Ext.data.Record);
				}
			}, {
				text : '删除',
				handler : function() {
					var records = teamgrid.getSelectionModel().getSelections();
					var recordsLen = records.length;
					if (recordsLen < 1) {
						Ext.Msg.alert("系统提示信息", "请选择记录后进行删除！");
						return;
					} else {
						teamstore.remove(records);
					}
				}
			} ]
		});
//答案
var teamgrid = new Ext.grid.EditorGridPanel( {
	height : 300,
	frame : true,
	overflow : 'auto',
	autoScroll : true,
	store : teamstore, // 数据存储
	stripeRows : true, // 斑马线
	cm : teamcm, // 列模型
	sm : teamsm,
	tbar : tbar0,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	}
});

var teamrownum1 = new Ext.grid.RowNumberer( {
	header : 'No.',
	width : 28
});

var teamsm1 = new Ext.grid.CheckboxSelectionModel();
var teamcm1 = new Ext.grid.ColumnModel( [ teamrownum1, {

	header : '选项内容',
	dataIndex : 'result',
	width : 300,
	editor : new Ext.form.TextField( {
		id : 'CREATE_RESULT'
	}),
	sortable : true,
	renderer : function(value, meta, record) {
		meta.attr = 'style="white-space:normal;"';
		return value;
	}
}, {
	header : '选项分值',
	dataIndex : 'resultScoring',
	editor : new Ext.form.TextField( {
		id : 'CREATE_RESULT_SCORING',
		regex :/^[-]?\d*[.]?(\d{0})?$/,
		maxLength : 3,			
		minLengthText : '该字段不可为空'
	}),
	width : 165,
	sortable : true
}, {
	header : '选项顺序',
	dataIndex : 'resultSort',
	editor : new Ext.form.NumberField( {
		id : 'CREATE_RESULT_SORT',
		decimalPrecision : 0,
		allowNegative : false,
		maxLength : 3,
		minLength : 1,
		minLengthText : '该字段不可为空'
	}),
	width : 165,
	sortable : true
} ]);
var tbar1 = new Ext.Toolbar( {
	items : [ {
		text : '新增',
		handler : function() {
			teamstore.add(new Ext.data.Record);
		}
	}, {
		text : '删除',
		handler : function() {
			var records = teamgrid1.getSelectionModel().getSelections();
			var recordsLen = records.length;
			if (recordsLen < 1) {
				Ext.Msg.alert("系统提示信息", "请选择记录后进行删除！");
				return;
			} else {
				teamstore.remove(records);
			}
		}
	} ]
});
//答案
var teamgrid1 = new Ext.grid.EditorGridPanel( {
	height : 300,
	frame : true,
	overflow : 'auto',
	autoScroll : true,
	store : teamstore, // 数据存储
	stripeRows : true, // 斑马线
	cm : teamcm1, // 列模型
	sm : teamsm1,
	tbar : tbar1,
	loadMask : {
	msg : '正在加载表格数据,请稍等...'
	}
});
var teamrownum2 = new Ext.grid.RowNumberer( {
	header : 'No.',
	width : 28
});

var teamsm2 = new Ext.grid.CheckboxSelectionModel();
var teamcm2 = new Ext.grid.ColumnModel( [ teamrownum2, {

	header : '选项内容',
	dataIndex : 'result',
	width : 300,
	sortable : true,
	renderer : function(value, meta, record) {
		meta.attr = 'style="white-space:normal;"';
		return value;
	}
}, {
	header : '选项分值',
	dataIndex : 'resultScoring',
	width : 165,
	sortable : true
}, {
	header : '选项顺序',
	dataIndex : 'resultSort',
	width : 165,
	sortable : true
} ]);
//答案
var teamgrid2 = new Ext.grid.EditorGridPanel( {
	height : 300,
	frame : true,
	overflow : 'auto',
	autoScroll : true,
	store : teamstore, // 数据存储
	stripeRows : true, // 斑马线
	cm : teamcm2, // 列模型
	sm : teamsm2,
	loadMask : {
	msg : '正在加载表格数据,请稍等...'
	}
});
//保存调用的方法
function save(formPanel){
	if (!formPanel.getForm().isValid()) {
		Ext.MessageBox.alert('系统提示','请正确输入各项必要信息！');
		return false;
	}
	var flag = false;
	var maxFlag = false;
	var answerFlag = false;
	var resultInfo = new Array();
	var i = 0;
	var name = '';
	var maxName = '';
	teamstore.each(function(item) {
				if (!flag) {
					if (item.data.result == ''|| item.data.result == undefined) {
						name += ',选项内容';
						flag = true;
					}
					if (item.data.resultScoring == ''|| item.data.resultScoring == undefined) {
						name += ',选项分值';
						flag = true;
					} else if (item.data.resultScoring > 999) {
						maxName += ',选项分值';
						maxFlag = true;
					}

					if (item.data.resultSort == ''|| item.data.resultSort == undefined) {
						name += ',选项顺序';
						flag = true;
					} else if (item.data.resultSort > 999) {
						maxName += ',选项顺序';
						maxFlag = true;
					}
					resultInfo[i] = item.data.result
							+ ":"
							+ item.data.resultScoring
							+ ":"
							+ item.data.resultSort;
					i += 1;
				}
			});
	var conditionStr = formPanel.getForm().getValues(false);
	if (flag) {
		Ext.MessageBox.alert('系统提示','请检查' + name + '不可为空！');
		return;
	} else if (maxFlag) {
		Ext.MessageBox.alert('系统提示','请检查' + maxName + '最大值为999！');
		return;
	} else if (!teamstore.data.length > 0) {
		Ext.MessageBox.alert('系统提示','请检查,选项不可为空！');
	} else {
		Ext.Ajax.request( {
					url : basepath + '/RiskQuession!createQuession.json',
					method : 'POST',
					params : {
						resultInfo : resultInfo,
						condition : Ext.encode(conditionStr)
					},
					failure : function(form,action) {
						Ext.MessageBox.alert('系统提示','保存失败！');

					},
					success : function(response) {
						Ext.MessageBox.alert('系统提示','保存成功！');
						reloadCurrentData();
						hideCurrentView();
					}
				});
	}

}

var url =  basepath + '/RiskQuession.json';
var createView = false;
var editView = false;
var detailView = false;

var lookupTypes = ['TITLE_TYPE'];

var fields = [{name:'TITLE_ID',hidden:true},
              {name:'TITLE_NAME',text:'试题标题',datdType:'string',resutlWidth:200,searchField: true,allowBlank:false},
              {name:'TITLE_TYPE',text:'试题分类',translateType : 'TITLE_TYPE',searchField: true,allowBlank:false},
              {name:'UPDATOR',hidden:true},
              {name:'HASUSED',text:'是否被试卷引用',hidden:true},
              {name:'USER_NAME',text:'更新人',dataType:'string'},
              {name:'UPDATE_DATE',text:'更新日期',dateType:'date'},
              {name:'TITLE_REMARK',text:'试题备注',xtype:'textarea',maxLength:400}
              ];

var tbar = [{
			text : '删除',
			handler : function(){
				if(getSelectedData() == false){
					Ext.Msg.alert('提示','请选择数据！');
					return false;
				}else{
					var ID = '';
					for (var i=0;i<getAllSelects().length;i++){
						if(getAllSelects()[i].data.HASUSED == 'yes'){
							Ext.Msg.alert('提示','所选试题:'+getAllSelects()[i].data.TITLE_NAME+'在问卷中引用，不能删除！');
							return false;
						}else{
							ID += getAllSelects()[i].data.TITLE_ID;
							ID += ",";
						}
					}
					ID = ID.substring(0, ID.length-1);
					Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
						if(buttonId.toLowerCase() == "no"){
						return false;
						} 
					    Ext.Ajax.request({
		                    url: basepath+'/RiskQuession!batchDel.json?idStr='+ID,                                
		                    success : function(){
		                        Ext.Msg.alert('提示', '删除成功');
		                        reloadCurrentData();
		                    },
		                    failure : function(){
		                        Ext.Msg.alert('提示', '删除失败');
		                    }
		                });
					});
				
					}
			}
}];
var customerView =  [{
	title : '新增试题',
	type : 'form',
	autoLoadSeleted : false,
	groups : [{
		fields : ['TITLE_ID','TITLE_NAME','TITLE_TYPE','UPDATOR','UPDATE_DATE'],
		fn : function(TITLE_ID,TITLE_NAME,TITLE_TYPE,UPDATOR,UPDATE_DATE){
			UPDATE_DATE.value = new Date().format('Y-m-d');
			UPDATOR.value = __userId;
			UPDATE_DATE.hidden = true;
			UPDATOR.hidden = true;
			return [TITLE_NAME,TITLE_TYPE,UPDATOR,UPDATE_DATE,TITLE_ID];
		}
	},{
		columnCount : 1 ,
		fields : ['TITLE_REMARK'],
		fn : function(TITLE_REMARK){
			return [TITLE_REMARK];
		}
	},{
		columnCount : 1 ,
		fields : ['TITLE_REMARK'],
		fn : function(TITLE_REMARK){
			return [teamgrid];
		}
	}
	],
	formButtons : [{
						text : '保存',
						fn : function(formPanel,basicForm) {
							//alert(formPanel);
							save(formPanel);
						}
					}, {
						text : '重置',
						id : 'reset',
						fn : function(formPanel,basicForm) {
							teamstore.removeAll();
							formPanel.getForm().reset();
						}
					} ]
},{
	title : '修改试题',
	type : 'form',
	autoLoadSeleted : true,
	groups : [{
		fields : ['TITLE_ID','TITLE_NAME','TITLE_TYPE','UPDATOR','UPDATE_DATE'],
		fn : function(TITLE_ID,TITLE_NAME,TITLE_TYPE,UPDATOR,UPDATE_DATE){
			UPDATE_DATE.value = new Date().format('Y-m-d');
			UPDATOR.value = __userId;
			UPDATE_DATE.hidden = true;
			UPDATOR.hidden = true;
			return [TITLE_NAME,TITLE_TYPE,UPDATOR,UPDATE_DATE,TITLE_ID];
		}
	},{
		columnCount : 1 ,
		fields : ['TITLE_REMARK'],
		fn : function(TITLE_REMARK){
			return [TITLE_REMARK];
		}
	},{
		columnCount : 1 ,
		fields : ['TITLE_REMARK'],
		fn : function(TITLE_REMARK){
			return [teamgrid1];
		}
	}
	],
	formButtons : [{
						text : '保存',
						fn : function(formPanel,basicForm) {
							save(formPanel);
							}
					}]

	
},{

	title : '查看试题',
	type : 'form',
	autoLoadSeleted : true,
	groups : [{
		fields : ['TITLE_ID','TITLE_NAME','TITLE_TYPE','UPDATOR','UPDATE_DATE'],
		fn : function(TITLE_ID,TITLE_NAME,TITLE_TYPE,UPDATOR,UPDATE_DATE){
			return [TITLE_NAME,TITLE_TYPE,UPDATOR,TITLE_ID];
		}
	},{
		columnCount : 1 ,
		fields : ['TITLE_REMARK'],
		fn : function(TITLE_REMARK){
			return [TITLE_REMARK];
		}
	},{
		columnCount : 1 ,
		fields : ['TITLE_REMARK'],
		fn : function(TITLE_REMARK){
			return [teamgrid2];
		}
	}
	]}
];

var beforeviewshow = function(view){
	if(view._defaultTitle=='修改试题'||view._defaultTitle=='查看试题'){
			if(getSelectedData() == false || getAllSelects().length>1){
				Ext.Msg.alert('提示','请选择一条数据');
				return false;
			}else{//加载属性数据
				if(view._defaultTitle=='修改试题'&& getSelectedData().data.HASUSED == 'yes'){
					Ext.Msg.alert('提示','所选试题:'+getSelectedData().data.TITLE_NAME+'在问卷中引用，不能修改！');
					return false;
				}
				teamstore.load( {
					params : {
						titleId : getSelectedData().data.TITLE_ID
					}
				});
			}	
	}
};

