package com.maxpaint.demo.service;

import com.maxpaint.demo.model.Employee;

import java.util.List;

public interface EmployeeService {

	List<Employee> getEmployees();

	Employee getEmployee(Long id);

	Employee addNewEmployee(Employee employee);
}
