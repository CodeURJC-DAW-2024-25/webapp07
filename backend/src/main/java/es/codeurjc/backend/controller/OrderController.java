package es.codeurjc.backend.controller;

import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

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
}
