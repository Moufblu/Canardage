package fxml.controller;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Nathan
 */
public class BoardView extends Application {
   
   private Pane pane;
   @Override
   public void start(Stage primaryStage) throws Exception {
      Parent root;
      try {
         root = FXMLLoader.load(getClass().getResource("/fxml/FXMLBoard.fxml"));
         Image value = new Image(getClass().getResourceAsStream("/images/canardBleuCopie.png"));
         ImageView duck = new ImageView(value);
         duck.setImage(value);
//         duck.setLayoutX(300);
//         duck.setLayoutY(200);
//         duck.fitHeightProperty().add(100);
//         duck.fitWidthProperty().add(100);
         duck.setVisible(true);
         Scene scene = new Scene(root);
         
         primaryStage.setTitle("Canardage");
         pane = new Pane();
         pane.getChildren().add(duck);
         primaryStage.centerOnScreen();
         GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
         Rectangle maximumWindowBounds=graphicsEnvironment.getMaximumWindowBounds();
         primaryStage.setMinHeight(maximumWindowBounds.height/2);
         primaryStage.setMinWidth(maximumWindowBounds.width/2);
//         primaryStage.setMaxHeight(767);
//         primaryStage.setMaxWidth(1310);
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
