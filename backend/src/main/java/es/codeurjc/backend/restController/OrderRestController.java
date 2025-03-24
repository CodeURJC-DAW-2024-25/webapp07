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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get all orders",
            description = "Retrieves a list of all orders in the system. Useful for admins or dashboards.",
            tags = {"Orders"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersAsDTOMap());
    }




    @Operation(
            summary = "Delete an order by ID",
            description = "Deletes an order from the database using its unique ID. Only accessible to admins.",
            tags = {"Orders"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error deleting order",
                    content = @Content)
    })
    @Parameter(name = "id", description = "ID of the order to delete", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        try {
            Map<String, String> response = orderService.deleteOrderAndReturnResponse(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting order."));
        }
    }




    @Operation(
            summary = "Update an order by ID",
            description = "Updates the address, status, or total price of an existing order. Admin access required.",
            tags = {"Orders"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error during update",
                    content = @Content)
    })
    @Parameter(name = "id", description = "Order ID to update", required = true)

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateOrder(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            Map<String, String> response = orderService.updateOrderFromMap(id, updates);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error updating order."));
        }
    }


    //USER

    @Operation(
            summary = "Get an order by ID",
            description = "Retrieves the details of a specific order by its ID. Only the user who placed the order can access it.",
            tags = {"Orders"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied for this order",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content)
    })
    @Parameter(name = "id", description = "Order ID to retrieve", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        OrderDTO orderDTO = orderService.getOrderDTOByIdForUser(id, userDetails.getUsername());
        return ResponseEntity.ok(orderDTO);
    }


    @Operation(
            summary = "Update order status",
            description = "Allows updating the status of an existing order. Admin or user with proper permissions required.",
            tags = {"Orders"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @Parameter(name = "id", description = "Order ID", required = true)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestBody String newStatus) {
        orderService.updateOrderStatusChecked(id, newStatus.trim());
        return ResponseEntity.noContent().build();
    }
























}

