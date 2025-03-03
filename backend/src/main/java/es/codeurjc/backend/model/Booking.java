package es.codeurjc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import es.codeurjc.backend.model.Restaurant;
import es.codeurjc.backend.model.User;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private LocalDate date;
    private String shift; // "LUNCH" or "DINNER"
    private int numPeople;

    public Booking() {}

    public Booking(Restaurant restaurant, User user, LocalDate date, String shift, int numPeople) {
        this.restaurant = restaurant;
        this.user = user;
        this.date = date;
        this.shift = shift;
        this.numPeople = numPeople;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }
}
