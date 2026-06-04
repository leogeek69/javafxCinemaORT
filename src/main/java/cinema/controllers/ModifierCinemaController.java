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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierCinemaController extends MenuController implements Initializable {

    @FXML
    private TextArea taLibSec;
    @FXML
    private TextField tfNomCinema, tfAdresseCinema;
    @FXML
    private ListView<String> lvVille;
    @FXML
    private ListView<String> lvNomFranchise;

    private int idCin;

    @FXML
    private Button bRetour, bEnregistrer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<String> villes = cinemaDAO.getVilles();
        lvVille.setItems(FXCollections.observableArrayList(villes));

        FranchiseDAO franchiseDAO = new FranchiseDAO();
        List<String> franchises = franchiseDAO.getNomFranchiseString();
        lvNomFranchise.setItems(FXCollections.observableArrayList(franchises));
    }

    public void setIdSec(int idCin) {
        this.idCin = idCin;
    }

    private ObservableList<Franchise> getNomFranchise() {
        FranchiseDAO franchiseDAO = new FranchiseDAO();
        List<Franchise> franchise = franchiseDAO.getNomFranchise();
        return FXCollections.observableArrayList(franchise);
    }

    private ObservableList<Cinema> getVilleCinemaList() {
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> cinemas = cinemaDAO.getAllVille();
        return FXCollections.observableArrayList(cinemas);
    }

    public void setAttributes(Cinema cinema) {
        this.idCin = cinema.getIdCinema();
        tfNomCinema.setText(cinema.getDenomination());
        tfAdresseCinema.setText(cinema.getAdresse());
        lvVille.getSelectionModel().select(cinema.getVille());
        lvNomFranchise.getSelectionModel().select(cinema.getNomFranchise());
    }

    @FXML
    private void bRetourClick(ActionEvent event) {
        Stage stageP = (Stage) bRetour.getScene().getWindow();
        stageP.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_liste_cinema.fxml"));
            Parent root = fxmlLoader.load();

            ListeCinemaController listeCinemaController = fxmlLoader.getController();
            listeCinemaController.setName(nameUti);

            Stage stage = new Stage();
            stage.setTitle("Liste franchises");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void bEnregistrerClick(ActionEvent event) {
        String nomCinema = tfNomCinema.getText();
        String adresseCinema = tfAdresseCinema.getText();
        String ville = lvVille.getSelectionModel().getSelectedItem();
        String nomSelectionne = lvNomFranchise.getSelectionModel().getSelectedItem();

        // Utilisation de la nouvelle méthode d'erreur globale
        if (nomCinema == null || nomCinema.isEmpty() || adresseCinema == null || adresseCinema.isEmpty() || ville == null || nomSelectionne == null) {
            afficherPopUpErreur("Champs incomplets", "Tous les champs et sélections doivent être renseignés.");
            return;
        }

        FranchiseDAO franchiseDAO = new FranchiseDAO();
        Franchise franchise = franchiseDAO.findByNom(nomSelectionne);
        int idFranchise = franchise.getIdFranchise();

        Cinema sec = new Cinema(idCin, nomCinema, adresseCinema, ville, idFranchise);
        CinemaDAO cinemaDAO = new CinemaDAO();
        boolean controle = cinemaDAO.update(sec);

        if (controle) {
            Stage stageP = (Stage) bRetour.getScene().getWindow();
            stageP.close();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_liste_cinema.fxml"));
                Parent root = fxmlLoader.load();


                ListeCinemaController listeCinemaController = fxmlLoader.getController();
                listeCinemaController.setName(nameUti);

                Stage stage = new Stage();
                stage.setTitle("Liste franchises");
                stage.setScene(new Scene(root));
                setIcone(stage);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            afficherPopUpErreur("Erreur de modification", "Impossible de mettre à jour le cinéma dans la base de données.");
        }
    }
}