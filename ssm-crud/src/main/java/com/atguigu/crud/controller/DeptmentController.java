package com.atguigu.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.DeptmentService;

/*
 * 部门控制类
 * */
@Controller
public class DeptmentController {
	
	@Autowired
	private DeptmentService deptmentService;
	
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDepts(){
		List<Department> list= deptmentService.getDempts();
		return Msg.success().add("depts", list);
	}
}
