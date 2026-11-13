package com.sms.controller;

import com.sms.model.Department;
import com.sms.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("recentStudents", dashboardService.getRecentStudents());
        model.addAttribute("recentActivities", dashboardService.getRecentActivities());
        model.addAttribute("deptDistribution", dashboardService.getDepartmentDistribution());
        model.addAttribute("departments", Department.values());

        // Format today's date for display
        LocalDate today = LocalDate.now();
        model.addAttribute("currentDateFormatted", today.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        model.addAttribute("activeNav", "dashboard");

        return "dashboard";
    }
}
