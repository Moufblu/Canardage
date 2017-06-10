/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import static canardage.action.WithLocation.BAD_LOCATION;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jiver
 */
public abstract class WithDirection extends WithLocation {

   protected boolean direction = true;   // Direction vers laquelle on veut bouger le 
   // canard; vrai - gauche, faux - droite
   
   /**
    * Demande une position en console (à modifier)
    * @return La position demandée à l'utilisateur
    */
   protected int getLocationChoice() {
      int positionChoice = BAD_LOCATION;

      // Bocle tant que le choix de l'utilisateur est fausse
      while(true) {
         try {
            positionChoice = client.getLocation();
            int adjacentPosition = client.getLocation();
            if(positionChoice + 1 == adjacentPosition) {
               direction = true;
            } else if (positionChoice - 1 == adjacentPosition) {
               direction = false;
            } else {
               positionChoice = BAD_LOCATION;
            }
         } catch(IOException ex) {
            Logger.getLogger(WithLocation.class.getName()).log(Level.SEVERE, null, ex);
         }
         if(isPlayable(positionChoice)) {
            break;
         }
      }
      return positionChoice;
   }
   
}
