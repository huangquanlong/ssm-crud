<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
	<title>客户关系管理系统</title>
	<meta name="keywords" content="客户关系管理系统,CRM" />
	<meta name="description" content="客户关系管理系统,CRM" />
	<meta name="Author" content="YuchengTech" />
	<link rel="shortcut icon" href="favicon.ico" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/contents/wljFrontFrame/frontIncludes.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/contents/wljFrontFrame/styles/search/searchcss/common.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/contents/wljFrontFrame/styles/search/searchcss/base_frame.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/contents/wljFrontFrame/styles/search/searchthemes/theme1/css/themes_frame.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/wljFrontFrame/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/Wlj-frame-base.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/frameControllers/Wlj-widgets.js"></script>
</head>
<body class="main_bg">
<div class="menu_menu_con">
	<div class="main_menu_div">
        <ul class="main_menu_ul">
        	<li id="BACK" style="display:none;">
            	<a class="lv1" href="javascript:void(0)">
            		<i class="icon0"></i>
                    <i class="word" title="返回">返回</i>
            	</a>
            </li>
        	<li id="MAIN">
            	<a class="lv1" href="javascript:void(0)">
            		<i class="icon1"></i>
                    <i class="word" title="主页">主页</i>
            	</a>
            </li>
            <li id="MENU">
            	<a class="lv1" href="javascript:void(0)">
            		<i class="icon2"></i>
                    <i class="word">菜单</i>
            	</a>   
                <div class="lv2_div">
                  <ul class="lv2_ul">
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon01.png" width="16" height="16" /></i>客户管理<i class="arr"></i></i>              
                          </a>
                          <div class="lv_div">
                            <ul class="lv_ul">
                                <li>
                                    <a class="lv" href="javascript:void(0)">              
                                        <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_folder.png" width="16" height="16" /></i>客户查询</i><i class="arr"></i>              
                                    </a>
                                    <div class="lv_div">
                                        <ul class="lv_ul">
                                            <li>
                                                <a class="lv" href="javascript:void(0)">              
                                                    <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>所辖客户查询</i>              
                                                </a>
                                            </li>
                                   			<li>
                                                <a class="lv" href="javascript:void(0)">              
                                                    <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>我关注的客户</i>              
                                                </a>
                                            </li>
                                            <li>
                                                <a class="lv" href="javascript:void(0)">              
                                                    <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>轻度即席查询</i>              
                                                </a>
                                            </li>
                                            <li>
                                                <a class="lv" href="javascript:void(0)">              
                                                    <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>客户细分查询</i>              
                                                </a>
                                            </li>
                                        </ul>
                                     </div>
                                </li>
                       			<li>
                                    <a class="lv" href="javascript:void(0)">              
                                        <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>潜在客户管理</i>              
                                    </a>
                                </li>
                               <li>
                                    <a class="lv" href="javascript:void(0)">              
                                        <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_folder.png" width="16" height="16" /></i>客户归属管理</i><i class="arr"></i>              
                                    </a>                                    
                                </li>
                                <li>
                                    <a class="lv" href="javascript:void(0)">              
                                        <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>客户评价管理</i>              
                                    </a>
                                </li>
                                <li>
                                    <a class="lv" href="javascript:void(0)">              
                                        <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>客户群管理</i>              
                                    </a>
                                </li>
                                <li>
                                    <a class="lv" href="javascript:void(0)">              
                                        <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>客户贵宾卡管理</i>              
                                    </a>
                                </li>
                                <li>
                                    <a class="lv" href="javascript:void(0)">              
                                        <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon_fun.png" width="16" height="16" /></i>客户信息管理</i>              
                                    </a>
                                </li>
                            </ul>
                          </div>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon02.png" width="16" height="16" /></i>客户经理管理<i class="arr"></i></i>              
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon03.png" width="16" height="16" /></i>积分管理<i class="arr"></i></i>              
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon04.png" width="16" height="16" /></i>工作平台<i class="arr"></i></i>              
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">             
                              <i class="word"> <i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon05.png" width="16" height="16" /></i>营销管理<i class="arr"></i></i>              
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon06.png" width="16" height="16" /></i>产品管理<i class="arr"></i></i>              
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon07.png" width="16" height="16" /></i>绩效考核<i class="arr"></i></i>             
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon08.png" width="16" height="16" /></i>服务管理<i class="arr"></i></i>              
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon09.png" width="16" height="16" /></i>统计分析<i class="arr"></i></i>              
                          </a>
                      </li>
                      <li>
                          <a class="lv2 arr" href="javascript:void(0)">              
                              <i class="word"><i class="ico"><img src="styles/search/searchthemes/theme1/pics/menu_icon10.png" width="16" height="16" /></i>系统管理<i class="arr"></i></i>              
                          </a>
                      </li>
                  </ul>
                </div>             
            </li>
            <li>
            	<a class="lv1" href="javascript:void(0)">
            		<i class="icon3"></i>
                    <i class="word" title="配置">配置</i>
            	</a>
            </li>
            <li id="SwitchBG">
            	<a class="lv1" href="javascript:void(0)">
            		<i class="icon4"></i>
                    <i class="word" title="个性化">个性化</i>
            	</a>
            </li>
            <li>
            	<a class="lv1" href="sheet_index.jsp">
            		<i class="icon5"></i>
                    <i class="word" title="模式">模式</i>
            	</a>
            </li>
        </ul>
    </div>
    <div class="user_frame">    
    	<div class="user_head">
  		 <div class="user_head_pic"><img src="styles/search/searchpics/user_head.jpg" width="45" height="45" /></div>
		</div>	
        <div class="user_info">
        	<p class="user_info_p">你好，<i class="name">杨姗</i>，欢迎登录客户关系管理系统！</p>
            <p class="user_info_p blue"><span class="tit">岗位：</span><span>客户经理</span></p>
            <p class="user_info_p blue"><span class="tit">机构：</span><span>方山支行</span></p>
        </div>
        <div class="user_oper">
        	<p><a class="loginout" href="<%=request.getContextPath()%>"></a></p>
        </div>
    </div>
</div>
<div class="main_menu">	
    <!--这里是png透明背景-->
</div>
<div id="LayoutSearch" class="layout layout_search" style="display:none">
	<div class="search">
            	<div class="search_fun">
                	<ul class="search_ul">
                    	<li class="selected">客户</li>
                        <li>产品</li>
                        <li>功能</li>
                    </ul>
                </div>
                <div class="search_inner">
                	<div class="search_input">
                    	<input name="" type="text" />
                    </div>
                    <div class="search_bt">
                    
                    </div>
                </div>
    </div>
    <div class="layout_fun">    	
    	<div class="layout_fun_pos">     
            <div class="tile base tile_c7">
                <div class="tile_fun">
            	<div class="tile_fun_pic"><img src="styles/search/searchpics/fun3.png" width="60" height="60" /></div>
                <div class="tile_fun_name">
               	  <p title="短信平台"><i>历史记录</i></p>
                </div>                
           		</div>
            </div>   	 
             <div class="tile w190h1 tile_c8">
             	<div class="tile_fun">
            	<div class="tile_fun_pic"><img src="styles/search/searchpics/fun4.png" width="60" height="60" /></div>
                <div class="tile_fun_name">
               	  <p title="短信平台"><i>经营统计分析</i></p>
                </div>                
           		</div>
             </div>
             <div class="tile w190h1 tile_c8">
             	<div class="tile_fun">
            	<div class="tile_fun_pic"><img src="styles/search/searchpics/fun1.png" width="60" height="60" /></div>
                <div class="tile_fun_name">
               	  <p title="短信平台"><i>所辖客户查询</i></p>
                </div>                
           		</div>
             </div>
			 <div class="tile w190h1 tile_c8">
             	<div class="tile_fun">
            	<div class="tile_fun_pic"><img src="styles/search/searchpics/fun5.png" width="60" height="60" /></div>
                <div class="tile_fun_name">
               	  <p title="短信平台"><i>公共参数设置</i></p>
                </div>                
           		</div>
             </div>
			 <div class="tile w190h1 tile_c8">
             	<div class="tile_fun">
            	<div class="tile_fun_pic"><img src="styles/search/searchpics/fun2.png" width="60" height="60" /></div>
                <div class="tile_fun_name">
               	  <p title="短信平台"><i>短信平台</i></p>
                </div>                
           		</div>
             </div>
             <div class="tile base">
                 
             </div>
             <div class="tile w190h1 tile_c8 tile_alpha">
             
             </div>
             <div class="tile w190h1 tile_c8 tile_alpha">
             
             </div>
             <div class="tile w190h1 tile_c8 tile_alpha">
             
             </div>
             <div class="tile w190h1 tile_c8 tile_alpha">
             
             </div>
        </div>
    </div>
</div>
<div id="LayoutTile" class="layout layout_tile">
 <div class="layout_contents">
	<div id="group1" class="layout_position">
    	<div class="tile w2h2">
        	<div class="calendar_bg">
            	<div class="calendar_ym">
                	<div class="calendar_ym_left"></div>
                    <p class="calendar_ym_p"><span class="year"><i>2013年</i></span><span class="month"><i>8月</i></span></p>							
                	<div class="calendar_ym_right"></div>
                </div>
            	<div class="calendar_tit">
                	<p class="calendar_tit_p"><span class="sun"><i>日</i></span><span class="mon"><i>一</i></span><span class="tue"><i>二</i></span><span class="wed"><i>三</i></span><span class="thurs"><i>四</i></span><span class="fri"><i>五</i></span><span class="sat"><i>六</i></span></p>
                </div>
            	<div class="calendar_date">
                	<p class="calendar_date_p"><span class="other"><i>28</i></span><span class="other"><i>29</i></span><span class="other"><i>30</i></span><span class="other"><i>31</i></span><span><i>1</i></span><span><i>2</i></span><span><i>3</i></span></p>
                    <p class="calendar_date_p"><span><i>4</i></span><span><i>5</i></span><span><i>6</i></span><span><i>7</i></span><span><i>8</i></span><span><i>9</i></span><span><i>10</i></span></p>
                    <p class="calendar_date_p"><span><i>11</i></span><span><i>12</i></span><span><i>13</i></span><span><i>14</i></span><span><i>15</i></span><span><i>16</i></span><span><i>17</i></span></p>
                    <p class="calendar_date_p"><span><i>18</i></span><span><i>19</i></span><span><i>20</i></span><span><i>21</i></span><span><i>22</i></span><span><i>23</i></span><span><i>24</i></span></p>
                    <p class="calendar_date_p"><span><i>25</i></span><span><i>26</i></span><span><i>27</i></span><span><i>28</i></span><span><i>29</i></span><span><i>30</i></span><span class="curr"><i>31</i></span></p>
                </div>
            </div>
             <div class="rc_list">
                    	<div class="rc_div"><p class="rc_p"><a href="javascript:void(0)" title="【会议】业务知识培训">【会议】业务知识培训</a></p></div>
                        <div class="rc_div"><p class="rc_p"><a href="javascript:void(0)" title="【会议】业务知识培训">【会议】业务知识培训</a></p></div>
                        <div class="rc_div"><p class="rc_p"><a href="javascript:void(0)" title="【会议】业务知识培训">【会议】业务知识培训</a></p></div>
                        <div class="rc_div"><p class="rc_p"><a href="javascript:void(0)" title="【会议】业务知识培训">【会议】业务知识培训</a></p></div>
                     
              </div>
        </div>
        <div class="tile w3h1 tile_c1">
        	<div class="tile_search">
            	<div class="tile_search_fun">
                	<ul class="tile_search_ul">
                    	<li class="selected">客户</li>
                        <li>产品</li>
                        <li>功能</li>
                    </ul>
                </div>
                <div class="tile_search_inner">
                	<div class="tile_search_input">
                    	<input name="" type="text" />
                    </div>
                    <div class="tile_search_bt">
                    
                    </div>
                </div>
            </div>
        </div>
        <div class="tile base tile_c2">
        	<div class="tile_fun">
            	<div class="tile_fun_pic"><img src="styles/search/searchpics/fun1.png" width="60" height="60" /></div>
                <div class="tile_fun_name">
               	  <p title="所辖客户查询所辖客户查询所辖客户查询"><i>所辖客户查询所辖客户查询所辖客户查询</i></p>
                </div>
            </div>
        </div>
        <div class="tile w3h1 tile_c3">
        	<div class="tile_tip">
            	<div class="tile_tip_tit">
                	<p class="tit">您当前共有<i class="light">10</i>条提醒</p>
                	<div class="tile_page">
                    	<p><span class="left"></span><span class="curr_num">1</span><span>/</span><span class="total_num">5</span><span class="right"></span></p>
                    </div>
                </div>
                <div class="tile_fun_list">
                	<ul class="tile_fun_ul">
                        <li>
                            <div class="tile_fun_line">
                                <p class="tit" title="大额定期存款到期提醒大额定期存款到期提醒大额定期存款到期提醒"><a href="javascript:void(0)">大额定期存款到期提醒大额定期存款到期提醒大额定期存款到期提醒</a></p>
                                <div class="tile_tipnum">
                                    <p class="tile_tipnum_p"><span><i>0</i></span></p>
                                </div>                      
                            </div>
                        </li>
                        <li>
                            <div class="tile_fun_line">
                                <p class="tit" title="普通贷款到期及应纳利息提醒"><a href="javascript:void(0)">普通贷款到期及应纳利息提醒</a></p>
                                <div class="tile_tipnum">
                                    <p class="tile_tipnum_p"><span><i>0</i></span></p>
                                </div>                      
                            </div>
                        </li>
                        <li>
                            <div class="tile_fun_line">
                                <p class="tit" title="普通贷款到期及应纳利息提醒"><a href="javascript:void(0)">普通贷款到期及应纳利息提醒</a></p>
                                <div class="tile_tipnum">
                                    <p class="tile_tipnum_p"><span><i>0</i></span></p>
                                </div>                      
                            </div>
                        </li>
                        <li>
                            <div class="tile_fun_line">
                                <p class="tit" title="普通贷款到期及应纳利息提醒"><a href="javascript:void(0)">普通贷款到期及应纳利息提醒</a></p>
                                <div class="tile_tipnum">
                                    <p class="tile_tipnum_p"><span><i>0</i></span></p>
                                </div>                      
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="tile base tile_c4">
        	<div class="tile_fun">
            	<div class="tile_fun_pic"><img src="styles/search/searchpics/fun2.png" width="60" height="60" /></div>
                <div class="tile_fun_name">
               	  <p title="短信平台"><i>短信平台</i></p>
                </div>                
            </div>
        </div>
        <div class="tile w2h1 tile_c5">
        	<div class="tile_chart_tit">
            	<p class="tit"><span class="icon">存款余额</span></p>
                <div class="mond">金额（亿元）</div>
            </div>
            <div style="width:100%;height:100%;">
            	component!
            </div>
        </div>
        <div class="tile w2h1 tile_c5">
        	<div class="tile_chart_tit">
            	<p class="tit"><span class="icon">贷款余额</span></p>
                <div class="mond">金额（亿元）</div>
            </div>
        </div>
        <div class="tile w2h1 tile_c5">
        	<div class="tile_chart_tit">
            	<p class="tit"><span class="icon">机构业绩</span></p>
                <div class="mond">金额（亿元）</div>
            </div>
        </div>
    </div>
    <div class="layout_position">
    	<div class="tile w2h2 tile_c6">
        	<div class="tile_tip_tit">
                	<p class="tit"><span class="icon">系统公告</span></p>
                	<div class="tile_page">
                    	<p><span class="left"></span><span class="curr_num">1</span><span>/</span><span class="total_num">5</span><span class="right"></span></p>
                    </div>
            </div>
            <div class="tile_notice_list">
            	<ul class="in_sys_ul">
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划产品计划">三季度重点营销产品计划产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                    <li>
                        <div class="in_sys_line">
                         <p class="in_sys_p"><span class="time">2013-09-01</span><span class="type red"></span><a class="tit" href="javascript:void(0)" title="三季度重点营销产品计划">三季度重点营销产品计划</a></p> 
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="tile w3h1 tile_c1">
        	
        </div>
        <div class="tile base tile_c2">
        	
        </div>
        <div class="tile w3h1 tile_c3">
        	
        </div>
        <div class="tile base tile_c4">
        	
        </div>
        <div class="tile w2h1 tile_c5">
        	<div class="tile_chart_tit">
            	<p class="tit"><span class="icon">贷款趋势</span></p>
                <div class="mond">金额（亿元）</div>
            </div>
        </div>
        <div class="tile w2h1 tile_c5">
        	<div class="tile_chart_tit">
            	<p class="tit"><span class="icon">存款趋势</span></p>
                <div class="mond">金额（亿元）</div>
            </div>
        </div>
        <div class="tile w2h1 tile_c5">
        	<div class="tile_chart_tit">
            	<p class="tit"><span class="icon">机构业绩趋势</span></p>
                <div class="mond">金额（亿元）</div>
            </div>
        </div>
    </div>
 </div>
</div>
<script type="text/javascript">
	var n=1;
	$(function(){
		//计算磁贴大块的高度
		$('#LayoutTile').height($(window).height()-158);
		//主页显示隐藏
		$('#MAIN').bind('click',function(){
			$('#MAIN').hide();
			$('#BACK').show();
			$('#LayoutSearch').fadeIn();
			$('#LayoutTile').fadeOut();			
		});
		$('#BACK').bind('click',function(){
			$('#BACK').hide();
			$('#MAIN').show();	
			$('#LayoutTile').fadeIn();		
			$('#LayoutSearch').hide();
			
		});
		//简易菜单js
		$('.lv2_div').hide();
		$('.lv_div').hide();
		$('#MENU').hover(function(){
			$('.lv2_div').show();
		},
		function(){
			$('.lv2_div').hide();
		});
		$('.lv2_div').hover(function(){
			$(this).find('.lv_div:first').show();
		},
		function(){
			$(this).find('.lv_div:first').hide();
		});
		$('.lv_div').hover(function(){
			$(this).find('.lv_div:first').show();
		},
		function(){
			$(this).find('.lv_div:first').hide();
		});
		//切换背景
		$('#SwitchBG').bind('click',function(){			
			n++;
			if(n>5){
				n=1;			
			}			
			$('.main_bg').css({
				'background':"url(styles/search/searchpics/themes"+n+".jpg) fixed center"
			});			
		});
	});	
	window.onresize=function(){
		$('.layout').height($(window).height()-158);
	}
</script>
</body>
</html>
