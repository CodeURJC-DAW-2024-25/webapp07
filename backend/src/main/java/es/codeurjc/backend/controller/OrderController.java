package es.codeurjc.backend.controller;

import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.DishService;
import es.codeurjc.backend.service.OrderService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long dishId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order cart = orderService.findCartByUser(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getDishes().removeIf(dish -> dish.getId().equals(dishId));
        orderService.saveOrder(cart);

        return "redirect:/orders/cart";
    }




    @GetMapping("/orders/pickup-delivery-order")
    public String showPickupOption(Model model){


        return "pickup-delivery-order";
    }

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

    @GetMapping("/{id}/more-info")
    public String showOrderMoreInfo(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();

            double deliveryCost = 4.99;
            double totalPrice = order.getTotalPrice();
            double finalPrice = totalPrice + deliveryCost;

            model.addAttribute("id", order.getId());
            model.addAttribute("dishes", order.getDishes());
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("deliveryCost", deliveryCost);
            model.addAttribute("finalPrice", finalPrice);
            model.addAttribute("address", order.getAddress());

            model.addAttribute("pageTitle", "Order information");

            return "order-more-info";
        } else {
            return "redirect:/error404";
        }
    }


    @GetMapping("/{id}/summary")
    public String showOrderSummary(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();

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
        } else {
            return "redirect:/error 404";
        }
    }

    @GetMapping("/{id}/confirmation")
    public String showOrderConfirmation(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            model.addAttribute("order", order);
            return "order-confirmation";
        } else {
            return "redirect:/error404";
        }
    }

    @PostMapping("/{id}/pay")
    public String payOrder(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            orderService.updateOrderStatus(id, "Paid");

            model.addAttribute("order", order);
            return "redirect:/orders/" + id + "/success";
        } else {
            return "redirect:/error404";
        }
    }



    @GetMapping("/{id}/success")
    public String orderSuccess(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            model.addAttribute("order", order);
            return "order-success";
        } else {
            return "redirect:/error404";
        }
    }


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

    @GetMapping("/{id}/invoice")
    public void downloadInvoice(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Order> orderOpt = orderService.getOrderById(id);

        if (orderOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }

        Order order = orderOpt.get();
        double deliveryCost = 4.99;
        double finalPrice = order.getTotalPrice() + deliveryCost;

        // Configurar respuesta HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_" + order.getId() + ".pdf");

        // Crear documento PDF
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Agregar título
        Paragraph title = new Paragraph("Invoice #" + order.getId())
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(20);
        document.add(title);
        document.add(new Paragraph(" ").setFontSize(8)); // Espacio

        // Agregar dirección
        document.add(new Paragraph("Address: " + order.getAddress())
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(" ").setFontSize(8)); // Espacio

        // Tabla de pedidos
        Table table = new Table(new float[]{4, 2})
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addHeaderCell(new Cell().add(new Paragraph("Dish").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Price (€)").setBold()));

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

        summaryTable.addCell(new Cell().add(new Paragraph("Total:").setBold()));
        summaryTable.addCell(new Cell().add(new Paragraph("€" + String.format("%.2f", finalPrice)).setBold())
                .setTextAlignment(TextAlignment.RIGHT));

        document.add(summaryTable);

        // Cerrar documento
        document.close();
    }


}