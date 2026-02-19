package application;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A simple JavaFX panel that collects JDBC connection info and returns a Connection.
 * Mirrors the idea of Liang's DBConnectionPanel (Exercise 34.3).
 */
public class DBConnectionPanel extends GridPane {

    private final TextField tfDriver = new TextField("com.mysql.cj.jdbc.Driver");
    private final TextField tfUrl = new TextField("jdbc:mysql://localhost:3306/test");
    private final TextField tfUser = new TextField("root");
    private final PasswordField pfPassword = new PasswordField();

    private final Label lblStatus = new Label("Not connected");

    public DBConnectionPanel() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        tfDriver.setPrefColumnCount(35);
        tfUrl.setPrefColumnCount(35);

        add(new Label("JDBC Driver"), 0, 0);
        add(tfDriver, 1, 0);

        add(new Label("Database URL"), 0, 1);
        add(tfUrl, 1, 1);

        add(new Label("Username"), 0, 2);
        add(tfUser, 1, 2);

        add(new Label("Password"), 0, 3);
        add(pfPassword, 1, 3);

        add(new Label("Status"), 0, 4);
        add(lblStatus, 1, 4);
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String driver = tfDriver.getText().trim();
        String url = tfUrl.getText().trim();
        String user = tfUser.getText().trim();
        String pass = pfPassword.getText(); // may be blank

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, pass);
        lblStatus.setText("Connected");
        return conn;
    }
}
