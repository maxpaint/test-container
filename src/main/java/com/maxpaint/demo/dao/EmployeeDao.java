package com.maxpaint.demo.dao;

import com.maxpaint.demo.model.Employee;

import java.io.Serializable;
import java.util.List;

public interface EmployeeDao {

	List<Employee> getEmployees();

	Employee save(Employee employee);

	Employee findById(Long id);

}
