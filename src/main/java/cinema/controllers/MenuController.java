package cinema.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Optional;

public class MenuController {
    @FXML
    protected MenuItem bListeFranchise, bAjouterFranchise, bListeCinema, bAjouterCinema, bQuitter, bAccueil, bListeSalle, bAjouterSalle;

    protected String nameUti;

    // Pop-up d'erreur
    protected void afficherPopUpErreur(String titre, String messageErreur) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(messageErreur);

        // Ajoute ton logo à la fenêtre de l'alerte
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        setIcone(stage);

        alert.showAndWait();
    }

    protected boolean afficherPopUpConfirmation(String titre, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        setIcone(stage);

        // Attend la réponse de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void bQuitterClick(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void bAccueilClick(ActionEvent event) {
        Stage StageE = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        StageE.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_accueil.fxml"));
            Parent root = fxmlLoader.load();
            AccueilController accueilController = fxmlLoader.getController();
            accueilController.setName(nameUti);
            accueilController.setBienvenue();
            Stage stage = new Stage();
            stage.setTitle("Accueil");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void bListFranchiseClick(ActionEvent event) {
        Stage stageP = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        stageP.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_liste_franchise.fxml"));
            Parent root = fxmlLoader.load();
            ListeFranchiseController listeFranchiseController = fxmlLoader.getController();
            listeFranchiseController.setName(nameUti);
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
    public void bAjouterFranchiseClick(ActionEvent event) {
        Stage stageP = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        stageP.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_ajout_franchise.fxml"));
            Parent root = fxmlLoader.load();
            AjouterFranchiseController ajouterFranchiseController = fxmlLoader.getController();
            ajouterFranchiseController.setName(nameUti);
            Stage stage = new Stage();
            stage.setTitle("Ajouter une franchise");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bListeCinemaClick(ActionEvent event) {
        Stage stageP = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        stageP.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_liste_cinema.fxml"));
            Parent root = fxmlLoader.load();
            ListeCinemaController listeSectionController = fxmlLoader.getController();
            listeSectionController.setName(nameUti);
            Stage stage = new Stage();
            stage.setTitle("Liste cinéma");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bAjouterCinemaClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_ajout_cinema.fxml"));
            Parent root = fxmlLoader.load();
            AjouterCinemaController ajouterCinemaController = fxmlLoader.getController();
            ajouterCinemaController.setName(nameUti);
            Stage stageP = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stageP.close();
            Stage stage = new Stage();
            stage.setTitle("Ajout d'un Cinéma");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bListeSalleClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_liste_salle.fxml"));
            Parent root = fxmlLoader.load();
            ListeSalleController listeSalleController = fxmlLoader.getController();
            listeSalleController.setName(nameUti);
            Stage stageP = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stageP.close();
            Stage stage = new Stage();
            stage.setTitle("Liste salles");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setName(String nameUti) {
        this.nameUti = nameUti;
    }

    @FXML
    public void bAjouterSalleClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_ajout_salle.fxml"));
            Parent root = fxmlLoader.load();
            Stage stageP = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stageP.close();
            AjouterSalleController ajouterSalleController = fxmlLoader.getController();
            ajouterSalleController.setName(nameUti);
            Stage stage = new Stage();
            stage.setTitle("Ajout d'une salle");
            stage.setScene(new Scene(root));
            setIcone(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setIcone(Stage stage) {
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/cinema/images/cinema_32x32.png")));
        } catch (Exception e) {
            System.out.println("Erreur icône : " + e.getMessage());
        }
    }
}