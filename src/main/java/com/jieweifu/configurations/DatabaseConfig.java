package com.jieweifu.configurations;

import com.jieweifu.common.utils.EncryptUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String dbDriverClassName;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(dbDriverClassName);
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(EncryptUtil.decrypt(dbUsername));
        dataSource.setPassword(EncryptUtil.decrypt(dbPassword));
        return dataSource;
    }
}
