package es.codeurjc.backend.security;

import java.util.ArrayList;
import java.util.List;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service responsible for loading user details from the database for authentication.
 */
@Service
public class RepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs the service with the given user repository.
     *
     * @param userRepository The repository for accessing user data.
     */
    public RepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username for authentication.
     *
     * @param username The username of the user to authenticate.
     * @return A {@link UserDetails} object containing user authentication data.
     * @throws UsernameNotFoundException If no user is found with the given username.
     * @throws DisabledException If the user is banned.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("🔍 [RepositoryUserDetailsService] Attempting to authenticate user: " + username);

        // Retrieve user from the repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("⚠️ [RepositoryUserDetailsService] User not found: " + username);
                    return new UsernameNotFoundException("User not found");
                });

        // Check if the user is banned
        if (user.isBanned()) {
            System.out.println("⛔ [RepositoryUserDetailsService] User is banned: " + username);
            throw new DisabledException("User is banned");
        }

        // Convert user roles to GrantedAuthority objects
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        System.out.println("✅ [RepositoryUserDetailsService] User authenticated: " + username + " with roles: " + roles);

        // Return a Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getEncodedPassword(),
                true,
                true,
                true,
                !user.isBanned(),
                roles
        );
    }
}
