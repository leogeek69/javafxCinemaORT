// ModifierCinemaController.java
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
        // hydrate les donnees metier pour edition
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<String> villes = cinemaDAO.getVilles();
        lvVille.setItems(FXCollections.observableArrayList(villes));

        FranchiseDAO franchiseDAO = new FranchiseDAO();
        List<String> franchises = franchiseDAO.getNomFranchiseString();
        lvNomFranchise.setItems(FXCollections.observableArrayList(franchises));
    }

    public void setIdSec(int idCin) {
        // utilitaire de passation parametre
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
        // remplis le formulaire selon l'objet recu par la table
        this.idCin = cinema.getIdCinema();
        tfNomCinema.setText(cinema.getDenomination());
        tfAdresseCinema.setText(cinema.getAdresse());
        lvVille.getSelectionModel().select(cinema.getVille());
        lvNomFranchise.getSelectionModel().select(cinema.getNomFranchise());
    }

    @FXML
    private void bRetourClick(ActionEvent event) {
        // annule completement la procedure d'edition
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
        // collecte du formulaire edite
        String nomCinema = tfNomCinema.getText();
        String adresseCinema = tfAdresseCinema.getText();
        String ville = lvVille.getSelectionModel().getSelectedItem();
        String nomSelectionne = lvNomFranchise.getSelectionModel().getSelectedItem();

        FranchiseDAO franchiseDAO = new FranchiseDAO();
        Franchise franchise = franchiseDAO.findByNom(nomSelectionne);
        int idFranchise = franchise.getIdFranchise();

        // s'assure qu'aucun dommage n'est fait aux valeurs obligatoires
        if (nomCinema != null && adresseCinema != null && ville != null && franchise != null) {
            Cinema sec = new Cinema(idCin, nomCinema, adresseCinema, ville, idFranchise);
            CinemaDAO cinemaDAO = new CinemaDAO();
            boolean controle = cinemaDAO.update(sec);

            if (controle) {
                // si ok on purge la pile et remet la data a jour
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
        } else {
            // declenche avertissement format s'il manque qch
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/popup_ajout_etu.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Erreur de saisie");
                stage.setScene(new Scene(root));
                setIcone(stage);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}