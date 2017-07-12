package fxml.controller;

import canardage.AlertPopup;
import canardage.Global;
import canardage.Player;
import canardage.action.WithTwoLocation;
import chat.Emoticon;
import duckException.BadGameInitialisation;
import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
 * Description: Classe qui sert à créer la fenêtre principale du jeu
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class FXMLCanardageController implements Initializable {

   private final int RESIZE_IMAGES = 5; // Sert à redimensionner la taille des images
   private final int MARGIN_LEFT = 20;  // Déplace le marge de gauche d'une image
   private final int MARGIN_DOWN = 15;  // Déplace le marge du bas d'une image

   private final int NUMBER_OF_SMILEYS = 4;  // Nombre d'émoticônes sur le plateau
   private final int GRID_POS = 2;  // Position sur la grille des émoticônes

   private int nbCurrentPlayers = 0;   // Nombre de joueurs dans la partie

   private boolean areCardsUsable = false;   // Utilisation d'une carte
   private boolean arePlayersClickable = false; // choix d'un joueur

   // Création d'un joueur
   Player player = Player.getInstance();

   @FXML
   private Button startButton;   // Bouton pour commencer la partie
   @FXML
   private GridPane playersGrid; // Grille pour afficher les joueurs
   @FXML
   private GridPane smileyGrid;  // Grille pour les émôticones
   @FXML
   private GridPane playerCardsGrid;   // Grille pour les cartes du joueur
   @FXML
   private GridPane targetsGrid; // Grille pour les cibles
   @FXML
   private GridPane ducksAndProtectionsGrid; // Grille pour les cartes du jeu
   
   private Button endSwappingButton;

   private final ArrayList<Label> playersChatList; // Liste de label des joueurs

   private final ArrayList<ImageView> playersList; // Liste de joueurs (canards)

   private final ArrayList<Button> smileysList; // Liste d'émoticônes

   private final ArrayList<Button> cardsList;   // Liste des cartes du joueur

   private final ArrayList<ImageView> targetsList; // Liste de cibles
   private final Image imageTarget; // Image d'une cible

   private final ArrayList<ImageView> ducksGameList;  // Liste de canards en jeu

   private final ArrayList<ImageView> ducksHidenList; // Liste de canards cachés

   private final ArrayList<ImageView> protectionCardsList;  // Liste de cartes protections
   private final Image imageProtection;   // Image d'une protection

   private final Image imageBackCard;  // Image du dos d'une carte
   private final ImageView viewBackCard;  // ImageView du dos d'uune carte

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
   ArrayList<ImageView> duckViews; // Images des cartes pour afficher

   /**
    * Constructeur de la classe FXMLCanardageController
    */
   public FXMLCanardageController() {

      imageProtection = new Image("/images/CardACouvert.jpg");
      imageTarget = new Image("/images/Target.png");
      imageBackCard = new Image("/images/CardBack.jpg");

      // Liste des images des canards en jeu
      playersList = new ArrayList(Global.Rules.MAX_NO_POS);
      playersChatList = new ArrayList(Global.Rules.MAX_NO_POS);
      smileysList = new ArrayList(NUMBER_OF_SMILEYS);

      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         final int j = i;
         playersList.add(new ImageView(duckImages[i + 1]));
         playersChatList.add(new Label());
         resizeImageView(playersList.get(i));
         
         playersList.get(i).setOnMouseClicked((MouseEvent event) -> {
            System.out.println("clic sur joueur no : " + j);
            if(arePlayersClickable) {
               System.out.println("est rentré dans ");
               player.playerChoose(j);
               arePlayersClickable = false;
            }
         });
      }

      
      for(int i = 0; i < NUMBER_OF_SMILEYS; i++) {
         smileysList.add(new Button());
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

            cardsList.stream().forEach((cardsButton) -> {
               cardsButton.setDisable(true);
            });
            player.playCard(trigger);
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
    * @param url Pas utilisé
    * @param rb Pas utilisé
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // Les boutons du chat
      for(int i = 0; i < NUMBER_OF_SMILEYS / GRID_POS; i++) {
         for(int j = 0; j < NUMBER_OF_SMILEYS / GRID_POS; j++) {

            final int position = i * GRID_POS + j;
            imagesPosition(smileysList, position, j, i, HPos.CENTER, VPos.CENTER);
            
            ImageView smiley = new ImageView(Emoticon.values()[position].getEmote());
            smileysList.get(position).setGraphic(smiley);
            resizeImageView(smiley);
            
            smileysList.get(position).setOnAction((ActionEvent event) -> {
               player.sendEmoticon(Emoticon.values()[position]);
            });
         }
      }

      smileyGrid.getChildren().addAll(smileysList);

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

      startButton = new Button("Commencer!");
      imagesPosition(Arrays.asList(startButton), 0, 0, 1, HPos.LEFT, VPos.BOTTOM);
      smileyGrid.getChildren().add(startButton);
      
      if(player.getPlayerNumber() != 0) {
         startButton.setVisible(false);
      }
      
      startButton.setOnAction((ActionEvent event) -> {
         try {
            player.startGame();
            player.startGame(this);
            startButton.setDisable(true);
            startButton.setVisible(false);
         } catch(BadGameInitialisation e) {
            AlertPopup.alert("Avertissement", "Pas assez de joueurs", e.getMessage(),
                    Alert.AlertType.WARNING);
         } catch(IllegalStateException | IOException e) {
            AlertPopup.alert(e);
         }
      });
      
      endSwappingButton = new Button("J'ai finis!");
      imagesPosition(Arrays.asList(endSwappingButton), 0, 0, 1, HPos.LEFT, VPos.BOTTOM);
      smileyGrid.getChildren().add(endSwappingButton);
      endSwappingButton.setVisible(false);
      endSwappingButton.setOnAction((ActionEvent event) -> {
         
         endSwappingButton.setDisable(true);
         endSwappingButton.setVisible(false);
         
         cardsList.stream().forEach((cardsButton) -> {
            cardsButton.setDisable(true);
         });
         player.posChoose(-1);
      });
   }
   
   public void startSwap() {
      endSwappingButton.setDisable(false);
      endSwappingButton.setVisible(true);
   }

   /**
    * Méthode pour afficher les émoticônes pour le chat
    * @param player Numéro du joueur que envoie un émoticône
    * @param emoticon L'émoticône envoyé par un joueur
    */
   public void showEmoticon(int player, Emoticon emoticon) {
      PauseTransition pause = new PauseTransition(Duration.seconds(5));
      Platform.runLater(() -> {
         ImageView smileyChat = new ImageView(emoticon.getEmote());
         resizeImageView(smileyChat);
         imagesMarginAndPosition(Arrays.asList(smileyChat), 0, player, 0,
                                 HPos.CENTER, VPos.BOTTOM, MARGIN_DOWN, 0);
         playersGrid.add(smileyChat, player, 0);
         pause.pause();
         pause.setOnFinished(e -> playersGrid.getChildren().removeAll(smileyChat));
         pause.play();
      });
   }
   
   /**
    * Affichage des joueurs en dessus en disant quel est le canard d'un joueur
    */
   public void showPlayers() {
      for(int i = nbCurrentPlayers; i < 6; i++) {
         nbCurrentPlayers++;
         playersGrid.addColumn(i, playersList.get(i), playersChatList.get(i));
         imagesPosition(playersList, i, i, 0, HPos.CENTER, VPos.TOP);
         imagesPosition(playersChatList, i, i, 0, HPos.CENTER, VPos.BOTTOM);
         if(i == player.getPlayerNumber()) {
            playersChatList.get(i).setText("Votre canard");
         }
      }
   }

   /**
    * Affichage des cartes des joueurs
    */
   public void showPlayerCards() {
      for(int i = 0; i < Global.Rules.HAND_SIZE; i++) {
         imagesPosition(cardsList, i, i, 0, HPos.CENTER, VPos.CENTER);
      }
      playerCardsGrid.getChildren().addAll(cardsList);
   }

   /**
    * Affichage des cibles
    */
   public void targets() {
      // Les cibles qui peuvent être posées sur le plateau de jeu
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         createAndResizeImageView(targetsList, i, imageTarget);
         imagesPosition(targetsList, i, i, 0, HPos.CENTER, VPos.CENTER);
         targetsList.get(i).setVisible(false); // On cache les cibles tant qu'on les joue pas
      }
      targetsGrid.getChildren().addAll(targetsList);
   }

   /**
    * Méthode pour cacher une cible
    * @param position La position de la cible
    */
   public void unshowTargets(int position) {
      targetsList.get(position).setVisible(false);
   }

   /**
    * Méthode pour afficher une cible
    * @param position La position de la cible
    */
   public void showTargets(int position) {
      targetsList.get(position).setVisible(true);
   }

   /**
    * Méthode pour afficher les canards cachés
    */
   public void hidenDucks() {
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         createAndResizeImageView(ducksHidenList, i, duckImages[i]);
         imagesMarginAndPosition(ducksHidenList, i, i, 0, HPos.CENTER, VPos.CENTER,
                 0, MARGIN_LEFT);
         ducksHidenList.get(i).setVisible(false);
      }
      ducksAndProtectionsGrid.getChildren().addAll(ducksHidenList);
   }

   /**
    * Sert à enlever un canard caché
    * @param position 
    */
   public void unshowHidenDucks(int position) {
      ducksHidenList.get(position).setVisible(false);
   }

   /**
    * Sert à afficher un canard caché derrière un autre
    * @param position Position du canard
    * @param duck Numéro du canard à caché
    */
   public void showHidenDuck(int position, int duck) {
      Platform.runLater(new Runnable() {

         @Override
         public void run() {
            ducksHidenList.get(position).setVisible(true);

            ducksAndProtectionsGrid.getChildren().removeAll(ducksHidenList.get(position));

            ducksHidenList.get(position).setImage(duckImages[duck]);

            ducksAndProtectionsGrid.getChildren().add(position, ducksHidenList.get(position));
            imagesMarginAndPosition(ducksHidenList, position, position, 0, HPos.CENTER, VPos.CENTER,
                    0, MARGIN_LEFT);
         }
      });
   }

   /**
    * Affichage des cartes du joueurs
    * @param cards Liste de cartes
    */
   private void showHand(Integer[] cards) {
      Platform.runLater(() -> {
         for(int i = 0; i < cards.length; i++) {
            ImageView viewCards = new ImageView(new Image(Global.cards[cards[i]].getFile()));
            resizeImageView(viewCards);

            cardsList.get(i).setGraphic(viewCards);
         }
      });
   }

   /**
    * Affichage d'un canard sur le jeu
    * @param position Position du canard
    * @param duck Le numéro du canard
    */
   public void showDuck(int position, int duck) {
      Platform.runLater(() -> {
         ducksGameList.get(position).setVisible(true);
         
         ducksAndProtectionsGrid.getChildren().removeAll(ducksGameList.get(position));
         
         ducksGameList.get(position).setImage(duckImages[duck]);
         
         ducksAndProtectionsGrid.getChildren().add(position, ducksGameList.get(position));
         imagesMarginAndPosition(ducksGameList, position, position, 0, HPos.LEFT, VPos.CENTER,
                 MARGIN_DOWN, MARGIN_LEFT);
      });
   }

   /**
    * Affichage des canards du jeu
    */
   public void ducksGame() {
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         final int trigger = i;
         createAndResizeImageView(ducksGameList, i, duckImages[i]);
      }
      ducksAndProtectionsGrid.getChildren().addAll(ducksGameList);

      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         imagesMarginAndPosition(ducksGameList, i, i, 0, HPos.LEFT, VPos.CENTER,
                 MARGIN_DOWN, MARGIN_LEFT);
         ducksGameList.get(i).setVisible(false);
      }
   }

   /**
    * Modification d'un canard en jeu
    * @param position Position du canard
    * @param duck Le numéro du canard
    */
   public void modifieDucksInGame(int position, int duck) {
      ducksAndProtectionsGrid.getChildren().removeAll(ducksGameList.get(position));

      ducksGameList.get(position).setImage(duckImages[duck]);

      ducksAndProtectionsGrid.getChildren().add(position, ducksGameList.get(position));
      imagesMarginAndPosition(ducksGameList, position, position, 0, HPos.LEFT, VPos.CENTER,
              MARGIN_DOWN, MARGIN_LEFT);
   }

   /**
    * Affichage des cartes protections
    */
   public void protectionCards() {
      for(int i = 0; i < Global.Rules.MAX_NO_POS; i++) {
         createAndResizeImageView(protectionCardsList, i, imageProtection);
         imagesMarginAndPosition(protectionCardsList, i, i, 0, HPos.LEFT, VPos.CENTER,
                 MARGIN_DOWN, 0);
         protectionCardsList.get(i).setVisible(false); // On les caches au début
      }
      ducksAndProtectionsGrid.getChildren().addAll(protectionCardsList);
   }

   /**
    * Méthode pour cacher une carte protection
    * @param position Position de la carte
    */
   public void unshowGuard(int position) {
      protectionCardsList.get(position).setVisible(false);
   }


   /**
    * Méthode pour afficher une carte protection
    * @param position Position de la carte
    */
   public void showGuard(int position) {
      protectionCardsList.get(position).setVisible(true);
   }

   /**
    * Création des images et redimensionnement des mêmes
    * @param list La liste des cartes à créer/redimensionner
    * @param position La position dans la liste
    * @param image L'image à redimensionner
    */
   private void createAndResizeImageView(List<ImageView> list, int position, Image image) {
      ImageView imageView = new ImageView(image);
      imageView.setOnMouseClicked((MouseEvent event) -> {
         System.out.println("clic sur case no : " + (Global.Rules.MAX_NO_POS - 1 - position));
         if(areCardsUsable) {
            System.out.println("est rentré dans ");
            player.posChoose(Global.Rules.MAX_NO_POS - 1 - position);
            areCardsUsable = false;
         }
      });
      imageView.setOnMouseEntered((MouseEvent event) -> {
         imageView.setOpacity(.5);
      });
      
      imageView.setOnMouseExited((MouseEvent event) -> {
         imageView.setOpacity(1);
      });

      list.add(position, imageView);
      resizeImageView(list.get(position));
   }

   /**
    * Redimensionnement d'une image
    * @param imageView L'image à redimensionner
    */
   private void resizeImageView(ImageView imageView) {
      imageView.setFitHeight(imageView.getBoundsInLocal().getHeight() / RESIZE_IMAGES);
      imageView.setFitWidth(imageView.getBoundsInLocal().getWidth() / RESIZE_IMAGES);
   }

   /**
    * Méthode pour donner une position aux différents éléments sur le plateau
    * @param list Liste des éléments à placer
    * @param position Position de l'élément à placer
    * @param columnPosition Position sur une colonne sur une grille
    * @param rowPosition Position sur une ligne sur une grille
    * @param hPos Position horizontale dans une grille
    * @param vPos Position verticale dans une grille
    */
   private void imagesPosition(List<?> list, int position,
           int columnPosition, int rowPosition,
           HPos hPos, VPos vPos) {
      GridPane.setConstraints((Node) list.get(position),
              columnPosition, rowPosition, 1, 1, hPos, vPos);
   }

   /**
    * 
    * @param list Liste des éléments à placer
    * @param position Position de l'élément à placer
    * @param columnPosition Position sur une colonne sur une grille
    * @param rowPosition Position sur une ligne sur une grille
    * @param hPos Position horizontale dans une grille
    * @param vPos Position verticale dans une grille
    * @param bottom Marge en bas sur une grille
    * @param left Marge à gache sur une grille
    */
   private void imagesMarginAndPosition(List<?> list, int position,
           int columnPosition, int rowPosition,
           HPos hPos, VPos vPos,
           double bottom, double left) {
      imagesPosition(list, position, columnPosition, rowPosition, hPos, vPos);
      GridPane.setMargin((Node) list.get(position), new Insets(0, 0, bottom, left));
   }

   /**
    * Actualisation des cartes du joueur
    * @param cards La liste de cartes du joueur
    */
   public void updateCards(Integer[] cards) {
      showHand(cards);
   }

   /**
    * Actualisation du plateau de jeu
    * @param stringBoard Affichage console de quoi il reçoit comme information
    */
   public void updateBoard(String stringBoard) {
      System.out.println("BOARD GOT : " + stringBoard);
      String[] blocs = stringBoard.split(String.valueOf(Global.BoardParam.SEPARATOR));
      boolean hasGuard;
      boolean hasTarget;
      int hiddenDuck;
      for(int k = 0; k < blocs.length; k++) {
         String splittedBoard = blocs[k];
         int displayDuck = Integer.parseInt(splittedBoard.substring(0, 1));

         showDuck(k, displayDuck);

         hasGuard = false;
         hasTarget = false;
         hiddenDuck = 0;

         for(int i = 1; i < splittedBoard.length(); i++) {
            char c = splittedBoard.charAt(i);
            switch(c) {
               case Global.BoardParam.GUARD:
                  hasGuard = true;
                  break;
               case Global.BoardParam.TARGET:
                  hasTarget = true;
                  break;
               default:
                  hiddenDuck = Character.getNumericValue(c);
                  break;
            }
         }

         if(hiddenDuck != 0) {
            showHidenDuck(k, hiddenDuck);
         } else {
            unshowHidenDucks(k);
         }

         if(hasTarget) {
            System.out.println("SHOW TARGET");
            showTargets(k);
         } else {
            System.out.println("HIDE TARGET");
            unshowTargets(k);
         }

         if(hasGuard) {
            showGuard(k);
         } else {
            unshowGuard(k);
         }

      }
   }

   /**
    * Demande d'une carte pour un joueur
    */
   public void askCard() {
      cardsList.stream().forEach((cardButton) -> {
         cardButton.setDisable(false);
      });
   }

   /**
    * Alerte si une action est fausse
    * @param message 
    */
   public void alert(Global.ERROR_MESSAGES message) {
      AlertPopup.alert("Wrong move", "You cannot do this action",
                     canardage.Global.ProtocolV1.messageError(message),
                     Alert.AlertType.INFORMATION);
   }

   /**
    * Demande d'une position pour utiliser une carte
    */
   public void askPosition() {
      areCardsUsable = true;
      AlertPopup.alert("Position", "Choisissez une position",
                     "Veuillez choisir une position", Alert.AlertType.INFORMATION);
   }
   
   public void askPlayerID() {
      arePlayersClickable = true;
      AlertPopup.alert("Joueur", "Choisissez un joueur",
                     "Veuillez choisir un joueur", Alert.AlertType.INFORMATION);
   }
}
