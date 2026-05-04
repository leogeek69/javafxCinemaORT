package cinema.controllers;

import cinema.BO.Cinema;
import cinema.BO.Franchise;
import cinema.DAO.CinemaDAO;
import cinema.DAO.FranchiseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterCinemaController extends MenuController implements Initializable {
    @FXML
    private TextField tfDenominationCinema, tfAdresseCinema;
    @FXML
    private Button bRetour;
    @FXML
    private ListView<Cinema> lvVille;
    @FXML
    private ListView<Franchise> lvNomFranchise;
    @FXML
    private ComboBox<String> comboBoxAdresses;

    public void initialize(URL location, ResourceBundle resources) {
        // charge les listes de choix au demarrage
        ObservableList<Cinema> cinemasVille = getVilleCinemaList();
        lvVille.setItems(cinemasVille);

        ObservableList<Franchise> nomFranchise = getNomFranchise();
        lvNomFranchise.setItems(nomFranchise);
    }

    private ObservableList<Cinema> getVilleCinemaList() {
        // interroge la bdd pour avoir les villes uniques
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> cinemas = cinemaDAO.getAllVille();
        return FXCollections.observableArrayList(cinemas);
    }

    private ObservableList<Franchise> getNomFranchise() {
        // interroge la bdd pour avoir les noms des franchises
        FranchiseDAO franchiseDAO = new FranchiseDAO();
        List<Franchise> franchise = franchiseDAO.getNomFranchise();
        return FXCollections.observableArrayList(franchise);
    }

    @FXML
    public void bRetourClick(ActionEvent event) {
        // ferme la fenetre courante et retourne au menu d'accueil
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

    @FXML
    public void bEnregistrerClick(ActionEvent event) {
        // recupere les informations du formulaire
        String nomCinema = tfDenominationCinema.getText();
        String adresseCinema = tfAdresseCinema.getText();
        Cinema villeCinema = lvVille.getSelectionModel().getSelectedItem();
        Franchise franchiseSelect = lvNomFranchise.getSelectionModel().getSelectedItem();

        // verifie que tout est correctement saisi et selectionne
        if (nomCinema == null || nomCinema.isEmpty() || adresseCinema == null || adresseCinema.isEmpty() || villeCinema == null || franchiseSelect == null) {
            return;
        }

        // prepare l'objet et procede a l'insertion bdd
        String ville = villeCinema.getVille();
        int idFranchise = franchiseSelect.getIdFranchise();
        Cinema cinema = new Cinema(0, nomCinema, adresseCinema, ville, idFranchise);

        CinemaDAO cinemaDAO = new CinemaDAO();
        boolean controle = cinemaDAO.create(cinema);

        // reinitialise le formulaire si l'ajout a reussi
        if (controle) {
            bEffacerClick(null);
        }
    }

    @FXML
    public void bEffacerClick(ActionEvent event) {
        // vide tous les champs textes et deselectionne les listes
        if (tfDenominationCinema != null) tfDenominationCinema.clear();
        tfAdresseCinema.clear();
        lvVille.getSelectionModel().clearSelection();
        lvNomFranchise.getSelectionModel().clearSelection();
    }
}