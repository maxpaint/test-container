package com.maxpaint.demo.controller;

import java.util.List;

import com.maxpaint.demo.model.Employee;
import com.maxpaint.demo.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import static java.util.Objects.isNull;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployee() {
        final List<Employee> employees = employeeService.getEmployees();
        if (isNull(employees) || employees.isEmpty()) {
            Employee empNew = new Employee("Bytes", "Tree", "Senior Developer", 2000);
            employeeService.addNewEmployee(empNew);
        }

        return employeeService.getEmployees();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity getEmployee(@PathVariable("id") Long id) {
        Employee employee = employeeService.getEmployee(id);
        if (isNull(employee)) {
            return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(employee, HttpStatus.OK);
    }

    @PostMapping(value = "/employees")
    @ResponseStatus(code = HttpStatus.OK)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.addNewEmployee(employee);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        return new ResponseEntity(id, HttpStatus.OK);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return new ResponseEntity(employee, HttpStatus.OK);
    }
}