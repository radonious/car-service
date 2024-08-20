package edu.carservice.servlet;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.carservice.annotations.Loggable;
import edu.carservice.dto.OrderDTO;
import edu.carservice.mapper.OrderMapper;
import edu.carservice.model.Order;
import edu.carservice.service.AuthService;
import edu.carservice.service.OrderService;
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
@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private OrderService orderService;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        orderService = new OrderService();

    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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
