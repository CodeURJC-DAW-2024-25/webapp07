package es.codeurjc.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling requests related to Mustache templates.
 * It manages authentication-based views such as the index page, login error page,
 * about us, and FAQs.
 */
@Controller
public class MustacheController {

    /**
     * Displays the index page and determines if the user is authenticated.
     *
     * @param model The model to pass attributes to the view.
     * @return The index view name.
     */
    @GetMapping("/")
    public String showIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

        model.addAttribute("isAuthenticated", isAuthenticated);
        return "index";
    }

    /**
     * Displays the login error page.
     *
     * @param model The model to pass attributes to the view.
     * @return The login error view name.
     */
    @GetMapping("/login/error")
    public String showLoginError(Model model) {
        return "login-error";
    }

    /**
     * Displays the "About Us" page.
     *
     * @param model The model to pass attributes to the view.
     * @return The about-us view name.
     */
    @GetMapping("/aboutUs")
    public String showAboutUs(Model model) {
        model.addAttribute("pageTitle", "About us");
        return "about-us";
    }

    /**
     * Displays the Frequently Asked Questions (FAQs) page.
     *
     * @param model The model to pass attributes to the view.
     * @return The FAQs view name.
     */
    @GetMapping("/faqs")
    public String showFaqs(Model model) {
        model.addAttribute("pageTitle", "Frequently Asked Questions");
        return "faqs";
    }

    /*
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
    */
}
