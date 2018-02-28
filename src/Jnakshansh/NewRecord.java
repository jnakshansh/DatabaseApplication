package Jnakshansh;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class NewRecord {

    @FXML
    private TextField enrollment_no;
    @FXML
    private TextField student_name;
    @FXML
    private TextField cgpa;
    @FXML
    private CheckBox check;

    private boolean confirm = false;

    public void confirm() {
        confirm = check.isSelected();
    }

    public void addNewStudent() {
        Database db = new Database();
        if (confirm) {
            db.open();
            try {
                int enroll_id = Integer.parseInt(enrollment_no.getText());
                String name = student_name.getText().toUpperCase();
                double student_cgpa = Double.parseDouble(cgpa.getText());
                System.out.println(db.addNewRecord(enroll_id, name, student_cgpa));

            } catch (Exception e) {
                System.out.println("Can not resolve data type: " + e.getMessage());
            }
            db.close();
        }
    }
}