package fxml.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Description: Classe principale de l'application, chargée d'afficher le menu 
 * principal pour pouvoir créer un serveur ou rejoindre une partie
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Canardage extends Application {
   
   private final String MENU_PATH = "/fxml/FXMLMenu.fxml"; // Chemin vers le FXML
   private final String MENU_NAME = "Canardage";   // Nom de la fenêtre

   /**
    * Surcharge de la méthode start() dans la classe application pour initialiser 
    * la fenêtre du menu
    * @param primaryStage 
    */
   @Override
   public void start(Stage primaryStage) {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource(MENU_PATH));
         Scene scene = new Scene(root);

         primaryStage.setTitle(MENU_NAME);
         primaryStage.resizableProperty().set(false);
         primaryStage.setScene(scene);

         primaryStage.show();
      } catch(IOException e) {
         Logger logger = Logger.getLogger(getClass().getName());
         logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
      }
      primaryStage.setOnCloseRequest((WindowEvent event) -> {
         Platform.exit();
         System.exit(0);
      });
   }

   public static void main(String[] args) {
      launch(args);
   }
}
