package com.sms.service;

import com.sms.model.Course;
import com.sms.model.Department;
import com.sms.model.Student;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportService(StudentRepository studentRepository, CourseRepository courseRepository, AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public Map<String, Object> getReportsData(Department filterDept) {
        Map<String, Object> data = new HashMap<>();

        long totalStudents = studentRepository.count();
        long activeStudents = studentRepository.countByStatus(com.sms.model.StudentStatus.ACTIVE);
        long totalCourses = courseRepository.count();

        data.put("totalStudents", totalStudents);
        data.put("activeStudents", activeStudents);
        data.put("totalCourses", totalCourses);

        List<Object[]> deptCounts = studentRepository.countStudentsByDepartment();
        Map<String, Long> deptMap = new HashMap<>();
        for (Object[] r : deptCounts) {
            Department d = (Department) r[0];
            deptMap.put(d != null ? d.getFullName() : "Other", (Long) r[1]);
        }
        data.put("deptCounts", deptMap);

        List<Student> students = (filterDept != null) ?
                studentRepository.findWithFilters("", filterDept, null, "", org.springframework.data.domain.Pageable.unpaged()).getContent() :
                studentRepository.findAll();
        data.put("students", students);

        List<Course> courses = courseRepository.findAll();
        data.put("courses", courses);

        return data;
    }

    public void writeStudentsCsv(PrintWriter writer, Department dept) {
        writer.println("Student ID,Full Name,Email,Phone,Department,Semester,Status,Enrollment Date,GPA");
        List<Student> list = (dept != null) ?
                studentRepository.findWithFilters("", dept, null, "", org.springframework.data.domain.Pageable.unpaged()).getContent() :
                studentRepository.findAll();

        for (Student s : list) {
            writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%.2f\"%n",
                    s.getStudentId(),
                    s.getFullName(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getDepartment() != null ? s.getDepartment().getFullName() : "",
                    s.getSemester(),
                    s.getStatus() != null ? s.getStatus().getLabel() : "",
                    s.getEnrollmentDate() != null ? s.getEnrollmentDate().toString() : "",
                    s.getGpa() != null ? s.getGpa() : 0.0
            );
        }
    }
}
