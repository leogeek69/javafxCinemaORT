package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cinema.BO.Cinema;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ListeCinemaController extends MenuController implements Initializable {

    @FXML
    private TableView<Cinema> tvCinema;
    @FXML
    private TableColumn<Cinema, String> tcDenomination, tcFranchise;
    @FXML
    private TableColumn<Cinema, Void> tcModif, tcSupp, tcVp;
    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // map les colonnes avec les attributs de l'objet cinema
        tcDenomination.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        tcFranchise.setCellValueFactory(new PropertyValueFactory<>("nomFranchise"));

        // initialise les colonnes d'actions (boutons)
        btnVoirPlus();
        btnModif();
        btnSupp();

        // peuple le tableau
        ObservableList<Cinema> data = getCinema();
        tvCinema.setItems(data);
    }

    private ObservableList<Cinema> getCinema() {
        // recupere tout le catalogue
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> mesCinemas = cinemaDAO.findAll();
        return FXCollections.observableArrayList(mesCinemas);
    }

    public void bRetourClick(ActionEvent actionEvent) {
        // gere le bouton de retour global
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

    private void btnVoirPlus() {
        // genere dynamiquement un bouton pour voir les details du cinema
        tcVp.setCellFactory(column -> new TableCell<Cinema, Void>() {
            private Button btn = new Button("Voir Plus");
            {
                btn.setOnAction(event -> {
                    Cinema cinema = getTableView().getItems().get(getIndex());
                    Stage stageP = (Stage) bRetour.getScene().getWindow();
                    stageP.close();
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_salles_cinema.fxml"));
                        Parent root = fxmlLoader.load();

                        SalleCinemaController salleCtrl = fxmlLoader.getController();
                        salleCtrl.setCinema(cinema);
                        salleCtrl.setName(nameUti);

                        Stage stage = new Stage();
                        stage.setTitle("Salles du cinema");
                        stage.setScene(new Scene(root));
                        setIcone(stage);
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

    private void btnModif() {
        // genere le bouton d'edition en passant l'objet courant
        tcModif.setCellFactory(column -> new TableCell<Cinema, Void>() {
            private Button btn = new Button("Modifier");
            {
                btn.setOnAction(event -> {
                    Cinema cinema = getTableView().getItems().get(getIndex());
                    Stage stageP = (Stage) bRetour.getScene().getWindow();
                    stageP.close();
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_modif_cinema.fxml"));
                        Parent root = fxmlLoader.load();

                        ModifierCinemaController modifierCinemaCtrl = fxmlLoader.getController();
                        modifierCinemaCtrl.setAttributes(cinema);
                        modifierCinemaCtrl.setName(nameUti);

                        Stage stage = new Stage();
                        stage.setTitle("Modification cinema");
                        stage.setScene(new Scene(root));
                        setIcone(stage);
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

    private void btnSupp() {
        // gere la suppression avec verification de dependances
        tcSupp.setCellFactory(col -> new TableCell<Cinema, Void>() {
            private Button btn = new Button("Supprimer");
            {
                btn.setOnAction(event -> {
                    Cinema cinema = getTableView().getItems().get(getIndex());
                    FranchiseDAO franchiseDAO = new FranchiseDAO();

                    // securite: empeche la suppression si lie a une franchise
                    if (franchiseDAO.getNbFranchiseByIdGerant(cinema.getIdCinema()) >= 1) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/popup_cinema.fxml"));
                            Parent root = fxmlLoader.load();

                            Stage stage = new Stage();
                            stage.setTitle("Action impossible");
                            stage.setScene(new Scene(root));
                            setIcone(stage);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // suppression vue et modele
                        tvCinema.getItems().remove(cinema);
                        CinemaDAO cinemaDAO = new CinemaDAO();
                        cinemaDAO.delete(cinema);
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
}