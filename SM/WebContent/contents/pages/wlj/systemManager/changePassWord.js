/**
 * 个人密码修改NEW
 * @author helin,
 * @since 2014-6-25
 */
Ext.onReady(function() {
	//初始化密码认证策略
	_PwdValid.initTactics();
    var updateForm = new Ext.form.FormPanel({
        title:'修改个人密码',
        frame: true,
        buttonAlign:'center',
        items:[{
            layout:'column',
            items:[{
                columnWidth:1,
                layout:'form',
                padding:'10px 0px 0px 0px',
                items:[
		            {xtype:'textfield',name: 'OLDPASSWORD',fieldLabel:'<font color="red">*</font>原密码',inputType: 'password',width:220,labelStyle:'text-align:right;',allowBlank:false},
					{xtype:'textfield',name: 'PASSWORD',fieldLabel:'<font color="red">*</font>密码',inputType: 'password',width:220,labelStyle:'text-align:right;',allowBlank:false},
					{xtype:'textfield',name: 'PASSWORD1',fieldLabel:'<font color="red">*</font>确认密码',inputType: 'password',width:220,labelStyle:'text-align:right;',allowBlank:false}
                ]
            },{
                columnWidth:1,
                layout:'form',
                padding:'15px 140px 0px',
                items:[
					{xtype:'button',text:'修改',width:80,handler:function(){
							if(!updateForm.getForm().isValid()){ 
								Ext.Msg.alert('提示','请输入必输项');
								return false;
							}
							var OLDPASSWORD = updateForm.getForm().findField('OLDPASSWORD').getValue();
							var PASSWORD = updateForm.getForm().findField('PASSWORD').getValue();
							var PASSWORD1 = updateForm.getForm().findField('PASSWORD1').getValue();
							if(PASSWORD != PASSWORD1){
								Ext.Msg.alert('提示', '两次输入密码不一致');
								updateForm.getForm().findField('PASSWORD1').setValue('');
				                return false;
							}
							var obj = _PwdValid.pwdValidate(PASSWORD);
							if(!obj.valid){
								Ext.Msg.alert('提示', obj.errorMsg);
								updateForm.getForm().findField('PASSWORD').setValue('');
								updateForm.getForm().findField('PASSWORD1').setValue('');
								return false;
							}
				            Ext.Ajax.request({
				                url: basepath + '/passwordChangeAction!authPassword.json',
				                method: 'POST',
				                params: {
				                    'userId': JsContext._userId,
				                    'updateUser': JsContext._userId,
				                    'authEnableFlag': _PwdValid.state6,//历史密码重复校验策略
				                    'historyPw': _PwdValid.historyPw,//历史密码重复校验策略参数
				                    'password': PASSWORD,
				                    'oldPassword':OLDPASSWORD,
				                    'oldPassword2': '1'
				                },
				                success: function(response) {
				                    if(parent._APP.securityBooter.secMainWindow
				                		&& parent._APP.securityBooter.secMainWindow.isVisible()){
				                		parent.__updatePwdStat = "1";
				                		parent._APP.securityBooter.secMainWindow.hide();
				                		parent.Ext.Msg.alert('提示', '密码修改成功！');
				                	}else{
				                		Ext.Msg.alert('提示', '密码修改成功！');
				                	}
				                    updateForm.getForm().reset();
				                },
				                failure: function(){
				                	Ext.Msg.alert('提示', '密码修改失败！');
				                	updateForm.getForm().reset();
				                }
				            });
						}
					}
                ]
            }]
        }]
    });
    var mainView = new Ext.Viewport({
		title : '修改个人密码',
		closable : true,
		plain : true,
		resizable : false,
		collapsible : false,
		height:400,
		width:400,
		draggable : false,
		closeAction : 'hide',
		modal : true, // 模态窗口 
		border : false,
		autoScroll : true,
		closable : true,
		animateTarget : Ext.getBody(),
		constrain : true,
		layout:'fit',
		items:[updateForm]
	});
});