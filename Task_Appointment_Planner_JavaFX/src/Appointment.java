import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment extends PlannerItem {
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;

    public Appointment(
            String id,
            String title,
            String description,
            LocalDate date,
            String priority,
            String status,
            LocalTime startTime,
            LocalTime endTime,
            String location
    ) {
        super(id, title, description, date, priority, status);
        setStartTime(startTime);
        setEndTime(endTime);
        setLocation(location);
        validate();
    }

    public LocalTime getStartTime() { return startTime; }

    public void setStartTime(LocalTime startTime) {
        if (startTime == null) throw new IllegalArgumentException("startTime is required");
        this.startTime = startTime;
        if (this.endTime != null) validateTimeRange(this.startTime, this.endTime);
    }

    public LocalTime getEndTime() { return endTime; }

    public void setEndTime(LocalTime endTime) {
        if (endTime == null) throw new IllegalArgumentException("endTime is required");
        this.endTime = endTime;
        if (this.startTime != null) validateTimeRange(this.startTime, this.endTime);
    }

    public String getLocation() { return location; }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("location is required");
        }
        this.location = location.trim();
    }

    @Override
    public void validate() {
        super.validate();
        if (startTime == null) throw new IllegalArgumentException("startTime is required");
        if (endTime == null) throw new IllegalArgumentException("endTime is required");
        if (location == null || location.trim().isEmpty()) throw new IllegalArgumentException("location is required");
        validateTimeRange(startTime, endTime);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + getId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", date=" + getDate() +
                ", priority='" + getPriority() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                '}';
    }

    // ---------- Private helper per UML ----------
    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime and endTime are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
    }
}
