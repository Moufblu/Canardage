package canardage.action;

import static canardage.action.WithLocation.BAD_LOCATION;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Classe pour les cartes ayant besoin de sélectionner une position
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public abstract class WithDirection extends WithLocation {

   protected boolean direction = true; // Direction vers laquelle on veut bouger le 
                                       // canard; vrai - gauche, faux - droite
   
   /**
    * Vérifie si l'on peut utiliser la carte sur une des positions
    * @return Vrai si possible, faux sinon
    */
   @Override
   public boolean hasEffect() {
      boolean effect = super.hasEffect();
      direction = !direction;
      effect |= super.hasEffect();

      return effect;
   }
   
   /**
    * Surcharge de la méthode 'getLocationChoice()' qui demande une position au joueur
    * @return La position demandée à l'utilisateur
    */
   @Override
   protected int getLocationChoice() {
      int positionChoice = BAD_LOCATION;

      // Boucle tant que le choix de l'utilisateur est faux
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
