package com.sms.controller;

import com.sms.model.Attendance;
import com.sms.model.AttendanceStatus;
import com.sms.model.Course;
import com.sms.model.Department;
import com.sms.model.Student;
import com.sms.service.AttendanceService;
import com.sms.service.CourseService;
import com.sms.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final CourseService courseService;

    public AttendanceController(AttendanceService attendanceService, StudentService studentService, CourseService courseService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String viewAttendance(
            @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "department", required = false) Department department,
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "semester", required = false, defaultValue = "") String semester,
            Model model) {

        LocalDate selectedDate = (date != null) ? date : LocalDate.now();

        List<Attendance> records = attendanceService.getFilteredAttendance(selectedDate, department, courseId, semester);

        // If today has no records generated yet, we can load all students to display for marking
        List<Student> students = studentService.getAllStudents();

        Map<String, Long> metrics = attendanceService.getAttendanceMetricsForDate(selectedDate);

        model.addAttribute("records", records);
        model.addAttribute("allStudents", students);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("selectedDept", department);
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedSemester", semester);

        model.addAttribute("metrics", metrics);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("departments", Department.values());
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("activeNav", "attendance");

        return "attendance/attendance";
    }

    @GetMapping("/export-csv")
    public void exportAttendanceCsv(
            @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "department", required = false) Department department,
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "semester", required = false, defaultValue = "") String semester,
            HttpServletResponse response) throws IOException {

        LocalDate selectedDate = (date != null) ? date : LocalDate.now();
        List<Attendance> records = attendanceService.getFilteredAttendance(selectedDate, department, courseId, semester);

        response.setContentType("text/csv");
        String filename = "attendance_" + selectedDate + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        PrintWriter writer = response.getWriter();
        writer.println("Student ID,Student Name,Department,Course,Status,Date,Remarks");
        for (Attendance a : records) {
            writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    a.getStudent().getStudentId(),
                    a.getStudent().getFullName(),
                    a.getStudent().getDepartment() != null ? a.getStudent().getDepartment().getFullName() : "",
                    a.getCourseName() != null ? a.getCourseName() : "",
                    a.getStatus() != null ? a.getStatus().getLabel() : "",
                    a.getAttendanceDate(),
                    a.getRemarks() != null ? a.getRemarks() : ""
            );
        }
    }

    @PostMapping("/mark")
    public String markSingleAttendance(
            @RequestParam("studentId") Long studentId,
            @RequestParam("courseId") Long courseId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam("status") AttendanceStatus status,
            @RequestParam(value = "remarks", required = false) String remarks,
            RedirectAttributes redirectAttributes) {

        studentService.getStudentById(studentId).ifPresent(student -> {
            Course course = courseService.getCourseById(courseId).orElse(null);
            Attendance att = new Attendance(student, course, date != null ? date : LocalDate.now(), status, remarks);
            attendanceService.saveAttendance(att);
        });

        redirectAttributes.addFlashAttribute("successMessage", "Attendance recorded successfully!");
        return "redirect:/attendance?date=" + (date != null ? date : LocalDate.now());
    }

    @PostMapping("/batch-save")
    public String saveBatchAttendance(
            @RequestParam("courseId") Long courseId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam("studentIds") List<Long> studentIds,
            @RequestParam("statuses") List<AttendanceStatus> statuses,
            RedirectAttributes redirectAttributes) {

        Course course = courseService.getCourseById(courseId).orElse(null);
        List<Attendance> list = new ArrayList<>();

        for (int i = 0; i < studentIds.size(); i++) {
            Long sId = studentIds.get(i);
            AttendanceStatus st = (i < statuses.size()) ? statuses.get(i) : AttendanceStatus.PRESENT;
            studentService.getStudentById(sId).ifPresent(student -> {
                list.add(new Attendance(student, course, date != null ? date : LocalDate.now(), st, "Marked via bulk sheet"));
            });
        }

        if (!list.isEmpty()) {
            attendanceService.saveAll(list, course != null ? course.getCourseName() : "Course");
            redirectAttributes.addFlashAttribute("successMessage", "Successfully marked attendance for " + list.size() + " students!");
        }

        return "redirect:/attendance?date=" + (date != null ? date : LocalDate.now());
    }
}
