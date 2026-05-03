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
    private TextField tfNumSalle, tfDescSalle, tfNbPlaces;
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
            int numSalle = Integer.parseInt(tfNumSalle.getText());
            String descSalle = tfDescSalle.getText();
            int nbPlaces = Integer.parseInt(tfNbPlaces.getText());
            Cinema selectedCinema = lvCinema.getSelectionModel().getSelectedItem();

            if (descSalle != null && selectedCinema != null && !descSalle.trim().isEmpty()) {
                int idNouveauCinema = selectedCinema.getIdCinema();


                Salle salleModifiee = new Salle(this.idSalle, numSalle, descSalle, nbPlaces, idNouveauCinema);

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
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Veuillez remplir tous les champs correctement.");
                            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur");
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
            stage.setTitle("Liste salles");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}