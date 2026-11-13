package com.sms.model;

public enum AttendanceStatus {
    PRESENT("Present", "badge-attend-present"),
    ABSENT("Absent", "badge-attend-absent"),
    LATE("Late", "badge-attend-late"),
    LEAVE("Leave", "badge-attend-leave");

    private final String label;
    private final String badgeClass;

    AttendanceStatus(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
