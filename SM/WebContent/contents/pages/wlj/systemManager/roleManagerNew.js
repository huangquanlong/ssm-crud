/**
 * 系统管理-角色管理
 * @author ZHANGHAO
 * @since 2014-4-29
 */

		/***
		 * 引入公共js文件
		 */
		imports([
		        '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js',
		        '/contents/pages/common/Com.yucheng.crm.common.OrgUserManage.js'
		        ]);	
		var roleIdgrant = '';//用于角色授权的ID
		var roleNamegrant = '';//用户查看授权用户的角色名称
		var _roleCodeGloble = "";//用于复制的角色ID
		var createView = true;
		var editView = true;
		var detailView = true;
		var lookupTypes = ['ROLE_TYPE','ROLE_LEVEL'];
		var url = basepath+'/roleManagerQuery.json';
		var comitUrl = basepath+'/roleManagerQuery.json';
		var fields = [
		  		    {name: 'ID',hidden : true},
		  		    {name: 'ROLE_ID',hidden : true},
		  		    {name: 'ACCOUNT_ID',text:'机构名称',hidden : true},
		  		    {name: 'ROLE_CODE', text : '角色编码',allowBlank:false},                                   
		  		    {name: 'ROLE_NAME',text:'角色名称', searchField: true,resutlWidth:150,allowBlank:false},  
		  		    {name: 'ROLE_TYPE', text :'角色类型', searchField: true,resutlWidth:150,translateType : 'ROLE_TYPE',allowBlank:false},
		  		    {name: 'ROLE_LEVEL', text:'角色层级',resutlWidth:350,translateType : 'ROLE_LEVEL',allowBlank:false},
		  		    {name: 'APP_ID',resutlWidth:350,hidden : true} ,
		  		    {name: 'ACCOUNT_ID',resutlWidth:350,hidden : true} 
		  		];
		  		
		var createFormViewer = [{
			fields : ['ACCOUNT_ID','ROLE_CODE','ROLE_NAME','ROLE_TYPE','ROLE_LEVEL','APP_ID'],
			fn : function(ACCOUNT_ID,ROLE_CODE,ROLE_NAME,ROLE_TYPE,ROLE_LEVEL,APP_ID){
					//给每个面板的APP_ID字段赋值62
					APP_ID.value='62';
				return [ACCOUNT_ID,ROLE_CODE,ROLE_NAME,ROLE_TYPE,ROLE_LEVEL,APP_ID];
			}
		}];	
		var editFormViewer = [{
			fields : ['ACCOUNT_ID','ROLE_CODE','ROLE_NAME','ROLE_TYPE','ROLE_LEVEL','APP_ID'],
			fn : function(ACCOUNT_ID,ROLE_CODE,ROLE_NAME,ROLE_TYPE,ROLE_LEVEL,APP_ID){
				ROLE_CODE.readOnly = true;
				ROLE_CODE.cls = "x-readOnly";
				ROLE_CODE.allowBlank = true;
					//给每个面板的APP_ID字段赋值62
					APP_ID.value='62';
				return [ACCOUNT_ID,ROLE_CODE,ROLE_NAME,ROLE_TYPE,ROLE_LEVEL,APP_ID];
			}
		}];	
		var detailFormViewer = [{
			fields : ['ACCOUNT_ID','ROLE_CODE','ROLE_NAME','ROLE_TYPE','ROLE_LEVEL','APP_ID'],
			fn : function(ACCOUNT_ID,ROLE_CODE,ROLE_NAME,ROLE_TYPE,ROLE_LEVEL,APP_ID){
				ROLE_CODE.readOnly = true;
				ROLE_CODE.cls = "x-readOnly";
				ROLE_CODE.allowBlank = true;
				ROLE_NAME.allowBlank = true;
				ROLE_TYPE.allowBlank = true;
				ROLE_LEVEL.allowBlank = true;
				ROLE_CODE.disabled = true;
				ROLE_NAME.disabled = true;
				ROLE_TYPE.disabled = true;
				ROLE_LEVEL.disabled = true;
				ROLE_CODE.cls = "x-readOnly";
				ROLE_NAME.cls = "x-readOnly";
				ROLE_TYPE.cls = "x-readOnly";
				ROLE_LEVEL.cls = "x-readOnly";
				return [ACCOUNT_ID,ROLE_CODE,ROLE_NAME,ROLE_TYPE,ROLE_LEVEL,APP_ID];
			}
		}];	
		var tbar = [{
			text : '删除',
			handler : function(){//删除角色数据
				if(getSelectedData() == false){//判断是否选择一行数据
						Ext.Msg.alert('提示','请选择一条数据！');
						return false;
					}else{
						var id=getSelectedData().data.ID;
						Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
							if(buttonId.toLowerCase() == "no"){
								return;
							}  
							var selectRe;
							var tempId;
							var tempCount;
							Ext.Ajax.request({
									url : basepath+ '/roleManagerQuery!batchDestroy.json?idStr='+ id,
									waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
									success : function() {
										Ext.Msg.alert('提示', '操作成功!' );
										reloadCurrentData();
										},
									failure : function() {
										Ext.Msg.alert('提示', '操作失败!' );
										reloadCurrentData();
								}
							});

						});
					}
			}
		}];
		var condition = {searchType:'SUBTREE'};
		var treeLoaders = [{
			key : 'DATASETMANAGERLOADER',
			url : basepath + '/commsearch.json?condition='+Ext.encode(condition),
			parentAttr : 'SUPERUNITID',
			locateAttr : 'UNITID',
			jsonRoot:'json.data',
			rootValue : JsContext._orgId,
			textField : 'UNITNAME',
			idProperties : 'UNITID'
		}];
		var treeCfgs = [{
			key : 'DATASETMANAGERTREE',
			loaderKey : 'DATASETMANAGERLOADER',
			autoScroll:true,
			rootCfg : {
				id:JsContext._orgId,
				expanded:true,
				text:JsContext._unitname,
				autoScroll:true,
				children:[]
			},clickFn : function(node){
				setSearchParams({//按机构ID查询
					accountId: node.id
				});
			}
		},{
			key : 'DATASETMANAGERTREE1',
			loaderKey : 'DATASETMANAGERLOADER',
			autoScroll:true,
			rootCfg : {
				id:JsContext._orgId,
				expanded:true,
				text:JsContext._unitname,
				autoScroll:true,
				children:[]
			}
		}];
		var edgeVies = {
				left : {
					width : 200,
					layout : 'form',
					items : [TreeManager.createTree('DATASETMANAGERTREE')]
				}
			};
		/***
		 * 自定义滑动面板
		 */
		var customerView = [{
			title : '复制角色',
			url : basepath + '/roleManagerQuery!getAuthRoles.json',
			type : 'grid',
			pageable : false,
			frame : true,
			buttonAlign: 'center',
			fields : {
				fields : [
					{name : 'ID'},
					{name : 'ROLE_ID'},
					{name : 'ROLE_CODE'},
					{name : 'ROLE_TYPE'},
					{name : 'ROLE_NAME',text : '角色名称',width: 300}
				],
				fn : function(ID, ROLE_ID,ROLE_CODE,ROLE_TYPE,ROLE_NAME){
					return [ID, ROLE_ID,ROLE_CODE,ROLE_TYPE,ROLE_NAME];
				}
			},
			gridButtons : [{
				text : '复制到选定角色',
				fn : function(grid){
					if(grid.getSelectionModel().selections.length>0){
						var _record = grid.getSelectionModel().getSelections();
						var roleIds = "";
						//拿到所有选中的角色ID
						for ( var i = 0; i < _record.length; i++) {
							if(i*1 == 0)
							{
								roleIds += _record[i].data.id;
							} else {
								roleIds = roleIds + ',' + _record[i].data.id;
							}
						}
						Ext.Ajax.request({//执行保存设置
							//增量数据操作url
							url : basepath + '/roleManagerQuery!copyRoleToRole.json?oldRoleCode='+_roleCodeGloble+'&toRoleCodes='+roleIds,
							method:'GET',
							success:function(response){
								Ext.Msg.alert('提示','操作成功！');
								grid.store.load();
							},
							failure:function(){
								Ext.Msg.alert('提示','操作失败！');
								grid.store.load();
							}
						});
					}else{
						Ext.Msg.alert("操作提示","请选择角色！");
					}
				}
			},{
				text:'复制并生成新角色',
				fn : function(grid){
					if(grid.getSelectionModel().selections.length!=1){
						Ext.Msg.alert('提示','请选择一条数据!');
						return false;
					}else{
						showCustomerViewByTitle('复制生成新角色面板');
						}
					}
				}]
		},{
			title : '复制生成新角色面板',
			type : 'form',
			hideTitle : true,
			autoLoadSeleted : true,
			groups : [{
				columnCount : 1 ,
				fields : [{name : 'NEW_ROLE_NAME',text : '新角色名称'
						},{name : 'NEW_ROLE_CODE',text : '新角色编码'
						}],
				fn : function(NEW_ROLE_NAME, NEW_ROLE_CODE){
								NEW_ROLE_NAME.allowBlank = false;
								NEW_ROLE_CODE.allowBlank = false;
					return [NEW_ROLE_NAME, NEW_ROLE_CODE];
				}
			}],
			formButtons : [{
				text : '确定',
				fn : function(formPanel,basicForm){
				if(!basicForm.isValid())
				{
					Ext.Msg.alert('提示','请输入完整！');
					return false;
				}
				var newRoleName = basicForm.findField('NEW_ROLE_NAME').getValue();
				var newRoleCode = basicForm.findField('NEW_ROLE_CODE').getValue();
				Ext.Ajax.request({//执行保存设置
					//增量数据操作url
					url : basepath + '/roleManagerQuery!copyNewRole.json?oldRoleCode='+_roleCodeGloble+'&newRoleCode='+newRoleCode+'&newRoleName='+newRoleName,
					method:'GET',
					success:function(response){
						Ext.Msg.alert('提示','操作成功！');
						showCustomerViewByTitle('复制角色');
					},
					failure:function(){
						Ext.Msg.alert('提示','操作失败！');
						showCustomerViewByTitle('复制角色');
					}
				});
			}
			},{
				text : '取消',
				fn : function(formPanel,basicForm){
				 		showCustomerViewByTitle('复制角色');
				}
			}]	
		},{
			title : '授权用户查看',
			url : basepath + '/roleWarrantUserInfoQuery.json',
			type : 'grid',
			pageable : true,
			frame : true,
			buttonAlign: 'center',
			fields : {
				fields : [{name : 'ID'
						},{name : 'ACCOUNT_NAME',text:'用户ID'
						},{name : 'USER_NAME',text:'用户姓名'
						},{name : 'ORG_ID',text : '机构ID'
						}],
						fn : function(ID, ACCOUNT_NAME,USER_NAME,ORG_ID){
							return [ID, ACCOUNT_NAME,USER_NAME,ORG_ID];
					}
					},
			gridButtons : [{
				text:'新增授权',
				fn : function(grid){
						showCustomerViewByTitle('新增授权面板');	
					}
				},{
					text:'取消授权',
					fn : function(grid){
						if (grid.selModel.hasSelection()) {//取消授权
							Ext.MessageBox.confirm('系统提示信息','确定要对所选的用户取消授权么?',
								function(buttonobj) {
									if (buttonobj == 'yes') {
										var records = grid.selModel.getSelections();// 得到被选择的行的数组
										var selectLength = records.length;// 得到行数组的长度
										var idStr = '';//用于存储将删除角色的id
										var tempId;
										for ( var i = 0; i < selectLength; i++) {
											selectRe = records[i].data.ID;
											tempId = selectRe;
											idStr += tempId;
											if (i != selectLength - 1){
												idStr += ',';
											}
										};	
										Ext.Ajax.request({
											url :basepath+'/roleAccountGrant-action!batchDestroy.json',//删除请求url
											params : {
												idStr : idStr//传递参数，将删除角色id
											},
											waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
											method : 'POST',
											scope : this,
											success : function() {
												Ext.Msg.alert('提示', '操作成功');
												grid.store.load({
													params : {
									 					roleId : roleIdgrant
									 				}
									 			});
											}
										});
									}
								}
							);
						} else {
							Ext.Msg.alert("提示", "请先选择要取消授权的用户的行!");
							}
						}
					}]
		},{
			title : '新增授权面板',
			type : 'form',
			hideTitle : true,
			autoLoadSeleted : true,
			groups : [{
				columnCount : 1 ,
				fields : [{name : 'ROLE_NAME',text : '所授权的角色'
						},{name : 'USE_RNAME',text : '所授权的用户',xtype:'userchoose',		
							callback:function(a){
											istr = '';
											var tempid = '';
												for ( var i = 0; i < a.length; i++) {
													seletId = a[i].data.id;
													tempid = seletId;
													istr += tempid;
													if (i != a.length - 1){
														istr += ',';
													}
												};	
										}
							}],
				fn : function(ROLE_NAME, USE_RNAME){
								ROLE_NAME.readOnly = true;
								ROLE_NAME.cls='x-readOnly' ;
								USE_RNAME.allowBlank = false;
					return [ROLE_NAME, USE_RNAME];
				}
			}],
			formButtons : [{
				text:'确定',
				fn : function(formPanel,basicForm){
					if(!basicForm.isValid()){ //输入项检查
						Ext.MessageBox.alert('提示','输入有误或存在漏输项,请检查！');
						return false;
					}
					var str = istr.split(","); 
					for(i=0;i<str.length;i++){
						for(j=1;j<str.length-1;j++){
							if(str[i]== str[j]){
								Ext.MessageBox.alert('提示','您所选择的用户重复,请检查！');
								return false;
							}
							break;
						}
				}
					Ext.Ajax.request( {
						url : basepath + '/roleAccountGrant-action!create.json',
						method : 'POST',
						params : {
							'roleId' : roleIdgrant,
							'accountIds':istr
					},
						success : checkResult,
						failure: checkResult
					});
					function checkResult(response) {
						var resultArray = Ext.util.JSON.decode(response.status);
						var resultError = response.responseText;
						if ((resultArray == 200 ||resultArray == 201)&&resultError=='') {
							Ext.Msg.alert('提示', '操作成功');
							showCustomerViewByTitle('授权用户查看');	
						} else {
							if(resultArray == 403){
								Ext.Msg.alert('提示', response.responseText);
							}else{
								Ext.Msg.alert('提示', '操作失败,失败原因:' + resultError);
							}
						};
				}				
		}
				},{
				text : '取消',
				fn : function(formPanel,basicForm){
				 		showCustomerViewByTitle('授权用户查看');
				}
			}]	
		}];	
		/***
		 * 方法：在面板滑入前进行逻辑操作
		 * @param view
		 * @return
		 */
		var beforeviewshow = function(view){
			if(view._defaultTitle!='新增'){
				if(!getSelectedData()||getAllSelects().length>1){//判断是否选择一条数据
					Ext.Msg.alert('提示信息','请选择一条数据后操作！');  
					return false;
				}else if(view._defaultTitle=='授权用户查看'){
					roleIdgrant = getSelectedData().data.ID;//获得选中记录的id
					roleNamegrant =  getSelectedData().data.ROLE_NAME;//获得选中记录的角色名称
					if(roleIdgrant!=''){
						view.setParameters({
							roleId : getSelectedData().data.ROLE_ID
						});
					}
				}else if(view._defaultTitle=='复制角色'){
					view.setParameters({});
					_roleCodeGloble = getSelectedData().data.ID;//赋值选中角色ID
				}	
			}
		};
		/***
		 * 方法：面板滑入时赋值
		 * @param view
		 * @return
		 */
		var viewshow = function(view){
			if(view._defaultTitle=='新增授权面板'){
				view.contentPanel.getForm().findField('ROLE_NAME').setValue(roleNamegrant);//给面板的ROLE_NAME赋值角色名称
			}
		};