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
	<script type="text/javascript" src="${APP_PATH }/static/js/jquery-3.2.1.min.js"></script>
	 <link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" >
	<script src="${APP_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>	
</head>
<body>

	<!--update modal -->
	<div class="modal fade" id="empUpdateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" >修改员工信息</h4>
      </div>
      <div class="modal-body">
						<form class="form-horizontal">
						  <div class="form-group">
						    <label for="inputEmail3" class="col-sm-2 control-label">员工姓名</label>
						    <div class="col-sm-10">
								<p class="form-control-static" id="empName_update_static"></p>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="inputPassword3" class="col-sm-2 control-label">员工邮箱</label>
						    <div class="col-sm-10">
						      <input type="text" name="empEmail" class="form-control" id="empEmail_update_input" placeholder="email@qq.com">
						    <span class="help-block"></span>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="inputPassword3" class="col-sm-2 control-label">员工性别</label>
						    <div class="col-sm-10">
						  <label class="radio-inline">
						  <input type="radio" name="empGender" id="empGender_update_label" value="M" checked="checked">男
						</label>
						<label class="radio-inline">
						  <input type="radio" name="empGender" id="empGender_update_label2" value="F"> 女
						</label>
						    </div>
						  </div>
						 <div class="form-group">
						    <label for="inputPassword3" class="col-sm-2 control-label">员工部门</label>
						    <div class="col-sm-4">
						    <select class="form-control" name="empDeptid" id="dept_update_select"></select>
						    </div>
						  </div>
						</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" id="emp_update_btn">更新</button>
      </div>
    </div>
  </div>
</div>

	<!--add modal -->
	<div class="modal fade" id="empAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">新增员工信息</h4>
      </div>
      <div class="modal-body">
						<form class="form-horizontal">
						  <div class="form-group">
						    <label for="inputEmail3" class="col-sm-2 control-label">员工姓名</label>
						    <div class="col-sm-10">
						      <input type="text" name="empName" class="form-control" id="empName_add_input" placeholder="empName">
						     <span class="help-block"></span>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="inputPassword3" class="col-sm-2 control-label">员工邮箱</label>
						    <div class="col-sm-10">
						      <input type="text" name="empEmail" class="form-control" id="empEmail_add_input" placeholder="email@qq.com">
						    <span class="help-block"></span>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="inputPassword3" class="col-sm-2 control-label">员工性别</label>
						    <div class="col-sm-10">
						  <label class="radio-inline">
						  <input type="radio" name="empGender" id="empGender_add_label" value="M" checked="checked">男
						</label>
						<label class="radio-inline">
						  <input type="radio" name="empGender" id="empGender_add_label2" value="F"> 女
						</label>
						    </div>
						  </div>
						 <div class="form-group">
						    <label for="inputPassword3" class="col-sm-2 control-label">员工部门</label>
						    <div class="col-sm-4">
						    <select class="form-control" name="empDeptid" id="dept_add_select"></select>
						    </div>
						  </div>
						</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" id="emp_save_btn">保存</button>
      </div>
    </div>
  </div>
</div>

	<!--显示页面-->
	<div>
		<div class="container">
			<div class="row">
				<div class="col-xs-12" style="color: greep" ><h1 >项目名称：云霞商城</h1></div>
			</div>
			<div class="row">
				<div class="col-md-4 col-md-offset-8">
					<button type="button" class="btn btn-primary" id="emps_add_btn">
					<span class="glyphicon glyphicon-plus" aria-hidden="true" ></span>
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
					<table class="table table-hover" id="emps_table">
					<thead>
						<tr>
							<th><input type="checkbox" id="check_all"/></th>
							<th>#</th>
							<th>empNme</th>
							<th>gender</th>
							<th>email</th>
							<th>deptName</th>
							<th>opration</th>
						</tr>
						</thead>
						<tbody>
					</tbody>
					</table>
				</div>
			</div>
			<div class="row">
			<div class="col-md-6" id="page_info_area"></div>
			<div class="col-md-6" id="page_nav_area"></div>
			</div>
		</div>	
	</div>
	
	
	
	<script type="text/javascript">
		$(function(){
			to_page(1)
		});
		function to_page(pn){
			$.ajax({
				url:"${APP_PATH}/emps",
				data:"pageNo="+pn,
				type:"GET",
				success:function(result){
					//console.log(result);
					//解析并显示员工数据
					base_emp_table(result);
					base_page_Info(result);
					base_page_nav(result);
				}
			});
		};
		//构建分页数据
		function base_emp_table(result){
			$("#emps_table tbody").empty();
			var emps=result.map.pageInfo.list;
			$.each(emps,function(index,item){
				var empId=$("<td></td>").append(item.empId);
				var empName=$("<td></td>").append(item.empName);
				var empGender=$("<td></td>").append(item.empGender=="M"?"男":"女");
				var empEmail=$("<td></td>").append(item.empEmail);
				var deptName=$("<td></td>").append(item.department.deptName);
				var btnEdit=$("<button></button>").addClass("btn btn-warning btn-sm edit_btn")
							.append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append(" ").append("编辑");
				btnEdit.attr("emp_id",item.empId);
				var btnQuery=$("<button></button>").addClass("btn btn-info btn-sm btn-danger")
							.append($("<span></span>").addClass("glyphicon glyphicon-trash")).append(" ").append("删除");
				btnQuery.attr("emp_id",item.empId);
				var EditQuery=$("<td></td>").append(btnEdit).append(" ").append(btnQuery);
				
				
				$("<tr></tr>").append(empId).append(empName)
				.append(empGender).append(empEmail).append(deptName).append(EditQuery)
				.appendTo("#emps_table tbody");
			});
		}
		//构建分页信息
		function base_page_Info(result){
			$("#page_info_area").empty();
			$("#page_info_area").append("当前第"+ result.map.pageInfo.pageNum +"页， 总共"+ result.map.pageInfo.pages +"页");
		}
		//构建分页条
		function base_page_nav(result){
			$("#page_nav_area").empty();
			var ul=$("<ul></ul>").addClass("pagination");
			if(result.map.pageInfo.isFirstPage == false){
			var firPage= $("<li></li>").append($("<a></a>").append("首页").attr("href","#"));
			firPage.click(function(){
				to_page(1);
			})
			}
			if(result.map.pageInfo.hasPreviousPage == true){
			var prePage= $("<li></li>").append($("<a></a>").append("&laquo;"));
			prePage.click(function(){
				to_page(result.map.pageInfo.pageNum-1);
			})
			}
			if(result.map.pageInfo.hasNextPage == true){
			var nextPage= $("<li></li>").append($("<a></a>").append("&raquo;"));
			nextPage.click(function(){
				to_page(result.map.pageInfo.pageNum+1);
			})
			}
			if(result.map.pageInfo.isLastPage == false){
			var lastPage= $("<li></li>").append($("<a></a>").append("末页").attr("href","#"));
			lastPage.click(function(){
				to_page(result.map.pageInfo.pages);
			})
			}
			var nav=$("<nav></nav>");
			ul.append(firPage).append(prePage); 
			$.each(result.map.pageInfo.navigatepageNums,function(index,item){
				var navLi=$("<li></li>").append($("<a></a>").append(item).attr("href","#"));
				if(result.map.pageInfo.pageNum == item){
					navLi.addClass("active");
				}
				navLi.click(function(){
					to_page(item);
				})
				ul.append(navLi);
			});
			ul.append(nextPage).append(lastPage);
			nav.append(ul).appendTo("#page_nav_area");
		}
		$("#emps_add_btn").click(function(){
			$("#empAddModal form")[0].reset();
			getDepts("#dept_add_select");
			$("#empAddModal").modal({
				backdrop:"static",keyboard:"true"
			});
		});
		function getDepts(ele){
			$(ele).empty();
			$.ajax({
				url:"${APP_PATH}/depts",
				type:"GET",
				success:function(result){
				$.each(result.map.depts,function(){
					var deptName=$("<option></option>").append(this.deptName).attr("value",this.deptId);
					deptName.appendTo(ele);
				});	
				}
			});
		}
		function regName(){
			var empName= $("#empName_add_input").val();
			var regName=/(^[a-zA-Z0-9]\w{4,15}$)|(^[\u4e00-\u9fa5]{2,5}$)/
			if(!regName.test(empName)){
				return false;
			}
			return true;
		}
		function regEmail(){
			var empMail= $("#empEmail_add_input").val();
			var regMail=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
			if(!regMail.test(empMail)){
				return false;
			}
			return true;
		}
		function show_msg(element,status,msg){
			$(element).parent().removeClass("has-success has-error");
			$(element).next("span").text("");
			if("success"==status){
				$(element).parent().addClass("has-success");
				$(element).next("span").text(msg);
				
			}else if("error"==status){
				$(element).parent().addClass("has-error");
				$(element).next("span").text(msg);
			}
		}
		$("#empName_add_input").change(function(){
			var empName= $("#empName_add_input").val();
			$.ajax({
				url:"${APP_PATH}/queryByempName",
				type:"POST",
				data:"empName="+empName,
				success:function(result){
					if(result.code==1000){
						show_msg("#empName_add_input","success","员工户名可用！");
						$("#emp_save_btn").attr("ajax_s","success");
					}else if(result.code==1001){
						show_msg("#empName_add_input","error","员工户名已存在！");
						$("#emp_save_btn").attr("ajax_s","error");
					}
				}
			});
		})
		
		$("#emp_save_btn").click(function(){
			//校验输入的名字和邮箱
			if(!regName()){
 				show_msg("#empName_add_input","error","员工名格式不正确！");
				return false; 	
			}else{
				show_msg("#empName_add_input","success","");
			}
			if(!regEmail()){
				show_msg("#empEmail_add_input","error","员工邮箱格式不正确！");
				return false; 
			}else{
				show_msg("#empEmail_add_input","success","");
			}
			var emp_save_btn=$(this).attr("ajax_s");
			if(emp_save_btn == "error"){
				return false;
			}
			$.ajax({
				url:"${APP_PATH}/emp",
				type:"POST",	
				data:$("#empAddModal form").serialize(),
				success:function(result){
					if(result.code == 1000){
					$("#empAddModal").modal('hide');
					to_page(1);
					}else{
						if(undefined != result.map.fieldError.empName){
						show_msg("#empName_add_input","error",result.map.fieldError.empName);
						}
						if(undefined != result.map.fieldError.empEmail){
						show_msg("#empEmail_add_input","error",result.map.fieldError.empEmail);
						}
					}
				}
			});
	});
	//修改的onclick事件	
	$(document).on("click",".edit_btn",function(){
		getDepts("#dept_update_select");
		getEmp($(this).attr("emp_id"));
		$("#emp_update_btn").attr("emp_id",$(this).attr("emp_id"));
		$("#empUpdateModal").modal({
			backdrop:"static",keyboard:"true"
		});
	});
	function getEmp(id){
		$.ajax({
			url:"${APP_PATH}/queryByEmpKey",
			type:"GET",
			data:"id="+id,
			success:function(result){
				var empData=result.map.emp;
				$("#empName_update_static").text(empData.empName);
				$("#empEmail_update_input").val(empData.empEmail);
				$("#empUpdateModal input[name=empGender]").val([empData.empGender]);
				$("#empUpdateModal select").val([empData.empDeptid])
			}
		});
	}
	$("#emp_update_btn").click(function(){
		var empMail= $("#empEmail_update_input").val();
		var regMail=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
		if(!regMail.test(empMail)){
			show_msg("#empEmail_update_input","error","员工邮箱格式不正确！");
			return false; 
		}else{
			show_msg("#empEmail_update_input","success","");
		}
		$.ajax({
			url:"${APP_PATH}/updateEmp/"+$(this).attr("emp_id"),
			type:"POST",
			data:$("#empUpdateModal form").serialize()+"&_method=PUT",
			success:function(result){
				$("#empUpdateModal").modal('hide');
				to_page(1);
			}
		});
	});

	//btn-danger
	$(document).on("click",".btn-danger" ,function(){
		var empName=$(this).parents("tr").find("td:eq(1)").text();
		var empId=$(this).attr("emp_id");
		if(confirm("确认要删除"+empName+"吗？")){
			$.ajax({
				url:"${APP_PATH}/delEMPById"+empId,
				type:"DELETE",
				susccess:function(result){
					alert(reselt.msg);
				}
			})
				to_page(1);
		}
	});
	
	</script> 
</body>
</html>