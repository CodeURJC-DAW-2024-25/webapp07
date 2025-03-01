package es.codeurjc.backend.service;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getAuthenticatedUser() {
        // obtain user from securityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username);
        }

        return Optional.empty();
    }


    public void registerUser(User user) {
        try {
            user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            throw e;
        }
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}