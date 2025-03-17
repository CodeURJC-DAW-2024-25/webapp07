package es.codeurjc.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private String address;
    private String status;
    private double totalPrice;

    public OrderDTO() {}

    public OrderDTO(Long id,Long userId, LocalDateTime orderDate, String address, String status, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.address = address;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public OrderDTO(Long id, List<DishDTO> dishDTOs, Long id1, LocalDateTime orderDate, String address, String status, double totalPrice) {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
