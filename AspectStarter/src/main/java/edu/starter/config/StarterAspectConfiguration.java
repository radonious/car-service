package edu.starter.config;

import edu.starter.aspect.LoggableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarterAspectConfiguration {
    @Bean
    LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}
