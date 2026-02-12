import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Exercise16_SetClockTime extends Application {

    @Override
    public void start(Stage stage) {
        ClockPane clock = new ClockPane();
        clock.setMinSize(200, 200); // allows it to grow/shrink nicely

        TextField tfHour = new TextField();
        tfHour.setPrefColumnCount(2);

        TextField tfMinute = new TextField();
        tfMinute.setPrefColumnCount(2);

        TextField tfSecond = new TextField();
        tfSecond.setPrefColumnCount(2);

        Button btSet = new Button("Set Time");

        // Optional: preload current time into fields
        tfHour.setText(String.valueOf(clock.getHour()));
        tfMinute.setText(String.valueOf(clock.getMinute()));
        tfSecond.setText(String.valueOf(clock.getSecond()));

        btSet.setOnAction(e -> {
            try {
                int h = Integer.parseInt(tfHour.getText().trim());
                int m = Integer.parseInt(tfMinute.getText().trim());
                int s = Integer.parseInt(tfSecond.getText().trim());

                if (h < 0 || h > 23) throw new IllegalArgumentException("Hour must be 0–23.");
                if (m < 0 || m > 59) throw new IllegalArgumentException("Minute must be 0–59.");
                if (s < 0 || s > 59) throw new IllegalArgumentException("Second must be 0–59.");

                clock.setHour(h);
                clock.setMinute(m);
                clock.setSecond(s);

            } catch (NumberFormatException ex) {
                showError("Please enter integers for hour, minute, and second.");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        HBox controls = new HBox(10,
                new Label("Hour"), tfHour,
                new Label("Minute"), tfMinute,
                new Label("Second"), tfSecond,
                btSet
        );
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(clock);     // clock centered
        root.setBottom(controls);  // fields + button at bottom
        BorderPane.setAlignment(clock, Pos.CENTER);

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Set Clock Time");
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Invalid Input");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
