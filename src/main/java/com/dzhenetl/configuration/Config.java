package com.dzhenetl.configuration;

import com.dzhenetl.util.PropertiesUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@EnableWebMvc
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.dzhenetl")
public class Config {

    private static final String JDBC_DRIVER_KEY = "db.driver";
    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USERNAME_KEY = "db.username";
    private static final String DB_PASSWORD_KEY = "db.password";
    private static final String ENTITY_PACKAGE_KEY = "entity.package";
    private static final String HIBERNATE_DIALECT_KEY = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL_KEY = "hibernate.show.sql";


    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(PropertiesUtil.get(JDBC_DRIVER_KEY));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        dataSource.setJdbcUrl(PropertiesUtil.get(DB_URL_KEY));
        dataSource.setUser(PropertiesUtil.get(DB_USERNAME_KEY));
        dataSource.setPassword(PropertiesUtil.get(DB_PASSWORD_KEY));

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(PropertiesUtil.get(ENTITY_PACKAGE_KEY));

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(HIBERNATE_DIALECT_KEY, PropertiesUtil.get(HIBERNATE_DIALECT_KEY));
        hibernateProperties.setProperty(HIBERNATE_SHOW_SQL_KEY, PropertiesUtil.get(HIBERNATE_SHOW_SQL_KEY));
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();

        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }

}
