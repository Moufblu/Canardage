package canardage.action;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Classe pour vérifier la position dans le tableau de jeu et pouvoir
 * jouer à une certaine position Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public abstract class WithLocation extends Action {

   protected static final int BAD_LOCATION = -1;
   
   /**
    * Constructeur de WithLocation (utile?)
    */
   public WithLocation() {
      super();
   }
   
   /**
    * Méthode à redéfinir dans les sous-classes pour savoir si la carte est jouable à
    * une position donnée
    * @param position La position sur laquelle on veut jouer la carte
    * @return Vrai si on peut jouer sur cette position, faux sinon
    */
   public abstract boolean isPlayable(int position);

   /**
    * Demande une position en console (à modifier)
    * @return La position demandée à l'utilisateur
    */
   protected int getLocationChoice() {
      int positionChoice = BAD_LOCATION;

      // Bocle tant que le choix de l'utilisateur est fausse
      while(true) {
         try {
            System.out.println(client);
            positionChoice = client.getLocation();
         } catch(IOException ex) {
            Logger.getLogger(WithLocation.class.getName()).log(Level.SEVERE, null, ex);
         }
         if(isPlayable(positionChoice)) {
            break;
         }
      }
      return positionChoice;
   }

   /**
    * Méthode qui vérifie si la carte possède un effet à ce moment sur le plateau
    * @return Vrai si on peut la jouer, faux sinon
    */
   @Override
   public boolean hasEffect() {

      for(int i = 0; i < board.getNbLocations(); i++) {
         if(isPlayable(i)) {
            return true;
         }
      }
      return false;
   }
}
