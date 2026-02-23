import java.time.LocalDate;

/**
 * Abstract superclass for planner items (Task or Appointment).
 * UI is intentionally excluded; this class models business logic + validation.
 */
public abstract class PlannerItem {
    private final String id;
    private String title;
    private String description;
    private LocalDate date;
    private String priority; // Low | Medium | High
    private String status;   // Pending | Completed

    public PlannerItem(String id, String title, String description, LocalDate date, String priority, String status) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id is required");
        }
        this.id = id.trim();
        setTitle(title);
        setDescription(description);
        setDate(date);
        setPriority(priority);
        setStatus(status);
    }

    public String getId() { return id; }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        validateTitle(title);
        this.title = title.trim();
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = (description == null) ? "" : description;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {
        validateDate(date);
        this.date = date;
    }

    public String getPriority() { return priority; }

    public void setPriority(String priority) {
        validatePriority(priority);
        this.priority = priority.trim();
    }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        validateStatus(status);
        this.status = status.trim();
    }

    public void validate() {
        validateTitle(this.title);
        validateDate(this.date);
        validatePriority(this.priority);
        validateStatus(this.status);
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

    // ---------- Private validation helpers per UML ----------
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
        if (priority == null) {
            throw new IllegalArgumentException("priority is required");
        }
        String p = priority.trim();
        if (!(p.equals("Low") || p.equals("Medium") || p.equals("High"))) {
            throw new IllegalArgumentException("priority must be Low, Medium, or High");
        }
    }

    private void validateStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        String s = status.trim();
        if (!(s.equals("Pending") || s.equals("Completed"))) {
            throw new IllegalArgumentException("status must be Pending or Completed");
        }
    }
}
