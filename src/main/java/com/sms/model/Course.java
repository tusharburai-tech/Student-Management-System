package com.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course code is required")
    @Column(nullable = false, unique = true)
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false)
    private String courseName;

    @NotNull(message = "Department is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    @Column(length = 1000)
    private String description;

    private String duration; // e.g., "4 Months", "1 Semester"

    private String semester; // e.g., "Semester 3"

    private Integer maxEnrollment = 60;

    private Integer currentEnrollment = 0;

    private String instructor;

    private String status = "Active";

    private LocalDateTime createdAt = LocalDateTime.now();

    public Course() {}

    public Course(String courseCode, String courseName, Department department, String description,
                  String duration, String semester, Integer maxEnrollment, String instructor, String status) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
        this.description = description;
        this.duration = duration;
        this.semester = semester;
        this.maxEnrollment = maxEnrollment;
        this.instructor = instructor;
        this.status = status;
        this.currentEnrollment = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(Integer maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public Integer getCurrentEnrollment() {
        return currentEnrollment;
    }

    public void setCurrentEnrollment(Integer currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
