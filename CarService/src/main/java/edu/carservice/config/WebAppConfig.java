package edu.carservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.carservice.repository.CarRepository;
import edu.carservice.repository.OrderRepository;
import edu.carservice.repository.UserRepository;
import edu.carservice.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@PropertySource("classpath:/application.properties")
public class WebAppConfig implements WebMvcConfigurer {

    @Bean
    public AuthService authService() {
        return new AuthService();
    }

    @Bean
    public CarService carService() {
        return new CarService(carRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(orderRepository());
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }

    @Bean
    public CarRepository carRepository() {
        return new CarRepository(dataSource());
    }

    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository(dataSource());
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepository(dataSource());
    }

    @Value("${spring.datasource.url}") private String url;

    @Value("${spring.datasource.username}") private String user;

    @Value("${spring.datasource.password}") private String password;

    @Value("${spring.datasource.driver-class-name}") private String driverName;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driverName);
        return new HikariDataSource(config);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MigrationService migrationService() {
        return new MigrationService(dataSource());
    }
}