package com.maxpaint.demo.dao;

import com.maxpaint.demo.model.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Repository
@Transactional
public class EmployeeDaoImpl extends AbstractDao<Long, Employee> implements EmployeeDao {

    /*@Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }*/

    @Override
    public List<Employee> getEmployees() {
        return getEntityManager()
            .createQuery("SELECT e FROM Employee e", Employee.class)
            .getResultList();
    }

    @Override
    public Employee save(Employee employee) {
        getEntityManager()
            .persist(employee);
        return employee;
    }

    @Override
    public Employee findById(final Long id) {
        return this.getByKey(id);
    }
}
