package es.codeurjc.backend.service;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(true);
        userRepository.save(user);
    }

    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(false);
        userRepository.save(user);
    }
    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingOrEmailContaining(query, query);
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