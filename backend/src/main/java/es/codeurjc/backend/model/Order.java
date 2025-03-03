package es.codeurjc.backend.model;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToMany
    private List<Dish> dishes;

    @ManyToOne // one user can order several orders
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    private LocalDateTime orderDate;
    private String address;
    private String status; // Could be "Pending" / "Accepted" / "Cancelled";
    private double totalPrice;

    public Order() {}

    public Order(List<Dish> dishes, User user, String address, String status) {
        this.dishes = dishes;
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.address = address;
        this.status = status;
        this.totalPrice = dishes.stream().mapToDouble(Dish::getPrice).sum(); // calculate total price sum each dish price
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


}