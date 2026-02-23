import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class MainApp extends Application {

    private final ObservableList<PlannerItem> items = FXCollections.observableArrayList();
    private final FileStorageManager storage = new FileStorageManager("|");

    private TableView<PlannerItem> table;
    private Label statusLabel;

    // Default data file (you can change from File -> Save As)
    private String currentFilePath = System.getProperty("user.home") + File.separator + "planner_data.txt";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task & Appointment Planner");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        table = buildTable();
        statusLabel = new Label("Ready.");

        root.setTop(buildToolbar(primaryStage));
        root.setCenter(table);
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(8, 0, 0, 0));

        // Load data on startup (event-driven user can also load different file)
        safeLoad(currentFilePath);

        Scene scene = new Scene(root, 980, 560);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<PlannerItem> buildTable() {
        TableView<PlannerItem> tv = new TableView<>();
        tv.setItems(items);

        TableColumn<PlannerItem, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cell -> new SimpleStringProperty(
                (cell.getValue() instanceof Task) ? "Task" : "Appointment"
        ));
        typeCol.setPrefWidth(110);

        TableColumn<PlannerItem, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(220);

        TableColumn<PlannerItem, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
        ));
        dateCol.setPrefWidth(110);

        TableColumn<PlannerItem, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(110);

        TableColumn<PlannerItem, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        TableColumn<PlannerItem, String> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellValueFactory(cell -> {
            PlannerItem it = cell.getValue();
            if (it instanceof Task) {
                Task t = (Task) it;
                return new SimpleStringProperty("Completed=" + t.isCompleted());
            } else {
                Appointment a = (Appointment) it;
                return new SimpleStringProperty(a.getStartTime() + " - " + a.getEndTime() + " @ " + a.getLocation());
            }
        });
        detailsCol.setPrefWidth(280);

        tv.getColumns().addAll(typeCol, titleCol, dateCol, priorityCol, statusCol, detailsCol);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
;

        return tv;
    }

    private HBox buildToolbar(Stage stage) {
        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        Button markCompleteBtn = new Button("Mark Complete");
        Button refreshBtn = new Button("Reload");

        MenuButton fileMenu = new MenuButton("File");
        MenuItem loadItem = new MenuItem("Load...");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As...");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(loadItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);

        // ---------- Events ----------
        addBtn.setOnAction(e -> onAdd());
        editBtn.setOnAction(e -> onEdit());
        deleteBtn.setOnAction(e -> onDelete());
        markCompleteBtn.setOnAction(e -> onMarkComplete());
        refreshBtn.setOnAction(e -> safeLoad(currentFilePath));

        loadItem.setOnAction(e -> onLoad(stage));
        saveItem.setOnAction(e -> safeSave(currentFilePath));
        saveAsItem.setOnAction(e -> onSaveAs(stage));
        exitItem.setOnAction(e -> stage.close());

        HBox bar = new HBox(8, fileMenu, addBtn, editBtn, deleteBtn, markCompleteBtn, refreshBtn);
        bar.setPadding(new Insets(0, 0, 10, 0));
        return bar;
    }

    private void onAdd() {
        PlannerItemDialog dialog = new PlannerItemDialog(null);
        Optional<PlannerItem> result = dialog.showAndWait();
        result.ifPresent(item -> {
            items.add(item);
            safeSave(currentFilePath);
            setStatus("Added item. Saved to: " + currentFilePath);
        });
    }

    private void onEdit() {
        PlannerItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Select an item to edit.");
            return;
        }
        PlannerItemDialog dialog = new PlannerItemDialog(selected);
        Optional<PlannerItem> result = dialog.showAndWait();
        result.ifPresent(updated -> {
            int idx = items.indexOf(selected);
            if (idx >= 0) {
                items.set(idx, updated);
                safeSave(currentFilePath);
                setStatus("Edited item. Saved to: " + currentFilePath);
            }
        });
    }

    private void onDelete() {
        PlannerItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Select an item to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete selected item?");
        confirm.setContentText(selected.getTitle());
        Optional<ButtonType> choice = confirm.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            items.remove(selected);
            safeSave(currentFilePath);
            setStatus("Deleted item. Saved to: " + currentFilePath);
        }
    }

    private void onMarkComplete() {
        PlannerItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Select a task to mark complete.");
            return;
        }
        if (!(selected instanceof Task)) {
            alert("Mark Complete only applies to Tasks.");
            return;
        }
        Task t = (Task) selected;
        t.markComplete();
        table.refresh();
        safeSave(currentFilePath);
        setStatus("Task marked complete. Saved to: " + currentFilePath);
    }

    private void onLoad(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Planner Data File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            currentFilePath = file.getAbsolutePath();
            safeLoad(currentFilePath);
            setStatus("Loaded from: " + currentFilePath);
        }
    }

    private void onSaveAs(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Planner Data File As");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            currentFilePath = file.getAbsolutePath();
            safeSave(currentFilePath);
            setStatus("Saved to: " + currentFilePath);
        }
    }

    private void safeLoad(String filePath) {
        try {
            ArrayList<PlannerItem> loaded = storage.loadItems(filePath);
            items.setAll(loaded);
            setStatus("Loaded " + loaded.size() + " item(s).");
        } catch (Exception ex) {
            alert("Load failed: " + ex.getMessage());
            setStatus("Load failed.");
        }
    }

    private void safeSave(String filePath) {
        try {
            storage.saveItems(filePath, items);
        } catch (Exception ex) {
            alert("Save failed: " + ex.getMessage());
            setStatus("Save failed.");
        }
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    private void alert(String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Task & Appointment Planner");
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
