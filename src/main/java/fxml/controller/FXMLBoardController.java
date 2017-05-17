/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Nathan
 */
public class FXMLBoardController implements Initializable {

   @FXML
   private Pane pane;
   @FXML
   private Button smiley1;
   @FXML
   private Button smiley2;
   @FXML
   private Button smiley3;
   @FXML
   private Button smiley4;

   private Image image;

   public FXMLBoardController() {
//      this.image = new Image(getClass().getResourceAsStream("/images/canardBleuCopie.png")); 
   }
   
   /**
    * Initializes the controller class.
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
//      ImageView duck = new ImageView(image);
//      pane.getChildren().add(duck);
   }

}
