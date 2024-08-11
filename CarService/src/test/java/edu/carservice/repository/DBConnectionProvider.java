package edu.carservice.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DBConnectionProvider {
    private final DataSource dataSource;

    public DBConnectionProvider(String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(password);
        dataSource = new HikariDataSource(config);
    }

    DataSource getDataSource() {
        return dataSource;
    }
}
