package com.sms.repository;

import com.sms.model.Attendance;
import com.sms.model.AttendanceStatus;
import com.sms.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByAttendanceDate(LocalDate date);

    List<Attendance> findByStudentId(Long studentId);

    @Query("SELECT a FROM Attendance a WHERE " +
           "(:date IS NULL OR a.attendanceDate = :date) AND " +
           "(:department IS NULL OR a.student.department = :department) AND " +
           "(:courseId IS NULL OR (a.course IS NOT NULL AND a.course.id = :courseId)) AND " +
           "(:semester IS NULL OR :semester = '' OR a.student.semester = :semester)")
    List<Attendance> findWithFilters(
            @Param("date") LocalDate date,
            @Param("department") Department department,
            @Param("courseId") Long courseId,
            @Param("semester") String semester
    );

    long countByAttendanceDateAndStatus(LocalDate date, AttendanceStatus status);

    long countByAttendanceDate(LocalDate date);

    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.attendanceDate = :date GROUP BY a.status")
    List<Object[]> countStatusForDate(@Param("date") LocalDate date);

    @Query("SELECT a.status, COUNT(a) FROM Attendance a GROUP BY a.status")
    List<Object[]> countOverallStatus();
}
