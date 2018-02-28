package Jnakshansh;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DeleteRecord {

    @FXML
    private TextField enrollment_no;
    @FXML
    private Label search_label;
    @FXML
    private CheckBox deleteAll;
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
                enrollment_no.isDisabled();
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

    public void deleteStudent() {
        if (deleteAll.isSelected() && confirm) {
            db.deleteAll(true);
            db.close();
            return;
        }
        if (valid & confirm) {
            try {
                int enroll_id = Integer.parseInt(enrollment_no.getText());
                System.out.println(db.deleteRecord(enroll_id));
            } catch (Exception e) {
                System.out.println("Can not resolve data type: " + e.getMessage());
            }
        }
        db.close();
    }
}
