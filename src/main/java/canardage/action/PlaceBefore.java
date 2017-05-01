/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

/**
 *
 * @author jimmy
 */
public class PlaceBefore extends WithLocation {

   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.swap(positionChoice, false);
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if(board.isDuck(position - 1) && board.isMyDuck(position, player) ) {
         return true;
      }
      return false;
   }
   
   @Override
   public boolean hasEffect() {
      for(int i = 1; i < board.NB_LOCATIONS; i++) {
         if(isPlayable(i)) {
            return true;
         }
      }
      return false;
   }
}
