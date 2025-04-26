package es.codeurjc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Represents a booking made by a user at a restaurant.
 */
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;
    private String shift; // "LUNCH" or "DINNER"
    private int numPeople;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;


    /**
     * Default constructor.
     */
    public Booking() {}

    /**
     * Creates a new booking with the specified details.
     *
     * @param restaurant The restaurant where the booking is made.
     * @param user The user who made the booking.
     * @param date The date of the booking.
     * @param shift The shift of the booking (LUNCH or DINNER).
     * @param numPeople The number of people in the booking.
     */
    public Booking(Restaurant restaurant, User user, LocalDate date, String shift, int numPeople) {
        this.restaurant = restaurant;
        this.user = user;
        this.date = date;
        this.shift = shift;
        this.numPeople = numPeople;
    }

    /**
     * Gets the booking ID.
     *
     * @return The booking ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the booking ID.
     *
     * @param id The booking ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the restaurant of the booking.
     *
     * @return The restaurant.
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Sets the restaurant for the booking.
     *
     * @param restaurant The restaurant.
     */
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Gets the user who made the booking.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who made the booking.
     *
     * @param user The user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the date of the booking.
     *
     * @return The booking date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the booking.
     *
     * @param date The booking date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the shift of the booking.
     *
     * @return The booking shift (LUNCH or DINNER).
     */
    public String getShift() {
        return shift;
    }

    /**
     * Sets the shift of the booking.
     *
     * @param shift The booking shift (LUNCH or DINNER).
     */
    public void setShift(String shift) {
        this.shift = shift;
    }

    /**
     * Gets the number of people in the booking.
     *
     * @return The number of people.
     */
    public int getNumPeople() {
        return numPeople;
    }

    /**
     * Sets the number of people in the booking.
     *
     * @param numPeople The number of people.
     */
    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    /**
     * Gets the first name of the person who made the booking.
     *
     * @return The first name.
     */
    public String getFirstName() {return firstName; }

    /**
     * Sets the first name of the person who made the booking.
     *
     * @param firstName The first name.
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Gets the last name of the person who made the booking.
     *
     * @return The last name.
     */
    public String getLastName() { return lastName; }

    /**
     * Sets the last name of the person who made the booking.
     *
     * @param lastName The last name.
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Gets the phone number of the person who made the booking.
     *
     * @return The phone number.
     */
    public String getPhone() { return phone; }

    /**
     * Sets the phone number of the person who made the booking.
     *
     * @param phone The phone number.
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Gets the email of the person who made the booking.
     *
     * @return The email.
     */
    public String getEmail() { return email; }

    /**
     * Sets the email of the person who made the booking.
     *
     * @param email The email.
     */
    public void setEmail(String email) { this.email = email; }


}