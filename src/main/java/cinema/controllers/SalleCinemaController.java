package cinema.controllers;

import cinema.BO.Cinema;
import cinema.BO.Salle;
import cinema.DAO.SalleDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SalleCinemaController extends MenuController implements Initializable {

    @FXML
    private ListView<Salle> lvSalles;

    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setCinema(Cinema cinema) {
        System.out.println("ID cinema : " + cinema.getIdCinema());
        SalleDAO salleDAO = new SalleDAO();
        List<Salle> salles = salleDAO.getAllByCinema(cinema.getIdCinema());
        System.out.println("Nb salles : " + salles.size());
        lvSalles.setItems(FXCollections.observableArrayList(salles));
    }

    @FXML
    private void bRetourClick(ActionEvent event) {
        Stage stageP = (Stage) bRetour.getScene().getWindow();
        stageP.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/cinema/views/page_liste_cinema.fxml"));
            Parent root = fxmlLoader.load();

            ListeCinemaController listeCinemaController = fxmlLoader.getController();
            listeCinemaController.setName(nameUti);

            Stage stage = new Stage();
            stage.setTitle("Liste cinemas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}