package edu.carservice.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.*;

import static org.mockito.Mockito.*;

public class AuthServletTest {

    @Test
    public void testDoPostValid200() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String reqJson = "{\"name\": \"admin\",\"password\": \"admin\"}";
        BufferedReader reader = new BufferedReader(new StringReader(reqJson));
        when(request.getReader()).thenReturn(reader);


        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        AuthServlet servlet = new AuthServlet();
        servlet.init();
        servlet.doPost(request, response);
        writer.flush();

        String expectedJson = "{\"token\":\"eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCJ9.-n_dFgkh21Hi3dOAMFN-pmkGQ71IMJFw3M3Z1Kj7hGqNkduAD6tGZVnCfEl5eASu\",\"type\":\"Bearer\"}";
        Assertions.assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoPostInvalid404() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String reqJson = "{\"name\": \"admin\",\"password\": \"admin123\"}";
        BufferedReader reader = new BufferedReader(new StringReader(reqJson));
        when(request.getReader()).thenReturn(reader);


        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        AuthServlet servlet = new AuthServlet();
        servlet.init();
        servlet.doPost(request, response);
        writer.flush();

        String expectedJson = "{\"token\":\"eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCJ9.-n_dFgkh21Hi3dOAMFN-pmkGQ71IMJFw3M3Z1Kj7hGqNkduAD6tGZVnCfEl5eASu\",\"type\":\"Bearer\"}";
        Assertions.assertNotEquals(expectedJson, stringWriter.toString());
    }
}

