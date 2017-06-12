package fxml.controller;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Nathan
 */
public class BoardView extends Application {
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource("/fxml/FXMLCanardage.fxml"));
         Scene scene = new Scene(root);
         
         primaryStage.setTitle("Canardage");
         primaryStage.resizableProperty().set(false);
         primaryStage.setScene(scene);
         
         primaryStage.centerOnScreen();
         primaryStage.setResizable(false);
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


//      String musicFile = "explosion.wav";     // For example
//
//      Media sound = new Media(new File(musicFile).toURI().toString());
//      MediaPlayer mediaPlayer = new MediaPlayer(sound);
//      mediaPlayer.play();
        