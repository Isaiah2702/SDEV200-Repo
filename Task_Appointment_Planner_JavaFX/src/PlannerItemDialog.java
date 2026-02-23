import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Modal dialog used by the JavaFX UI to add/edit Tasks or Appointments.
 * This keeps UI concerns out of the core model classes.
 */
public class PlannerItemDialog extends Dialog<PlannerItem> {

    private final ChoiceBox<String> typeChoice = new ChoiceBox<>(FXCollections.observableArrayList("Task", "Appointment"));

    private final TextField titleField = new TextField();
    private final TextArea descArea = new TextArea();
    private final DatePicker datePicker = new DatePicker(LocalDate.now());
    private final ComboBox<String> priorityBox = new ComboBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
    private final ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList("Pending", "Completed"));

    // Task fields
    private final CheckBox completedBox = new CheckBox("Completed");

    // Appointment fields
    private final TextField startField = new TextField(); // HH:MM
    private final TextField endField = new TextField();   // HH:MM
    private final TextField locationField = new TextField();

    private final PlannerItem original;

    public PlannerItemDialog(PlannerItem original) {
        this.original = original;

        setTitle(original == null ? "Add Item" : "Edit Item");
        setHeaderText("Enter details. Invalid input will show an error message.");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        priorityBox.setValue("Medium");
        statusBox.setValue("Pending");
        typeChoice.setValue("Task");

        descArea.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        int r = 0;
        grid.add(new Label("Type:"), 0, r);
        grid.add(typeChoice, 1, r++);

        grid.add(new Label("Title:"), 0, r);
        grid.add(titleField, 1, r++);

        grid.add(new Label("Description:"), 0, r);
        grid.add(descArea, 1, r++);

        grid.add(new Label("Date:"), 0, r);
        grid.add(datePicker, 1, r++);

        grid.add(new Label("Priority:"), 0, r);
        grid.add(priorityBox, 1, r++);

        grid.add(new Label("Status:"), 0, r);
        grid.add(statusBox, 1, r++);

        Separator sep1 = new Separator();
        grid.add(sep1, 0, r++, 2, 1);

        // Task section
        grid.add(new Label("Task:"), 0, r);
        grid.add(completedBox, 1, r++);
        Separator sep2 = new Separator();
        grid.add(sep2, 0, r++, 2, 1);

        // Appointment section
        grid.add(new Label("Appointment Start (HH:MM):"), 0, r);
        grid.add(startField, 1, r++);

        grid.add(new Label("Appointment End (HH:MM):"), 0, r);
        grid.add(endField, 1, r++);

        grid.add(new Label("Location:"), 0, r);
        grid.add(locationField, 1, r++);

        getDialogPane().setContent(grid);

        // Prefill if editing
        if (original != null) {
            titleField.setText(original.getTitle());
            descArea.setText(original.getDescription());
            datePicker.setValue(original.getDate());
            priorityBox.setValue(original.getPriority());
            statusBox.setValue(original.getStatus());

            if (original instanceof Task) {
                typeChoice.setValue("Task");
                completedBox.setSelected(((Task) original).isCompleted());
            } else {
                typeChoice.setValue("Appointment");
                Appointment a = (Appointment) original;
                startField.setText(a.getStartTime().toString());
                endField.setText(a.getEndTime().toString());
                locationField.setText(a.getLocation());
            }
        } else {
            // sensible defaults
            startField.setText("09:00");
            endField.setText("10:00");
            locationField.setText("Main Office");
        }

        // Enable/disable sections based on type
        typeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> updateEnabledState());
        updateEnabledState();

        setResultConverter(btn -> {
            if (btn != ButtonType.OK) return null;

            try {
                String id = (original == null) ? MainApp.newId() : original.getId();
                String title = titleField.getText();
                String desc = descArea.getText();
                LocalDate date = datePicker.getValue();
                String priority = priorityBox.getValue();
                String status = statusBox.getValue();

                if (typeChoice.getValue().equals("Task")) {
                    boolean completed = completedBox.isSelected();
                    return new Task(id, title, desc, date, priority, status, completed);
                } else {
                    LocalTime start = parseTime(startField.getText(), "startTime");
                    LocalTime end = parseTime(endField.getText(), "endTime");
                    String loc = locationField.getText();
                    return new Appointment(id, title, desc, date, priority, status, start, end, loc);
                }
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Validation Error");
                a.setHeaderText("Please fix the highlighted issue");
                a.setContentText(ex.getMessage());
                a.showAndWait();
                return null;
            }
        });
    }

    private void updateEnabledState() {
        boolean isTask = typeChoice.getValue().equals("Task");

        completedBox.setDisable(!isTask);

        startField.setDisable(isTask);
        endField.setDisable(isTask);
        locationField.setDisable(isTask);
    }

    private LocalTime parseTime(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required (HH:MM)");
        }
        return LocalTime.parse(text.trim());
    }
}
