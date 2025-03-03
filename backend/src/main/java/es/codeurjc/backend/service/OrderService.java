package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.OrderRepository;
import es.codeurjc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishService dishService;

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

    public Optional<Order> findCartByUser(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId, "Cart")
                .stream()
                .findFirst();
    }



    public void saveOrder(Order order) {
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("Error saving order: " + e.getMessage());
            throw e;
        }
    }
    public void addDishToOrder(Long orderId, Long dishId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Optional<Dish> dishOpt = dishService.findById(dishId);
            if (dishOpt.isPresent()) {
                Dish dish = dishOpt.get();
                order.getDishes().add(dish);
                order.setTotalPrice(order.calculateTotalPrice());
                orderRepository.save(order);
            } else {
                throw new RuntimeException("Dish not found");
            }
        } else {
            throw new RuntimeException("Order not found");
        }
    }






}
