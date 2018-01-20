package com.atguigu.crud.bean;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

public class Employee {
    private Integer empId;
    
    @Pattern(regexp="(^[a-zA-Z0-9]\\w{4,15}$)|(^[\\u4e00-\\u9fa5]{2,5}$)" ,
    		message="员工名格式不正确！")
    private String empName;

    private String empGender;
    @Email(regexp="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",
 		   message="员工邮箱格式不正确！")
    private String empEmail;

    private Integer empDeptid;
    
    private Department department;
    
    public Employee() {
		super();
	}

	public Employee(Integer empId, String empName, String empGender, String empEmail, Integer empDeptid) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empGender = empGender;
		this.empEmail = empEmail;
		this.empDeptid = empDeptid;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender == null ? null : empGender.trim();
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail == null ? null : empEmail.trim();
    }

    public Integer getEmpDeptid() {
        return empDeptid;
    }

    public void setEmpDeptid(Integer empDeptid) {
        this.empDeptid = empDeptid;
    }
}