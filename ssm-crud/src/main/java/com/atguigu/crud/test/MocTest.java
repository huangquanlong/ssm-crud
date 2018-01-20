package com.atguigu.crud.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.atguigu.crud.bean.Employee;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:applicationContext.xml","file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class MocTest {
	
	//传入springMVC的IOC
	@Autowired
	WebApplicationContext context;
	//虚拟MVC请求，拿到处理结果
	MockMvc mockMvc;
	
	@Before
	public void initMockMvc(){
		mockMvc=MockMvcBuilders.webAppContextSetup(context).build();
	}
	@Test
	public void testPage() throws Exception{
	//模拟拿到返回值
	MvcResult result=	mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pageNo", "5")).andReturn();
	//请求成功后，请求域中会有pageInfo，我们可以拿出pageInfo进行验证
	MockHttpServletRequest request= result.getRequest();
		PageInfo pi=(PageInfo) request.getAttribute("pageInfo");
		System.out.println("当前页码："+pi.getPageNum());
		System.out.println("总记录数："+pi.getTotal());
		System.out.println("总页码："+pi.getPages());
		System.out.println("在页面需要连续显示的页码：");
		int [] num= pi.getNavigatepageNums();
		for(int n:num){
			System.out.print(" "+n);
		}
		List<Employee> list= pi.getList();
		for(Employee emp:list){
			System.out.println("员工姓名："+emp.getEmpName()+" "+"员工部门："+emp.getDepartment().getDeptName());
		}
	}
}
