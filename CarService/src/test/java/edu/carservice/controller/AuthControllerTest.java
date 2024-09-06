package edu.carservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.carservice.model.User;
import edu.carservice.service.AuthService;
import edu.carservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        mapper = new ObjectMapper();
    }

    @Test
    void authEmptyBodyTest() throws Exception {
        mockMvc.perform(post("/auth")).andExpect(status().is4xxClientError());
    }

    @Test
    void authOkBodyTest() throws Exception {
        String name = "admin";
        String password = "admin";
        User user = new User(null, name, password, null);
        String json = mapper.writeValueAsString(user);
        when(userService.checkPassword(name, password)).thenReturn(true);
        mockMvc.perform(
                post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    void authFailBodyTest() throws Exception {
        String name = "admin";
        String password = "not admin";
        User user = new User(null, name, password, null);
        String json = mapper.writeValueAsString(user);
        when(userService.checkPassword(name, password)).thenReturn(false);
        mockMvc.perform(
                post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().is4xxClientError());
    }

}
