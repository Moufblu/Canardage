package fxml.controller;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 */
public class Canardage extends Application{

   private Pane pane;

   @Override
   public void start(Stage primaryStage) {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource("/fxml/FXMLMenu.fxml"));
         Scene scene = new Scene(root);

         primaryStage.setTitle("Canardage");
         primaryStage.resizableProperty().set(false);
         primaryStage.setScene(scene);

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
