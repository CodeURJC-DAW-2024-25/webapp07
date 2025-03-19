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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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





















}

