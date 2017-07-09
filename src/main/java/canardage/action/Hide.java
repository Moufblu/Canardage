package canardage.action;

import canardage.Board;

/**
 * Description: Classe pour implémenter la carte pour se cacher derrière un canard
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Hide extends WithDirection {
   
   private static final int ID;  // Id de la carte
   
   static {
      ID = Action.counter++;
   }
   
   /**
    * Surcharge de la méthode 'getId()' pour l'obtention de l'ID de la carte
    * @return L'ID de la carte
    */
   @Override
   public int getID() {
      return ID;
   }
   
   /**
    * Vérifie si la carte a un effet et réalise l'effet correspondant si possible
    */
   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
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
      
      return board.possibleHide(position, direction)&& board.isMyDuck(position, getPlayer().getId());
   }

   

   /**
    * Surcharge de la méthode 'getNbCards()' pour nous donner le nombre de cartes de 
    * disponibles de ce type
    * @return Le numéro de la carte
    */
   @Override
   public int getNbCards() {
      return 2;
   }

   /**
    * Surcharge de la méthode 'getFile()' pour obtenir le chemin vers l'image de la 
    * carte
    * @return Le chemin vers cett image
    */
   @Override
   public String getFile() {
      return "/images/CardPlanque.jpg";
   }
}
