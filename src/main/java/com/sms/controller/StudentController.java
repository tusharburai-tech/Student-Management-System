package com.sms.controller;

import com.sms.model.Department;
import com.sms.model.Gender;
import com.sms.model.Student;
import com.sms.model.StudentStatus;
import com.sms.service.AttendanceService;
import com.sms.service.CourseService;
import com.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final AttendanceService attendanceService;

    public StudentController(StudentService studentService, CourseService courseService, AttendanceService attendanceService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.attendanceService = attendanceService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String listStudents(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "department", required = false) Department department,
            @RequestParam(name = "status", required = false) StudentStatus status,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "createdAt") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            Model model) {

        Page<Student> studentPage = studentService.getFilteredStudents(keyword, department, status, year, page, size, sortField, sortDir);

        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedDept", department);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedYear", year);

        model.addAttribute("departments", Department.values());
        model.addAttribute("statuses", StudentStatus.values());
        model.addAttribute("activeNav", "students");

        return "students/students";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("student")) {
            Student student = new Student();
            student.setStudentId(studentService.generateNextStudentId());
            student.setEnrollmentDate(LocalDate.now());
            model.addAttribute("student", student);
        }
        populateFormReferenceData(model);
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "students");
        return "students/add-student";
    }

    @PostMapping("/add")
    public String processAddStudent(@Valid @ModelAttribute("student") Student student,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        // Check for duplicate Student ID
        if (student.getStudentId() != null && studentService.existsByStudentId(student.getStudentId())) {
            bindingResult.rejectValue("studentId", "duplicate", "Student ID '" + student.getStudentId() + "' is already in use.");
        }

        // Check for duplicate Email
        if (student.getEmail() != null && studentService.existsByEmail(student.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email address '" + student.getEmail() + "' is already registered.");
        }

        if (bindingResult.hasErrors()) {
            populateFormReferenceData(model);
            model.addAttribute("isEdit", false);
            model.addAttribute("activeNav", "students");
            return "students/add-student";
        }

        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student " + student.getFullName() + " registered successfully!");
            return "redirect:/students";
        } catch (Exception ex) {
            populateFormReferenceData(model);
            model.addAttribute("isEdit", false);
            model.addAttribute("activeNav", "students");
            model.addAttribute("errorMessage", "Error saving student: " + (ex.getMessage() != null ? ex.getMessage() : "Database error occurred."));
            return "students/add-student";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found with ID: " + id);
            return "redirect:/students";
        }

        model.addAttribute("student", studentOpt.get());
        populateFormReferenceData(model);
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "students");
        return "students/edit-student";
    }

    @PostMapping("/edit/{id}")
    public String processEditStudent(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute("student") Student student,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        // Check for duplicate Student ID excluding current student
        if (student.getStudentId() != null && studentService.existsByStudentIdAndIdNot(student.getStudentId(), id)) {
            bindingResult.rejectValue("studentId", "duplicate", "Student ID '" + student.getStudentId() + "' is already in use.");
        }

        // Check for duplicate Email excluding current student
        if (student.getEmail() != null && studentService.existsByEmailAndIdNot(student.getEmail(), id)) {
            bindingResult.rejectValue("email", "duplicate", "Email address '" + student.getEmail() + "' is already registered.");
        }

        if (bindingResult.hasErrors()) {
            populateFormReferenceData(model);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeNav", "students");
            return "students/edit-student";
        }

        try {
            student.setId(id);
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student " + student.getFullName() + " updated successfully!");
            return "redirect:/students/" + id;
        } catch (Exception ex) {
            populateFormReferenceData(model);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeNav", "students");
            model.addAttribute("errorMessage", "Error updating student: " + (ex.getMessage() != null ? ex.getMessage() : "Database error occurred."));
            return "students/edit-student";
        }
    }

    @GetMapping("/{id}")
    public String viewStudentProfile(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found with ID: " + id);
            return "redirect:/students";
        }

        Student student = studentOpt.get();
        model.addAttribute("student", student);
        model.addAttribute("attendanceRecords", attendanceService.getAttendanceByStudent(student.getId()));
        model.addAttribute("activeNav", "students");
        return "students/student-profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student removed successfully.");
        return "redirect:/students";
    }

    private void populateFormReferenceData(Model model) {
        model.addAttribute("departments", Department.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("statuses", StudentStatus.values());
        model.addAttribute("courses", courseService.getAllCourses());
    }
}
