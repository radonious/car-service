package edu.carservice.service;

import edu.carservice.annotations.Loggable;
import edu.carservice.util.ConnectionPool;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Loggable
public class MigrationService {
    public static void migrate() {
        URL fileUrl = ConnectionPool.class.getClassLoader().getResource("db.properties");

        try (
                FileInputStream fis = new FileInputStream(fileUrl.getFile());
                Connection c = ConnectionPool.getDataSource().getConnection();
                Statement statement = c.createStatement()
        ) {
            Properties properties = new Properties();
            properties.load(fis);
            String defaultSchema = properties.getProperty("liquibase.schema.default");
            String serviceSchema = properties.getProperty("liquibase.schema.service");
            String path = properties.getProperty("liquibase.changelog.path");

            statement.execute("create schema if not exists " + defaultSchema);
            statement.execute("create schema if not exists " + serviceSchema);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
            database.setDefaultSchemaName(defaultSchema);
            database.setLiquibaseSchemaName(serviceSchema);

            Liquibase liquibase = new Liquibase(path, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException | IOException e) {
            System.err.println("Database migration error: " + e.getMessage());
        }
    }
}
