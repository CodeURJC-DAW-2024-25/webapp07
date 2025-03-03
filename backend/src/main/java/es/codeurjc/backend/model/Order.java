package es.codeurjc.backend.model;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToMany
    private List<Dish> dishes;


    @ElementCollection
    @CollectionTable(name = "order_dish_quantities", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "dish_id")
    @Column(name = "quantity")
    private Map<Dish, Integer> dishQuantities = new HashMap<>();


    @ManyToOne // one user can order several orders
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    private LocalDateTime orderDate;
    private String address;
    private String status; // Could be "Pending" / "Accepted" / "Cancelled";
    private double totalPrice;

    public Order() {}



    public Order(List<Dish> dishes, User user, String address, String status, Double totalprice) {
        this.dishes = dishes;
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.address = address;
        this.status = status;
        this.totalPrice = totalprice; // calculate total price sum each dish price
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Dish> getDishes(){
        return dishes;
    }

    public void setDishes(List<Dish> dishes){
        this.dishes = dishes;
        this.totalPrice = dishes.stream().mapToDouble(Dish::getPrice).sum(); // calculate new price
    }

    public LocalDateTime getOrderDate(){
        return orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getStatus (){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public double getTotalPrice (){
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public double calculateTotalPrice() {
        return dishes.stream().mapToDouble(Dish::getPrice).sum();
    }
    public Map<Dish, Integer> getDishQuantities() {
        return dishQuantities;
    }

    public void setDishQuantities(Map<Dish, Integer> dishQuantities) {
        this.dishQuantities = dishQuantities;
    }
}