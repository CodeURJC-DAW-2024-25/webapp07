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
    @GetMapping("/aboutUs")
    public String showAboutUs(Model model) {
        return "about-us";
    }
    @GetMapping("/header")
    public String showHeader(Model model) {
        return "fragments/header";
    }
    @GetMapping("/footer")
    public String showFooter(Model model) {
        return "fragments/footer";
    }
    @GetMapping("/header-admin")
    public String showHeaderAdmin(Model model) {
        return "fragments/header-admin";
    }

}
