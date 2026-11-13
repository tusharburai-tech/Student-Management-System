/**
 * Attendance Management Client Script
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Mark All Shortcut (Present / Absent)
    window.setAllAttendanceStatus = function(statusValue) {
        const radios = document.querySelectorAll(`input[type="radio"][value="${statusValue}"]`);
        radios.forEach(radio => {
            radio.checked = true;
        });
    };

    // 2. Quick Mark Modal Opener
    window.openQuickMarkModal = function(studentId, studentName, defaultCourseId) {
        const studentIdInput = document.getElementById('quickMarkStudentId');
        const studentNameDisplay = document.getElementById('quickMarkStudentName');
        const courseSelect = document.getElementById('quickMarkCourseSelect');

        if (studentIdInput && studentNameDisplay) {
            studentIdInput.value = studentId;
            studentNameDisplay.textContent = studentName;
            if (courseSelect && defaultCourseId) {
                courseSelect.value = defaultCourseId;
            }
            window.openModal('quickMarkAttendanceModal');
        }
    };

    // 3. Auto reload on date change
    const dateInput = document.getElementById('attendanceDatePicker');
    if (dateInput) {
        dateInput.addEventListener('change', () => {
            const form = document.getElementById('attendanceFilterForm');
            if (form) {
                form.submit();
            }
        });
    }
});
