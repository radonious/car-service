package edu.carservice.servlet;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.carservice.annotations.Loggable;
import edu.carservice.model.User;
import edu.carservice.service.AuthService;
import edu.carservice.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@Loggable
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private ObjectMapper mapper;
    private UserService userService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        super.init();
        mapper = new ObjectMapper();
        userService = new UserService();
        authService = new AuthService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        try {
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            User user = mapper.readValue(body, User.class);
            boolean isValid = userService.checkPassword(user.getName(), user.getPassword());
            if (isValid) {
                String token = authService.createToken();
                ObjectNode node = mapper.createObjectNode();
                node.put("token", token);
                node.put("type", "Bearer");
                resp.getWriter().write(mapper.writeValueAsString(node));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
