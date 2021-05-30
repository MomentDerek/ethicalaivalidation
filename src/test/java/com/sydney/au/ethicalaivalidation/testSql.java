package com.sydney.au.ethicalaivalidation;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class testSql {


    @Test
    void test() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new FileSystemResource("src/main/resources/database/database create statement.sql"));
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://localhost:3306/?serverTimezone=UTC&useUnicode=true&zeroDateTimeBehavior=convertToNull");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("momincong");
        resourceDatabasePopulator.execute(mysqlDataSource);
    }
}
