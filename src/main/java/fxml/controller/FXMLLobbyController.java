package fxml.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import canardage.Player;
import canardage.Server;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel-Portable
 */
public class FXMLLobbyController implements Initializable {

   @FXML
   private Button joinServerBtn;
   @FXML
   private Button refreshBtn;
   @FXML
   private ListView<Server> serverList; // Juste pour afficher, faudra changer

   private boolean thereIsPassword = true; // Mettre à false quand on fera les liens

   Player player;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // TODO OR NOT :3

      player = Player.getInstance();
      refresh();
   }

   @FXML
   public void joinGame(ActionEvent event) {
      // METTRE A FALSE LA VARIABLE ET VERIFIER S'IL Y A UN MOT DE PASSE SELON LA CREATION DE LA PARTIE
      if(thereIsPassword) {
         Parent root;
         try {
            root = FXMLLoader.load(getClass().getResource("/fxml/FXMLPassword.fxml"));
            Stage joinStage = new Stage();
            Scene scene = new Scene(root);

            joinStage.setTitle("Put Password");
            joinStage.resizableProperty().set(false);
            joinStage.setScene(scene);

            ((Node) (event.getSource())).getScene().getWindow().hide();
            joinStage.show();
         } catch(IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Erreur à la création d'une nouvelle fenêtre.", e);
         }
      }
      // SINON ON AFFICHE LE BOARD AVEC LES JOUEURS QUI REJOIGNENT LA PARTIE
      // (METTRE LES CANARDS GRISÉS ET METTRE EN COULEUR SI UN JOUEUR SE JOIN)
      // (LAISSER LES EMOTICONES POUR QUE LES JOUEURS S'AMUSENT AVANT LE DEBUT)
   }

   @FXML
   public void refreshServerList(ActionEvent event) {
      throw new UnsupportedOperationException("DO THAT SHIT BITCH");
      // TODO
   }

   private void refresh() {
      refreshBtn.setDisable(true);
      serverList.getItems().clear();
      serverList.getItems().addAll(player.getServers());
      refreshBtn.setDisable(false);
   }

}
