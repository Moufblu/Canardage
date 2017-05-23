/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.controller;

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
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel-Portable
 */
public class FXMLMenuController implements Initializable {

   @FXML
   private Button joinGameBtn;
   @FXML
   private Button createServerBtn;
   @FXML
   private Button quitBtn;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // TODO
   }

   @FXML
   private void onJoinGame(ActionEvent event) {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource("/fxml/FXMLLobby.fxml"));
         Stage joinStage = new Stage();
         Scene scene = new Scene(root);

         joinStage.setTitle("Join Game");
         joinStage.resizableProperty().set(false);
         joinStage.setScene(scene);

         ((Node) (event.getSource())).getScene().getWindow().hide();
         joinStage.show();
      } catch(IOException e) {
         Logger logger = Logger.getLogger(getClass().getName());
         logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
      }
   }

   @FXML
   private void onCreateServer(ActionEvent event) {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource("/fxml/FXMLCreateServer.fxml"));
         Stage serverStage = new Stage();
         Scene scene = new Scene(root);

         serverStage.setTitle("Create Server");
         serverStage.resizableProperty().set(false);
         serverStage.setScene(scene);

         ((Node) (event.getSource())).getScene().getWindow().hide();
         serverStage.show();
      } catch(IOException e) {
         Logger logger = Logger.getLogger(getClass().getName());
         logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
      }
   }

   @FXML
   private void onQuit(ActionEvent event) {
      ((Node) (event.getSource())).getScene().getWindow().hide();
   }

}
