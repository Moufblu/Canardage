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
import javafx.scene.control.ComboBox;

/**
 *
 * @author Nathan
 */
public class FXMLBoardController implements Initializable {

   @FXML
   private Button button;
   @FXML
   private ComboBox<?> menuMap;
   @FXML
   private Button button1;
   @FXML
   private Button button2;
   @FXML
   private Button button3;

   @FXML
   private void handleButtonAction(ActionEvent event) {
   }

   @FXML
   private void selectionMenuMap(ActionEvent event) {
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      // TODO
   }

}
