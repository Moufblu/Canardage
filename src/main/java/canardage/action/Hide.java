package canardage.action;

import canardage.Board;
import java.util.Scanner;

/**
 * Description: Classe pour implémenter la carte pour se cacher derrière un canard
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Hide extends WithLocation implements IDirection {

   private boolean direction = true;   // Direction vers laquelle on veut bouger le 
   // canard; vrai - gauche, faux - droite

   /**
    * Vérifie si la carte a un effet et réalise l'effet correspondant si possible
    */
   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice;
         do {
            positionChoice = getLocationChoice();
            direction = getDirectionChoice();
         } while(!isPlayable(positionChoice));
         board.hide(positionChoice, direction);
      }
   }

   /**
    * Vérifie si la carte pour se cacher derrière un canard peut être jouée
    * @param position La postion sur laquelle on veut jouer la carte
    * @return Vrai si c'est possible, faux sinon
    */
   @Override
   public boolean isPlayable(int position) {
      if((position == 0 && !direction)
              || (position == Board.NB_LOCATIONS - 1 && direction)) {

         return false;
      }
      if(board.possibleHide(position, direction)) {
         return true;
      }
      return false;
   }

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
    * Méthode pour que l'utilisateur puisse donner la position voulue
    * @return Vrai si il veut jouer é gauche, faux si cest à droite
    */
   @Override
   public boolean getDirectionChoice() {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Souhaitez-vous aller à gauche ou à droite ? (true/false)");
      return scanner.nextBoolean();
   }
}
