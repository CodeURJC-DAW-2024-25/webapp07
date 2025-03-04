package es.codeurjc.backend.service;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling user-related operations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user by encoding their password and saving them to the database.
     *
     * @param user The user to register.
     * @throws RuntimeException if an error occurs during saving.
     */
    public void registerUser(User user) {
        try {
            user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return An {@link Optional} containing the user if found, otherwise empty.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Bans a user by setting their banned status to {@code true}.
     *
     * @param userId The ID of the user to ban.
     * @throws IllegalArgumentException if the user is not found.
     */
    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(true);
        userRepository.save(user);
    }

    /**
     * Unbans a user by setting their banned status to {@code false}.
     *
     * @param userId The ID of the user to unban.
     * @throws IllegalArgumentException if the user is not found.
     */
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(false);
        userRepository.save(user);
    }

    /**
     * Searches for users whose username or email contains the given query.
     *
     * @param query The search query.
     * @return A list of users matching the search criteria.
     */
    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingOrEmailContaining(query, query);
    }

    /**
     * Updates an existing user's information.
     *
     * @param user The user entity with updated information.
     */
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Checks if a user with the given username exists.
     *
     * @param username The username to check.
     * @return {@code true} if a user with the given username exists, otherwise {@code false}.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email to check.
     * @return {@code true} if a user with the given email exists, otherwise {@code false}.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
