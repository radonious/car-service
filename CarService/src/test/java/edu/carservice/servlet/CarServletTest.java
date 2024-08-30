package edu.carservice.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarServletTest {
//    @Test
//    public void testDoPostValid200() throws IOException, ServletException {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        String reqJson = "{\"brand\": \"TEST\",\"model\": \"TEST\",\"year\": 2008,\"price\": 1200000,\"condition\": \"USED\"}";
//        BufferedReader reader = new BufferedReader(new StringReader(reqJson));
//        when(request.getReader()).thenReturn(reader);
//
//        AuthServlet servlet = new AuthServlet();
//        servlet.init();
//        servlet.doPost(request, response);
//
//        Assertions.assertEquals(200, response.getStatus());
//    }
}
