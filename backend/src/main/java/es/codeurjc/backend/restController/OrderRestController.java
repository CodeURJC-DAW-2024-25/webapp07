package es.codeurjc.backend.restController;

import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.mapper.OrderMapper;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.DishService;
import es.codeurjc.backend.service.OrderService;
import es.codeurjc.backend.service.PdfService;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PdfService pdfService;



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
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestBody String newStatus) {
        OrderDTO updatedOrder = orderService.updateOrderStatusChecked(id, newStatus.trim());
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(
            summary = "Add a dish to the cart",
            description = "Adds a dish to the current user's shopping cart. If no cart exists, one will be created.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish added to cart successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Missing 'dishId' in request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "User or Dish not found", content = @Content)
    })
    @PostMapping("/cart")
    public ResponseEntity<OrderDTO> addToCart(@RequestBody Map<String, Long> request,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        if (!request.containsKey("dishId")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing 'dishId' in request body");
        }
        Long dishId = request.get("dishId");
        OrderDTO cartDTO = orderService.addDishToUserCart(dishId, userDetails.getUsername());
        return ResponseEntity.ok(cartDTO);
    }

    @Operation(
            summary = "View current cart",
            description = "Retrieves the current user's shopping cart. If none exists, an empty cart is returned.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/cart")
    public ResponseEntity<OrderDTO> viewCart(@AuthenticationPrincipal UserDetails userDetails) {
        OrderDTO cartDTO = orderService.viewCartForUser(userDetails.getUsername());
        return ResponseEntity.ok(cartDTO);
    }


    @Operation(
            summary = "Clear user's cart",
            description = "Removes all dishes from the user's shopping cart.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/cart/dish")
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestParam Long dishId,@AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> response = orderService.clearUserCart(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Clear user's cart",
            description = "Removes all dishes from the user's shopping cart.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String,Object>> getOrderSummary(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Map<String,Object> summary = orderService.getOrderSummaryDTOById(id, userDetails.getUsername());
        return ResponseEntity.ok(summary);
    }


    @Operation(
            summary = "Get user order history",
            description = "Returns all 'Paid' orders for the authenticated user.",
            tags = {"Orders"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order history retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getUserOrderHistory(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderDTO> orderDTOs = orderService.getPaidOrderDTOsByUserId(user.getId());

        response.put("success", true);
        response.put("message", "Order history retrieved successfully");
        response.put("orders", orderDTOs);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Update order status and address",
            description = "Updates the status and address of an existing order. Accessible by the order's owner.",
            tags = {"Orders"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized to update this order", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderStatusAndAddress(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates) {

        String status = updates.getOrDefault("status", "").trim();
        String address = updates.getOrDefault("address", "").trim();

        OrderDTO updatedOrder = orderService.updateOrderStatusAndAddressChecked(id, status, address);
        return ResponseEntity.ok(updatedOrder);
    }


    @GetMapping("/{id}/invoice")
    public void downloadInvoice(
            @PathVariable Long id,
            HttpServletResponse response,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {

        // Obtener datos necesarios
        OrderDTO orderDTO = orderService.getOrderDTOByIdForUser(id, userDetails.getUsername());
        UserDTO userDTO = userService.findUserDtoByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Generar PDF usando el servicio
        byte[] pdfBytes = pdfService.generateInvoicePdf(orderDTO, userDTO);

        // Configurar respuesta HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_" + orderDTO.id() + ".pdf");
        response.getOutputStream().write(pdfBytes);
    }

    @PostMapping("/cart/remove")
    public ResponseEntity<Void> removeDishFromCart(
            @RequestParam Long dishId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        orderService.removeDishFromCart(user.getId(), dishId);

        return ResponseEntity.noContent().build(); // 204 No Content
    }
}

