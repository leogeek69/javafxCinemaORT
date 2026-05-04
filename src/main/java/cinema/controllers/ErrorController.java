// ErrorController.java
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
    public void initialize(URL location, ResourceBundle resources) {}

    public void setMajLabel(String message) {
        // permet d'injecter du texte dynamique dans la popup
        labelMessage.setText(message);
    }

    public void ButtonOkOnAction(ActionEvent actionEvent) {
        // ferme la popup simplement
        Stage stage = (Stage) ButtonOk.getScene().getWindow();
        stage.close();
    }
}