package com.sms.controller;

import com.sms.model.AdminUser;
import com.sms.repository.AdminUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final AdminUserRepository adminUserRepository;

    public AuthController(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            HttpSession session,
                            Model model) {
        // If already logged in, redirect to dashboard
        if (session != null && session.getAttribute("loggedAdmin") != null) {
            return "redirect:/dashboard";
        }

        if ("unauthorized".equals(error)) {
            model.addAttribute("errorMessage", "Access Denied. Please sign in with valid university credentials to proceed.");
        } else if (error != null) {
            model.addAttribute("errorMessage", "Invalid email address or password. Please try again.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("errorMessage", "Please provide both university email and password.");
            model.addAttribute("enteredEmail", email);
            return "login";
        }

        Optional<AdminUser> adminOpt = adminUserRepository.findByEmail(email.trim().toLowerCase());
        if (adminOpt.isPresent()) {
            AdminUser admin = adminOpt.get();
            if (admin.getPassword().equals(password.trim())) {
                // Successful authentication
                session.setAttribute("loggedAdmin", admin);
                return "redirect:/dashboard";
            }
        }

        // Invalid credentials
        model.addAttribute("errorMessage", "Invalid email address or password. Please check your credentials.");
        model.addAttribute("enteredEmail", email);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }
}
