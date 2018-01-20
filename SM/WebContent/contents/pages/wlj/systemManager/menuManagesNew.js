/**
 * @description 菜单项管理
 * @author helin
 * @since 2014-06-30
 */
imports([
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js'
]);

//初始化提示框
Ext.QuickTips.init();
var addUpdate = '0';//用来区分是‘新增’还是‘修改’操作的标志，‘0’表示新增，‘1’表示修改
//本地数据字典定义
var localLookup = {
	'IF_FLAG' : [  //是否
		{key : '1',value : '是'},
	    {key : '0',value : '否'}
	]
};

//树配置查询类型
var treeLoaders = [{
	key : 'MENU_TREE_LOADER_KEY',	//菜单树loader
	url : basepath + '/menuInit.json',
	parentAttr : 'PARENT_ID',
	locateAttr : 'ID',
	rootValue : '0',
	textField : 'NAME',
	idProperties : 'ID',
	jsonRoot:'json.data'
},{
	key : 'MODULE_TREE_LOADER_KEY',  //模块树loader
	url : basepath + '/fwFunction-Action.json',
	parentAttr : 'PARENT_ID',
	locateAttr : 'ID',
	rootValue : '0',
	textField : 'NAME',
	idProperties : 'ID',
	jsonRoot:'json.data'
}];
var treeCfgs = [{
	key : 'MENU_TREE_KEY',
	loaderKey : 'MENU_TREE_LOADER_KEY',
	autoScroll:true,
	title : '菜单维护',
	rootCfg : {
		expanded:true,
		id:'root',
		text:'主菜单',
		autoScroll:true,
		children:[]
	},
	tbar : [{
	    text : '新增',
	    handler : function() {//点击新增按钮的时候所触发的事件
			addUpdate = '0';
			sel = leftMenuTree.getSelectionModel().getSelectedNode();
			menuForm.getForm().reset();
			if(sel){
				if(sel.attributes.id == 'root'){
					Ext.getCmp('parentId').setValue('0');
					Ext.getCmp('parentName').setValue(sel.attributes.text);
				}else{
					Ext.getCmp('parentId').setValue(sel.attributes.ID);
					Ext.getCmp('parentName').setValue(sel.attributes.NAME);
				}
			}
			Ext.getCmp('appId').setValue('62');
			Ext.getCmp('crtDate').setValue(new Date());
			Ext.getCmp('_issamewin').setValue('0');
			Ext.getCmp('funcName').getValue();
//			if(sel.attributes.ICON != undefined ){//新增时移除显示的图标
//				if(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes.length>1){
//					Ext.ComponentMgr.all.map.icon.el.dom.parentNode.removeChild(
//							Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes[1]);
//				}
//			}
	    }
	},{
	    text : '删除',
	    handler : function() {
			var record = leftMenuTree.getSelectionModel().getSelectedNode();
			var sel =  leftMenuTree.root.findChild('id', record.id, true);
			Ext.Msg.confirm('请确认','<b>提示!:</b><span  style="color:red" >删除该菜单项的同时将删掉其子菜单,请慎重! </span> <br/>继续删除吗?',
				function(btn, text) {
					if (btn == 'no') {
						return;
					}
					//当确认删除时，获取该节点及其节点下的所有子节点的ID值
					Ext.Ajax.request({
						url : basepath + '/CntMenu-action!batchDestroy.json',
						method : 'POST',
						params : {
							'idStr' : sel.id
						},
						waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
						scope : this,
						success : function() {
							if(record.attributes.PARENT_ID == '0'){
								leftMenuTree.root.removeChild(record,true);
								rightMenuTree.root.removeChild(record,true);
							}else{
								leftMenuTree.deleteNode(record);
								rightMenuTree.deleteNode(record);
							}
							menuForm.getForm().reset();
							if(Ext.getCmp('icon').getValue() != undefined){//新增时移除显示的图标
								if(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes.length>1){
									Ext.ComponentMgr.all.map.icon.el.dom.parentNode.removeChild(
											Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes[1]);
								}	
							}
						},	
						failure : function() {
							Ext.Msg.alert('提示', '操作失败');
						}
					});
				}
			);
			
	    }
	}],
	clickFn : function(node){
		sel = leftMenuTree.getSelectionModel().getSelectedNode();
		Ext.getCmp('id').setValue((node.attributes.ID));
		Ext.getCmp('name').setValue((node.text));
		if(node.attributes.ICON != undefined  ){
			var test2 = Ext.util.Format.substr(node.attributes.ICON,0,1);//验证图片路径是否规范，若不规范则只显示值，不显示图标
			if(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes.length>1){
				Ext.ComponentMgr.all.map.icon.el.dom.parentNode.removeChild(
						Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes[1]);
			}
			if(test2 == '/'){
				var tImp = document.createElement('img');
				tImp.src = basepath+'/contents'+node.attributes.ICON;
				Ext.ComponentMgr.all.map.icon.el.dom.parentNode.appendChild(tImp);
			}
			Ext.getCmp('icon').setValue((node.attributes.ICON));
		}else {
			if(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes.length>1){
				Ext.ComponentMgr.all.map.icon.el.dom.parentNode.removeChild(
						Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes[1]);
			}
			Ext.getCmp('icon').setValue(node.attributes.ICON);
		}
		
		Ext.getCmp('order').setValue((node.attributes.ORDER_));
		Ext.getCmp('_issamewin').setValue((node.attributes.ISSAMEWIN));
		Ext.getCmp('modFuncId').setValue((node.attributes.MOD_FUNC_ID));
		Ext.getCmp('funcName').setValue((node.attributes.FUNC_NAME));
		Ext.getCmp('appId').setValue((node.attributes.APP_ID));
		Ext.getCmp('crtDate').setValue((node.attributes.CRT_DATE));

		if(node.attributes.PARENT_ID == '0'){
			Ext.getCmp('parentId').setValue((node.attributes.PARENT_ID));
			Ext.getCmp('parentName').setValue(('主菜单 '));
		}else{
			Ext.getCmp('parentId').setValue((node.attributes.PARENT_ID));
			Ext.getCmp('parentName').setValue((node.attributes.PARENT_NAME));
		}
		addUpdate = '1';//代表进行修改操作
	}
},{
	key : 'RIGHT_MENU_TREE_KEY',
	loaderKey : 'MENU_TREE_LOADER_KEY',
	autoScroll:true,
	title: '上层菜单',
	rootCfg : {
		expanded:true,
		id:'root',
		text:'主菜单',
		autoScroll:true,
		children:[]
	},
	clickFn : function(node){
		Ext.getCmp('parentName').setValue(node.text);
		Ext.getCmp('parentId').setValue(node.id);
	}
},{
	key : 'MODULE_TREE_KEY',
	loaderKey : 'MODULE_TREE_LOADER_KEY',
	autoScroll:true,
	title: '模块功能选择',
	rootCfg : {
		expanded:true,
		id:'root',
		text:'模块功能',
		autoScroll:true,
		children:[]
	},
	clickFn : function(node){
		if(node.attributes.ACTION == ''){
			Ext.MessageBox.alert('提示', '不能选择根节点,请重新选择 !');
			return;
		}else{
			Ext.getCmp('funcName').setValue(node.attributes.NAME);
			Ext.getCmp('modFuncId').setValue(node.attributes.ID);
		}
	}
}];

var needGrid = false;
WLJUTIL.suspendViews=false;  //自定义面板是否浮动

var fields = [
	{name: 'TEST',text:'此文件fields必须要有一个无用字段', resutlWidth:80}
];

var menuForm = new Ext.FormPanel({
	title: '菜单设置',
	buttonAlign: 'center',
	items: [
		{id:'id',name: 'id',xtype:'textfield',fieldLabel: 'ID',hidden : true,anchor:'90%'},
		{id:'name',name: 'name',xtype:'textfield',fieldLabel: '名称<font color=red>*</font>',allowBlank: false,anchor:'90%'},
		{id:'icon',name: 'icon',xtype:'textfield',fieldLabel: '图标<font color=red>*</font>',allowBlank: true,readOnly:true,anchor:'90%',hidden:true,
			listeners:{//监控事件，当鼠标点击到该field的时候自动打开右侧相应的页签
				focus:function(c){
					var iconShow = menuForm.getForm().findField('parentId').getValue();
					if(iconShow == '0'){
						getEdgePanel('right').layout.setActiveItem(mainIconView);
					}else{
						getEdgePanel('right').layout.setActiveItem(iconView);
					}
				}
			}
		},
		{id:'order',name:'order',xtype:'numberfield',fieldLabel: '排序<font color=red>*</font>',allowBlank: false,anchor:'90%'},
		{id : '_issamewin',name : 'issamewin',xtype : 'combo',fieldLabel: '新窗口打开',editable : false,mode : 'local',
			triggerAction:'all',store:new Ext.data.Store(),valueField:'key',displayField:'value',anchor:'90%'},
		{id:'modFuncId',name:'modFuncId',xtype:'textfield',fieldLabel: '功能模块选择',hidden:true,anchor:'90%'},
		{
			layout:'column',
			items:[{
				columnWidth:.85,
				layout: 'form',
				items: [
					{id:'funcName',name:'funcName',xtype : 'textfield',fieldLabel: '功能模块选择',hideTrigger:false,readOnly : true,anchor:'98%',
						listeners:{//监控事件，当鼠标点击到该field的时候自动打开右侧相应的页签
							focus:function(a){
								getEdgePanel('right').layout.setActiveItem(moduleTree);
							}
						}
					}]
			},{
				columnWidth:.15,
				layout: 'form',
				items: [
					{text:'清空',xtype:'button',width: 45,handler:function(){
							Ext.getCmp('funcName').setValue('');
							Ext.getCmp('modFuncId').setValue('');
						}
					}
				]
			}]
		},
		{id:'parentId',name:'parentId',xtype:'textfield',readOnly:true,fieldLabel: '上层菜单选择',anchor:'90%',hidden : true},
		{id:'parentName',name:'parentName',xtype:'textfield',fieldLabel: '上层菜单选择<font color=red>*</font>',readOnly:true,allowBlank : false,anchor:'90%',
			listeners:{//监控事件，当鼠标点击到该field的时候自动打开右侧相应的页签
				focus:function(b){
					getEdgePanel('right').layout.setActiveItem(rightMenuTree);
				}
			}
		},
		{id : 'appId',name : 'appId',xtype : 'textfield',fieldLabel : '逻辑ID',anchor :'90%',hidden : true},
		{id : 'crtDate',name : 'crtDate',xtype : 'datefield',format : 'Y-m-d',fieldLabel : '日期',readOnly : true,hidden : true,anchor : '90%'},
		{id : 'leafFlag',name : 'leafFlag',xtype : 'textfield',fieldLabel : '是否是叶子节点',hidden : true,anchor : '90%'}
	],
	buttons:[{
		id:'saveButt',
		text:'保存',
		handler:function(){
			if(!menuForm.getForm().isValid()){
				Ext.Msg.alert('提示','输入有误或存在漏输项,请检查！');
				return false;
			}
			var lf = Ext.getCmp('funcName').getValue();
			if(lf != ''){
				Ext.getCmp('leafFlag').setValue('1');
			}else{
				Ext.getCmp('leafFlag').setValue('0');

			}
			if(addUpdate == '1'){
		        //点击修改按钮的时候所触发的事件
				var selectNode = leftMenuTree.getSelectionModel().getSelectedNode();//获取当前所选的记录
				if(selectNode.id == 'root'){
					Ext.MessageBox.alert('提示', '根节点不能修改 !');
					return false;
				}
				if(Ext.getCmp("parentId").getValue() == Ext.getCmp('id').getValue()){
					Ext.Msg.alert('提示', '当前菜单不能做为上层菜单!否则会出现未知错误');
					return false;
				}
			}
			Ext.getCmp('saveButt').setDisabled(true);
			Ext.getCmp('cancelButt').setDisabled(true);
			//新增成功后将新添加的节点显示到该树上
			Ext.Ajax.request({
				url : basepath + '/CntMenu-action.json',
				method : 'POST',
				params : menuForm.getForm().getFieldValues(),
				waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
				scope : this,
				success : function() {
					Ext.Msg.alert('提示', '操作成功');
					menuForm.getForm().reset();
					//{}括号括起来的表示重置需要执行的操作，执行为新增后的状态
					{
						addUpdate = '0';
						Ext.getCmp('appId').setValue('62');
						Ext.getCmp('crtDate').setValue(new Date());
						Ext.getCmp('_issamewin').setValue('0');
						Ext.getCmp('funcName').getValue();
//						if(sel.attributes.ICON != undefined ){//新增时移除显示的图标
//							if(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes.length>1){
//								Ext.ComponentMgr.all.map.icon.el.dom.parentNode.removeChild(
//										Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes[1]);
//							}
//						}
					}
					Ext.Ajax.request({//重新加载菜单树
						url : basepath + '/menuInit.json',
						method : 'GET',
						success : function(response) {
							var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
							rightMenuTree.resloader.nodeArray = nodeArra;//重新获取后台数据
							rightMenuTree.resloader.refreshCache(); // 刷新缓存
							var children = rightMenuTree.resloader.loadAll(); //得到相应的树数据
							leftMenuTree.root.removeAll(true); // 清掉以前的数据
							leftMenuTree.appendChild(children);// 把数据重新填充
							
//							Ext.each(nodeArra,function(n){
//								if(n.ICON){
//									var test = Ext.util.Format.substr(n.ICON,0,1);
//									if(test=='/'){
//										n.icon = basepath+'/contents'+n.ICON;//对从台读取图标数据进行处理，变成绝对路径，那样才能显示图标
//									}else{
//										n.icon = '';
//									}
//								}
//							});
//							leftMenuTree.resloader.nodeArray = nodeArra;//重新获取后台数据
//							leftMenuTree.resloader.refreshCache(); // 刷新缓存
//							var children = leftMenuTree.resloader.loadAll(); //得到相应的树数据
//							leftMenuTree.root.removeAll(true); // 清掉以前的数据
//							leftMenuTree.appendChild(children);// 把数据重新填充
						}
					});
					Ext.getCmp('saveButt').setDisabled(false);
					Ext.getCmp('cancelButt').setDisabled(false);
				},
				failure : function() {
					Ext.Msg.alert('提示', '操作失败');
				}
			});
		}
	},{
		id:'cancelButt',
		text:'重置',
		handler:function(){//点击重置按钮的时候，面板清空，所有按钮置灰，移除显示的图标
			menuForm.getForm().reset();
			if(Ext.getCmp('icon').getValue() != undefined){//取消后移除显示的图标
				if(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes.length>1){
					Ext.ComponentMgr.all.map.icon.el.dom.parentNode.removeChild(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes[1]);
				}
			}
		}
	}]
});

//结果域扩展功能面板
var customerView = [{
	/**
	 * 自定义菜单维护面板
	 */
	title:'菜单设置',
	hideTitle: true,
	items: [menuForm]
}];


var leftMenuTree = TreeManager.createTree('MENU_TREE_KEY');
var moduleTree = TreeManager.createTree('MODULE_TREE_KEY');
var rightMenuTree = TreeManager.createTree('RIGHT_MENU_TREE_KEY');

/**
 * 图片icon的右侧展示时所用到的静态数据
 * @author songxs
 * @since 2012-9-23
 */
var iconData = [	//id--图标的路径
    ['/contents/images/fw/icon_menu_management.gif'],
    ['/contents/images/fw/icon_menu_menu_management.gif'],
    ['/contents/images/fw/icon_menu_report_management.gif'],
    ['/contents/images/fw/icon_menu_template_manageme.gif'],
    ['/contents/images/fw/icon_menu_182.gif'],
    ['/contents/images/fw/icon_menu_016.gif'],
    ['/contents/images/fw/icon_menu_072.gif'],
    ['/contents/images/fw/icon_menu_219.gif'],
    ['/contents/images/fw/icon_menu_202.gif'],
    ['/contents/images/fw/icon_menu_075.gif'],
    ['/contents/images/fw/icon_menu_093.gif'],
    ['/contents/images/fw/icon_menu_284.gif'],
    ['/contents/images/fw/icon_menu_274.gif'],
    ['/contents/images/fw/icon_menu_065.gif'],
    ['/contents/images/fw/icon_menu_071.gif'],
    ['/contents/images/fw/icon_menu_248.gif'],
    ['/contents/images/fw/icon_menu_285.gif'],
    ['/contents/images/fw/icon_menu_286.gif'],
    ['/contents/images/fw/icon_menu_027.gif'],
    ['/contents/images/fw/icon_menu_107.gif'],
    ['/contents/images/fw/icon_menu_108.gif'],
    ['/contents/images/fw/icon_menu_157.gif'],
    ['/contents/images/fw/icon_menu_229.gif'],
    ['/contents/images/fw/icon_menu_304.gif'],
    ['/contents/images/fw/icon_menu_311.gif'],
    ['/contents/images/fw/icon_menu_020.gif'],
    ['/contents/images/fw/icon_menu_156.gif'],
    ['/contents/images/fw/icon_menu_326.gif'],
    ['/contents/images/fw/icon_menu_325.gif'],
    ['/contents/images/fw/icon_menu_020.gif'],
    ['/contents/images/fw/icon_menu_027.gif'],
    ['/contents/images/fw/icon_menu_041.gif'],
    ['/contents/images/fw/icon_menu_051.gif'],
    ['/contents/images/fw/icon_menu_072.gif'],
    ['/contents/images/fw/icon_menu_190.gif'],
    ['/contents/images/fw/icon_menu_254.gif'],
    ['/contents/images/fw/icon_menu_255.gif']
];
var iconData_1 = [
  ['1','0px -103px'],
  ['2','-81px -103px'],
  ['3','-162px -103px'],
  ['4','-243px -103px'],
  ['5','-324px -103px'],
  ['6','-405px -103px'],
  ['7','-486px -103px'],
  ['8','-567px -103px'],
  ['9','-648px -103px'],
  ['10','-729px -103px'],
  ['11','-810px -103px']
];
var iconDataStore = new Ext.data.ArrayStore({//右侧图片选择时展示所用的store
	proxy : new Ext.data.MemoryProxy(),
	fields : ['id'],
	sortInfo: {
		field : 'id',
		direction: 'ASC'
	}
});
var iconDataMainStore = new Ext.data.ArrayStore({//右侧图片选择时展示所用的store
	proxy : new Ext.data.MemoryProxy(),
	fields : ['id','backGroundP'],
	sortInfo: {
		field : 'id',
		direction: 'ASC'
	}
});

iconDataStore.loadData (iconData);//右侧图标展示时加载静态数据
iconDataMainStore.loadData (iconData_1);//右侧图标展示时加载静态数据
/**
 * @constructor 当点击右侧展示的图标的时候在panel上显示相应的图标
 * @param id: id是指静态数组
 * @return ;
 */
function setIcon(id){
	Ext.ComponentMgr.all.map.icon.setValue(Ext.getCmp('icon').setValue(id));
	Ext.getCmp('icon').setValue(Ext.util.Format.substr(id,9,100));
	var tImp = document.createElement('img');
	tImp.src = basepath+id;
	if(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes.length>1){
		Ext.ComponentMgr.all.map.icon.el.dom.parentNode.removeChild
		(Ext.ComponentMgr.all.map.icon.el.dom.parentNode.childNodes[1]);
	}
	Ext.ComponentMgr.all.map.icon.el.dom.parentNode.appendChild(tImp);
} 
function setMainIcon(id){
	Ext.getCmp('icon').setValue(id);
}

var dataView = new Ext.DataView({//右侧图标展示的样式(包括图标的排版)
	store: iconDataStore,
	id: 'iconss',
	tpl  : new Ext.XTemplate(
			'<ul>',
			'<tpl for=".">',
			'<li class="icon" onclick="setIcon(\'{[values.id]}\')" style="float:left;margin-left:12px;margin-right:12px;margin-top:10px;">',
			'<img src= "'+basepath+'/{[values.id]}" />',
			'</li>',
			'</tpl>',
			'</ul>'
	),
	itemSelector: 'li.icon',
	overClass   : 'phone-hover',
	singleSelect: true,
	autoScroll  : true
});
var dataViewMain = new Ext.DataView({//右侧图标展示的样式(包括图标的排版)
	store: iconDataMainStore,
	id: 'iconss_1',
	tpl  : new Ext.XTemplate(
			'<ul>',
			'<tpl for=".">',
			'<a href="#" onclick="setMainIcon(\'{[values.id]}\')" style="display:block;float:left;margin-left:12px;margin-right:12px;margin-top:10px;	padding:62px 0px 0px 0px;background:url('+basepath+'/contents/images/blue/navbg.gif) no-repeat;background-position:-567px 0px;width:81px;height:22px;background-position:{[values.backGroundP]};"></a>',
			'</tpl>',
			'</ul>'
	),
	overClass   : 'phone-hover',
	singleSelect: true,
	autoScroll  : true
});

var iconView = new Ext.Panel({//右侧图标展示的Panel
	title: '图标',
	id :'iconView',
	layout: 'fit',
	items : dataView,
	height: 700,
	width: 300
});
	
var mainIconView = new Ext.Panel({
	title: '一级菜单图标',
	id :'mainIconView',
	layout: 'fit',
	items : dataViewMain,
	height: 700,
	width: 300
});

//边缘面板配置
var edgeVies = {
	left : {
		width : 300,
		layout : 'fit',
		collapsible : false,
		items : [leftMenuTree]
	},
	right : {
		width : 300,
		collapsible : false,
		items : [moduleTree,rightMenuTree]//,mainIconView,iconView]
	}
};

/**
 * 结果域面板滑入后触发,系统提供listener事件方法
 */
var viewshow = function(theview){
	if(theview._defaultTitle == '菜单设置'){
		menuForm.getForm().findField('issamewin').bindStore(findLookupByType('IF_FLAG'));
		//不需要展示图标，故不用再次请求
//		Ext.Ajax.request({//重新加载菜单树
//			url : basepath + '/menuInit.json',
//			method : 'GET',
//			success : function(response) {
//				var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
//				Ext.each(nodeArra,function(n){
//					if(n.ICON){
//						var test = Ext.util.Format.substr(n.ICON,0,1);
//						if(test=='/'){
//							n.icon = basepath+'/contents'+n.ICON;//对从台读取图标数据进行处理，变成绝对路径，那样才能显示图标
//						}else{
//							n.icon = '';
//						}
//					}
//				});
//				leftMenuTree.resloader.nodeArray = nodeArra;//重新获取后台数据
//				leftMenuTree.resloader.refreshCache(); // 刷新缓存
//				var children = leftMenuTree.resloader.loadAll(); //得到相应的树数据
//				leftMenuTree.root.removeAll(true); // 清掉以前的数据
//				leftMenuTree.appendChild(children);// 把数据重新填充
//			}
//		});
	}
};

