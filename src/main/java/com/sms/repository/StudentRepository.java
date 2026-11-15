package com.sms.repository;

import com.sms.model.Department;
import com.sms.model.Student;
import com.sms.model.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String studentId);

    Optional<Student> findByEmail(String email);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    boolean existsByStudentIdAndIdNot(String studentId, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Student> findTop5ByOrderByCreatedAtDesc();

    long countByStatus(StudentStatus status);

    long countByDepartment(Department department);

    @Query("SELECT s FROM Student s WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:department IS NULL OR s.department = :department) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:year IS NULL OR :year = '' OR s.academicYear LIKE CONCAT('%', :year, '%'))")
    Page<Student> findWithFilters(
            @Param("keyword") String keyword,
            @Param("department") Department department,
            @Param("status") StudentStatus status,
            @Param("year") String year,
            Pageable pageable
    );

    @Query("SELECT s.department, COUNT(s) FROM Student s GROUP BY s.department")
    List<Object[]> countStudentsByDepartment();
}
