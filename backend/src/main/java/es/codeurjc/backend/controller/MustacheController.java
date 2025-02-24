package es.codeurjc.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MustacheController {

    @GetMapping("/")
    public String showIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

        model.addAttribute("isAuthenticated", isAuthenticated);
        return "index";
    }
    @GetMapping("/login/error")
    public String showLoginError(Model model) {
        return "login-error";
    }
    @GetMapping("/aboutUs")
    public String showAboutUs(Model model) {
        model.addAttribute("pageTitle", "About us");
        return "about-us";
    }
    @GetMapping("/aboutUs")
    public String showFaqs(Model model) {
        model.addAttribute("pageTitle", "Frecuently Asked Questions");
        return "faqs";
    }
//    @GetMapping("/header")
//    public String showHeader(Model model) {
//        return "fragments/header";
//    }
//    @GetMapping("/footer")
//    public String showFooter(Model model) {
//        return "fragments/footer";
//    }
//    @GetMapping("/header-admin")
//    public String showHeaderAdmin(Model model) {
//        return "fragments/header-admin";
//    }

}
