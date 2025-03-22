package es.codeurjc.backend.security.jwt;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class UnauthorizedHandlerJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandlerJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String json = """
            {
                "status": 401,
                "error": "Unauthorized",
                "message": "%s",
                "path": "%s"
            }
            """.formatted(authException.getMessage(), request.getServletPath());

        response.getWriter().write(json);
    }
}
