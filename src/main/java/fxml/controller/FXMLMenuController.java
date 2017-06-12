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
import javafx.event.EventHandler;
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
 * FXML Controller class
 *
 * @author Miguel-Portable
 */
public class FXMLMenuController implements Initializable {
   
   private final String JOIN_GAME_PATH = "/fxml/FXMLLobby.fxml";
   private final String GAME_WINDOW_NAME = "Create Server";
   private final String CREATE_SERVER_PATH = "/fxml/FXMLCreateServer.fxml";
   private final String SERVER_WINDOW_NAME = "Join Game";
   
   @FXML
   private Button joinGameBtn;
   @FXML
   private Button createServerBtn;
   @FXML
   private Button quitBtn;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {

   }

   @FXML
   public void onJoinGame(ActionEvent event) {
      createWindow(JOIN_GAME_PATH, GAME_WINDOW_NAME, event);
   }

   @FXML
   public void onCreateServer(ActionEvent event) {
      createWindow(CREATE_SERVER_PATH, SERVER_WINDOW_NAME, event);
   }

   @FXML
   public void onQuit(ActionEvent event) {
      ((Node) (event.getSource())).getScene().getWindow().hide();
   }
   
   private void createWindow(String path, String name, ActionEvent event) {
      try {
         Parent root = FXMLLoader.load(getClass().getResource(path));
         Stage serverStage = new Stage();
         Scene scene = new Scene(root);

         serverStage.setTitle(name);
         serverStage.resizableProperty().set(false);
         serverStage.setScene(scene);
         
         ((Node) (event.getSource())).getScene().getWindow().hide();
         serverStage.show();
         serverStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
               Platform.exit();
               System.exit(0);
            }
         });
      } catch(IOException e) {
         Logger logger = Logger.getLogger(getClass().getName());
         logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
      }
   }
}
