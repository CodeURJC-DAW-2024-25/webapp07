package es.codeurjc.backend.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Custom error controller to handle different HTTP error statuses.
 * Displays user-friendly error messages based on the error code.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handles errors and displays an appropriate error page.
     * Supports specific handling for 404 (Not Found) and 403 (Forbidden) errors.
     *
     * @param request The HTTP request containing error details.
     * @param model   The model to pass attributes to the view.
     * @return The view name for the error page.
     */
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("pageTitle", "Error");
        model.addAttribute("extraButton", "");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == 404) {
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorMessage", "Page not found");
                model.addAttribute("errorDescription", "Oops! The page you are looking for does not exist.");
                return "error/error";
            } else if (statusCode == 403) {
                model.addAttribute("errorCode", "403");
                model.addAttribute("errorMessage", "Forbidden");
                model.addAttribute("errorDescription", "You don't have permission to access this page.");
                model.addAttribute("extraButton", "<p class=\"my-4\">or</p>\n" + "<a class=\"btn btn-primary py-3 px-5\" href=\"/login\">Log in</a>");
                return "error/error";
            }
        }

        model.addAttribute("pageTitle", "Error");
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error/error";
    }
}
