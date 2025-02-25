package es.codeurjc.backend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage;

        if (exception instanceof DisabledException ||
                (exception.getCause() != null && exception.getCause() instanceof DisabledException)) {
            errorMessage = "Your account has been banned. Contact support.";
        } else {
            errorMessage = "Incorrect credentials. Please try again.";
        }


        System.out.println("🔹 [CustomAuthenticationFailureHandler] Mensaje de error: " + errorMessage);

        request.getSession().setAttribute("errorMessage", errorMessage);


        System.out.println("🔹 [CustomAuthenticationFailureHandler] Mensaje establecido en sesión: " + request.getSession().getAttribute("errorMessage"));

        response.sendRedirect("/login");
    }
}
