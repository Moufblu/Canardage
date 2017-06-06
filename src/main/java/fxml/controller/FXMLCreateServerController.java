/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.controller;

import Protocol.AlertPopup;
import canardage.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * FXML Controller class
 *
 * @author Miguel-Portable
 */
public class FXMLCreateServerController implements Initializable {

   @FXML
   private Button createServer;
   @FXML
   private GridPane gridPane;
   @FXML
   private TextField serverNameField;
   @FXML
   private TextField passwordField;

   private String gameName;
   private String password;

   Player player;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      player = Player.getInstance();
   }

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
         } catch(IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
         }
      } else {
         AlertPopup.alert("Erreur", "Erreur de création",
                 "Erreur lors de la connexion au serveur " + serverNameField,
                 Alert.AlertType.WARNING);
      }
   }
}
