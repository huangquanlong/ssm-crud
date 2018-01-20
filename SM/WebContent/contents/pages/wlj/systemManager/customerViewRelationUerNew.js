/**
 * @description 客户视图授权
 * @author helin
 * @since 2014-07-01
 */
imports([
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js'
]);

Ext.QuickTips.init();
//树配置查询类型
var treeLoaders = [{
	key : 'ROLE_TREE_LOADER_KEY',	//角色树loader
	url : basepath + '/sysRole-kind-tree.json',
	parentAttr : 'APP_ID',
	locateAttr : 'ID',
	rootValue : '62',
	textField : 'ROLE_NAME',
	idProperties : 'ID',
	jsonRoot:'json.data'
},{
	key : 'VIEW_TREE_LOADER_KEY',  //菜单功能树loader
	url : basepath + '/queryCustViewAuthorize.json',
	parentAttr : 'PARENTID',
	locateAttr : 'ID',
	rootValue : '0_0',
	textField : 'NAME',
	idProperties : 'ID',
	jsonRoot:'JSON.data'
}];
var treeCfgs = [{
	key : 'ROLE_TREE_KEY',
	loaderKey : 'ROLE_TREE_LOADER_KEY',
	autoScroll:true,
	title : '角色栏',
	rootVisible: false,
	rootCfg : {
		expanded:true,
		id:'root',
		text:'所有角色',
		autoScroll:true,
		children:[]
	},
	clickFn : function(node){
		sUserid = node.id;
		roleCodeGloble=sUserid;
		if(node != undefined){
			Ext.Ajax.request( {
				url : basepath+ '/queryCustViewAuthorize!queryAuthorizeData.json?role_id='+ sUserid,
				method : 'GET',
				success : function(response) {
					treeLoader = Ext.util.JSON.decode(response.responseText).JSON.data;//获取选择角色的菜单项
					for(var i=0;i<viewTree.root.childNodes.length;i++){//清空原角色选择情况
						viewTree.root.childNodes[i].fireEvent('checkchange',viewTree.root.childNodes[i],false,undefined);
					}
					for(var i=0;i<treeLoader.length;i++){
						var tn = viewTree.root.findChild('id',treeLoader[i].VIEW_ID,true);
						if(tn!=undefined && tn.childNodes.length == 0){
							tn.fireEvent('checkchange',tn,true);
						}
					}
					addStr = [];
					delStr = [];
					ifFlag = [];
				}
			});
		}
	}
},{
	key : 'VIEW_TREE_KEY',
	loaderKey : 'VIEW_TREE_LOADER_KEY',
	autoScroll:true,
	title: '客户视图',
	rootVisible: false,
	checkBox:true,
	loadMaskWorking : true,
	expandedCount : 0,
	rootCfg : {
		expanded:true,
		id:'root',
		text:'客户视图',
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
	    	for(var i=0;i<viewTree.root.childNodes.length;i++){//清空原角色选择情况
				viewTree.root.childNodes[i].fireEvent('checkchange',viewTree.root.childNodes[i],false,undefined);
			}
	    }
	}],
	listeners:{
		'expandnode': function(node){
			if(!this.loadMaskWorking){
				return;
			}
			this.expandedCount++;
			if(this.expandedCount >= this.resloader.nodeArray.length){
				//lm.hide();
				this.loadMaskWorking = false;
				return;
			}
		},
		'click': function(node){
		},
		'checkchange' : function(node, checked, source) {
			if(sUserid!=''){
				if(treeLoader!==''){
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
					if(source==undefined){									//操作节点状态，并调用父节点和子节点事件
						node.getUI().checkbox.indeterminate=false;
						node.getUI().toggleCheck(checked);
		        		if(node.childNodes){
		        			Ext.each(node.childNodes,function(cn){
		        				cn.fireEvent('checkchange',cn,checked,'1');//若存在子节点，则传递参数以触发相应checkChange监听事件
		        			});
		        		}
		        		if(node.parentNode && node.parentNode !== this.root){
		        			node.parentNode.fireEvent('checkchange',node.parentNode,checked,'2');//若存在父节点，则传递参数以触发相应监听事件
		        		}
		        	}else if(source == '1'){								//操作节点状态，并调用子节点事件
		        		node.getUI().checkbox.indeterminate=false;
		        		node.getUI().toggleCheck(checked);
		        		if(node.childNodes){
		        			Ext.each(node.childNodes,function(cn){
		        				cn.fireEvent('checkchange',cn,checked,'1');//若存在子节点，则传递参数以触发相应checkChange监听事件
		        			});
		        		}
			        }else if(source == '2'){								//操作节点状态，并调用父节点事件
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
	        	}else{
	        		Ext.Msg.alert('提示','请选择授权角色');
	        		return;
	        	}
			}else{
				Ext.Msg.alert('提示','请选择授权角色');
				node.getUI().toggleCheck(false);
				return;
			}
		}
	}
}];

var needGrid = false;
WLJUTIL.suspendViews=false;  //自定义面板是否浮动

var fields = [
	{name: 'TEST',text:'此文件fields必须要有一个无用字段', resutlWidth:80}
];

//左则角色树
var roleTree = TreeManager.createTree('ROLE_TREE_KEY');
var viewTree = TreeManager.createTree('VIEW_TREE_KEY');

//结果域扩展功能面板
var customerView = [{
	/**
	 * 自定义客户视图面板
	 */
	title:'客户视图',
	hideTitle: true,
	items: [viewTree]
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
 * 结果域面板滑入后触发,系统提供listener事件方法
 */
var viewshow = function(theview){
	if(theview._defaultTitle == '客户视图'){
		Ext.Ajax.request({
			url : basepath + '/queryCustViewAuthorize.json',
			method : 'GET',
			success : function(response) {
				viewTree.root.removeAll(true); // 清掉以前的数据
				var nodeArra = Ext.util.JSON.decode(response.responseText).JSON.data;
				viewTree.resloader.nodeArray = nodeArra;//重新获取后台数据
				viewTree.resloader.refreshCache(); // 刷新缓存
				var children = viewTree.resloader.loadAll(); //得到相应的树数据
				viewTree.root.removeAll(true); // 清掉以前的数据
				viewTree.appendChild(children);// 把数据重新填充
				viewTree.expandAll();//默认展开树，解决前台取不到子节点，从而不能递归check问题
			}
		});
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
		if(arr[i].VIEW_ID==id){
			return true;
		}
	}
	return false;
};
var record ='';//选择的角色编码
//删除数组
var delStr = [];
//新增数组
var addStr = [];
var treeLoader='';
var index = [];//控制点授权数组
var listenNode = true;
var sUserid = '';
var roleCodeGloble='';
//遮罩层定义
//var lm = new Ext.LoadMask (document.body,{
//   	msg : '正在加载客户视图数据,请稍等...'
//});
//lm.show();
/**
 * 保存数据
 * @return {Boolean}
 */
function saveSet(){
	if(sUserid == undefined || sUserid == "" || sUserid == null){//未选择角色编码时，提示必选信息
		Ext.Msg.alert('提示','请选择角色！');
		return false ;
	}
	var optionCode = '';//所选全部功能按钮编码
	Ext.Ajax.request({//执行保存设置
		url :basepath + '/userviewrelation.json?addStr='+addStr+'&delStr='+delStr+'&user_id='+sUserid,
		method:'POST',
		success:function(response){
			Ext.Ajax.request( {
				url : basepath+ '/queryCustViewAuthorize!queryAuthorizeData.json?role_id='+ sUserid,
				method : 'GET',
				success : function(response) {
					treeLoader = Ext.util.JSON.decode(response.responseText).JSON.data;//获取选择角色的菜单项
					for(var i=0;i<viewTree.root.childNodes.length;i++){//清空原角色选择情况
						viewTree.root.childNodes[i].fireEvent('checkchange',viewTree.root.childNodes[i],false,undefined);
					}
					for(var i=0;i<treeLoader.length;i++){
						var tn = viewTree.root.findChild('id',treeLoader[i].VIEW_ID,true);
						if(tn!=undefined && tn.childNodes.length == 0){
							tn.fireEvent('checkchange',tn,true);
						}
					}
					addStr = [];
					delStr = [];
					Ext.Msg.alert('提示','保存成功！');
				}
			});
		},
		failure:function(){
			Ext.Msg.alert('提示','操作失败！');
		}
	});
};
