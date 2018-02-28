package Jnakshansh;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class UpdateRecord {

    @FXML
    private TextField enrollment_no;
    @FXML
    private TextField student_name;
    @FXML
    private TextField cgpa;
    @FXML
    private Label search_label;
    @FXML
    private CheckBox check;

    private Database db = new Database();
    private boolean valid = false;
    private boolean confirm = false;

    @FXML
    public void initialize() {
        db.open();
    }

    public void confirm() {
        confirm = check.isSelected();
    }

    @FXML
    public void onButtonClicked() {
        try {
            if (db.queryStudent(Integer.parseInt(enrollment_no.getText()))) {
                search_label.setText("Student id exists...");
                search_label.setTextFill(Color.GREEN);
                valid = true;
            } else {
                search_label.setText("Invalid student id!");
                search_label.setTextFill(Color.RED);
                valid = false;
            }
        } catch (Exception e) {
            System.out.println("Can not search student id: " + e.getMessage());
            search_label.setTextFill(Color.RED);
            valid = false;
        }
    }

    @FXML
    public void updateStudent() {
        if (valid && confirm) {
            try {
                int enroll_id = Integer.parseInt(enrollment_no.getText());
                String name = student_name.getText().toUpperCase();
                double student_cgpa = Double.parseDouble(cgpa.getText());
                System.out.println(db.updateRecord(enroll_id, name, student_cgpa));
            } catch (Exception e) {
                System.out.println("Can not resolve data type: " + e.getMessage());
            }
        }
        db.close();
    }
}
