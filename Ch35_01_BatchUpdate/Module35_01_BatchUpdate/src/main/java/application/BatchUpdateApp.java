package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Random;

/**
 * Liang Ch. 35 - Programming Exercise 35.1
 * Inserts 1,000 records into Temp(num1, num2, num3) and compares time
 * with and without JDBC batch updates.
 *
 * JavaFX-only GUI (no Swing/AWT).
 */
public class BatchUpdateApp extends Application {

    private Connection connection; // set after "Connect to Database"
    private final TextArea taOutput = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        Button btConnect = new Button("Connect to Database");
        Button btInsertNoBatch = new Button("Insert 1000 Records (No Batch)");
        Button btInsertBatch = new Button("Insert 1000 Records (Batch Update)");

        btInsertNoBatch.setDisable(true);
        btInsertBatch.setDisable(true);

        taOutput.setEditable(false);
        taOutput.setWrapText(true);

        btConnect.setOnAction(e -> {
            Connection conn = showConnectionDialog(primaryStage);
            if (conn != null) {
                this.connection = conn;
                taOutput.appendText("Connected.\n");
                btInsertNoBatch.setDisable(false);
                btInsertBatch.setDisable(false);
            } else {
                taOutput.appendText("Connection canceled or failed.\n");
            }
        });

        btInsertNoBatch.setOnAction(e -> runNoBatch());

        btInsertBatch.setOnAction(e -> runBatch());

        HBox top = new HBox(10, btConnect, btInsertNoBatch, btInsertBatch);
        top.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(new ScrollPane(taOutput));

        Scene scene = new Scene(root, 950, 520);
        primaryStage.setTitle("PE 35.1 - Batch Update Performance");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runNoBatch() {
        if (connection == null) return;
        try {
            ensureTempTable(connection);
            clearTempTable(connection);

            long ms = insertNoBatch(connection, 1000);
            taOutput.appendText("No-batch insert time (ms): " + ms + "\n");
        } catch (Exception ex) {
            taOutput.appendText("ERROR (no batch): " + ex.getMessage() + "\n");
        }
    }

    private void runBatch() {
        if (connection == null) return;
        try {
            ensureTempTable(connection);
            clearTempTable(connection);

            long ms = insertWithBatch(connection, 1000);
            taOutput.appendText("Batch insert time (ms): " + ms + "\n");
        } catch (Exception ex) {
            taOutput.appendText("ERROR (batch): " + ex.getMessage() + "\n");
        }
    }

    /**
     * Shows a modal dialog containing DBConnectionPanel.
     * Returns a Connection if the user successfully connects.
     */
    private Connection showConnectionDialog(Stage owner) {
        DBConnectionPanel panel = new DBConnectionPanel();

        ButtonType connectType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Dialog<Connection> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Database Connection");
        dialog.getDialogPane().setContent(panel);
        dialog.getDialogPane().getButtonTypes().addAll(connectType, cancelType);

        dialog.setResultConverter(btn -> {
            if (btn == connectType) {
                try {
                    return panel.getConnection();
                } catch (Exception ex) {
                    showError("Connection failed", ex);
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private void ensureTempTable(Connection conn) throws SQLException {
        String ddl = "CREATE TABLE IF NOT EXISTS Temp(" +
                "num1 DOUBLE, num2 DOUBLE, num3 DOUBLE" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(ddl);
        }
    }

    private void clearTempTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Temp");
        }
    }

    /**
     * Inserts rows one-by-one (no batch).
     */
    private long insertNoBatch(Connection conn, int rows) throws SQLException {
        String sql = "INSERT INTO Temp(num1, num2, num3) VALUES (?, ?, ?)";
        Random r = new Random();

        long start = System.currentTimeMillis();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < rows; i++) {
                ps.setDouble(1, r.nextDouble()); // like Math.random()
                ps.setDouble(2, r.nextDouble());
                ps.setDouble(3, r.nextDouble());
                ps.executeUpdate();
            }
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Inserts rows using JDBC batch updates for faster performance.
     */
    private long insertWithBatch(Connection conn, int rows) throws SQLException {
        String sql = "INSERT INTO Temp(num1, num2, num3) VALUES (?, ?, ?)";
        Random r = new Random();

        boolean oldAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false); // important for batching speed

        long start = System.currentTimeMillis();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < rows; i++) {
                ps.setDouble(1, r.nextDouble());
                ps.setDouble(2, r.nextDouble());
                ps.setDouble(3, r.nextDouble());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(oldAutoCommit);
        }

        return System.currentTimeMillis() - start;
    }

    private void showError(String title, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(ex.getClass().getSimpleName());
        alert.setContentText(ex.getMessage());
        alert.showAndWait();
    }

    @Override
    public void stop() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException ignored) {}
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
