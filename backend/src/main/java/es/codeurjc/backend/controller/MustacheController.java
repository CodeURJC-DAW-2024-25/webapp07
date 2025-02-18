package es.codeurjc.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MustacheController {

    @GetMapping("/")
    public String showIndex(Model model) {
        return "index";
    }
    @GetMapping("/auth/login/error")
    public String showLoginError(Model model) {
        return "login-error";
    }
}
