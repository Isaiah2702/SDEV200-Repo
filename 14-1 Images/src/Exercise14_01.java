import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Exercise14_01 extends Application {

    @Override
    public void start(Stage stage) {

        // Load the flag images
        ImageView flag1 = new ImageView(new Image("file:Images/flag1.gif"));
        ImageView flag2 = new ImageView(new Image("file:Images/flag2.gif"));
        ImageView flag6 = new ImageView(new Image("file:Images/flag6.gif"));
        ImageView flag7 = new ImageView(new Image("file:Images/flag7.gif"));

        // Optional sizing (keeps layout neat)
        flag1.setFitWidth(200);
        flag1.setPreserveRatio(true);

        flag2.setFitWidth(200);
        flag2.setPreserveRatio(true);

        flag6.setFitWidth(200);
        flag6.setPreserveRatio(true);

        flag7.setFitWidth(200);
        flag7.setPreserveRatio(true);

        // Create GridPane layout
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        pane.add(flag1, 0, 0);
        pane.add(flag2, 1, 0);
        pane.add(flag6, 0, 1);
        pane.add(flag7, 1, 1);

        Scene scene = new Scene(pane, 450, 350);
        stage.setTitle("Exercise 14.1 - Flags Grid");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
