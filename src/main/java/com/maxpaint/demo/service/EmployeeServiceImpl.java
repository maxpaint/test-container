package com.maxpaint.demo.service;

import com.maxpaint.demo.dao.EmployeeDao;
import com.maxpaint.demo.model.Employee;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	final static Logger logger = Logger.getLogger(EmployeeServiceImpl.class);

	private EmployeeDao employeeDao;

	public EmployeeServiceImpl(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Employee> getEmployees() {
		return employeeDao.getEmployees();
	}

	@Transactional(readOnly = true)
	public Employee getEmployee(Long id) {
		logger.debug("Getting employee with id " + id);
		return employeeDao.findById(id);
	}


	@Override
	@Transactional
	public Employee addNewEmployee(Employee employee) {
		final Employee emp = employeeDao.save(employee);
		logger.debug("Id of new Employee " + emp.getId());
		return emp;
	}

}
