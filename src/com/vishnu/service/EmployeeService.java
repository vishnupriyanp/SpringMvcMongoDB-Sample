package com.vishnu.service;

import java.util.List;

import com.vishnu.model.Employee;

public interface EmployeeService {
	
	public Boolean addEmployee(Employee employee);

	public List<Employee> listEmployeess();
	
	public Employee getEmployee(String empid);
	
	public Boolean deleteEmployee(String empid);
}
