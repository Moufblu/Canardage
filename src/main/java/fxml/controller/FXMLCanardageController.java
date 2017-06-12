package fxml.controller;

import Protocol.AlertPopup;
import Protocol.ProtocolV1;
import canardage.Global;
import canardage.Player;
import chat.Emoticon;
import duckException.BadGameInitialisation;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

   // A VÉRIFIER SI ON DOIT METTRE DANS LE PROTOCOL ET VOIR LEQUELS ENLEVER ET
   // UTILISER CELLES DU PROTOCOL À LA PLACE
   private final int RESIZE_CARDS = 5;
   private final int MARGIN_LEFT = 20;
   private final int MARGIN_DOWN = 15;

   private final int NUMBER_OF_SMILEYS = 4;
   private final int GRID_POS = 2;

   private int nbCurrentPlayers = 0;

   private boolean areCardsUsable = false; 
   
   // Changer les trucs en dur (String) par base de données pour tout ce qui est carte et affichage (?)
   Player player = Player.getInstance();

   @FXML
   private Button startButton;
   @FXML
   private GridPane playersGrid;
   @FXML
   private GridPane smileyGrid;
   @FXML
   private GridPane playerCardsGrid;
   @FXML
   private GridPane targetsGrid;
   @FXML
   private GridPane ducksAndProtectionsGrid;

   private final ArrayList<Label> playersChatList;

   private final ArrayList<ImageView> playersList;

   private final ArrayList<Button> buttonsList;

   private final ArrayList<Button> cardsList;

   private final ArrayList<ImageView> targetsList;
   private final Image imageTarget;

   private final ArrayList<ImageView> ducksGameList;

   private final ArrayList<ImageView> ducksHidenList;

   private final ArrayList<ImageView> protectionCardsList;
   private final Image imageProtection;

   private final Image imageBackCard;
   private final ImageView viewBackCard;

   /**
    * Variables pour les cartes, à faire dans un autre endroit plutôt (?)
    */
   Image[] duckImages = {
      new Image("/images/DuckEmpty.jpg"),
      new Image("/images/DuckGreen.jpg"),
      new Image("/images/DuckPink.jpg"),
      new Image("/images/DuckPurple.jpg"),
      new Image("/images/DuckBlue.jpg"),
      new Image("/images/DuckYellow.jpg"),
      new Image("/images/DuckOrange.jpg")
   };
   ArrayList<ImageView> duckViews;

   public FXMLCanardageController() {

      imageProtection = new Image("/images/CardACouvert.jpg");
      imageTarget = new Image("/images/Target.png");
      imageBackCard = new Image("/images/CardBack.jpg");

      // Liste des images des canards en jeu
      playersList = new ArrayList(Global.Rules.MAX_NO_POS);
      playersChatList = new ArrayList(Global.Rules.MAX_NO_POS);
      buttonsList = new ArrayList(NUMBER_OF_SMILEYS);
      
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         playersList.add(new ImageView(duckImages[i + 1]));
         playersChatList.add(new Label());
         resizeImageView(playersList.get(i));
      }

      for(int i = 0; i < NUMBER_OF_SMILEYS; i++) {
         buttonsList.add(new Button("B" + i));
      }


      cardsList = new ArrayList(Global.Rules.HAND_SIZE);
      for(int i = 0; i < Global.Rules.HAND_SIZE; i++) {
         ImageView viewCards = new ImageView(imageBackCard);
         resizeImageView(viewCards);
         Button b = new Button();
         b.setGraphic(viewCards);
         b.setDisable(true);

         final int trigger = i;
         b.setOnAction((ActionEvent event) -> {
            player.playCard(trigger);
            
            cardsList.stream().forEach((cardsButton) -> {
               cardsButton.setDisable(true);
            });
         });
         cardsList.add(b);
      }

      duckViews = new ArrayList<>(duckImages.length);
      for(int i = 0; i < duckImages.length; i++) {
         createAndResizeImageView(duckViews, i, duckImages[i]);
      }

      targetsList = new ArrayList(Global.Rules.MAX_NO_POS);

      protectionCardsList = new ArrayList(Global.Rules.MAX_NO_POS);

      ducksGameList = new ArrayList(Global.Rules.MAX_NO_POS);

      ducksHidenList = new ArrayList(Global.Rules.MAX_NO_POS);

      viewBackCard = new ImageView(imageBackCard);
      resizeImageView(viewBackCard);
   }

   /**
    * Initialise le controlleur de la classe.
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // Les boutons du chat
      for(int i = 0; i < NUMBER_OF_SMILEYS / GRID_POS; i++) {
         for(int j = 0; j < NUMBER_OF_SMILEYS / GRID_POS; j++) {

            final int position = i * GRID_POS + j;
            Button b = buttonsList.get(position);
            imagesPosition(buttonsList, position, j, i, HPos.CENTER, VPos.CENTER);

            b.setOnAction((ActionEvent event) -> {
               player.sendEmoticon(Emoticon.values()[position]);
            });
         }
      }

      smileyGrid.getChildren().addAll(buttonsList);

      // La partie arrière d'une carte canard pour la pile de canard
      imagesMarginAndPosition(Arrays.asList(viewBackCard), 0, 6, 0,
              HPos.CENTER, VPos.CENTER, MARGIN_DOWN, 0);
      ducksAndProtectionsGrid.getChildren().add(viewBackCard);

      showPlayers();
      targets();
      hidenDucks();
      ducksGame();
      protectionCards();
      showPlayerCards();
   }

   @FXML
   private void startGame(ActionEvent event) {
      try {
         player.startGame(this);
         startButton.setDisable(true);
         startButton.setVisible(false);
      } catch(BadGameInitialisation e) {
         AlertPopup.alert("Avertissement", "Pas assez de joueurs", e.getMessage(), Alert.AlertType.WARNING);
      } catch(IllegalStateException | IOException e) {
         AlertPopup.alert(e);
      }
   }

   public void showPlayers() {
      for(int i = nbCurrentPlayers; i < 6; i++) {
         nbCurrentPlayers++;
         playersGrid.addColumn(i, playersList.get(i), playersChatList.get(i));
         imagesPosition(playersList, i, i, 0, HPos.CENTER, VPos.TOP);
         imagesPosition(playersChatList, i, i, 0, HPos.CENTER, VPos.BOTTOM);
         if(i == player.getPlayerNumber()){
            playersChatList.get(i).setText("You");
         }
      }
   }

   public void showPlayerCards() {
      // Les cartes des joueurs, à refaire selon la liste de cartes dans Player, liste aussi à changer pour que ce soit une liste de boutons
      for(int i = 0; i < Global.Rules.HAND_SIZE; i++) {
         imagesPosition(cardsList, i, i, 0, HPos.CENTER, VPos.CENTER);
      }
      playerCardsGrid.getChildren().addAll(cardsList);
   }

   public void targets() {
      // Les cibles qui peuvent être posées sur le plateau de jeu
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         createAndResizeImageView(targetsList, i, imageTarget);
         imagesPosition(targetsList, i, i, 0, HPos.CENTER, VPos.CENTER);
         targetsList.get(i).setVisible(false); // On cache les cibles tant qu'on les joue pas
      }
      targetsGrid.getChildren().addAll(targetsList);
   }

   public void unshowTargets(int position) {
      targetsList.get(position).setVisible(false);
   }

   public void showTargets(int position) {
      targetsList.get(position).setVisible(true);
   }

   public void hidenDucks() {
      // BOUCLE POUR LES CANARDS CACHÉS, FAIT JUSTE POUR L'AFFICHAGE, UTILISÉ SI ON VEUT CACHER UN CANARD PLUS TARD
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         createAndResizeImageView(ducksHidenList, i, duckImages[i]);
         imagesMarginAndPosition(ducksHidenList, i, i, 0, HPos.CENTER, VPos.CENTER,
                 0, MARGIN_LEFT);
         ducksHidenList.get(i).setVisible(false);
      }
      ducksAndProtectionsGrid.getChildren().addAll(ducksHidenList);
   }

   public void unshowHidenDucks(int position) {
      ducksHidenList.get(position).setVisible(false);
   }

   public void showHidenDucks(int position, int duck) {
      ducksHidenList.get(position).setVisible(true);

      ducksAndProtectionsGrid.getChildren().removeAll(ducksHidenList.get(position));

      ducksHidenList.get(position).setImage(duckImages[duck]);

      ducksAndProtectionsGrid.getChildren().add(position, ducksHidenList.get(position));
      imagesMarginAndPosition(ducksHidenList, position, position, 0, HPos.CENTER, VPos.CENTER,
              0, MARGIN_LEFT);
   }

   public void ducksGame() {
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         final int trigger = i;
         createAndResizeImageView(ducksGameList, i, duckImages[i]);
         ducksAndProtectionsGrid.setOnMouseClicked((MouseEvent event) -> {
            if(areCardsUsable){
               player.posChoose(Global.Rules.MAX_NO_POS - 1 - trigger);
               areCardsUsable = false;
            }
         });
      }
      ducksAndProtectionsGrid.getChildren().addAll(ducksGameList);
   }

   public void showDucksGame() {
      // Les canards des joueurs, à refaire selon la liste des joueurs qui sont dnas le jeu et prendre les 6 premiers du tas de canards mélangé
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         imagesMarginAndPosition(ducksGameList, i, i, 0, HPos.LEFT, VPos.CENTER,
                 MARGIN_DOWN, MARGIN_LEFT);
      }
   }

   public void modifieDucksInGame(int position, int duck) {
      ducksAndProtectionsGrid.getChildren().removeAll(ducksGameList.get(position));

      ducksGameList.get(position).setImage(duckImages[duck]);

      ducksAndProtectionsGrid.getChildren().add(position, ducksGameList.get(position));
      imagesMarginAndPosition(ducksGameList, position, position, 0, HPos.LEFT, VPos.CENTER,
              MARGIN_DOWN, MARGIN_LEFT);
   }

   public void protectionCards() {
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         createAndResizeImageView(protectionCardsList, i, imageProtection);
         imagesMarginAndPosition(protectionCardsList, i, i, 0, HPos.LEFT, VPos.CENTER,
                 MARGIN_DOWN, 0);
         protectionCardsList.get(i).setVisible(false);
      }
      ducksAndProtectionsGrid.getChildren().addAll(protectionCardsList);
   }

   public void unshowGuard(int position) {
      protectionCardsList.get(position).setVisible(false);
   }

   public void showGuard(int position) {
      protectionCardsList.get(position).setVisible(true);
   }

   private void createAndResizeImageView(List<ImageView> list, int position, Image image) {
      list.add(position, new ImageView(image));
      resizeImageView(list.get(position));
   }

   private void resizeImageView(ImageView imageView) {
      imageView.setFitHeight(imageView.getBoundsInLocal().getHeight() / RESIZE_CARDS);
      imageView.setFitWidth(imageView.getBoundsInLocal().getWidth() / RESIZE_CARDS);
   }

   private void imagesPosition(List<?> list, int position,
           int columnPosition, int rowPosition,
           HPos hPos, VPos vPos) {
      GridPane.setConstraints((Node) list.get(position),
              columnPosition, rowPosition, 1, 1, hPos, vPos);
   }

   private void imagesMarginAndPosition(List<?> list, int position,
           int columnPosition, int rowPosition,
           HPos hPos, VPos vPos,
           double bottom, double left) {
      imagesPosition(list, position, columnPosition, rowPosition, hPos, vPos);
      GridPane.setMargin((Node) list.get(position), new Insets(0, 0, bottom, left));
   }

   public void update() {
      
      showDucksGame();

      showTargets(2);
      showTargets(5);

      showGuard(2);

      modifieDucksInGame(0, 6);

      showHidenDucks(2, 4);
      showHidenDucks(5, 6);
      showHidenDucks(3, 2);
      showHidenDucks(1, 3);
      showHidenDucks(0, 5);
      showHidenDucks(4, 1);

      showGuard(0);
      showGuard(4);
   }

   public void updateCards(Integer[] cards) {
      
   }

   public void updateBoard(String stringBoard) {
      String[] blocs = stringBoard.split(String.valueOf(Global.Board.SEPARATOR));
      boolean hasGuard;
      boolean hasTarget;
      int hiddenDuck;
      for(int k = 0; k < blocs.length; k++) {
         String splittedBoard = blocs[k];
         Integer.parseInt(splittedBoard.substring(0, 1));

         hasGuard = false;
         hasTarget = false;
         hiddenDuck = 0;

         for(int i = 1; i < splittedBoard.length(); i++) {
            char c = splittedBoard.charAt(i);
            switch(c) {
               case Global.Board.GUARD:
                  hasGuard = true;
                  break;
               case Global.Board.TARGET:
                  hasTarget = true;
                  break;
               default:
                  hiddenDuck = Character.getNumericValue(c);
                  break;
            }

            if(hiddenDuck != 0) {
               showHidenDucks(k, hiddenDuck);
            } else {
               unshowHidenDucks(k);
            }

            if(hasTarget) {
               showTargets(k);
            } else {
               unshowTargets(k);
            }

            if(hasGuard) {
               showGuard(k);
            } else {
               unshowGuard(k);
            }
         }
      }
   }

   public void askCard() {
      cardsList.stream().forEach((cardButton) -> {
         cardButton.setDisable(false);
      });
   }

   public void alert(Global.ERROR_MESSAGES message) {
      AlertPopup.alert("Wrong move", "You cannot do this action", ProtocolV1.messageError(message), Alert.AlertType.INFORMATION);
   }

   public void askPosition() {
      areCardsUsable = true;
      AlertPopup.alert("Position", "Choisissez une position", "Veuillez choisir une position", Alert.AlertType.INFORMATION);
   }

}
