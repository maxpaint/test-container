package com.maxpaint.demo.dao;

import com.maxpaint.demo.config.AppConfig;
import com.maxpaint.demo.config.AppInitializer;
import com.maxpaint.demo.config.HibernateConfig;
import com.maxpaint.demo.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class, AppInitializer.class, HibernateConfig.class})
@WebAppConfiguration
public class EmployeeDaoTest {

    @Qualifier("oracleDataSource")
    private DataSource dataSource;

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void save() {
        final DataSource r = dataSource;
        Employee empNew = new  Employee("Bytes", "Tree", "Senior Developer", 2000);
        employeeDao.save(empNew);
    }
}