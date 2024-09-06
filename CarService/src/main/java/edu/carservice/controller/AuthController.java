package edu.carservice.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.carservice.model.User;
import edu.carservice.service.AuthService;
import edu.carservice.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    private ObjectMapper mapper;
    private UserService userService;
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService, UserService userService, ObjectMapper mapper) {
        this.authService = authService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        try {
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            User user = mapper.readValue(body, User.class);
            if (user == null) throw new IOException("invalid body");
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
