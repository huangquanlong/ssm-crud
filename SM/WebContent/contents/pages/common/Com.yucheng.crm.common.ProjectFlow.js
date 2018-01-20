Ext.ns('Com.yucheng.crm.common');
Com.yucheng.crm.common.ProjectFlow = Ext.extend(Ext.Panel, {
	maximize:false,
	projId : '',
	callFrom : '',//调用项目流程图页面的标识：indexpage首页调用流程图
	flowurl : basepath+'/contents/pages/wlj/project/projectFlowChart.jsp',
	flowId : 'projectflow_center',
	initComponent : function(){
		var _this = this;
		if(_this.maximize){
			_this.tools=[{
	        	id:'maximize',
	        	handler:function(e,target,panel){
	            	var maxwin = new Ext.Window({
	            		title: _this.title,
	            		maximized:true,
	            		closable:true,
	            		closeAction : 'close' ,
	            		layout:'fit',
	            		items:[]
	            	});
	            	maxwin.add(new Com.yucheng.crm.common.ProjectFlow({
						projId : _this.projId
					}));
	            	maxwin.show(target);
	        	}
	       	},{
		        id:'close',
		        handler: function(e, target, panel){
		        	try{
		        		var ct = panel.ownerCt;
		        		if(typeof ct.removeThis === 'function'){
		        			ct.removeThis();
		        		}else{
		        			ct.remove(panel,true);
		        		}
		        	}catch(e){
		        	}
		        }
	    	}];
		}
		
		_this.builtflowurl();
		
		Com.yucheng.crm.common.FusionChartPanel.superclass.initComponent.call(_this);
	},
	onRender : function(ct, position){
		Com.yucheng.crm.common.FusionChartPanel.superclass.onRender.call(this, ct, position);
		if(!this.projId){
			this.html = '代码错误，无projId设置';
			return;
		}
	},
	builtflowurl : function(){
		var iframeurl = this.flowurl.split('.jsp')[0]+'.jsp';
		iframeurl = iframeurl.indexOf('?')>=0 ? iframeurl + '&projId='+this.projId : iframeurl + '?projId='+this.projId ;
		iframeurl += '&callFrom='+this.callFrom;
		this.html = '<iframe id="'+this.flowId+'" name="'+this.flowId+'" src="'+iframeurl+'" style="width:100%;height:100%;" frameborder="no"/>'
	}
});