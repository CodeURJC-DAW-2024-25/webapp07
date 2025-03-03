package es.codeurjc.backend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom authentication failure handler that provides specific error messages
 * when authentication fails.
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * Handles authentication failure by setting an appropriate error message in the session
     * and redirecting the user to the login page.
     *
     * @param request   The HTTP request.
     * @param response  The HTTP response.
     * @param exception The authentication exception that caused the failure.
     * @throws IOException      If an input or output error occurs while handling the request.
     * @throws ServletException If the request could not be handled.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage;

        // Determine the error message based on the type of exception
        if (exception instanceof DisabledException ||
                (exception.getCause() != null && exception.getCause() instanceof DisabledException)) {
            errorMessage = "Your account has been banned. Contact support.";
        } else {
            errorMessage = "Incorrect credentials. Please try again.";
        }

        System.out.println("🔹 [CustomAuthenticationFailureHandler] Error message: " + errorMessage);

        // Store the error message in the session
        request.getSession().setAttribute("errorMessage", errorMessage);

        System.out.println("🔹 [CustomAuthenticationFailureHandler] Message set in session: " + request.getSession().getAttribute("errorMessage"));

        // Redirect to the login page
        response.sendRedirect("/login");
    }
}
