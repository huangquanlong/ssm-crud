/**
描述：需求视图JS文件，本页面只布局了需求目录（及目录下的需求）数，在点击需求目录树下的需求（红色的叶子节点），在右侧渲染展示需求视图页面
作者：wzy
时间：2014-02-15
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
	viewType : '1',//视图类型：需求视图
	CURRENT_VIEW_URL: '',
	containDIVid : 'viewport_center_dir',
	jspUrl : '/contents/frameControllers/view/Wlj-reqmentview-base.jsp',
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
        
        _this.initProReqDirTree();//初始化需求目录
		_this.viewPanel = new Ext.Panel({
			region:'center',
			html:'<iframe id="viewport_center_dir" name="viewport_center_dir" src="" style="width:100%;height:100%;" frameborder="no"/>'
		});
		
		_this.queryReqDirTree();//查询需求目录
		
		_this.add(_this.viewPanel);
		_this.add({
			region:'west',
			id:'leftLayout',
			layout:'accordion',			
			width:220,
			autoScroll :true,
			collapsible : true,//面板支持收缩
			layoutConfig: {
		        titleCollapse: false,
		        animate: false,
		        hideCollapseTool : true,
		        activeOnTop: true
		    },
			items:[_this.viewTreeReqDir]
		});
	},
	//初始化项目下的需求目录树
	initProReqDirTree : function(){
		var _this = this;
		_this.viewLoaderReqDir = new Com.yucheng.bcrm.ArrayTreeLoader({
			parentAttr : 'REQ_DIR_PARENT_NO',
			idProperties : 'REQ_DIR_ID',
			textField : 'REQ_DIR_NAME',
			locateAttr : 'REQ_DIR_ID',
			rootValue : '0'
		});
		_this.viewTreeReqDir = new Com.yucheng.bcrm.TreePanel({
			title:'需求目录树',
			width:220,
			region:'west',
			rootVisible:false,
			collapsible:false,
			autoScroll :true,
			loadMaskWorking : true,
			expandedCount : 0,			
			root: new Ext.tree.AsyncTreeNode({
				id :'0',
				text:'需求目录',
				expanded:true,
				autoScroll:true,
				children:[]
			}),
			resloader:_this.viewLoaderReqDir,
		    listeners: {
		    	'click':function(node){
					_this.viewClickHandler(node,_this.viewTreeReqDir,_this.myViewTree);
				},
				'afterlayout':function(){//布局完成后，查询在需求列表页面选中的需求视图信息
//					_this.openReqView(_this.jspUrl);
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
						var mName = "";
				        if(CUSTVIEW.reqName){
				        	mName = CUSTVIEW.reqName;//要打开的“需求视图目录树”的菜单名称
				        }
				        mName = "<font color=\"red\">"+mName+"</font>";
						var tn = this.root.findChild('REQ_DIR_NAME',mName,true);
						if(tn){
							tn.fireEvent('click',tn);
						}
						_this.openReqView(_this.jspUrl);//左侧目录树节点单击完成后，查询在需求列表页面选中的需求视图信息
						return;
					}
				}
		    }
		});
	},
	//查询全部需求目录树节点
	queryReqDirTree : function(){
		var _this = this;
		Ext.Ajax.request({
			url : basepath + '/queryProjectViewAuthorizeAction!queryProReqDir.json?viewtype=2&projId='+_this.projId,
			method:'GET',
			success:function(response){
				var nodeArra = Ext.util.JSON.decode(response.responseText).JSON.data;
				_this.viewTreeReqDir.resloader.nodeArray = nodeArra;//重新获取后台数据
				_this.viewTreeReqDir.resloader.refreshCache(); // 刷新缓存
				var children = _this.viewTreeReqDir.resloader.loadAll(); //得到相应的树数据
				_this.viewTreeReqDir.root.removeAll(true); // 清掉以前的数据
				_this.viewTreeReqDir.appendChild(children);// 把数据重新填充
				_this.viewTreeReqDir.expandAll();
//				在所有节点没有完全展开的情况下，查找二级及以下节点时，获取不到，所有在事件“expandnode”中处理打开菜单的逻辑
//				var tn = _this.viewTreeReqDir.root.findChild('NAME',_this.reqName,true);
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
		if (node.id && node.id != '0' && node.leaf && node.leaf == true && node.attributes.ISDIR && node.attributes.ISDIR == '0') {
			this.CURRENT_VIEW_URL = node.attributes.ADDR;			
   			var jspUrl = this.jspUrl;//需求视图JSP路径   
			var url = this.builtfunctionurl(jspUrl,node);
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
	builtfunctionurl : function(baseUrl,node){
		var url = false;
		if(baseUrl.indexOf('.jsp') < 0 ){
			url = basepath + '/contents/frameControllers/view/Wlj-proj-view-function.jsp';
		}else{
			url = basepath + baseUrl.split('.jsp')[0]+'.jsp';
		}
		var resId = node.id +'$-$' + this.viewType +'$-$' + this.projId+'$-$' + this.projNo+'$-$' + this.projName
		            +'$-$' + node.id+'$-$' + node.attributes.REQ_DIR_NO+'$-$' + node.attributes.REQ_DIR_NAME;
		var turl = baseUrl.indexOf('?')>=0 ? (baseUrl + '&resId='+encodeURI(encodeURI(resId)) ): 
		           (baseUrl + '?resId='+encodeURI(encodeURI(resId))) ;
		url += '?' + turl.split('?')[1];
		url += encodeURI(encodeURI('&projId='+this.projId+ '&projNo='+this.projNo+ '&projName='+this.projName
		    +'&viewId='+node.id+'&viewType='+this.viewType
		    +'&reqId='+node.id+'&reqNo='+node.attributes.REQ_DIR_NO+'&reqName='+node.attributes.REQ_DIR_NAME));//对参数做了转码
		return url;
	},
	//打开本页面过后，默认查询在需求列表中选中的需求信息
	openReqView : function(baseUrl){
		var url = false;
		if(baseUrl.indexOf('.jsp') < 0 ){
			url = basepath + '/contents/frameControllers/view/Wlj-proj-view-function.jsp';
		}else{
			url = basepath + baseUrl.split('.jsp')[0]+'.jsp';
		}
		var resId = this.id +'$-$' + this.viewType +'$-$' + this.projId+'$-$' + this.projNo+'$-$' + this.projName
		            +'$-$' + this.reqId+'$-$' + this.reqNo+'$-$' + this.reqName+'$-$' + this.menuName;
		var turl = baseUrl.indexOf('?')>=0 ? (baseUrl + '&resId='+encodeURI(encodeURI(resId)) ): 
		           (baseUrl + '?resId='+encodeURI(encodeURI(resId))) ;
		url += '?' + turl.split('?')[1];
		url += encodeURI(encodeURI('&projId='+this.projId+ '&projNo='+this.projNo+ '&projName='+this.projName
		    +'&viewId='+this.id+'&viewType='+this.viewType
		    +'&reqId='+this.reqId+'&reqNo='+this.reqNo+'&reqName='+this.reqName+'&menuName='+this.menuName));//对参数做了转码
		document.getElementById(this.containDIVid).src = url;
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