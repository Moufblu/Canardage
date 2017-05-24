package fxml.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class FXMLPasswordController implements Initializable {
   
   @FXML
   private Button joinGame;
   @FXML
   private TextField passwordField;
   
   private String password = "";
   
   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // TODO
   }
   
   @FXML
   public void putPassword(ActionEvent event) {
      
      password  = passwordField.getText();
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
