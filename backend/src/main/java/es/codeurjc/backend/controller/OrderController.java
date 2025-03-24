package es.codeurjc.backend.controller;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.DishService;
import es.codeurjc.backend.service.OrderService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private DishService dishService;
    /**
     * Displays the user's cart.
     *
     * @param model The model to pass attributes to the view.
     * @param userDetails The details of the authenticated user.
     * @return The view name for the cart page.
     */
    @GetMapping("/cart")
    public String viewCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseGet(() -> new Order(new ArrayList<>(), user, "", "Cart",0.0));

        boolean hasDishes = !cart.getDishes().isEmpty();
        double totalPrice = cart.getDishes().stream()
                .mapToDouble(Dish::getPrice)
                .sum();

        cart.setTotalPrice(totalPrice);
        orderService.saveOrder(cart);

        model.addAttribute("pageTitle", "Cart");

        model.addAttribute("hasDishes", hasDishes);
        model.addAttribute("orders", cart);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    /**
     * Clears the user's cart.
     *
     * @param userDetails The details of the authenticated user.
     * @return Redirects to the cart page.
     */
    @PostMapping("/cart/clear")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseGet(() -> new Order(new ArrayList<>(), user, "", "Cart",0.0));

        cart.getDishes().clear();
        orderService.saveOrder(cart);

        return "redirect:/orders/cart";
    }

    /**
     * Adds a dish to the user's cart.
     *
     * @param dishId The ID of the dish to be added to the cart.
     * @param userDetails The details of the authenticated user.
     * @return A map containing the success status and a message.
     */
    @PostMapping("/cart/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam Long dishId, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseGet(() -> new Order(new ArrayList<>(), user, "", "Cart", 0.0));

        Dish dish = dishService.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        cart.getDishes().add(dish);
        orderService.saveOrder(cart);

        response.put("success", true);
        response.put("message", "Dish added to cart");

        return response;
    }

    /**
     * Removes a dish from the user's cart.
     *
     * @param dishId The ID of the dish to be removed from the cart.
     * @param userDetails The details of the authenticated user.
     * @return Redirects to the cart page.
     */
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long dishId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<Dish> dishes = cart.getDishes();
        for (int i = 0; i < dishes.size(); i++) {
            if (dishes.get(i).getId().equals(dishId)) {
                dishes.remove(i);
                break;
            }
        }

        orderService.saveOrder(cart);

        return "redirect:/orders/cart";
    }

    /**
     * Displays the pickup and delivery options page.
     *
     * @param model The model to pass attributes to the view.
     * @return The view name for the pickup and delivery options page.
     */
    @GetMapping("/orders/pickup-delivery-order")
    public String showPickupOption(Model model){


        return "pickup-delivery-order";
    }

    /**
     * Displays the order history for the authenticated user.
     *
     * @param model The model to pass attributes to the view.
     * @return The view name for the user order history page, or redirects to the login page if the user is not authenticated.
     */
    @GetMapping("/history")
    public String showUserOrderHistory(Model model) {
        Optional<User> userOpt = userService.getAuthenticatedUser();

        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();

            //search order for this user
            List<Order> orders = orderService.getPaidOrdersByUserId(userId);
            model.addAttribute("orders", orders);

            model.addAttribute("pageTitle", "Order history");

            return "user-order-history";
        }

        return "redirect:/login";
    }

    /**
     * Displays detailed information about a specific order.
     *
     * @param id The ID of the order to display more information about.
     * @param model The model to pass attributes to the view.
     * @return The view name for the order information page, or redirects to the error page if the order is not found.
     */
    @GetMapping("/{id}/more-info")
    public String showOrderMoreInfo(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!"Paid".equals(order.getStatus())) {
            return "redirect:/orders/" + id + "/summary";
        }

        double deliveryCost = 4.99;
        double totalPrice = order.getTotalPrice();
        double finalPrice = Math.round((totalPrice + deliveryCost) * 100.0) / 100.0;

        model.addAttribute("id", order.getId());
        model.addAttribute("dishes", order.getDishes());
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("deliveryCost", deliveryCost);
        model.addAttribute("finalPrice", finalPrice);
        model.addAttribute("address", order.getAddress());

        model.addAttribute("pageTitle", "Order information");

        return "order-more-info";

    }

    /**
     * Displays the summary of a specific order.
     *
     * @param id The ID of the order to display the summary for.
     * @param model The model to pass attributes to the view.
     * @param userDetails The details of the authenticated user.
     * @return The view name for the order summary page, or redirects to the error page if the order is not found.
     */
    @GetMapping("/{id}/summary")
    public String showOrderSummary(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if ("Paid".equals(order.getStatus())) {
            return "redirect:/orders/" + id + "/more-info";
        }

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        double deliveryCost = 4.99;
        double totalPrice = order.getTotalPrice();
        double finalPrice = Math.round((totalPrice + deliveryCost) * 100.0) / 100.0;

        // transfer the data to the view
        model.addAttribute("id", order.getId());
        model.addAttribute("dishes", order.getDishes());
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("deliveryCost", deliveryCost);
        model.addAttribute("finalPrice", finalPrice);
        model.addAttribute("address", order.getAddress());

        model.addAttribute("pageTitle", " Order summary" );
        model.addAttribute("user", user);
        return "order-summary"; // show mustache
    }

    /**
     * Displays the confirmation page for a specific order.
     *
     * @param id The ID of the order to display the confirmation for.
     * @param model The model to pass attributes to the view.
     * @return The view name for the order confirmation page, or redirects to the error page if the order is not found.
     */
    @GetMapping("/{id}/confirmation")
    public String showOrderConfirmation(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if ("Paid".equals(order.getStatus())) {
            return "redirect:/orders/" + id + "/more-info";
        }

        if (orderOpt.isPresent()) {
            model.addAttribute("order", order);
            return "order-confirmation";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/{id}/pay")
    public String showPayPage(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if ("Paid".equals(order.getStatus())) {
            return "redirect:/orders/" + id + "/more-info";
        } else {
            return "redirect:/orders/" + id + "/summary";
        }
    }

    /**
     * Processes the payment for a specific order.
     *
     * @param id The ID of the order to be paid.
     * @param model The model to pass attributes to the view.
     * @return Redirects to the success page for the order if payment is successful, or redirects to the error page if the order is not found.
     */
    @PostMapping("/{id}/pay")
    public String payOrder(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            orderService.updateOrderStatus(id, "Paid");

            model.addAttribute("order", order);
            return "redirect:/orders/" + id + "/success";
        } else {
            return "redirect:/error";
        }
    }

    /**
     * Displays the success page for a specific order.
     *
     * @param id The ID of the order to display the success page for.
     * @param model The model to pass attributes to the view.
     * @return The view name for the order success page, or redirects to the error page if the order is not found.
     */
    @GetMapping("/{id}/success")
    public String orderSuccess(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (orderOpt.isPresent()) {
            model.addAttribute("order", order);
            return "order-success";
        } else {
            return "redirect:/error";
        }
    }

    /**
     * Updates the details of a specific order.
     *
     * @param orderId The ID of the order to be updated.
     * @param address The new address for the order (optional).
     * @param status The new status for the order (optional).
     * @param totalPrice The new total price for the order (optional).
     * @param userDetails The details of the authenticated user.
     * @return Redirects to the confirmation page for the updated order.
     * @throws RuntimeException If the user or order is not found, or if the user is unauthorized to update the order.
     */
    @PostMapping("/update")
    public String updateOrder(@RequestParam Long orderId,
                              @RequestParam(required = false) String address,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) Double totalPrice,
                              @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized: You can't update this order.");
        }

        if (address != null && !address.isBlank()) {
            order.setAddress(address);
        }
        if (status != null && !status.isBlank()) {
            order.setStatus(status);
        }
        if (totalPrice != null) {
            order.setTotalPrice(totalPrice);
        }

        orderService.saveOrder(order);

        return "redirect:/orders/"+ orderId +"/confirmation";
    }

    /**
     * Generates and downloads a PDF invoice for a specific order.
     *
     * @param id The ID of the order to generate the invoice for.
     * @param response The HTTP response to write the PDF to.
     * @throws IOException If an input or output exception occurs.
     */
    @GetMapping("/{id}/invoice")
    public void downloadInvoice(@PathVariable Long id, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }

        Order order = orderOpt.get();

        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to view this invoice");
            return;
        }

        double deliveryCost = 4.99;
        double finalPrice = order.getTotalPrice() + deliveryCost;

        // Configurar respuesta HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_" + order.getId() + ".pdf");

        // Crear documento PDF
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.setMargins(20, 20, 20, 20);

        // "Voltereta Croqueta"
        Paragraph header = new Paragraph("Voltereta Croqueta")
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(15, 23, 43))
                .setFontColor(new DeviceRgb(254, 161, 22))
                .setFontSize(32)
                .simulateBold()
                .setMarginTop(0)
                .setMarginLeft(0)
                .setMarginRight(0)
                .setMarginBottom(20);
        document.add(header);
        document.add(new Paragraph(" ").setFontSize(8)); // Space

        // Agregar título
        Paragraph title = new Paragraph("Order Nº: " + order.getId())
                .setTextAlignment(TextAlignment.CENTER)
                .simulateBold()
                .setFontSize(20);
        document.add(title);
        document.add(new Paragraph(" ").setFontSize(8)); // Espacio

        // Add user information table
        String userName = order.getUser().getUsername();
        Optional<User> userOpt = userService.findByUsername(userName);
        User user = userOpt.get();

        Table userTable = new Table(new float[]{2, 4})
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        userTable.addCell(new Cell().add(new Paragraph("Name:").simulateBold()));
        userTable.addCell(new Cell().add(new Paragraph(user.getFirstName() + " " + user.getLastName())));

        userTable.addCell(new Cell().add(new Paragraph("Address:").simulateBold()));
        userTable.addCell(new Cell().add(new Paragraph(order.getAddress())));

        document.add(userTable);
        document.add(new Paragraph(" ").setFontSize(8)); // Space

        // Tabla de pedidos
        Table table = new Table(new float[]{4, 2})
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addHeaderCell(new Cell().add(new Paragraph("Dish").simulateBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Price (€)").simulateBold()));

        for (Dish dish : order.getDishes()) {
            table.addCell(new Cell().add(new Paragraph(dish.getName())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", (double) dish.getPrice())))
                    .setTextAlignment(TextAlignment.RIGHT));
        }

        document.add(table);
        document.add(new Paragraph(" ").setFontSize(8)); // Espacio

        // Subtotal, Delivery y Total
        Table summaryTable = new Table(new float[]{4, 2})
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        summaryTable.addCell(new Cell().add(new Paragraph("Subtotal:")));
        summaryTable.addCell(new Cell().add(new Paragraph("€" + String.format("%.2f", order.getTotalPrice())))
                .setTextAlignment(TextAlignment.RIGHT));

        summaryTable.addCell(new Cell().add(new Paragraph("Delivery:")));
        summaryTable.addCell(new Cell().add(new Paragraph("€4.99"))
                .setTextAlignment(TextAlignment.RIGHT));

        summaryTable.addCell(new Cell().add(new Paragraph("Total:").simulateBold()));
        summaryTable.addCell(new Cell().add(new Paragraph("€" + String.format("%.2f", finalPrice)).simulateBold())
                .setTextAlignment(TextAlignment.RIGHT));

        document.add(summaryTable);

        //footer
        Paragraph footer = new Paragraph("© 2025 Voltereta Croqueta. All rights reserved.\nCalle Gran Vía 10 Madrid\nVoltereta Croqueta")
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setFontSize(10);
        document.add(new Paragraph(" ").setFontSize(8)); // Space
        document.add(footer);

        // Cerrar documento
        document.close();

    }
}