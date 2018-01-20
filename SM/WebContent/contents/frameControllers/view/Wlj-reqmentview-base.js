/**
描述：需求视图JS文件，根据CRM中的客户视图代码进行逻辑修改
作者：wzy
时间：2014-02-06
*/
Ext.ns('Wlj.view');
Wlj.view.ReqView = Ext.extend(Ext.Panel,{
	layout: 'border',
	resId: '',	//视图资源ID,格式为： task_view$-$0$-$100,  0表示视图类型,100项目ID或需求ID
	projId: '', //项目ID
	projNo: '', //项目标识
	projName: '', //项目名称
	reqId: '', //需求ID
	reqNo: '', //需求标识
	reqName: '', //需求名称
	menuName: '',//要打开的视图的菜单名称
	CURRENT_VIEW_URL: '',
	containDIVid : 'viewport_center',
	initComponent : function(){
        Wlj.view.ReqView.superclass.initComponent.call(this);
        
		var _this = this;
        _this.resId = JsContext._resId;
        var resIdArr = this.resId.split('$-$');
        if(resIdArr.length < 8){
        	return;
        }
        _this.projId = resIdArr[2];//项目ID
        _this.projNo = resIdArr[3];//项目标识
        _this.projName = resIdArr[4];//项目名称
        _this.reqId = resIdArr[5];//需求ID
        _this.reqNo = resIdArr[6];//需求标识
        _this.reqName = resIdArr[7];//需求名称
        if(resIdArr[8]){
        	_this.menuName = resIdArr[8];//要打开的视图的菜单名称
        }
        
        _this.initAllViewTree();
		_this.viewPanel = new Ext.Panel({
			region:'center',
			html:'<iframe id="viewport_center" name="viewport_center" src="" style="width:100%;height:100%;" frameborder="no"/>'
		});
		
		_this.queryViewTree();
		
		_this.add(_this.viewPanel);
		_this.add({
			region:'west',
			id:'leftLayout',
			layout:'accordion',
			width:220,
			collapsible : true,//面板支持收缩
			layoutConfig: {
		        titleCollapse: false,
		        animate: false,
		        hideCollapseTool : true,
		        activeOnTop: true
		    },
			items:[_this.viewTree]
		});
	},
	//初始化全部视图
	initAllViewTree : function(){
		var _this = this;
		_this.viewLoader = new Com.yucheng.bcrm.ArrayTreeLoader({
			parentAttr : 'PARENTID',
			idProperties : 'ID',
			textField : 'NAME',
			locateAttr : 'ID',
			rootValue : '0'
		});
		_this.viewTree = new Com.yucheng.bcrm.TreePanel({
			title:'需求视图树',
			width:220,
			region:'west',
			rootVisible:false,
			collapsible:false,
			autoScroll :true,
			loadMaskWorking : true,
			expandedCount : 0,
			root: new Ext.tree.AsyncTreeNode({
				id :'0',
				text:'需求视图',
				expanded:true,
				autoScroll:true,
				children:[]
			}),
			resloader:_this.viewLoader,
		    listeners: {
		    	'click':function(node){
					_this.viewClickHandler(node,_this.viewTree,_this.myViewTree);
				},				
		        /**
				 * 展开节点事件：当所有节点都展开后，打开对应的视图菜单
				 * @param {} node
				 */
				'expandnode':function(node){
					if(!this.loadMaskWorking){
						return;
					}
					this.expandedCount++;
					if(this.expandedCount >= this.resloader.nodeArray.length){
						this.loadMaskWorking = false;
						var mName = "需求基本信息";				
				        if(CUSTVIEW.menuName){
				        	mName = CUSTVIEW.menuName;//要打开的视图的菜单名称
				        }
						var tn = this.root.findChild('NAME',mName,true);
						if(tn){
							tn.fireEvent('click',tn);
						}
						return;
					}
				}
		    }
		});
	},
	//查询全部需求视图树
	queryViewTree : function(){
		var _this = this;
		Ext.Ajax.request({
			url : basepath + '/queryProjectViewAuthorizeAction!queryCustViewTree.json?viewtype=2&projId='+_this.projId,
			method:'GET',
			success:function(response){
				var nodeArra = Ext.util.JSON.decode(response.responseText).JSON.data;
				_this.viewTree.resloader.nodeArray = nodeArra;//重新获取后台数据
				_this.viewTree.resloader.refreshCache(); // 刷新缓存
				var children = _this.viewTree.resloader.loadAll(); //得到相应的树数据
				_this.viewTree.root.removeAll(true); // 清掉以前的数据
				_this.viewTree.appendChild(children);// 把数据重新填充
				_this.viewTree.expandAll();
//				在所有节点没有完全展开的情况下，查找二级及以下节点时，获取不到，所有在事件“expandnode”中处理打开菜单的逻辑
//				var mName = "需求基本信息";				
//		        if(_this.menuName){
//		        	mName = _this.menuName;//要打开的视图的菜单名称
//		        }
//				var tn = _this.viewTree.root.findChild('NAME',mName,true);
//				if(tn){
//					tn.fireEvent('click',tn);
//				}
			}
		});
	},
	
	/**
	 * 视图click事件触发
	 * @param {} node	当前节点
	 * @param {} currViewTree	当前点击树
	 * @param {} otherViewTree  当前未点击树
	 */
	viewClickHandler : function(node,currViewTree,otherViewTree){
		if (node.attributes.ADDR && node.attributes.ADDR != '0') {
			//_this.viewPanel.setTitle(node.text);
			this.CURRENT_VIEW_URL = node.attributes.ADDR;
			var url = this.builtfunctionurl(node.attributes.ADDR,node.id);
			document.getElementById(this.containDIVid).src = url;
			if(otherViewTree){
				otherViewTree.getSelectionModel().clearSelections();
				var tn = otherViewTree.root.findChild('id',node.id,true);
				if(tn){
					tn.select();
				}
			}
		}
	},
	/**
	 * 构建视图业务功能url
	 * @param {} baseUrl
	 * @param {} viewId:视图ID
	 * @return {}
	 */
	builtfunctionurl : function(baseUrl,viewId){
		var url = false;
		if(baseUrl.indexOf('.jsp') < 0 ){
			url = basepath + '/contents/frameControllers/view/Wlj-proj-view-function.jsp';
		}else{
			url = basepath + baseUrl.split('.jsp')[0]+'.jsp';
		}
		var turl = baseUrl.indexOf('?')>=0 ? (baseUrl + '&resId='+encodeURI(encodeURI(this.resId)) ): (baseUrl + '?resId='+encodeURI(encodeURI(this.resId))) ;
		url += '?' + turl.split('?')[1];
		url += encodeURI(encodeURI('&projId='+this.projId+ '&projNo='+this.projNo+ '&projName='+this.projName
		    +'&viewId='+viewId+'&viewType='+this.viewType+'&reqId='+this.reqId+'&reqNo='+this.reqNo
		    +'&reqName='+this.reqName));//对参数做了转码
		return url;
	}
});

/**
 * 重写Ext树节点展现类
 * @class Wlj.view.TreeNodeUI
 * @extends Ext.tree.TreeNodeUI
 */
Wlj.view.TreeNodeUI = Ext.extend(Ext.tree.TreeNodeUI,{
    // private
    renderElements : function(n, a, targetNode, bulkRender){
        // add some indent caching, this helps performance when rendering a large tree
        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';

        var cb = Ext.isBoolean(a.checked),
            nel,
            href = this.getHref(a.href),
            buf = ['<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf x-unselectable ', a.cls,' cust-viewList" unselectable="on">',
            '<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
            '<img alt="" src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow" />',
            '<img alt="" src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on" />',
            cb ? ('<input class="x-tree-node-cb" type="checkbox" ' + (a.checked ? 'checked="checked" />' : '/>')) : '',
            '<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ',
             a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '><span unselectable="on">',n.text,"</span></a>"
             ,'<a class="view-remove" href="javascript:void(0);"></a></div>',
            '<ul class="x-tree-node-ct" style="display:none;"></ul>',
            "</li>"].join('');

        if(bulkRender !== true && n.nextSibling && (nel = n.nextSibling.ui.getEl())){
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin", nel, buf);
        }else{
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf);
        }

        this.elNode = this.wrap.childNodes[0];
        this.ctNode = this.wrap.childNodes[1];
        var cs = this.elNode.childNodes;
        this.indentNode = cs[0];
        this.ecNode = cs[1];
        this.iconNode = cs[2];
        var index = 3;
        if(cb){
            this.checkbox = cs[3];
            // fix for IE6
            this.checkbox.defaultChecked = this.checkbox.checked;
            index++;
        }
        this.anchor = cs[index];
        this.textNode = cs[index].firstChild;
    }
});

Ext.onReady(function(){
	window.CUSTVIEW = new Wlj.view.ReqView();
	var viewport = new Ext.Viewport({
        layout : 'fit',
        items: [window.CUSTVIEW]
    });
});