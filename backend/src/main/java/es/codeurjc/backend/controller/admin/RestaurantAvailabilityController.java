package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.dto.RestaurantDTO;
import es.codeurjc.backend.service.BookingService;
import es.codeurjc.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing restaurant availability in the admin panel.
 * Provides functionality to display available seats for lunch and dinner on a specific date.
 */
@Controller
public class RestaurantAvailabilityController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private BookingService bookingService;

    /**
     * Displays the restaurant availability page.
     * Retrieves available seats for lunch and dinner based on the selected date.
     *
     * @param date  The date for which availability is requested (optional, defaults to today).
     * @param model The model to pass attributes to the view.
     * @return The view name for the restaurant availability page.
     */
    @GetMapping("/admin/restaurant-availability")
    public String showRestaurantAvailability(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        if (date == null) {
            date = LocalDate.now();
        }

        final LocalDate finalDate = date;

        List<RestaurantDTO> restaurants = restaurantService.findAll();
        List<RestaurantAvailability> availabilityList = restaurants.stream()
                .map(restaurant -> new RestaurantAvailability(
                        restaurant.location(),
                        bookingService.getAvailableSeats(restaurant.id(), finalDate, "LUNCH"),
                        bookingService.getAvailableSeats(restaurant.id(), finalDate, "DINNER")
                ))
                .collect(Collectors.toList());

        model.addAttribute("availabilityList", availabilityList);
        model.addAttribute("selectedDate", finalDate);
        return "admin/restaurant-availability";
    }

    /**
     * Inner class representing the availability of a restaurant.
     * Stores the restaurant location and available seats for lunch and dinner.
     */
    public static class RestaurantAvailability {
        private String location;
        private int lunchAvailableSeats;
        private int dinnerAvailableSeats;

        /**
         * Constructs a RestaurantAvailability object with location and available seats data.
         *
         * @param location            The location of the restaurant.
         * @param lunchAvailableSeats The number of available seats for lunch.
         * @param dinnerAvailableSeats The number of available seats for dinner.
         */
        public RestaurantAvailability(String location, int lunchAvailableSeats, int dinnerAvailableSeats) {
            this.location = location;
            this.lunchAvailableSeats = lunchAvailableSeats;
            this.dinnerAvailableSeats = dinnerAvailableSeats;
        }

        /**
         * Gets the location of the restaurant.
         *
         * @return The restaurant location.
         */
        public String getLocation() {
            return location;
        }

        /**
         * Gets the number of available seats for lunch.
         *
         * @return The number of available seats for lunch.
         */
        public int getLunchAvailableSeats() {
            return lunchAvailableSeats;
        }

        /**
         * Gets the number of available seats for dinner.
         *
         * @return The number of available seats for dinner.
         */
        public int getDinnerAvailableSeats() {
            return dinnerAvailableSeats;
        }
    }
}
