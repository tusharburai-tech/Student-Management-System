/**
 * Dashboard Specific Interactivity
 */

document.addEventListener('DOMContentLoaded', () => {
    // Quick metric refresh indicator animation
    const refreshBadges = document.querySelectorAll('.stat-growth');
    refreshBadges.forEach(badge => {
        badge.addEventListener('mouseenter', () => {
            badge.style.transform = 'scale(1.08)';
            badge.style.transition = 'transform 0.2s ease';
        });
        badge.addEventListener('mouseleave', () => {
            badge.style.transform = 'scale(1)';
        });
    });
});
