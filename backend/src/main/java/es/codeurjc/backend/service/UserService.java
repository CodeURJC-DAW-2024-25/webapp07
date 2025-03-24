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


    public Optional<UserDTO> getAuthenticatedUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .map(userMapper::toDto);
        }

        return Optional.empty();
    }




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



    public Optional<UserDTO> findUserDtoByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto);
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserDTO> searchUsers(String query) {
        return userRepository.findByUsernameContainingOrEmailContaining(query, query)
                .stream()
                .map(userMapper::toDto)
                .toList();
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


    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }


    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }


}