package com.sms.service;

import com.sms.model.ActivityLog;
import com.sms.model.Department;
import com.sms.model.Student;
import com.sms.model.StudentStatus;
import com.sms.repository.ActivityLogRepository;
import com.sms.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final ActivityLogRepository activityLogRepository;

    public StudentService(StudentRepository studentRepository, ActivityLogRepository activityLogRepository) {
        this.studentRepository = studentRepository;
        this.activityLogRepository = activityLogRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Page<Student> getFilteredStudents(String keyword, Department department,
                                             StudentStatus status, String year,
                                             int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return studentRepository.findWithFilters(keyword, department, status, year, pageable);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    public Student saveStudent(Student student) {
        boolean isNew = (student.getId() == null);
        if (student.getStudentId() == null || student.getStudentId().isBlank()) {
            long count = studentRepository.count() + 101;
            student.setStudentId("SMS-2025-" + count);
        }
        student.setUpdatedAt(LocalDateTime.now());
        Student saved = studentRepository.save(student);

        if (isNew) {
            activityLogRepository.save(new ActivityLog(
                    "New Student Registered",
                    saved.getFullName() + " (" + saved.getStudentId() + ") was enrolled in " +
                    (saved.getDepartment() != null ? saved.getDepartment().getFullName() : "University"),
                    "STUDENT_REG",
                    "bi-person-plus",
                    "badge-activity-primary"
            ));
        } else {
            activityLogRepository.save(new ActivityLog(
                    "Student Updated",
                    "Profile for " + saved.getFullName() + " (" + saved.getStudentId() + ") was updated",
                    "STUDENT_UPDATE",
                    "bi-pencil-square",
                    "badge-activity-info"
            ));
        }
        return saved;
    }

    public void deleteStudent(Long id) {
        studentRepository.findById(id).ifPresent(student -> {
            activityLogRepository.save(new ActivityLog(
                    "Student Record Removed",
                    student.getFullName() + " (" + student.getStudentId() + ") was removed from the system",
                    "STUDENT_DELETE",
                    "bi-trash",
                    "badge-activity-danger"
            ));
            studentRepository.delete(student);
        });
    }

    public List<Student> getRecentStudents() {
        return studentRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public long getTotalStudents() {
        return studentRepository.count();
    }

    public long getActiveStudents() {
        return studentRepository.countByStatus(StudentStatus.ACTIVE);
    }
}
