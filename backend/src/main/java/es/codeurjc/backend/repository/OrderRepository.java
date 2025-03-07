package es.codeurjc.backend.repository;

import es.codeurjc.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing orders in the database.
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves a list of orders for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of orders associated with the given user ID.
     */
    List<Order> findByUserId(Long userId);

    /**
     * Retrieves a list of orders for a specific user with a given status.
     *
     * @param userId The ID of the user.
     * @param status The status of the orders (e.g., "Pending", "Accepted", "Cancelled").
     * @return A list of orders that match the given user ID and status.
     */
    List<Order> findByUserIdAndStatus(Long userId, String status);
}