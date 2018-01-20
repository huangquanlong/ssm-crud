/**
 * @description 用户认证策略
 * @since 2014-07-19
 */
Ext.QuickTips.init(); 

var needGrid = false;
WLJUTIL.suspendViews=false;  //自定义面板是否浮动

var fields = [
	{name: 'TEST',text:'此文件fields必须要有一个无用字段', resutlWidth:80}
];

	//是否
var yesnoStore = new Ext.data.ArrayStore({
	fields : [ 'value', 'text' ],
	data : [ [ 1, '是' ], [ 2, '否' ] ]
});
var loginIpStore = new Ext.data.ArrayStore({
    fields : [ 'value', 'text' ],
    data : [ [ 1, '冻结用户' ], [ 2, '禁止' ], [ 3, '警告' ] ]
});
var qForm = new Ext.form.FormPanel({
	title : "用户认证策略(说明：勾选表示启用该认证策略)",
	labelWidth : 15, // 标签宽度
	frame : true, // 是否渲染表单面板背景色
	labelAlign : 'middle', // 标签对齐方式
	buttonAlign : 'center',
	region:'center',
	split:true,
		layout : 'column',
		items : [{
			columnWidth : .5,
			layout : 'form',
			items : [{
			    title: '首次登陆策略',
			    id:'TacticsFlag1',
			    xtype: 'fieldset',
			    checkboxToggle: true,
			    animCollapse :true,
			    height: 65,
			    items:[{
			        xtype: 'compositefield',
			        fieldLabel: '',
			        combineErrors: false,
			        items: [{
			            xtype: 'displayfield',
			            value: '首次登陆强制修改口令'
					}]
			    }]
			},{
			    title: '强制修改口令策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag2',
			    checkboxToggle: true,
			    animCollapse :true,
			    hideParent : false,
			    height: 65,
			    items:[{
                    xtype: 'compositefield',
                    fieldLabel: '',
                    combineErrors: false,
                    items: [{
                        xtype: 'displayfield',
                        value: '密码修改时间超过'
                    },{
                        name : 'hours',
                        id:'TacticsDetail2',
                        xtype: 'numberfield',
                        width: 30,
                        maxValue :360,//最大值
                        allowNegative:false,//正数
                        decimalPrecision:0,//整数
                        allowBlank: true
                    }, {
                        xtype: 'displayfield',
                        value: '天后口令过期，认证管理将执行'
                    },{
                        xtype : 'combo',
                        width:80,
                        anchor : '90%',
                        name : 'F_NAME',
                        id:'TacticsAction2',
                        store : loginIpStore,
                        valueField : 'value',
                        displayField : 'text',
                        triggerAction : 'all',
                        mode : 'local'
                    },{
                        xtype: 'displayfield',
                        value: '动作'
                    }]
			    }]
			},{
			    title: '登陆IP策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag3',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
                    xtype: 'compositefield',
                    fieldLabel: '',
                    combineErrors: false,
                    items: [{
                        xtype: 'displayfield',
                        value: '当发现登陆的IP为非常用IP时，认证管理将执行'
                    },{
                        xtype : 'combo',
                        width:80,
                        anchor : '90%',
                        name : 'F_NAME',
                        id:'TacticsAction3',
                        store : loginIpStore,
                        valueField : 'value',
                        displayField : 'text',
                        triggerAction : 'all',
                        mode : 'local'
                    }, {
			           xtype: 'displayfield',
			           value: '动作'
                    }]
			    }]
			},{
			    title: '口令错误策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag4',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
				    xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
			           xtype: 'displayfield',
			           value: '定义当用户的静态口令连续出错'
				    },{
			           name : 'hours',
			           id:'TacticsDetail4',
			           xtype: 'numberfield',
			           width: 20,
			           maxValue :99,//最大值
                       allowNegative:false,//正数
                       decimalPrecision:0,//整数
			           allowBlank: true
			       },{
			           xtype: 'displayfield',
			           value: '次时，认证管理将执行'
			       },{
			           xtype : 'combo',
			           width:80,
			           anchor : '90%',
			           name : 'F_NAME',
			           id:'TacticsAction4',
			           store : loginIpStore,
			           valueField : 'value',
			           displayField : 'text',
			           triggerAction : 'all',
			           mode : 'local'
			       },{
			           xtype: 'displayfield',
			           value: '动作'
			       }]
			    }]
			},{
			    title: '在线用户数策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag11',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
				    xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
			           xtype: 'displayfield',
			           value: '当前在线最大用户数超过'
				    },{
			           name : 'hours',
			           id:'TacticsDetail11',
			           xtype: 'numberfield',
			           width: 80,
			           //maxValue :99,//最大值
                       allowNegative:false,//正数
                       decimalPrecision:0,//整数
			           allowBlank: true
			       },{
			           xtype: 'displayfield',
			           value: '时，认证管理将执行'
			       },{
			           xtype : 'combo',
			           width:80,
			           anchor : '90%',
			           name : 'F_NAME',
			           id:'TacticsAction11',
			           store : loginIpStore,
			           valueField : 'value',
			           displayField : 'text',
			           triggerAction : 'all',
			           mode : 'local'
			       },{
			           xtype: 'displayfield',
			           value: '动作'
			       }]
			    }]
			}, /*{
			    title: '口令期限策略',
			    xtype: 'fieldset',
			    checkboxToggle: true,
			    height: 65,
			    items:[
					{
					    xtype: 'compositefield',
					    fieldLabel: '',
					    combineErrors: false,
					    items: [
					       {
					           xtype: 'displayfield',
					           value: '用户的口令有效期限为'
					       },{
					           name : 'hours',
					           xtype: 'numberfield',
					           width: 20,
					           maxValue :99,//最大值
                               allowNegative:false,//正数
                               decimalPrecision:0,//整数
					           allowBlank: true
					       },
					       {
					           xtype: 'displayfield',
					           value: '天（如90天）。'
					       }
					    ]
					}
			    ]
		 },*/
			{
			    title: '登录时间段策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag5',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
			        xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
				        xtype: 'displayfield',
				        value: '是否仅工作日登录'
				    },{
				        xtype : 'combo',
						id:'TacticsDetailyesNo5',
						width:80,
						anchor : '90%',
						name : 'F_NAME',
						store:yesnoStore,
						valueField : 'value',
						displayField : 'text',
						triggerAction : 'all',
						mode : 'local'
				   },{
			           xtype: 'displayfield',
			           value: '登录的时间段为'
			       },new Ext.form.TimeField({
			           fieldLabel: '',
			           name: 'time',
			           id:'TacticsDetailBeginTime5',
			           format:'G:i',
			           width:80//,
//										           minValue: '0:00',
//										           maxValue: '24:00'
			       }),{
			           xtype: 'displayfield',
			           value: '至'
			       },new Ext.form.TimeField({
			           fieldLabel: '',
			           id:'TacticsDetailEndTime5',
			           format:'G:i',
			           width:80,
			           name: 'time'//,
//										           minValue: '8:00am',
//										           maxValue: '6:00pm'
			       })]
			    }]
			}]
		},{
		    columnWidth : .5,
		    layout : 'form',
	        items : [{
	            title: '口令重复策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag6',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
				    xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
			           xtype: 'displayfield',
			           value: '定义用户口令不能与最近的'
			       },{
			           name : 'hours',
			           id:'TacticsDetail6',
			           xtype: 'numberfield',
			           width: 20,
			           maxValue :99,//最大值
                       allowNegative:false,//正数
                       decimalPrecision:0,//整数
			           allowBlank: true
			       },{
			           xtype: 'displayfield',
			           value: '次历史口令重复'
			       }]
				}]
			},{
			    title: '口令长度策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag7',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
				    xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
			           xtype: 'displayfield',
			           value: '口令最短长度'
			       },{
			           name : 'hours',
			           xtype: 'numberfield',
			           id:'TacticsDetail7',
			           width: 20,
			           maxValue :99,//最大值
                       allowNegative:false,//正数
                       decimalPrecision:0,//整数
			           allowBlank: true
			       }]
			    }]
			},{
			    title: '口令复杂度策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag8',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
				    xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
				        xtype: 'displayfield',
				        value: '口令必须包含组合：'
				    },{
				        fieldLabel: '',
				        xtype: 'checkboxgroup',
				        id:'TacticsDetail8',
				        columns: [50,80,80,80],
				        items: [
			                {id:'td81',boxLabel: '数字',inputValue :'1'},
			                {id:'td82',boxLabel: '大写字母',inputValue :'2'},
			                {id:'td83',boxLabel: '小写字母',inputValue :'3'},
			                {id:'td84',boxLabel: '其它符号',inputValue :'4'}]
				    }]
			    }]
			},{
			    title: '口令不重复长度策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag9',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
			        xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
				        xtype: 'displayfield',
				        value: '口令禁止重复的字符数'
				    },{
				        name : 'hours',
				        id:'TacticsDetail9',
				        xtype: 'numberfield',
				        width: 20,
				        maxValue :99,//最大值
				        allowNegative:false,//正数
                        decimalPrecision:0,//整数
                        allowBlank: true
				    },{
				        xtype: 'displayfield',
				        value: '(例如111111， aaaaaa)'
				    }]
			    }]
			},{
			    title: '口令连续长度策略',
			    xtype: 'fieldset',
			    id:'TacticsFlag10',
			    animCollapse :true,
			    checkboxToggle: true,
			    height: 65,
			    items:[{
				    xtype: 'compositefield',
				    fieldLabel: '',
				    combineErrors: false,
				    items: [{
                       xtype: 'displayfield',
                       value: '口令禁止连续的字符数'
                    },{
                       name : 'hours',
                       id:'TacticsDetail10',
                       xtype: 'numberfield',
                       width: 20,
                       maxValue :99,//最大值
                       allowNegative:false,//正数
                       decimalPrecision:0,//整数
                       allowBlank: true
                    },{
                        xtype: 'displayfield',
                        value: '(例如12345， 54321)'
                    }]
			    }]
			}]
		}],
		buttons : [ {
		    text : '保存',
		    handler : function() {
//		    	var condition = qForm.getForm().getValues(false);
			    var flagArr = new Array();//启用标志
			    var saveStr = "";
			    for(var i=1;i<=11;i++){
			        if(Ext.getCmp('TacticsFlag'+i).collapsed)
			        {
			        	flagArr.push(2);//禁用策略
			        }else{
			        	flagArr.push(1);//启用策略
			        }
		        }
			    for(var i=1;i<=flagArr.length;i++){
		            switch (i) {
		                case 1:
							saveStr += '1,,3,';
							saveStr = saveStr+flagArr[i-1]+";";
							break;
						case 2:
							var detail = Ext.getCmp('TacticsDetail'+i).getValue();
							var action = Ext.getCmp('TacticsAction'+i).getValue();
							if((detail=="" || action=="")&& flagArr[i-1]==1){
							    Ext.Msg.alert('提示','请输入 强制修改口令策略 的参数值和执行动作');
							    return false;
							}
							saveStr = saveStr+'2,'+detail+','+action+',';
							saveStr = saveStr+flagArr[i-1]+";";
							break;
						case 3:
						    var action = Ext.getCmp('TacticsAction'+i).getValue();
						    if(action==""&& flagArr[i-1]==1){
						        Ext.Msg.alert('提示','请输入 登录IP策略 的执行动作');
						        return false;
						    }
						    saveStr = saveStr+'3,,'+action+',';
						    saveStr = saveStr+flagArr[i-1]+";";
						    break;
						case 4:
						    var detail = Ext.getCmp('TacticsDetail'+i).getValue();
							var action = Ext.getCmp('TacticsAction'+i).getValue();
							if((detail=="" || action=="")&& flagArr[i-1]==1){
								Ext.Msg.alert('提示','请输入 口令错误策略 的参数值和执行动作');
								return false;
							}
							saveStr = saveStr+'4,'+detail+','+action+',';
							saveStr = saveStr+flagArr[i-1]+";";
							break;
						case 5:
						    var isNo = Ext.getCmp('TacticsDetailyesNo5').getValue();
						    var beg = Ext.getCmp('TacticsDetailBeginTime5').getValue();
						    var end = Ext.getCmp('TacticsDetailEndTime5').getValue();
//								if(detail==""&& flagArr[i-1]==1)
//								{
//									Ext.Msg.alert('提示','请输入 口令重复策略 的参数值');
//									return false;
//								}
						    var detail =isNo+"/"+beg+"/"+end;
						    saveStr = saveStr+'5,'+detail+',,';
						    saveStr = saveStr+flagArr[i-1]+";";
						    break;
						case 6:
						    var detail = Ext.getCmp('TacticsDetail'+i).getValue();
						    if(detail==""&& flagArr[i-1]==1){
						        Ext.Msg.alert('提示','请输入 口令重复策略 的参数值');
						        return false;
						    }
						    saveStr = saveStr+'6,'+detail+',,';
						    saveStr = saveStr+flagArr[i-1]+";";
						    break;
						case 7:
						    var detail = Ext.getCmp('TacticsDetail'+i).getValue();
						    if(detail==""&& flagArr[i-1]==1){
						        Ext.Msg.alert('提示','请输入 口令长度策略 的参数值');
						        return false;
						    }
						    saveStr = saveStr+'7,'+detail+',,';
						    saveStr = saveStr+flagArr[i-1]+";";
						    break;
						case 8:
						    var detail = Ext.getCmp('TacticsDetail'+i).getValue();
						    var temp_td8 =""; 
						    if(detail.length==0&& flagArr[i-1]==1){
						        Ext.Msg.alert('提示','请勾选 口令复杂度策略 的参数值');
						        return false;
						    }
						    for(var j = 0;j<detail.length;j++){
						        if(j==0)
								temp_td8 = temp_td8+detail[j].inputValue;
							else
								temp_td8 = temp_td8+'/'+detail[j].inputValue;
						    }
						    saveStr = saveStr+'8,'+temp_td8+',,';
						    saveStr = saveStr+flagArr[i-1]+";";
						    break;
						case 9:
							var detail = Ext.getCmp('TacticsDetail'+i).getValue();
							if(detail==""&& flagArr[i-1]==1){
								Ext.Msg.alert('提示','请输入 口令不重复长度策略 的参数值');
								return false;
							}
							saveStr = saveStr+'9,'+detail+',,';
							saveStr = saveStr+flagArr[i-1]+";";
							break;
						case 10:
						    var detail = Ext.getCmp('TacticsDetail'+i).getValue();
						    if(detail=="" && flagArr[i-1]==1){
						        Ext.Msg.alert('提示','请输入 口令连续长度策略 的参数值');
						        return false;
						    }
						    saveStr = saveStr+'10,'+detail+',,';
						    saveStr = saveStr+flagArr[i-1]+";";
						    break;
						case 11:
						    var detail = Ext.getCmp('TacticsDetail'+i).getValue();
							var action = Ext.getCmp('TacticsAction'+i).getValue();
							if((detail=="" || action=="")&& flagArr[i-1]==1){
								Ext.Msg.alert('提示','请输入 在线用户数策略 的参数值和执行动作');
								return false;
							}
							saveStr = saveStr+'11,'+detail+','+action+',';
							saveStr = saveStr+flagArr[i-1]+";";
							break;
						default: break;
		            }
		        }
		        Ext.Ajax.request({
				    url:basepath+'/userApproveTactics!saveData.json',
				    method: 'POST',
				    params : {
						'saveStr' : saveStr
					},
					success : function(response) {
						Ext.Msg.alert('提示', '保存成功');
						store.reload();
					},
					failure : function(response) {
						Ext.Msg.alert('提示', response.responseText);
					}
		        });
		    }
		},{
		    text : '重置',
		    handler : function() {
		        qForm.getForm().reset();
				store.reload();
		    }
		}]
});
		
var record = Ext.data.Record.create([ {
	name : 'ID',
	mapping : 'ID'
}, {
	name : 'NAME',
	mapping : 'NAME'//策略名称
},{
	name : 'ENABLE_FLAG',
	mapping : 'ENABLE_FLAG'//启用状态
},{
	name : 'DETAIL',
	mapping : 'DETAIL'//策略明细
},{
	name : 'ACTIONTYPE',
	mapping : 'ACTIONTYPE'//执行动作
}]);

/**
 * 数据存储
 */
var store = new Ext.data.Store({
	restful : true,
	proxy : new Ext.data.HttpProxy({
		url : basepath + '/userApproveTacticsQuery.json'
	}),
	reader : new Ext.data.JsonReader({
		successProperty : 'success',
		idProperty : 'F_ID',
		messageProperty : 'message',
		root : 'json.data',
		totalProperty : 'json.count'
	}, record)
});
store.on('load',function(store,records,options){
	var dataArr = store.data.items;
	for(var i=0;i<dataArr.length;i++)
	{
		var _id = dataArr[i].data.ID;
		var rec = dataArr[i].data;
		if(rec.ENABLE_FLAG == 2)
			Ext.getCmp('TacticsFlag'+(_id-0)).collapse(true);//initialConfig;
		if(rec.ENABLE_FLAG == 1)
			Ext.getCmp('TacticsFlag'+(_id-0)).expand();
		switch (_id-0) {
			case 1:
				break;
			case 2:
				Ext.getCmp('TacticsDetail2').setValue(rec.DETAIL);
				Ext.getCmp('TacticsAction2').setValue(rec.ACTIONTYPE);
				break;
			case 3:
				Ext.getCmp('TacticsAction3').setValue(rec.ACTIONTYPE);
				break;
			case 4:
				Ext.getCmp('TacticsDetail4').setValue(rec.DETAIL);
				Ext.getCmp('TacticsAction4').setValue(rec.ACTIONTYPE);
				break;
			case 5:
				var arrLst = rec.DETAIL.split('/');
				Ext.getCmp('TacticsDetailyesNo5').setValue(arrLst[0]);
				Ext.getCmp('TacticsDetailBeginTime5').setValue(arrLst[1]);
				Ext.getCmp('TacticsDetailEndTime5').setValue(arrLst[2]);
//							Ext.getCmp('TacticsDetail4').setValue(rec.DETAIL);
				break;
			case 6:
				Ext.getCmp('TacticsDetail6').setValue(rec.DETAIL);
				break;
			case 7:
				Ext.getCmp('TacticsDetail7').setValue(rec.DETAIL);
				break;
			case 8:
				var temp = rec.DETAIL.split('/');
				for(var j=0;j<temp.length;j++)
				{
					if(Ext.getCmp('td8'+temp[j])!= undefined)
						Ext.getCmp('td8'+temp[j]).setValue(1);
				}
				
//							Ext.getCmp('TacticsDetail8').setValue(rec.DETAIL);
//							Ext.getCmp('TacticsAction2').setValue(rec.ACTIONTYPE);
				break;
			case 9:
				Ext.getCmp('TacticsDetail9').setValue(rec.DETAIL);
				break;
			case 10:
				Ext.getCmp('TacticsDetail10').setValue(rec.DETAIL);
				break;
			case 11:
				Ext.getCmp('TacticsDetail11').setValue(rec.DETAIL);
				Ext.getCmp('TacticsAction11').setValue(rec.ACTIONTYPE);
				break;
			default:
				break;
		}
	}
});
store.load();

//结果域扩展功能面板
var customerView = [{
	/**
	 * 自定义用户认证策略面板
	 */
	title:'用户认证策略面板',
	hideTitle: true,
	items: [qForm]
}];