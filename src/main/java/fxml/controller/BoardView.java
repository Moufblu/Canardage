package fxml.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Nathan
 */
public class BoardView extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource("/fxml/FXMLBoard.fxml"));

         Scene scene = new Scene(root);
         primaryStage.setTitle("Canardage");
         primaryStage.setScene(scene);

         primaryStage.setMinHeight(767);
         primaryStage.setMinWidth(1310);
         primaryStage.setMaxHeight(767);
         primaryStage.setMaxWidth(1310);

         primaryStage.show();
      } catch(IOException e) {
         Logger logger = Logger.getLogger(getClass().getName());
         logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
      }
   }

   public static void main(String[] args) {
      launch(args);
   }
}
