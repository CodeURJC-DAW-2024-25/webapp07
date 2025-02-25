package es.codeurjc.backend.model;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO);
    @Id
    private Long id;



    @ManyToMany
    private List<Dish> dishes;

    private LocalDateTime orderDate;
    private String address;
    private String status; // Could be "Pending" / "Accepted" / "Cancelled";
    private double totalPrice;

    public Order() {}

    public Order(List<Dish> dishes, String address, String status) {
        this.dishes = dishes;
        this.orderDate = LocalDateTime.now();
        this.address = address;
        this.status = status;
        this.totalPrice = dishes.stream().mapToDouble(Dish::getPrice).sum();
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