package es.codeurjc.backend.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("pageTitle", "Error");
        model.addAttribute("errorMessage", "Oops! The page you are looking for does not exist.");

        return "error/error";
    }
}