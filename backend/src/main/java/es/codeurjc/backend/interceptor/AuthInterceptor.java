package es.codeurjc.backend.interceptor;

import es.codeurjc.backend.model.User;
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


@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("🔹 AuthInterceptor ejecutado para: " + request.getRequestURI());

            boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
            System.out.println("✅ Usuario autenticado: " + isAuthenticated);

            boolean isAdmin = false;

            if (isAuthenticated) {
                Object principal = auth.getPrincipal();
                System.out.println("📌 Tipo de principal: " + principal.getClass().getName());

                if (principal instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principal;
                    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                    System.out.println("🔍 Roles del usuario: " + authorities);

                    isAdmin = authorities.stream()
                            .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

                    System.out.println("⚡ Usuario es admin: " + isAdmin);
                } else {
                    System.out.println("⚠️ Principal no es instancia de UserDetails, no se pueden verificar roles.");
                }
            }

            modelAndView.addObject("isAuthenticated", isAuthenticated);
            modelAndView.addObject("isAdmin", isAdmin);
        }
    }
}
