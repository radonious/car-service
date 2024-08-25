package edu.carservice.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.carservice.dto.UserDTO;
import edu.carservice.mapper.UserMapper;
import edu.carservice.model.User;
import edu.carservice.service.AuthService;
import edu.carservice.service.UserService;
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
@RequestMapping(path = "/user")
public class UserController {
    private ObjectMapper objectMapper;
    private UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(ObjectMapper objectMapper, UserService userService, UserMapper userMapper) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
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

    @RequestMapping(method = RequestMethod.POST)
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

    @RequestMapping(method = RequestMethod.PUT)
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

    @RequestMapping(method = RequestMethod.DELETE)
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
