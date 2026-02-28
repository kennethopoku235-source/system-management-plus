package ui;

import service.StudentService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow {
    private StudentService service = new StudentService();

    public void show(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Admin Login");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        TextField txtUser = new TextField();
        txtUser.setPromptText("Username");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");

        Button btnLogin = new Button("Login");
        btnLogin.setDefaultButton(true);

        btnLogin.setOnAction(e -> {
            String user = txtUser.getText();
            String pass = txtPass.getText();

            if (service.login(user, pass)) {
                MainApp dashboard = new MainApp();
                dashboard.setCurrentAdmin(user); // Now this method exists!

                try {
                    dashboard.start(new Stage());
                    stage.close(); // Close login window
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid credentials!").show();
            }
        });

        root.getChildren().addAll(title, txtUser, txtPass, btnLogin);
        stage.setScene(new Scene(root, 350, 300));
        stage.setTitle("SMS - Login");
        stage.show();
    }
}