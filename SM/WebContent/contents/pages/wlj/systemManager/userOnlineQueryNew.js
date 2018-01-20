/**
 * @description 在线用户监控
 * @author helin
 * @since 2014-06-25
 */

/**
 * 初始化提示框
 */
Ext.QuickTips.init();

//历史在线用户数
var onlineMax = '0';
//当前在线用户数
var onlineNum = '0';

var url = basepath+'/userOnlineAction!Query.json';

var fields = [
    {name: 'ID',hidden : true},
    {name: 'userId', text : '登录名',resutlWidth: 100},                                   
    {name: 'cname',text:'用户姓名', searchField: true, resutlWidth:150},         
    {name: 'currentIP',text:'登录IP', resutlWidth:150},  
    {name: 'unitId',text:'机构编号', resutlWidth:100},                             
    {name: 'unitName',text:'机构名称', searchField: true, resutlWidth:150}			   				   
];

var createView = false;
var editView = false;
var detailView = false;

Ext.Ajax.request({
	url : basepath + '/userOnlineAction!getOnlineMax.json',
	method : 'GET',
	success : function(response) {
		 var resultInfo = Ext.util.JSON.decode(response.responseText);
		 onlineMax = resultInfo.json.onlineMax;
		 onlineNum = resultInfo.json.onlineNum;
	}
});

var tbar = [{
	/**
	 * 显示当前系统在线用户数
	 */
	text : '最大在线用户数',
	handler : function(){
		Ext.Msg.alert('',"<b>在线用户个数：["+onlineNum+"];最大在线用户数:["+onlineMax+"]</b>");
	}
}];
