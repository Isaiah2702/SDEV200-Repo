package com.isaiahplanner.model;

import java.time.LocalDate;

/**
 * Task extends PlannerItem (UML 1:1).
 */
public class Task extends PlannerItem {

    private boolean completed;

    public Task(String id,
                String title,
                String description,
                LocalDate date,
                String priority,
                String status,
                boolean completed) {
        super(id, title, description, date, priority, status);
        this.completed = completed;
        validate();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void markComplete() {
        this.completed = true;
        setStatus("Completed");
    }

    @Override
    public void validate() {
        super.validate();
        // Keep status + completed consistent (recommended for business correctness)
        if (completed && !"Completed".equalsIgnoreCase(getStatus())) {
            throw new IllegalArgumentException("If completed=true, status must be Completed");
        }
        if (!completed && "Completed".equalsIgnoreCase(getStatus())) {
            // Allow this if you prefer status-driven completion, but UML includes both.
            // We'll enforce consistency to reduce bugs.
            throw new IllegalArgumentException("If status=Completed, completed must be true");
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + getId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", date=" + getDate() +
                ", priority='" + getPriority() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", completed=" + completed +
                '}';
    }
}
