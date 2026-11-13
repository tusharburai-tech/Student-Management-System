package com.sms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String activityType; // STUDENT_REG, COURSE_ADD, ATTENDANCE, STUDENT_UPDATE
    private String icon; // Lucide / Bootstrap icon name e.g. "bi-person-plus", "bi-book", "bi-check2-circle"
    private String badgeClass; // badge-activity-primary, badge-activity-success, etc.

    private LocalDateTime createdAt = LocalDateTime.now();

    public ActivityLog() {}

    public ActivityLog(String title, String description, String activityType, String icon, String badgeClass) {
        this.title = title;
        this.description = description;
        this.activityType = activityType;
        this.icon = icon;
        this.badgeClass = badgeClass;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public void setBadgeClass(String badgeClass) {
        this.badgeClass = badgeClass;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
