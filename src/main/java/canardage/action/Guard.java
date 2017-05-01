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
public class Guard extends WithLocation {

   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.guard(positionChoice);
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if(!board.isGuarded(position)) {
         return true;
      }
      return false;
   }

   
   
}
