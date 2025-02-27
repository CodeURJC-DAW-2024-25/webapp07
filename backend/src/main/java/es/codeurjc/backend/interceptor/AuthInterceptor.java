package es.codeurjc.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * Interceptor that adds authentication and authorization details to the model
 * after a request has been handled.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * Adds authentication details to the model after the request is processed.
     *
     * @param request      The HTTP request.
     * @param response     The HTTP response.
     * @param handler      The handler that processed the request.
     * @param modelAndView The model and view returned by the controller.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("🔹 AuthInterceptor executed for: " + request.getRequestURI());

            boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
            System.out.println("✅ User authenticated: " + isAuthenticated);

            boolean isAdmin = false;

            if (isAuthenticated) {
                Object principal = auth.getPrincipal();
                System.out.println("📌 Principal type: " + principal.getClass().getName());

                if (principal instanceof UserDetails userDetails) {
                    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                    System.out.println("🔍 User roles: " + authorities);

                    isAdmin = authorities.stream()
                            .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

                    System.out.println("⚡ User is admin: " + isAdmin);
                } else {
                    System.out.println("⚠️ Principal is not an instance of UserDetails, roles cannot be verified.");
                }
            }

            // Add authentication details to the model
            modelAndView.addObject("isAuthenticated", isAuthenticated);
            modelAndView.addObject("isAdmin", isAdmin);
        }
    }
}
