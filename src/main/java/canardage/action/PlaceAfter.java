/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

/**
 *
 * @author jiver
 */
public class PlaceAfter  extends WithLocation {
   
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
         board.swap(positionChoice, true);
      }
   }

   /**
    * Vérifie si la carte pour se mettre une position derrière peut être jouée
    * @param position La postion sur laquelle on veut jouer la carte
    * @return Vrai si c'est possible, faux sinon
    */
   @Override
   public boolean isPlayable(int position) {
      if(position == board.getNbLocations() - 1) {
         return false;
      } else {
         return board.isDuck(position) && board.isDuck(position + 1) && board.isMyDuck(position, getPlayer().getId());
      }
   }

   /**
    * Vérifie à chaque case s'il y a le canard du joueur qui veut jouer la carte et
    * s'il y a un canard juste derrière celui-ci
    * @return Vrai si c'est possible d'utiliser l'effet de la carte, faux sinon
    */
   @Override
   public boolean hasEffect() {
      for(int i = 0; i < board.getNbLocations() - 1; i++) {
         if(isPlayable(i)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Surcharge de la méthode 'getNbCards()' pour nous donner le nombre de cartes de 
    * disponibles de ce type
    * @return Le numéro de la carte
    */
   @Override
   public int getNbCards() {
      return 50;
   }

   /**
    * Surcharge de la méthode 'getFile()' pour obtenir le chemin vers l'image de la 
    * carte
    * @return Le chemin vers cett image
    */
   @Override
   public String getFile() {
      return "/images/CardCanardPresse.jpg";
   }
}