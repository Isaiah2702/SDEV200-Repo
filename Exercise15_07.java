import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Exercise15_07 extends Application {

    @Override
    public void start(Stage stage) {

        // Create circle
        Circle circle = new Circle(100);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        // Mouse pressed -> black
        circle.setOnMousePressed(e -> {
            circle.setFill(Color.BLACK);
        });

        // Mouse released -> white
        circle.setOnMouseReleased(e -> {
            circle.setFill(Color.WHITE);
        });

        // Layout
        StackPane pane = new StackPane(circle);

        Scene scene = new Scene(pane, 300, 300);
        stage.setTitle("Exercise 15.7 - Change Color");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
