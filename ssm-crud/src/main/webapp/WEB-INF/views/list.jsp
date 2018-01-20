<%@ page language="java"  contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" content="text/html;charset=utf-8">
<title>员工列表</title>
<% pageContext.setAttribute("APP_PATH", request.getContextPath()); %> 
<!-- 
 不以/开头的相对路径，找资源，以当前资源的路径为主，经常容易出问题。
 以/开头的相对路径，找资源，以服务器的路径为主(http://localhost:8090/ssm-crud),需要加上项目
 -->
	<script type="text/javascript" src="${APP_PATH }/static/js/jquery-1.8.3.js"></script>
	 <link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" >
	<script src="${APP_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>	
</head>
<body>
	<div>
		<div class="container">
			<div class="row">
				<div class="col-xs-12" style="color: greep" ><h1 >项目名称：云霞商城</h1></div>
			</div>
			<div class="row">
				<div class="col-md-4 col-md-offset-8">
					<button type="button" class="btn btn-primary">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					新增
					</button>
					<button type="button" class="btn btn-danger">
					<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
					删除
					</button>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-hover">
						<tr>
							<th>#</th>
							<th>empNme</th>
							<th>gender</th>
							<th>email</th>
							<th>deptName</th>
							<th>opration</th>
						</tr>
						<c:forEach items="${pageInfo.list }" var="emp">
						<tr>
							<th>${emp.empId}</th>
							<th>${emp.empName}</th>
							<th>${emp.empGender=="M"?"男":"女"}</th>
							<th>${emp.empEmail}</th>
							<th>${emp.department.deptName}</th>
							<th>
								<button class="btn btn-warning btn-sm">
								<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
								修改
								</button>
								<button class="btn btn-info btn-sm">
								<span class="glyphicon glyphicon-qrcode" aria-hidden="true"></span>
								查看
								</button>
							</th>
						</tr>
						
						</c:forEach>
					</table>
				</div>
			</div>
			<div class="row">
			<div class="col-md-6">
				<h4>当前第<span style="color: red">${pageInfo.pageNum }</span> 页， 总共<span style="color: red">${pageInfo.pages}</span> 页 </h4>
			</div>
			<div class="col-md-6">
				<nav aria-label="Page navigation">
				  <ul class="pagination" class="pagination pagination-sm">
				  	<li><a href="${APP_PATH }/emps?pageNo=1" aria-label="Previous">首页</a></li>
				  	<c:if test="${pageInfo.hasPreviousPage }">
				    <li>
				      <a href="${APP_PATH }/emps?pageNo=${pageInfo.pageNum-1 }" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				      </a>
				    </li>
				  	</c:if>
				    <c:forEach items="${pageInfo.navigatepageNums }" var="page_Num">
				    <c:if test="${pageInfo.pageNum==page_Num }">
				    <li class="active"><a href="#">${page_Num }</a></li>
				    </c:if>
				    <c:if test="${pageInfo.pageNum !=page_Num }">
				    <li><a href="${APP_PATH }/emps?pageNo=${page_Num}">${page_Num }</a></li>
				    </c:if>
				    </c:forEach>
				    <c:if test="${pageInfo.hasNextPage }">
				    <li>
				      <a href="${APP_PATH }/emps?pageNo=${pageInfo.pageNum+1 }" aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				      </a>
				    </li>
				    </c:if>
				    <li><a href="${APP_PATH }/emps?pageNo=${pageInfo.pages}" aria-label="Previous">末页</a></li>
				  </ul>
				</nav>
			</div>
			</div>
		</div>	
	</div> 
</body>
</html>