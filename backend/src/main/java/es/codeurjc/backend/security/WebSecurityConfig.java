package es.codeurjc.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationFailureHandler customFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        authProvider.setHideUserNotFoundExceptions(false);

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.csrf(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES
                        .requestMatchers("/css/**", "/img/**", "/images/**", "/js/**", "/lib/**", "/scss/**").permitAll()
                        .requestMatchers("/", "aboutUs", "faqs").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/menu").permitAll()
                        .requestMatchers("/menu/{id}").permitAll()
                        .requestMatchers("/api/menu").permitAll()

                        // PRIVATE PAGES
                        .requestMatchers(request -> request.getServletPath().startsWith("/profile")).authenticated()
                        .requestMatchers("/menu/new-dish").hasAnyRole("ADMIN")
                        .requestMatchers("/menu/{id}/edit-dish").hasAnyRole("ADMIN")



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
