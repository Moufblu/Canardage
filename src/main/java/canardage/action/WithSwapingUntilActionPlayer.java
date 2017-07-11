
package canardage.action;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WithSwapingUntilActionPlayer extends Action {
   
   protected static int firstLocation = BAD_LOCATION;
   protected static int secondLocation = BAD_LOCATION;
   protected static boolean ended = false;
   protected final WithSwapingUntilActionPlayer mutex = this;
   
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
   
   public static int endSwapping() {
      ended = true;
      return BAD_LOCATION;
   }
   
   protected void getTwoLocationsChoice() {

      client.startSwaps();
      // Boucle tant que le choix de l'utilisateur est fausse
      while(true) {
         try {
            synchronized (this) {
               if(!ended) {
                  firstLocation = client.getLocation();
                  if(ended) {
                     break;
                  }
               }
            }
            synchronized (this) {
               if(!ended) {
                  secondLocation = client.getLocation();
                  if(ended) {
                     break;
                  }
               }
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
   }
}
