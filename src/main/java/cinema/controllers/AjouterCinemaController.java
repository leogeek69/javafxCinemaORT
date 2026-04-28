package cinema.controllers;

import cinema.BO.Cinema;
import cinema.BO.Franchise;
import cinema.BO.Cinema;
import cinema.DAO.CinemaDAO;
import cinema.DAO.FranchiseDAO;
import cinema.DAO.CinemaDAO;
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
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterCinemaController extends MenuController implements Initializable {
    @FXML
    private TextField tfDenominationCinema, tfAdresseCinema;
    @FXML
    private Button bRetour;
    @FXML
    private ListView<Cinema> lvVille;


    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Cinema> cinemas = getCinemaList();

        lvVille.setItems(cinemas);
    }

    private ObservableList<Cinema> getCinemaList() {

        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> cinemas = cinemaDAO.getAllVille();

        ObservableList<Cinema> list = FXCollections.observableArrayList(cinemas);
        return list;

    }

    @FXML
    public void bRetourClick(ActionEvent event) {
        // On fait le lien avec l'ecran actuel
        Stage stageP = (Stage) bRetour.getScene().getWindow();
        // on ferme l'écran
        stageP.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/cinema/views/page_accueil.fxml"));
            Parent root = fxmlLoader.load();

            AccueilController accueilController = fxmlLoader.getController();
            accueilController.setName(nameUti);
            accueilController.setBienvenue();

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Liste cinemas");
            stage.setScene(new Scene(root));

            // Configurer la fenêtre en tant que modal
            stage.initModality(Modality.APPLICATION_MODAL);

            // Afficher la fenêtre et attendre qu'elle se ferme
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void bEnregistrerClick(ActionEvent event) {

        String nomCinema = tfDenominationCinema.getText();
        String adresseCinema = tfAdresseCinema.getText();
        Cinema villeCinema = lvVille.getSelectionModel().getSelectedItem();

        //on vérifie que rien n'est vide et qu'un gérant est bien cliqué
        if (nomCinema == null || nomCinema.isEmpty() ||
                adresseCinema == null || adresseCinema.isEmpty() ||
                villeCinema == null) {

            System.out.println("Erreur : Vous devez remplir tous les champs");

            return;
        }

        //si c'est sélectionné on valides
        String ville = villeCinema.getDenomination();
        int idCinema = villeCinema.getIdFranchise();
        Cinema cinema = new Cinema(0, nomCinema, adresseCinema, ville, idCinema);

        CinemaDAO cinemaDAO = new CinemaDAO();
        boolean controle = cinemaDAO.create(cinema);

        if (controle) {
            tfDenominationCinema.clear();
            tfAdresseCinema.clear();
            lvVille.getSelectionModel().clearSelection();
        }
    }

    @FXML
    public void bEffacerClick(ActionEvent event) {
        if (tfDenominationCinema != null)
            tfDenominationCinema.clear();
        if (tfAdresseCinema != null)
            tfAdresseCinema.clear();
        lvVille.getSelectionModel().clearSelection();
    }
}
