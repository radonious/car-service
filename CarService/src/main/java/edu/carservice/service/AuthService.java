package edu.carservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.carservice.annotations.Loggable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Loggable
@Service
public class AuthService {
    public static boolean checkToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC384("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return true;
    }

    public static boolean checkToken(HttpServletRequest req) throws JWTVerificationException {
        if (req.getHeader("Authorization") == null) throw new JWTVerificationException("No Authorization header");
        String header = req.getHeader("Authorization");
        if (!header.matches("^Bearer ((?:\\.?[A-Za-z0-9-_]+){3})$"))  throw new JWTVerificationException("Invalid Authorization header");
        String token = header.substring(7);
        return checkToken(token);
    }

    public String createToken() {
        try {
            Algorithm algorithm = Algorithm.HMAC384("secret");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            return null;
        }
    }
}
