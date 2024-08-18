package edu.carservice.servlet;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.carservice.annotations.Loggable;
import edu.carservice.dto.UserDTO;
import edu.carservice.mapper.UserMapper;
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
import java.util.List;
import java.util.stream.Collectors;

@Loggable
@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private UserService userService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            List<UserDTO> users =  userMapper.toUserDTOList(userService.getUsers());
            objectMapper.writeValue(resp.getWriter(), users);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JWTVerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            AuthService.checkToken(req);
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            User user = objectMapper.readValue(body, User.class);
            userService.addUser(user);
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
            User user = objectMapper.readValue(body, User.class);
            userService.updateUser(user);
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
            userService.removeUser(id);
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
