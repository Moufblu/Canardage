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
public class PeaceAndLove extends Action {

   private static final int ID;  // Id de la carte
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public int getNbCards() {
      return 50;
   }

   @Override
   public int getID() {
      return ID;
   }

   @Override
   public void effect() {
      if(hasEffect()) {
         int nbLocation = board.getNbLocations();
         for(int i = 0; i < nbLocation; i++) {
            board.setTarget(i, false);
         }
      }
   }

   @Override
   public boolean hasEffect() {
      int nbLocation = board.getNbLocations();
      for(int i = 0; i < nbLocation; i++) {
         if(board.isTargetted(i)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public String getFile() {
      return "/images/CardPeaceAndLove.jpg";
   }
   
}
