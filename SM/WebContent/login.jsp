<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.springframework.security.web.WebAttributes"%>
<%@ page import="com.yuchengtech.bob.core.CrmLisenceManager"%>
<%
	String currentTime = ""+System.currentTimeMillis();	
	String versionInfo = "Copyright &copy; "
						+ "横琴金融局 2015";//CrmLisenceManager.getInstance().getCustomerName();
						//+ " Version："
						//+ CrmLisenceManager.getInstance().getVersion();
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>横琴金融局</title>
	<meta name="keywords" content="YC.PMS" />
	<meta name="description" content="YC.PMS" />
	<meta name="Author" content="YuchengTech" />
	<link rel="shortcut icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="contents/wljFrontFrame/styles/search/searchthemes/blue/login.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/commonjs/controlCookie.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/commonjs/jquery-1.5.2.min.js"></script>
	<!--[if lt IE 9]>
	<script type="text/javascript" src="<%=request.getContextPath()%>/contents/commonjs/PIE_IE678.js"></script>
	<![endif]-->		
	<script>
		window.document.onkeydown = function(e){
			e = !e ? window.event : e;
			var key = window.event ? e.keyCode:e.which;
			if(13==key){//监视是否按下'Enter'键
				  setFromValue();
			  }
		}
		 
		var currentTime = <%=currentTime%>;
		function setTarget(){
			var target = 'chat<%=currentTime%>';
			document.getElementById('formLogin').target = target;
			//closeWindow();
		}
	
		//
		function check() {
			if (document.getElementById("username").value.length == 0) {
				alert("用户名为空，请输入");
				document.getElementById("username").focus();
				document.getElementById('submitBtn').disabled = false;
				return false;
			}
	
			if (document.getElementById("password").value.length == 0) {
				alert("密码为空，请输入");
				document.getElementById("password").focus();
				document.getElementById('submitBtn').disabled = false;
				return false;
			}
			return true;
		}
	
		function setFromValue() {
			document.getElementById('submitBtn').disabled = true;
			document.getElementById('j_username').value = document
					.getElementById("username").value;
			document.getElementById('j_password').value = document
					.getElementById("password").value;
			var tempValue = document.getElementById("username").value;
			setCookie("CRM_USER_ID1", "", 30);
			setCookie("CRM_USER_ID1", tempValue, 30);
			document.getElementById('submit').click();
		}
		//从Cookie获取用户登录ID
		function getUserId() {
			var userid = getCookie("CRM_USER_ID1");
			if (userid != null && userid != "") {
				document.getElementById("username").value = userid;
				document.getElementById("password").focus();
			} else
				document.getElementById("username").focus();
		}
	
		function showErrorMsg() {
			document.getElementById('submitBtn').disabled = false;
			var msg = document.getElementById("errorMsg").value;
			//closeWindow();
			if (msg != '' && msg != 'undefined') {
				alert(msg);
				document.getElementById('errorMsg').value = '';
				document.getElementById("password").focus();
			}
		}
	
		function closeWindow() {
			if (window.opener != null) {
				window.focus();
				window.opener.windowClose();
				window.opener = null;
			}
		}	
	
		$(function() {
			setLoginUi();
		});
		function setLoginUi(){

			if (window.PIE) {
		        $("#loginContainer").each(function() {
		            PIE.attach(this);
		        });
		    }
			$("#username,#password").each(function() {
				$(this).hover(function() {
					$(this).parent("li").addClass("fulCheck");
				}, function() {
					if (!$(this).is(":focus")) {
						$(this).parent("li").removeClass("fulCheck");
					}
				});
				$(this).focus(function() {
					$(this).parent("li").addClass("fulCheck");
				});
				$(this).blur(function() {
					$(this).parent("li").removeClass("fulCheck");
				});
			});
			$("#loginContainer,#versionInfo").fadeIn(
				1000,
				function(){
					 $("").fadeIn();
					 }
			);
		};
	</script>
</head>
<body onload="getUserId();showErrorMsg();">
		<div id="loginContainer">
			<div id="logo">横琴金融局</div>
			<div id="loginIco"></div>
			<div id="formContainer">
				<ul>
					<li><input type="text" id="username" /></li>
					<li><input type="password" id="password" /></li>
					<li><a id="submitBtn" href="#" onclick="setFromValue();" />登 录</a>
				</ul>
			</div>			
		</div>
		<div id="versionInfo"><%=versionInfo%></div>
		<form id="formLogin"
			action="<%=request.getContextPath()%>/j_spring_security_check"
			method="post">
			<input type="hidden" value="" id="j_username" name="j_username" />
			<input type="hidden" value="" id="j_password" name="j_password" />
			<input type="submit" class="button" id="submit" value="登录"
				style="visibility: hidden;" onclick="if(!check()) return false; " />
			<%
				if (session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) {
			%>
			<input type="hidden" id="errorMsg" name="errorMsg"
				value='${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}' />
			<%
				} else {
			%>
			<input type="hidden" id="errorMsg" name="errorMsg" value='' />
			<%
				}
			%>
	
		</form>
</body>
</html>
