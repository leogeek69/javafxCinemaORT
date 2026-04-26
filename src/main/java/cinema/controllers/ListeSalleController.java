package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import cinema.BO.Salle;
import cinema.BO.Cinema;
import cinema.DAO.SalleDAO;
import cinema.DAO.CinemaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ListeSalleController extends MenuController implements Initializable {

    @FXML
    private TableView<Salle> tvSalles;

    @FXML
    private TableColumn<Salle, Integer> tcNumSalle;

    @FXML
    private TableColumn<Salle, String> tcDescSalle;

    @FXML
    private TableColumn<Salle, Integer> tcNbPlaces;

    @FXML
    private TableColumn<Salle, String> tcCinema;

    @FXML
    private TableColumn<Salle, Void> tcModifier;

    @FXML
    private TableColumn<Salle, Void> tcSupprimer;

    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CinemaDAO cinemaDAO = new CinemaDAO();

        // Récupération de tous les cinémas pour mapper l'idCinema avec le nom du cinéma
        Map<Integer, Cinema> cinemas = cinemaDAO.findAll()
                .stream()
                .collect(Collectors.toMap(Cinema::getIdCinema, c -> c));

        // Affichage personnalisé pour la colonne Cinéma
        tcCinema.setCellValueFactory(cellData -> {
            Cinema cinema = cinemas.get(cellData.getValue().getIdCinema());
            // On suppose que ta classe Cinema a une méthode getDenomination() (basé sur ton SQL)
            return new SimpleStringProperty(
                    cinema != null ? cinema.getDenomination() : "Aucun cinéma");
        });

        tcNumSalle.setCellValueFactory(new PropertyValueFactory<>("numSalle"));
        tcDescSalle.setCellValueFactory(new PropertyValueFactory<>("descSalle"));
        tcNbPlaces.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));

        ObservableList<Salle> data = getSalleList();
        tvSalles.setItems(data);

        addButtonModifierToTable();
        addButtonSupprimerToTable();
    }

    private ObservableList<Salle> getSalleList() {
        SalleDAO salleDAO = new SalleDAO();
        List<Salle> listeSalles = salleDAO.findAll();

        ObservableList<Salle> list = FXCollections.observableArrayList();
        if (listeSalles != null) {
            list.addAll(listeSalles);
        }
        return list;
    }

    @FXML
    private void bRetourClick() {
        Stage stageP = (Stage) bRetour.getScene().getWindow();
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
            stage.setTitle("Accueil");
            stage.setScene(new Scene(root));

            // Configurer la fenêtre en tant que modal
            stage.initModality(Modality.APPLICATION_MODAL);

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addButtonModifierToTable() {
        tcModifier.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setOnAction(event -> {
                    Salle salle = getTableView().getItems().get(getIndex());
                    Stage stageP = (Stage) bRetour.getScene().getWindow();
                    stageP.close();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(
                                getClass().getResource("/cinema/views/page_modif_salle.fxml"));
                        Parent root = fxmlLoader.load();

                        // Assure-toi d'avoir créé le ModifierSalleController avec ces méthodes
                        /*ModifierSalleController modifierSalleCtrl = fxmlLoader.getController();
                        modifierSalleCtrl.setAttributes(salle);
                        modifierSalleCtrl.setName(nameUti);*/

                        Stage stage = new Stage();
                        stage.setTitle("Modification salle");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL);

                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void addButtonSupprimerToTable() {
        tcSupprimer.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Salle salle = getTableView().getItems().get(getIndex());
                    // Suppression de la vue
                    tvSalles.getItems().remove(salle);
                    // Suppression en base de données
                    SalleDAO salleDAO = new SalleDAO();
                    salleDAO.delete(salle);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
}