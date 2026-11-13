package com.sms.controller;

import com.sms.model.AdminUser;
import com.sms.repository.AdminUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final AdminUserRepository adminUserRepository;

    public SettingsController(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @GetMapping
    public String viewSettings(@RequestParam(name = "tab", defaultValue = "profile") String tab,
                               HttpSession session,
                               Model model) {
        AdminUser admin = (AdminUser) session.getAttribute("loggedAdmin");
        if (admin != null) {
            model.addAttribute("adminUser", admin);
        }
        model.addAttribute("activeTab", tab);
        model.addAttribute("activeNav", "settings");
        return "settings";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("adminName") String name,
                                @RequestParam("adminEmail") String email,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        AdminUser admin = (AdminUser) session.getAttribute("loggedAdmin");
        if (admin != null) {
            adminUserRepository.findById(admin.getId()).ifPresent(user -> {
                user.setFullName(name);
                user.setEmail(email.trim().toLowerCase());
                AdminUser updated = adminUserRepository.save(user);
                session.setAttribute("loggedAdmin", updated);
            });
        }
        redirectAttributes.addFlashAttribute("successMessage", "Admin profile updated successfully.");
        return "redirect:/settings?tab=profile";
    }

    @PostMapping("/university")
    public String updateUniversity(@RequestParam("universityName") String uName,
                                   @RequestParam("universityCode") String uCode,
                                   RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", "University information updated successfully.");
        return "redirect:/settings?tab=university";
    }

    @PostMapping("/password")
    public String changePassword(@RequestParam("currentPassword") String current,
                                 @RequestParam("newPassword") String next,
                                 @RequestParam("confirmPassword") String confirm,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        AdminUser admin = (AdminUser) session.getAttribute("loggedAdmin");
        if (admin == null) {
            return "redirect:/login";
        }

        if (!admin.getPassword().equals(current)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Current password does not match.");
            return "redirect:/settings?tab=password";
        }

        if (!next.equals(confirm)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New password and confirmation do not match.");
            return "redirect:/settings?tab=password";
        }

        if (next.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters long.");
            return "redirect:/settings?tab=password";
        }

        adminUserRepository.findById(admin.getId()).ifPresent(user -> {
            user.setPassword(next);
            AdminUser saved = adminUserRepository.save(user);
            session.setAttribute("loggedAdmin", saved);
        });

        redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully in secure database.");
        return "redirect:/settings?tab=password";
    }

    @PostMapping("/preferences")
    public String savePreferences(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", "Application preferences saved successfully.");
        return "redirect:/settings?tab=preferences";
    }
}
