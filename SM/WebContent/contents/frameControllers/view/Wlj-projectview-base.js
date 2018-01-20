/**
描述：项目视图JS文件，根据CRM中的客户视图代码进行逻辑修改
作者：wzy
时间：2014-01-30
*/
Ext.ns('Wlj.view');
Wlj.view.ProjView = Ext.extend(Ext.Panel,{
	layout: 'border',
	resId: '',	//视图资源ID,格式为： task_view$-$0$-$100,  0表示视图类型,100项目ID或需求ID
	projId: '', //项目ID
	projNo: '', //项目标识
	projName: '', //项目名称
	menuName: '',//要打开的视图的菜单名称
	CURRENT_VIEW_URL: '',
	containDIVid : 'viewport_center',
	initComponent : function(){
        Wlj.view.ProjView.superclass.initComponent.call(this);
        
		var _this = this;
        _this.resId = JsContext._resId;
        var resIdArr = this.resId.split('$-$');
        if(resIdArr.length < 8){
        	return;
        }
        _this.projId = resIdArr[2];//项目ID
        _this.projNo = resIdArr[3];//项目标识
        _this.projName = resIdArr[4];//项目名称
        if(resIdArr[8]){
        	_this.menuName = resIdArr[8];//要打开的视图的菜单名称
        }
        
        _this.initAllViewTree();
        _this.initMyViewTree();
		_this.viewPanel = new Ext.Panel({
			region:'center',
			html:'<iframe id="viewport_center" name="viewport_center" src="" style="width:100%;height:100%;" frameborder="no"/>'
		});
		
		_this.queryViewTree();
		_this.queryMyViewTree();
		
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
			items:[_this.viewTree,_this.myViewTree]
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
			title:'全部项目视图',
			width:220,
			region:'west',
			rootVisible:false,
			collapsible:false,
			autoScroll :true,
			loadMaskWorking : true,
			expandedCount : 0,
			root: new Ext.tree.AsyncTreeNode({
				id :'0',
				text:'项目视图',
				expanded:true,
				autoScroll:true,
				children:[]
			}),
			resloader:_this.viewLoader,
			tools : [{
	        	id:'allview',
	        	handler:function(e,target,panel){
	        		_this.switchView(false);
	        	}
	       	}],
		    listeners: {
		    	'click':function(node){
					_this.viewClickHandler(node,_this.viewTree,_this.myViewTree);
				},
		        contextmenu: function(node, e) {
		        	if(!node.leaf){
		        		return;
		        	}
		        	var c = node.getOwnerTree().contextMenu;
				    var tn = _this.myViewTree.root.findChild('id',node.id,true);
					if(tn){
						c.items.items[0].setVisible(false);
						c.items.items[1].setVisible(true);
					}else{
						c.items.items[0].setVisible(true);
						c.items.items[1].setVisible(false);
					}
		            c.contextNode = node;
		            c.showAt(e.getXY());
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
						var mName = "项目首页";
				        if(CUSTVIEW.menuName){
				        	mName = CUSTVIEW.menuName;//要打开的视图的菜单名称
				        }
						var tn = this.root.findChild('NAME',mName,true);//打开“项目流程图”菜单对应的页面
						if(tn){
							tn.fireEvent('click',tn);
						}
						return;
					}
				}
		    },
		    contextMenu: new Ext.menu.Menu({
		        items: [{
		            id: 'addnode',
		            iconCls:'ico-g-1',
		            text: '添加到我的视图'
		        },{
		            id: 'removenode',
		            iconCls:'ico-g-2',
		            text: '从我的视图移除'
				}],
		        listeners: {
		            itemclick: function(item) {
		                switch (item.id) {
		                    case 'addnode':
		                    {
		                    	var node = item.parentMenu.contextNode;
		                    	_this.addMyView(node);
		                        break;
		                    }
		                    case 'removenode':
		                    {
		                    	var node = item.parentMenu.contextNode;
		                    	var tn = _this.myViewTree.root.findChild('id',node.id,true);
								if(tn){
									 _this.removeMyView(tn);
								}
		                        break;
		                    }
		                }
		            }
		        }
		    })
		});
	},
	//初始化我的视图
	initMyViewTree : function(){
		var _this = this;
		_this.myViewLoader = new Com.yucheng.bcrm.ArrayTreeLoader({
			parentAttr : 'PARENTID',
			idProperties : 'ID',
			textField : 'NAME',
			locateAttr : 'ID',
			rootValue : '0'
		});
		_this.myViewTree = new Com.yucheng.bcrm.TreePanel({
			title:'我的项目视图',
			rootVisible:false,
			hidden:true,
			autoScroll :true,
			root: new Ext.tree.AsyncTreeNode({
				id :'0',
				text:'我的项目视图',
				expanded:true,
				autoScroll:true,
				children:[]
			}),
			resloader:_this.myViewLoader,
			tools : [{
	        	id:'myview',
	        	handler:function(e,target,panel){
	        		_this.switchView(true);
	        	}
	       	}],
	       	listeners:{
	       		beforeclick : function(node,e){
	       			var a = e.getTarget('a');
	       			if(a && a.className === 'view-remove'){
	       				//从我的客户视图中移除
	       				_this.removeMyView(node);
	       				//返回false阻止树节点click事件触发
	       				return false;
	       			}
	       		},
				'click':function(node){
					_this.viewClickHandler(node,_this.myViewTree,_this.viewTree);
				}
		    }
		});
	},
	//查询全部项目视图树
	queryViewTree : function(){
		var _this = this;
		Ext.Ajax.request({
			url : basepath + '/queryProjectViewAuthorizeAction!queryCustViewTree.json?viewtype=1&projId='+_this.projId,
			method:'GET',
			success:function(response){
				var nodeArra = Ext.util.JSON.decode(response.responseText).JSON.data;
				_this.viewTree.resloader.nodeArray = nodeArra;//重新获取后台数据
				_this.viewTree.resloader.refreshCache(); // 刷新缓存
				var children = _this.viewTree.resloader.loadAll(); //得到相应的树数据
				_this.viewTree.root.removeAll(true); // 清掉以前的数据
				_this.viewTree.appendChild(children);// 把数据重新填充
				_this.viewTree.render();
				_this.viewTree.expandAll();
//				在所有节点没有完全展开的情况下，查找二级及以下节点时，获取不到，所有在事件“expandnode”中处理打开菜单的逻辑
//				var mName = "项目流程图";
//		        if(_this.menuName){
//		        	mName = _this.menuName;//要打开的视图的菜单名称
//		        }
//				var tn = _this.viewTree.root.findChild('NAME',mName,true);//打开“项目流程图”菜单对应的页面
//				if(tn){
//					tn.fireEvent('click',tn);
//				}
			}
		});
	},
	//查询我的项目视图树
	queryMyViewTree : function(){
		var _this = this;
		Ext.Ajax.request({
			url : basepath + '/queryProjectViewAuthorizeAction!queryMyViewTree.json?viewtype=1&projId='+_this.projId,
			method:'GET',
			success:function(response){
				if(Ext.util.JSON.decode(response.responseText).JSON == null){
					Ext.Msg.alert('提示','项目视图树加载失败！');
					return false;
				}
				var nodeArra = Ext.util.JSON.decode(response.responseText).JSON.data;
				_this.myViewTree.resloader.nodeArray = nodeArra;//重新获取后台数据
				_this.myViewTree.resloader.refreshCache(); // 刷新缓存
				var children = _this.myViewTree.resloader.loadAll(); //得到相应的树数据
				Ext.each(children,function(i){
					i.uiProvider = Wlj.view.TreeNodeUI;
				});
				_this.myViewTree.root.removeAll(true); // 清掉以前的数据
				_this.myViewTree.appendChild(children);// 把数据重新填充
				_this.myViewTree.expandAll();
				
				var node = _this.viewTree.getSelectionModel().getSelectedNode();
				if(node){
					//判断全部视图上选择的节点，是否存在
					var tn = _this.myViewTree.root.findChild('id',node.id,true);
					if(tn){
						tn.select();
					}
				}
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
			otherViewTree.getSelectionModel().clearSelections();
			var tn = otherViewTree.root.findChild('id',node.id,true);
			if(tn){
				tn.select();
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
		    +'&viewId='+viewId+'&viewType='+this.viewType));//对参数做了转码
		return url;
	},
	
	/**
	 * 切换视图
	 * @param bool true表示显示全部视图，隐藏我的视图
	 */
	switchView : function(bool){
		this.viewTree.setVisible(bool);
		this.myViewTree.setVisible(!bool);
		Ext.getCmp('leftLayout').layout.setActiveItem(bool?this.viewTree:this.myViewTree);
	},
	addMyView : function(node){
		var _this = this;
		Ext.Ajax.request({
			url : basepath + '/myCustView.json',
			params:{
				viewId : node.id,
				userId : JsContext._userId,
				projId : this.projId
			},
			method:'POST',
			success:function(response){
				_this.queryMyViewTree();
			}
		});
	},
	removeMyView : function(node){
		var _this = this;
		Ext.Ajax.request({
			url : basepath + '/myCustView!batchDestroy.json',
			params:{
				ids : node.attributes.MY_VIEW_ID
			},
			method:'POST',
			success:function(response){
				_this.queryMyViewTree();
			}
		});
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
	window.CUSTVIEW = new Wlj.view.ProjView();
	var viewport = new Ext.Viewport({
        layout : 'fit',
        items: [window.CUSTVIEW]
    });
});