/**
 * 问卷管理页面
 * @author luyy
 * @since 2014-06-13
 */
var paperId = '';
var url = basepath+'/paperManage.json';

var lookupTypes = ['OPTION_TYPE','AVAILABLE'];

var createView = true;
var editView = true;
var detailView = true;

/**
 * 试卷选题列表
 */
var titleTypeStore =  new Ext.data.Store( {
	restful : true,
	autoLoad : true,
	proxy : new Ext.data.HttpProxy( {
		url : basepath + '/lookup.json?name=TITLE_TYPE'
	}),
	reader : new Ext.data.JsonReader( {
		root : 'JSON'
	}, [ 'key', 'value' ])
});
var sm2 = new Ext.grid.CheckboxSelectionModel();
var sm3 = new Ext.grid.CheckboxSelectionModel();
var rownum = new Ext.grid.RowNumberer( {
	header : 'No.',
	width : 28
});
var rownum1 = new Ext.grid.RowNumberer( {
	header : 'No.',
	width : 28
});
// 定义列模型
var questionCm = new Ext.grid.ColumnModel( [ rownum,{
	header : '试题标题',
	dataIndex : 'titleName',
	sortable : true,
	menuDisabled : true,
	width : document.body.scrollWidth / 4,
	renderer : function(value, meta, record) {
		meta.attr = 'style="white-space:normaddl;"';
        		return value;
	}
}, {
	header : '试题分类',
	dataIndex : 'titleType',
	sortable : true, 
	menuDisabled : true,
	width : document.body.scrollWidth / 8,
	renderer : function(value) {
		if (value != '') {
			var index = titleTypeStore.find('key', value);
			return titleTypeStore.getAt(index).get('value');
		}

	}
}, {
	header : '选题',
	dataIndex : 'IS_CHECKED',
	width : document.body.scrollWidth / 12,
	renderer:function(value,record,e){
		var checked =(e.json.IS_CHECKED=='1')?'checked':'';
		var checkBox = '<input id='+e.id+'_check type="checkbox" '+checked+' onclick="setSort(this,\''+e.id+'\');"/>';
		return  checkBox;
	},
	menuDisabled : true,
	hidden : false
}, {
	header : '选项顺序',
	dataIndex : 'titlesort',
	renderer:function(value,record,e){
		var sortValue =e.json.QUESTION_ORDER;
		var sort;
		if(sortValue=='')
			sort = '<input id='+e.id+'sort  type="textfield"  style="display:none;" value='+sortValue+' >';
		else
			sort = '<input id='+e.id+'sort  type="textfield"  value='+sortValue+' >';
		return  sort;
	},
	width : 165,
	sortable : true
}, {
	header : 'title_id',
	dataIndex : 'titleId',
	menuDisabled : true,
	hidden : true
} ]);


var questionCm1 = new Ext.grid.ColumnModel( [ rownum1,{
	header : '试题标题',
	dataIndex : 'titleName',
	sortable : true,
	menuDisabled : true,
	width : document.body.scrollWidth / 4,
	renderer : function(value, meta, record) {
		meta.attr = 'style="white-space:normaddl;"';
        		return value;
	}
}, {
	header : '试题分类',
	dataIndex : 'titleType',
	sortable : true, 
	menuDisabled : true,
	width : document.body.scrollWidth / 8,
	renderer : function(value) {
		if (value != '') {
			var index = titleTypeStore.find('key', value);
			return titleTypeStore.getAt(index).get('value');
		}

	}
}, {
	header : 'title_id',
	dataIndex : 'titleId',
	menuDisabled : true,
	hidden : true
} ]);


var store = new Ext.data.Store( {
	restful : true,
	proxy : new Ext.data.HttpProxy( {
		url : basepath + '/questionQuery.json'
	}),
	reader : new Ext.data.JsonReader( {
		totalProperty : 'json.count',
		root : 'json.data'
	}, [ {
		name : 'titleName',
		mapping : 'TITLE_NAME'
	}, {
		name : 'titleType',
		mapping : 'TITLE_TYPE'
	}, {
		name : 'titleSort',
		type:'number',
		mapping : 'QUESTION_ORDER'
	}, {
		name : 'available',
		mapping : 'AVAILABLE'
	}, {
		name : 'updator',
		mapping : 'USER_NAME'
	}, {
		name : 'updateDate',
		mapping : 'UPDATE_DATE'
	}, {
		name : 'titleId',
		mapping : 'TITLE_ID'
	}, {
		name : 'is_checked',
		mapping : 'IS_CHECKED'
	} ])
});
var store1 = new Ext.data.Store( {
	restful : true,
	proxy : new Ext.data.HttpProxy( {
		url : basepath + '/QueryCheckedQuestion.json'
	}),
	reader : new Ext.data.JsonReader( {
		totalProperty : 'json.count',
		root : 'json.data'
	}, [ {
		name : 'titleName',
		mapping : 'TITLE_NAME'
	}, {
		name : 'titleType',
		mapping : 'TITLE_TYPE'
	}, {
		name : 'titleSort',
		type:'number',
		mapping : 'QUESTION_ORDER'
	}, {
		name : 'available',
		mapping : 'AVAILABLE'
	}, {
		name : 'updator',
		mapping : 'USER_NAME'
	}, {
		name : 'updateDate',
		mapping : 'UPDATE_DATE'
	}, {
		name : 'titleId',
		mapping : 'TITLE_ID'
	}, {
		name : 'is_checked',
		mapping : 'IS_CHECKED'
	} ])
});

var questionGrid =  new Ext.grid.EditorGridPanel( {
	height : 350,
	width : 790,
	frame : true,
	autoScroll : true,
	region : 'center', // 返回给页面的div
	store : store,
	stripeRows : true, // 斑马线
	cm : questionCm,
	sm : sm2,
	viewConfig : {}
});
var questionGrid1 =  new Ext.grid.EditorGridPanel( {
	height : 200,
	width : 820,
	frame : true,
	autoScroll : true,
	region : 'center', // 返回给页面的div
	store : store1,
	stripeRows : true, // 斑马线
	cm : questionCm1,
	sm : sm3,
	viewConfig : {}
});

var questionForm = new Ext.Panel({
	layout:'fit',
	items:[questionGrid],
	buttonAlign : "center",
	buttons:[{
		text : '保存',
		id:'save',
		handler : function() {
    	 var addArray=[] ;
    	 var deleteArray =[];
			for(var j=0;j<questionGrid.store.data.items.length;j++){
			var firstArray = {};
			var secondArray ={};
			var k =questionGrid.store.data.keys;
			var one =document.getElementById(k[j]+"_check");
			var two =document.getElementById(k[j]+"sort");
			var children = questionGrid.store.data.items[j].json;
			if(one.checked==true){
				if (children.IS_CHECKED=='0'){
					firstArray.paper_id=paperId;
					firstArray.question_id=children.TITLE_ID;
					if(two.value == '/' || two.value == '' ||two.value == undefined){
						Ext.Msg.alert('提示', '试题：'+children.TITLE_NAME+'的试题顺序未填写');
						return false;
					}
					firstArray.sort_id=two.value;
					addArray.push(firstArray);
				}
			}else{
				if(children.IS_CHECKED=='1'){
					secondArray.paper_id=paperId;
					secondArray.question_id=children.TITLE_ID;
					secondArray.id=children.ID;
					deleteArray.push(secondArray);
				}
			}
		}
    	 Ext.Ajax.request({
    		 url:basepath+'/paperManage!saveQuestion.json',
    		 method: 'POST',
    		 params : {
    		 'addArray':Ext.encode(addArray),
    		 'deleteArray':Ext.encode(deleteArray)
    	 },
    	 success : function(response) {
    		 Ext.Msg.alert('提示', '保存成功');
    		 hideCurrentView();
    		 reloadCurrentData();
    	 },
    	 failure : function(response) {
    		 Ext.Msg.alert('提示', '保存失败');
    	 }
    	 });
	}
	}]
});

var fields = [{name:'ID',hidden:true},
              {name:'PAPER_NAME',text:'问卷名称',dataType:'string',searchField:true,allowBlank:false},
              {name:'OPTION_TYPE',text:'问卷类型',translateType : 'OPTION_TYPE',searchField: true,allowBlank:false},
              {name:'AVAILABLE',text:'问卷状态',translateType:'AVAILABLE',allowBlank:false},
              {name:'CREATOR',text:'创建者',dataType:'string'},
              {name:'CREATE_ORG',text:'创建机构',dataType:'string'},
              {name:'CREATE_DATE',text:'创建日期',dataType:'date',hidden:true,cls:'x-readOnly'},//创建日期后台插入,不在页面作展示
              {name:'PUBLISH_DATE',text:'发布日期',dataType:'date'},
              {name:'REMARK',text:'问卷备注',xtype:'textarea'},
              {name:'HASQ',hidden:true,text:'是否选择试题'}
              ];


var createFormViewer = [{
	fields : ['ID','OPTION_TYPE','PAPER_NAME','AVAILABLE','CREATOR','CREATE_ORG','CREATE_DATE'],
	fn : function(ID,OPTION_TYPE,PAPER_NAME,AVAILABLE,CREATOR,CREATE_ORG,CREATE_DATE){
			CREATE_DATE.value = new Date().format('Y-m-d');
			AVAILABLE.value = '1';
			AVAILABLE.readOnly = true;
			AVAILABLE.cls='x-readOnly' ;
			CREATOR.value = __userName;
			CREATE_ORG.value = __unitname;
			CREATOR.hidden = true;
			CREATE_ORG.hidden = true;
		return [PAPER_NAME,OPTION_TYPE,AVAILABLE,CREATOR,CREATE_ORG,CREATE_DATE,ID];
	}
},{
	columnCount : 1 ,
	fields : ['REMARK'],
	fn : function(REMARK){
		return [REMARK];
	}
}];

var editFormViewer =[{
	fields : ['ID','OPTION_TYPE','PAPER_NAME','AVAILABLE','CREATOR','CREATE_ORG','CREATE_DATE'],
	fn : function(ID,OPTION_TYPE,PAPER_NAME,AVAILABLE,CREATOR,CREATE_ORG,CREATE_DATE){
			CREATOR.hidden = true;
			AVAILABLE.readOnly = true;
			AVAILABLE.cls='x-readOnly' ;
			CREATE_ORG.hidden = true;
		return [PAPER_NAME,OPTION_TYPE,AVAILABLE,CREATOR,CREATE_ORG,CREATE_DATE,ID];
	}
},{
	columnCount : 1 ,
	fields : ['REMARK'],
	fn : function(REMARK){
		return [REMARK];
	}
}];

var detailFormViewer =[{
	fields : ['ID','OPTION_TYPE','PAPER_NAME','AVAILABLE','CREATOR','CREATE_ORG','CREATE_DATE'],
	fn : function(ID,OPTION_TYPE,PAPER_NAME,AVAILABLE,CREATOR,CREATE_ORG,CREATE_DATE){
		return [PAPER_NAME,OPTION_TYPE,AVAILABLE,CREATOR,CREATE_ORG,CREATE_DATE,ID];
	}
},{
	columnCount : 1 ,
	fields : ['REMARK'],
	fn : function(REMARK){
		return [REMARK];
	}
},{
	columnCount : 1 ,
	fields : ['REMARK'],
	fn : function(REMARK){
		return [questionGrid1];
	}
}];



var tbar = [{
	text:'删除',
	handler:function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择数据！');
			return false;
		}else{
			var ID = '';
			for (var i=0;i<getAllSelects().length;i++){
				if(getAllSelects()[i].data.AVAILABLE!= '1'){
					Ext.Msg.alert('提示','只能删除[未审核]状态的问卷信息！');
					return false;
				}else{
					ID += getAllSelects()[i].data.ID;
					ID += ",";
				}
			}
			ID = ID.substring(0, ID.length-1);
			Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
				if(buttonId.toLowerCase() == "no"){
				return false;
				} 
			    Ext.Ajax.request({
                    url: basepath+'/paperManage!batchDel.json?idStr='+ID,                                
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
},{text:'提交审核',
	handler:function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
			if(getSelectedData().data.AVAILABLE != '1'){
				Ext.Msg.alert('提示','只能提交[未审核]状态的问卷！');
				return false;
			}else if(getSelectedData().data.HASQ != 'yes'){
				Ext.Msg.alert('提示','该问卷还未选题，不能提交！');
				return false;
			}else{
				Ext.Msg.wait('正在处理，请稍后......','系统提示');
				Ext.Ajax.request({
					url : basepath + '/paperManage!initFlow.json',
					method : 'GET',
					params : {
						instanceid:getSelectedData().data.ID, //将id传给后台关联流程的实例号（唯一）
						name:getSelectedData().data.PAPER_NAME
					},
					waitMsg : '正在提交申请,请等待...',										
					success : function(response) {
//						Ext.Ajax.request({
//							url : basepath + '/paperManage!initFlowJob.json',
//							method : 'POST',
//							params : {
//								instanceid:getSelectedData().data.ID //将id传给后台关联流程的实例号（唯一）
//							},success : function() {
//								Ext.Msg.alert('提示', '提交成功!');	
//								reloadCurrentData();
//							},	
//							failure : function() {
//								Ext.Msg.alert('提示', '提交失败,请手动到代办任务中提交!');	
//							}
//						});
						var ret = Ext.decode(response.responseText);
						var instanceid = ret.instanceid;//流程实例ID
						var currNode = ret.currNode;//当前节点
						var nextNode = ret.nextNode;//下一步节点
						selectUserList(instanceid,currNode,nextNode);//选择下一步办理人
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败');
						reloadCurrentData();
					}
				});
			
			}
		}
	}
},{
	text:'发布',
	handler:function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
			if(getSelectedData().data.AVAILABLE != '3'){
				Ext.Msg.alert('提示','只能发布[未发布]状态的问卷！');
				return false;
			}else{
				Ext.Msg.wait('正在处理，请稍后......','系统提示');
				Ext.Ajax.request({
					url : basepath + '/paperManage!publish.json',
					method : 'POST',
					params : {
						id:getSelectedData().data.ID, 
						type:getSelectedData().data.OPTION_TYPE
					},
					waitMsg : '正在提交申请,请等待...',										
					success : function() {
								Ext.Msg.alert('提示', '发布成功!');
								reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败');
						reloadCurrentData();
					}
				});
			
			}
		}
	}

}];


var customerView = [{
	title : '选题',
	xtype : 'panel',
	frame : true,
	suspendWidth:.6,
	layout : 'fit',
	items:[questionForm],
	recordView : true
}];

var beforeviewshow = function(view){
	if(view == getEditView()||view == getDetailView()||view._defaultTitle=='选题'){
			if(getSelectedData() == false||getAllSelects().length>1){
				Ext.Msg.alert('提示','请选择一条数据');
				return false;
			}else{//加载属性数据
				if(view == getEditView()&& getSelectedData().data.AVAILABLE != '1'){
					Ext.Msg.alert('提示','只能修改[未审核]状态的问卷信息！');
					return false;
				}
				if(view.baseType == 'detailView'){//详情面板，需要加载试题信息
					store1.load({
						 params : {
							'paperId':getSelectedData().data.ID
						 }
					});
				}else if(view._defaultTitle=='选题'&& getSelectedData().data.AVAILABLE != '1'){
					Ext.Msg.alert('提示','只能为[未审核]状态的问卷选题！');
					return false;
				}else{
					paperId = getSelectedData().data.ID;
					store.load({
		    			 params : {
		    				'paperId':getSelectedData().data.ID
		    			 }
					});
					if(getSelectedData().data.AVAILABLE == '1'){//控制选题的按钮
						Ext.getCmp('save').show();
					}else{
						Ext.getCmp('save').hide();
					}
				}
			}	
	}
};
//var aftereditload =function(view){
//	for(var j=0;j<questionGrid.store.data.items.length;j++){
//		
//		var k =questionGrid.store.data.keys;
//		var one =document.getElementById(k[j]+"_check");
//		var two =document.getElementById(k[j]+"sort");
//		var children = questionGrid.store.data.items[j].json;
//		
//		setSort(one,k[j]);
//	}
//}
	
//根据checkbox框的选中与否设置输入框的显示和隐藏
function setSort(obj,id){
	if(obj.checked){
		document.getElementById(id+'sort').style.display="";
	}else{
		document.getElementById(id+'sort').style.display="none";
		document.getElementById(id+'sort').value="";
	}
}
//判断输入的值是否重复
function judgeVal(obj){
	var s =document.getElementsByName("titlesort");
	for( var i = 0; i < s.length; i++ ){ 
			if(s[i].value==obj.value&&s[i].id!=obj.id){
				Ext.Msg.alert('提示','选题顺序重复请重新输入');
				document.getElementById(obj.id).value="";
				return;
			} ;
	} 
}
