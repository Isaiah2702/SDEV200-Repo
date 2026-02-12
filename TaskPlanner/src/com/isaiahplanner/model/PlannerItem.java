package com.isaiahplanner.model;

import java.time.LocalDate;

/**
 * Abstract base class for planner items (Task, Appointment).
 * Matches UML 1:1 (fields, methods, and validation helpers).
 */
public abstract class PlannerItem {

    private String id;
    private String title;
    private String description;
    private LocalDate date;
    private String priority; // Low, Medium, High
    private String status;   // Pending, Completed

    public PlannerItem(String id,
                       String title,
                       String description,
                       LocalDate date,
                       String priority,
                       String status) {
        this.id = id;
        setTitle(title);
        setDescription(description);
        setDate(date);
        setPriority(priority);
        setStatus(status);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        validateTitle(title);
        this.title = title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        // Description isn't marked "required" in UML, but keep it non-null.
        this.description = (description == null) ? "" : description.trim();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        validateDate(date);
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        validatePriority(priority);
        this.priority = normalizePriority(priority);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        validateStatus(status);
        this.status = normalizeStatus(status);
    }

    /**
     * Validates the whole object. Subclasses may override and call super.validate().
     */
    public void validate() {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        validateTitle(title);
        validateDate(date);
        validatePriority(priority);
        validateStatus(status);
    }

    @Override
    public String toString() {
        return "PlannerItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    // ------------------- UML private validation helpers -------------------

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
    }

    private void validatePriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            throw new IllegalArgumentException("priority is required");
        }
        String p = priority.trim().toLowerCase();
        if (!(p.equals("low") || p.equals("medium") || p.equals("high"))) {
            throw new IllegalArgumentException("priority must be Low, Medium, or High");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("status is required");
        }
        String s = status.trim().toLowerCase();
        if (!(s.equals("pending") || s.equals("completed"))) {
            throw new IllegalArgumentException("status must be Pending or Completed");
        }
    }

    // ------------------- Normalization helpers (not in UML but harmless) -------------------

    private static String normalizePriority(String priority) {
        String p = priority.trim().toLowerCase();
        return Character.toUpperCase(p.charAt(0)) + p.substring(1);
    }

    private static String normalizeStatus(String status) {
        String s = status.trim().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
