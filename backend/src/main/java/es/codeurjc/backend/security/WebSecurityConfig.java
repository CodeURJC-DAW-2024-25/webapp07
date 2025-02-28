package es.codeurjc.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for security settings using Spring Security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationFailureHandler customFailureHandler;

    /**
     * Creates a password encoder bean using BCrypt.
     *
     * @return A {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

    /**
     * Configures security filters and access control for the application.
     *
     * @param http The {@link HttpSecurity} object for configuring security.
     * @return A {@link SecurityFilterChain} instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Set authentication provider
        http.authenticationProvider(authenticationProvider());

        // Disable CSRF for now
        http.csrf(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(authorize -> authorize
                        // Public resources
                        .requestMatchers("/css/**", "/img/**", "/images/**", "/js/**", "/lib/**", "/scss/**").permitAll()
                        .requestMatchers("/", "aboutUs", "faqs").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/menu").permitAll()
                        .requestMatchers("/menu/filter").permitAll()
                        .requestMatchers("/menu/sort").permitAll()
                        .requestMatchers("/menu/{id}").permitAll()
                        .requestMatchers("/api/menu").permitAll()

                        // Private pages (authenticated users)
                        .requestMatchers(request -> request.getServletPath().startsWith("/profile")).authenticated()

                        // Private pages (authenticated users)
                        .requestMatchers(request -> request.getServletPath().startsWith("/profile")).authenticated()
                        .requestMatchers("/booking/**").hasRole("USER") // Solo usuarios registrados pueden acceder a reservas
                        .requestMatchers("/admin/bookings/**").hasRole("ADMIN") //  Solo admins pueden gestionar reservas

                        // Admin-restricted pages
                        .requestMatchers(HttpMethod.GET, "/menu/admin/new-dish").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/menu/{id}/admin/edit-dish").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/menu/admin/new-dish/save").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/menu/{id}/admin/edit-dish/save").hasAnyRole("ADMIN")
                        .requestMatchers("/menu/{id}/admin/remove-dish").hasAnyRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
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
