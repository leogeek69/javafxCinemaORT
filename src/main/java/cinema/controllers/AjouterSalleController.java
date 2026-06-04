package cinema.controllers;

import cinema.BO.Cinema;
import cinema.BO.Salle;
import cinema.DAO.CinemaDAO;
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
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterSalleController extends MenuController implements Initializable {

    @FXML
    private TextField tfNumSalle, tfDescSalle, tfNbPlaces, tfParticularite;
    @FXML
    private Button bRetour;
    @FXML
    private ListView<Cinema> lvCinema;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Cinema> listeCinemas = getCinemaList();
        lvCinema.setItems(listeCinemas);
    }

    private ObservableList<Cinema> getCinemaList() {
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> cinemas = cinemaDAO.findAll();
        return FXCollections.observableArrayList(cinemas);
    }


    @FXML
    public void bEnregistrerClick(ActionEvent event) {
        try {
            // Vérification globale avant de tenter le parsing numérique
            if (tfNumSalle.getText().isEmpty() || tfNbPlaces.getText().isEmpty()) {
                afficherPopUpErreur("Champs manquants", "Le numéro de salle et le nombre de places sont obligatoires.");
                return;
            }

            int numSalle = Integer.parseInt(tfNumSalle.getText());
            String descSalle = tfDescSalle.getText();
            String particularite = tfParticularite.getText();
            int nbPlaces = Integer.parseInt(tfNbPlaces.getText());
            Cinema cinemaSelectionne = lvCinema.getSelectionModel().getSelectedItem();

            if (descSalle == null || descSalle.isEmpty()) {
                afficherPopUpErreur("Erreur de saisie", "La description de la salle est obligatoire.");
                return;
            }
            if (cinemaSelectionne == null) {
                afficherPopUpErreur("Sélection manquante", "Veuillez attribuer cette salle à un cinéma.");
                return;
            }

            Salle nouvelleSalle = new Salle(0, numSalle, descSalle, particularite, nbPlaces, cinemaSelectionne.getIdCinema());
            SalleDAO salleDAO = new SalleDAO();
            boolean controle = salleDAO.create(nouvelleSalle);

            if (controle) {
                bEffacerClick(null);
            } else {
                afficherPopUpErreur("Erreur BDD", "Impossible d'insérer la nouvelle salle.");
            }
        } catch (NumberFormatException e) {
            // Le catch attrape maintenant l'erreur de format et prévient l'utilisateur au lieu de l'ignorer !
            afficherPopUpErreur("Format Incorrect", "Le numéro de salle et le nombre de places doivent obligatoirement être des nombres.");
        }
    }

    @FXML
    public void bEffacerClick(ActionEvent event) {
        if (tfNumSalle != null) tfNumSalle.clear();
        if (tfDescSalle != null) tfDescSalle.clear();
        if (tfParticularite != null) tfParticularite.clear();
        if (tfNbPlaces != null) tfNbPlaces.clear();
        if (lvCinema != null) lvCinema.getSelectionModel().clearSelection();
    }

    @FXML
    public void bRetourClick(ActionEvent event) {
        Stage stageP = (Stage) bRetour.getScene().getWindow();
        stageP.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_accueil.fxml"));
            Parent root = fxmlLoader.load();

            AccueilController accueilController = fxmlLoader.getController();
            accueilController.setName(nameUti);
            accueilController.setBienvenue();

            Stage stage = new Stage();
            stage.setTitle("Accueil Gestion de Franchises");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}