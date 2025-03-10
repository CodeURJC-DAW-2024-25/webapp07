package es.codeurjc.backend.service;

import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.OrderRepository;
import es.codeurjc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing orders.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /*@Autowired
    private UserRepository userRepository;*/

    @Autowired
    private DishService dishService;

    /**
     * Saves a new order in the database.
     *
     * @param order The order to be saved.
     */
    public void createOrder(Order order) {
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("Error saving order: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order.
     * @return An Optional containing the order if found.
     */
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return A list of all orders.
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of orders associated with the user.
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Updates the status of an existing order.
     *
     * @param id        The ID of the order.
     * @param newStatus The new status to be set (e.g., "Pending", "Paid", "Cancelled").
     */
    public void updateOrderStatus(Long id, String newStatus) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.setStatus(newStatus);
            orderRepository.save(updatedOrder);
        } else {
            System.err.println("Order with id " + id + " not found.");
            throw new RuntimeException("Order not found.");
        }
    }

    /**
     * Retrieves all orders with a "Paid" status for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of orders with status "Paid".
     */
    public List<Order> getPaidOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId, "Paid");
    }

    /**
     * Retrieves the cart (order with status "Cart") for a specific user.
     *
     * @param userId The ID of the user.
     * @return An Optional containing the cart order if found.
     */
    public Optional<Order> findCartByUser(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId, "Cart")
                .stream()
                .findFirst();
    }

    /**
     * Saves an order in the database.
     *
     * @param order The order to be saved.
     */
    public void saveOrder(Order order) {
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("Error saving order: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Adds a dish to an existing order.
     *
     * @param orderId The ID of the order.
     * @param dishId  The ID of the dish to be added.
     */
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

    /**
     * Deletes an order by its ID.
     *
     * @param id The ID of the order to delete.
     */
    public void deleteOrderById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order with ID " + id + " not found.");
        }
    }

    public void updateOrder(Long orderId, String address, String status, Double totalPrice) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setAddress(address);
            order.setStatus(status);
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }




}