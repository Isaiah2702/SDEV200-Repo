import java.time.LocalDate;

public class Task extends PlannerItem {
    private boolean completed;

    public Task(String id, String title, String description, LocalDate date, String priority, String status, boolean completed) {
        super(id, title, description, date, priority, status);
        this.completed = completed;

        if (completed) {
            super.setStatus("Completed");
        }
        validate();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            super.setStatus("Completed");
        }
        validate();
    }

    public void markComplete() {
        this.completed = true;
        super.setStatus("Completed");
        validate();
    }

    @Override
    public void validate() {
        super.validate();
        if (completed && !getStatus().equals("Completed")) {
            throw new IllegalArgumentException("If completed is true, status must be Completed");
        }
        if (!completed && getStatus().equals("Completed")) {
            this.completed = true;
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
