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

@Service
public class RepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public RepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("🔍 [RepositoryUserDetailsService] Intentando autenticar usuario: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("⚠️ [RepositoryUserDetailsService] Usuario no encontrado: " + username);
                    return new UsernameNotFoundException("User not found");
                });

        if (user.isBanned()) {
            System.out.println("⛔ [RepositoryUserDetailsService] Usuario baneado: " + username);
            throw new DisabledException("User is banned");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        System.out.println("✅ [RepositoryUserDetailsService] Usuario autenticado: " + username + " con roles: " + roles);


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getEncodedPassword(),
                true,
                true,
                true, !user.isBanned(),
                roles);
    }
}
