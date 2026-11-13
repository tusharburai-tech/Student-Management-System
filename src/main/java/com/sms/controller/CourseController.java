package com.sms.controller;

import com.sms.model.Course;
import com.sms.model.Department;
import com.sms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String listCourses(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                              @RequestParam(value = "department", required = false) Department department,
                              Model model) {
        model.addAttribute("courses", courseService.searchCourses(keyword, department));
        model.addAttribute("totalCourses", courseService.getTotalCourses());
        model.addAttribute("activeCourses", courseService.getActiveCoursesCount());
        model.addAttribute("totalStudentsEnrolled", courseService.getTotalEnrolledStudents());
        model.addAttribute("departments", Department.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedDept", department);
        model.addAttribute("newCourse", new Course());
        model.addAttribute("activeNav", "courses");

        return "courses/courses";
    }

    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        if (!model.containsAttribute("newCourse")) {
            model.addAttribute("newCourse", new Course());
        }
        model.addAttribute("departments", Department.values());
        model.addAttribute("activeNav", "courses");
        return "courses/add-course";
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute("newCourse") Course course,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please correct the errors in the course form.");
            return "redirect:/courses";
        }
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course " + course.getCourseName() + " (" + course.getCourseCode() + ") created successfully!");
        return "redirect:/courses";
    }

    @PostMapping("/edit/{id}")
    public String editCourse(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("course") Course course,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating course. Please check inputs.");
            return "redirect:/courses";
        }
        course.setId(id);
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course " + course.getCourseName() + " updated successfully!");
        return "redirect:/courses";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("successMessage", "Course removed successfully.");
        return "redirect:/courses";
    }
}
