
package canardage.action;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WithTwoLocation extends Action {
   
   protected static int firstLocation = BAD_LOCATION;
   protected static int secondLocation = BAD_LOCATION;
   
   /**
    * Méthode à redéfinir dans les sous-classes pour savoir si la carte est jouable à
    * une position donnée
    * @param position La position sur laquelle on veut jouer la carte
    * @return Vrai si on peut jouer sur cette position, faux sinon
    */
   public abstract boolean isPlayable(int firstLocation, int secondLocation);
   
   @Override
   public boolean hasEffect() {
      int nbLocationMax = board.getNbLocations();
      for(int i = 0; i < nbLocationMax; i++) {
         for(int j = 0; j < nbLocationMax; j++) {
            if(i != j && isPlayable(firstLocation, secondLocation)) {
               return true;
            }
         }
      }
      return false;
   }
   
   protected void getTwoLocationsChoice() {

      // Boucle tant que le choix de l'utilisateur est fausse
      while(true) {
         try {
            firstLocation = client.getLocation();
            if(firstLocation == BAD_LOCATION) {
               break;
            }
            secondLocation = client.getLocation();
            if(secondLocation == BAD_LOCATION) {
               break;
            }
         } catch(IOException ex) {
            Logger.getLogger(WithLocation.class.getName()).log(Level.SEVERE, null, ex);
         }
         if(isPlayable(firstLocation, secondLocation)) {
            System.out.println("Les positions séléctionnée sont jouable");
            break;
         } else {
            System.out.println("Les positions choisies ne sont pas valides");
         }
      }
      System.out.println("J'aiFINIS de DEMANDER LES POSITIONS");
   }
}
