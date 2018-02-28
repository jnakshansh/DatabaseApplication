package Jnakshansh;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;


public class MainFile {

    Database db = new Database();
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private MenuItem addNewRecord;
    @FXML
    private MenuItem addBatchRecord;
    @FXML
    private MenuItem updateRecord;
    @FXML
    private MenuItem deleteRecord;
    @FXML
    private MenuItem exit;
    @FXML
    private TextField search_keyword;
    @FXML
    private Button search_button;
    @FXML
    private Label search_Label;
    @FXML
    private RadioButton by_id;
    @FXML
    private RadioButton by_name;
    @FXML
    private TableView<Jnakshansh.Student> tableView;
    private TableColumn<Student, Integer> enrollIdCol;
    private TableColumn<Student, String> nameCol;
    private TableColumn<Student, Double> cgpaCol;

    @FXML
    public void initialize() {
        search_button.setDisable(true);
        enrollIdCol = new TableColumn<>("Enroll_ID");
        enrollIdCol.setMinWidth(265);

        enrollIdCol.setCellValueFactory(new PropertyValueFactory<>("enroll_id"));
        nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(265);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cgpaCol = new TableColumn<>("CGPA");
        cgpaCol.setMinWidth(268);

        cgpaCol.setCellValueFactory(new PropertyValueFactory<>("cgpa"));

        tableView.getColumns().addAll(enrollIdCol, nameCol, cgpaCol);

    }

    @FXML
    public void addNewDialog(ActionEvent e) throws FileNotFoundException {
        if (e.getSource() == addNewRecord)
            addNewDialogBox("addNewRecord.fxml", "Add New Student Record");
        else if (e.getSource() == addBatchRecord)
            locateFile();
        else if (e.getSource() == updateRecord)
            updateRecordDialogBox("updateRecord.fxml", "Update Student Record");
        else if (e.getSource() == deleteRecord)
            deleteRecordDialogBox("deleteRecord.fxml", "Delete Student Record");
        else if (e.getSource() == exit)
            Platform.exit();
    }

    @FXML
    public void addNewDialogBox(String fxml_name, String title) {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(title);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml_name));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewRecord controller = fxmlLoader.getController();
            controller.addNewStudent();
            showTable();

        }
    }

    @FXML
    public void updateRecordDialogBox(String fxml_name, String title) {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(title);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml_name));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            UpdateRecord controller = fxmlLoader.getController();
            controller.updateStudent();
            showTable();


        }
    }

    @FXML
    public void deleteRecordDialogBox(String fxml_name, String title) {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(title);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml_name));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DeleteRecord controller = fxmlLoader.getController();
            controller.deleteStudent();
            showTable();

        }
    }

    @FXML
    public void showTable() {
        search_button.setDisable(true);
        db.open();
        search_keyword.setText(null);
        tableView.setItems(db.getStudents());
        db.close();
    }

    @FXML
    public void onTextField() {
        search_button.setDisable(search_keyword.getText().trim().isEmpty());
    }

    @FXML
    public void searchRecord() {
        try {
            if (by_id.isSelected()) {
                db.open();
                int enroll_id = Integer.parseInt(search_keyword.getText());
                if (!db.queryStudent(enroll_id)) {
                    search_Label.setText("Student ID is not available!!");
                    search_Label.setTextFill(Color.RED);
                    tableView.setItems(null);
                    return;
                }
                tableView.setItems(db.getStudentsSet(enroll_id));
                search_Label.setText("Student Record Found!!");
                search_Label.setTextFill(Color.GREEN);
                db.close();
            } else if (by_name.isSelected()) {
                db.open();
                String name = search_keyword.getText().toUpperCase();
                if (!db.queryStudent(name)) {
                    search_Label.setText("Student Name is not available!!");
                    search_Label.setTextFill(Color.RED);
                    tableView.setItems(null);
                    return;
                }
                tableView.setItems(db.getStudentsSet(name));
                search_Label.setText("Student Record Found!!");
                search_Label.setTextFill(Color.GREEN);
                db.close();
            }
        } catch (Exception e) {
            System.out.println("Can not resolve data type: " + e.getMessage());
            search_Label.setText("Entered Id is Invalid!!");
            search_Label.setTextFill(Color.RED);
            tableView.setItems(null);
        }
    }

    @FXML
    protected void locateFile() throws FileNotFoundException {

        ObservableList<String> status_data = FXCollections.observableArrayList();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        try {
            File file = chooser.showOpenDialog(new Stage());
            System.out.println(file.exists());
            System.out.println(file.isFile());

            System.out.println(file);
            Scanner sc = new Scanner(file);

            String[] list;
            int status = 0;

            db.open();
            while (sc.hasNextLine()) {
                list = sc.nextLine().split("\\t");
                status = db.addNewRecord(Integer.parseInt(list[0]), list[1].toUpperCase(), Double.parseDouble(list[2]));
                if (status == 1) {
                    status_data.add(list[0] + ": Added Successfully");
                } else if (status == 2) {
                    status_data.add(list[0] + ": Cannot add to Database");
                } else if (status == 3) {
                    status_data.add(list[0] + ": Already Exists");
                }
            }
            db.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}