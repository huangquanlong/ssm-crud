Ext.ns('Com.yucheng.bcrm.common');
/**
 * 需求初始化数据选择放大镜
 * @author:hujun
 * @since:2015.02.10
 */
Com.yucheng.bcrm.common.ReqmentInitQuery = Ext.extend(Ext.form.TwinTriggerField, {
	
	initComponent : function(){
		Com.yucheng.bcrm.common.ReqmentInitQuery.superclass.initComponent.call(this);
	},
	onRender : function(ct, position){
		Com.yucheng.bcrm.common.ReqmentInitQuery.superclass.onRender.call(this, ct, position);
		if(this.hiddenName){
			var ownerForm = this;
			while(ownerForm.ownerCt && !Ext.instanceOf(ownerForm.ownerCt,'form')/*&&!Ext.instanceOf(ownerForm.ownerCt,'toolbar')*/){				//根据条件查询放大镜控件的最外层容器
				ownerForm = ownerForm.ownerCt;
			};
			if(Ext.instanceOf(ownerForm.ownerCt,'form')){										//判断父容器是否为form类型
				ownerForm = ownerForm.ownerCt;
				this.ownerForm = ownerForm;
				if(ownerForm.getForm().findField(this.hiddenName)){								//如果已经创建隐藏域
					this.hiddenField = ownerForm.getForm().findField(this.hiddenName);
				}else {																			//如果未创建隐藏域，则根据hiddenName属性创建隐藏域
					this.hiddenField = ownerForm.add({
						id:this.hiddenName,
						xtype : 'hidden',
						name: this.hiddenName
					});
				}
			}/*else if(Ext.instanceOf(ownerForm.ownerCt,'toolbar')){								//判断父容器是否为toolbar类型
				ownerForm = ownerForm.ownerCt;
				this.ownerForm = ownerForm;
				this.hiddenField = ownerForm.add({
					id:this.hiddenName,
					xtype : 'hidden',
					name: this.hiddenName
				});
			}*/
		}
	},
	callback : false,							//点击确定按钮后回调函数。Type： function
	userId : '',
	validationEvent : false,
	validateOnBlur : false,
	trigger1Class : 'x-form-clear-trigger',
	trigger2Class : 'x-form-search-trigger',
	hideTrigger1 : true,
	width : 180,
	paramName : 'query',
	orgWindow : false,
	editable:false,
	orgTreeForShow : false,
	searchType:'SUBTREE',
	checkBox:'',
	hiddenName:false, 
	listeners:{//增加鼠标点击放大镜输入框触发onTrigger2Click事件
		focus:function(){
			if(!this.disabled){ //禁用的放大镜不允许弹出选择
				this.onTrigger2Click();
			}
		}
	},
	onTrigger2Click : function() {
		if(this.disabled){ //禁用的放大镜不允许弹出选择
			return;
		}
		var _this = this;
		var s = new String("需求");
		if (_this.orgWindow) {
			_this.orgWindow.show();
			return;
		}
	
		_this.loader = new Com.yucheng.bcrm.ArrayTreeLoader({
			checkField : 'ASTRUE',//选择字段
			parentAttr : 'REQ_DIR_PARENT_NO',
			locateAttr : 'root',//UNITID
			rootValue :'0',
			textField : 'REQ_DIR_NAME',
			idProperties : 'REQ_DIR_ID'//节点点击事件句柄
		});
		var condition = {searchType:_this.searchType};
		Ext.Ajax.request({
			url : basepath + '/ReqmentContetTreeAndVerAction.json',
			method : 'GET',
			success : function(response) {
				var nodeArra = Ext.util.JSON.decode(response.responseText).json.data;
				_this.loader.nodeArray = nodeArra;
				var children = _this.loader.loadAll();
				_this.orgTreeForShow.appendChild(children);
				//_this.orgTreeForShow.expandAll();
			},
			failure : function(a, b, c) {
			}
		});
	
		var s2 = new String('所有机构');
		_this.orgTreeForShow = new Com.yucheng.bcrm.TreePanel({
			checkBox : this.checkBox,
			width : 300,
			heigth : 385,
			autoScroll : true,
			/** 虚拟树形根节点 */
			root : new Ext.tree.AsyncTreeNode({
				id:'root',
				expanded:true,
				text:'需求目录树',
				autoScroll:true,
				children:[]
			}),
			resloader : _this.loader,
			split : true,
			listeners:{
				'checkchange' : function(node, checked) {
					
					//if(node.leaf){
						if (checked) {
							node.expand();
							var childNodes = node.childNodes;
							for ( var i = 0; i < childNodes.length; i++) {
									//childNodes[i].attributes.checked=true;
									childNodes[i].getUI().toggleCheck(true);
								
							}
						} else {
							//node.cancelExpand();
							var childNodes = node.childNodes;
							for ( var i = 0; i < childNodes.length; i++) {
								//childNodes[i].attributes.checked=false;
								childNodes[i].getUI().toggleCheck(false);
							}
						}
//					}else{
//						var childNodes = node.childNodes;
//						node.getUI().toggleCheck(false);
//						Ext.Msg.alert('提示','只能选择子节点！');
//						for ( var i = 0; i < childNodes.length; i++) {
//							childNodes[i].getUI().toggleCheck(false);
//						}
						
						//return false;
					//}
					
				},
				'click':function(node){ //事件在节点被单击时触发,判断该节点是否被选中
					//if(node.leaf){
						if(node.getUI().isChecked()){
							
							node.getUI().toggleCheck(false);
						}else{
								//node.attributes.checked=true;
								node.getUI().toggleCheck(true);
							
						} 
//					}else{
//						Ext.Msg.alert('提示','只能选择子节点！');
//						return false;
//					}
				}
			
			},
	
			_hiddens : []
			
		});
		
		_this.orgWindow = new Ext.Window({
			title : '需求列表',
			resizable : true,
			width : 700,
			height : 420,
			draggable : true,
			layout : 'fit',
			closeAction : 'hide',
			modal : true, // 模态窗口
			border : false,
			closable : true,
			layout : 'border',
			items : [{
				region : 'center',
				layout : 'fit',
				items : [ _this.orgTreeForShow ]
			}],
			buttonAlign : 'center',
			buttons : [{
				text : '确定',
				handler : function() {
					var json =[];
					var sName = '';
					if(_this.hiddenName){
						if(!_this.checkBox){//单选状态
							if(!(_this.orgTreeForShow.getSelectionModel().selNode==null)){
								if(!_this.orgTreeForShow.getSelectionModel().selNode.isRoot){
									json.push(_this.orgTreeForShow.getSelectionModel().selNode.attributes.id);
									sName = sName + _this.orgTreeForShow.getSelectionModel().selNode.attributes.text;
									_this.hiddenField.setValue(json[0]);
								}else{
									var checkedNodes = _this.orgTreeForShow.getSelectionModel().selNode;
									json.push(checkedNodes.attributes.id);
									if (i == 0) {
										sName = sName + checkedNodes.attributes.text;
									} else {
										sName = checkedNodes.attributes.text;
									}
									_this.hiddenField.setValue(json);
								}
							}
						}else{//多选状态
							if(!(_this.orgTreeForShow.getSelectionModel().selNode==null)&&(_this.orgTreeForShow.getChecked().length==0)){
								if(_this.orgTreeForShow.getSelectionModel().selNode.isRoot){
									var checkedNodes = _this.orgTreeForShow.getSelectionModel().selNode;
									if(checkedNodes.leaf){
										json.push(checkedNodes.attributes.id);
										sName =  checkedNodes.attributes.text.split(';')[0];
										if(_this.hiddenField){
											_this.hiddenField.setValue(json);
										}
									}
									
								}
							}else if ((_this.orgTreeForShow.getSelectionModel().selNode==null)&&(!(_this.orgTreeForShow.getChecked().length==0))){
								var checkedNodes = _this.orgTreeForShow.getChecked();
								for (var i = 0; i < checkedNodes.length; i++) {
									if(checkedNodes[i].leaf){
										json.push(checkedNodes[i].attributes.id);
										if (i == 0) {
											sName = sName + checkedNodes[i].attributes.text.split(';')[0];
										} else {
											sName = sName + ',' + checkedNodes[i].attributes.text.split(';')[0];
										}
										
									}
								}
								if(_this.hiddenField){
									_this.hiddenField.setValue(json);
								}
							}else if ((!(_this.orgTreeForShow.getSelectionModel().selNode==null))&&(!(_this.orgTreeForShow.getChecked().length==0))){
								if(_this.orgTreeForShow.getSelectionModel().selNode.isRoot){
									var checkedNodes = _this.orgTreeForShow.getSelectionModel().selNode;
									if(checkedNodes.leaf){
										json.push(checkedNodes.attributes.id);
										sName =  checkedNodes.attributes.text.split(';')[0]+',';
									}
								}
								var checkedNodes2 = _this.orgTreeForShow.getChecked();
								for (var i = 0; i < checkedNodes2.length; i++) {
									if(checkedNodes2[i].leaf){
										json.push(checkedNodes2[i].attributes.id);
										if (i == 0) {
											sName = sName + checkedNodes2[i].attributes.text.split(';')[0];
										} else {
											sName = sName + ',' + checkedNodes2[i].attributes.text.split(';')[0];
										}
									}
									
								}
								if(_this.hiddenField){
									_this.hiddenField.setValue(json);
								}
							} else {//多选时若不选择任何机构直接确认  将隐藏域中的机构号置空
								if(_this.hiddenField){
									_this.hiddenField.setValue("");
								}
							}
						}
					}
					_this.Id = json;
					_this.setRawValue(sName);
					_this.orgWindow.hide();
					var nodess=_this.orgTreeForShow.getChecked();
					for(var i = 0; i < nodess.length; i++){
						nodess[i].getUI().toggleCheck(true);
					}
					if (typeof _this.callback == 'function') {
						_this.callback(_this.orgTreeForShow.getChecked());
					}
				}
			}, {
				text : '取消',
				handler : function() {
					var nodess=_this.orgTreeForShow.getChecked();
					for(var i = 0; i < nodess.length; i++){
						nodess[i].getUI().toggleCheck(true);
					}
					_this.orgWindow.hide();
					
				}
			}]
		});
		_this.orgWindow.show(this.id);
		return;
	}
});
Ext.reg('initQuery',Com.yucheng.bcrm.common.ReqmentInitQuery);