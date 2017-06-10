package fxml.controller;

import Protocol.AlertPopup;
import Protocol.ProtocolV1;
import canardage.Player;
import chat.Emoticon;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Miguel-Portable
 */
public class FXMLCanardageController implements Initializable {
   
   private final int RESIZE_CARDS = 5;
   private final int MARGIN_LEFT = 20;
   private final int MARGIN_DOWN = 15;
   // A METTRE DANS LE PROTOCOL (?)
   private final int NUMBER_OF_PROTECTIONS = 6;
   private final int NUMBER_OF_TARGETS = 6;
   
   private final int NUMBER_OF_SMILEYS = 4;
   private final int NUMBER_OF_CARDS   = 3;
   private final int GRID_POS          = 2;
   
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
   
   // images pour test à remplacer par BD
   private final Image[] imageCards = {
      new Image("/images/CardOups.jpg"),
      new Image("/images/CardCanarchie.jpg"),
      new Image("/images/CardPan.jpg")
   };

   private final ArrayList<ImageView> targetsList;
   private final Image imageTarget;
   
   private final ArrayList<ImageView> ducksGameList;
   
   private final ArrayList<ImageView> ducksHidenList;
   private ImageView viewDuckHiden;
   
   private final ArrayList<ImageView> protectionCardsList;
   private final Image imageProtection;
   
   private final Image imageBackCard;
   private final ImageView viewBackCard;
   
   /**
    * Variables pour les cartes
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
      
      // Liste des images des canards en jeu
      playersList = new ArrayList(ProtocolV1.MAX_NO_POS);
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         playersList.add(new ImageView(duckImages[i + 1]));
      }
      
      playersChatList = new ArrayList(ProtocolV1.MAX_NO_POS);
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         playersChatList.add(new Label());
      }

      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         playersChatList.get(i).setText("Something" + i);
         resizeImageView(playersList.get(i));
      }
      
      buttonsList = new ArrayList(NUMBER_OF_SMILEYS);
      for(int i = 0; i < NUMBER_OF_SMILEYS; i++) {
         buttonsList.add(new Button("B" + i));
      }
      
      // A faire dans Player pour avoir la liste de cartes dans le Player
      cardsList = new ArrayList(NUMBER_OF_CARDS);
      for(int i = 0; i < NUMBER_OF_CARDS; i++) {
         ImageView viewCards = new ImageView(imageCards[i]);
         resizeImageView(viewCards);
         Button b = new Button();
         b.setGraphic(viewCards);
         cardsList.add(b);
      }
      
      duckViews = new ArrayList<>(duckImages.length);
      for(int i = 0; i < duckImages.length; i++) {
         createAndResizeImageView(duckViews, i, duckImages[i]);
      }
      
      imageTarget = new Image("/images/Target.png");
      targetsList = new ArrayList(NUMBER_OF_TARGETS);
      
      imageProtection = new Image("/images/CardACouvert.jpg");
      protectionCardsList = new ArrayList(NUMBER_OF_PROTECTIONS);
      
      ducksGameList = new ArrayList(ProtocolV1.MAX_NO_POS);
      
      ducksHidenList = new ArrayList(ProtocolV1.MAX_NO_POS);
      
      imageBackCard = new Image("/images/CardBack.jpg");
      viewBackCard = new ImageView(imageBackCard);
      resizeImageView(viewBackCard);
   }
   /**
    * Initialises le controlleur de la classe.
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
            
            b.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                  player.sendEmoticon(Emoticon.values()[position]);
               }
            });
         }
      }
      
      smileyGrid.getChildren().addAll(buttonsList);
      
      // La partie arrière d'une carte canard pour la pile de canard
      imagesMarginAndPosition(Arrays.asList(viewBackCard), 0, 6, 0,
                              HPos.CENTER, VPos.CENTER, MARGIN_DOWN, 0);
      ducksAndProtectionsGrid.getChildren().add(viewBackCard);
      
      targets();
      
      hidenDucks();
      
      ducksGame();
      
      protectionCards();
      
      update();
      
   }

   @FXML
   private void startGame(ActionEvent event) {
      startButton.setDisable(true);
      startButton.setVisible(false);
   }
   
   public void showPlayers(int nbPlayers) {
      for(int i = 0; i < nbPlayers; i++) {
         playersGrid.addColumn(i, playersList.get(i), playersChatList.get(i));
         imagesPosition(playersList, i, i, 0, HPos.CENTER, VPos.TOP);
         imagesPosition(playersChatList, i, i, 0, HPos.CENTER, VPos.BOTTOM);
      }
   }
   
   public void showPlayerCards() {
      // Les cartes des joueurs, à refaire selon la liste de cartes dans Player, liste aussi à changer pour que ce soit une liste de boutons
      for(int i = 0; i < NUMBER_OF_CARDS; i++) {
         imagesPosition(cardsList, i, i, 0, HPos.CENTER, VPos.CENTER);
      }
      playerCardsGrid.getChildren().addAll(cardsList);
   }
   
   public void targets() {
      // Les cibles qui peuvent être posées sur le plateau de jeu
      for(int i = 0; i < NUMBER_OF_TARGETS; i++) {
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
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
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
      // C'est ici la merde Miguel :3
      for(Object label : duckViews) {
         System.out.println("THINGS " + ((ImageView)label).toString());
      }
      System.out.println("HERE");
      ducksHidenList.get(position).setVisible(true);
      System.out.println("VISIBLE " + ducksHidenList.get(position).toString());
      viewDuckHiden = ducksHidenList.remove(position);
      ducksHidenList.add(position, duckViews.get(duck));
//      viewDuckHiden = ducksHidenList.set(position, duckViews.get(duck));
      System.out.println("OLD VISIBLE " + viewDuckHiden.toString() + " NEXT VISIBLE "
              + ducksHidenList.get(position).toString());
      System.out.println("OUT");
   }
   
   public void ducksGame() {
      // BOUCLE POUR LES CANARDS SUR LE PLATEAU, FAIT JUSTE POUR L'AFFICHAGE, UTILISÉ SI ON VEUT AFFICHER UN CANARD DU PLATEAU PLUS TARD
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         createAndResizeImageView(ducksGameList, i, duckImages[i]);
      }
      ducksAndProtectionsGrid.getChildren().addAll(ducksGameList);
   }
   
   public void showDucksGame() {
      // Les canards des joueurs, à refaire selon la liste des joueurs qui sont dnas le jeu et prendre les 6 premiers du tas de canards mélangé
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         imagesMarginAndPosition(ducksGameList, i, i, 0, HPos.LEFT, VPos.CENTER,
                                 MARGIN_DOWN, MARGIN_LEFT);
      }
   }
   
   public void protectionCards() {
      for(int i = 0; i < NUMBER_OF_PROTECTIONS; i++) {
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
   
   private void createAndResizeImageView(ArrayList<ImageView> list, int position, Image image) {
      list.add(new ImageView(image));
      resizeImageView(list.get(position));
   }
   
   private void resizeImageView(ImageView imageView) {
      imageView.setFitHeight(imageView.getBoundsInLocal().getHeight() / RESIZE_CARDS);
      imageView.setFitWidth(imageView.getBoundsInLocal().getWidth() / RESIZE_CARDS);
   }
   
   private void imagesPosition(List<?> list, int position,
                              int columnPosition, int rowPosition,
                              HPos hPos, VPos vPos) {
      GridPane.setConstraints((Node)list.get(position),
                              columnPosition, rowPosition, 1, 1, hPos, vPos);
   }
   
   private void imagesMarginAndPosition(List<?> list, int position,
                                       int columnPosition, int rowPosition,
                                       HPos hPos, VPos vPos,
                                       double bottom, double left) {
      imagesPosition(list, position, columnPosition, rowPosition, hPos, vPos);
      GridPane.setMargin((Node)list.get(position), new Insets(0, 0, bottom, left));
   }
   
   public void update() {
      showPlayers(4); // Here the number of players that will play the game
      
      showPlayerCards();
      
      showDucksGame();
      
      showTargets(2);
      showTargets(5);
      
      showHidenDucks(2, 1);
      showHidenDucks(5, 6);
      showHidenDucks(3, 4);
      
//      showGuard(1);
//      showGuard(4);
   }

   public void updateCards(List<Integer> cards) {
      // need Database ?
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void updateBoard(String[] splittedCommands) {
      boolean hasGuard;
      boolean hasTarget;
      int hiddenDuck;
      for(int k = 0; k < splittedCommands.length; k++) {
         String splittedCommand = splittedCommands[k];
         Integer.parseInt(splittedCommand.substring(0, 1));
         
         hasGuard = false;
         hasTarget = false;
         hiddenDuck = 0;
         
         for(int i = 1; i < splittedCommand.length(); i++) {
            char c = splittedCommand.charAt(i); 
            switch (c){
               case 'G':
                  hasGuard = true;
                  break;
               case '*':
                  hasTarget = true;
                  break;
               default:
                  hiddenDuck = Character.getNumericValue(c);
                  break;
            }
            
            if(hiddenDuck != 0){
               showHidenDucks(k, hiddenDuck);
            }else{
               unshowHidenDucks(k);
            }
            
            if(hasTarget) {
               showTargets(k);
            } else {
               unshowTargets(k);
            }
            
            if(hasGuard) {
               showGuard(k);
            }else{
               unshowGuard(k);
            }
         }
      }
   }

   public int askCard() {
      // activer boutons action
      
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void alert(int parseInt) {
      AlertPopup.alert("Wrong move", "You cannot do this action", ProtocolV1.messageRefuse(parseInt), Alert.AlertType.INFORMATION);
   }

   public int askPosition() {
      // activer cartes position (possible ?)
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   
}
