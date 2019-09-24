package com.maxpaint.demo.dao;

import com.maxpaint.demo.config.AppConfig;
import com.maxpaint.demo.config.AppInitializer;
import com.maxpaint.demo.config.HibernateConfig;
import com.maxpaint.demo.model.Employee;
import org.hibernate.annotations.Filter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class, AppInitializer.class, HibernateConfig.class})
@WebAppConfiguration
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void save() {
        Employee empNew = new Employee("Bytes", "Tree", "Senior Developer", 2000);
        employeeDao.save(empNew);
    }
}