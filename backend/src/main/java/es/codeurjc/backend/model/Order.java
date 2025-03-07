package es.codeurjc.backend.model;

import jakarta.persistence.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Represents an order made by a user.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private List<Dish> dishes;

    @ManyToOne // One user can place multiple orders
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime orderDate;
    private String address;
    private String status; // Possible values: "Pending", "Accepted", "Cancelled"
    private double totalPrice;

    /**
     * Default constructor.
     */
    public Order() {}

    /**
     * Constructs an order with the given details.
     * @param dishes List of dishes included in the order.
     * @param user The user who placed the order.
     * @param address Delivery address.
     * @param status Status of the order (Pending, Accepted, Cancelled).
     * @param totalPrice Total price of the order.
     */
    public Order(List<Dish> dishes, User user, String address, String status, Double totalPrice) {
        this.dishes = dishes;
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.address = address;
        this.status = status;
        this.totalPrice = totalPrice; // Calculate total price by summing up dish prices
    }

    /**
     * Gets the order ID.
     * @return Order ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the order ID.
     * @param id Order ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the list of dishes in the order.
     * @return List of dishes.
     */
    public List<Dish> getDishes(){
        return dishes;
    }

    /**
     * Sets the list of dishes in the order and recalculates the total price.
     * @param dishes List of dishes.
     */
    public void setDishes(List<Dish> dishes){
        this.dishes = dishes;
        this.totalPrice = dishes.stream().mapToDouble(Dish::getPrice).sum(); // Recalculate total price
    }

    /**
     * Gets the order date.
     * @return Order date.
     */
    public LocalDateTime getOrderDate(){
        return orderDate;
    }

    /**
     * Gets the user who placed the order.
     * @return User.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who placed the order.
     * @param user User.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the delivery address.
     * @return Delivery address.
     */
    public String getAddress(){
        return address;
    }

    /**
     * Sets the delivery address.
     * @param address Delivery address.
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * Gets the status of the order.
     * @return Order status.
     */
    public String getStatus (){
        return status;
    }

    /**
     * Sets the status of the order.
     * @param status Order status (Pending, Accepted, Cancelled).
     */
    public void setStatus(String status){
        this.status = status;
    }

    /**
     * Gets the total price of the order.
     * @return Total price.
     */
    public double getTotalPrice (){
        return totalPrice;
    }

    /**
     * Sets the total price of the order.
     * @param totalPrice Total price.
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Calculates and returns the total price of the order by summing up the prices of the dishes.
     * @return Total price of the order.
     */
    public double calculateTotalPrice() {
        return dishes.stream().mapToDouble(Dish::getPrice).sum();
    }

    /**
     * Formats and returns the order date in a readable string format.
     * @return Formatted order date.
     */
    public String getFormattedOrderDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM - HH:mm", new Locale("es", "ES"));
        return orderDate.format(formatter);
    }
}