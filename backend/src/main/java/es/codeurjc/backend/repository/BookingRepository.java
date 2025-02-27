package es.codeurjc.backend.repository;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Buscar la reserva activa de un usuario
    Optional<Booking> findByUser(User user);

    // Buscar reservas de un restaurante en un turno específico
    @Query("SELECT b FROM Booking b WHERE b.restaurant = :restaurant AND b.shift = :shift")
    List<Booking> findByRestaurantAndShift(@Param("restaurant") Restaurant restaurant, @Param("shift") String shift);
}