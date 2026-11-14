/**
 * Student Management Client Script
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Select All Checkboxes
    const selectAllCheckbox = document.getElementById('selectAllStudents');
    const studentCheckboxes = document.querySelectorAll('.student-row-check');

    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', () => {
            studentCheckboxes.forEach(cb => {
                cb.checked = selectAllCheckbox.checked;
            });
        });
    }

    // 2. Delete Student Confirmation Modal
    window.confirmDeleteStudent = function(studentId, studentName) {
        const modal = document.getElementById('deleteConfirmModal');
        const form = document.getElementById('deleteStudentForm');
        const nameSpan = document.getElementById('deleteStudentNameSpan');

        if (modal && form && nameSpan) {
            form.action = `/students/delete/${studentId}`;
            nameSpan.textContent = studentName;
            window.openModal('deleteConfirmModal');
        }
    };

    // 3. Reset Filters
    const resetFilterBtn = document.getElementById('resetFilterBtn');
    if (resetFilterBtn) {
        resetFilterBtn.addEventListener('click', () => {
            window.location.href = '/students';
        });
    }
});
