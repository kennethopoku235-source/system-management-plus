package ui;

import domain.Student;
import service.StudentService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class MainApp extends Application {
    private StudentService service = new StudentService();
    private String currentAdmin;
    private BorderPane mainLayout = new BorderPane();
    private double atRiskThreshold = 2.0;

    public void setCurrentAdmin(String admin) {
        this.currentAdmin = admin;
    }

    @Override
    public void start(Stage primaryStage) {
        if (currentAdmin == null) currentAdmin = "Administrator";

        primaryStage.setTitle("Student Management System");

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-min-width: 220;");

        Label lblBrand = new Label("SMS PLUS");
        lblBrand.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold;");

        Label lblUser = new Label("Logged in: " + currentAdmin);
        lblUser.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 12;");

        Button btnDash = createNavBtn("Dashboard");
        Button btnStudents = createNavBtn("Students List");
        Button btnImport = createNavBtn("Batch Import");
        Button btnSettings = createNavBtn("Settings");

        sidebar.getChildren().addAll(lblBrand, lblUser, new Separator(), btnDash, btnStudents, btnImport, btnSettings);

        btnDash.setOnAction(e -> showDashboard());
        btnStudents.setOnAction(e -> showStudentsScreen());
        btnImport.setOnAction(e -> showImportScreen());
        btnSettings.setOnAction(e -> showSettingsScreen());

        mainLayout.setLeft(sidebar);
        showDashboard();

        Scene scene = new Scene(mainLayout, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showDashboard() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));

        List<Student> all = service.getFiltered("", "ALL", "fullName", atRiskThreshold);

        HBox stats = new HBox(20);
        stats.getChildren().addAll(
                createStatCard("Total", String.valueOf(all.size()), "#3498db"),
                createStatCard("Avg GPA", String.format("%.2f", all.stream().mapToDouble(Student::getGpa).average().orElse(0.0)), "#9b59b6")
        );

        root.getChildren().addAll(new Label("Dashboard Overview"), stats);
        mainLayout.setCenter(root);
    }

    private void showStudentsScreen() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        TableView<Student> table = new TableView<>();
        setupTable(table);

        TextField search = new TextField();
        search.setPromptText("Search by name...");
        search.textProperty().addListener((o, old, val) -> {
            table.setItems(FXCollections.observableArrayList(service.getFiltered(val, "ALL", "fullName", atRiskThreshold)));
        });

        Button btnAdd = new Button("+ Add New");
        btnAdd.setOnAction(e -> {
            Student s = StudentForm.showAndWait(null);
            if (s != null) {
                try {
                    service.registerStudent(s, currentAdmin);
                    showStudentsScreen();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
                }
            }
        });

        root.getChildren().addAll(new HBox(10, search, btnAdd), table);
        table.setItems(FXCollections.observableArrayList(service.getFiltered("", "ALL", "fullName", atRiskThreshold)));
        mainLayout.setCenter(root);
    }

    private void showImportScreen() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        Button btn = new Button("Select CSV File");
        btn.setOnAction(e -> {
            File f = new FileChooser().showOpenDialog(null);
            if (f != null) {
                new Thread(() -> {
                    try {
                        String report = service.importFromCSV(f.getAbsolutePath(), currentAdmin);
                        Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, report).show());
                    } catch (Exception ex) {
                        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, ex.getMessage()).show());
                    }
                }).start();
            }
        });
        root.getChildren().addAll(new Label("Import Student Data"), btn);
        mainLayout.setCenter(root);
    }

    private void showSettingsScreen() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        TextField tf = new TextField(String.valueOf(atRiskThreshold));
        Button btn = new Button("Save Changes");
        btn.setOnAction(e -> {
            atRiskThreshold = Double.parseDouble(tf.getText());
            new Alert(Alert.AlertType.INFORMATION, "Settings Saved").show();
        });
        root.getChildren().addAll(new Label("GPA Threshold"), tf, btn);
        mainLayout.setCenter(root);
    }

    private void setupTable(TableView<Student> t) {
        TableColumn<Student, String> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<Student, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        t.getColumns().setAll(c1, c2);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private Button createNavBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER_LEFT;");
        return b;
    }

    private VBox createStatCard(String t, String v, String color) {
        VBox card = new VBox(5, new Label(t), new Label(v));
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");
        return card;
    }

    public static void main(String[] args) { launch(args); }
}