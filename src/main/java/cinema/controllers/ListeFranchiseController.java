package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import cinema.BO.Franchise;
import cinema.BO.Utilisateur;
import cinema.BO.Cinema;
import cinema.DAO.FranchiseDAO;
import cinema.DAO.UtilisateurDAO;
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

public class ListeFranchiseController extends MenuController implements Initializable {
    @FXML
    private TableView<Franchise> tvFranchises;
    @FXML
    private TableColumn<Franchise, String> tcNomFranchise;
    @FXML
    private TableColumn<Franchise, String> tcSiegeSocial;
    @FXML
    private TableColumn<Franchise, String> tcGerant;
    @FXML
    private TableColumn<Franchise, Void> tcModifier;
    @FXML
    private TableColumn<Franchise, Void> tcSupprimer;
    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UtilisateurDAO gerantDAO = new UtilisateurDAO();

        // met en cache tous les gerants pour eviter une requete par ligne
        Map<Integer, Utilisateur> gerants = gerantDAO.findAll()
                .stream()
                .collect(Collectors.toMap(Utilisateur::getIdUtilisateur, u -> u));

        // lie la colonne avec la map creee
        tcGerant.setCellValueFactory(cellData -> {
            Utilisateur gerant = gerants.get(cellData.getValue().getIdGerant());
            return new SimpleStringProperty(gerant != null ? gerant.getNom() + " " + gerant.getPrenom() : "Aucun gérant");
        });

        tcNomFranchise.setCellValueFactory(new PropertyValueFactory<>("nomFranchise"));
        tcSiegeSocial.setCellValueFactory(new PropertyValueFactory<>("siegeSocial"));

        ObservableList<Franchise> data = getFranchiseList();
        tvFranchises.setItems(data);

        addButtonModifierToTable();
        addButtonSupprimerToTable();
    }

    private ObservableList<Franchise> getFranchiseList() {
        // recupere toutes les donnees
        FranchiseDAO franchiseDAO = new FranchiseDAO();
        List<Franchise> franchises = franchiseDAO.findAll();
        ObservableList<Franchise> list = FXCollections.observableArrayList();
        if (franchises != null) {
            list.addAll(franchises);
        }
        return list;
    }

    @FXML
    private void bRetourClick() {
        // gere le retour au menu principal
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

    private void addButtonModifierToTable() {
        // ajoute un bouton qui ouvre la vue edition preremplie
        tcModifier.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setOnAction(event -> {
                    Franchise franchise = getTableView().getItems().get(getIndex());
                    Stage stageP = (Stage) bRetour.getScene().getWindow();
                    stageP.close();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_modif_franchise.fxml"));
                        Parent root = fxmlLoader.load();

                        ModifierFranchiseController modifierFranchiseCtrl = fxmlLoader.getController();
                        modifierFranchiseCtrl.setAttributes(franchise);
                        modifierFranchiseCtrl.setName(nameUti);

                        Stage stage = new Stage();
                        stage.setTitle("Modification franchise");
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

    private void addButtonSupprimerToTable() {
        // ajoute un bouton de suppression avec sécurités
        tcSupprimer.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setOnAction(event -> {
                    Franchise franchise = getTableView().getItems().get(getIndex());

                    // 1. Vérification s'il y a des cinémas liés à cette franchise
                    CinemaDAO cinemaDAO = new CinemaDAO();
                    List<Cinema> tousLesCinemas = cinemaDAO.findAll();

                    boolean possedeCinemas = false;
                    for (Cinema c : tousLesCinemas) {
                        if (c.getNomFranchise() != null && c.getNomFranchise().equals(franchise.getNomFranchise())) {
                            possedeCinemas = true;
                            break;
                        }
                    }

                    // 2. Gestion de l'affichage selon le résultat
                    if (possedeCinemas) {
                        // Impossible de supprimer : on affiche l'erreur native qu'on a créée
                        afficherPopUpErreur(
                                "Action impossible",
                                "La franchise '" + franchise.getNomFranchise() + "' ne peut pas être supprimée car elle possède encore des cinémas.\n\nVeuillez d'abord supprimer ou réattribuer ses cinémas."
                        );
                    } else {
                        // Autorisé : on demande confirmation
                        boolean confirmer = afficherPopUpConfirmation(
                                "Confirmation de suppression",
                                "Êtes-vous sûr de vouloir supprimer définitivement la franchise '" + franchise.getNomFranchise() + "' ?"
                        );

                        if (confirmer) {
                            FranchiseDAO franchiseDAO = new FranchiseDAO();
                            franchiseDAO.delete(franchise);
                            tvFranchises.getItems().remove(franchise);
                        }
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