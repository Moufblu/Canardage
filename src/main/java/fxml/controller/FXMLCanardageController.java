package fxml.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Miguel-Portable
 */
public class FXMLCanardageController implements Initializable {
   
   private final int NUMBER_OF_TARGETS = 6;
   private final int NUMBER_OF_SMILEYS = 4;
   private final int NUMBER_OF_CARDS = 3;
   private final int GRID_POS = 2;
   
   @FXML
   private GridPane playersGrid;
   @FXML
   private GridPane smileyGrid;
   @FXML
   private GridPane playerCardsGrid;
   
   private ArrayList<Button> buttonsList;
   private Button smiley1;
   private Button smiley2;
   private Button smiley3;
   private Button smiley4;
   
   private ArrayList<Button> cardsList;
   private Button card1;
   private Button card2;
   private Button card3;
   
   private Image imageCard1;
   private Image imageCard2;
   private Image imageCard3;
   private ImageView viewCard1;
   private ImageView viewCard2;
   private ImageView viewCard3;

   private ArrayList<ImageView> targetsList;
   private Image imageTarget;
   private ImageView viewTarget;
   @FXML
   private GridPane targetsGrid;
   
   public FXMLCanardageController() {
      
      this.smiley1 = new Button("B1");
      this.smiley2 = new Button("B2");
      this.smiley3 = new Button("B3");
      this.smiley4 = new Button("B4");
      this.buttonsList = new ArrayList(NUMBER_OF_SMILEYS);
      buttonsList.add(smiley1);
      buttonsList.add(smiley2);
      buttonsList.add(smiley3);
      buttonsList.add(smiley4);
      
      this.imageCard1 = new Image("/images/CardOups.jpg");
      this.imageCard2 = new Image("/images/CardCanarchie.jpg");
      this.imageCard3 = new Image("/images/CardPan.jpg");
      this.viewCard1 = new ImageView(imageCard1);
      this.viewCard2 = new ImageView(imageCard2);
      this.viewCard3 = new ImageView(imageCard3);
      
      viewCard1.setFitHeight(viewCard1.getBoundsInLocal().getHeight()/ 5);
      viewCard1.setFitWidth(viewCard1.getBoundsInLocal().getWidth() / 5);
      viewCard2.setFitHeight(viewCard2.getBoundsInLocal().getHeight()/ 5);
      viewCard2.setFitWidth(viewCard2.getBoundsInLocal().getWidth() / 5);
      viewCard3.setFitHeight(viewCard3.getBoundsInLocal().getHeight()/ 5);
      viewCard3.setFitWidth(viewCard3.getBoundsInLocal().getWidth() / 5);
      
      this.card1 = new Button();
      this.card2 = new Button();
      this.card3 = new Button();
      card1.setGraphic(viewCard1);
      card2.setGraphic(viewCard2);
      card3.setGraphic(viewCard3);
      
      this.cardsList = new ArrayList(NUMBER_OF_CARDS);
      cardsList.add(card1);
      cardsList.add(card2);
      cardsList.add(card3);
      
      this.imageTarget = new Image("/images/Target.png");
//      this.viewTarget = new ImageView(imageTarget);
      
      this.targetsList = new ArrayList(NUMBER_OF_TARGETS);
   }
   /**
    * Initializes the controller class.
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      for(int i = 0; i < NUMBER_OF_SMILEYS / GRID_POS; i++) {
         for(int j = 0; j < NUMBER_OF_SMILEYS / GRID_POS; j++) {
            GridPane.setConstraints(buttonsList.get(i * GRID_POS + j), j, i, 1, 1, HPos.CENTER, VPos.CENTER);
         }
      }
      smileyGrid.getChildren().addAll(buttonsList);
      
      for(int i = 0; i < NUMBER_OF_CARDS; i++) {
         GridPane.setConstraints(cardsList.get(i), i, 0, 1, 1, HPos.CENTER, VPos.CENTER);
      }
      playerCardsGrid.getChildren().addAll(cardsList);
      
      for(int i = 0; i < NUMBER_OF_TARGETS; i++) {
         targetsList.add(new ImageView(imageTarget));
         targetsList.get(i).setFitHeight(targetsList.get(i).getBoundsInLocal().getHeight() / 5);
         targetsList.get(i).setFitWidth(targetsList.get(i).getBoundsInLocal().getWidth()/ 5);
         GridPane.setConstraints(targetsList.get(i), i, 0, 1, 1, HPos.CENTER, VPos.CENTER);
      }
      targetsGrid.getChildren().addAll(targetsList);
   }
}
