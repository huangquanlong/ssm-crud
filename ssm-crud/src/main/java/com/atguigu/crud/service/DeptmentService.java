package com.atguigu.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crud.bean.Department;
import com.atguigu.crud.dao.DepartmentMapper;
@Service
public class DeptmentService {
	
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	public List<Department> getDempts() {
		List<Department> list= departmentMapper.selectByExample(null);
		return list;
	}

}
