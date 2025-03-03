package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.model.Dish;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.OrderService;
import es.codeurjc.backend.service.DishService;
import es.codeurjc.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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


    @PostMapping("/orders/add_dish")
    public String showPickupOption(Long id, Model model, @AuthenticationPrincipal User user){
        Optional<Dish> dish = dishService.findById(id);
        List<Dish> cart = user.getCart();

        if (dish.isPresent()) {
            cart.add(dish.get());
        } else {
            throw new IllegalArgumentException("Dish not found for ID: " + id);
        }
        user.setCart(cart);
        System.out.println(cart);
        userService.updateUser(user);

        return "redirect:/menu";
    }

    @GetMapping("/history")
    public String showUserOrderHistory(Model model) {
        Optional<User> userOpt = userService.getAuthenticatedUser();

        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();

            //search order for this user
            List<Order> orders = orderService.getPaidOrdersByUserId(userId);
            model.addAttribute("orders", orders);

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

            // Transferir datos a la vista
            model.addAttribute("id", order.getId());
            model.addAttribute("dishes", order.getDishes());
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("deliveryCost", deliveryCost);
            model.addAttribute("finalPrice", finalPrice);
            model.addAttribute("address", order.getAddress());

            return "order-more-info"; // Renderiza la nueva plantilla
        } else {
            return "redirect:/error404";
        }
    }


    @GetMapping("/{id}/summary")
    public String showOrderSummary(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(id); //search in database.

        if (orderOpt.isPresent()) { //verify the user autenticated is the owner of the order.
            Order order = orderOpt.get();

            double deliveryCost = 4.99;
            double totalPrice = order.getTotalPrice();
            double finalPrice = totalPrice + deliveryCost;


            // transfer the data to the view
            model.addAttribute("id", order.getId());
            model.addAttribute("dishes", order.getDishes());
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("deliveryCost", deliveryCost);
            model.addAttribute("finalPrice", finalPrice);
            model.addAttribute("address", order.getAddress());

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





}
