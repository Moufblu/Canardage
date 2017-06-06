package fxml.controller;

import Protocol.AlertPopup;
import Protocol.ProtocolV1;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
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
   
   private ArrayList<Label> playersChatList;
   private Label chatPlayer1;
   private Label chatPlayer2;
   private Label chatPlayer3;
   private Label chatPlayer4;
   private Label chatPlayer5;
   private Label chatPlayer6;
   
   private ArrayList<ImageView> playersList;
   private Image imageDuckPlayer1;
   private Image imageDuckPlayer2;
   private Image imageDuckPlayer3;
   private Image imageDuckPlayer4;
   private Image imageDuckPlayer5;
   private Image imageDuckPlayer6;
   private ImageView viewDuckPlayer1;
   private ImageView viewDuckPlayer2;
   private ImageView viewDuckPlayer3;
   private ImageView viewDuckPlayer4;
   private ImageView viewDuckPlayer5;
   private ImageView viewDuckPlayer6;
   
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
   
   private ArrayList<ImageView> ducksGameList;
   private Image imageDuckGame;
   
   private ArrayList<ImageView> ducksHidenList;
   private Image imageDuckHiden;
   
   private ArrayList<ImageView> protectionCardsList;
   private Image imageProtection;
   
   private Image imageBackCard;
   private ImageView viewBackCard;
   
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
   
   public FXMLCanardageController() {
      
      this.viewDuckPlayer1 = new ImageView(imageDuckPlayer1);
      this.viewDuckPlayer2 = new ImageView(imageDuckPlayer2);
      this.viewDuckPlayer3 = new ImageView(imageDuckPlayer3);
      this.viewDuckPlayer4 = new ImageView(imageDuckPlayer4);
      this.viewDuckPlayer5 = new ImageView(imageDuckPlayer5);
      this.viewDuckPlayer6 = new ImageView(imageDuckPlayer6);
      this.playersList = new ArrayList(ProtocolV1.MAX_NO_POS);
      playersList.add(viewDuckPlayer1);
      playersList.add(viewDuckPlayer2);
      playersList.add(viewDuckPlayer3);
      playersList.add(viewDuckPlayer4);
      playersList.add(viewDuckPlayer5);
      playersList.add(viewDuckPlayer6);
      
      this.chatPlayer1 = new Label();
      this.chatPlayer2 = new Label();
      this.chatPlayer3 = new Label();
      this.chatPlayer4 = new Label();
      this.chatPlayer5 = new Label();
      this.chatPlayer6 = new Label();
      this.playersChatList = new ArrayList(ProtocolV1.MAX_NO_POS);
      playersChatList.add(chatPlayer1);
      playersChatList.add(chatPlayer2);
      playersChatList.add(chatPlayer3);
      playersChatList.add(chatPlayer4);
      playersChatList.add(chatPlayer5);
      playersChatList.add(chatPlayer6);
      
      this.playersGrid = new GridPane();
      
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         playersChatList.get(i).setText("Something" + i);
         playersList.get(i).setFitHeight(playersList.get(i).getBoundsInLocal().getHeight() / RESIZE_CARDS);
         playersList.get(i).setFitWidth(playersList.get(i).getBoundsInLocal().getWidth() / RESIZE_CARDS);
      }
      
      this.smiley1 = new Button("B1");
      this.smiley2 = new Button("B2");
      this.smiley3 = new Button("B3");
      this.smiley4 = new Button("B4");
      this.buttonsList = new ArrayList(NUMBER_OF_SMILEYS);
      buttonsList.add(smiley1);
      buttonsList.add(smiley2);
      buttonsList.add(smiley3);
      buttonsList.add(smiley4);
      // A faire dans Player pour avoirr la liste de cartes dans le Player
      this.imageCard1 = new Image("/images/CardOups.jpg");
      this.imageCard2 = new Image("/images/CardCanarchie.jpg");
      this.imageCard3 = new Image("/images/CardPan.jpg");
      this.viewCard1 = new ImageView(imageCard1);
      this.viewCard2 = new ImageView(imageCard2);
      this.viewCard3 = new ImageView(imageCard3);
      
      viewCard1.setFitHeight(viewCard1.getBoundsInLocal().getHeight() / RESIZE_CARDS);
      viewCard1.setFitWidth(viewCard1.getBoundsInLocal().getWidth() / RESIZE_CARDS);
      viewCard2.setFitHeight(viewCard2.getBoundsInLocal().getHeight() / RESIZE_CARDS);
      viewCard2.setFitWidth(viewCard2.getBoundsInLocal().getWidth() / RESIZE_CARDS);
      viewCard3.setFitHeight(viewCard3.getBoundsInLocal().getHeight() / RESIZE_CARDS);
      viewCard3.setFitWidth(viewCard3.getBoundsInLocal().getWidth() / RESIZE_CARDS);
      
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
      this.targetsList = new ArrayList(NUMBER_OF_TARGETS);
      
      this.imageProtection = new Image("/images/CardACouvert.jpg");
      this.protectionCardsList = new ArrayList(NUMBER_OF_PROTECTIONS);
      
      this.imageDuckGame = new Image("/images/DuckPink.jpg");
      this.ducksGameList = new ArrayList(ProtocolV1.MAX_NO_POS);
      
      this.imageDuckHiden = new Image("/images/DuckGreen.jpg");
      this.ducksHidenList = new ArrayList(ProtocolV1.MAX_NO_POS);
      
      this.imageBackCard = new Image("/images/CardBack.jpg");
      this.viewBackCard = new ImageView(imageBackCard);
      viewBackCard.setFitHeight(viewBackCard.getBoundsInLocal().getHeight() / RESIZE_CARDS);
      viewBackCard.setFitWidth(viewBackCard.getBoundsInLocal().getWidth() / RESIZE_CARDS);
   }
   /**
    * Initialises le controlleur de la classe.
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // Th buttons for the chat
      for(int i = 0; i < NUMBER_OF_SMILEYS / GRID_POS; i++) {
         for(int j = 0; j < NUMBER_OF_SMILEYS / GRID_POS; j++) {
            GridPane.setConstraints(buttonsList.get(i * GRID_POS + j), j, i, 1, 1, HPos.CENTER, VPos.CENTER);
         }
      }
      smileyGrid.getChildren().addAll(buttonsList);
      
      // La partie arrière d'une carte canard pour la pile de canard
      GridPane.setConstraints(viewBackCard, 6, 0, 1, 1, HPos.CENTER, VPos.CENTER);
      GridPane.setMargin(viewBackCard, new Insets(0, 0, MARGIN_DOWN, 0));
      ducksAndProtectionsGrid.getChildren().add(viewBackCard);
      
      targets();
      
      hidenDucks();
      
      ducksGame();
      
      protectionCards();
      
      update();
      
   }
   
   public void showPlayers(int nbPlayers) {
      for(int i = 0; i < nbPlayers; i++) {
         playersGrid.addColumn(i, playersList.get(i), playersChatList.get(i));
         GridPane.setConstraints(playersList.get(i), i, 0, 1, 1, HPos.CENTER, VPos.TOP);
         GridPane.setConstraints(playersChatList.get(i), i, 0, 1, 1, HPos.CENTER, VPos.BOTTOM);
      }
   }
   
   public void showPlayerCards() {
      // Les cartes des joueurs, à refaire selon la liste de cartes dans Player, liste aussi à changer pour que ce soit une liste de boutons
      for(int i = 0; i < NUMBER_OF_CARDS; i++) {
         GridPane.setConstraints(cardsList.get(i), i, 0, 1, 1, HPos.CENTER, VPos.CENTER);
      }
      playerCardsGrid.getChildren().addAll(cardsList);
   }
   
   public void targets() {
      // Les cibles qui peuvent être posées sur le plateau de jeu
      for(int i = 0; i < NUMBER_OF_TARGETS; i++) {
         targetsList.add(new ImageView(imageTarget));
         targetsList.get(i).setFitHeight(targetsList.get(i).getBoundsInLocal().getHeight() / RESIZE_CARDS);
         targetsList.get(i).setFitWidth(targetsList.get(i).getBoundsInLocal().getWidth() / RESIZE_CARDS);
         GridPane.setConstraints(targetsList.get(i), i, 0, 1, 1, HPos.CENTER, VPos.CENTER);
         targetsList.get(i).setVisible(false); // On cache les cibles tant qu'on les joues pas
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
         ducksHidenList.add(new ImageView(imageDuckHiden));
         ducksHidenList.get(i).setFitHeight(ducksHidenList.get(i).getBoundsInLocal().getHeight() / RESIZE_CARDS);
         ducksHidenList.get(i).setFitWidth(ducksHidenList.get(i).getBoundsInLocal().getWidth() / RESIZE_CARDS);
         GridPane.setConstraints(ducksHidenList.get(i), i, 0, 1, 1, HPos.CENTER, VPos.CENTER);
         GridPane.setMargin(ducksHidenList.get(i), new Insets(0, 0, 0, MARGIN_LEFT));
         ducksHidenList.get(i).setVisible(false);
      }
      ducksAndProtectionsGrid.getChildren().addAll(ducksHidenList);
   }
   
   public void unshowHidenDucks(int position) {
      ducksHidenList.get(position).setVisible(false);
   }
   
   public void showHidenDucks(int position, int duck) {
      ducksHidenList.get(position).setImage(duckImages[duck]);
      ducksHidenList.get(position).setVisible(true);
   }
   
   public void ducksGame() {
      // BOUCLE POUR LES CANARDS SUR LE PLATEAU, FAIT JUSTE POUR L'AFFICHAGE, UTILISÉ SI ON VEUT AFFICHER UN CANARD DU PLATEAU PLUS TARD
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         ducksGameList.add(new ImageView(imageDuckGame));
         ducksGameList.get(i).setFitHeight(ducksGameList.get(i).getBoundsInLocal().getHeight() / RESIZE_CARDS);
         ducksGameList.get(i).setFitWidth(ducksGameList.get(i).getBoundsInLocal().getWidth() / RESIZE_CARDS);
         GridPane.setConstraints(ducksGameList.get(i), i, 0, 1, 1, HPos.LEFT, VPos.CENTER);
         GridPane.setMargin(ducksGameList.get(i), new Insets(0, 0, MARGIN_DOWN, MARGIN_LEFT));
      }
      ducksAndProtectionsGrid.getChildren().addAll(ducksGameList);
   }
   
   public void showDucksGame() {
      // Les cartes des joueurs, à refaire selon la liste de cartes dans Player, liste aussi à changer pour que ce soit une liste de boutons
      for(int i = 0; i < ProtocolV1.MAX_NO_POS; i++) {
         GridPane.setConstraints(ducksGameList.get(i), i, 0, 1, 1, HPos.LEFT, VPos.CENTER);
         GridPane.setMargin(ducksGameList.get(i), new Insets(0, 0, MARGIN_DOWN, MARGIN_LEFT));
      }
//      playerCardsGrid.getChildren().addAll(cardsList);
   }
   
   public void protectionCards() {
      // BOUCLE POUR LA CARTE À COUVERT, FAIT JUSTE POUR L'AFFICHAGE, UTILISÉ SI ON VEUT UTILISER LA CARTE À COUVERT PLUS TARD
      for(int i = 0; i < NUMBER_OF_PROTECTIONS; i++) {
         protectionCardsList.add(new ImageView(imageProtection));
         protectionCardsList.get(i).setFitHeight(protectionCardsList.get(i).getBoundsInLocal().getHeight() / RESIZE_CARDS);
         protectionCardsList.get(i).setFitWidth(protectionCardsList.get(i).getBoundsInLocal().getWidth() / RESIZE_CARDS);
         GridPane.setConstraints(protectionCardsList.get(i), i, 0, 1, 1, HPos.LEFT, VPos.CENTER);
         GridPane.setMargin(protectionCardsList.get(i), new Insets(0, 0, MARGIN_DOWN, 0));
         protectionCardsList.get(i).setVisible(false);
      }
      ducksAndProtectionsGrid.getChildren().addAll(protectionCardsList);
   }
   
   public void unshowGuard(int position) {
      protectionCardsList.get(position).setVisible(false);
   }
   
   public void showGuard(int position) {
      protectionCardsList.get(position).setVisible(true);
//      protectionCardsList.get(position).setStyle(value);
   }
   
   public void update() {
      showPlayers(4); // Here the number of players that will play the game
      
      showPlayerCards();
      
      showDucksGame();
      
      showTargets(2);
      showTargets(5);
      
      showHidenDucks(0, 2);
      showHidenDucks(1, 3);
      showHidenDucks(3, 1);
      
      showGuard(1);
      showGuard(4);
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
