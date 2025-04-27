package es.codeurjc.backend.repository;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Booking entities.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds an active booking for a specific user.
     *
     * @param userId The ID of the user.
     * @return An optional containing the active booking if found.
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.date >= CURRENT_DATE")
    Optional<Booking> findActiveBookingByUserId(@Param("userId") Long userId);

    /**
     * Retrieves all active bookings (future or current dates).
     *
     * @return A list of active bookings.
     */
    @Query("SELECT b FROM Booking b WHERE b.date >= CURRENT_DATE ORDER BY b.date ASC")
    List<Booking> findActiveBookings();


    /**
     * Finds bookings for a specific restaurant, shift, and date.
     *
     * @param restaurant The restaurant entity.
     * @param shift The shift (Lunch or Dinner).
     * @param date The booking date.
     * @return A list of matching bookings.
     */
    @Query("SELECT b FROM Booking b WHERE b.restaurant = :restaurant AND b.shift = :shift AND b.date = :date")
    List<Booking> findByRestaurantAndShift(@Param("restaurant") Restaurant restaurant,
                                           @Param("shift") String shift,
                                           @Param("date") LocalDate date);

    /**
     * Finds bookings for a specific restaurant ID, shift, and date.
     *
     * @param restaurantId The ID of the restaurant.
     * @param shift The shift (Lunch or Dinner).
     * @param date The booking date.
     * @return A list of matching bookings.
     */
    @Query("SELECT b FROM Booking b WHERE b.restaurant.id = :restaurantId AND b.shift = :shift AND b.date = :date")
    List<Booking> findByRestaurantAndShiftAndDate(@Param("restaurantId") Long restaurantId,
                                                  @Param("shift") String shift,
                                                  @Param("date") LocalDate date);

    @Query("""
    SELECT b FROM Booking b
    WHERE 
      (LOWER(b.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
      OR LOWER(b.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
      OR LOWER(b.phone) LIKE LOWER(CONCAT('%', :query, '%'))
      OR LOWER(b.email) LIKE LOWER(CONCAT('%', :query, '%'))
      OR LOWER(b.restaurant.location) LIKE LOWER(CONCAT('%', :query, '%'))
      OR LOWER(b.shift) LIKE LOWER(CONCAT('%', :query, '%'))
      OR STR(b.numPeople) LIKE CONCAT('%', :query, '%')
      OR STR(b.date) LIKE CONCAT('%', :query, '%'))
      AND b.date >= CURRENT_DATE
""")
    List<Booking> searchBookingsByAllFields(@Param("query") String query);

}