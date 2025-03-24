package es.codeurjc.backend.controller.admin;

import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.model.Order;
import es.codeurjc.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;

@Controller
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/orders")
    public String showOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrdersAsDTOList();

        model.addAttribute("orders", orders);
        model.addAttribute("hasOrders", !orders.isEmpty());

        model.addAttribute("modalId", "confirmationModal");
        model.addAttribute("confirmButtonId", "confirmAction");
        model.addAttribute("modalMessage", "Are you sure you want to proceed with this action?");

        return "admin/manage-orders";
    }


    /**
     * Deletes an order by its ID.
     *
     * @param orderId The ID of the order to delete.
     * @param redirectAttributes Redirect attributes to show messages.
     * @return Redirects back to the order management page.
     */
    @PostMapping("/admin/orders/delete")
    public String deleteOrder(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrderAndReturnResponse(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting order.");
        }
        return "redirect:/admin/orders";
    }


    /**
     * Show edit form for an order.
     */
    @GetMapping("/admin/orders/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<OrderDTO> orderOpt = orderService.findOrderDtoById(id);

        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "edit-order";
        } else {
            return "redirect:/admin/orders?error=Order not found";
        }
    }


    @PostMapping("/admin/orders/update")
    public String updateOrder(@RequestParam Long orderId,
                              @RequestParam String address,
                              @RequestParam String status,
                              @RequestParam Double totalPrice,
                              RedirectAttributes redirectAttributes) {

        try {
            OrderDTO dto = new OrderDTO(
                    orderId,
                    null, // dishes
                    null, // user
                    null, // orderDate
                    address,
                    status,
                    totalPrice
            );

            orderService.updateOrderFromAdmin(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Order updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order.");
        }

        return "redirect:/admin/orders";
    }



}
