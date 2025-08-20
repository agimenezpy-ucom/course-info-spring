package com.pluralsight.courseinfo.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class JdbcRepositoryFactory {

    @Value("${database-file}")
    private String databaseFile;

    public void setDatabaseFile(String databaseFile) {
        this.databaseFile = databaseFile;
    }

    private static final String H2_DATABASE_URL = 
        "jdbc:h2:file:%s;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM './db_init.sql'";

    @Bean
    public DataSource openDataSource() {
        var dataSource = new DriverManagerDataSource(H2_DATABASE_URL.formatted(databaseFile));

        return dataSource;
    }
}
