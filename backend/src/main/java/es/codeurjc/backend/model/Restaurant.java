package es.codeurjc.backend.model;

import jakarta.persistence.*;

/**
 * Represents a restaurant entity.
 */
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String location;

    /**
     * Default constructor.
     */
    public Restaurant() {}

    /**
     * Creates a new restaurant with the specified location.
     *
     * @param location The location of the restaurant.
     */
    public Restaurant(String location) {
        this.location = location;
    }

    /**
     * Gets the ID of the restaurant.
     *
     * @return The restaurant ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the restaurant.
     *
     * @param id The restaurant ID.
     */
    public void setId(Long id) {
        this.id = id;
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
     * Sets the location of the restaurant.
     *
     * @param location The restaurant location.
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
