package ui;

import domain.Student;
import service.StudentService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentForm {
    private static StudentService service = new StudentService();

    public static Student showAndWait(Student existing) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(existing == null ? "Register Student" : "Edit Student");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10); grid.setHgap(10);

        TextField txtId = new TextField(); txtId.setPromptText("Ex: STU101");
        TextField txtName = new TextField(); txtName.setPromptText("Full Name");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("email@domain.com");
        TextField txtGpa = new TextField(); txtGpa.setPromptText("0.0 - 4.0");
        ComboBox<String> cbStatus = new ComboBox<>();
        cbStatus.getItems().addAll("Active", "Inactive");
        cbStatus.setValue("Active");

        if (existing != null) {
            txtId.setText(existing.getStudentId()); txtId.setEditable(false);
            txtName.setText(existing.getFullName());
            txtEmail.setText(existing.getEmail());
            txtGpa.setText(String.valueOf(existing.getGpa()));
            cbStatus.setValue(existing.getStatus());
        }

        grid.add(new Label("ID:"), 0, 0); grid.add(txtId, 1, 0);
        grid.add(new Label("Name:"), 0, 1); grid.add(txtName, 1, 1);
        grid.add(new Label("Email:"), 0, 2); grid.add(txtEmail, 1, 2);
        grid.add(new Label("GPA:"), 0, 3); grid.add(txtGpa, 1, 3);
        grid.add(new Label("Status:"), 0, 4); grid.add(cbStatus, 1, 4);

        Button btnSave = new Button("Save");
        final Student[] result = {null};

        btnSave.setOnAction(e -> {
            try {
                Student s = new Student();
                s.setStudentId(txtId.getText());
                s.setFullName(txtName.getText());
                s.setEmail(txtEmail.getText());
                s.setGpa(Double.parseDouble(txtGpa.getText()));
                s.setStatus(cbStatus.getValue());

                service.validateForUI(s); // Only proceeds if valid
                result[0] = s;
                window.close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        grid.add(btnSave, 1, 5);
        window.setScene(new Scene(grid, 400, 450));
        window.showAndWait();
        return result[0];
    }
}