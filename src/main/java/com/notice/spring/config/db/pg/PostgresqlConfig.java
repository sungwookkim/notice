package com.notice.spring.config.db.pg;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * <pre>
 *     Postgresql 설정
 * </pre>
 */
@Profile("!vanilla")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.notice.repository.rdb")
public class PostgresqlConfig {
    private final String driverClassName;
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public PostgresqlConfig(@Value("${spring.datasource.pg.driver-class-name}") String driverClassName
            , @Value("${spring.datasource.pg.jdbc-url}") String jdbcUrl
            , @Value("${spring.datasource.pg.username}") String username
            , @Value("${spring.datasource.pg.password}") String password) {

        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Bean
    public DataSource postgresqlDataSource() {

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName("pg");
        hikariDataSource.setDriverClassName(this.driverClassName);
        hikariDataSource.setUsername(this.username);
        hikariDataSource.setJdbcUrl(this.jdbcUrl);
        hikariDataSource.setPassword(this.password);

        return hikariDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource postgresqlDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresqlDataSource);
        em.setPackagesToScan("com.notice.domain");

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {

        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());

        return jpaTransactionManager;
    }
}
