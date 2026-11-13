package com.sms.config;

import com.sms.model.*;
import com.sms.repository.ActivityLogRepository;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final ActivityLogRepository activityLogRepository;
    private final com.sms.repository.AdminUserRepository adminUserRepository;

    public DataInitializer(StudentRepository studentRepository,
                           CourseRepository courseRepository,
                           AttendanceRepository attendanceRepository,
                           ActivityLogRepository activityLogRepository,
                           com.sms.repository.AdminUserRepository adminUserRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.activityLogRepository = activityLogRepository;
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public void run(String... args) {
        // Seed default Admin if not exists
        if (!adminUserRepository.existsByEmail("admin@university.edu")) {
            adminUserRepository.save(new AdminUser(
                    "Dr. Arthur Pendelton",
                    "admin@university.edu",
                    "admin123",
                    "SUPER_ADMIN",
                    "Dean of Administration"
            ));
        }

        if (courseRepository.count() > 0) {
            return; // Data already seeded
        }

        // 1. Seed Courses
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("CS101", "Introduction to Computer Science", Department.COMPUTER_SCIENCE,
                "Foundational course covering computational thinking, algorithms, and Python programming.",
                "4 Months", "Semester 1", 60, "Dr. Robert Vance", "Active"));
        courses.add(new Course("CS204", "Data Structures & Algorithms", Department.COMPUTER_SCIENCE,
                "Advanced principles of trees, graphs, dynamic programming, and complexity analysis.",
                "4 Months", "Semester 3", 50, "Prof. Amanda Chen", "Active"));
        courses.add(new Course("IT305", "Cloud Architecture & DevOps", Department.INFORMATION_TECHNOLOGY,
                "Enterprise infrastructure, containerization with Docker/Kubernetes, and CI/CD pipelines.",
                "4 Months", "Semester 5", 45, "Dr. Michael Scott", "Active"));
        courses.add(new Course("ME201", "Thermodynamics & Heat Transfer", Department.MECHANICAL_ENGINEERING,
                "First and second laws of thermodynamics, power cycles, and heat conduction/convection.",
                "4 Months", "Semester 4", 55, "Dr. Harold Evans", "Active"));
        courses.add(new Course("CE302", "Structural Analysis & Design", Department.CIVIL_ENGINEERING,
                "Behavior of structures under stress, beam deflection, and seismic design foundations.",
                "4 Months", "Semester 6", 40, "Prof. Elena Rostova", "Active"));
        courses.add(new Course("BA102", "Principles of Modern Management", Department.BUSINESS_ADMINISTRATION,
                "Organizational behavior, strategic decision-making, and ethical corporate governance.",
                "4 Months", "Semester 2", 70, "Dr. Angela Martin", "Active"));
        courses.add(new Course("EE401", "Digital Signal Processing", Department.ELECTRICAL_ENGINEERING,
                "Fourier transforms, filter design, and real-time audio/image signal processing.",
                "4 Months", "Semester 7", 35, "Prof. David Miller", "Active"));
        courses.add(new Course("CS408", "Artificial Intelligence & Neural Nets", Department.COMPUTER_SCIENCE,
                "Machine learning theory, deep learning models, backpropagation, and transformer architectures.",
                "4 Months", "Semester 7", 40, "Dr. Amanda Chen", "Active"));

        courseRepository.saveAll(courses);

        // 2. Seed Students
        List<Student> students = new ArrayList<>();
        students.add(new Student("SMS-2024-101", "Alexander Wright", "a.wright@university.edu", "+1 (555) 234-8901",
                LocalDate.of(2003, 4, 15), Gender.MALE, Department.COMPUTER_SCIENCE, "Data Structures & Algorithms",
                "Semester 4", LocalDate.of(2023, 8, 20), "2023-2027", StudentStatus.ACTIVE,
                "742 Evergreen Terrace", "Boston", "Massachusetts", "02108"));
        students.add(new Student("SMS-2024-102", "Sophia Maria Rodriguez", "s.rodriguez@university.edu", "+1 (555) 345-6712",
                LocalDate.of(2003, 9, 28), Gender.FEMALE, Department.INFORMATION_TECHNOLOGY, "Cloud Architecture & DevOps",
                "Semester 4", LocalDate.of(2023, 8, 20), "2023-2027", StudentStatus.ACTIVE,
                "124 Conch Street", "Cambridge", "Massachusetts", "02138"));
        students.add(new Student("SMS-2024-103", "Ethan James Mitchell", "e.mitchell@university.edu", "+1 (555) 456-7890",
                LocalDate.of(2002, 11, 5), Gender.MALE, Department.MECHANICAL_ENGINEERING, "Thermodynamics & Heat Transfer",
                "Semester 6", LocalDate.of(2022, 8, 15), "2022-2026", StudentStatus.ACTIVE,
                "350 Fifth Avenue", "New York", "New York", "10118"));
        students.add(new Student("SMS-2024-104", "Emma Claire Thompson", "e.thompson@university.edu", "+1 (555) 567-8901",
                LocalDate.of(2004, 2, 14), Gender.FEMALE, Department.BUSINESS_ADMINISTRATION, "Principles of Modern Management",
                "Semester 2", LocalDate.of(2024, 8, 25), "2024-2028", StudentStatus.ACTIVE,
                "221B Baker Street", "Chicago", "Illinois", "60601"));
        students.add(new Student("SMS-2024-105", "Liam Patel", "l.patel@university.edu", "+1 (555) 678-9012",
                LocalDate.of(2002, 6, 21), Gender.MALE, Department.COMPUTER_SCIENCE, "Artificial Intelligence & Neural Nets",
                "Semester 6", LocalDate.of(2022, 8, 15), "2022-2026", StudentStatus.ACTIVE,
                "1600 Pennsylvania Ave", "San Jose", "California", "95112"));
        students.add(new Student("SMS-2024-106", "Olivia Grace Bennett", "o.bennett@university.edu", "+1 (555) 789-0123",
                LocalDate.of(2003, 12, 10), Gender.FEMALE, Department.CIVIL_ENGINEERING, "Structural Analysis & Design",
                "Semester 4", LocalDate.of(2023, 8, 20), "2023-2027", StudentStatus.ACTIVE,
                "42 Wallaby Way", "Austin", "Texas", "78701"));
        students.add(new Student("SMS-2024-107", "Noah Christopher Davis", "n.davis@university.edu", "+1 (555) 890-1234",
                LocalDate.of(2004, 7, 8), Gender.MALE, Department.ELECTRICAL_ENGINEERING, "Digital Signal Processing",
                "Semester 2", LocalDate.of(2024, 8, 25), "2024-2028", StudentStatus.INACTIVE,
                "10 Downing Road", "Seattle", "Washington", "98101"));
        students.add(new Student("SMS-2024-108", "Ava Elizabeth Taylor", "a.taylor@university.edu", "+1 (555) 901-2345",
                LocalDate.of(2001, 3, 30), Gender.FEMALE, Department.COMPUTER_SCIENCE, "Artificial Intelligence & Neural Nets",
                "Semester 8", LocalDate.of(2021, 8, 18), "2021-2025", StudentStatus.GRADUATED,
                "88 Market Street", "San Francisco", "California", "94105"));
        students.add(new Student("SMS-2024-109", "Lucas William Harrison", "l.harrison@university.edu", "+1 (555) 012-3456",
                LocalDate.of(2003, 8, 19), Gender.MALE, Department.MECHANICAL_ENGINEERING, "Thermodynamics & Heat Transfer",
                "Semester 4", LocalDate.of(2023, 8, 20), "2023-2027", StudentStatus.ACTIVE,
                "500 Elm Street", "Dallas", "Texas", "75201"));
        students.add(new Student("SMS-2024-110", "Mia Isabella Rossi", "m.rossi@university.edu", "+1 (555) 123-4567",
                LocalDate.of(2004, 1, 25), Gender.FEMALE, Department.INFORMATION_TECHNOLOGY, "Cloud Architecture & DevOps",
                "Semester 2", LocalDate.of(2024, 8, 25), "2024-2028", StudentStatus.ACTIVE,
                "77 Beacon Street", "Boston", "Massachusetts", "02108"));

        studentRepository.saveAll(students);

        // Update Course enrollment counts
        Course csCourse = courses.get(1); // CS204
        csCourse.setCurrentEnrollment(38);
        courseRepository.save(csCourse);

        Course itCourse = courses.get(2);
        itCourse.setCurrentEnrollment(42);
        courseRepository.save(itCourse);

        // 3. Seed Attendance Records for Today
        LocalDate today = LocalDate.now();
        List<Attendance> attendanceList = new ArrayList<>();
        attendanceList.add(new Attendance(students.get(0), csCourse, today, AttendanceStatus.PRESENT, "On time"));
        attendanceList.add(new Attendance(students.get(1), itCourse, today, AttendanceStatus.PRESENT, "On time"));
        attendanceList.add(new Attendance(students.get(2), courses.get(3), today, AttendanceStatus.LATE, "15 minutes late"));
        attendanceList.add(new Attendance(students.get(3), courses.get(5), today, AttendanceStatus.PRESENT, "On time"));
        attendanceList.add(new Attendance(students.get(4), courses.get(7), today, AttendanceStatus.ABSENT, "Medical notice pending"));
        attendanceList.add(new Attendance(students.get(5), courses.get(4), today, AttendanceStatus.PRESENT, "On time"));
        attendanceList.add(new Attendance(students.get(6), courses.get(6), today, AttendanceStatus.LEAVE, "Approved athletic leave"));
        attendanceList.add(new Attendance(students.get(8), courses.get(3), today, AttendanceStatus.PRESENT, "On time"));
        attendanceList.add(new Attendance(students.get(9), itCourse, today, AttendanceStatus.PRESENT, "On time"));

        attendanceRepository.saveAll(attendanceList);

        // 4. Seed Activity Logs
        List<ActivityLog> logs = new ArrayList<>();
        logs.add(new ActivityLog("New Student Enrolled", "Mia Isabella Rossi joined Information Technology", "STUDENT_REG", "bi-person-plus", "badge-activity-primary"));
        logs.add(new ActivityLog("Attendance Session Marked", "CS204: 38 students marked present by Prof. Amanda Chen", "ATTENDANCE", "bi-clipboard-check", "badge-activity-success"));
        logs.add(new ActivityLog("Course Syllabus Updated", "IT305 Cloud Architecture prerequisites revised", "COURSE_ADD", "bi-book", "badge-activity-info"));
        logs.add(new ActivityLog("Student Profile Updated", "Alexander Wright updated residential contact address", "STUDENT_UPDATE", "bi-pencil-square", "badge-activity-warning"));
        logs.add(new ActivityLog("Semester Report Generated", "Fall 2025 Departmental enrollment figures exported", "REPORT", "bi-file-earmark-bar-graph", "badge-activity-primary"));

        activityLogRepository.saveAll(logs);
    }
}
