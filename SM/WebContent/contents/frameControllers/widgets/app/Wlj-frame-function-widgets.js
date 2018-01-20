Ext.ns('Wlj.frame.functions.app.widgets');

/**
 * 页面查询条件面板类
 */
Wlj.frame.functions.app.widgets.SearchContainer = Ext.extend(Ext.Panel,{
	vs : false,
	columnCount : 4,
	layout : 'fit',
	WCLP : false,
	needCloseLable4DCF : false,
	multiSelectSeparator : false,
	dyfield : new Array(),
	buttonCfg : {},
	
	reminding : '提示',
	conditionErrorText : '请填写必要的查询条件',
	
	initComponent : function(){
		var _this = this;
		_this._APP.fireEvent('beforeconditioninit',_this,_this._APP);
		this.height = this.vs.height;
		this.width = this.vs.width;
		Wlj.frame.functions.app.widgets.SearchContainer.superclass.initComponent.call(this);
		_this._APP.fireEvent('afterconditioninit',_this,_this._APP);
		_this._APP.fireEvent('beforeconditionrender',_this, _this._APP);
	},
	onRender : function(ct, position){
		var vs =  this.vs;
		var _this = this;
		
		var columnCount = 0;
		if(vs.width > 1024){
			columnCount = 4;
		} else {
			columnCount = 3;
		}
		this.columnCount = columnCount;
		this.createColumnItems(columnCount);
		Wlj.frame.functions.app.widgets.SearchContainer.superclass.onRender.call(this,ct,position);
		this.el.on('contextmenu', function(eve){
			eve.stopEvent();
			_this.onContextMenu(eve, []);
		});
		_this._APP.fireEvent('afterconditionrender',_this, _this._APP);
	},
	destroy : function(){
		this.dyfield = [];
		this.purgeListeners();
		this.itemsArray = null;
		this.searchPanel.purgeListeners();
		this.searchPanel = null;
		for(var key in this.buttonCfg){
			this[key+'Button'] = null;
			delete this[key+'Button'];
		}
		this.buttonCfg = {};
		this.DropZone.destroy();
		Wlj.frame.functions.app.widgets.SearchContainer.superclass.destroy.call(this);
	},
	initEvents : function(){
		this.addEvents({
			beforeconditionadd : true,
			conditionadd : true,
			beforeconditionremove : true,
			conditionremove : true,
			beforedyfieldclear : true,
			afterdyfieldclear : true,
			beforecondtitionfieldvalue : true,
			condtitionfieldvalue : true
		});
	},
	createColumnItems : function(columnCount){
		var _this = this;
		var items = this.items;
		if(!items){
			return false;
		}
		if(columnCount<1){
			return false;
		}
		var columns = new Array();
		for(var i = 0; i<columnCount;i++){
			var columnCfg = {};
			columnCfg.layout = 'form';
			columnCfg.defaultType = 'textfield';
			columnCfg.columnWidth = 1/columnCount;
			columnCfg.items = [];
			Ext.each(this.itemsArray,function(it){
				if(it.dataType && WLJDATATYPE[it.dataType]){
					it.xtype = WLJDATATYPE[it.dataType].getFieldXtype();
					Ext.applyIf(it, WLJDATATYPE[it.dataType].getFieldSpecialCfg());
				}
				if(_this.itemsArray.indexOf(it) % columnCount == i){
					if(it.cAllowBlank === false){
						it.allowBlank = false;
						it.fieldLabel = WLJTOOL.addBlankFlag(it.text);
					}
					it.anchor = '90%';
					columnCfg.items.push(it);
				}
			});
			columns.push(columnCfg);
		}
		var buttonCfg = _this.buttonCfg;
		var buttons = [];
		for(var key in buttonCfg){
			var cfg = {};
			Ext.apply(cfg,buttonCfg[key]);
			cfg.handler = buttonCfg[key].fn.createDelegate(this);
			_this[key+'Button'] = new Ext.Button(cfg);
			buttons.push(_this[key+'Button']);
		}
		
		_this.searchPanel = new Ext.form.FormPanel({
			frame : true,
			layout : 'column',
			items : columns,
			buttons : buttons,
			listeners : {
				'afterrender' : function(){
					_this.createDynaticSearchTarget();
					this.el.on('keydown',function(e,t,o){
						if(!e.ctrlKey && !e.altKey && e.keyCode === e.ENTER){
							e.stopEvent();
							_this.searchHandler();
						}
					});
				}
			}
		});
		_this.add(_this.searchPanel) ;
	},
	hasConditionButton : function(btn){
		if (this[btn+'Button'] && this.searchPanel.buttons.indexOf(this[btn+'Button'])> -1) {
			return true;
		}
		return false;
	},
	enableConditionButton : function(btn){
		if (this.hasConditionButton(btn)) {
			this[btn+'Button'].enable();
		}
	},
	disableConditionButton : function(btn){
		if (this.hasConditionButton(btn)) {
			this[btn+'Button'].disable();
		}
	},
	showConditionButton : function(btn){
		if (this.hasConditionButton(btn)) {
			this[btn+'Button'].show();
		}
	},
	hideConditionButton : function(btn){
		if (this.hasConditionButton(btn)) {
			this[btn+'Button'].hide();
		}
	},
	getNumberedColumn : function(max_min){
		var _this = this;
		var res = true;//true:取最大长度；false：取最小长度；
		if(max_min){
			res = true;
		}else{
			res = false;
		}
		var p = _this.searchPanel;
		var resi = 0;
		p.items.each(function(c){
			if(c.xtype !== 'hidden' && c.items){
				if(res && c.items.getCount() > p.get(resi).items.getCount()){
					resi = p.items.indexOf(c);
				}else if(!res &&  c.items.getCount() < p.get(resi).items.getCount()){
					resi = p.items.indexOf(c);
				}
			}
		});
		return resi;
	},
	fixSearchHeight : function(){
		var _this = this;
		var count = _this.searchPanel.items.get(_this.getNumberedColumn(true)).items.getCount();
		_this._APP.setSearchSize({
			height : 39 + 33 * count + 6
		});
	},
	removeAllDyField : function(){
		if(this.dyfield.length == 0){
			return false;
		}
		var clearable = this.fireEvent('beforedyfieldclear', this, this.searchPanel, this.dyfield);
		if(clearable === false){
			return false;
		}
		while(this.dyfield.length>0){
			var df = this.dyfield.pop();
			df.ownerCt.remove(df);
		}
		this.searchPanel.doLayout();
		this.fixSearchHeight();
		this.fireEvent('afterdyfieldclear', this, this.searchPanel, this.dyfield);
	},
	createDynaticSearchTarget : function(){
		var _this = this;
		this.DropZone = new Ext.dd.DropTarget(_this.searchPanel.el.dom, {
			ddGroup : 'searchDomainDrop',
			notifyDrop  : function(ddSource, e, data){
				var dataInfo = data.tile.data;
				if(dataInfo.name){
					var targetField = _this.addField(dataInfo.name);
					if(targetField.setValue){
						var setAble = _this.fireEvent('beforecondtitionfieldvalue',targetField,dataInfo,dataInfo.value);
						if(setAble){
							targetField.setValue(dataInfo.value);
							_this.fireEvent('condtitionfieldvalue',targetField,dataInfo,dataInfo.value);
						}
					}
				}
			}
		});	
	},
	
	addField : function(fieldCfg){
		var _this = this;
		var targetField = _this.searchPanel.getForm().findField(fieldCfg);
		if(!targetField){
			var fCfg = this.getFieldCfg(fieldCfg);
			if(!fCfg) return false;
			var columnIndexT = _this.getNumberedColumn(false);
			var addable = _this.fireEvent('beforeconditionadd',fCfg,columnIndexT,_this.searchPanel);
			if(addable === false){
				return false;
			}
			var targetField = _this.searchPanel.items.get(columnIndexT).add(fCfg);
			_this.dyfield.push(targetField);
			_this.searchPanel.doLayout();
		}
		return targetField;
	},
	
	getFieldCfg : function(fieldCfg){
		var _this = this;
		if(Ext.isString(fieldCfg)){
			var f = _this._APP.getFieldsByName(fieldCfg);
			if(!f){
				return false;
			}
			var fCfg = _this.formatFieldCfg(f);
			return fCfg;
		}else{
			var fCfg = _this.formatFieldCfg(fieldCfg);
			return fCfg;
		}
	},
	
	formatFieldCfg : function(f){
		var _this = this;
		var fCfg = {};
		if(f.translateType){
			fCfg.xtype = f.multiSelect?'lovcombo':'combo';
			fCfg.store = _this._APP.lookupManager[f.translateType];
			if(!fCfg.store){
				Ext.error('字段【'+f.text+'】数据字典映射项store【'+f.translateType+'】获取错误，请检查[lookupTypes|localLookup]项配置');
				return false;
			}
			fCfg.valueField = 'key';
			fCfg.displayField = 'value';
			fCfg.editable = typeof f.editable === 'boolean' ? f.editable : false;
			fCfg.forceSelection = true;
			fCfg.triggerAction = 'all';
			fCfg.mode = 'local';
			fCfg.hiddenName = f.name;
			fCfg.separator = f.multiSeparator?f.multiSeparator:_this.multiSelectSeparator;
		}else{
			fCfg.name = f.name;
			if(f.dataType && WLJDATATYPE[f.dataType]){
				fCfg.xtype = WLJDATATYPE[f.dataType].getFieldXtype();
				Ext.applyIf(fCfg, WLJDATATYPE[f.dataType].getFieldSpecialCfg());
			}
		}
		fCfg.fieldLabel = f.text ? f.text : f.name;
		fCfg.anchor = '90%';
		Ext.applyIf(fCfg, f);
		delete fCfg.allowBlank;
		if(f.cAllowBlank === false){
			fCfg.allowBlank = false;
			fCfg.fieldLabel = WLJTOOL.addBlankFlag(f.text);
		}
		fCfg.listeners = {
			'afterrender':function(fitem){
				if(_this.needCloseLable4DCF){
					var rTemplate = new Ext.Template(
					'<div class="del-searchField"></div>');
					var t = rTemplate.insertFirst(fitem.el.parent());
					fitem.closeLabel = t;
					t.style.zIndex = 15000;
					t.onclick = function(){
						_this.removeField(fitem);
					};
				}
				_this.fixSearchHeight();
				_this.fireEvent('conditionadd',fitem,_this.searchPanel);
			}
		};
		return fCfg;
	},
	
	removeField : function(field){
		
		var _this = this;
		
		if(Ext.isString(field)){
			var fieldOb = this.searchPanel.getForm().findField(field);
			if(fieldOb){
				var removeable = _this.fireEvent('beforeconditionremove',fieldOb,_this.searchPanel);
				if(removeable === false){
					return false;
				}
				_this.dyfield.remove(fieldOb);
				fieldOb.ownerCt.remove(fieldOb);
				_this.searchPanel.doLayout();
				_this.fixSearchHeight();
				_this.fireEvent('conditionremove',_this.searchPanel);
			}
		}else if(Ext.isObject(field) && field.ownerCt){
			var removeable = _this.fireEvent('beforeconditionremove',field,_this.searchPanel);
			if(removeable === false){
				return false;
			}
			_this.dyfield.remove(field);
			field.ownerCt.remove(field);
			_this.searchPanel.doLayout();
			_this.fixSearchHeight();
			_this.fireEvent('conditionremove',_this.searchPanel);
		}else if(Ext.isObject(field) && !field.ownerCt){
			if(!field.name){
				return false;
			}
			var fieldOb = this.searchPanel.getForm().findField(field.name);
			if(fieldOb){
				var removeable = _this.fireEvent('beforeconditionremove',fieldOb,_this.searchPanel);
				if(removeable === false){
					return false;
				}
				_this.dyfield.remove(fieldOb);
				fieldOb.ownerCt.remove(fieldOb);
				_this.searchPanel.doLayout();
				_this.fixSearchHeight();
				_this.fireEvent('conditionremove',_this.searchPanel);
			}
		}else{
			return false;
		}
		
	},
	
	getField : function(field){
		var _this = this;
		if(Ext.isString(field)){
			return this.searchPanel.getForm().findField(field);
		}else if(Ext.isObject(field)){
			if(!field.name){
				return false;
			}else{
				return this.searchPanel.getForm().findField(field.name);
			}
		}else{
			return false;
		}
		
	},
	
	searchHandler : function(){
		var _this = this;
		if(!_this.searchPanel.getForm().isValid()){
			Ext.Msg.alert(_this.reminding,_this.conditionErrorText);
			return false;
		}
		pars = _this.searchPanel.getForm().getFieldValues();
		for(var key in pars){
			if(!pars[key]){
				delete pars[key];
			} else if(typeof pars[key] === 'string'){
				pars[key] = pars[key].replace(/\s+$|^\s+/g,'');
				pars[key] = pars[key].replace(/'/g,'\'\'');
			}
		}
		_this._APP.setSearchParams(pars,true,true);
	
	},
	resetCondition : function(removeDy){
		if(removeDy===true){
			this.removeAllDyField();
		}
		this.searchPanel.getForm().reset();
	},
	resetHandler : function(){
		this.resetCondition(true);
	},
	clearHandler : function(){
		this.resetCondition(false);
	},
	onContextMenu : function(eve, added){
		var cmenus =Ext.isString(WLJUTIL.contextMenus.condition) ? eval(WLJUTIL.contextMenus.condition) : WLJUTIL.contextMenus.condition;
		for(var key in cmenus){
			var omenu = {};
			omenu.text = cmenus[key].text;
			omenu.handler = cmenus[key].fn.createDelegate(this);
			added.push(omenu);
		}
		added.push('-');
		this._APP.onContextMenu(eve, added);
	}
}); 
Ext.reg('searchcontainer', Wlj.frame.functions.app.widgets.SearchContainer);

/**
 * 页面查询结果列表容器;
 */
Wlj.frame.functions.app.widgets.ResultContainer = Ext.extend(Ext.Panel, {
	
	needGrid : true,
	needTbar : true,
	autoLoadGrid : true,
	
	singleSelect : false,
	multiSelectSeparator : false,
	
	gridLockedHole : false,
	gridLockedOnce : false,
	
	suspendViews : false,
	suspendViewsWidth : 0,
	alwaysLockCurrentView : false,
	
	tbarButtonAlign : 'left',
	tbarViewAlign : 'left',
	
	needRN : false,
	rnWidth : false,
	
	CREATE_VIEW:'createView',
	EDIT_VIEW:'editView',
	DETAIL_VIEW:'detailView',
	GRID_VIEW : 'gridView',
	
	createView : true,
	editView : true,
	detailView : true,
	
	createFieldsCopy : false,
	editFieldsCopy : false,
	detailFieldsCopy :false,
	
	createFormViewer : false,
	editFormViewer : false,
	detailFormViewer : false,
	
	createValidates : false,
	editValidates : false,
	
	createLinkages : false,
	editLinkages : false,
	
	createViewText : '新增',
	editViewText : '修改',
	detailViewText : '详情',	
	gridViewText : '列表',
	
	pageSize : 10,
	
	url : false,
	dataFields : [],
	jsonRoot : 'json.data',
	jsonCount : 'json.count',
	
	store : false,
	currentParams : {},
	formButtons : false,
	viewPanel : {
		createView : false,
		editView : false,
		detailView : false
	},
	
	easingStrtegy : false,
	
	customerViewPanels : [],
	/**
	 * resize事件
	 */
	beresized : function(p,aw,ah,rw,rh){
		var h = parseInt(ah, 10), w = parseInt(aw, 10);
		var vh,vw;
		var _this = this;
		if(Ext.isNumber(h)){
			if(_this.needTbar){
				var theight = this.tbar.getViewSize().height;
				vh = h - theight;/**to check**/
			}else{
				vh = h;
			}
			if(!this._APP.needCondition){
				vh = vh - 8;
			}
			if(vh < 0){
				vh = 0;
			}
		}
		if(Ext.isNumber(w)){
			vw = w;
		}
		Ext.iterate(this.viewPanel, function(key, panel){
			if(panel){
				if(vh)
					panel.setHeight(vh);
				if(vw){
					if(panel.rendered){
						if(!_this.suspendViews)
							panel.setWidth(vw);
						if(panel !== _this.currentView){
							panel.el.applyStyles({
								left : vw + 'px'
							});
						}else{
							panel.el.applyStyles({
								left : (vw - panel.el.getViewSize().width) + 'px'
							});
						}
					}
				}
			}
		});
		Ext.each(this.customerViewPanels, function(panel){
			if(vh)
				panel.setHeight(vh);
			if(vw){
				if(panel.rendered){
					if(!_this.suspendViews)
						panel.setWidth(vw);
					if(panel !== _this.currentView){
						panel.el.applyStyles({
							left : vw + 'px'
						});
					}else{
						panel.el.applyStyles({
							left : (vw - panel.el.getViewSize().width) + 'px'
						});
					}
				}
			}
		});
		if(this.needGrid){
			if(vh)
				this.searchGridView.setHeight(vh);
			if(vw){
				this.searchGridView.setWidth(vw);
				if(this.gridOuted){
					Ext.fly(this.searchGridView.el).applyStyles({
						marginLeft : -vw + 'px'
					});
				}
			}
		}
	},
	
	initComponent : function(){
		var _this = this;
		_this.validateFieldsCfg();
		_this._APP.fireEvent('beforeresultinit', _this, _this._APP);
		if(_this.needTbar){
			var pbs = [];
			if(_this.tbarButtonAlign === 'right'){
				pbs.push('->');
			}
			if(Ext.isArray(this.tbar)){
				for(var i=0;i<this.tbar.length;i++){
					if(Ext.isObject(this.tbar[i]) && this.tbar[i].text){
						if(!this.tbar[i].iconCls){
							this.tbar[i].iconCls = 'ico-w-'+Math.ceil(Math.random()*100);
						}
						pbs.push(this.tbar[i]);
					}
				}
			}
			if(_this.tbarViewAlign === 'right'){
				if(_this.tbarButtonAlign !== 'right'){
					pbs.push('->');
				}else{
					if(pbs.length>0)
						pbs.push('-');
				}
			}else{
				if(pbs.length>0){
					pbs.push('-');
				}
			}
			
			if(!_this.suspendViews)
				pbs.push({
					text : this.gridViewText,
					hidden : !this.needGrid,
					handler : function(){
						_this.gridMoveIn();
					},
					listeners : {
						afterrender : function(button){
							_this.gridButton = button;
							_this.gridButton.toggle(true,true);
						}
					}
				});
			this.tbar = pbs;
		}else{
			delete this.tbar;
		}
		Wlj.frame.functions.app.widgets.ResultContainer.superclass.initComponent.call(this);
		_this._APP.fireEvent('afterresultinit', _this, _this._APP);
		_this._APP.fireEvent('beforeresultrender', _this, _this._APP);
	},
	validateFieldsCfg : function(){
		var dfs = this.dataFields;
		var targetFs = [];
		for(var i=0;i<dfs.length; i++){
			if(!dfs[i].name){
				continue;
			}else{
				if(!dfs[i].text){
					dfs[i].text = dfs[i].name;
					dfs[i].hidden = true;
				}
				targetFs.push(dfs[i]);
			}
		}
		this.dataFields = targetFs;
	},
	onRender : function(ct, position){
		var _this = this;
		this.width = this.vs.width;
		this.height = this.vs.height;
		Wlj.frame.functions.app.widgets.ResultContainer.superclass.onRender.call(this, ct, position);
		if(this.needGrid){
			this.initStore();
			this.searchGridView = new Wlj.frame.functions.app.widgets.SearchGrid({
				store : this.store,
				searchDomain : this,
				_APP : this._APP,
				pageSize : this.pageSize,
				needRN : this.needRN,
				rnWidth : this.rnWidth,
				easingStrtegy : this.easingStrtegy
			});
			this.add(this.searchGridView);
			this.searchGridView.on('recordselect', function(record, store,tile){
				_this.fireEvent('recordselect', record, store,tile);
				if(_this.currentView){
					var showable = _this.fireEvent('beforeviewshow',_this.currentView);
					if(showable === false){
						return false;
					}else{
						if(_this.currentView.hasListener('moveout')){
							_this.currentView.fireEvent('moveout');
						}
						if(_this.currentView.hasListener('movein')){
							_this.currentView.fireEvent('movein',record);
						}
						_this.fireEvent('viewshow',_this.currentView);
					}
				}
			});
			this.searchGridView.on('rowdblclick', function(tile, record){
				_this.fireEvent('rowdblclick', tile, record);
			});
		}
		this.initViews();
		if(this.needGrid){
			if(this.autoLoadGrid){
				var pars = this._APP.searchDomain.searchPanel.getForm().getFieldValues();
				for(var key in pars){
					if(!pars[key]){
						delete pars[key];
					}
				}
				this._APP.setSearchParams(pars,true,true);
			}
		}else{
			this.showView(this.firstView());
		}
		this.initEvents();
		var _this = this;
		this.el.on('contextmenu', function(eve){
			eve.stopEvent();
			_this.onContextMenu(eve, []);
		});
	},
	afterRender : function(){
		Wlj.frame.functions.app.widgets.ResultContainer.superclass.afterRender.call(this);
		this._APP.fireEvent('afterresultrender', this, this._APP);
	},
	initEvents : function(){
		this.addEvents({
			beforeviewshow : true,
			viewshow : true,
			beforeviewhide : true,
			viewhide : true,
			recorddelete : true,
			beforevalidate :true,
			validate : true,
			recordselect : true,
			beforecreateviewrender : true,
			aftercreateviewrender : true,
			beforeeditviewrender : true,
			aftereditviewrender : true,
			beforedetailviewrender : true,
			afterdetailviewrender : true,
			rowdblclick : true
		});
		this.on('resize',this.beresized);
	},
	initViews : function(){
		var thisVs = this.vs;
		var _this = this;
		
		var supportWidth = 0;
		if(!this.suspendViews){
			supportWidth = thisVs.width - 12;
		}else{
			if(this.suspendViewsWidth>thisVs.width-12-this.rnWidth){
				supportWidth = thisVs.width-12-this.rnWidth -30;
			}else{
				supportWidth= this.suspendViewsWidth;
			}
		}
		
		function buildWidth(supportWidth, vscontainer, formCfg){
			if(formCfg.suspendFitAll){
				return vscontainer.width;
			}else if(Ext.isNumber(formCfg.suspendWidth)){
				if(formCfg.suspendWidth>1){
					return formCfg.suspendWidth;
				}else{
					return vscontainer.width * formCfg.suspendWidth;
				}
			}else{
				return supportWidth;
			}
		}
		if(this[this.CREATE_VIEW]){
			var CVIEWCFG = {
				ownerDomain : this,
				ownerApp : this._APP,
				vs : {
					height : thisVs.height - 37,
					width : _this.suspendViews ? buildWidth(supportWidth, thisVs, this.createFormCfgs) : supportWidth
				},
				title : this[this.CREATE_VIEW+'Text'],
				formViewer : this.createFormViewer,
				fields : this.createFieldsCopy,
				validates : this.createValidates,
				linkages : this.createLinkages,
				baseType : this.CREATE_VIEW,
				suspended : this.suspendViews,
				tabIco : 'ico-w-6'
			};
			Ext.apply(CVIEWCFG,this.createFormCfgs);
			this.viewPanel[this.CREATE_VIEW] = new Wlj.frame.functions.app.widgets.CView(CVIEWCFG);
			this.viewPanel[this.CREATE_VIEW].on({
				moveout : {
					fn : this.viewPanel[this.CREATE_VIEW].reset,
					scope : this.viewPanel[this.CREATE_VIEW],
					delay : 0
				},
				beforevalidate : {
					fn : function(view, panel){
						return _this.fireEvent('beforevalidate', view, panel);
					},
					scope : _this,
					delay : 0
				},
				validate : {
					fn : function(view, panel, error){
						_this.fireEvent('validate', view, panel,error);
					},
					scope : _this,
					delay : 0
				},
				afterviewrender : {
					fn : function(view){
						_this.fireEvent('aftercreateviewrender', view);
					},
					scope : _this,
					delay : 0
				}
			});
			//this.viewPanel[this.CREATE_VIEW].render(this.body);
		}
		if(this[this.EDIT_VIEW]){
			var EVIEWCFG = {
				ownerDomain : this,
				ownerApp : this._APP,
				vs : {
					height : thisVs.height - 37,
					width : _this.suspendViews ? buildWidth(supportWidth, thisVs, this.editFormCfgs) : supportWidth
				},
				title : this[this.EDIT_VIEW+'Text'],
				formViewer : this.editFormViewer,
				fields : this.editFieldsCopy,
				validates : this.editValidates,
				linkages : this.editLinkages,
				baseType : this.EDIT_VIEW,
				suspended : this.suspendViews,
				tabIco : 'ico-w-79'
			};
			Ext.apply(EVIEWCFG, this.editFormCfgs);
			this.viewPanel[this.EDIT_VIEW] =new Wlj.frame.functions.app.widgets.CView(EVIEWCFG);
			this.viewPanel[this.EDIT_VIEW].on({
				moveout : {
					fn : this.viewPanel[this.EDIT_VIEW].reset,
					scope : this.viewPanel[this.EDIT_VIEW],
					delay : 0
				},
				movein : {
					fn : this.viewPanel[this.EDIT_VIEW].setRecord,
					scope : this.viewPanel[this.EDIT_VIEW],
					delay : 0
				},
				beforeloadrecord : {
					fn : function(view, record){
						return _this._APP.fireEvent('beforeeditload', view, record);
					},
					scope : this.viewPanel[this.EDIT_VIEW],
					delay : 0
				},
				loadrecord : {
					fn : function(view, record){
						_this._APP.fireEvent('aftereditload', view, record);
					},
					scope : this.viewPanel[this.EDIT_VIEW],
					delay : 0
				},
				beforevalidate : {
					fn : function(view, panel){
						return _this.fireEvent('beforevalidate', view, panel);
					},
					scope : _this,
					delay : 0
				},
				validate : {
					fn : function(view, panel, error){
						_this.fireEvent('validate', view, panel,error);
					},
					scope : _this,
					delay : 0
				},
				afterviewrender : {
					fn : function(view){
						_this.fireEvent('aftereditviewrender', view);
					},
					scope : _this,
					delay : 0
				}
			});
			//this.viewPanel[this.EDIT_VIEW].render(this.body);
		}
		if(this[this.DETAIL_VIEW]){
			var DVIEWCFG = {
				ownerDomain : this,
				ownerApp : this._APP,
				svButton : false,
				vs : {
					height : thisVs.height - 37,
					width :_this.suspendViews ? buildWidth(supportWidth, thisVs, this.detailFormCfgs) : supportWidth
				},
				title : this[this.DETAIL_VIEW+'Text'],
				formViewer : this.detailFormViewer,
				fields : this.detailFieldsCopy,
				baseType : this.DETAIL_VIEW,
				suspended : this.suspendViews,
				tabIco : 'ico-w-27'
			};
			Ext.apply(DVIEWCFG, this.detailFormCfgs);
			this.viewPanel[this.DETAIL_VIEW] =new Wlj.frame.functions.app.widgets.CView(DVIEWCFG);
			this.viewPanel[this.DETAIL_VIEW].on({
				moveout : {
					fn : this.viewPanel[this.DETAIL_VIEW].reset,
					scope : this.viewPanel[this.DETAIL_VIEW],
					delay : 0
				},
				movein : {
					fn : this.viewPanel[this.DETAIL_VIEW].setRecord,
					scope : this.viewPanel[this.DETAIL_VIEW],
					delay : 0
				},
				beforeloadrecord : {
					fn : function(view, record){
						return _this._APP.fireEvent('beforedetailload', view, record);
					},
					scope : this.viewPanel[this.EDIT_VIEW],
					delay : 0
				},
				loadrecord : {
					fn : function(view, record){
						_this._APP.fireEvent('afterdetailload', view, record);
					},
					scope : this.viewPanel[this.EDIT_VIEW],
					delay : 0
				},
				afterviewrender : {
					fn : function(view){
						_this.fireEvent('afterdetailviewrender', view);
					},
					scope : _this,
					delay : 0
				}
			});
			//this.viewPanel[this.DETAIL_VIEW].render(this.body);
		}
		this.createCustomerViews();
	},
	clearViews : function(){
		
		this.createFormViewer = false;
		this.createFieldsCopy = false;
		this.createFormCfgs = false;
		
		this.editFormViewer = false;
		this.editFieldsCopy = false;
		this.editFormCfgs = false;
		
		this.detailFormViewer = false;
		this.detailFieldsCopy = false;
		this.detailFormCfgs = false;
		
	},
	createCustomerViews : function(){
		var customerViewCfg = this._APP.customerView;
		if(!Ext.isArray(customerViewCfg)){
			return;
		}
		var _this = this;
		var thisVs = this.el.getViewSize();
		
		var supportWidth = 0;
		if(!this.suspendViews){
			supportWidth = thisVs.width;
		}else{
			if(this.suspendViewsWidth>thisVs.width-12-this.rnWidth){
				supportWidth = thisVs.width-12-this.rnWidth -30;
			}else{
				supportWidth= this.suspendViewsWidth;
			}
		}
		
		Ext.each(customerViewCfg, function(cvc){
			var vTitile = cvc.title;
			var viewType = cvc.type;
			delete cvc.title;
			
			function buildWidth(supportWidth, vscontainer, formCfg){
				if(formCfg.suspendFitAll){
					return vscontainer.width;
				}else if(Ext.isNumber(formCfg.suspendWidth)){
					if(formCfg.suspendWidth>1){
						return formCfg.suspendWidth;
					}else{
						return vscontainer.width * formCfg.suspendWidth;
					}
				}else{
					return supportWidth;
				}
			}
			
			var cvcCfg = {
				title : vTitile? vTitile : 'Bview',
				vs : {
					height : thisVs.height - (_this.needTbar ? 37 : 0),
					width : _this.suspendViews? buildWidth(supportWidth, thisVs, cvc) : supportWidth
				},
				ownerDomain : _this,
				ownerApp : _this._APP,
				suspended : _this.suspendViews
			};
			Ext.apply(cvcCfg, cvc);
			
			if(!cvcCfg.tabIco){
				cvcCfg.tabIco = 'ico-w-'+Math.ceil(Math.random()*100);
			}
			
			var cvcp = false;
			if(viewType === 'form'){
				cvcp = new Wlj.frame.functions.app.widgets.FormView(cvcCfg);
			}else if(viewType === 'grid'){
				cvcp = new Wlj.frame.functions.app.widgets.GridView(cvcCfg);
			}else{
				cvcp = new Wlj.frame.functions.app.widgets.BView(cvcCfg);
			}
			_this.customerViewPanels.push(cvcp);
			//cvcp.render(_this.body);
		});
	},
	showView : function(viewType){
		if(!viewType){
			return;
		}
		var hideable = this.hideCurrentView();
		if(hideable === false){
			return false;
		}
		if(this.needGrid){
			this.gridMoveOut();
		}
		var thisVs = this.el.getViewSize();
		if(!viewType.rendered){
			viewType.render(this.body);
			viewType.el.applyStyles({
				left :  thisVs.width + 'px'
			});
		}
		var showable = this.fireEvent('beforeviewshow',viewType);
		if(showable === false){
			return false;
		}
		/***
		 * TODO FIT ALL
		 */
		var viewVs = viewType.el.getViewSize();
		var ccEl = viewType.el;
		ccEl.animate({
			left : {
				to : thisVs.width - viewVs.width,
				from : thisVs.width
			}
		},
		0.5,
		null,
		null,
		'run');
		this.currentView = viewType;
		viewType.fireEvent('movein',this.searchGridView?this.searchGridView.getSelected()[this.searchGridView.getSelected().length-1]:'');
		if(this.needTbar){
			this.currentView.togogleButton.toggle(true,true);
		}
		this.fireEvent('viewshow',this.currentView);
	},
	hideCurrentView : function(){
		if(this.currentView){
			var hideable = this.fireEvent('beforeviewhide',this.currentView);
			if(hideable === false){
				return false;
			}
			/**
			 * TODO FIT ALL
			 */
			var thisVs = this.el.getViewSize();
			var cvEl = this.currentView.el;
			cvEl.animate({
				left:{from:cvEl.getLeft(),to:thisVs.width}
			},
			0.5,
			null,
			'easeOut',
			'run');
			this.currentView.fireEvent('moveout');
			this.fireEvent('viewhide',this.currentView);
			if(this.needTbar){
				this.currentView.togogleButton.toggle(false,true);
			}
			this.currentView = false;
		}
	},
	initStore : function(){
		var url = this.url;
		if(!url){
			return;
		}
		var fields = this.createStoreFields();
		var jsonRoot = this.jsonRoot;
		var jsonCount = this.jsonCount;
		var store = new Ext.data.Store({
			restful:true,	
	        proxy : new Ext.data.HttpProxy({url:url}),
	        reader : new Ext.data.JsonReader({
	        	totalProperty : jsonCount,
	        	root : jsonRoot
	        },fields)
		});
		this.store = store;
	},
	createStoreFields : function(){
		var fields = this.dataFields;
		var sFields = [];
		Ext.each(fields, function(f){
			if(f.dataType && WLJDATATYPE[f.dataType]){
				f.type = WLJDATATYPE[f.dataType].getStoreType();
				Ext.applyIf(f, WLJDATATYPE[f.dataType].getStoreSpecialCfg());
			}
			sFields.push(f);
		});
		return sFields;
	},
	gridMoveOut : function(){
		if(this.gridLockedHole){
			return;
		}
		var _this = this;
		var layTargetEl = Ext.fly(_this.getLayoutTarget());
		_this.searchGridView.el.animate({
			marginLeft : {from : 0, to : -layTargetEl.getViewSize().width}
		},
		.35,
		 null,      
		 'easeOut', 
		'run' );
		_this.gridOuted = true;
		if(this.needTbar){
			_this.gridButton.toggle(false,true);
		}
	},
	gridMoveIn : function(){
		if(this.gridLockedHole){
			return;
		}
		var _this = this;
		_this.hideCurrentView();
		var layTargetEl = Ext.fly(_this.getLayoutTarget());
		_this.searchGridView.el.animate({
			marginLeft : {to : 0, from : -layTargetEl.getViewSize().width}
		},
		.35,
		 null,      
		 'easeOut', 
		'run' );
		_this.gridOuted = false;
		if(this.needTbar){
			_this.gridButton.toggle(true,true);
		}
	},
	destroy : function(){
		for(var key in this.viewPanel){
			if(this.viewPanel[key].destroy){
				this.viewPanel[key].destroy();
				this.viewPanel[key] = false;
			}
		}
		this.clearViews();
		this.currentView = false;
		for(var i=0;i<this.customerViewPanels.length;i++){
			this.customerViewPanels[i].destroy();
		}
		this.customerViewPanels=[];
		if(this.searchGridView && this.searchGridView.destroy){
			this.searchGridView.destroy();
			this.searchGridView = null;
		}
		if(this.store){
			this.store.removeAll();
			this.store.destroy();
			this.store = null;
		}
		this.url = false;
		this.dataFields = [];
		this.jsonRoot = 'json.data';
		this.jsonCount = 'json.count';
		this.currentParams = {};
		Wlj.frame.functions.app.widgets.ResultContainer.superclass.destroy.call(this);
		
	},
	onMetaAdd : function(field){
		this.dataFields.push(field);
		this.onMetaChange();
		this.searchGridView.onMetaAdd(field);
	},
	onMetaAddAfter : function(addField, theField){
		var theIndex = this.getFieldIndex(theField);
		this.dataFields.splice(theIndex+1,0,addField);
		this.onMetaChange();
		this.searchGridView.onMetaAddByIndex(addField, theIndex+1);
	},
	onMetaAddBefore : function(addField, theField){
		var theIndex = this.getFieldIndex(theField);
		this.dataFields.splice(theIndex,0,addField);
		this.onMetaChange();
		this.searchGridView.onMetaAddByIndex(addField, theIndex);
	},
	getFieldIndex : function(theField){
		for(var i=0; i<this.dataFields.length; i++){
			if(this.dataFields[i].name === theField){
				return i;
			}
		}
		return i;
	},
	onMetaRemove : function(field){
		for(var i=0;i<this.dataFields.length;i++){
			if(this.dataFields[i].name === field){
				this.dataFields.remove(this.dataFields[i]);
				break;
			}
		}
		this.onMetaChange();
		this.searchGridView.onMetaRemove(field);
	},
	storeMetaChange : function(){
		var readerMeta = {
			totalProperty : this.jsonCount,
			root : this.jsonRoot,
			fields : this.createStoreFields()
		};
		this.store.reader.onMetaChange(readerMeta);
	},
	onMetaChange : function(){
		this.storeMetaChange();
		this.searchGridView.titleTile.createRecordTileEl();
		var rs = [];
		if(this.store.getCount()>0){
			rs = this.store.reader.readRecords(this.store.reader.jsonData);
		}
		this.store.removeAll();
		if(rs.success && rs.totalRecords>0){
			this.store.loadRecords( rs, this.store.lastOptions, true);
		}
	},
	/*******************public API***************/
	nextPageHandler : function(){
		this.searchGridView.nextPageHandler();
	},
	prePageHandler : function(){
		this.searchGridView.prePageHandler();
	},
	firstPageHandler : function(){
		this.searchGridView.firstPageHandler();
	},
	lastPageHandler : function(){
		this.searchGridView.lastPageHandler();
	},
	gridViewHandler : function(){
		this.gridMoveIn();
	},
	getTotleViewIndex : function(view){
		var index = -1;
		if(this.viewPanel[this.CREATE_VIEW]){
			if(view === this.viewPanel[this.CREATE_VIEW]){
				return index + 1;
			}else{
				index++;
			}
		}
		if(this.viewPanel[this.EDIT_VIEW]){
			if(view === this.viewPanel[this.EDIT_VIEW]){
				return index + 1;
			}else{
				index++;
			}
		}
		if(this.viewPanel[this.DETAIL_VIEW]){
			if(view === this.viewPanel[this.DETAIL_VIEW]){
				return index + 1;
			}else{
				index++;
			}
		}
		var cvi = this.getCustomerViewIndex(view);
		return index+cvi+1;
	},
	getCustomerViewIndex : function(view){
		return this.customerViewPanels.indexOf(view);
	},
	getViewByTotleIndex : function(index){
		if(!Ext.isNumber(index)){
			return false;
		}
		var ci = parseInt(index);
		if(this.viewPanel[this.CREATE_VIEW]){
			if(ci == 0){
				return this.viewPanel[this.CREATE_VIEW];
			}
			ci--;
		}
		if(this.viewPanel[this.EDIT_VIEW]){
			if(ci == 0){
				return this.viewPanel[this.EDIT_VIEW];
			}
			ci--;
		}
		if(this.viewPanel[this.DETAIL_VIEW]){
			if(ci == 0){
				return this.viewPanel[this.DETAIL_VIEW];
			}
			ci--;
		}
		return this.customerViewPanels[ci] ? this.customerViewPanels[ci] : false;
	},
	firstView : function(){
		if(this.viewPanel[this.CREATE_VIEW])
			return this.viewPanel[this.CREATE_VIEW];
		if(this.viewPanel[this.EDIT_VIEW])
			return this.viewPanel[this.EDIT_VIEW];
		if(this.viewPanel[this.DETAIL_VIEW])
			return this.viewPanel[this.DETAIL_VIEW];
		if(this.customerViewPanels.length > 0)
			return this.customerViewPanels[0];
		return false;
	},
	showNextView : function(){
		var nextView = this.getViewByTotleIndex(this.getTotleViewIndex(this.currentView)+1);
		if(nextView){
			this.showView(nextView);
		}else{
			this.showView(this.firstView());
		}
	},
	createTbarMenu : function(){
		var tbarMenus = [];
		if(this.needTbar){
			this.topToolbar.items.each(function(button){
				if(button.text && button.handler && !button.hidden){
					var menuCfg = {};
					menuCfg.text = button.text;
					menuCfg.tbutton = button;
					menuCfg.handler = button.handler;
					tbarMenus.push(menuCfg);
				}
			});
		}
		return tbarMenus;
	},
	onContextMenu : function(eve, added){
		this._APP.onContextMenu(eve, added);
	}
});
Ext.reg('resultcontainer', Wlj.frame.functions.app.widgets.ResultContainer);

Wlj.frame.functions.app.widgets.SearchGrid = Ext.extend(Ext.Panel, {
	
	pageSize : 10,
	
	easingStrtegy : false,
	
	store : false,
	currentParams : {},
	autoScroll : false,
	rnWidth : 40,
	needRN : false,
	pagingDisplayMsg : '显示{0}条到{1}条，共{2}条',
	emptyText : '没有符合条件的记录',
	
	beresized : function(p,aw,ah,rw,rh){
		var h = parseInt(ah, 10);
		var titleH = this.titleTile.titleTile.el.getViewSize().height;
		var bbarH = this.bbar.getViewSize().height;
		var bh = h - titleH - 2 - bbarH;
		var w = parseInt(aw, 10);
		if(Ext.isNumber(bh)){
			this.scrollElement.applyStyles({
				height : bh + 'px'
			});
		}
		if(Ext.isNumber(w)){
			this.hdElement.applyStyles({
				width : w + 'px'
			});
		}
	},
	onColumnResize : function(index, res, width, height, e, name, totleWidth){
		this.store.fields.get(name).resutlWidth = width;
		this.searchDomain.dataFields[index-1].resutlWidth = width;
		this.getLayoutTarget().applyStyles({
			width : totleWidth + 'px'
		});
		var rows = this.getRows();
		var len = rows.length;
		for(var i=0;i<len;i++){
			rows[i].style.width = totleWidth+'px';
			rows[i].childNodes[index].style.width = width+'px';
		}
	},
	initComponent : function(){
		var _this = this;
		this.pageSizeCombo = new Ext.form.ComboBox({
	        triggerAction : 'all',
	        mode : 'local',
	        store : new Ext.data.ArrayStore({
	            fields : ['value', 'text'],
	            data : [ [ 10, '10条/页' ], 
	                     [ 20, '20条/页' ], 
	                     [ 50, '50条/页' ],
	                     [ 100, '100条/页' ], 
	                     [ 250, '250条/页' ],
	                     [ 500, '500条/页' ],
	                     [ 1000, '1000条/页'] ]
	        }),
	        width:80,
	        valueField : 'value',
	        displayField : 'text',
	        value : _this.pageSize,
	        editable : false,
	        listeners : {
				select : function(combo, record, index){
					var ps = parseInt(combo.getValue());
					_this.currBar.pageSize = ps;
					_this.searchDomain.pageSize = ps;
					_this.pageSize = ps;
					_this._APP.searchDomain.searchHandler();
				}
			}
	    });
			
		this.bbar = new Ext.PagingToolbar({
	        pageSize : _this.pageSize,
	        store : _this.store,
	        displayInfo : true,
	        displayMsg : _this.pagingDisplayMsg,       
	        emptyMsg : _this.emptyText,
	        items : ['-', '&nbsp;&nbsp;',this.pageSizeCombo],
	        listeners:{
	        	'beforechange':function(a,b){
	        		pars = _this._APP.searchDomain.searchPanel.getForm().getFieldValues();
	        		for(var key in pars){
	        			if(!pars[key]){
	        				delete pars[key];
	        			}
	        		}
	        		_this._APP.setSearchParams(pars,false,true);
	        	}
	        }
	    });
		this.currBar = this.bbar;
		this.bbarCfg = {
			cls : 'yc-grid-footer'
		};
		Wlj.frame.functions.app.widgets.SearchGrid.superclass.initComponent.call(this);
		if(!this.store){
			return;
		}
		this.initDataEvent();
	},
	
	destroy : function(){
		this.pageSizeCombo.destroy();
		this.pageSizeCombo = false;
		this.currBar.destroy();
		this.currBar = false;
		this.store = false;
		this.currentParams = {};
		Ext.destroy(this.hdElement);
		Ext.destroy(this.scrollElement);
		Ext.destroy(this.dtElement);
		delete this.hdElement;
		delete this.scrollElement;
		delete this.dtElement;
		Wlj.frame.functions.app.widgets.SearchGrid.superclass.destroy.call(this);
	},
	
	onRender : function(ct, position){
		Wlj.frame.functions.app.widgets.SearchGrid.superclass.onRender.call(this, ct, position);
		this.initElements();
		this.on('resize',this.beresized);
	},
	createTbarMenu : function(added){
		var tbars = this.searchDomain.createTbarMenu();
		if(tbars.length > 0){
			added.push.apply(added, tbars);
			added.push('-');
		}
	},
	onMetaAdd : function(field){
		this.titleTile.onMetaAdd(field);
	},
	onMetaAddByIndex : function(addField, theIndex){
		this.titleTile.onMetaAddByIndex(addField, theIndex);
	},
	onMetaRemove : function(field){
		this.titleTile.onMetaRemove(field);
		this.dtElement.dom.style.width = this.titleTile.recordWidth+'px';
	},
	onContextMenu : function(eve, html, obj, added){
		var row = Ext.fly(html).parent('.ygc-row');
		
		if(row){
			if(!row.hasClass('ygc-row-selected')){
				this.onRowClick(eve, html, obj);
			}
			this.createTbarMenu(added);
		}
		
		var gmenus = WLJUTIL.contextMenus.grid;
		for(var key in gmenus){
			var amenu = {};
			amenu.text = gmenus[key].text;
			amenu.handler = gmenus[key].fn.createDelegate(this);
			added.push(amenu);
		}
		added.push('-');
		this.searchDomain.onContextMenu(eve,added);
	},
	initEvents : function(){
		this.addEvents({
			recorddelete : true,
			recordselect : true,
			rowdblclick : true
		});
		var _this = this;
		this.getLayoutTarget().on('click',function(eve, html, obj){
			_this.onRowClick(eve, html, obj);
		});
		this.getLayoutTarget().on('dblclick', function(eve, html, obj){
			eve.stopEvent();
			_this.onRowDblclick(eve, html, obj);
		});
		this.getLayoutTarget().on('mousedown', function(eve, html, obj){
			eve.stopEvent();
			if(Ext.fly(html).hasClass('ygc-cell-no') || Ext.fly(html).hasClass('ygc-row')){
				return false;
			}
			_this.createDragGhost(eve, html, obj);
		});
		this.el.on('contextmenu',function(eve, html, obj){
			eve.stopEvent();
			_this.onContextMenu(eve, html, obj, []);
		});
	},
	onRowClick : function(eve, html, obj){
		var _this = this;
		var row = Ext.fly(html).hasClass('ygc-row')?Ext.fly(html):Ext.fly(html).parent('.ygc-row');
		if(!row){
			return false;
		}
		var rowIndex = parseInt(row.dom.getAttribute('rowIndex'));
		if(Ext.fly(html).hasClass('ygc-cell-no')){
			if(!row.hasClass('ygc-row-selected')){
				row.addClass('ygc-row-selected');
				_this.fireEvent('recordselect',this.store.getAt(rowIndex), this.store, html);
			}else{
				row.removeClass('ygc-row-selected');
			}
			return;
		}else{
			if(!row.hasClass('ygc-row-selected')){
				_this.clearSelect();
				row.addClass('ygc-row-selected');
				_this.fireEvent('recordselect',this.store.getAt(rowIndex), this.store, html);
			}else{
				_this.clearSelect();
				row.removeClass('ygc-row-selected');
			}
		}
	},
	onRowDblclick : function(eve, html, obj){
		var _this = this;
		var row = Ext.fly(html).parent('.ygc-row');
		var rowIndex = parseInt(row.dom.rowIndex);
		if(!row.hasClass('ygc-row-selected')){
			this.onRowClick(eve, html, obj);
		}
		_this.fireEvent('rowdblclick',html, this.store.getAt(rowIndex));
	},
	createDragGhost : function(eve, html, obj){
		var data = this.getCellData(html);
		if(this.store.fields.get(data.name).enableCondition === false){
			return false;
		}
		var ds = new Wlj.frame.functions.app.widgets.CellDD(Ext.fly(html), {
			dragData : {tile:{data:data}},
			ddGroup : 'searchDomainDrop'
		});
		ds.handleMouseDown(eve, ds);
	},
	initElements : function(){
		var Element = Ext.Element;
		var body = this.body;
		this.hdElement = body.createChild({
			tag : 'div',
			cls : 'yc-grid-header',
			style : 'width:100%;overflow:hidden;'
		});
		this.scrollElement = body.createChild({
			tag : 'div',
			style : 'overflow:auto;'
		});
		this.dtElement = this.scrollElement.createChild({
			tag : 'div',
			style : 'min-height:1px;'
		});
		this.createFieldsTitle();
	},
	getCellData : function(html){
		var row = Ext.fly(html).parent('.ygc-row');
		var rowIndex = parseInt(row.dom.getAttribute('rowIndex'));
		if(!Ext.fly(html).hasClass("ygc-cell")){
			html = Ext.fly(html).parent(".ygc-cell").dom;
		}
		var colIndex = Array.prototype.indexOf.call(row.dom.childNodes, html);
		var cellDT = this.titleTile.titleTile.items.itemAt(colIndex).data;
		cellDT.value = this.store.getAt(rowIndex).get(cellDT.name);
		return cellDT;
	},
	getLayoutTarget : function(){
		return this.dtElement;
	},
	createFieldsTitle : function(){
		var bwrap = this.bwrap;
		var body = this.body;
		var _this = this;
		this.titleTile = new Wlj.frame.functions.app.widgets.TitleTile({
			store : _this.store,
			vs : _this.vs,
			_APP : _this._APP,
			rnWidth : _this.rnWidth,
			searchGridView : _this,
			needRN : _this.needRN,
			easingStrtegy : _this.easingStrtegy
		});
		this.titleTile.titleTile.render(this.hdElement); 
		this.hdElement.applyStyles({
			height : this.titleTile.titleTile.el.getViewSize().height+'px'
		});
		this.titleTile.titleTile.el.applyStyles({
			top : 0,
			left : 0,
			marginTop : 0,
			marginLeft : 0
		});
		this.dtElement.applyStyles({
			width : this.titleTile.recordWidth + 'px'
		});
		this.scrollElement.on('scroll',function(){
			_this.synHDScroll();
		});
	},
	synHDScroll : function(){
		var innerHd = this.hdElement.dom ;
		var scrollLeft = this.scrollElement.dom.scrollLeft;
		innerHd.scrollLeft = scrollLeft;
		innerHd.scrollLeft = scrollLeft; // second time for IE (1/2 time first fails, other browsers ignore)
		
//		if(innerHd.scrollLeft<scrollLeft){
//			innerHd.style.marginLeft = (innerHd.scrollLeft-scrollLeft) + 'px'
//		}else{
//			if(parseInt(innerHd.style.marginLeft)!=0){
//				innerHd.style.marginLeft = 0;
//			}
//		}
	},
	booterDataElements : function(store, records){
		var _this = store.resultContainer;
		_this.clearRows();
		_this.titleTile.bootEls();
	},
	hasRows : function(){
		var ltEl = this.getLayoutTarget();
		if(ltEl.dom.firstChild){
			return true;
		}else return false;
	},
	clearRows : function(){
		var _this = this;
		_this.getLayoutTarget().dom.innerHTML = '';
	},
	getRows : function(){
		var _this = this;
		if(!_this.hasRows()){
			return [];
		}else{
			return this.getLayoutTarget().dom.childNodes;
		}
	},
	initDataEvent : function(){
		if(!this.store){
			this.initStore();
		}
		if(this.store){
			this.store.on('add', this.onDataAdd);
			this.store.on('load', this.onDataLoad);
			this.store.on('exception', this.onExceptionLoad);
			this.store.on('beforeload', this.onBeforeLoad);
			this.store.on('remove', this.onDataRemove);
			this.store.on('clear', this.onDataClear);
			this.store.resultContainer = this;
		}
	},
	onDataAdd : function(store, records, index){
		var _this = store.resultContainer;
		_this.booterDataElements(store, records);
	},
	onDataLoad : function(store, records, option){
		var _this = store.resultContainer;
		_this.totalLength = store.totalLength;
		_this.booterDataElements(store, records);
		_this._APP.unmaskRegion('resultDomain');
		_this._APP.enableConditionButton(WLJUTIL.BUTTON_TYPE.SEARCH);
	},
	onExceptionLoad : function(store, records, option){
		var _this = this.resultContainer;
		_this._APP.unmaskRegion('resultDomain');
		_this._APP.enableConditionButton(WLJUTIL.BUTTON_TYPE.SEARCH);
	},
	onBeforeLoad : function(store, option){
		var _this = store.resultContainer;
		delete option.add;
		store.removeAll();
		if(_this.getLayoutTarget())
			_this.getLayoutTarget().innerHTML = '';
		_this._APP.maskRegion('resultDomain',_this.searchDomain.loadMaskMsg);
		_this._APP.disableConditionButton(WLJUTIL.BUTTON_TYPE.SEARCH);
	},
	onDataRemove : function(store, record, index){
		store.resultContainer.getLayoutTarget().dom.removeChild(store.resultContainer.getLayoutTarget().dom.childNodes[index]);
	},
	onDataClear : function(store){
		if(store.resultContainer.getLayoutTarget())
			store.resultContainer.getLayoutTarget().dom.innerHTML = '';
	},
	turnToCurrentPage : function(){
		var _this = this;
		if(!this.store || !this.store.load){
			return false;
		}
		var pars = this.currentParams;
		this.store.baseParams = {"condition":Ext.encode(pars)};
		this.store.load({
			params : {
				start : 0 ,
				limit : _this.pageSize
			},callback : function(){
				if(!_this.searchDomain.gridLockedHole){
					if(!_this.searchDomain.gridLockedOnce){
						_this.searchDomain.gridMoveIn();
					}else{
						_this.searchDomain.gridLockedOnce = false;
					}
				}
				if(_this.searchDomain.suspendViews){
					if(!_this.searchDomain.alwaysLockCurrentView){
						_this.searchDomain.hideCurrentView();
					}
				}
			}
		});
	},
	getSelected : function(){
		var _this = this;
		_this.selected = [];
		this.el.select('.ygc-row-selected').each(function(se){
			_this.selected.push(_this.store.getAt(parseInt(se.getAttribute('rowIndex'))));
		});
		return _this.selected;
	},
	selectByIndex : function(index){
		var _this = this;
		if(Ext.isNumber(index) && this.items.itemAt(index)){
			this.clearSelect();
			this.items.itemAt(index).el.addClass('ygc-row-selected');
		}else if(Ext.isArray(index)){
			if(index.length>0){
				this.clearSelect();
				Ext.each(index, function(i){
					if(_this.items.itemAt(parseInt(i)))
						_this.items.itemAt(parseInt(i)).el.addClass('ygc-row-selected');
				});
			}
		}
	},
	clearSelect : function(){
		this.el.select('.ygc-row-selected').removeClass('ygc-row-selected');
	},
	antiSelect : function(){
		var rws = this.getLayoutTarget().select('.ygc-row', true);
		rws.each(function(el){
			if(!el.hasClass('ygc-row-selected')){
				el.addClass('ygc-row-selected');
			}else{
				el.removeClass('ygc-row-selected');
			}
		});
	},
	allSelect : function(){
		this.getLayoutTarget().select('.ygc-row').addClass('ygc-row-selected');
	},
	sort : function(dataIndex, info){
		this.titleTile.updateSortIcon(dataIndex, info);
		this.store.sort(dataIndex, info);
		this.booterDataElements(this.store);
	},
	showFields : function(fields){
		var indexes = this.getFieldsIndex(fields);
		for(var i=0;i<indexes.length;i++){
			this.store.fields.get(indexes[i]).hidden = false;
			this.searchDomain.dataFields[indexes[i]].hidden = false;
		}
		this.titleTile.showFields(indexes);
		var rows = this.getRows();
		for(var i=0;i<rows.length;i++){
			for(var c=0; c<indexes.length; c++){
				if(indexes[c]>=0 && indexes[c] < rows[i].childNodes.length){
					rows[i].childNodes[indexes[c]+1].style.display='block';
				}
			}
			rows[i].style.width = this.titleTile.recordWidth+'px';
		}
		this.dtElement.dom.style.width = this.titleTile.recordWidth+'px';
	},
	hideFields : function(fields){
		var indexes = this.getFieldsIndex(fields);
		for(var i=0;i<indexes.length;i++){
			this.store.fields.get(indexes[i]).hidden = true;
			this.searchDomain.dataFields[indexes[i]].hidden = true;
		}
		this.titleTile.hideFields(indexes);
		var rows = this.getRows();
		for(var i=0;i<rows.length;i++){
			for(var c=0; c<indexes.length; c++){
				if(indexes[c]>=0 && indexes[c] < rows[i].childNodes.length){
					rows[i].childNodes[indexes[c]+1].style.display='none';
				}
			}
			rows[i].style.width = this.titleTile.recordWidth+'px';
		}
		this.dtElement.dom.style.width = this.titleTile.recordWidth+'px';
	},
	getFieldsIndex : function(fields){
		var fnames = [];
		var findexes = [];
		if(Ext.isArray(fields)){
			fnames = fields;
		}else{
			fnames = [fields];
		}
		for(var i=0; i<fnames.length; i++){
			var index = this.store.fields.indexOf(this.store.fields.get(fnames[i]));
			if(index >= 0){
				findexes.push(index);
			}
		}
		return findexes;
	},
	nextPageHandler : function(){
		if(!this.currBar.next.disabled){
			this.currBar.moveNext();
		}
	},
	prePageHandler : function(){
		if(!this.currBar.prev.disabled){
			this.currBar.movePrevious();
		}
	},
	firstPageHandler : function(){
		if(!this.currBar.first.disabled)
			this.currBar.moveFirst();
	},
	lastPageHandler : function(){
		if(!this.currBar.last.disabled)
			this.currBar.moveLast(); 
	},
	refreshPageHandler : function(){
	}
});

Wlj.frame.functions.app.widgets.TitleTile = function(cfg){
	Ext.apply(this,cfg);
	Wlj.frame.functions.app.widgets.TitleTile.superclass.constructor.call(this);
	this.createTitle();
	this.createRecordTileEl();
};
Ext.extend(Wlj.frame.functions.app.widgets.TitleTile, Ext.util.Observable, {
	
	alwaysField : true,
	float : 'left',
	
	lineHeight : 27,
	defaultFieldWidth : 150,
	rnWidth : 40,
	needRN : false,
	easingStrtegy : false,
	indexTitleText : '序号',
	
	
	getTitleClass : function(field){
		var dataType = field.dataType;
		if(dataType && WLJDATATYPE[dataType]){
			dataType = WLJDATATYPE[dataType];
			return dataType.getTitleClass();
		}
		return '';
	},
	createTitle : function(){
		var _this = this;
		_this.recordWidth = 0;
		var fields = this.store.fields;
		var fieldsTiels = [];
		if(this.needRN){
			this.indexTile = new Wlj.widgets.search.tile.Tile({
				ownerW : 10,
				removeable : false,
				dragable : false,
				baseSize : _this.lineHeight,
				baseWidth : _this.rnWidth,
				baseMargin : 0,
				cls : 'ygh-hd',
				float : 'left',
				html : _this.indexTitleText
			});
			this.indexTile.on('afterrender', function(itile){
				itile.el.on('click',function(){
					if(!itile.__ALLS){
						_this.store.resultContainer.allSelect();
						itile.__ALLS=true;
					}else{
						_this.store.resultContainer.clearSelect();
						itile.__ALLS=false;
					}
				});
			});
			fieldsTiels.push(this.indexTile);
			_this.recordWidth = parseInt(_this.recordWidth) + parseInt(this.indexTile.baseMargin)*2 + parseInt(_this.rnWidth) + 12;
		}
		
		fields.each(function(field){
			var tf = _this.createFieldTile(field);
			if(tf){
				fieldsTiels.push(tf);
			}
		});
		var tile = new Wlj.widgets.search.tile.Tile({
			ownerW : 10,
			ownerWI : -10,
			removeable : false,
			dragable : false,
			baseSize : _this.lineHeight,
			baseWidth : _this.recordWidth,
			baseMargin : 1,
			recordView : this,
			cls : 'ygh-container',
			float : 'left',
			style : {
				//border : '1px solid #000',
				overflowX :'hidden'
			},
			pos_size : {
				TX : 0,
				TY : 0,
				TW : 1,
				TH : 1
			},
			items : fieldsTiels,
			listeners : {
				afterrender : function(tileThis){
					tileThis.el.on('contextmenu', function(eve, html, obj){
						eve.stopEvent();
						tileThis.recordView.onTitleContextMenu(eve, html, obj, []);
					});
					_this.initColumnDD();
					_this.resetWidth();
				}
			}
		});
		_this.titleTile = tile;
	},
	createDataIndexEl : function(){
		var _this = this;
		var indexHTML = '<div class="ygc-cell ygc-cell-no" style="width:'+_this.rnWidth+'px;position: relative; margin: 0px; float: left; height: 27px;">'+
			'{index+1}' + 
			'</div>';
		return indexHTML;
	},
	createFieldEl : function(tf){
		var _this=  this;
		var fieldHTML = '';
		if(tf.text && tf.gridField !== false){
				fieldHTML =
					'<tpl for="'+tf.name+'">'+
					'<div title="{title}" class="ygc-cell '+_this.getFieldClass(tf)+'" style="position: relative; margin: 0px; width: '+
					(tf.resutlWidth?tf.resutlWidth:_this.defaultFieldWidth)+'px; float: left; height: 27px; '+(tf.hidden?'display:none;':'')+'">'+
					'{display}'+
					'</div>'+
					'</tpl>';
				return fieldHTML;
		}
		return '';
	},
	createRecordTileEl : function(){
		var _this = this;
		var store = this.store;
		var fields = store.fields;
		var ElBuffer = [];
		var createString = '<div class="ygc-row {oddc}" style="position: relative; overflow-x: hidden; margin: 0px; width: '+this.recordWidth+'px; float: left; height: 27px;" rowIndex="{index}">';
		ElBuffer.push(createString);
		ElBuffer.push(this.createDataIndexEl());
		ElBuffer.push('<tpl for="data">');
		fields.each(function(tf){
			ElBuffer.push(_this.createFieldEl(tf));
		});
		ElBuffer.push('</tpl>');
		ElBuffer.push('</div>');
		_this.recordTemplate = new Ext.XTemplate(ElBuffer.join(''),{
			formatFieldData : function(field, data){
				var dataFormat = '&nbsp;';
				if(data){
					dataFormat = data;
				}
				if(Ext.isFunction(field.viewFn)){
					dataFormat = field.viewFn(dataFormat);
				}
				return dataFormat;
			}
		});
	},
	bootEls : function(){
		var _this = this;
		var dc = this.store.getCount();
		
		if(!_this.easingStrtegy || 
		   !_this.easingStrtegy.type || 
		   !_this.easingStrtegy.firstStep || 
		   !Ext.isFunction(_this[_this.easingStrtegy.type+'DataLineRender']) ||
		   dc <= _this.easingStrtegy.firstStep){
			_this.defaultDataLineRender();
		}else{
			_this[_this.easingStrtegy.type+'DataLineRender'].call(_this, _this.easingStrtegy.initialConfig);
		}
	},
	defaultDataLineRender : function(){
		var _this = this;
		var grid = this.searchGridView;
		var layoutEl = grid.getLayoutTarget();
		var store = this.store;
		store.data.each(function(item, index, length){
			var oddc = index % 2 ===0 ? "ygc-row-odd" : "";
			var data = _this.buildData(item);
			_this.recordTemplate.append(layoutEl, {
				oddc : oddc,
				index : index,
				data : data
			});
		});
	},
	settimeoutDataLineRender : function(initialConfig){
		var _this = this;
		var grid = this.searchGridView;
		var layoutEl = grid.getLayoutTarget();
		var store = this.store;
		var firstStep = this.easingStrtegy.firstStep ?  this.easingStrtegy.firstStep : 50;
		store.data.each(function(item, index, length){
			var oddc = index % 2 ===0 ? "ygc-row-odd" : "";
			var data = _this.buildData(item);
			if(index < firstStep){
				_this.recordTemplate.append(layoutEl, {
					oddc : oddc,
					index : index,
					data : data
				});
			}else{
				setTimeout(function(){
					_this.recordTemplate.append(layoutEl, {
						oddc : oddc,
						index : index,
						data : data
					});
				},1);
			}
		});
	},
	
	buildData : function(record){
		var _this = this;
		var dataObj = {};
		record.fields.each(function(tf){
			var fData = _this.formatFieldData(tf,_this.translateFieldData(tf, record.get(tf.name)));
			dataObj[tf.name] = {
					display : fData,
					title : tf.noTitle===true?tf.text:record.get(tf.name)
			};
		});
		return dataObj;
	},
	getFieldClass : function(field){
		var dataType = field.dataType;
		if(dataType && WLJDATATYPE[dataType]){
			dataType = WLJDATATYPE[dataType];
			return dataType.getFieldClass();
		}
		return '';
	},
	
	formatFieldData : function(field, data){
		var dataFormat = '&nbsp;';
		if(data){
			dataFormat = data;
		}
		if(Ext.isFunction(field.viewFn)){
			dataFormat = field.viewFn(dataFormat);
		}
		return dataFormat;
	},
	translateFieldData : function(field, data){
		var app = this._APP;
		var reData = '&nbsp';
		if(field.translateType){
			if (field.multiSelect) {
				var separator = field.multiSeparator?field.multiSeparator:this.multiSelectSeparator;
				var de = app.translateLookupByMultiKey(field.translateType, data, separator);
			} else {
				var de = app.translateLookupByKey(field.translateType, data);
			}
			if(de){
				reData = de;
			}
		}else{
			reData = data;
		}
		
		var dataType = field.dataType;
		if(dataType && WLJDATATYPE[dataType]){
			dataType = WLJDATATYPE[dataType];
			if(reData !== '&nbsp'){
				reData = dataType.formatFn(reData);
				dataType = null;
			}
		}
		return reData;
	},
	initColumnDD : function(){
		var _this = this;
		this.DropZone = new Ext.dd.DropZone(_this.titleTile.el.dom, {
			ddGroup : 'searchDomainDrop',
			notifyDrop  : function(ddSource, e, data){
				var px = e.getXY()[0];
				var py = e.getXY()[1];
				var newIndex = 0;
				var tf = _this.titleTile.el.first().first('.ygh-hd');
				while(tf){
					if(px <= tf.getRight()){
						break;
					}else{
						tf = tf.next();
						newIndex ++;
					}
				}
				setTimeout(function(){
					_this.setColumnOrder(data.tile.data.name,newIndex);
				},50);
			}
		});	
	},
	createFieldTile : function(tf){
		var _this = this;
		if( tf.text && tf.gridField !== false){
			var tfHTML = '<div title="'+tf.text+'" class="ygh-hd-text">'+tf.text+'</div>';
			var fTile = new Wlj.widgets.search.tile.Tile({
				ownerW : 10,
				removeable : false,
				defaultDDGroup : 'searchDomainDrop',
				dragable : tf.enableCondition !== false,
				baseSize : _this.lineHeight,
				baseWidth : tf.resutlWidth ? tf.resutlWidth : _this.defaultFieldWidth,
				float : 'left',
				cls : 'ygh-hd '+_this.getTitleClass(tf),
				baseMargin : 0,
				html : tfHTML,
				data : {
					name : tf.name,
					value : ''
				},
				clearSortIcon : function(){
					this.el.first().removeClass('ygh-hd-order-desc');
					this.el.first().removeClass('ygh-hd-order-asc');
				},
				addSortIcon : function(info){
					this.el.first().addClass('ygh-hd-order-'+info);
				},
				listeners : {
					afterrender : function( tileThis ){
						tileThis.el.on('click',function(eve){
							eve.stopEvent();
							if(!tileThis.el.first().hasClass('ygh-hd-order-desc')){
								_this._APP.sortByDataIndex(tileThis.data.name,'desc');
							}else{
								_this._APP.sortByDataIndex(tileThis.data.name,'asc');
							}
						});
						tileThis.el.on('contextmenu',function(eve, html, obj){
							eve.stopEvent();
							_this.onTitleFieldContextMenu(eve, html, obj, []);
						});
						this.RESIZEABLE = new Ext.Resizable(this.el, {
							handles : 'e',
							height : this.el.getHeight(),
							width : this.el.getWidth()
						});
						this.RESIZEABLE.on('resize',function(res, width, height, e){
							e.stopEvent();
							var index = _this.titleTile.items.indexOf(tileThis);
							_this.onColumnResize(index, res, width, height, e, tileThis.data.name);
						});
						if(this.dragable){
							this.dd.startDrag = function(){
								this.tile.el.applyStyles({
									display:''
								});
								this.proxy.getEl().dom.innerText = this.tile.el.dom.innerText;
							};
						}
						if(tf.hidden){
							this.hide();
						}
					}
				}
			});
			if(!tf.hidden){
				_this.recordWidth = parseInt(_this.recordWidth) + parseInt(fTile.baseMargin)*2 + parseInt(fTile.baseWidth) + 12;
			}
			return fTile;
		}
		return false;
	},
	onColumnResize : function(index, res, width, height, e, name){
		var recordWidth = parseInt(this.recordWidth) - parseInt(this.titleTile.items.get(index).baseWidth) + parseInt(width);
		this.titleTile.items.get(index).baseWidth = width;
		this.titleTile.items.get(index).initialConfig.baseWidth = width;
		this.recordWidth = recordWidth;
		this.titleTile.baseWidth = recordWidth;
		this.titleTile.get(index).el.applyStyles({
			width : width + 'px'
		});
		this.resetWidth();
		this.searchGridView.onColumnResize(index, res, width, height, e, name, recordWidth);
		this.createRecordTileEl();
	},
	setColumnOrder : function(name, index){
		var tile = null;
		this.titleTile.items.each(function(it){
			if(it.data && it.data.name === name){
				tile = it;
			}
		});
		var oldIndex = this.titleTile.items.indexOf(tile);
		var ic = tile.initialConfig;
		this.titleTile.remove(tile);
		this.titleTile.insert(index,new Wlj.widgets.search.tile.Tile(ic));
		this.titleTile.doLayout();
		var rows = this.searchGridView.getRows();
		for(var i=0;i<rows.length;i++){
			var tNode = rows[i].removeChild(rows[i].childNodes[oldIndex]);
			rows[i].childNodes[index-1].insertAdjacentElement('afterEnd', tNode);
		}
		var df = this.searchGridView.searchDomain.dataFields;
		var mf = false;
		for(var i=0;i<df.length;i++){
			if(df[i].name === name){
				mf = df[i];
				break;
			}
		}
		if(mf){
			df.remove(mf);
			df.splice(index-1,0,mf);
			this.searchGridView.searchDomain.storeMetaChange();
		}
		this.createRecordTileEl();
	},
	onMetaAdd : function(field){
		var addedTile = this.createFieldTile(field);
		var addedTile = this.titleTile.add(addedTile);
		this.resetWidth();
		this.titleTile.doLayout();
		this.createRecordTileEl();
		var addedTemp = new Ext.XTemplate(
				'<div class="ygc-cell" style="position: relative; margin: 0px; width: '+
				(field.resutlWidth ? field.resutlWidth : this.defaultFieldWidth)+'px; float: left; height: 27px;">'+
				'</div>');
		var rows = this.searchGridView.getRows();
		var len = rows.length;
		for(var i=0;i<len;i++){
			addedTemp.append(rows[i]);
			rows[i].style.width = this.recordWidth+'px';
		}
		
	},
	onMetaAddByIndex : function(addField, theIndex){
		var addedTile = this.createFieldTile(addField);
		var addedTile = this.titleTile.insert(theIndex+1, addedTile);
		this.resetWidth();
		this.titleTile.doLayout();
		this.createRecordTileEl();
	},
	onMetaRemove : function(field){
		var theTiles = this.titleTile.findBy(function(i){if(i.data && field===i.data.name)return true;return false;});
		if(theTiles.length>0){
			var theTile = theTiles[0];
			if(!theTile.hidden){
				this.recordWidth = parseInt(this.recordWidth) - (parseInt(theTile.baseMargin) * 2 + parseInt(theTile.baseWidth) + 12);
			}
			this.titleTile.remove(theTile);
			this.resetWidth();
			this.titleTile.doLayout();
			this.createRecordTileEl();
			var row = this.searchGridView.getRows();
			var len = row.length;
			for(var i=0;i<len;i++){
				row[i].style.width = this.recordWidth+'px';
			}
		}
	},
	resetWidth : function(){
		this.titleTile.el.applyStyles({
			width : (this.recordWidth+20)+'px'
		});
		Ext.fly(this.titleTile.getLayoutTarget()).applyStyles({
			width : (this.recordWidth+20)+'px'
		});
	},
	clearSortIcons : function(){
		this.titleTile.items.each(function(f){
			if(f.clearSortIcon){
				f.clearSortIcon.call(f);
			}
		});
	},
	updateSortIcon : function(dataIndex, info){
		this.titleTile.items.each(function(f){
			if(f.clearSortIcon){
				f.clearSortIcon.call(f);
			}
			if(f.data && f.data.name === dataIndex && f.addSortIcon){
				f.addSortIcon.call(f, info);
			}
		});
	},
	showFields : function(dataIndexes){
		var _this = this;
		Ext.each(dataIndexes, function(di){
			var f = _this.titleTile.items.get(di+1);
			if(f && f.hidden){
				_this.recordWidth = parseInt(_this.recordWidth) + (parseInt(f.baseMargin) * 2 + parseInt(f.baseWidth) + 12);
				f.show();
			}
		});
		this.titleTile.doLayout();
		_this.resetWidth();
		this.createRecordTileEl();
	},
	hideFields : function(dataIndexes){
		var _this = this;
		Ext.each(dataIndexes, function(di){
			var f = _this.titleTile.items.get(di+1);
			if(f && !f.hidden){
				_this.recordWidth = parseInt(_this.recordWidth) - (parseInt(f.baseMargin) * 2 + parseInt(f.baseWidth) + 12);
				f.hide();
			}
		});
		_this.resetWidth();
		this.createRecordTileEl();
	},
	onTitleContextMenu : function(eve, html, obj, added){
		this.searchGridView.onContextMenu(eve, html, obj, added);
	},
	onTitleFieldContextMenu : function(eve, html, obj, added){
		this.onTitleContextMenu(eve, html, obj, added);
	},
	destroy : function(){
		this.titleTile.removeThis();
	}
});
/**
 * 结果容器扩展面板基类;
 */
Wlj.frame.functions.app.widgets.View = Ext.extend(Ext.Panel,{
	autoEl : {
		tag : 'div',
		cls : 'wlj-view-container'
	},
	autoScroll : true,
	layout : 'fit',
	_defaultTitle : 'hello world',
	initComponent : function(){
		Wlj.frame.functions.app.widgets.View.superclass.initComponent.call(this);
		this.addEvents({
			movein : true,
			moveout : true,
			beforeloadrecord : true,
			loadrecord : true
		});
	},
	render : function(container, position){
		this.width = this.vs.width;
		Wlj.frame.functions.app.widgets.View.superclass.render.call(this,container, position);
	}
});
Ext.reg('basicview', Wlj.frame.functions.app.widgets.View);

/**
 * 查询结果系统扩展面板基类
 */
Wlj.frame.functions.app.widgets.RView = Ext.extend(Wlj.frame.functions.app.widgets.View,{
	_defaultTitle : 'Rview',
	ownerDomain : false,
	ownerApp : false,
	hideTitle : false,
	suspended : false,
	vs : false,
	tabIco : false,
	initComponent : function(){
		this.width = this.vs.width;
		if(this.title){
			this._defaultTitle = this.title;
		}else{
			this.title = this._defaultTitle;
		}
		this.iconCls='hide-wvc-right';
		this.headerCssClass='wvc-header';
		if(!this.ownerDomain.suspendViews){
			delete this.title;
		}
		Wlj.frame.functions.app.widgets.RView.superclass.initComponent.call(this);
		var _this = this;
		if(_this.ownerDomain.needTbar){
			_this.togogleButton = this.ownerDomain.getTopToolbar().addButton({
				text : _this._defaultTitle,
				enableToggle : true,
				hidden : _this.hideTitle,
				iconCls : _this.tabIco,
				handler : function(){
					if(_this.ownerDomain.currentView === _this){
						if(_this.ownerDomain.suspendViews){
							_this.ownerDomain.hideCurrentView();
						}else{
							_this.ownerDomain.gridMoveIn();
						}
					}else{
						var result = _this.ownerDomain.showView(_this);
						if(result === false){
							_this.togogleButton.toggle( false );
						}
					}
				}
			});
		}
	},
	onRender : function(ct, position){
		Wlj.frame.functions.app.widgets.RView.superclass.onRender.call(this, ct, position);
		var _this  = this;
		if(_this.suspended){
			_this.header.on('click', function(e,t,o){
				e.stopEvent();
				if(_this.ownerDomain.suspendViews){
					_this.ownerDomain.hideCurrentView();
				}else{
					_this.ownerDomain.gridMoveIn();
				}
			});
		}
	},
	getIndex : function(){
		return this.ownerDomain.getTotleViewIndex(this);
	},
	setValues : function(object){
		if(!this.contentPanel){
			return false;
		}
		var cp = this.contentPanel;
		if(!cp.getForm || !cp.getForm()){
			return false;
		}
		var form = cp.getForm();
		form.setValues(object);
	}
});
Ext.reg('resultview', Wlj.frame.functions.app.widgets.RView);

/**
 * 系统扩展面板类：用于新增、修改、详情
 */
Wlj.frame.functions.app.widgets.CView = Ext.extend(Wlj.frame.functions.app.widgets.RView,{
	_defaultTitle : 'Cview',
	baseType : false,
	openValidate : true,
	svButton : true,
	fields : false,
	validates : false,
	linkages : false,
	formButtons : false,
	//layout : 'form',
	autoValidateText : '字段校验失败，请检查输入项!',
	svButtonText : '保存',
	reminding : '提示',
	
	suspendFitAll : false,
	
	toBeValidate : true,
	
	record : false,
	initComponent : function(){
		Wlj.frame.functions.app.widgets.CView.superclass.initComponent.call(this);
		this.width = this.vs.width;
		this.buildContent();
		this.add(this.contentPanel);
		var bt = this.baseType.toLowerCase();
		this.ownerDomain.fireEvent('before'+bt+'render', this);
	},
	destory : function(){
		this.contentPanel = false;
		Wlj.frame.functions.app.widgets.CView.superclass.destory.call(this);
	},
	
	onRender : function(ct, position){
		Wlj.frame.functions.app.widgets.CView.superclass.onRender.call(this,ct,position);
		this.fireEvent('afterviewrender', this);
	},
	initEvents : function(){
		this.addEvents({
			beforevalidate :true,
			validate : true,
			afterviewrender : true
		});
	},
	setRecord : function(record){
		this.record = record;
		if(this.rendered === true){
			var loadrecordable = this.fireEvent('beforeloadrecord', this, this.record);
			if(loadrecordable === false){
				return false;
			}
			this.contentPanel.getForm().reset();
			if(this.record){
				this.contentPanel.getForm().loadRecord(this.record);
			}
			this.fireEvent('loadrecord', this, this.record);
		}
	},
	reset : function(){
		if(this.contentPanel && this.contentPanel.getForm()){
			this.contentPanel.getForm().reset();
		}
	},
	buildContent : function(){
		var _this = this;
		this.contentPanel = new Ext.form.FormPanel({
			items : this.buildFormField(),
			frame:true,
			height : 'auto',
			autoScroll : true,
			buttonAlign : 'center',
			layout : 'form',
			buttons : _this.buildButtons()
		});
	},
	buildButtons : function(){
		var _this = this;
		var buttons = [];
		if(_this.svButton){
			var svButtonCfg = {
				text : _this.svButtonText,
				cls:'simple-btn',
				overCls:'simple-btn-hover',
				handler : function(){
					if(_this.openValidate){
						var validateAble = _this.fireEvent('beforevalidate',_this,_this.contentPanel);
						if(validateAble === false){
							return false;
						}
						if(!_this.contentPanel.getForm().isValid()){
							Ext.MessageBox.alert(_this.reminding, _this.autoValidateText);
							return false;
						}
						var error = _this.validateData();
						_this.fireEvent('validate',_this,_this.contentPanel,error);
						if(!error.passable){
							Ext.Msg.alert(_this.reminding,error.info);
							return;
						}
					}
					var data = _this.contentPanel.getForm().getFieldValues();
					_this.ownerApp.comitData(data);
				}
			};
			buttons.push(svButtonCfg);
		}
		if(Ext.isArray(_this.formButtons)){
			Ext.each(_this.formButtons,function(fb){
				if(Ext.isString(fb.text)){
					if(Ext.isFunction(fb.fn)){
						fb.handler = function(){
							fb.fn.call(_this, _this.contentPanel, _this.contentPanel.getForm());
						};
					}else{
						fb.handler = Ext.emptyFn;
					}
					buttons.push(fb);
				}
			});
		}
		return buttons.length>0?buttons:false;
	},
	
	//private
	createColumnsCfg : function(columnCount, fields){
		var columnsCfg = [];
		var _this = this;
		for(var i=0; i< columnCount;i++){
			var cfg = {};
			cfg.layout = 'form';
			cfg.columnWidth = 1/columnCount;
			cfg.items = [];
			columnsCfg.push(cfg);
		}
		Ext.each(fields, function(f){
			if(Ext.isObject(f)){
				f.fieldLabel  = f.text ? f.text : f.name;
				f.xtype = f.xtype? f.xtype : 'textfield';
				f.hidden = typeof f.hidden === 'boolean' ? f.hidden : f.text ? false : true;
				f.anchor = '90%';
				if(f.allowBlank === false){
					f.fieldLabel = WLJTOOL.addBlankFlag(f.fieldLabel);
				}
				if(f.translateType){
					f.xtype = f.multiSelect?'lovcombo':'combo';
					f.store = _this.ownerApp.lookupManager[f.translateType];
					if(!f.store){
						Ext.error('字段【'+f.text+'】的数据字典映射项store【'+f.translateType+'】获取错误，请检查[lookupTypes|localLookup]项配置');
						return false;
					}
					f.valueField = 'key';
					f.displayField = 'value';
					f.editable = typeof f.editable === 'boolean' ? f.editable : false;
					f.forceSelection = true;
					f.triggerAction = 'all';
					f.mode = 'local';
					f.hiddenName = f.name;
					f.separator = f.multiSeparator?f.multiSeparator:_this.ownerDomain.multiSelectSeparator;
				}else{
					f.name = f.name;
					if(f.dataType && WLJDATATYPE[f.dataType]){
						f.xtype = WLJDATATYPE[f.dataType].getFieldXtype();
						Ext.applyIf(f, WLJDATATYPE[f.dataType].getFieldSpecialCfg());
					}
				}		
				var changeListeners = {
					change : function(field,newValue,oldValue){
						_this.linkaging(field,newValue,oldValue);
					}
				};
				if(!f.listeners){
					f.listeners = changeListeners;
				}else{
					Ext.apply(f.listeners,changeListeners);
				}
				var columnI = fields.indexOf(f) % columnCount;
				columnsCfg[columnI].items.push(f);
			}
		});
		return columnsCfg;
	},
	buildFormField : function(){
		var _this = this;
		var groups = _this.formViewer;
		var groupPanels = [];
		var vs = _this.vs;
		var columnCount = vs.width > 1024 ? 4:3;
		if(vs.width < 800 ){
			columnCount = 2;
		}
		if(vs.width < 500){
			columnCount = 1;
		}
		var allFields = _this.fields;
		Ext.each(groups, function(g){
			var gFs = g.fields;
			var gColumnCount = columnCount;
			if(g.columnCount){
				gColumnCount = g.columnCount;
			}
			var panelFieldsCfg = _this.getFieldsByName(gFs);
			
			if(!panelFieldsCfg){
				return;
			}
			
			var items;
			if(Ext.isFunction(g.fn)){
				try{
					items = g.fn.apply(_this, panelFieldsCfg);
				}catch(Werror){
					Ext.error('['+_this._defaultTitle+']面板第['+groups.indexOf(g)+']个字段组渲染出错,字段组将以默认顺序解析。见【formViewers|createFormViewer|editFormViewer|detailFormViewer】，TYPE:【'+Werror.name+'】;MESSAGE:【'+Werror.message+'】!');
					items = panelFieldsCfg;
				}
			}else{
				items = panelFieldsCfg;
			}
			items.length = panelFieldsCfg.length;
			var tItems = new Array();
			for(var ti=0; ti<items.length; ti++){
				if(Ext.isObject(items[ti]) && items[ti].hidden !== true){
					if( _this.baseType === "detailView" ){
						items[ti].readOnly = true;
					}
					tItems.push(items[ti]);
				}else continue;
			}
			items = _this.createColumnsCfg(gColumnCount,tItems);
			delete tItems;
			var lines = Math.ceil(gFs.length/gColumnCount);
			var panelCfg = {};
			panelCfg.xtype = 'panel';
			panelCfg.layout = 'column';
			if (g.labelWidth) {
				panelCfg.labelWidth = g.labelWidth;
			}
			panelCfg.items = items;
			groupPanels.push(panelCfg);
		});
		Ext.each(allFields,function(af){
			if(af.hidden || !af.text){
				af.xtype = 'textfield';
				af.hidden = true;
				groupPanels.push(af);
			}
		});
		return groupPanels;
	},
	getFieldsByName : function(name){
		if(!this.fields){
			return false;
		}
		if(!name){
			return false;
		}
		var _this = this;
		if(Ext.isArray(name)){
			var fields = new Array(name.length);
			Ext.each(_this.fields,function(f){
				if(name.indexOf(f.name)>=0){
					fields[name.indexOf(f.name)] = f;
				}
			});
			return fields.length>0?fields:false;
		}else if(Ext.isString(name)){
			for(var i=0;i< this.fields.length; i++){
				var field = this.fields[i];
				if(field.name === name){
					return field;
				}
			}
		}
		return false;
	},
	validateData : function(){
		var _this = this;
		var result = {
				passable : true,
				info : '',
				addInfo : function(info){
			result.info += '<br>' + info;
		}
		};
		if(!_this.toBeValidate){
			return result;
		}
		var vas = this.validates;
		if(!vas){
			return result;
		}
		var panel = _this.contentPanel;
		if(!panel.getForm()){
			return result;
		}
		var values = panel.getForm().getFieldValues();
		var _this = this;	
		
		Ext.each(vas,function(v){
			if(Ext.isFunction(v.fn)){
				var arrays = [];
				Ext.each(v.dataFields,function(vf){
					arrays.push(values[vf]);
				});
				try{
					if( v.fn.apply(v,arrays) === false){
						result.passable = false;
						result.addInfo(v.desc);
					}
				}catch(Werror){
					Ext.error('['+_this._defaultTitle+']面板校验规则['+v.desc+']:运行错误，校验结果将按照失败计算。见【validates|createValidates|editValidates】，TYPE:【'+Werror.name+'】;MESSAGE:【'+Werror.message+'】!');
					result.passable = false;
					result.addInfo(v.desc);
				}
			}else{
				Ext.warn('['+_this._defaultTitle+']面板校验规则['+v.desc+']函数配置有误，或者中途被改变，校验结果将按照成功计算');
			}
		});
		return result;
	},
	linkaging : function(field,newValue,oldValue){
		var _this = this;
		if(!_this.linkages){
			return;
		}
		var name = field.name;
		var _linkage = _this.linkages[name];
		if(!_linkage){
			return false;
		}
		var linkedFields = _this.getLinkFields(_linkage.fields);
		var allerror = '字段:';
		var haserror = false;
		Ext.each(linkedFields, function(lf){
			if(lf.noFieldError){
				haserror = true;
				allerror += '['+lf.name+']';
			}
		});
		if(haserror){
			Ext.error('['+_this._defaultTitle+']面板['+name+']字段联动错误。'+allerror+'不在定义的字段中。见【linkages|createLinkages|editLinkages】中fields属性');
		}
		linkedFields.unshift(field);
		try{
			_linkage.fn.apply(_this,linkedFields);
		}catch(Werror){
			Ext.error('['+_this._defaultTitle+']面板['+name+']字段联动逻辑运行错误。见【linkages|createLinkages|editLinkages】中fn属性，TYPE:【'+Werror.name+'】;MESSAGE:【'+Werror.message+'】!');
		}
	},
	getLinkFields : function(o){
		if(!Ext.isArray(o)){
			return false;
		}
		var linkFields = [];
		var _this = this;
		Ext.each(o, function(of){
			var ofo = _this.contentPanel.getForm().findField(of);
			if(ofo){
				linkFields.push(ofo);
			}else{
				linkFields.push({
					name : of,
					noFieldError : 'field:['+of+'] is not found!'
				});
			}
		});
		return linkFields;
	}
});
Ext.reg('cview', Wlj.frame.functions.app.widgets.CView);

/**
 * 结果容器扩展面板类，用户自定义扩展面板均采用此类面板做为容器；
 */
Wlj.frame.functions.app.widgets.BView = Ext.extend(Wlj.frame.functions.app.widgets.RView  ,{
	_defaultTitle : 'Bview',
	ownerDomain : false,
	ownerApp : false,
	vs : false,
	initComponent : function(){
		/***
		 * TODO fit all
		 */
		this.width = this.vs.width;
		Wlj.frame.functions.app.widgets.BView.superclass.initComponent.call(this);
	},
	getCustomerViewIndex : function(){
		return this.ownerDomain.getCustomerViewIndex(this);
	}
});
Ext.reg('buisnessview', Wlj.frame.functions.app.widgets.BView);

Wlj.frame.functions.app.widgets.FormView = Ext.extend(Wlj.frame.functions.app.widgets.BView ,{
	
	autoLoadSeleted : false,
	formButtons : false,
	
	initComponent : function(){
		delete this.buttons;
		Wlj.frame.functions.app.widgets.FormView.superclass.initComponent.call(this);
		this.buildContentObject();
		var _this = this;
		if(this.autoLoadSeleted){
			this.on('movein', function(rec){
				_this.loadRecord(rec);
			});
		}
		this.on('moveout', function(){
			_this.reset();
		});
	},
	buildContentObject : function(){
		var _this = this;
		this.resolveButtons();
		this.contentPanel = new Ext.form.FormPanel({
			items : _this.createGroups(),
			frame:true,
			height : 'auto',
			autoScroll : true,
			buttonAlign : 'center',
			labelAlign : 'center',
			layout : 'form',
			buttons : _this.formButtons
		});
		this.add(this.contentPanel);
	},
	resolveButtons : function(){
		var buts = this.formButtons;
		var _this = this;
		Ext.each(buts,function(b){
			delete b.handler;
			b.ownerView = _this;
			if(Ext.isFunction(b.fn)){
				b.handler = function(){
					b.fn.call(_this, _this.contentPanel, _this.contentPanel.getForm());
				};
			}
		});
	},
	createGroups : function(){
		var groupPanels = [];
		var groupCfgs = this.groups;
		var _this = this;
		Ext.each(groupCfgs, function(g){
			groupPanels.push(_this.createGroupPanel(g));
		});
		return groupPanels;
	},
	createGroupPanel : function(groupCfg){
		var panelCfg = {};
		var vs = this.vs;
		var columnCount;
		if(vs.width>1024){
			columnCount = 4;
		}else if(vs.width > 800){
			columnCount = 3;
		}else if(vs.width > 500){
			columnCount = 2;
		}else{
			columnCount = 1;
		}
		if(Ext.isNumber(groupCfg.columnCount)){
			columnCount = groupCfg.columnCount;
		}
		var fields = groupCfg.fields;
		panelCfg.layout = 'column';
		if (groupCfg.labelWidth) {
			panelCfg.labelWidth = groupCfg.labelWidth;
		}
		panelCfg.items = new Array();
		for(var i=0;i<columnCount;i++){
			panelCfg.items.push({
				layout : 'form',
				columnWidth : 1/columnCount
			});
		}
		var fieldsCfg = this.getFieldsCfg(fields);
		if(Ext.isFunction(groupCfg.fn)){
			try{
				fieldsCfg = groupCfg.fn.apply(this, fieldsCfg);
			}catch(Werror){
				fieldsCfg = fieldsCfg;
			}
		}else{
			fieldsCfg = fieldsCfg;
		}
		if(!fieldsCfg){
			fieldsCfg=[];
		}
		var fluse = fieldsCfg.length -1;
		while(fluse >=0 ){
			if(!Ext.isObject(fieldsCfg[fluse])){
				fieldsCfg.remove(fieldsCfg[fluse]);
			}
			fluse -- ;
		}
		for(var i=0;i<fieldsCfg.length; i++){
			if(!panelCfg.items[i%columnCount].items){
				panelCfg.items[i%columnCount].items=[];
			}
			panelCfg.items[i%columnCount].items.push(fieldsCfg[i]);
		}
		return panelCfg;
	},
	getFieldsCfg : function(fields){
		var result = [];
		var _this = this;
		if(Ext.isArray(fields)){
			Ext.each(fields, function(field){
				var f = false;
				if(Ext.isEmpty(field)){
					f = false;
				}else if(Ext.isString(field)){
					f =  _this.ownerApp.copyFieldsByName(field);
				}else{
					f =  field;
				}
				if(f){
					f.fieldLabel  = f.text ? f.text : f.name;
					f.xtype = f.xtype? f.xtype : 'textfield';
					f.hidden = f.text ? false : true;
					f.anchor = '90%';
					if(f.allowBlank === false){
						f.fieldLabel = WLJTOOL.addBlankFlag(f.fieldLabel);
					}
					if(f.translateType){
						f.xtype = f.multiSelect?'lovcombo':'combo';
						f.store = _this.ownerApp.lookupManager[f.translateType];
						if(!f.store){
							Ext.error('字段【'+f.text+'】的数据字典映射项store【'+f.translateType+'】获取错误，请检查[lookupTypes|localLookup]项配置');
							return false;
						}
						f.valueField = 'key';
						f.displayField = 'value';
						f.editable = typeof f.editable === 'boolean' ? f.editable : false;
						f.forceSelection = true;
						f.triggerAction = 'all';
						f.mode = 'local';
						f.hiddenName = f.name;
						f.separator = f.multiSeparator?f.multiSeparator:_this.ownerDomain.multiSelectSeparator;
					}else{
						f.name = f.name;
						if(f.dataType && WLJDATATYPE[f.dataType]){
							f.xtype = WLJDATATYPE[f.dataType].getFieldXtype();
							Ext.applyIf(f, WLJDATATYPE[f.dataType].getFieldSpecialCfg());
						}
					}
				}
				result.push(f);
			});
		}
		return result;
	},
	getFormPanel : function(){
		return this.contentPanel();
	},
	reset : function(){
		if(this.contentPanel && this.contentPanel.getForm()){
			this.contentPanel.getForm().reset();
		}
	},
	loadRecord : function(record){
		this.reset();
		if(record){
			this.contentPanel.getForm().loadRecord(record);
		}
	},
	getFieldByName : function(name){
		return this.contentPanel.getForm().findField(name);
	},
	getValues : function(asString){
		if(asString === true){
			return this.contentPanel.getForm().getValues(true);
		}else{
			return this.contentPanel.getForm().getValues(false);
		}
	}
});
Ext.reg('rformview', Wlj.frame.functions.app.widgets.FormView);


Wlj.frame.functions.app.widgets.GridView = Ext.extend(Wlj.frame.functions.app.widgets.BView, {
	
	url : false,
	fields : false,
	pageable : true,
	grideditable : false,
	isCsm : true,
	isRn : true,
	buttons : false,
	jsonCount : 'json.count',
	jsonRoot : 'json.data',
	pagingDisplayMsg : '显示{0}条到{1}条，共{2}条',
	emptyText : '没有符合条件的记录',
	
	initComponent : function(){
		Wlj.frame.functions.app.widgets.GridView.superclass.initComponent.call(this);
		this.collectFileds();
		this.buildStore();
		if(this.pageable){
			this.buildPagingBar();
		}
		this.buildGrid();
		this.add(this.grid);
	},
	
	collectFileds : function(){
		var fieldsCfg = [];
		var _this = this;
		if(Ext.isArray(_this.fields.fields)){
			Ext.each(_this.fields.fields, function(f){
				if(Ext.isString(f)){
					var fCFG = _this.ownerApp.copyFieldsByName(f);
					if(fCFG){
						fCFG.header = fCFG.text;
						if(!fCFG.text){
							fCFG.hidden = true;
						}
						fCFG.dataIndex = fCFG.name;
						delete fCFG.xtype;
						
						if(fCFG.dataType && WLJDATATYPE[fCFG.dataType]){
							fCFG.type = WLJDATATYPE[fCFG.dataType].getStoreType();
							Ext.applyIf(fCFG, WLJDATATYPE[fCFG.dataType].getStoreSpecialCfg());
						}
						
						fieldsCfg.push(fCFG);
					}
				}else if(Ext.isObject(f)){
					if(f.name){
						f.header = f.text;
						if(!f.text){
							f.hidden = true;
						}
						f.dataIndex = f.name;
						delete f.xtype;
						if(f.dataType && WLJDATATYPE[f.dataType]){
							f.type = WLJDATATYPE[f.dataType].getStoreType();
							Ext.applyIf(f, WLJDATATYPE[f.dataType].getStoreSpecialCfg());
						}
						fieldsCfg.push(f);
					}
				}else{
					fieldsCfg.push(false);
				}
			});
		}
		
		if(Ext.isFunction(_this.fields.fn)){
			try{
				fieldsCfg = _this.fields.fn.apply(this, fieldsCfg);
			}catch(Werror){
				fieldsCfg = fieldsCfg;
			}
		}else{
			fieldsCfg = fieldsCfg;
		}
		
		var i= fieldsCfg.length - 1;
		while(i>=0){
			if(!fieldsCfg[i]){
				fieldsCfg.remove(fieldsCfg[i]);
			}
			i--;
		}
		_this.fields.fields = fieldsCfg;
	},
	
	buildStore : function(){
		var _this = this;
		_this.store = new Ext.data.Store({
			restful:true,	
	        proxy : new Ext.data.HttpProxy({url:_this.url}),
	        reader : new Ext.data.JsonReader({
	        	totalProperty : _this.jsonCount,
	        	root : _this.jsonRoot
	        },_this.fields.fields)
		});		
	},
	
	buildPagingBar : function(){
		var _this = this;
		this.pagingCombo =  new Ext.form.ComboBox({
	        name : 'pagesize',
	        triggerAction : 'all',
	        mode : 'local',
	        store : new Ext.data.ArrayStore({
	            fields : ['value', 'text'],
	            data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
							[ 100, '100条/页' ], [ 250, '250条/页' ],
							[ 500, '500条/页' ] ]
	        }),
	        valueField : 'value',
	        displayField : 'text',
	        value: 20,
	        editable : false,
	        width : 85
	    });
		this.pagingbar = new Ext.PagingToolbar({
			pageSize : parseInt(this.pagingCombo.getValue()),
			store : this.store,
			displayInfo : true,
			displayMsg : _this.pagingDisplayMsg,       
			emptyMsg : _this.emptyText,
			items : ['-', '&nbsp;&nbsp;', this.pagingCombo]
		});
		
		this.pagingCombo.on("select", function(comboBox) {
			_this.pagingbar.pageSize = parseInt(comboBox.getValue()),
	        _this.store.reload({
	            params : {
	                start : 0,
	                limit : parseInt(comboBox.getValue())
	            }
	        });
	    });
	},
	createTbarCfg : function(){
		var tbs = [];
		var _this = this;
		if(Ext.isArray(_this.gridButtons)){
			Ext.each(_this.gridButtons, function(gb){
				if(Ext.isFunction(gb.fn)){
					gb.handler = function(){
						gb.fn.call(_this, _this.grid);
					};
				}
				tbs.push(gb);
			});
		}
		return tbs.length>0 ? tbs : false;
	},
	
	buildGrid : function(){
		var _this = this;
		if(_this.isCsm){
			_this.csm = new Ext.grid.CheckboxSelectionModel();
			_this.fields.fields.unshift(_this.csm);
		}
		if(_this.isRn){
			_this.rn = new Ext.grid.RowNumberer({
		        header : 'No.',
		        width : 35
			});
			_this.fields.fields.unshift(_this.rn);
		}
		if(this.grideditable){
			this.grid = new Ext.grid.EditorGridPanel({
				store : _this.store,
				frame : true,
				clicksToEdit:1,
				tbar : _this.createTbarCfg(),
				viewConfig : {
					autoFill : false 
				},
				cm : new Ext.grid.ColumnModel(_this.fields.fields),
				sm : _this.isCsm?_this.csm:null,
				footer : false,
				bbar : _this.pageable ? _this.pagingbar : false
			});
		}else{
			this.grid = new Ext.grid.GridPanel({
				store : _this.store,
				frame : true,
				tbar : _this.createTbarCfg(),
				viewConfig : {
					autoFill : false 
				},
				cm : new Ext.grid.ColumnModel(_this.fields.fields),
				sm : _this.isCsm?_this.csm:null,
				footer : false,
				bbar : _this.pageable ? _this.pagingbar : false
			});
		}
	},	
	getStore : function(){
		return this.store;
	},
	getGrid : function(){
		return this.grid;
	},
//	nextpage : function(){},
//	prepage : function(){},
//	firstpage : function(){},
//	lastpage : function(){},
//	currentpage : function(){},
	setParameters : function(params){
		delete this.currentParams;
		delete this.store.baseParams;
		this.currentParams = params;
		this.store.baseParams = params;
		if(this.pageable){
			this.store.load({
				params : {
					start : 0,
					limit : this.pagingbar.pageSize                                                      
				}
			});
		}else{
			this.store.load();
		}
	},
	getSeleted : function(){}
});
Ext.reg('rgridview', Wlj.frame.functions.app.widgets.GridView);

/**
 * 下拉树组件
 */
Wlj.frame.functions.app.widgets.ComboTree = Ext.extend(Ext.form.ComboBox, {
	
	innerTree : false,
	showField : false,
	hideField : false,
	singleSelect : true,
	
	anchor : '95%',
	mode : 'local',
	resizable :false,
	forceSelection:true,
	triggerAction : 'all',
	maxHeight : 390,
	onSelect : Ext.emptyFn,
	assertValue : Ext.emptyFn,
	
	initComponent : function(){
		this.store = new Ext.data.SimpleStore({
			fields : [],
			data : [[]]
		});
		this.tplId = 'innerContainer_'+this.id;
		this.tpl =  "<tpl for='.'><div id='"+this.tplId+"'></div></tpl>";
		Wlj.frame.functions.app.widgets.ComboTree.superclass.initComponent.call(this);
	},
	onRender : function(ct, position){
		Wlj.frame.functions.app.widgets.ComboTree.superclass.onRender.call(this, ct, position);
		if(!this.innerTree){
			return false;
		}
		if(typeof this.innerTree == "string" && TreeManager){
			this.innerTree = TreeManager.createTree(this.innerTree);
		}else if(typeof this.innerTree == 'object'){
			if(!this.innerTree instanceof Ext.tree.TreePanel){
				this.innerTree = new Com.yucheng.bcrm.TreePanel(this.innerTree);
			}
		}
		this.innerTree.frame = true;
		var _this = this;
		this.innerTree.on('click', function(node){
			_this.clickFn(node);
		});
	},
	expand : function(){
		Wlj.frame.functions.app.widgets.ComboTree.superclass.expand.call(this);
		if(!this.innerTree.rendered){
			this.innerTree.render(this.tplId);
			this.innerTree.setHeight(390);
		}
	},
	clickFn : function(node){
		var attribute = node.attributes;
		var showField  = this.showField;
		var hideField = this.hideField;
		this.setValue(attribute[hideField],node);
		if(this.singleSelect){
			this.collapse();
		}
	},
	setValue : function(hidevalue, node){
		node  = node ? node : this.innerTree.resloader.hasNodeByProperties(this.hideField, hidevalue);
		if(!node ){
			this.selectedNode = false;
			this.showValue = hidevalue;
			Ext.form.ComboBox.superclass.setValue.call(this, hidevalue);
		}else{
			this.selectedNode = node;
			var showvalue = node.attributes ? node.attributes[this.showField] : node[this.showField];
			this.showValue = showvalue;
			Ext.form.ComboBox.superclass.setValue.call(this, hidevalue);
			
			this.el.dom.value = showvalue;
		}
		this.lastSelectionText = this.showValue;
	},
	getValue : function(){
		return this.value ? this.value : this.showValue;
	},
	getShowValue : function(){
		return this.showValue;
	},
	clearValue : function(){
		Wlj.frame.functions.app.widgets.ComboTree.superclass.clearValue.call(this);
		this.selectedNode = false;
	},
	getSelectNode : function(){
		return this.selectedNode;
	}
});
Ext.reg('wcombotree', Wlj.frame.functions.app.widgets.ComboTree);

Wlj.frame.functions.app.widgets.TreeManager = Ext.extend(Ext.util.Observable, {
	loaded : false,
	constructor : function(cfg){
		this.loaders = {};
		this.treesCfg = {};
		this.dataStores = {};
		Wlj.frame.functions.app.widgets.TreeManager.superclass.constructor.call(this, cfg);
	},
	addLoader : function(key, cfg){
		if(!Ext.isString(key)){
			Ext.warn('错误','loader的key值设置失败，应配置为String类型。');
			return false;
		}
		if(!cfg.url){
			Ext.warn('错误','loader['+key+']创建失败，没有URL');
			return false;
		}
		if(this.loaders[key]){
			Ext.warn('错误','key['+key+']:已存在对应loader对象，请修改key值');
			return false;
		}
		var _this = this;
		var loader = new Com.yucheng.bcrm.ArrayTreeLoader(cfg);
		Ext.Ajax.request({
			url : cfg.url,
			method:'GET',
			success:function(response){
				var nodeArra = Ext.util.JSON.decode(response.responseText);
				if(Ext.isString(loader.jsonRoot)){
					try{
						loader.nodeArray = eval('nodeArra.'+loader.jsonRoot);
					}catch(e){
						loader.nodeArray = nodeArra;
					}
				}else{
					loader.nodeArray = nodeArra;
				}
				
				var children = loader.loadAll();
				var lookupstore = new Ext.data.JsonStore({
					fields : [{
						name : 'key',
						mapping : 'id'
					},{
						name : 'value',
						mapping : 'text'
					}],
					data : loader.nodeArray
				});
				_this.dataStores[key] = lookupstore;
				Ext.each(loader.supportedTrees,function(atree){
					atree.appendChild(children);
				});
				if(Ext.isFunction(cfg.callback)){
					cfg.callback.apply(loader,[loader]);
				}
			}
		});
		this.loaders[key] = loader;
		return loader;
	},
	initTree : function(key, cfg){
		if(!Ext.isString(key)){
			Ext.warn('tree的key值设置失败，应配置为String类型。');
			return false;
		}
		if(this.treesCfg[key]){
			Ext.warn('key['+key+']:已存在对应tree面板配置，请修改key值');
			return false;
		}
		this.treesCfg[key] = cfg;
		return cfg;
	},
	/**
	 * cfg : string/object
	 */
	createTree : function(cfg){
		if(!this.loaded){
			this.loadTreesCfgs();
		}
		if(Ext.isString(cfg)){
			var treeCfg = this.treesCfg[cfg];
			if(!treeCfg){
				Ext.warn('['+cfg+']:未查找到相关树形结构配置！');
				return false;
			}
			if(treeCfg.loaderKey && !treeCfg.resloader){
				treeCfg.resloader = this.loaders[treeCfg.loaderKey];
			}
			
			var reTreeCfg = Ext.apply({
				autoScroll : true
			},treeCfg);
			
			if(reTreeCfg.rootCfg){
				reTreeCfg.root = new Ext.tree.AsyncTreeNode(reTreeCfg.rootCfg);
			}
			return new Com.yucheng.bcrm.TreePanel(reTreeCfg);
		}else{
			if(!cfg.key){
				Ext.warn('树形面板配置没有key字段，请为其配置string类型唯一key值！');
				return false;
			}
			if(this.initTree(cfg.key, cfg)){
				return this.createTree(cfg.key);
			}
		}
	},
	loadTreesCfgs : function(){
		this.checkTreesCfgs();
		this.loaded = true;
		Ext.log('初始化树形结构管理器！');
		var loaderCfgs = window.treeLoaders;
		var treesCfg = window.treeCfgs;
		if(Ext.isArray(loaderCfgs)){
			Ext.each(loaderCfgs, function(l){
				TreeManager.addLoader(l.key,l);
			});
			if(Ext.isArray(treesCfg)){
				Ext.each(treesCfg, function(t){
					TreeManager.initTree(t.key,t);
				});
			}
		}
	},
	checkTreesCfgs : function(){
		Ext.log('树形结构配置检查，treeLoaders|treeCfgs');
		var loaderKeyMap = {};
		if(!Ext.isArray(window.treeLoaders)){
			Ext.log('无树形结构数据源配置[treeLoaders],或配置结构不正确');
			window.treeLoaders = false;
			window.treeCfgs = false;
		}else{
			Ext.log('发现['+treeLoaders.length+']个树形结构数据源配置:');
			for(var tli =0; tli<treeLoaders.length;tli++){
				var tl = treeLoaders[tli];
				if(!tl.key){
					Ext.warn('第['+tli+']个树形结构数据源无key属性；将被忽略');
					continue;
				}
				if(!tl.url){
					Ext.warn('第['+tli+']个树形结构数据源无url属性；将被忽略');
					continue;
				}
				if(!tl.parentAttr){
					Ext.warn('第['+tli+']个树形结构数据源无parentAttr属性；将被忽略');
					continue;
				}
				if(!tl.locateAttr){
					Ext.warn('第['+tli+']个树形结构数据源无locateAttr属性；将被忽略');
					continue;
				}
				if(!tl.rootValue){
					Ext.warn('第['+tli+']个树形结构数据源无rootValue属性；将被忽略');
					continue;
				}
				if(!tl.textField){
					Ext.warn('第['+tli+']个树形结构数据源无textField属性；将被忽略');
					continue;
				}
				if(!tl.idProperties){
					Ext.warn('第['+tli+']个树形结构数据源无idProperties属性；将被忽略');
					continue;
				}
				loaderKeyMap[tl.key] = tl;
			}
			Ext.log('树形结构数据源配置检查完毕');
		}
		if(!Ext.isArray(window.treeCfgs)){
			Ext.log('无树形面板配置[treeCfgs],或配置结构不正确');
			window.treeCfgs = false;
		}else{
			Ext.log('发现['+treeCfgs.length+']个树形面板配置:');
			for(var tci=0;tci<treeCfgs.length;tci++){
				var tc = treeCfgs[tci];
				if(!tc.key){
					Ext.warn('第['+tci+']个树形面板无key属性；将被忽略');
					continue;
				}
				if(tc.loaderKey){
					if(!loaderKeyMap[tc.loaderKey]){
						Ext.warn('第['+tci+']个树形面板关联数据源不存在，请检查loaderKey属性！');
					}
				}
			}
			Ext.log('树形面板配置检查完毕');
		}
		Ext.log('树形结构配置treeLoaders|treeCfgs检查完毕');
	}
});

TreeManager = new Wlj.frame.functions.app.widgets.TreeManager();



Wlj.frame.functions.app.widgets.CellProxy = Ext.extend(Object, {
	
	constructor : function(tile, config){
    	this.tile = tile;
    	this.id = this.tile.id +'-ddproxy';
    	Ext.apply(this, config);        
	},
	/**
	 * true:拖动的时候在容器内插入代理的节点；false：不插入。
	 */
	insertProxy : false,
    setStatus : Ext.emptyFn,
    reset : Ext.emptyFn,
    update : Ext.emptyFn,
    stop : Ext.emptyFn,
    sync: Ext.emptyFn,
    
    getEl :  function(){
		return this.ghost;
	},
	getGhost : function(){
		return this.ghost;
	},
	buildCellGhost : function(appendTo){
		var el = document.createElement('div');
			el.style.width =150 + 'px';
			el.style.height = 27 + 'px';
			el.className = 'x-panel-ghost '+this.tile.dom.className;
			el.innerHTML = '<b>'+this.tile.dom.innerText+'</b>';
			Ext.getDom(appendTo).appendChild(el);
		return Ext.get(el);
	},
	getProxy : function(){
		return this.proxy;
	},
	hide : function(){
		if(this.ghost){
			if(this.proxy){
				this.proxy.remove();
				delete this.proxy;
			}
			this.ghost.remove();
			delete this.ghost;
		}
	},
	show : function(){
		if(!this.ghost){
			this.ghost = this.buildCellGhost(Ext.getBody());
			this.ghost.setXY(this.tile.getXY());
			if(this.insertProxy){
				this.proxy = this.tile.el.insertSibling({cls:'tile'});
				this.proxy.setSize(this.tile.getSize());
			}
			//this.tile.el.dom.style.display = 'none';
		}
	},
	repair : function(xy, callback, scope){
		this.hide();
		if(typeof callback == 'function'){
			callback.call(scope || this);
		}
	},
	moveProxy : function(parentNode, before){
		if(this.proxy){
			parentNode.insertBefore(this.proxy.dom, before);
		}
	}
});

Wlj.frame.functions.app.widgets.CellDD = Ext.extend(Ext.dd.DragSource, {
	constructor : function(tile, cfg){
		this.tile = tile;
		this.proxy = new Wlj.frame.functions.app.widgets.CellProxy(tile, cfg);
		Wlj.frame.functions.app.widgets.CellDD.superclass.constructor.call(this, tile, cfg);
		var el = tile;
		this.scroll = false;
	},
	showFrame: Ext.emptyFn,
    startDrag: Ext.emptyFn,
    b4StartDrag : function(x, y){
		this.proxy.show();
	},
	b4MouseDown : function(e){
		var x = e.getPageX(),
			y = e.getPageY();
		this.autoOffset(x, y);
	},
	onInitDrag : function(x, y){
		this.onStartDrag(x, y);
		return true;
	},
	createFrame : Ext.emptyFn,
	getDragEl : function(e){
		return this.proxy.ghost.dom;
	},
	endDrag : function(e){
		this.proxy.hide();
		this.destroy();
	},
	autoOffset : function(x, y){
		x -= this.startPageX;
		y -= this.startPageY;
		this.setDelta(x, y);
	},
	onDrag : function(e){
	}
});
