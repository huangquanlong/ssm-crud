Ext.ns('Wlj.widgets.search.window');
Wlj.widgets.search.window.TaskBar = Ext.extend(Ext.Container,{
	autoEl:{
		tag : 'div',
		cls : 'main_task'
	},
	windowManager : new Ext.WindowGroup(),
	containerTemplate : new Ext.XTemplate(
			'<div class=main_task_div >',
				'<div class=task_box >',
				'</div>',
			'</div>'),
	onRender : function(ct, position){
		Wlj.widgets.search.window.TaskBar.superclass.onRender.call(this, ct, position);
		this.containerEl = this.containerTemplate.append(this.el);
		var _this = this;
		this.el.on('contextmenu',function(eve){
			eve.stopEvent();
			_this.onContextMenu(eve, []);
		});
		
	},
	getLayoutTarget : function(){
		return this.containerEl;
	},
	openWindow : function(cfg){
		var tid = cfg.id;
		var task = this.getTaskByTaskId(tid);
		
		if(task){
			task.select();
		}else {
			this.add(new Wlj.widgets.search.window.TaskItem(Ext.apply(cfg,{
				appObject : this.appObject,
				height : this.height-2
			})));
			this.doLayout();
		}
	},
	getTaskByTaskId : function(taskId){
		var task = this.items.get(taskId);
		return task ? task:false;
	},
	closeAll : function(){
		this.items.each(function(i){
			i.close();
		});
	},
	closeWithOutCurrent : function(){
		var _this = this;
		this.items.each(function(i){
			if(i!==_this.currentItem){
				i.close();
			}
		});
	},
	closeWithOut : function(theItem){
		var _this = this;
		this.items.each(function(i){
			if(i!==theItem){
				i.close();
			}
		});
	},
	onContextMenu : function(e, added){
		var tbarMenu = Wlj.search.App.CONTEXT_MENU.TASK_BAR;
		if(added.length>0 && tbarMenu.length>0){
			added.push('-');
		}
		var _this = this;
		if(tbarMenu.length>0){
			Ext.each(tbarMenu, function(tm){
				if(!tm.handler)
					tm.handler = tm.fn.createDelegate(_this);
				added.push(tm);
			});
		}
		this.appObject.onContextMenu(e, added);
	}
});
Ext.reg('taskbar', Wlj.widgets.search.window.TaskBar);
Wlj.widgets.search.window.TaskItem = Ext.extend(Ext.BoxComponent,{
	name : '任务1',
	action : 'aaaa',
	minisized : false,
	serviceObject :false,
	autoEl : {
		tag : 'div',
		cls : 'task_normal'
	},
	taskTemplate : new Ext.XTemplate(
			'<div class=bg>',
				'<p>',
					'<a title={taskName} class=tab href="javascript:void(0);" >',
						'{taskName}',
					'</a>',
				'</p>',
			'</div>'),
	closeTemplate : new Ext.XTemplate('<div class=close />'),
	initComponent : function(){
		Wlj.widgets.search.window.TaskItem.superclass.initComponent.call(this);
		Wlj.TaskMgr.addTask(this);
		if(this.serviceObject){
			this.serviceObject.paddingFlag(true);
		}
	},
	onRender : function(ct, position){
		Wlj.widgets.search.window.TaskItem.superclass.onRender.call(this, ct, position);
		var _this = this;
		var innerEl = this.taskTemplate.append(this.el,{
			taskName : this.name
		});
		this.createWindow();
		this.select(function(){
			_this.creativeCb();
		});
		this.el.on('click',function(){
			_this.itemClick();
		});
		this.el.on('contextmenu', function(eve){
			eve.stopEvent();
			_this.onContextMenu(eve, []);
		});
	},
	createWindow : function(){
		var _this = this;
		this.windowObject = new Wlj.widgets.search.window.Window({
			id : 'window' + this.resId,
			maximizable : true,
			manager : this.ownerCt.windowManager,
			resizable : true,
			minimizable : true,
			top:200,
			left:200,
			height : 400,
			width : 700,
			taskObject : this,
			appObject : this.appObject
		});
	},
	creativeCb : function(){
		this.windowObject.maximize();
		var url = this.builtfunctionurl();
		this.frame = this.windowObject.body.createChild({
			tag : 'iframe',
			style : {
				width:'100%',
				height:'100%',
				border:'none'
			},
			src : url
		});
		Wlj.TaskMgr.toFront();
	},
	builtfunctionurl : function(){
		var url = false;
		if(this.action.indexOf('.jnlp') > 0){//luyy 2014-06-10日修改以加载流程配置页面
			url = this.action.split('.jnlp')[0]+'.jnlp';
		}else if(this.action.indexOf('http://')>=0 ){
			return this.action;
		}
		else if(this.action.indexOf('.jsp') < 0 ){
			url = basepath + '/contents/frameControllers/wlj-function.jsp';
		}else{
			url = this.action.split('.jsp')[0]+'.jsp';
		}
		var turl = this.action.indexOf('?')>=0 ? this.action + '&resId='+this.resId : this.action + '?resId='+this.resId ;
		url += '?' + turl.split('?')[1];
//		_APP.reqIndex ++;
//		_secseq = __secseed + (__secsalt * _APP.reqIndex);
//		url += url.indexOf('?'>=0) ? '&sechand='+_secseq : '?sechand=' + _secseq;
		return url;
	},
	select : function(callback){
		this.minisized = false;
		if(this.ownerCt.currentItem){
			this.ownerCt.currentItem.el.removeClass('task_active');
		}
		this.ownerCt.currentItem = this;
		this.el.addClass('task_active');
		this.windowObject.show(this.el,callback);
		this.ownerCt.windowManager.bringToFront(this.windowObject) ;
	},
	blur : function(){
		this.el.removeClass('task_active');
		this.windowObject.setActive(false);
	},
	close : function(){
		this.windowObject.close();
	},
	closeOthers : function(){
		this.ownerCt.closeWithOut(this);
	},
	removeFromManager : function(){
		if(this.serviceObject){
			this.serviceObject.paddingFlag(false);
		}
		Wlj.TaskMgr.removeTask(this);
		this.ownerCt.remove(this);
	},
	itemClick : function(){
		if(this.minisized || this.windowObject !== this.windowObject.manager.getActive() ){
			this.select();
		}else{
			this.minisize();
		} 
	},
	windowClick : function(){
		this.select();
	},
	minisize : function(){
		this.minisized = true;
		this.windowObject.hide(this.el);
		this.el.removeClass('task_active');
	},
	maxisize : function(){
		this.select();
		this.windowObject.maximize();
	},
	reload : function(){
		var url = this.builtfunctionurl();
		this.frame.dom.src = url;
	},
	destroy : function(){
		this.frame.dom.src = '';
		this.el.removeAllListeners();
		this.el.remove();
		delete this.frame;
		Wlj.widgets.search.window.TaskItem.superclass.destroy.call(this);
	},
	onContextMenu : function(e, added){
		var _this = this;
		var taskMenu = Wlj.search.App.CONTEXT_MENU.TASK_ITEM;
		if(added.length>0 && taskMenu.length>0){
			added.push('-');
		}
		if(taskMenu.length>0){
			Ext.each(taskMenu, function(tm){
				tm.handler = tm.fn.createDelegate(_this);
				added.push(tm);
			});
		}
		this.ownerCt.onContextMenu(e, added);
	}
});
Ext.reg('taskitem', Wlj.widgets.search.window.TaskItem);

Wlj.widgets.search.window.Window = Ext.extend(Ext.Window, {
	guardCfg : {
	},
	guardStyle : {
		backgroundColor : '#000',
		width : '100px',
		height : '20px'
	},
	fitContainer : function(){
        var vs = this.container.getViewSize(false);
        this.setSize(vs.width, vs.height - this.appObject.taskBar.height);
    },
	onRender : function(ct, position){
		Wlj.widgets.search.window.Window.superclass.onRender.call(this, ct, position);
		this.windowBar = new Wlj.widgets.search.window.WindowBar({
			appObject : this.appObject,
			windowObject : this
		});
		this.windowBar.render(this.header.dom);
	},
	listeners : {
		activate : function(window){
    		window.taskObject.windowClick();
    	},
    	close : function(window){
    		window.taskObject.removeFromManager();
			return false;
    	},
    	minimize : function(window){
    		window.taskObject.minisize();
    	}
    },
    destroy : function(){
    	this.windowBar.destroy();
    	delete this.windowBar;
    	Wlj.widgets.search.window.Window.superclass.destroy.call(this);
    }
});
Ext.reg('wljwindow', Wlj.widgets.search.window.Window);

Wlj.widgets.search.window.WindowBar = Ext.extend(Ext.Toolbar, {
	toolbarCls : 'x-toolbar x-windowbar',
	hideBorders : true,
	initComponent : function(){
		Wlj.widgets.search.window.WindowBar.superclass.initComponent.call(this);
		this.createInnerMenuItem();
	},
	onRender : function(ct, position){
		Wlj.widgets.search.window.WindowBar.superclass.onRender.call(this,ct, position);
	},
	createInnerMenuItem : function(){
		var _this = this;
		if(APPUTIL.menuInWindow){
			if(!window.windowRoot){
				var roots= this.appObject.createRootMenuCfg();
				Ext.each(roots,function(r){
					delete r.id;
					r.text = r.NAME;
					r.menu = [];
					r._windowObject = _this.windowObject;
					r.xtype = 'wljinnermenuitem';
				});
				window.windowRoot = roots;
			}
		}
		var comsits = '';
		if(!_this.windowObject.taskObject.serviceObject){
			comsits = false;
		}else{
			comsits = _this.windowObject.taskObject.serviceObject.menuData.COMSITS;
		}
		var hasCom = comsits ? false : true;
		var comsitsMenus = [];
		if(!hasCom){
			Ext.each(comsits.split(','),function(mid){
				var cService = Wlj.ServiceMgr.findServiceByID(mid);
				if(cService && cService.menuData){
					var cMenu = {};
					Ext.apply(cMenu,cService.menuData);
					cMenu.name = cMenu.NAME;
					cMenu.isLeaf = true;
					cMenu.action = cMenu.ACTION;
					cMenu.appObject = _this.appObject;
					cMenu.text = cMenu.NAME;
					cMenu._windowObject = _this.windowObject;
					cMenu.xtype = "wljinnermenuitem";
					comsitsMenus.push(cMenu);
				}
			});
		}
		if(APPUTIL.menuInWindow){
			this.add({
				text : '系统菜单',
				cls:'simple-btn1',
				overCls:'simple-btn1-hover',
				plain:true,
				appObject : _this.appObject,
				menu:{
					items : window.windowRoot
				}
			},{
				xtype: 'tbseparator',
				hidden : hasCom
			});
		}
		this.add({
//			text : '相关功能',
//			cls:'simple-btn1',
//			overCls:'simple-btn1-hover',
//			hidden : hasCom,
//			menu : {
//				items : comsitsMenus
//			}
//		},'-',{
//			text: '常规操作',
//			cls:'simple-btn1',
//			overCls:'simple-btn1-hover',
//			id : 'usersssss'+_this.windowObject.id,
//			menu: {
//            	xtype: 'menu',
//            	plain: true,
//            	items: {
//					xtype: 'buttongroup',
//					title: '',
//					autoWidth: true,
//					columns: 2,
//					defaults: {
//                    	xtype: 'button',
//                    	scale: 'large',
//                    	width: '100%',
//                    	iconAlign: 'left'
//                	},
//                	items: [{
//                		text: '刷新',
//                		iconCls: 'edit',
//                		handler : function(){
//                			Ext.getCmp('usersssss'+_this.windowObject.id).hideMenu();
//                			_this.windowObject.taskObject.reload();
//                		}
//                	},{
//                		text : '任务管理器',
//                		handler : function(){
//                			Ext.getCmp('usersssss'+_this.windowObject.id).hideMenu();
//                			Wlj.TaskMgr.showTasks();
//                		},
//                		iconCls: 'add',
//                		width: 'auto',
//                		tooltip: 'Add user'
//                	},{
//                		colspan: 2,
//                		text: '下一个',
//                		scale: 'small',
//                		handler : function(){
//                			Ext.getCmp('usersssss'+_this.windowObject.id).hideMenu();
//                			var task = _this.windowObject.taskObject;
//                			if(Wlj.TaskMgr.tasks.itemAt(Wlj.TaskMgr.tasks.indexOf(task)+1)){
//                				Wlj.TaskMgr.tasks.itemAt(Wlj.TaskMgr.tasks.indexOf(task)+1).select();
//                			}
//                		}
//                	},{
//                		colspan: 2,
//                		text: '上一个',
//                		scale: 'small',
//                		handler : function(){
//                			Ext.getCmp('usersssss'+_this.windowObject.id).hideMenu();
//                			var task = _this.windowObject.taskObject;
//                			if(Wlj.TaskMgr.tasks.itemAt(Wlj.TaskMgr.tasks.indexOf(task)-1)){
//                				Wlj.TaskMgr.tasks.itemAt(Wlj.TaskMgr.tasks.indexOf(task)-1).select();
//                			}
//                		}
//                	}]
//            	}
//        	}
//		},'-',{
			text : '刷新',
			cls:'simple-btn1',
			overCls:'simple-btn1-hover',
			handler : function(){
				_this.windowObject.taskObject.reload();
			}
		},'-',{
			text : '返回首页',
			cls:'simple-btn1',
			overCls:'simple-btn1-hover',
			handler : function(){
				Wlj.TaskMgr.tasks.each(function(task){
					task.minisize();
				});
			}
		},'-',{
			text : '登出',
			cls:'simple-btn1',
			overCls:'simple-btn1-hover',
			handler : function(){
				top.window.location.href = basepath+'/j_spring_security_logout';
			}
		});
		delete comsitsMenus;
	}
});
Ext.reg('wljwindowbar', Wlj.widgets.search.window.Window);


Wlj.widgets.search.window.InnerMenu = Ext.extend(Ext.menu.Item ,{
	windowObject : false,
	initComponent : function(){
		var roots= this.appObject.createSubMenuCfg(this.ID);
		var _this = this;
		Ext.each(roots,function(r){
			r.id = r.id+'_innermenu_'+_this.id;
			r.text = r.NAME;
			r.menu = [];
			r._windowObject = _this._windowObject;
			r.xtype = 'wljinnermenuitem';
		});
		this.menu = this.isLeaf ? false : roots;
		Wlj.widgets.search.window.InnerMenu.superclass.initComponent.call(this);
	},
	handler : function(){
		if(this.ACTION){
			Wlj.ServiceMgr.findServiceByID(this.ID).execute();
		}
	}
});
Ext.reg('wljinnermenuitem', Wlj.widgets.search.window.InnerMenu);