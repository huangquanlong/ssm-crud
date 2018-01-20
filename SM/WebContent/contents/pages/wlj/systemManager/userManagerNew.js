/**
 * @description 用户管理
 * @author helin
 * @since 2014-04-22
 */
imports([
	'/contents/pages/common/Com.yucheng.crm.common.PasswordValidate.js',
    '/contents/pages/com.yucheng.bcrm/com.yucheng.bcrm.js',
    /**
	 * 未引用公共机构放大镜的原因：连动时事件setValue（）时强制触发，公共机构放大镜中是setRawValue（），无法触发,故在Com.yucheng.bcrm.common.OrgFieldx.js
	 * 中奖第300行setRawValue（）改为setValue()
	 * 具体参见：Crm-Ext-Patch-1.000-v1.0.js中410行（补丁）
	 * **/
	'/contents/pages/common/Com.yucheng.bcrm.common.OrgFieldx.js' // 机构放大镜
]);

Ext.QuickTips.init();

//数据字典定义
var lookupTypes = [
	'XD000016',	 //性别
	'SYS_USER_STATE',//系统用户状态
	'READ_FLAG',     //阅读标识
	'XD000309' //归属条线
];
//选择部门store,需要根据选择的归属机构动态变换
var departStore = new Ext.data.Store( {
	autoLoad : false,
	restful : true,
	proxy : new Ext.data.HttpProxy( {
		url : basepath + '/departmentManagerAction!getDptParents.json'
	}),
	reader : new Ext.data.JsonReader({
		root : 'json.data'
	}, [
		{name : 'dptId'},
		{name : 'dptName'}
	])
});

//树配置查询类型
var treeCondition = {
	searchType: 'SUBTREE'
};
var treeLoaders = [{
	key : 'DATASETMANAGERLOADER',
	url : basepath + '/commsearch.json?condition=' + Ext.encode(treeCondition),
	parentAttr : 'SUPERUNITID',
	locateAttr : 'root',
	jsonRoot:'json.data',
	rootValue : JsContext._orgId,
	textField : 'UNITNAME',
	idProperties : 'ID'
}];
var treeCfgs = [{
	key : 'DATASETMANAGERTREE',
	loaderKey : 'DATASETMANAGERLOADER',
	autoScroll:true,
	rootCfg : {
		expanded:true,
		id:JsContext._orgId,
		text:JsContext._unitname,
		autoScroll:true,
		children:[]
	},
	clickFn : function(node){
		setSearchParams({
			TREE_STORE : node.id
		});
		getConditionField('TREE_STORE').setValue(node.id);
	}
}];

var url = basepath+'/usermanagequery.json';
var comitUrl = basepath+'/userManager!save.json';

var fields = [
    {name: 'ID',hidden : true},
    {name: 'ACCOUNT_NAME', text : '登录名', searchField: true,resutlWidth: 80,allowBlank: false},
    {name: 'USER_NAME',text:'用户姓名', searchField: true, resutlWidth:100,allowBlank: false},
    {name: 'USER_CODE', text : '员工号', searchField: true,resutlWidth: 80,allowBlank: false},
    {name: 'PASSWORD',text:'密码', resutlWidth:80,hidden: true,allowBlank: true},
    {name: 'PASSWORD1',text:'确认密码', resutlWidth:80,hidden: true,allowBlank: true},
    {name: 'APP_ID',text:'APP_ID',hidden:true, resutlWidth:80},
    {name: 'ORG_ID',text:'所属机构',resutlWidth:80,allowBlank: false,hidden:true},
    {name: 'ORG_NAME',text:'所属机构',resutlWidth:120,xtype:'orgchoose',hiddenName:'ORG_ID',searchType:'SUBTREE',allowBlank: false},
    {name: 'DPT_NAME',text:'部门', resutlWidth:120},
	{name : 'DIR_ID',text : '部门',hidden : true,xtype: 'combo',store : departStore,forceSelection : true,
		editable : false,ComboBox : true,mode : 'local',triggerAction : 'all',displayField : 'dptName',valueField : 'dptId'},
    {name: 'BIRTHDAY',text:'生日',  resutlWidth:80,dataType:'date',format:'Y-m-d'},
    {name: 'DEADLINE',text:'有效日期', resutlWidth:80,dataType:'date',format:'Y-m-d',allowBlank: false},
    {name: 'MOBILEPHONE',text:'手机',  resutlWidth:80,vtype:'mobile'},
    {name: 'OFFICETEL',text:'办公电话',  resutlWidth:80,vtype:'telephone'},
    {name: 'EMAIL',text:'邮箱', resutlWidth:80,vtype:'email'},
    {name: 'SEX',text:'性别',  resutlWidth:80,translateType : 'XD000016'},
    {name: 'USER_STATE',text:'状态',  resutlWidth:80,translateType : 'SYS_USER_STATE'},
    {name: 'BELONG_BUSI_LINE',text:'归属业务条线',translateType : 'XD000309'},
    {name: 'BELONG_TEAM_HEAD',text:'归属Team Head'},
    {name: 'OFFENIP',text:'常用IP', resutlWidth:80,vtype:'ip'},
    {name: 'LASTLOGINTIME',text:'最近登录时间', resutlWidth:80,hidden:true},
    {name: 'TEMP1',text:'头像文件名',readOnly:true,cls:'x-readOnly'},
    {name: 'TEMP2',text:'密码修改标志',hidden:true},
    {name: 'EDUCATION',text:'学历',hidden:true},
    {name: 'CERTIFICATE',text:'资格证书',hidden:true},
    {name: 'ENTRANTS_DATE',text:'入行日期',hidden:true},
    {name: 'POSITION_TIME',text:'任职时间',hidden:true},
    {name: 'POSITION_DEGREE',text:'职级（法金）',hidden:true},
    {name: 'FINANCIAL_JOB_TIME',text:'金融从业时间',hidden:true},
    {name: 'TREE_STORE',text:'树节点',searchField: true,hidden:true}
];

var createView = true;
var editView = true;
var detailView = false;

//照片上传
var uploadForm = new Ext.form.FormPanel({
	frame : true,
	fileUpload : true,//这个属性可以让form实现上传
	uploadFile:true,
	style : 'margin:10px',
	items : [{
		xtype: 'textfield',
		id : 'upload',
		name: 'image',
		width:250,
		fieldLabel: '文件上传',
		inputType: 'file',
		blankText: '请选择你要上传的头像'
	}],
	buttonAlign:'right',
	buttons : [{
		text : '上传',
		handler : function() {
			//限制图片格式
			var localFile = Ext.getCmp('upload').getEl().getValue();
			if(!localFile || localFile == ''){
				Ext.Msg.alert("提示", '请选择你要上传的头像！');
				return false;
			}
			var t_ext = localFile.substring(localFile.lastIndexOf('.'));
			var extArr = ['.jpg', '.jpeg', '.gif', '.png'];
			if(extArr.indexOf(t_ext) == - 1){
				Ext.Msg.alert('提示', '上传文件格式必须为:"jpg", "jpeg", "gif", "png"！');
				return false;
			}
			uploadForm.getForm().submit({
				url : basepath + '/FileUpload?isImage=isImage', // 上传图片处理 ,
				waitTitle : "请稍候",
				waitMsg : '正在上传...',
				success : function(form, o) {
					Ext.Msg.alert("提示", "上传成功！");
					var _tempFileName = Ext.util.JSON.decode(o.response.responseText).realFileName;
					getCurrentView().contentPanel.getForm().findField('TEMP1').setValue(_tempFileName);
					if(Ext.getCmp('image').getEl()){
						Ext.getCmp('image').getEl().dom.src = basepath+ '/imgshow.json?t='+new Date().getTime()+'&path='+_tempFileName ;
					}
					if(Ext.getCmp('image1').getEl()){
						Ext.getCmp('image1').getEl().dom.src = basepath+ '/imgshow.json?t='+new Date().getTime()+'&path='+_tempFileName ;
					}
					uploadWin.hide();
				},
				failure : function() {
					Ext.MessageBox.alert("提示", "上传失败！");
				}
			});
		}
	}]
});

var uploadWin = new Ext.Window({
	title : '头像上传',
	width : 400,
	height : 200,
	modal : true,
	closable : true,
	closeAction: 'hide',
	buttonAlign : 'center',
	items : [uploadForm]
});

    
    
/**
 * 新增、修改、详情设计
 */
var createFormViewer = [{
	columnCount : 3,
	fields : ['ID','ACCOUNT_NAME','USER_NAME','PASSWORD','PASSWORD1','ORG_NAME','DEADLINE','DIR_ID','BIRTHDAY','MOBILEPHONE','OFFICETEL'
		,'EMAIL','SEX','OFFENIP','USER_STATE','APP_ID','LAST_LOGIN_TIME','TEMP1'
		,'USER_CODE','BELONG_BUSI_LINE','BELONG_TEAM_HEAD','EDUCATION','CERTIFICATE','ENTRANTS_DATE','POSITION_TIME','POSITION_DEGREE','FINANCIAL_JOB_TIME','TEMP2'],
	fn : function(ID,ACCOUNT_NAME,USER_NAME,PASSWORD,PASSWORD1,ORG_NAME,DEADLINE,DIR_ID,BIRTHDAY,MOBILEPHONE,OFFICETEL
		,EMAIL,SEX,OFFENIP,USER_STATE,APP_ID,LAST_LOGIN_TIME,TEMP1
		,USER_CODE,BELONG_BUSI_LINE,BELONG_TEAM_HEAD,EDUCATION,CERTIFICATE,ENTRANTS_DATE,POSITION_TIME,POSITION_DEGREE,FINANCIAL_JOB_TIME,TEMP2){
		PASSWORD.hidden=false;
		PASSWORD1.hidden=false;
		USER_STATE.hidden=true;
		PASSWORD.allowBlank=false;
		PASSWORD1.allowBlank=false;
		PASSWORD.inputType= 'password',
		PASSWORD1.inputType= 'password',
		USER_STATE.value='1';
		APP_ID.value='62';
		DIR_ID.hidden = false;
		return [ID,ACCOUNT_NAME,USER_NAME,ORG_NAME,PASSWORD,PASSWORD1,DIR_ID,USER_CODE,DEADLINE,SEX,BIRTHDAY,MOBILEPHONE,OFFICETEL
			,EMAIL,OFFENIP,APP_ID,LAST_LOGIN_TIME
			,BELONG_BUSI_LINE,BELONG_TEAM_HEAD,TEMP1,EDUCATION,CERTIFICATE,ENTRANTS_DATE,POSITION_TIME,POSITION_DEGREE,FINANCIAL_JOB_TIME,TEMP2];
	}
},{
	columnCount : 1,
	fields : ['ID'],
	fn : function(ID){
		var panel = new Ext.Panel({
			text:'用户头像',
			name:'PHOTO',
			width: 200, 
			height: 190,
			items:[{
				width : 160,
				xtype : 'button',
				text : "上传头像",
				handler:function(){
					uploadWin.show();
				}
			},{
				xtype: 'box',id: 'image',width: 160, height: 160,
				autoEl: {
					tag: 'img',
               		src:''
            	}
			}]
		});
		return [panel];
	}
}];
		
var formViewers = [{
	columnCount : 3,
	fields : ['ID','ACCOUNT_NAME','USER_NAME','ORG_ID','ORG_NAME','DEADLINE','DIR_ID','BIRTHDAY','MOBILEPHONE','OFFICETEL'
		,'EMAIL','SEX','OFFENIP','USER_STATE','APP_ID','LAST_LOGIN_TIME','TEMP1'
		,'USER_CODE','BELONG_BUSI_LINE','BELONG_TEAM_HEAD','EDUCATION','CERTIFICATE','ENTRANTS_DATE','POSITION_TIME','POSITION_DEGREE','FINANCIAL_JOB_TIME','TEMP2'],
	fn : function(ID,ACCOUNT_NAME,USER_NAME,ORG_ID,ORG_NAME,DEADLINE,DIR_ID,BIRTHDAY,MOBILEPHONE,OFFICETEL
		,EMAIL,SEX,OFFENIP,USER_STATE,APP_ID,LAST_LOGIN_TIME,TEMP1
		,USER_CODE,BELONG_BUSI_LINE,BELONG_TEAM_HEAD,EDUCATION,CERTIFICATE,ENTRANTS_DATE,POSITION_TIME,POSITION_DEGREE,FINANCIAL_JOB_TIME,TEMP2){
		ACCOUNT_NAME.allowBlank = true;
		ACCOUNT_NAME.readOnly = true;
		ACCOUNT_NAME.cls = "x-readOnly";
		USER_STATE.hidden=true;
		APP_ID.value='62';
		DIR_ID.hidden = false;
		return [ID,ACCOUNT_NAME,USER_NAME,ORG_ID,ORG_NAME,SEX,BIRTHDAY,DIR_ID,USER_CODE,DEADLINE,MOBILEPHONE,OFFICETEL
			,EMAIL,OFFENIP,USER_STATE,APP_ID,LAST_LOGIN_TIME
			,BELONG_BUSI_LINE,BELONG_TEAM_HEAD,TEMP1,EDUCATION,CERTIFICATE,ENTRANTS_DATE,POSITION_TIME,POSITION_DEGREE,FINANCIAL_JOB_TIME,TEMP2];
	}
},{
	columnCount : 1,
	fields : ['ID'],
	fn : function(ID){
		var panel = new Ext.Panel({
			text:'用户头像',
			name:'PHOTO',
			width: 200, 
			height: 190,
			items:[{
				width : 160,
				xtype : 'button',
				padding : '0 0 10px 0',
				text : "上传头像",
				handler:function(){
					uploadWin.show();
				}
			},{
				xtype: 'box',id: 'image1',width: 160, height: 160,
				autoEl: {
					tag: 'img',
               		src:''
            	}
			}]
		});
		return [panel];
	}
}];

var createValidates = [{
	desc : '密码认证策略校验',
	dataFields : ['PASSWORD','PASSWORD1'],
	/**
	 * 密码认证策略校验
	 * @param {} PASSWORD  密码
	 * @param {} PASSWORD1 确认密码
	 * @return {Boolean}
	 */
	fn : function(PASSWORD,PASSWORD1){
		if(PASSWORD != PASSWORD1){
			Ext.debug('确认密码与所属密码不一致！');
			return false;
		}
		/**重置密码无需校验密码修改策略*/
//		var obj = _PwdValid.pwdValidate(PASSWORD);
//		if(!obj.valid){
//			Ext.debug(obj.errorMsg);
//			return false;
//		}
	}
	
}];

//结果域扩展功能面板
var customerView = [{
	title : '用户角色配置',
	type : 'grid',
	url : basepath + '/roleInfoQuery.json',
	pageable : false,
	isCsm : false,
	currentParams :{ //查询用户对应层级的角色
		roleLevel: __unitlevel
	},
	frame : true,
	buttonAlign:'center',
	fields: {
		fields : [
			{name: 'ID',hidden: true},
			{name: 'ROLE_LEVEL',hidden: true,text: '角色层级',resutlWidth:100},
			{name: 'ROLE_NAME',text: '角色名称',width:240},
			{name: 'IS_CHECKED',text: '角色授权',width:80,align:'center',
				renderer : function(value, record, e) {
	                var checked = (value == '1') ? 'checked': '';
	                var checkBox = '<input id=' + e.id + '_check type="checkbox" ' + checked + ' />';
	                return checkBox;
	            }
	        }
		]
	},
	gridButtons:[{
		/**
		 * 提交角色配置
		 */
		text : '保存',
		fn : function(grid){
        	var accountId = tempPid; //设置要配置角色授权的用户
        	if(getSelectedData()){
        		accountId=getSelectedData().data.ID;
        	}
            var addArray = [];
            var deleteArray = [];
            for (var j = 0; j < grid.store.data.items.length; j++) {
                var firstArray = {};
                var secondArray = {};
                var k = grid.store.data.keys;
                var one = document.getElementById(k[j] + "_check");
                var children = grid.store.data.items[j].json;
                if (one.checked == true) {
                    if (children.IS_CHECKED == '0') {
                        firstArray.accountId = accountId;
                        firstArray.roleId = children.ID;
                        firstArray.appId = '62';
                        addArray.push(firstArray);
                    }
                } else {
                    if (children.IS_CHECKED == '1') {
                        secondArray.accountId = accountId;
                        secondArray.roleId = children.ID;
                        secondArray.id = children.check;
                        deleteArray.push(secondArray);
                    }
                }
            }
            Ext.Ajax.request({
                url: basepath + '/userManagerForAuth.json',
                method: 'POST',
                params: {
                    'addArray': Ext.encode(addArray),
                    'deleteArray': Ext.encode(deleteArray)
                },
                success: function(response) {
                    reloadCurrentData();
                    Ext.Msg.alert('提示', '保存成功');
                },
                failure: function(response) {
                    Ext.Msg.alert('提示', '保存失败');
                }
            });
		}
	}]
},{
	/**
	 * 自定义重置密码面板
	 */
	title:'重置密码',
	type: 'form',
	groups:[{
		columnCount : 1,
		fields : ['ID','ACCOUNT_NAME','USER_NAME',
			{name: 'PASSWORD',text:'密码', resutlWidth:80,inputType: 'password'},
			{name: 'PASSWORD1',text:'确认密码', resutlWidth:80,inputType: 'password'}
		],
		/**
		 *重置密码面板字段初始化处理
		 */
		fn : function(ID,ACCOUNT_NAME,USER_NAME,PASSWORD,PASSWORD1){
			ACCOUNT_NAME.allowBlank = true;
			USER_NAME.allowBlank = true;
			ACCOUNT_NAME.cls = "x-readOnly";
			ACCOUNT_NAME.readOnly = true;
			USER_NAME.cls = "x-readOnly";
			USER_NAME.readOnly = true;
			
			PASSWORD.allowBlank=false;
			PASSWORD1.allowBlank=false;
			PASSWORD.text='密码<font color="red">*</font>';
			PASSWORD1.text='确认密码<font color="red">*</font>';
			return [ID,ACCOUNT_NAME,USER_NAME,PASSWORD,PASSWORD1];
		}
	}],
	formButtons:[{
		/**
		 * 重置密码-保存按钮
		 */
		text : '保存',
		fn : function(contentPanel, baseform){
			var PASSWORD = baseform.findField('PASSWORD').getValue();
			var PASSWORD1 = baseform.findField('PASSWORD1').getValue();
			if(PASSWORD != PASSWORD1){
				Ext.Msg.alert('提示', '两次输入密码不一致');
				baseform.findField('PASSWORD1').setValue('');
                return false;
			}
			/**重置密码无需校验密码修改策略*/
//			var obj = _PwdValid.pwdValidate(PASSWORD);
//			if(!obj.valid){
//				Ext.Msg.alert('提示', obj.errorMsg);
//				baseform.findField('PASSWORD').setValue('');
//				baseform.findField('PASSWORD1').setValue('');
//				return false;
//			}
            Ext.Ajax.request({
                url: basepath + '/passwordChangeAction!authPassword.json',
                method: 'POST',
                params: {
                	'id': baseform.findField('ID').getValue(),
                    'userId': baseform.findField('ACCOUNT_NAME').getValue(),
                    'updateUser': JsContext._userId,
                    'authEnableFlag': _PwdValid.state6,//历史密码重复校验策略
                    'historyPw': _PwdValid.historyPw,//历史密码重复校验策略参数
                    'password': PASSWORD,
                    'oldPassword2': ''
                },
                success: function(response) {
                    Ext.Msg.alert('提示', '重置密码成功！');
                    reloadCurrentData();
                },
                failure: function(){
                	Ext.Msg.alert('提示', '重置密码失败！');
                }
            });
                    
		}
	}]
}];

//边缘面板配置
var edgeVies = {
	left : {
		width : 200,
		layout : 'form',
		items : [TreeManager.createTree('DATASETMANAGERTREE')]
	}
};

/**
 * 数据联动，当数据发生变化，且失去焦点的时候，会调用该联动逻辑
 */
var linkages = {
	ORG_NAME : {
		fields : ['DIR_ID','ORG_ID'],
		fn : function(ORG_NAME,DIR_ID,ORG_ID){
			DIR_ID.store.load( {
				params : {
					belongOrgId : ORG_ID.value
				}
			});
		}
	}
};

/**
 * 自定义工具条上按钮
 * 注：批量选择未实现,目前只支持单条删除、启用、停用
 */
var tbar = [{
	/**
	 * 系统用户删除
	 */
	text : '删除',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
			var selectRecords = getAllSelects();
			var ids = selectRecords[0].data.ID;
			for(var i=1;i<selectRecords.length;i++){
				ids += ',' + selectRecords[i].data.ID;
			}
			Ext.MessageBox.confirm('提示','确定删除吗?',function(buttonId){
				if(buttonId.toLowerCase() == "no"){
					return;
				}  
				Ext.Ajax.request({
					url: basepath + '/userManager!batchDestroy.json',
					waitMsg : '正在保存数据,请等待...', // 显示读盘的动画效果，执行完成后效果消失
	                params: {
                        'idStr': ids
                    },
					success : function() {
                        Ext.Msg.alert('提示', '删除用户成功' );
						reloadCurrentData();
					},
					failure : function() {
						Ext.Msg.alert('提示', '删除用户失败' );
						reloadCurrentData();
					}
				});
			});
		}
	}
},{
	/**
	 * 系统用户状态的停用
	 */
	text : '停用',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
			if(getSelectedData().data.ACCOUNT_NAME=='admin'){
	        	Ext.Msg.alert('提示', 'admin用户不能停用!');
	            return false;
	        }
			Ext.Ajax.request({
				url: basepath + '/userManager!updateState.json',
				method: 'POST',
	            params: {
	               'idStr': getSelectedData().data.ID,
	               'userState': 0
	            },
	            success: function(response) {
	        	   reloadCurrentData();
	               Ext.Msg.alert('提示', '停用用户成功');
	            },
	            failure: function(response) {
	               Ext.Msg.alert('提示', '停用用户失败');
	            }
	        });
		}
	}
},{
	/**
	 * 系统用户状态的启用
	 */
	text : '启用',
	handler : function(){
		if(getSelectedData() == false){
			Ext.Msg.alert('提示','请选择一条数据！');
			return false;
		}else{
	        Ext.Ajax.request({
	            url: basepath + '/userManager!updateState.json',
	            method: 'POST',
	            params: {
	                'idStr': getSelectedData().data.ID,
	                'userState': 1
	            },
	            success: function(response) {
	            	reloadCurrentData();
	                Ext.Msg.alert('提示', '启用用户成功');
	            },
	            failure: function(response) {
	                Ext.Msg.alert('提示', '启用用户失败');
	            }
	        });		
        }
	}
}];

/**
 * 结果域面板滑入前触发,系统提供listener事件方法
 * @param {} view
 * @return {Boolean}
 */
var beforeviewshow = function(view){
	if(view == getEditView()){
		if(getSelectedData()==false || getAllSelects().length > 1){
			Ext.Msg.alert('提示','请选择一条数据!');
			return false;
		}
	}
	//初始化密码认证策略
	_PwdValid.initTactics();
	if(view.baseType == 'createView'){
		Ext.getCmp('image').getEl().dom.src = '';
	}
	if(view.baseType != 'createView'){
		if(view.baseType == 'editView'){
			if(getSelectedData().data.TEMP1 != ''){
				Ext.getCmp('image1').getEl().dom.src = basepath+ '/imgshow.json?path='+getSelectedData().data.TEMP1;
			}else{
				Ext.getCmp('image1').getEl().dom.src = '';
			}
			
		}
		if(view._defaultTitle == '用户角色配置'){
			if(getSelectedData()){
				view.setParameters({
					'accountId': getSelectedData().data.ID,
					roleLevel: __unitlevel
				});
			    return true;
			}else if(tempPid!=''&&accountName!=''){
                //注：当tempPid与accountName都不为空时，认为是新增后直接进入用户角色配置面板,并重置下列变量
				view.setParameters({
					'accountId': tempPid,
					roleLevel: __unitlevel
				});
				tempPid=='';
				accountName=='';
				return true;
			}else{
				Ext.Msg.alert('提示','请选择一条数据进行操作！');
				return false;
			}
		}else if(!getSelectedData()){ //注：beforeviewshow事件不包含进入列表，因此可以此调用
			Ext.Msg.alert('提示','请选择一条数据进行操作！');
			return false;
		}
	}
};

/**
 * 结果域面板滑入后触发,系统提供listener事件方法
 */
var viewshow = function(theview){
	if(theview._defaultTitle == '重置密码'){
		if(getSelectedData()){
			var tempData = getSelectedData().data;
			theview.contentPanel.getForm().reset();
			theview.contentPanel.getForm().findField('ID').setValue(tempData.id);
			theview.contentPanel.getForm().findField('ACCOUNT_NAME').setValue(tempData.ACCOUNT_NAME);
			theview.contentPanel.getForm().findField('USER_NAME').setValue(tempData.USER_NAME);
		}
	}
};

//新增数据获取的临时账号id及账号登录名
var tempPid = '';
var accountName = '';
/**
 * 数据提交之后触发,系统提供listener事件方法
 * @param {} data
 * @param {} cUrl
 * @param {} result
 */
var afertcommit = function(data, cUrl, result){
	var tempView = getCurrentView();
	if(tempView.baseType == 'createView'){
		Ext.Ajax.request({
			url : cUrl.split('!')[0]+'!getPid.json',
			method : 'GET',
			success : function(response) {
				var nodeArra = Ext.util.JSON.decode(response.responseText);
				accountName = data.ACCOUNT_NAME;
				tempPid = nodeArra.pid;
				lockGrid();
				showCustomerViewByTitle('用户角色配置');
			}
		});
	}
};