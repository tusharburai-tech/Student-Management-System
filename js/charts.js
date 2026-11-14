/**
 * Chart.js Visualizations for Student Management System
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Enrollment Comparison Area Chart
    const enrollmentCanvas = document.getElementById('enrollmentChart');
    if (enrollmentCanvas && typeof Chart !== 'undefined') {
        const ctx = enrollmentCanvas.getContext('2d');

        // Create gradients
        const gradientCurrent = ctx.createLinearGradient(0, 0, 0, 300);
        gradientCurrent.addColorStop(0, 'rgba(37, 99, 235, 0.35)');
        gradientCurrent.addColorStop(1, 'rgba(37, 99, 235, 0.0)');

        const gradientPrevious = ctx.createLinearGradient(0, 0, 0, 300);
        gradientPrevious.addColorStop(0, 'rgba(148, 163, 184, 0.25)');
        gradientPrevious.addColorStop(1, 'rgba(148, 163, 184, 0.0)');

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [
                    {
                        label: 'Current Year (2025)',
                        data: [140, 180, 240, 310, 420, 580, 750, 920, 1450, 1820, 2150, 2450],
                        borderColor: '#2563EB',
                        backgroundColor: gradientCurrent,
                        borderWidth: 3,
                        pointBackgroundColor: '#2563EB',
                        pointBorderColor: '#FFFFFF',
                        pointBorderWidth: 2,
                        pointRadius: 4,
                        pointHoverRadius: 6,
                        tension: 0.38,
                        fill: true
                    },
                    {
                        label: 'Previous Year (2024)',
                        data: [110, 140, 190, 260, 350, 480, 620, 780, 1150, 1480, 1810, 2180],
                        borderColor: '#94A3B8',
                        backgroundColor: gradientPrevious,
                        borderWidth: 2,
                        borderDash: [5, 5],
                        pointBackgroundColor: '#94A3B8',
                        pointBorderColor: '#FFFFFF',
                        pointBorderWidth: 2,
                        pointRadius: 3,
                        pointHoverRadius: 5,
                        tension: 0.38,
                        fill: true
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                        align: 'end',
                        labels: {
                            boxWidth: 12,
                            usePointStyle: true,
                            font: { family: "'Plus Jakarta Sans', sans-serif", size: 12, weight: 600 },
                            color: '#64748B'
                        }
                    },
                    tooltip: {
                        backgroundColor: '#172554',
                        titleFont: { family: "'Plus Jakarta Sans', sans-serif", size: 13, weight: 700 },
                        bodyFont: { family: "'Plus Jakarta Sans', sans-serif", size: 12 },
                        padding: 12,
                        cornerRadius: 8,
                        displayColors: true
                    }
                },
                scales: {
                    y: {
                        grid: { color: '#EEF2F6', drawBorder: false },
                        ticks: {
                            color: '#94A3B8',
                            font: { family: "'Plus Jakarta Sans', sans-serif", size: 11 },
                            callback: function(val) { return val.toLocaleString(); }
                        }
                    },
                    x: {
                        grid: { display: false },
                        ticks: {
                            color: '#94A3B8',
                            font: { family: "'Plus Jakarta Sans', sans-serif", size: 12 }
                        }
                    }
                }
            }
        });
    }

    // 2. Department Distribution Doughnut Chart
    const deptCanvas = document.getElementById('deptChart');
    if (deptCanvas && typeof Chart !== 'undefined') {
        const ctx = deptCanvas.getContext('2d');

        let labels = ['Computer Science', 'Information Tech', 'Mechanical Eng', 'Civil Eng', 'Electrical Eng', 'Business Admin'];
        let data = [35, 25, 15, 10, 8, 7];

        if (deptCanvas.dataset.labels && deptCanvas.dataset.values) {
            const parsedLabels = deptCanvas.dataset.labels.split('|');
            const parsedValues = deptCanvas.dataset.values.split('|').map(v => parseInt(v, 10) || 0);
            const sum = parsedValues.reduce((a, b) => a + b, 0);
            if (sum > 0) {
                labels = parsedLabels;
                data = parsedValues;
            }
        }

        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: data,
                    backgroundColor: [
                        '#2563EB', // Blue
                        '#0284C7', // Sky
                        '#059669', // Emerald
                        '#F59E0B', // Amber
                        '#7C3AED', // Violet
                        '#64748B'  // Slate
                    ],
                    borderWidth: 2,
                    borderColor: '#FFFFFF',
                    hoverOffset: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '72%',
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        backgroundColor: '#172554',
                        titleFont: { family: "'Plus Jakarta Sans', sans-serif", size: 13, weight: 700 },
                        bodyFont: { family: "'Plus Jakarta Sans', sans-serif", size: 12 },
                        padding: 10,
                        cornerRadius: 8,
                        callbacks: {
                            label: function(context) {
                                return ` ${context.label}: ${context.raw} students`;
                            }
                        }
                    }
                }
            }
        });
    }

    // 3. Attendance Overview Status Bar Chart (Reports / Attendance)
    const attendanceCanvas = document.getElementById('attendanceOverviewChart');
    if (attendanceCanvas && typeof Chart !== 'undefined') {
        const ctx = attendanceCanvas.getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'],
                datasets: [
                    {
                        label: 'Present',
                        data: [92, 94, 91, 95, 89],
                        backgroundColor: '#16A34A',
                        borderRadius: 6
                    },
                    {
                        label: 'Absent',
                        data: [5, 4, 6, 3, 7],
                        backgroundColor: '#DC2626',
                        borderRadius: 6
                    },
                    {
                        label: 'Late / Leave',
                        data: [3, 2, 3, 2, 4],
                        backgroundColor: '#F59E0B',
                        borderRadius: 6
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: { stacked: true, grid: { display: false } },
                    y: {
                        stacked: true,
                        max: 100,
                        ticks: {
                            callback: function(v) { return v + '%'; }
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                        labels: { boxWidth: 12, usePointStyle: true }
                    }
                }
            }
        });
    }
});
