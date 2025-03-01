package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // sava new order
    public void createOrder (Order order){
        try{
            orderRepository.save(order);
        }catch (Exception e){
            System.err.println("Error saving order" + e.getMessage());
            throw e;
        }
    }

    // get order by id
    public Optional<Order> getOrderById (Long id){
        return orderRepository.findById(id);
    }

    //get all orders
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    //get orders from a specific user
    public List<Order> getOrdersByUserId(Long userId){
        return orderRepository.findByUserId(userId);
    }

    //update order status.
    public void updateOrderStatus(Long id, String newStatus) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.setStatus(newStatus);
            orderRepository.save(updatedOrder);
        } else {
            System.err.println("Order with id " + id + " not finded.");
            throw new RuntimeException("Order not finded.");
        }
    }


    public List<Order> getPaidOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId, "Paid");
    }
}
