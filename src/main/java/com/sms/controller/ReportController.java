package com.sms.controller;

import com.sms.model.Department;
import com.sms.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String viewReports(@RequestParam(name = "department", required = false) Department department,
                              Model model) {
        Map<String, Object> data = reportService.getReportsData(department);
        model.addAttribute("reportData", data);
        model.addAttribute("selectedDept", department);
        model.addAttribute("departments", Department.values());
        model.addAttribute("reportDate", LocalDate.now());
        model.addAttribute("activeNav", "reports");
        return "reports";
    }

    @GetMapping("/export-csv")
    public void exportCsv(@RequestParam(name = "department", required = false) Department department,
                          HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        String filename = "students_report_" + LocalDate.now() + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        reportService.writeStudentsCsv(response.getWriter(), department);
    }
}
