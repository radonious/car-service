package edu.carservice.listener;

import edu.carservice.aspect.LoggableAspect;
import edu.carservice.service.MigrationService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartStopListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("DB migration process...");
        LoggableAspect aspect = new LoggableAspect();
        MigrationService.migrate();
        System.out.println("WEB Server running...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("WEB Server shutting down...");
    }
}
