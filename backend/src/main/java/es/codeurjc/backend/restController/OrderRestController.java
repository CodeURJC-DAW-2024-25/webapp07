package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.mapper.OrderMapper;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.DishService;
import es.codeurjc.backend.service.OrderService;
import es.codeurjc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderMapper orderMapper;


    //ADMIN

    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders);
        response.put("hasOrders", !orders.isEmpty());
        response.put("modalId", "confirmationModal");
        response.put("confirmButtonId", "confirmAction");
        response.put("modalMessage", "Are you sure you want to proceed with this action?");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        try {
            orderService.deleteOrderById(id);
            response.put("message", "Order deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error deleting order.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PutMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> updateOrder(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        try {
            String status = (String) updates.getOrDefault("status", "Pending");
            String address = (String) updates.get("address");
            Double totalPrice = ((Number) updates.get("totalPrice")).doubleValue();

            orderService.updateOrder(id, address, status, totalPrice);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Order updated successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error updating order."));
        }
    }




    //USER




    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Order not found"));
        }

        Order order = orderOpt.get();

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access denied"));
        }

        return ResponseEntity.ok(order);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestBody String newStatus) {
        Optional<Order> existingOrder = orderService.getOrderById(id);

        if (existingOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        orderService.updateOrderStatus(id, newStatus.trim());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestBody Map<String, Long> request, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();


        if (!request.containsKey("dishId")) {
            throw new RuntimeException("Missing 'dishId' in request body");
        }

        Long dishId = request.get("dishId");

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseGet(() -> {
                    Order newCart = new Order(new ArrayList<>(), user, "", "Cart", 0.0);
                    orderService.saveOrder(newCart);
                    return newCart;
                });

        Dish dish = dishService.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        cart.getDishes().add(dish);
        orderService.saveOrder(cart);

        response.put("success", true);
        response.put("message", "Dish added to cart");
        response.put("cartId", cart.getId());

        return response;
    }

    @GetMapping("/cart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> viewCart(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseGet(() -> new Order(new ArrayList<>(), user, "", "Cart", 0.0));

        boolean hasDishes = !cart.getDishes().isEmpty();

        double totalPrice = cart.getDishes().stream()
                .mapToDouble(Dish::getPrice)
                .sum();

        cart.setTotalPrice(totalPrice);
        orderService.saveOrder(cart);

        response.put("success", true);
        response.put("message", "Cart retrieved successfully");
        response.put("cartId", cart.getId());
        response.put("hasDishes", hasDishes);
        response.put("totalPrice", totalPrice);
        response.put("dishes", cart.getDishes());

        return ResponseEntity.ok(response);
    }



    @PostMapping("/cart/clear")
    public ResponseEntity<Map<String, Object>> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseGet(() -> new Order(new ArrayList<>(), user, "", "Cart", 0.0));

        cart.getDishes().clear();
        cart.setTotalPrice(0.0);
        orderService.saveOrder(cart);

        response.put("success", true);
        response.put("message", "Cart cleared successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cart/remove")
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestBody Map<String, Long> request,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        if (!request.containsKey("dishId")) {
            response.put("success", false);
            response.put("message", "Missing 'dishId' in request body");
            return ResponseEntity.badRequest().body(response);
        }

        Long dishId = request.get("dishId");

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        boolean removed = cart.getDishes().removeIf(dish -> dish.getId().equals(dishId));

        if (!removed) {
            response.put("success", false);
            response.put("message", "Dish not found in cart");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        orderService.saveOrder(cart);

        response.put("success", true);
        response.put("message", "Dish removed from cart");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String, Object>> getOrderSummary(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view this order.");
        }

        if ("Paid".equals(order.getStatus())) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/api/v1/orders/" + id + "/more-info")
                    .build();
        }

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        double deliveryCost = 4.99;
        double totalPrice = order.getTotalPrice();
        double finalPrice = Math.round((totalPrice + deliveryCost) * 100.0) / 100.0;


        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getId());
        response.put("dishes", order.getDishes());
        response.put("totalPrice", totalPrice);
        response.put("deliveryCost", deliveryCost);
        response.put("finalPrice", finalPrice);
        response.put("address", order.getAddress());
        response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName()
        ));

        return ResponseEntity.ok(response);
    }


    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getUserOrderHistory(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();


        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<Order> orders = orderService.getPaidOrdersByUserId(user.getId());

        response.put("success", true);
        response.put("message", "Order history retrieved successfully");
        response.put("orders", orders);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Map<String, Object>> updateOrder(@PathVariable Long id,@RequestBody Map<String, Object> updates,@AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized: You can't update this order.");
        }

        if (updates.containsKey("address") && updates.get("address") instanceof String address) {
            order.setAddress(address);
        }
        if (updates.containsKey("status") && updates.get("status") instanceof String status) {
            order.setStatus(status);
        }
        if (updates.containsKey("totalPrice") && updates.get("totalPrice") instanceof Number totalPrice) {
            order.setTotalPrice(totalPrice.doubleValue());
        }

        orderService.saveOrder(order);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order updated successfully");
        response.put("orderId", order.getId());
        response.put("status", order.getStatus());
        response.put("address", order.getAddress());
        response.put("totalPrice", order.getTotalPrice());

        return ResponseEntity.ok(response);
    }














}

