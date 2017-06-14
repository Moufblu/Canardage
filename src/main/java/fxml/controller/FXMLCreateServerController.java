package fxml.controller;

import canardage.AlertPopup;
import canardage.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Description: Classe responsable de créer la fenêtre pour la création d'un serveur
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class FXMLCreateServerController implements Initializable {

   @FXML
   private Button createServer;  // Bouton pour créer le serveur
   @FXML
   private GridPane gridPane;    // Panel pour l'affichage de FieldText et Label
   @FXML
   private TextField serverNameField; // Nom du serveur
   @FXML
   private TextField passwordField; // Mot de passe du serveur

   private String gameName;   // Nom donnée à la partie
   private String password;   // Mot de passe de la partie

   Player player;

   /**
    * Initialise le controller de cette classe
    * @param url Pas utilisé
    * @param rb Pas utilisé
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      player = Player.getInstance();
   }

   /**
    * Méthode pour la création du serveur pour une partie
    * @param event Donne l'événement de cette fenêtre courante
    */
   @FXML
   public void createServer(ActionEvent event) {
      gameName = serverNameField.getText();
      password = passwordField.getText();

      if(player.createServer(gameName, password)) {

         Parent root;
         try {
            root = FXMLLoader.load(getClass().getResource("/fxml/FXMLCanardage.fxml"));
            Stage joinStage = new Stage();
            Scene scene = new Scene(root);

            joinStage.setTitle(gameName);
            joinStage.resizableProperty().set(false);
            joinStage.setScene(scene);

            ((Node) (event.getSource())).getScene().getWindow().hide();
            joinStage.show();
            joinStage.setOnCloseRequest((WindowEvent event1) -> {
               Platform.exit();
               System.exit(0);
            });
         } catch(IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
         }
      } else {
         AlertPopup.alert("Erreur", "Erreur de création",
                 "Erreur lors de la connexion au serveur " + serverNameField.getText(),
                 Alert.AlertType.WARNING);
      }
   }
}
