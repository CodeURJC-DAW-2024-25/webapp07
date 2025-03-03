package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Booking;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Buscar la reserva activa de un usuario
    public Optional<Booking> findActiveBookingByUser(User user) {
        return bookingRepository.findActiveBookingByUserId(user.getId());
    }


    // Crear una nueva reserva si hay disponibilidad
    public boolean createBooking(Restaurant restaurant, User user, LocalDate date, String shift, int numPeople) {
        List<Booking> existingBookings = bookingRepository.findByRestaurantAndShift(restaurant, shift, date);
        int totalPeople = existingBookings.stream().mapToInt(Booking::getNumPeople).sum();

        if (totalPeople + numPeople > 40) {
            return false; // No hay suficientes sillas disponibles
        }

        Booking booking = new Booking(restaurant, user, date, shift, numPeople);
        bookingRepository.save(booking);
        return true;
    }



    // Cancelar una reserva
    public void cancelBooking(Booking booking) {
        bookingRepository.delete(booking);
    }

    // Obtener todas las reservas activas para el administrador
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    // Cancelar una reserva por ID
    public void cancelBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        booking.ifPresent(bookingRepository::delete);
    }
    public List<Booking> findBookingsByRestaurantAndShift(Long restaurantId, LocalDate date, String shift) {
        return bookingRepository.findByRestaurantAndShiftAndDate(restaurantId, shift, date);
    }


}

