package fxml.controller;

import canardage.AlertPopup;
import canardage.Player;
import canardage.Server;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Description: Classe pour le lobby qui affiche les serveurs que l'on peut rejoindre
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class FXMLLobbyController implements Initializable {

   @FXML
   private Button joinServerBtn; // Bouton pour rejoindre une partie
   @FXML
   private Button refreshBtn;    // Bouton pour actualilser les serveurs
   @FXML
   private ListView<Server> serverList; // Liste de serveurs
   @FXML
   private TextField passwordTextField;   // Champs de texte pour mettre le mot de passe

   Player player;

   /**
    * Initialise le controller de cette classe
    * @param url Pas utilisé
    * @param rb Pas utilisé
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      player = Player.getInstance();
   }

   /**
    * Méthode qui sert à rejoindre une partie après avoir sélectionné un serveur et 
    * mis un mot de passe si nécessaire
    * @param event Donne l'événement de cette fenêtre courante
    */
   @FXML
   public void joinGame(ActionEvent event) {

      Server server = serverList.getSelectionModel().getSelectedItem();
      boolean connected = false;

      if(server != null) {
         System.out.println("Trying to connect to " + server);
         try {
            connected = player.connect(server, passwordTextField.getText());
         } catch(NoSuchAlgorithmException | ProtocolException ex) {
            AlertPopup.alert(ex);
         } catch(IOException e) {
            AlertPopup.alert("Erreur", "Erreur de connexion",
                    "Echec lors de la connextion au serveur : " + server,
                    Alert.AlertType.WARNING);
            refresh();
         }
         if(connected) {
            System.out.println("LANCEMENT");
            System.out.println("affichage board");
            Parent root;
            try {
               FXMLLoader fxmlLoader = new FXMLLoader();
               fxmlLoader.setLocation(getClass().getResource("/fxml/FXMLCanardage.fxml"));
               root = fxmlLoader.load();
               Stage joinStage = new Stage();
               Scene scene = new Scene(root);

               joinStage.setTitle("CANARDAGE");
               joinStage.resizableProperty().set(false);
               joinStage.setScene(scene);

               ((Node) (event.getSource())).getScene().getWindow().hide();
               joinStage.show();
               joinStage.setOnCloseRequest((WindowEvent event1) -> {
                  Platform.exit();
                  System.exit(0);
               });
                              player.startGame(fxmlLoader.getController());
            } catch(IOException e) {
               Logger logger = Logger.getLogger(getClass().getName());
               logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
            }

            
         } else {
            AlertPopup.alert("Info", "mot de passe erroné", "Veuillez indiquer un bon mot de passe", Alert.AlertType.INFORMATION);
         }
      } else {
         AlertPopup.alert("Info", "Aucune sélection", "Veuillez sélectionner un serveur", Alert.AlertType.INFORMATION);
      }
   }

   @FXML
   public void refreshServerList(ActionEvent event) {
      refresh();
   }

   private void refresh() {
      refreshBtn.setDisable(true);
      serverList.getItems().clear();
      Set<Server> s = player.getServers();
      if(!s.isEmpty()) {
         serverList.getItems().addAll(player.getServers());
      }
      refreshBtn.setDisable(false);
   }
}
