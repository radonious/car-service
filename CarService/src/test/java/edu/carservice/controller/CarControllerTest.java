package edu.carservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.carservice.mapper.CarMapper;
import edu.carservice.model.Car;
import edu.carservice.service.CarService;
import edu.carservice.util.CarCondition;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CarControllerTest {

    @Spy
    ObjectMapper objectMapper;

    @Mock
    CarService carService;

    @Spy
    CarMapper carMapper;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        mapper = new ObjectMapper();
    }

    @Test
    void getCarsNoToken() throws Exception {
        mockMvc.perform(get("/car")).andExpect(status().is4xxClientError());
    }

    @Test
    void getCars() throws Exception {
        mockMvc.perform(get("/car")
                .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCJ9.-n_dFgkh21Hi3dOAMFN-pmkGQ71IMJFw3M3Z1Kj7hGqNkduAD6tGZVnCfEl5eASu")
        ).andExpect(status().isOk());
    }

    @Test
    void postCars() throws Exception {
        Car car = new Car(null, "BRAND", "MODEL", 2024, 1000000, CarCondition.NEW);
        String json = mapper.writeValueAsString(car);

        mockMvc.perform(post("/car")
                .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCJ9.-n_dFgkh21Hi3dOAMFN-pmkGQ71IMJFw3M3Z1Kj7hGqNkduAD6tGZVnCfEl5eASu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    void deleteCars() throws Exception {
        String json = mapper.writeValueAsString(mapper.createObjectNode().put("id", 3));
        mockMvc.perform(delete("/car")
                .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCJ9.-n_dFgkh21Hi3dOAMFN-pmkGQ71IMJFw3M3Z1Kj7hGqNkduAD6tGZVnCfEl5eASu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    void putCars() throws Exception {
        Car car = new Car(3L, "BRAND", "MODEL", 2024, 1000000, CarCondition.NEW);
        String json = mapper.writeValueAsString(car);
        mockMvc.perform(put("/car")
                .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCJ9.-n_dFgkh21Hi3dOAMFN-pmkGQ71IMJFw3M3Z1Kj7hGqNkduAD6tGZVnCfEl5eASu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }
}
