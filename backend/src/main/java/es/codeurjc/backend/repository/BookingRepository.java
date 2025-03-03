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

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.date >= CURRENT_DATE")
    Optional<Booking> findActiveBookingByUserId(@Param("userId") Long userId);


    // Buscar reservas de un restaurante en un turno específico
    @Query("SELECT b FROM Booking b WHERE b.restaurant = :restaurant AND b.shift = :shift AND b.date = :date")
    List<Booking> findByRestaurantAndShift(@Param("restaurant") Restaurant restaurant,
                                           @Param("shift") String shift,
                                           @Param("date") LocalDate date);
    @Query("SELECT b FROM Booking b WHERE b.restaurant.id = :restaurantId AND b.shift = :shift AND b.date = :date")
    List<Booking> findByRestaurantAndShiftAndDate(@Param("restaurantId") Long restaurantId,
                                                  @Param("shift") String shift,
                                                  @Param("date") LocalDate date);

}