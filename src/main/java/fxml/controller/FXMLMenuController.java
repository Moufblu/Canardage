/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Description: Classe contenant le menu principale de l'application, qui donnera 
 * les options de rejoindre une partie, créer un serveur ou quitter le jeu
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class FXMLMenuController implements Initializable {
    // Chemin vers le FXML de Lobby
   private final String JOIN_GAME_PATH = "/fxml/FXMLLobby.fxml";
   // Chemin vers le FXML de la création du serveur
   private final String CREATE_SERVER_PATH = "/fxml/FXMLCreateServer.fxml";
   // Nom de la fenêtre pour créer le serveur
   private final String GAME_WINDOW_NAME = "Rejoindre une Partie";
   // Nom de la fenêtre pour rejoindre une partie
   private final String SERVER_WINDOW_NAME = "Créer un Server";
   
   @FXML
   private Button joinGameBtn;      // Bouton pour rejoindre une partie
   @FXML
   private Button createServerBtn;  // Bouton pour créer un serveur
   @FXML
   private Button quitBtn;          // Bouton pour quitter la partie

   /**
    * Initialise le controller de cette classe
    * @param url Pas utilisé
    * @param rb Pas utilisé
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {

   }

   /**
    * Création de la fenêtre pour rejoindre une partie
    * @param event Pas utilisé
    */
   @FXML
   public void onJoinGame(ActionEvent event) {
      createWindow(JOIN_GAME_PATH, GAME_WINDOW_NAME, event);
   }

   /**
    * Création de la fenêtre pour créer le serveur d'une partie
    * @param event Pas utilisé
    */
   @FXML
   public void onCreateServer(ActionEvent event) {
      createWindow(CREATE_SERVER_PATH, SERVER_WINDOW_NAME, event);
   }

   /**
    * Méthode pour cacher la fenêtre courante
    * @param event 
    */
   @FXML
   public void onQuit(ActionEvent event) {
      ((Node) (event.getSource())).getScene().getWindow().hide();
   }
   
   /**
    * Méthode privée pour pouvoir lancer une ou autre fenêtre selon la sélection 
    * sur le menu
    * @param path Chemin vers le FXML pour créer la fenêtre
    * @param name Nom de la fenêtre à créer
    * @param event Donne l'événement de cette fenêtre courante
    */
   private void createWindow(String path, String name, ActionEvent event) {
      try {
         Parent root = FXMLLoader.load(getClass().getResource(path));
         Stage serverStage = new Stage();
         Scene scene = new Scene(root);

         serverStage.setTitle(name);
         serverStage.resizableProperty().set(false);
         serverStage.setScene(scene);
         
         this.onQuit(event);
         serverStage.show();
         serverStage.setOnCloseRequest((WindowEvent event1) -> {
            Platform.exit();
            System.exit(0);
         });
      } catch(IOException e) {
         Logger logger = Logger.getLogger(getClass().getName());
         logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
      }
   }
}
