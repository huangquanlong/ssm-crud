Ext.ns('Wlj.view');
/**
 * 视图工厂
 * @class Wlj.view.View
 * @extends Ext.util.Observable
 */
Wlj.view.View = Ext.extend(Ext.util.Observable,{
	viewId: 'view$-$',
	resId : 0,
	viewType: 0, //视图类型：0项目视图,1需求视图
	viewUrl: '',
	constructor: function(config){
        Wlj.view.View.superclass.constructor.call(this, config)
    },
    /**
     * 打开视图方法
     * @param {} viewType 视图类型：0项目视图,1需求视图
     * @param {} projId	项目ID
     * @param {} projNo	项目标识
     * @param {} projName 项目名称
     * @param {} reqId	需求ID
     * @param {} reqNo	需求标识
     * @param {} reqName 需求名称
     */
	openViewWindow : function(viewType,projId,projNo,projName,reqId,reqNo,reqName){
		this.viewType = viewType;
		this.resId = this.viewId + this.viewType +'$-$' + projId+'$-$' + projNo+'$-$' + projName+'$-$' + reqId+'$-$' + reqNo+'$-$' + reqName;		
		_APP.taskBar.openWindow({
			name : Wlj.view.View.VIEW_PRE_NAME[this.viewType] + ((reqName==null || reqName=="undefined" || reqName=='')?projName:reqName),
			action : basepath + Wlj.view.View.VIEW_BASE_URL[this.viewType],
			resId : this.resId,
			id : 'task_'+this.resId,
			serviceObject : false
		});
	},
	/**
     * 打开视图方法
     * @param {} viewType 视图类型：0项目视图,1需求视图
     * @param {} projId	项目ID
     * @param {} projNo	项目标识
     * @param {} projName 项目名称
     * @param {} reqId	需求ID
     * @param {} reqNo	需求标识
     * @param {} reqName 需求名称
     * @param {} menuName 要打开的视图的菜单名称
     * @param {} winName 窗体名称：项目视图，取项目名称；需求视图，取需求名称
     */
	openViewWindowByMenuname : function(viewType,projId,projNo,projName,reqId,reqNo,reqName,menuName,winName){
		this.viewType = viewType;
		this.resId = this.viewId + this.viewType +'$-$' + projId+'$-$' + projNo+'$-$' + projName+'$-$' + reqId+'$-$' + reqNo+'$-$' + reqName+'$-$' + menuName;
		if(winName==null || winName=="undefined" || winName==''){
			winName = ((reqName==null || reqName=="undefined" || reqName=='')?projName:reqName);
		}
		_APP.taskBar.openWindow({
			name : Wlj.view.View.VIEW_PRE_NAME[this.viewType] + winName,
			action : basepath + Wlj.view.View.VIEW_BASE_URL[this.viewType],
			resId : this.resId,
			id : 'task_'+this.resId,
			serviceObject : false
		});
	}
});

Wlj.view.ViewController = new Wlj.view.View();
Wlj.ViewMgr = Wlj.view.ViewController;

Wlj.view.View.VIEW_BASE_URL = [
	'/contents/frameControllers/view/Wlj-projectview-base.jsp'//项目视图JSP路径
   ,'/contents/frameControllers/view/Wlj-reqmentviewdir-base.jsp'//需求视图JSP路径
];
Wlj.view.View.VIEW_PRE_NAME = [
	'项目：',
	'需求：'
];