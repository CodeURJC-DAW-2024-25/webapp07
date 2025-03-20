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


    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> ordersDTO = orderService.getAllOrders()
                .stream()
                .map(orderMapper::toDto)
                .toList();
        return ResponseEntity.ok(ordersDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrdersById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(orderMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isPresent()) {
            orderService.deleteOrderById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        Optional<Order> existingOrder = orderService.getOrderById(id);

        if (existingOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        orderService.updateOrder(id, orderDTO.address(), orderDTO.status(), orderDTO.totalPrice());

        return ResponseEntity.noContent().build();
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












}

