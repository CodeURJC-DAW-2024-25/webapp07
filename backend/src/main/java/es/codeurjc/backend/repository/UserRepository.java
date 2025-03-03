package es.codeurjc.backend.repository;

import es.codeurjc.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods for retrieving and checking user existence based on username and email.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return An {@link Optional} containing the user if found, otherwise empty.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username The username to check.
     * @return {@code true} if a user with the given username exists, otherwise {@code false}.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email to check.
     * @return {@code true} if a user with the given email exists, otherwise {@code false}.
     */
    boolean existsByEmail(String email);

    /**
     * Finds users whose username or email contains the specified value.
     *
     * @param username The username fragment to search for.
     * @param email The email fragment to search for.
     * @return A list of users matching the criteria.
     */
    List<User> findByUsernameContainingOrEmailContaining(String username, String email);
}
