package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cinema.BO.Franchise;
import cinema.BO.Utilisateur;
import cinema.DAO.FranchiseDAO;
import cinema.DAO.UtilisateurDAO;
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

public class AjouterFranchiseController extends MenuController implements Initializable {

    @FXML
    private TextField tfNomFranchise, tfSiegeSocial;
    @FXML
    private Button bRetour;
    @FXML
    private ListView<Utilisateur> lvGerantFranchise;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Utilisateur> utilisateurs = getUtilisateurList();
        lvGerantFranchise.setItems(utilisateurs);
    }

    private ObservableList<Utilisateur> getUtilisateurList() {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        List<Utilisateur> utilisateurs = utilisateurDAO.findAll();
        return FXCollections.observableArrayList(utilisateurs);
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


    @FXML
    public void bEnregistrerClick(ActionEvent event) {
        String nomFranchise = tfNomFranchise.getText();
        String siegeSocial = tfSiegeSocial.getText();
        Utilisateur gerantSelect = lvGerantFranchise.getSelectionModel().getSelectedItem();

        // Contrôles de saisies avec messages explicites
        if (nomFranchise == null || nomFranchise.isEmpty()) {
            afficherPopUpErreur("Erreur de saisie", "Le nom de la franchise ne peut pas être vide.");
            return;
        }
        if (siegeSocial == null || siegeSocial.isEmpty()) {
            afficherPopUpErreur("Erreur de saisie", "Le siège social de la franchise ne peut pas être vide.");
            return;
        }
        if (gerantSelect == null) {
            afficherPopUpErreur("Sélection manquante", "Veuillez sélectionner un gérant dans la liste.");
            return;
        }

        int id = gerantSelect.getIdUtilisateur();
        Franchise franchise = new Franchise(0, nomFranchise, siegeSocial, id);
        FranchiseDAO franchiseDAO = new FranchiseDAO();
        boolean controle = franchiseDAO.create(franchise);

        if (controle) {
            bEffacerClick(null);
        } else {
            afficherPopUpErreur("Erreur BDD", "Une erreur est survenue lors de la création de la franchise.");
        }
    }

    @FXML
    public void bEffacerClick(ActionEvent event) {
        if (tfNomFranchise != null) tfNomFranchise.clear();
        if (tfSiegeSocial != null) tfSiegeSocial.clear();
        lvGerantFranchise.getSelectionModel().clearSelection();
    }
}