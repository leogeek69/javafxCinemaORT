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
        //on récupére la liste des cinémas pour les affecter à la salle
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
            //on récupère et on convertit des champs
            int numSalle = Integer.parseInt(tfNumSalle.getText());
            String descSalle = tfDescSalle.getText();
            String particularite = tfParticularite.getText();
            int nbPlaces = Integer.parseInt(tfNbPlaces.getText());
            Cinema cinemaSelectionne = lvCinema.getSelectionModel().getSelectedItem();

            //on vérifie que les champs sont pas vides (et qu'un cinéma est sélectionné)
            if (descSalle == null || descSalle.isEmpty() || cinemaSelectionne == null) {
                System.out.println("Erreur : Vous devez remplir tous les champs et sélectionner un cinéma.");
                return;
            }

            //création de l'objet Salle
            Salle nouvelleSalle = new Salle(0, numSalle, descSalle, particularite, nbPlaces, cinemaSelectionne.getIdCinema());

            // Sauvegarde en base de données
            SalleDAO salleDAO = new SalleDAO();
            boolean controle = salleDAO.create(nouvelleSalle);

            if (controle) {
                System.out.println("Succes : La salle a bien été ajoutee.");
                bEffacerClick(null); //on vide le formulaire après l'ajout
            } else {
                System.out.println("Erreur : Probleme lors de l'insertion en base de donnees.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Erreur");
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
            stage.setTitle("Accueil Gestion de Franchises");
            stage.setScene(new Scene(root));

            setIcone(stage);

            // Configurer la fenêtre en tant que modal
            stage.initModality(Modality.APPLICATION_MODAL);

            // Afficher la fenêtre et attendre qu'elle se ferme
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}