package edu.carservice.servlet;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.carservice.annotations.Loggable;
import edu.carservice.dto.CarDTO;
import edu.carservice.mapper.CarMapper;
import edu.carservice.model.Car;
import edu.carservice.service.AuthService;
import edu.carservice.service.CarService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Loggable
@WebServlet("/car")
public class CarServlet extends HttpServlet {
    private ObjectMapper mapper;
    private CarService carService;
    private final CarMapper carMapper = CarMapper.INSTANCE;


    @Override
    public void init() throws ServletException {
        super.init();
        mapper = new ObjectMapper();
        carService = new CarService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            List<Car> cars = carService.getCars();
            mapper.writeValue(resp.getWriter(), carMapper.toCarDTOList(cars));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            CarDTO car = mapper.readValue(body, CarDTO.class);
            carService.addCar(car.brand(), car.model(), car.year(), car.price(), car.condition());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Car car = mapper.readValue(body, Car.class);
            carService.updateCar(car);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            long id = Long.parseLong(mapper.readTree(body).get("id").toString());
            carService.removeCar(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
