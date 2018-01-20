package com.atguigu.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	
	//按主键删除员工信息
	@RequestMapping(value="/delEMPById{id}" ,method=RequestMethod.DELETE)
	@ResponseBody
	public Msg delByEmpId(@PathVariable(value="id") Integer id){
		//System.out.println(id);
		employeeService.delByEmpId(id);
		return Msg.success();
	}
	//按主键修改员工信息
	@RequestMapping(value="/updateEmp/{empId}" ,method=RequestMethod.PUT)
	@ResponseBody
	public Msg updateByEmpId(Employee employee){
		 employeeService.UpdataByEmpId(employee);
		return Msg.success();
	}
	//按主键查找员工信息
	@RequestMapping(value="/queryByEmpKey" ,method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@RequestParam(value="id") Integer id){
		Employee employee= employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}
	
	//新增员工
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee ,BindingResult result ){
		if (result.hasErrors()) {
			Map<String, String> map=new HashMap<String, String>();
			List<FieldError> fieldErrors= result.getFieldErrors();
			for(FieldError fieldError:fieldErrors){
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("fieldError", map);
		}else {
			 employeeService.addEmp(employee);
				return Msg.success();
		}
	}
	
	//查询用户名是否可用
	@RequestMapping(value="/queryByempName",method=RequestMethod.POST)
	@ResponseBody
	public Msg querryByempName(@RequestParam(value="empName") String empName){
		boolean b= employeeService.querryByempKey(empName);
		if (b) {
			 return Msg.success();
		}else{
			return Msg.fail();
		}
	}
	
	//查询全部员工并分页显示
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pageNo",defaultValue="1")Integer pageNo){
		//引入Pagehelper插件 
		//在查询之前只需调用,传入页码，以及每页的大小
		PageHelper.startPage(pageNo, 5);
		//紧跟在这个分页之后的是查询方法；
		List<Employee> emps= employeeService.getALL();
		//用PageInfo对结果进行包装，只需要将pageinfo交给页面
		//封装了页面详细信息，同时也包含了查询出来的数据
		PageInfo page = new PageInfo(emps,5);
		return Msg.success().add("pageInfo", page);
	}
	
/*	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pageNo",defaultValue="1")Integer pageNo,
			Model model){
		//引入Pagehelper插件
		//在查询之前只需调用,传入页码，以及每页的大小
		PageHelper.startPage(pageNo, 6);
		//紧跟在这个分页之后的是查询方法；
		List<Employee> emps= employeeService.getALL();
		//用PageInfo对结果进行包装，只需要将pageinfo交给页面
		//封装了页面详细信息，同时也包含了查询出来的数据
		PageInfo<Employee> page = new PageInfo(emps,5);
		model.addAttribute("pageInfo", page);
		return "list";
	}*/
}
