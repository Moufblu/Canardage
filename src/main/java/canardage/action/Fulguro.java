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
public class Fulguro extends WithLocation {

   private static final int ID;
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public boolean isPlayable(int position) {
      if(position == board.getNbLocations() - 1) {
         return false;
      }
      if(board.isMyDuck(position, getPlayer().getId())) {
         return true;
      }
      return false;
   }

   @Override
   public int getNbCards() {
      return 3;
   }

   @Override
   public int getID() {
      return ID;
   }

   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         for(int i = positionChoice; i < board.getNbLocations() - 1; i++) {
            board.swap(i, true);
         }
      }
   }

   @Override
   public String getFile() {
      return "/images/CardFulguroCoin.jpg";
   }
   
}
