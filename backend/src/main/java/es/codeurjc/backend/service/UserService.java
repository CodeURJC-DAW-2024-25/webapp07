package es.codeurjc.backend.service;

import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.mapper.UserMapper;
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
import java.time.format.DateTimeParseException;
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

    @Autowired
    private UserMapper userMapper;

    /**
     * Retrieves the currently authenticated user and returns it as a {@link UserDTO}.
     *
     * @return An {@link Optional} containing the {@link UserDTO} if authenticated, or empty otherwise.
     */
    public Optional<UserDTO> getAuthenticatedUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .map(userMapper::toDto);
        }

        return Optional.empty();
    }

    /**
     * Registers a new user from a {@link UserDTO}, applying necessary validations.
     *
     * @param userDTO The DTO containing user registration data.
     * @return The ID of the newly created user.
     * @throws IllegalArgumentException If username or email is already in use, or if data is invalid.
     */
    public Long registerUser(UserDTO userDTO) {
        if (existsByUsername(userDTO.username())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (existsByEmail(userDTO.email())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        LocalDate dob;
        try {
            dob = userDTO.dateOfBirth();
            if (dob == null) throw new IllegalArgumentException("Date of birth cannot be null.");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }

        User user = userMapper.toEntity(userDTO);
        user.setEncodedPassword(passwordEncoder.encode(userDTO.password()));
        user.setDateOfBirth(dob);
        user.setRoles(List.of("USER"));

        try {
            userRepository.save(user);
            System.out.println("✅ User '" + user.getUsername() + "' registered successfully");
            return user.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    /**
     * Finds a user by their username and maps it to a {@link UserDTO}.
     *
     * @param username The username to search for.
     * @return An {@link Optional} containing the corresponding {@link UserDTO}, if found.
     */
    public Optional<UserDTO> findUserDtoByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto);
    }

    /**
     * Retrieves all users from the database and maps them to {@link UserDTO}.
     *
     * @return A list of {@link UserDTO} representing all users.
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Searches users whose username or email contains the given query string.
     *
     * @param query The search query.
     * @return A list of matching {@link UserDTO} objects.
     */
    public List<UserDTO> searchUsers(String query) {
        return userRepository.findByUsernameContainingOrEmailContaining(query, query)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Marks a user as banned.
     *
     * @param userId The ID of the user to ban.
     * @throws IllegalArgumentException If the user does not exist.
     */
    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(true);
        userRepository.save(user);
    }

    /**
     * Removes the ban from a user.
     *
     * @param userId The ID of the user to unban.
     * @throws IllegalArgumentException If the user does not exist.
     */
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(false);
        userRepository.save(user);
    }

    /**
     * Updates the details of a user based on the provided {@link UserDTO}.
     *
     * @param id The ID of the user to update.
     * @param userDTO The DTO containing updated user information.
     * @return An {@link Optional} containing the updated {@link UserDTO}, or empty if not found.
     */
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(userDTO.firstName());
            existingUser.setLastName(userDTO.lastName());
            existingUser.setEmail(userDTO.email());
            existingUser.setPhoneNumber(userDTO.phoneNumber());
            existingUser.setAddress(userDTO.address());

            User savedUser = userRepository.save(existingUser);
            return userMapper.toDto(savedUser);
        });
    }

    /**
     * Checks if a username already exists in the system.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if an email already exists in the system.
     *
     * @param email The email to check.
     * @return True if the email exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Finds a user by their ID and maps them to a {@link UserDTO}.
     *
     * @param id The ID of the user to retrieve.
     * @return An {@link Optional} containing the {@link UserDTO}, if found.
     */
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @throws IllegalArgumentException If the user with the given ID does not exist.
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}