package com.sms.service;

import com.sms.model.ActivityLog;
import com.sms.model.Course;
import com.sms.model.Department;
import com.sms.repository.ActivityLogRepository;
import com.sms.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final ActivityLogRepository activityLogRepository;

    public CourseService(CourseRepository courseRepository, ActivityLogRepository activityLogRepository) {
        this.courseRepository = courseRepository;
        this.activityLogRepository = activityLogRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> searchCourses(String keyword, Department department) {
        return courseRepository.searchCourses(keyword, department);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        boolean isNew = (course.getId() == null);
        Course saved = courseRepository.save(course);
        if (isNew) {
            activityLogRepository.save(new ActivityLog(
                    "New Course Created",
                    "Course " + saved.getCourseCode() + " - " + saved.getCourseName() + " was added",
                    "COURSE_ADD",
                    "bi-book",
                    "badge-activity-success"
            ));
        }
        return saved;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public long getTotalCourses() {
        return courseRepository.count();
    }

    public long getActiveCoursesCount() {
        return courseRepository.findByStatus("Active").size();
    }

    public long getTotalEnrolledStudents() {
        Long sum = courseRepository.sumTotalEnrollment();
        return (sum != null) ? sum : 0L;
    }
}
