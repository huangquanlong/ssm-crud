package com.atguigu.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.EmployeeExample;
import com.atguigu.crud.bean.EmployeeExample.Criteria;
import com.atguigu.crud.dao.EmployeeMapper;

@Service
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;
	
	public List<Employee> getALL(){
		
		
		return employeeMapper.selectByExampleWithDept(null);
	}

	public void addEmp(Employee employee) {
	 employeeMapper.insertSelective(employee);
	}

	public boolean querryByempKey(String empName) {
		EmployeeExample example=new EmployeeExample();
		Criteria criteria= example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long l= employeeMapper.countByExample(example);
		return l==0;
	}

	public Employee getEmp(Integer id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	public void UpdataByEmpId(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
	}

	public void delByEmpId(Integer id) {
		employeeMapper.deleteByPrimaryKey(id);
	}
}
