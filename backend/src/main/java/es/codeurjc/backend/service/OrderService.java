package es.codeurjc.backend.service;

import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.mapper.OrderMapper;
import es.codeurjc.backend.mapper.UserMapper;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.OrderRepository;
import es.codeurjc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserMapper userMapper;

    public OrderDTO addDishToUserCart(Long dishId, String username) {
        UserDTO userDTO = userService.findUserDtoByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = userMapper.toEntity(userDTO);

        Order cart = findCartByUser(user.getId())
                .orElseGet(() -> {
                    Order newCart = new Order(new ArrayList<>(), user, "", "Cart", 0.0);
                    saveOrder(newCart);
                    return newCart;
                });

        Dish dish = dishService.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        cart.getDishes().add(dish);
        cart.setTotalPrice(cart.calculateTotalPrice());
        saveOrder(cart);

        return orderMapper.toDto(cart);
    }





    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order.
     * @return An Optional containing the order if found.
     */
    public OrderDTO getOrderDTOByIdForUser(Long id, String username) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return orderMapper.toDto(order);
    }


    /**
     * Retrieves all orders from the database.
     *
     * @return A list of all orders.
     */
    public Map<String, Object> getAllOrdersAsDTOMap() {
        List<Order> orders = orderRepository.findAll();

        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orderDTOs);
        response.put("hasOrders", !orderDTOs.isEmpty());
        response.put("modalId", "confirmationModal");
        response.put("confirmButtonId", "confirmAction");
        response.put("modalMessage", "Are you sure you want to proceed with this action?");

        return response;
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
    public OrderDTO updateOrderStatusChecked(Long id, String newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }



    /**
     * Retrieves all orders with a "Paid" status for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of orders with status "Paid".
     */
    public List<OrderDTO> getPaidOrderDTOsByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdAndStatus(userId, "Paid");
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
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
    public Map<String, String> deleteOrderAndReturnResponse(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);

        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order with ID " + id + " not found.");
        }

        orderRepository.deleteById(id);

        return Map.of("message", "Order deleted successfully!");
    }


    public Map<String, String> updateOrderFromMap(Long orderId, Map<String, Object> updates) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        Order order = orderOpt.get();

        if (updates.containsKey("address")) {
            order.setAddress((String) updates.get("address"));
        }

        if (updates.containsKey("status")) {
            order.setStatus((String) updates.get("status"));
        }

        if (updates.containsKey("totalPrice")) {
            order.setTotalPrice(((Number) updates.get("totalPrice")).doubleValue());
        }

        orderRepository.save(order);

        return Map.of("message", "Order updated successfully!");
    }

    public OrderDTO viewCartForUser(String username) {
        UserDTO userDTO = userService.findUserDtoByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = userMapper.toEntity(userDTO);

        Order cart = findCartByUser(user.getId())
                .orElseGet(() -> {
                    Order newCart = new Order(new ArrayList<>(), user, "", "Cart", 0.0);
                    saveOrder(newCart);
                    return newCart;
                });

        cart.setTotalPrice(cart.calculateTotalPrice());
        saveOrder(cart);

        return orderMapper.toDto(cart);
    }

    public Map<String, Object> clearUserCart(String username) {
        UserDTO userDTO = userService.findUserDtoByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = userMapper.toEntity(userDTO);

        Order cart = findCartByUser(user.getId())
                .orElseGet(() -> new Order(new ArrayList<>(), user, "", "Cart", 0.0));

        cart.getDishes().clear();
        cart.setTotalPrice(0.0);
        saveOrder(cart);

        return Map.of(
                "success", true,
                "message", "Cart cleared successfully"
        );
    }




    public Map<String, Object> getOrderSummaryDTOById(Long orderId, String username) {
        OrderDTO orderDTO = getOrderDTOByIdForUser(orderId, username);

        UserDTO userDTO = userService.findUserDtoByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("Paid".equals(orderDTO.status())) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Redirect to paid info");
        }

        double deliveryCost = 4.99;
        double totalPrice = orderDTO.totalPrice();
        double finalPrice = Math.round((totalPrice + deliveryCost) * 100.0) / 100.0;

        Map<String, Object> response = new HashMap<>();
        response.put("id", orderDTO.id());
        response.put("dishes", orderDTO.dishes());
        response.put("totalPrice", totalPrice);
        response.put("deliveryCost", deliveryCost);
        response.put("finalPrice", finalPrice);
        response.put("address", orderDTO.address());
        response.put("user", Map.of(
                "id", userDTO.id(),
                "username", userDTO.username(),
                "firstName", userDTO.firstName(),
                "lastName", userDTO.lastName()
        ));

        return response;
    }

    public OrderDTO updateOrderStatusAndAddressChecked(Long id, String newStatus, String newAddress) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setStatus(newStatus);
        order.setAddress(newAddress);

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }





}




