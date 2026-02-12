package com.isaiahplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Appointment extends PlannerItem (UML 1:1).
 */
public class Appointment extends PlannerItem {

    private LocalTime startTime;
    private LocalTime endTime;
    private String location;

    public Appointment(String id,
                       String title,
                       String description,
                       LocalDate date,
                       String priority,
                       String status,
                       LocalTime startTime,
                       LocalTime endTime,
                       String location) {
        super(id, title, description, date, priority, status);
        setStartTime(startTime);
        setEndTime(endTime);
        setLocation(location);
        validate();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("startTime is required");
        }
        this.startTime = startTime;
        // If endTime already set, validate range
        if (this.endTime != null) {
            validateTimeRange(this.startTime, this.endTime);
        }
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("endTime is required");
        }
        if (this.startTime != null) {
            validateTimeRange(this.startTime, endTime);
        }
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = (location == null) ? "" : location.trim();
    }

    @Override
    public void validate() {
        super.validate();
        if (startTime == null) {
            throw new IllegalArgumentException("startTime is required");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("endTime is required");
        }
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

    // UML specifies this as private
    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
    }
}
