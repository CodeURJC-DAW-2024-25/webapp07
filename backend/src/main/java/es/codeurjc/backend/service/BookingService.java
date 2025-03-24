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

/**
 * Service class for managing booking operations.
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Finds the active booking of a user.
     *
     * @param user The user whose active booking is being searched.
     * @return An optional containing the active booking if found.
     */
    public Optional<Booking> findActiveBookingByUser(User user) {
        return bookingRepository.findActiveBookingByUserId(user.getId());
    }
    public Optional<Booking> findActiveBookingByUserId(Long userId) {
        return bookingRepository.findActiveBookingByUserId(userId);
    }
    /**
     * Creates a new booking if there is availability.
     *
     * @param restaurant The restaurant where the booking is being made.
     * @param user The user making the booking.
     * @param date The booking date.
     * @param shift The shift (LUNCH or DINNER).
     * @param numPeople The number of people in the booking.
     * @return True if the booking was successfully created, false if there was no availability.
     */
    public boolean createBooking(Restaurant restaurant, User user, LocalDate date, String shift, int numPeople) {
        List<Booking> existingBookings = bookingRepository.findByRestaurantAndShift(restaurant, shift, date);
        int totalPeople = existingBookings.stream().mapToInt(Booking::getNumPeople).sum();

        if (totalPeople + numPeople > 40) {
            return false; // Not enough seats available
        }

        Booking booking = new Booking(restaurant, user, date, shift, numPeople);
        bookingRepository.save(booking);
        return true;
    }

    /**
     * Cancels a booking.
     *
     * @param booking The booking to be canceled.
     */
    public void cancelBooking(Booking booking) {
        bookingRepository.delete(booking);
    }

    /**
     * Retrieves all active bookings for the admin panel.
     *
     * @return A list of active bookings.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findActiveBookings(); // Now returns only active bookings
    }

    /**
     * Cancels a booking by its ID.
     *
     * @param id The ID of the booking to be canceled.
     */
    public void cancelBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        booking.ifPresent(bookingRepository::delete);
    }

    /**
     * Finds bookings by restaurant, shift, and date.
     *
     * @param restaurantId The ID of the restaurant.
     * @param date The date of the booking.
     * @param shift The shift (LUNCH or DINNER).
     * @return A list of matching bookings.
     */
    public List<Booking> findBookingsByRestaurantAndShift(Long restaurantId, LocalDate date, String shift) {
        return bookingRepository.findByRestaurantAndShiftAndDate(restaurantId, shift, date);
    }

    /**
     * Gets the number of available seats for a specific restaurant, shift, and date.
     *
     * @param restaurantId the ID of the restaurant
     * @param date the date of the reservation
     * @param shift the shift (LUNCH or DINNER)
     * @return the number of available seats (maximum 40 per shift)
     */
    public int getAvailableSeats(Long restaurantId, LocalDate date, String shift) {
        List<Booking> existingBookings = bookingRepository.findByRestaurantAndShiftAndDate(restaurantId, shift, date);
        int totalPeopleReserved = existingBookings.stream().mapToInt(Booking::getNumPeople).sum();
        return Math.max(40 - totalPeopleReserved, 0);
    }

    /**
     * Searches bookings by user information: username, email or phone number.
     *
     * @param query the search query to match against user details
     * @return a list of matching bookings
     */
    public List<Booking> searchBookings(String query) {
        return bookingRepository.findByUser_UsernameContainingOrUser_EmailContainingOrUser_PhoneNumberContaining(query, query, query);
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the ID of the booking
     * @return an optional containing the booking if found, or empty otherwise
     */
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Saves a booking to the repository.
     *
     * @param booking the booking to save
     */
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    /**
     * Performs an advanced search for bookings by user info, shift, restaurant ID and/or date.
     *
     * @param query optional query string to match username, email, or phone number
     * @param shift optional shift (LUNCH or DINNER)
     * @param restaurantId optional ID of the restaurant
     * @param date optional booking date
     * @return a list of bookings matching all provided criteria
     */
    public List<Booking> advancedSearch(String query, String shift, Long restaurantId, LocalDate date) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getDate().isAfter(LocalDate.now()) || b.getDate().isEqual(LocalDate.now()))
                .filter(b -> query == null ||
                        b.getUser().getUsername().toLowerCase().contains(query.toLowerCase()) ||
                        b.getUser().getEmail().toLowerCase().contains(query.toLowerCase()) ||
                        b.getUser().getPhoneNumber().toLowerCase().contains(query.toLowerCase()))
                .filter(b -> shift == null || b.getShift().equalsIgnoreCase(shift))
                .filter(b -> restaurantId == null || b.getRestaurant().getId().equals(restaurantId))
                .filter(b -> date == null || b.getDate().isEqual(date))
                .toList();
    }

}
