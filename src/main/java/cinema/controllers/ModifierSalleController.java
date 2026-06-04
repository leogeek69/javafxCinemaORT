package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

public class ModifierSalleController extends MenuController implements Initializable {

    @FXML
    private TextField tfNumSalle, tfDescSalle, tfNbPlaces, tfParticularite;
    @FXML
    private Button bRetour;
    @FXML
    private ListView<Cinema> lvCinema;

    private int idSalle;
    private int idCinemaSelectionne;

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

    public void setAttributes(Salle salle) {
        this.idSalle = salle.getIdSalle();
        tfNumSalle.setText(String.valueOf(salle.getNumSalle()));
        tfDescSalle.setText(salle.getDescSalle());
        tfParticularite.setText(salle.getParticularite() != null ? salle.getParticularite() : "");
        tfNbPlaces.setText(String.valueOf(salle.getNbPlaces()));
        this.idCinemaSelectionne = salle.getIdCinema();

        ObservableList<Cinema> items = lvCinema.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIdCinema() == this.idCinemaSelectionne) {
                lvCinema.getSelectionModel().select(i);
                break;
            }
        }
    }

    @FXML
    private void bEnregistrerClick(ActionEvent event) {
        try {
            // Vérifie les champs vides avant le parsing pour un message plus clair
            if (tfNumSalle.getText().isEmpty() || tfNbPlaces.getText().isEmpty()) {
                afficherPopUpErreur("Champs incomplets", "Le numéro de salle et le nombre de places sont obligatoires.");
                return;
            }

            int numSalle = Integer.parseInt(tfNumSalle.getText());
            String descSalle = tfDescSalle.getText();
            String particularite = tfParticularite.getText();
            int nbPlaces = Integer.parseInt(tfNbPlaces.getText());
            Cinema selectedCinema = lvCinema.getSelectionModel().getSelectedItem();

            if (descSalle != null && selectedCinema != null && !descSalle.trim().isEmpty()) {
                int idNouveauCinema = selectedCinema.getIdCinema();

                Salle salleModifiee = new Salle(this.idSalle, numSalle, descSalle, particularite, nbPlaces, idNouveauCinema);

                SalleDAO salleDAO = new SalleDAO();
                boolean controle = salleDAO.update(salleModifiee);

                if (controle) {
                    Stage stageP = (Stage) bRetour.getScene().getWindow();
                    stageP.close();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(
                                getClass().getResource("/cinema/views/page_liste_salle.fxml"));
                        Parent root = fxmlLoader.load();

                        ListeSalleController listeSalleController = fxmlLoader.getController();
                        listeSalleController.setName(nameUti);

                        Stage stage = new Stage();
                        stage.setTitle("Liste salles");
                        stage.setScene(new Scene(root));

                        setIcone(stage);

                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    afficherPopUpErreur("Erreur BDD", "Impossible d'appliquer la modification en base de données.");
                }
            } else {
                afficherPopUpErreur("Champs incomplets", "Veuillez remplir la description et sélectionner un cinéma.");
            }
        } catch (NumberFormatException e) {
            afficherPopUpErreur("Format Incorrect", "Le numéro de salle et le nombre de places doivent être des nombres.");
        }
    }

    @FXML
    private void bRetourClick(ActionEvent event) {
        Stage stageP = (Stage) bRetour.getScene().getWindow();
        stageP.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/cinema/views/page_liste_salle.fxml"));
            Parent root = fxmlLoader.load();

            ListeSalleController listeSalleController = fxmlLoader.getController();
            listeSalleController.setName(nameUti);

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