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
   private String password = "";
   private boolean enoughPlayers = false;
   private int amountPlayers = 0;
   
   public FXMLCreateServerController() {
      this.serverNameField = new TextField();
   }

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      
   }
   
   
   @FXML
   public void showGameName(ActionEvent event) {
      // A CREER
//      enoughPlayers = Server.methodEnoughPlayers();
      if(!enoughPlayers) {
         gameName = serverNameField.getText();
         password  = passwordField.getText();

         // HASH PASSWORD

         // CREATE SERVER
         
         System.out.println(gameName);
         System.out.println(password);

         Parent root;
         try {
            root = FXMLLoader.load(getClass().getResource("/fxml/FXMLCanardage.fxml"));
            Stage joinStage = new Stage();
            Scene scene = new Scene(root);

            joinStage.setTitle("CANARDAGE");
            joinStage.resizableProperty().set(false);
            joinStage.setScene(scene);

            ((Node) (event.getSource())).getScene().getWindow().hide();
            joinStage.show();
         } catch(IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
         }
      }
   }
}
