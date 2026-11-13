package com.sms.service;

import com.sms.model.ActivityLog;
import com.sms.model.Attendance;
import com.sms.model.AttendanceStatus;
import com.sms.model.Department;
import com.sms.repository.ActivityLogRepository;
import com.sms.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ActivityLogRepository activityLogRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, ActivityLogRepository activityLogRepository) {
        this.attendanceRepository = attendanceRepository;
        this.activityLogRepository = activityLogRepository;
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date);
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getFilteredAttendance(LocalDate date, Department department, Long courseId, String semester) {
        return attendanceRepository.findWithFilters(date, department, courseId, semester);
    }

    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public void saveAll(List<Attendance> list, String courseTitle) {
        attendanceRepository.saveAll(list);
        activityLogRepository.save(new ActivityLog(
                "Attendance Marked",
                "Attendance recorded for " + list.size() + " students in " + (courseTitle != null ? courseTitle : "course"),
                "ATTENDANCE",
                "bi-clipboard-check",
                "badge-activity-warning"
        ));
    }

    public Map<String, Long> getAttendanceMetricsForDate(LocalDate date) {
        Map<String, Long> metrics = new HashMap<>();
        long total = attendanceRepository.countByAttendanceDate(date);
        long present = attendanceRepository.countByAttendanceDateAndStatus(date, AttendanceStatus.PRESENT);
        long absent = attendanceRepository.countByAttendanceDateAndStatus(date, AttendanceStatus.ABSENT);
        long late = attendanceRepository.countByAttendanceDateAndStatus(date, AttendanceStatus.LATE);
        long leave = attendanceRepository.countByAttendanceDateAndStatus(date, AttendanceStatus.LEAVE);

        metrics.put("total", total);
        metrics.put("present", present);
        metrics.put("absent", absent);
        metrics.put("late", late);
        metrics.put("leave", leave);

        double rate = total > 0 ? ((double) present / total) * 100.0 : 0.0;
        metrics.put("rate", Math.round(rate));
        return metrics;
    }

    public Map<String, Long> getOverallAttendanceDistribution() {
        Map<String, Long> dist = new HashMap<>();
        List<Object[]> rows = attendanceRepository.countOverallStatus();
        for (Object[] r : rows) {
            AttendanceStatus status = (AttendanceStatus) r[0];
            Long count = (Long) r[1];
            dist.put(status.name(), count);
        }
        return dist;
    }
}
