package es.codeurjc.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to register CSRF protection handler in the application.
 * Adds a custom interceptor to handle CSRF tokens in HTTP requests.
 */
@Configuration
public class CSRFHandler implements WebMvcConfigurer {

    /**
     * Registers the CSRF interceptor to handle CSRF tokens in HTTP requests.
     *
     * @param registry The registry used to add interceptors.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CSRFHandlerInterceptor());
    }
}

/**
 * Custom interceptor to extract and add the CSRF token to the model.
 * This interceptor ensures that the CSRF token is available in the view.
 */
class CSRFHandlerInterceptor implements HandlerInterceptor {

    /**
     * Intercepts the request after it is handled by the controller.
     * Adds the CSRF token to the model if it is available.
     *
     * @param request       The HTTP request.
     * @param response      The HTTP response.
     * @param handler       The handler (controller method) that processed the request.
     * @param modelAndView  The model and view returned by the handler.
     * @throws Exception If an error occurs while processing the request.
     */
    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
            if (token != null) {
                modelAndView.addObject("token", token.getToken());
                System.out.println("❗ CSRF Token: " + token.getToken());
            } else {
                System.out.println("❌ No CSRF Token found ❌");
            }
        }
    }
}
