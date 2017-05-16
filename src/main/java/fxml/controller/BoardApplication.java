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
public class BoardApplication extends Application {

   @Override
   public void start(Stage stage) throws Exception {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

         Scene scene = new Scene(root);
         stage.setTitle("Canardage");
         stage.setScene(scene);

         stage.setMinHeight(767);
         stage.setMinWidth(1300);
         stage.setMaxHeight(767);
         stage.setMaxWidth(1300);

         stage.show();
      } catch(IOException e) {
         Logger logger = Logger.getLogger(getClass().getName());
         logger.log(Level.SEVERE, "Failed to create new Window.", e);
      }
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      launch(args);
   }
}
