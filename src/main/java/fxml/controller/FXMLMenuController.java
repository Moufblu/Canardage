/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
   }

   @FXML
   private void onCreateServer(ActionEvent event) {
   }

   @FXML
   private void onQuit(ActionEvent event) {
   }
   
}
