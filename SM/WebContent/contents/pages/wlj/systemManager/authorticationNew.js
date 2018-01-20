/**
 * @description 角色功能授权
 * @author helin
 * @since 2014-07-01
 */
imports([
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js'
]);

//自定义变量定义
var roleNameGloble = '';// 选择的角色名称 全局变量
var roleCodeGloble = '';// 选择的角色编码 全局变量
var menuCodeGloble = '';// 选择的菜单编码 全局变量
var selectNode = ''; 	// 选择的角色编码树节点

var delGrantsMap = [];// del功能按钮编码
var addGrantsMap = [];// add功能按钮编码
var delGrantsStr = '';// del功能按钮编码转换的字符串
var addGrantsStr = '';// add功能按钮编码转换的字符串

var delStr = [];// 菜单项删除数组
var addStr = [];// 菜单项新增数组
var delMenuStr = '';// 菜单项删除数组转换的字符串
var addMenuStr = '';// 菜单项新增数组转换的字符串

var treeLoader = '';	//选择的角色加载的授权数据
var responseTemp = {};	//暂存角色菜单权限，提供给‘重复制’按钮及重置按钮使用

//遮罩层定义
var lm = new Ext.LoadMask (document.body,{
   	msg : '正在加载菜单数据,请稍等...'
});
lm.show();

//全局变量定义
var needGrid = false;
WLJUTIL.suspendViews=false;  //自定义面板是否浮动

//页面主工作区域所涉及到的字段申明;该申明,将被运用于查询面板、结果列表、增删查改的展示、逻辑运算等;
var fields = [
	{name: 'TEST',text:'此文件fields必须要有一个无用字段', resutlWidth:80}
];

//树配置查询类型
var treeLoaders = [{
	key : 'ROLE_TREE_LOADER_KEY',	//角色树loader
	url : basepath + '/roleManagerQuery!getAuthRoles.json',
	parentAttr : 'PARENT_ID',
	locateAttr : 'ID',
	rootValue : '0',
	textField : 'ROLE_NAME',
	idProperties : 'ID',
	jsonRoot:'json.data'
},{
	key : 'MENU_TREE_LOADER_KEY',  //菜单功能树loader
	url : basepath + '/menuInitAuthortication.json',
	parentAttr : 'PARENT_ID',
	locateAttr : 'ID',
	rootValue : '0',
	textField : 'COUNTNAME',
	idProperties : 'ID',
	jsonRoot:'json.data',
	callback:function(){
		menuTree.expandAll();//默认展开树，解决前台取不到子节点，从而不能递归check问题
	}
}];
//树形面板对象预配置
var treeCfgs = [{
	key : 'ROLE_TREE_KEY',
	loaderKey : 'ROLE_TREE_LOADER_KEY',
	autoScroll:true,
	rootVisible: false,
	title : '角色栏',
	rootCfg : {
		expanded:true,
		id:'root',
		text:'所有角色',
		autoScroll:true,
		children:[]
	},
	tbar : [{
	    text : '导出授权',
	    handler : function() {
			var rolesStr = '';
			var roleArr  = roleTree.resloader.nodeArray;
			for(var i = 0; i < roleArr.length; i++) {
				if (rolesStr.length == 0) {
					rolesStr = '\''+roleArr[i].ID+'\'';
				} else {
					rolesStr += ',\''+roleArr[i].ID+'\'';
				}
			}
			Ext.Ajax.request({
				url:basepath + '/menuInitAuthortication!exportAuthInfo.json?rolesStr='+rolesStr,
				method:'GET',
				success:function(response){
					var res = Ext.util.JSON.decode(response.responseText);
					if(res.filename){
                    	window.location.href = basepath+'/FileDownload?filename='+res.filename;
                    }
				},
				failure:function(){
					Ext.Msg.alert('提示','导出授权信息操作失败！');
				}
			});
	    }
	},{
	    text : '复制授权',
	    qtips: '复制授权(功能/数据权限)',
	    handler : function() {
	    	if(roleCodeGloble == undefined || roleCodeGloble == "" || roleCodeGloble == null){//未选择角色编码时，提示必选信息
				Ext.Msg.alert('提示','请选择角色！');
				return ;
			}
			copyRoleWin.setTitle("复制角色: ["+ roleNameGloble+"] 权限到已有角色");
			copyRoleWin.show();
	    }
	}],
	clickFn : function(node){
		//选择任意角色，在菜单栏中进行相应数据勾选操作
		if(node.id != 'root'){
			selectNode = node;
			roleCodeGloble = node.attributes.ID;
			roleNameGloble = node.attributes.ROLE_NAME;
			Ext.Ajax.request({
				url:basepath + '/roleMenuQuery.json?roleId='+node.attributes.ID,
				method:'GET',
				success:function(response){
					responseTemp = response;// 暂存角色菜单权限，提供给‘重复制’按钮使用
					refreshCheckItem(response);
				},
				failure:function(){
					Ext.Msg.alert('提示','查询角色功能授权失败！');
				}
			});
		}else{
			selectNode = undefined;
			roleCodeGloble = '';
			roleNameGloble = '';
		}
	}
},{
	key : 'MENU_TREE_KEY',
	loaderKey : 'MENU_TREE_LOADER_KEY',
	autoScroll:true,
	title: '菜单功能栏',
	rootVisible: false,
	checkBox:true,
	loadMaskWorking : true,
	expandedCount : 0,
	rootCfg : {
		expanded:true,
		id:'root',
		text:'菜单功能',
		autoScroll:true,
		children:[]
	},
	tbar : [{
	    text : '保存',
	    handler : function() {
	    	saveSet();
	    }
	},{
	    text : '清空',
	    handler : function() {
	    	for(var i=0;i<menuTree.root.childNodes.length;i++){//清空原角色选择情况
				menuTree.root.childNodes[i].fireEvent('checkchange',menuTree.root.childNodes[i],false,undefined);
			}
	    }
	},{
	    text : '重置',
	    handler : function() {
	    	if(responseTemp != undefined){
				refreshCheckItem(responseTemp);
	    	}
	    }
	}],
	listeners:{
		/**
		 * 展开节点事件,用于计算展开结点总数隐藏遮罩
		 * @param {} node
		 */
		'expandnode':function(node){
			if(!this.loadMaskWorking){
				return;
			}
			this.expandedCount++;
			if(this.expandedCount >= this.resloader.nodeArray.length){
				lm.hide();
				this.loadMaskWorking = false;
				return;
			}
		},
		/**
		 * 重写树节点checkchange事件
		 * @param {} node	树节点
		 * @param {} checked 是否选中
		 * @param {} source 自定义来源,undefined 自身触发的checkchange事件,1手动触发子节点checkchange事件,2手动触发父节点checkchange事件
		 */
		'checkchange' : function(node, checked, source) {
			if (treeLoader === '') {
        		Ext.Msg.alert('提示','请选择授权角色');
        		return false;
			}
			if (node.attributes.NODETYPE == '0') {
				if(checked){
					delStr.remove(node.id);
					if(addStr.indexOf(node.id)<0 && !includeControll(treeLoader, node.id)){
						addStr.push(node.id);
					}
				}else{
					addStr.remove(node.id);
					if(delStr.indexOf(node.id)<0 && includeControll(treeLoader, node.id)){
						delStr.push(node.id);
					}
				}
			}
			if (node.attributes.NODETYPE == '1') {
				if (checked) {
					delGrantsMap.remove(node);
					addGrantsMap.push(node);
				} else {
					delGrantsMap.push(node);
					addGrantsMap.remove(node);
				}
			}
			if(source==undefined){									//操作节点状态，并调用父节点和子节点事件
				node.getUI().checkbox.indeterminate=false;
				node.getUI().toggleCheck(checked);
        		if(node.childNodes){
        			Ext.each(node.childNodes,function(cn){
        				cn.fireEvent('checkchange',cn,checked,'1');//若存在子节点，则传递参数以触发相应checkChange监听事件
        			});
        		}
        		if(node.parentNode && node.parentNode !== this.root){
        			if (node.attributes.NODETYPE == '0') { 			//菜单项checkchange事件，手动触发父节点checkchange事件
        				node.parentNode.fireEvent('checkchange',node.parentNode,checked,'2');//若存在父节点，则传递参数以触发相应监听事件
					} else {
						//当是控制点选中事件时,触发上级事件,控制点取消事件时,不触发事件
						if (checked) {
        					node.parentNode.fireEvent('checkchange',node.parentNode,checked,'2');//若存在父节点，则传递参数以触发相应监听事件
        				}
					}
        		}
        	}else if(source == '1'){								//操作节点状态，并调用子节点事件
        		node.getUI().checkbox.indeterminate=false;
        		node.getUI().toggleCheck(checked);
        		if(node.childNodes){
        			Ext.each(node.childNodes,function(cn){
        				cn.fireEvent('checkchange',cn,checked,'1');//若存在子节点，则传递参数以触发相应checkChange监听事件
        			});
        		}
        	}else if(source == '2'){
        		if(node.childNodes && node.childNodes.length > 0){
        			var checkcount = 0;
        			var indeterminate = false;
        			for(var i=0; i<node.childNodes.length;i++){
        				if(node.childNodes[i].getUI().checkbox.indeterminate){
        					indeterminate = true;
        					break;
        				}
        				if(node.childNodes[i].getUI().checkbox.checked){
        					checkcount ++;
        				}
        			}
        			if(!indeterminate && checkcount==0){
        				node.getUI().checkbox.indeterminate = false;
        				node.getUI().toggleCheck(false);
        			}else if(indeterminate || checkcount < node.childNodes.length){
        				node.getUI().checkbox.indeterminate = true;
        				delStr.remove(node.id);
        				if(!includeControll(treeLoader, node.id) && addStr.indexOf(node.id) < 0){
        					addStr.push(node.id);
        				}
        			}else if(checkcount == node.childNodes.length){
        				node.getUI().checkbox.indeterminate = false;
        				node.getUI().toggleCheck(true);
        			}
        		}
        		if(node.parentNode && node.parentNode != this.root){//若存在父节点，则传递参数以触发相应监听事件
        			node.parentNode.fireEvent('checkchange',node.parentNode,checked,'2');
        		}
        	}
		}
	}
},{
	key : 'COPY_ROLE_TREE_KEY',
	loaderKey : 'ROLE_TREE_LOADER_KEY',
	autoScroll:true,
	//title : '复制权限到已有角色',
	rootVisible: false,
	checkBox:true,
	rootCfg : {
		expanded:true,
		id:'root',
		text:'所有角色',
		autoScroll:true,
		children:[]
	},
	tbar : [{
	    text : '复制到选定角色',
	    handler : function() {
	    	var selectNodes = copyRoleTree.getChecked();
	    	if(selectNodes.length < 1){
	    		Ext.Msg.alert("提示","请选择角色！");
	    		return false;
	    	}
	    	var roleIds = "";
	    	for(var i=0;i<selectNodes.length;i++){
	    		if(i == 0){
					roleIds += selectNodes[i].attributes.ID;
				} else {
					roleIds = roleIds + ',' + selectNodes[i].attributes.ID;
				}
	    	}
			Ext.Ajax.request({//执行保存设置//增量数据操作url
				url : basepath + '/roleManagerQuery!copyRoleToRole.json?oldRoleCode='+roleCodeGloble+'&toRoleCodes='+roleIds,
				method:'GET',
				success:function(response){
					Ext.Msg.alert('提示','操作成功！');
					copyRoleWin.hide();
				},
				failure:function(){
					Ext.Msg.alert('提示','操作失败！');
				}
			});
	    }
	},{
	    text : '关闭',
	    handler : function() {
	    	copyRoleWin.hide();
	    }
	}]
}];

//左则角色树
var roleTree = TreeManager.createTree('ROLE_TREE_KEY');
var menuTree = TreeManager.createTree('MENU_TREE_KEY');
var copyRoleTree = TreeManager.createTree('COPY_ROLE_TREE_KEY');

//结果域扩展功能面板
var customerView = [{
	/**
	 * 自定义菜单功能栏面板
	 */
	title:'菜单功能栏',
	hideTitle: true,
	items: [menuTree]
}];

//边缘面板配置
var edgeVies = {
	left : {
		width : 300,
		layout : 'fit',
		collapsible : false,
		items : [roleTree]
	}
};

/**
 * 重写Ext.tree.TreeNodeUI.toggleCheck方法，使其不再触发TreeNode的checkchange事件！
 * 仅限本页面使用。 
 */
Ext.override(Ext.tree.TreeNodeUI,{
	toggleCheck : function(value){
    	var cb = this.checkbox;
    	if(cb){
    		cb.checked = (value === undefined ? !cb.checked : value);
    	}
    	this.checkbox.defaultChecked = value;
    	this.node.attributes.checked = value;
	}	
});

/**
 * 根据菜单ID判断是否在数组arr中存在
 */
function includeControll(arr,id){
	for(var i=0;i<arr.length;i++){
		if(arr[i].RES_CODE==id){
			return true;
		}
	}
	return false;
};
	


/**
 * deal Controllers data
 */
function beforeSaveSet(){
	for(var i=0;i<delGrantsMap.length;i++){
		var obj = {};
		obj.menuCode = delGrantsMap[i].attributes.PARENT_ID;
		obj.opCode   = delGrantsMap[i].attributes.OPCODE;
		if (delGrantsStr.length == 0) {
			delGrantsStr = obj.menuCode + ' ' +obj.opCode;
		} else {
			delGrantsStr += ',' + obj.menuCode + ' ' +obj.opCode;
		}
	}
	for(var i=0;i<addGrantsMap.length;i++){
		var obj = {};
		obj.menuCode  = addGrantsMap[i].attributes.PARENT_ID;
		obj.opCode    = addGrantsMap[i].attributes.OPCODE;
		if (addGrantsStr.length == 0) {
			addGrantsStr = obj.menuCode + ' ' +obj.opCode;
		} else {
			addGrantsStr += ',' + obj.menuCode + ' ' +obj.opCode;
		}
	}
	for(var i=0;i<addStr.length;i++){
		if(addMenuStr == '') {
			addMenuStr = addStr[i];
		} else {
			addMenuStr = addMenuStr + ',' + addStr[i];
		}
	}
	for(var i=0;i<delStr.length;i++){
		if(delMenuStr == '') {
			delMenuStr = delStr[i];
		} else {
			delMenuStr = delMenuStr + ',' + delStr[i];
		}
	}
};

/**
 * 保存设置
 */
function saveSet(){
	if(roleCodeGloble == undefined || roleCodeGloble == "" || roleCodeGloble == null){//未选择角色编码时，提示必选信息
		Ext.Msg.alert('提示','请选择角色！');
		return ;
	}
	beforeSaveSet();
	Ext.Ajax.request({//执行保存设置
		//增量数据操作url
		url : basepath + '/roleMenuOptionAction!saveOptionSet.json',
		method:'POST',
		params :{
			addStr : addMenuStr,
			delStr : delMenuStr,
			roleCodeGloble : roleCodeGloble,
			menuCodeGloble : menuCodeGloble,
			addGrantsStr : addGrantsStr,
			delGrantsStr : delGrantsStr
		},
		success:function(response){
			Ext.Msg.alert('提示','操作成功！');
			if(selectNode != undefined && selectNode.id != 'root'){
				roleCodeGloble = selectNode.attributes.ID;
				Ext.Ajax.request({
					url:basepath + '/roleMenuQuery.json?roleId='+selectNode.attributes.ID,
					method:'GET',
					success:function(response){
						refreshCheckItem(response);
					},
    				failure:function(){
    				}
				});
			}
		},
		failure:function(){
			Ext.Msg.alert('提示','操作失败！');
		}
	});
}

/**
 * 刷新选择项
 * @param {} response
 */
var refreshCheckItem = function (response){
	treeLoader = Ext.util.JSON.decode(response.responseText).json.data;//获取选择角色的菜单项
	for(var i=0;i<menuTree.root.childNodes.length;i++){//清空原角色选择情况
		menuTree.root.childNodes[i].fireEvent('checkchange',menuTree.root.childNodes[i],false,undefined);
	}
	for(var i=0;i<treeLoader.length;i++){
		var tn = menuTree.root.findChild('id',treeLoader[i].RES_CODE,true);
		if (tn!=undefined && tn.attributes.LEAF_FLAG == 1) {
			tn.fireEvent('checkchange',tn,true);
			for(var j=0; j<tn.childNodes.length;j++){
				tn.childNodes[j].getUI().toggleCheck(false);
			}
		}
		if(tn!=undefined && treeLoader[i].OPERATE_KEY != '["VIEW","AUTH_PERMISSION"]'){
			for(var j=0; j<tn.childNodes.length;j++){
				if(treeLoader[i].OPERATE_KEY.indexOf('\"'+tn.childNodes[j].attributes.OPCODE+'\"') != -1){
					tn.childNodes[j].getUI().toggleCheck(true);
				}
			}
		}
	}
	addStr = [];
	delStr = [];
	addMenuStr = '';
	delMenuStr = '';
	delGrantsMap = [];
	addGrantsMap = [];
	addGrantsStr = '';
	delGrantsStr = '';		
};

//复制角色窗口
var copyRoleWin = new Ext.Window({
	title:'复制角色：[XXX] 权限到已有角色',
	width:400,
	height:450,
	closeAction:'hide',
	autoScroll:true,
	closable:true,
	maximizable:false,
	animCollapse:false,
	constrainHeader:true,
	modal:true,
	frame:true,
	layout:'fit',
	items:[copyRoleTree],
	listeners:{
		beforeshow: function(){
			Ext.Ajax.request({//重新加载角色树
				url : basepath + '/roleManagerQuery!getAuthRoles.json',
				method : 'GET',
				success : function(response) {
					var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
					for(var i=0;i<nodeArra.length;i++){
						if(nodeArra[i].ID == roleCodeGloble){
							nodeArra.remove(nodeArra[i]);//注：涉及到移除数组数制的这种，在遍历时要特别小心越界
							break;
						}
					}
					copyRoleTree.resloader.nodeArray = nodeArra;//重新获取后台数据
					copyRoleTree.resloader.refreshCache(); // 刷新缓存
					var children = copyRoleTree.resloader.loadAll(); //得到相应的树数据
					copyRoleTree.root.removeAll(true); // 清掉以前的数据
					copyRoleTree.appendChild(children);// 把数据重新填充
				}
			});
		}
	}
});
