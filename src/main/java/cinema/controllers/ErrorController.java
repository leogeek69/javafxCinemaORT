package cinema.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController implements Initializable {
    @FXML
    private Button ButtonOk;
    @FXML
    private Label labelMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setMajLabel(String message) {
        labelMessage.setText(message);
    }
    public void ButtonOkOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ButtonOk.getScene().getWindow();
        stage.close();

    }
}
