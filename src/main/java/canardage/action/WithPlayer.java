/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import canardage.Player;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jiver
 */
public abstract class WithPlayer extends Action {
   /**
    * Méthode à redéfinir dans les sous-classes pour savoir si la carte est jouable à
    * une position donnée
    * @param position La position sur laquelle on veut jouer la carte
    * @return Vrai si on peut jouer sur cette position, faux sinon
    */
   public abstract boolean isPlayable(int idPlayer);
   
   @Override
   public boolean hasEffect() {
      int nbPlayers = board.getNbPlayers();
      for(int i = 0; i < nbPlayers; i++) {
         if(board.hasDeadDuck(i)) {
            return true;
         }
      }
      return false;
   }
   
   protected int getIDPlayerChoice() {
      int playerChoice = Action.BAD_ID_PLAYER;

      // Boucle tant que le choix de l'utilisateur est fausse
      while(true) {
         try {
            playerChoice = client.getIDPlayer();
         } catch(IOException ex) {
            Logger.getLogger(WithLocation.class.getName()).log(Level.SEVERE, null, ex);
         }
         if(isPlayable(playerChoice)) {
            System.out.println("Le joueur choisie est bien séléctionnable dans cette situation");
            break;
         }
         System.out.println("Le joueur choisie n'est pas valide");
      }
      return playerChoice;
   }
}
