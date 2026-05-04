package cinema.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import cinema.BO.Utilisateur;
import cinema.DAO.UtilisateurDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConnexionController implements Initializable {
    // gere le nombre de tentatives echouees
    private int compteur = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField tfMDP;
    @FXML
    private Button bConnexion;

    @FXML
    public void bConnexionClick(ActionEvent event) {
        // recupere les identifiants
        String login = tfLogin.getText();
        String mdp = tfMDP.getText();

        // verifie en bdd avec gestion du hash bcrypt
        UtilisateurDAO userDAO = new UtilisateurDAO();
        Utilisateur user = userDAO.authenticate(login, mdp);

        // aiguillage selon le resultat
        if (user != null) {
            compteur = 0;
            String nomComplet = user.getPrenom() + " " + user.getNom();
            showAccueil(nomComplet);
        } else {
            compteur++;
            showError();
        }
    }

    private void showAccueil(String name) {
        // demarre la session en ouvrant la vue principale
        Stage stageP = (Stage) bConnexion.getScene().getWindow();
        stageP.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/page_accueil.fxml"));
            Parent root = fxmlLoader.load();

            AccueilController accueilController = fxmlLoader.getController();
            accueilController.setName(name);
            accueilController.setBienvenue();

            Stage stage = new Stage();
            stage.setTitle("Accueil Gestion de franchises");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/cinema/images/cinema_32x32.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showError() {
        // affiche une popup non bloquante avec le compteur d'erreur
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/views/ErreurConnexion.fxml"));
            Parent root = fxmlLoader.load();

            ErrorController errorController = fxmlLoader.getController();
            errorController.setMajLabel(Integer.toString(compteur));

            Stage stage = new Stage();
            stage.setTitle("Erreur de connexion");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/cinema/images/cinema_32x32.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}