<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/contents/wljFrontFrame/frontIncludes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="styles/sheet/sheetcss/common.css"/>
<link rel="stylesheet" type="text/css" href="styles/sheet/sheetcss/base_frame.css"/>
<link rel="stylesheet" type="text/css" href="styles/sheet/sheetthemes/theme1/css/themes_frame.css"/>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>

</head>

<body>
<div class="in_head ">
	<div class="in_top">
    	<div class="in_logo"></div>
        <div class="in_fun">
        	<p class="in_fun_p">
            	<a href="javascript:void(0)" title="我的信息"><img src="styles/sheet/sheetthemes/theme1/pics/icon01.gif" width="27" height="28" /></a>
            	<a href="javascript:void(0)" title="我的工作台"><img src="styles/sheet/sheetthemes/theme1/pics/icon02.gif" width="27" height="28" /></a>
            	<a href="javascript:void(0)" title="常用功能"><img src="styles/sheet/sheetthemes/theme1/pics/icon03.gif" width="27" height="28" /></a>
            	<a href="javascript:void(0)" title="个性化"><img src="styles/sheet/sheetthemes/theme1/pics/icon04.gif" width="27" height="28" /></a>
                <a href="search_index.jsp" title="切换模式"><img src="styles/sheet/sheetthemes/theme1/pics/icon05.gif" width="27" height="28" /></a>
                <a href="<%=request.getContextPath()%>" title="安全退出"><img src="styles/sheet/sheetthemes/theme1/pics/icon06.gif" width="27" height="28" /></a>
            </p>
        </div>
    </div>
    <div class="in_menu">
    	<ul class="in_menu_ul">
        	<li class="lv1"><a href="javascript:void(0)"><i>首页</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>客户管理</i></a>
            	<div class="menu_lv2" style="display:none">                 	
                	<div class="menu_lv2_div">
                    <div class="menu_lv2_left"><a href="javascript:void(0)"></a></div>                       	
                        <div class="menu_lv2_con">         
                            <ul class="menu_lv2_ul">
                                <li class="lv2"><a href="javascript:void(0)"><i>客户查询</i></a>                              	
                                    <ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>所辖客户查询</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>我关注的客户</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>轻度即席查询</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户细分查询</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>潜在客户管理</i></a></li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户归属管理</i></a>
                                	<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>客户合并</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户分配</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户认领</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户移交</i></a></li>
                                        <li class="lv3 has"><a href="javascript:void(0)"><i>客户托管<img src="styles/sheet/sheetthemes/theme1/pics/menu_arr.gif" width="16" height="6" /></i></a>
                                        	<ul class="lv_sub">
                                        		<li class="lv4"><a href="javascript:void(0)"><i>查询</i></a></li>
                                            	<li class="lv4"><a href="javascript:void(0)"><i>设置</i></a></li>
                                                <li class="lv4"><a href="javascript:void(0)"><i>审批</i></a></li>
                                                <li class="lv4"><a href="javascript:void(0)"><i>日志</i></a></li>
                                            </ul>
                                        </li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>维护人业务统计量</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户评价管理</i></a>
                                	<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>客户利润贡献度</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户评价管理</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户群管理</i></a>
                                	<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>客户群组管理</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>决策支持维度管理</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户关系网络管理</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>家庭群组</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户贵宾卡管理</i></a>
                               		<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>贵宾卡参数设置</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>贵宾卡申请</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>贵宾卡审批</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>贵宾客户交叉销售统计</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>潜在客户管理</i></a></li>
                            </ul>  
                        </div>
                    <div class="menu_lv2_right"><a href="javascript:void(0)"></a></div>
                    <div class="menu_lv2_mask"></div>
                    </div>                    
                </div>                
            </li>
            <li class="lv1"><a href="javascript:void(0)"><i>客户经理管理</i></a>
            	<div class="menu_lv2" style="display:none">                 	
                	<div class="menu_lv2_div">
                    <div class="menu_lv2_left"><a href="javascript:void(0)"></a></div>                       	
                        <div class="menu_lv2_con">         
                            <ul class="menu_lv2_ul">
                                <li class="lv2"><a href="javascript:void(0)"><i>客户经理认证</i></a></li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户经理查询</i></a></li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户经理业绩管理</i></a>
                                	<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>客户经理账户分配</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>明细查询</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>汇总查询</i></a></li>                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户评价管理</i></a>
                                	<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>客户利润贡献度</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户评价管理</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户群管理</i></a>
                                	<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>客户群组管理</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>决策支持维度管理</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>客户关系网络管理</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>家庭群组</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>客户贵宾卡管理</i></a>
                               		<ul class="menu_lv3_ul">
                                    	<li class="lv3"><a href="javascript:void(0)"><i>贵宾卡参数设置</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>贵宾卡申请</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>贵宾卡审批</i></a></li>
                                        <li class="lv3"><a href="javascript:void(0)"><i>贵宾客户交叉销售统计</i></a></li>
                                    </ul>
                                </li>
                                <li class="lv2"><a href="javascript:void(0)"><i>潜在客户管理</i></a></li>
                            </ul>  
                        </div>
                    <div class="menu_lv2_right"><a href="javascript:void(0)"></a></div>
                    <div class="menu_lv2_mask"></div>
                    </div>                    
                </div>
            </li>
            <li class="lv1"><a href="javascript:void(0)"><i>积分管理</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>工作平台</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>营销管理</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>产品管理</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>绩效考核</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>服务管理</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>统计分析</i></a></li>
            <li class="lv1"><a href="javascript:void(0)"><i>系统管理</i></a></li>
        </ul>
    </div>
</div>
<div class="in_tab">
	<div class="in_tab_div">
    	<ul class="in_tab_ul">
        	<li class="curr"><a href="javascript:void(0)"><i>日常工作</i></a></li>
            <li><a href="javascript:void(0)"><i title="业绩统计业绩统计">业绩统计业绩统计</i><span class="close"></span></a></li>
        </ul>
    </div>
</div>
<div class="in_contents">
<iframe id="tabframe" name="tabframe" allowtransparency="true" style="display:inline;" class="tab_iframe" frameborder="0" scrolling="auto" hidefocus="true"  src="sheet_main.jsp"></iframe>
</div>
<script type="text/javascript">
	$(function(){
		var sh=$(window).height();
		$('.tab_iframe').height(sh-123);	
	
		$('.in_menu_ul>li').hover(function(){
			$('.in_menu_ul>li').each(function(index, element) {
				$(this).find('.menu_lv2').hide();
			});
				$(this).find('.menu_lv2').show();
		},
		function(){
			$('.in_menu_ul>li').each(function(index, element) {
				$(this).find('.menu_lv2').hide();
			});
		})
	});
	
	window.onresize=function(){
		var sh=$(window).height();
		$('.tab_iframe').height(sh-123);	
	}
</script>
</body>
</html>
