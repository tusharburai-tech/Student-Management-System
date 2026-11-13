package com.sms.repository;

import com.sms.model.Course;
import com.sms.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByDepartment(Department department);

    List<Course> findByStatus(String status);

    @Query("SELECT c FROM Course c WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:department IS NULL OR c.department = :department)")
    List<Course> searchCourses(@Param("keyword") String keyword, @Param("department") Department department);

    @Query("SELECT COALESCE(SUM(c.currentEnrollment), 0) FROM Course c")
    Long sumTotalEnrollment();
}
