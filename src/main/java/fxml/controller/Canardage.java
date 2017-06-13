package fxml.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Classe principale de 
 */
public class Canardage extends Application {
   
   private final String MENU_PATH = "/fxml/FXMLMenu.fxml";
   private final String MENU_NAME = "Canardage";

   private Pane pane;

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
      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         @Override
         public void handle(WindowEvent event) {
            Platform.exit();
            System.exit(0);
         }
      });
   }

   public static void main(String[] args) {
      launch(args);
   }
}
