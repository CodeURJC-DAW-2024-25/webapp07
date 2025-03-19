package es.codeurjc.backend.security;

import es.codeurjc.backend.security.jwt.JwtRequestFilter;
import es.codeurjc.backend.security.jwt.UnauthorizedHandlerJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for security settings using Spring Security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationFailureHandler customFailureHandler;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Creates a password encoder bean using BCrypt.
     *
     * @return A {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the authentication provider using a DAO-based approach.
     *
     * @return A {@link DaoAuthenticationProvider} instance.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        authProvider.setHideUserNotFoundExceptions(false);

        return authProvider;
    }
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http
                .authorizeHttpRequests(authorize -> authorize
                        // AUTH ENDPOINTS
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/logout").permitAll()
                        // PRIVATE USER ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/new").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/menu").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/menu/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/menu/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/menu/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/menu/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/cart/add").authenticated()










                        // PUBLIC ENDPOINTS
                        .anyRequest().permitAll()
                );

        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    /**
     * Configures security filters and access control for the application.
     *
     * @param http The {@link HttpSecurity} object for configuring security.
     * @return A {@link SecurityFilterChain} instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Set authentication provider
        http.authenticationProvider(authenticationProvider());
        //http.csrf(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(authorize -> authorize

                        // Private pages (authenticated users)
                        .requestMatchers(request -> request.getServletPath().startsWith("/profile")).authenticated()
                        .requestMatchers("/booking/**").hasRole("USER") // Solo usuarios registrados pueden acceder a reservas
                        .requestMatchers("/booking-cancelled").hasRole("USER")// Permitir acceso a la página de confirmación

                        // Admin-restricted pages
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/menu/admin/new-dish").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/menu/{id}/admin/edit-dish").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/menu/admin/new-dish/save").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/menu/{id}/admin/edit-dish/save").hasAnyRole("ADMIN")
                        .requestMatchers("/menu/{id}/admin/remove-dish").hasAnyRole("ADMIN")
                        .requestMatchers("/menu/{id}/admin/mark-unavailable-dish").hasAnyRole("ADMIN")




                        // Public resources
                        .requestMatchers("/**").permitAll()
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureHandler(customFailureHandler)
                        .defaultSuccessUrl("/")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }
}