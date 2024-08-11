package edu.carservice.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ConnectionPool {
    private static DataSource dataSource;

    private ConnectionPool() {
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            initialize();
        }
        return dataSource;
    }

    private static void initialize() {

        URL fileUrl = ConnectionPool.class.getClassLoader().getResource("db.properties");

        try (FileInputStream fis = new FileInputStream(fileUrl.getFile())) {
            Properties properties = new Properties();
            properties.load(fis);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            String driverName = properties.getProperty("db.driver.name");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setDriverClassName(driverName);
            dataSource = new HikariDataSource(config);

        } catch (IOException ex) {
            System.out.println("Config file load error: " + ex.getMessage());
        }
    }
}
