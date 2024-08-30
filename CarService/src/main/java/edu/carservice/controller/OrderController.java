package edu.carservice.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.carservice.dto.OrderDTO;
import edu.carservice.mapper.OrderMapper;
import edu.carservice.model.Order;
import edu.carservice.service.AuthService;
import edu.carservice.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/order")
public class OrderController {
    private ObjectMapper objectMapper;
    private OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public  OrderController(ObjectMapper objectMapper, OrderService orderService, OrderMapper orderMapper) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            List<OrderDTO> orders = orderMapper.toOrderDTOList(orderService.getOrders());
            objectMapper.writeValue(resp.getWriter(), orders);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            OrderDTO order = objectMapper.readValue(body, OrderDTO.class);
            orderService.addOrder(order.userId(), order.carId(), order.category());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Order order = objectMapper.readValue(body, Order.class);
            orderService.updateOrder(order);
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

    @RequestMapping(method = RequestMethod.DELETE)
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            long id = Long.parseLong(objectMapper.readTree(body).get("id").toString());
            orderService.removeOrder(id);
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
