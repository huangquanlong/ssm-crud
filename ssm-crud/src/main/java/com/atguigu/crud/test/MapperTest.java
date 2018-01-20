package com.atguigu.crud.test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.dao.DepartmentMapper;
import com.atguigu.crud.dao.EmployeeMapper;
/**
 * @Title: MapperTest 
* @Description:  测试dao层的工作
* 推荐spring项目就可以使用spring的单元测试，可以自动注入我们需要的组件
* 导入springTest模块
* @ContextConfiguration指定application.xml文件位置
* 直接autowired要使用的组件
* @date 2017年9月25日 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MapperTest {
	@Autowired
	EmployeeMapper employeeMpper;
	
	@Autowired
	DepartmentMapper departmentMapper;
	@Autowired
	SqlSession sqlSession;
	@Test
	public void testCRUD(){
		System.out.println(departmentMapper);
/*		departmentMapper.insert(new Department(null, "开发一部"));
		departmentMapper.insert(new Department(null, "开发二部"));*/
//		employeeMpper.insertSelective(new Employee(null, "jack" ,"M", "jack.qq.com", 1));
		EmployeeMapper mapper=sqlSession.getMapper(EmployeeMapper.class);	
		for(int i=0;i<500;i++){
			String user= UUID.randomUUID().toString().substring(0, 5)+i;
			mapper.insertSelective(new Employee(null, user, "M", user+"@qq.com", 1));
		}
		System.out.println("批量完成");
	}
}
