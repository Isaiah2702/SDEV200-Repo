import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.*;

public class StaffApp extends Application {

    // ---- UPDATE THESE FOR YOUR LOCAL MYSQL SETUP ----
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/javabook?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "isaiah2702";
    // -------------------------------------------------

    private final TextField tfId = new TextField();
    private final TextField tfLastName = new TextField();
    private final TextField tfFirstName = new TextField();
    private final TextField tfMi = new TextField();
    private final TextField tfAddress = new TextField();
    private final TextField tfCity = new TextField();
    private final TextField tfState = new TextField();
    private final TextField tfTelephone = new TextField();
    private final TextField tfEmail = new TextField();

    @Override
    public void start(Stage stage) {
        tfMi.setPrefColumnCount(1);
        tfState.setPrefColumnCount(2);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(12));
        grid.setHgap(10);
        grid.setVgap(10);

        int r = 0;
        grid.add(new Label("ID"), 0, r);
        grid.add(tfId, 1, r++);

        grid.add(new Label("Last Name"), 0, r);
        grid.add(tfLastName, 1, r++);

        grid.add(new Label("First Name"), 0, r);
        grid.add(tfFirstName, 1, r++);

        grid.add(new Label("MI"), 0, r);
        grid.add(tfMi, 1, r++);

        grid.add(new Label("Address"), 0, r);
        grid.add(tfAddress, 1, r++);

        grid.add(new Label("City"), 0, r);
        grid.add(tfCity, 1, r++);

        grid.add(new Label("State"), 0, r);
        grid.add(tfState, 1, r++);

        grid.add(new Label("Telephone"), 0, r);
        grid.add(tfTelephone, 1, r++);

        grid.add(new Label("Email"), 0, r);
        grid.add(tfEmail, 1, r++);

        Button btnView = new Button("View");
        Button btnInsert = new Button("Insert");
        Button btnUpdate = new Button("Update");

        HBox buttons = new HBox(10, btnView, btnInsert, btnUpdate);
        buttons.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttons, 1, r);

        btnView.setOnAction(e -> viewStaff());
        btnInsert.setOnAction(e -> insertStaff());
        btnUpdate.setOnAction(e -> updateStaff());

        Scene scene = new Scene(grid, 520, 420);
        stage.setTitle("Staff Database");
        stage.setScene(scene);
        stage.show();
    }

    private void viewStaff() {
        String id = tfId.getText().trim();
        if (!isValidId(id)) return;

        String sql = "SELECT id, lastName, firstName, mi, address, city, state, telephone, email " +
                "FROM Staff WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tfId.setText(rs.getString("id"));
                    tfLastName.setText(nullToEmpty(rs.getString("lastName")));
                    tfFirstName.setText(nullToEmpty(rs.getString("firstName")));
                    tfMi.setText(nullToEmpty(rs.getString("mi")));
                    tfAddress.setText(nullToEmpty(rs.getString("address")));
                    tfCity.setText(nullToEmpty(rs.getString("city")));
                    tfState.setText(nullToEmpty(rs.getString("state")));
                    tfTelephone.setText(nullToEmpty(rs.getString("telephone")));
                    tfEmail.setText(nullToEmpty(rs.getString("email")));
                } else {
                    showInfo("Not found", "No staff record exists for ID: " + id);
                }
            }

        } catch (SQLException ex) {
            showError("Database error (View)", ex);
        }
    }

    private void insertStaff() {
        String id = tfId.getText().trim();
        if (!isValidId(id)) return;
        if (!isValidLengths()) return;

        String sql = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            fillInsertStatement(ps);

            int rows = ps.executeUpdate();
            if (rows == 1) showInfo("Inserted", "Record inserted for ID: " + id);

        } catch (SQLIntegrityConstraintViolationException dup) {
            showInfo("Insert failed", "A record with ID " + id + " already exists.");
        } catch (SQLException ex) {
            showError("Database error (Insert)", ex);
        }
    }

    private void updateStaff() {
        String id = tfId.getText().trim();
        if (!isValidId(id)) return;
        if (!isValidLengths()) return;

        String sql = "UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, city=?, state=?, telephone=?, email=? " +
                "WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emptyToNull(tfLastName.getText()));
            ps.setString(2, emptyToNull(tfFirstName.getText()));
            ps.setString(3, emptyToNull(tfMi.getText()));
            ps.setString(4, emptyToNull(tfAddress.getText()));
            ps.setString(5, emptyToNull(tfCity.getText()));
            ps.setString(6, emptyToNull(tfState.getText()));
            ps.setString(7, emptyToNull(tfTelephone.getText()));
            ps.setString(8, emptyToNull(tfEmail.getText()));
            ps.setString(9, id);

            int rows = ps.executeUpdate();
            if (rows == 1) {
                showInfo("Updated", "Record updated for ID: " + id);
            } else {
                showInfo("Update failed", "No record exists for ID: " + id);
            }

        } catch (SQLException ex) {
            showError("Database error (Update)", ex);
        }
    }

    private void fillInsertStatement(PreparedStatement ps) throws SQLException {
        ps.setString(1, tfId.getText().trim());
        ps.setString(2, emptyToNull(tfLastName.getText()));
        ps.setString(3, emptyToNull(tfFirstName.getText()));
        ps.setString(4, emptyToNull(tfMi.getText()));
        ps.setString(5, emptyToNull(tfAddress.getText()));
        ps.setString(6, emptyToNull(tfCity.getText()));
        ps.setString(7, emptyToNull(tfState.getText()));
        ps.setString(8, emptyToNull(tfTelephone.getText()));
        ps.setString(9, emptyToNull(tfEmail.getText()));
    }

    private boolean isValidId(String id) {
        if (id.isEmpty()) {
            showInfo("Validation", "ID is required (char(9)).");
            return false;
        }
        if (id.length() != 9) {
            showInfo("Validation", "ID must be exactly 9 characters (char(9)).");
            return false;
        }
        return true;
    }

    private boolean isValidLengths() {
        if (tfLastName.getText().trim().length() > 15) return tooLong("Last Name", 15);
        if (tfFirstName.getText().trim().length() > 15) return tooLong("First Name", 15);
        if (tfMi.getText().trim().length() > 1) return tooLong("MI", 1);
        if (tfAddress.getText().trim().length() > 20) return tooLong("Address", 20);
        if (tfCity.getText().trim().length() > 20) return tooLong("City", 20);
        if (tfState.getText().trim().length() > 2) return tooLong("State", 2);
        if (tfTelephone.getText().trim().length() > 10) return tooLong("Telephone", 10);
        if (tfEmail.getText().trim().length() > 40) return tooLong("Email", 40);
        return true;
    }

    private boolean tooLong(String field, int max) {
        showInfo("Validation", field + " is too long. Max length is " + max + ".");
        return false;
    }

    private static String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showError(String title, Exception ex) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(title);
        a.setContentText(ex.getMessage());
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
