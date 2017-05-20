/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
   @FXML
   private ImageView duck;

   public FXMLBoardController() {
      this.smiley1 = new Button();
   }
   
   /**
    * Initializes the controller class.
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      
      Image value = new Image(getClass().getResourceAsStream("/images/canardBleuCopie.png"));
      duck = new ImageView(value);
      duck.setImage(value);
      duck.setLayoutX(300);
      duck.setLayoutY(200);
      duck.fitHeightProperty().add(100);
      duck.fitWidthProperty().add(100);
      duck.setVisible(true);
      
      smiley1.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent e) {
             duck.setImage(new Image(getClass().getResourceAsStream("/images/canardVert.png")));
         }
      });
   }
}
