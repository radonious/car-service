package edu.carservice.service;

import edu.carservice.annotations.Loggable;

import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Loggable
@Service
@PropertySource("classpath:/application.properties")
public class MigrationService {

    DataSource dataSource;

    @Value("${liquibase.schema.default}") private String defaultSchema;

    @Value("${liquibase.schema.service}") private String serviceSchema;

    @Value("${liquibase.changelog.path}") private String path;

    @Autowired
    public MigrationService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void migrate() {
        try (
                Connection c = dataSource.getConnection();
                Statement statement = c.createStatement()
        ) {

            statement.execute("create schema if not exists " + defaultSchema);
            statement.execute("create schema if not exists " + serviceSchema);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
            database.setDefaultSchemaName(defaultSchema);
            database.setLiquibaseSchemaName(serviceSchema);

            Liquibase liquibase = new Liquibase(path, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException e) {
            System.err.println("Database migration error: " + e.getMessage());
        }
    }
}
