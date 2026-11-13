package com.sms.model;

public enum StudentStatus {
    ACTIVE("Active", "badge-status-active"),
    INACTIVE("Inactive", "badge-status-inactive"),
    SUSPENDED("Suspended", "badge-status-suspended"),
    GRADUATED("Graduated", "badge-status-graduated");

    private final String label;
    private final String badgeClass;

    StudentStatus(String label, String badgeClass) {
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
