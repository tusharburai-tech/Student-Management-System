package com.sms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex, Model model) {
        log.error("Database integrity violation: {}", ex.getMessage());
        model.addAttribute("status", 409);
        model.addAttribute("error", "Duplicate Entry");
        model.addAttribute("errorMessage", "A record with this identifier (Student ID or Email) already exists in the system.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        log.error("Unhandled exception caught by GlobalExceptionHandler: ", ex);
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred.");
        return "error";
    }
}
