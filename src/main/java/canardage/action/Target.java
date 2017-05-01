/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import canardage.Board;


public class Target extends WithLocation {

   /**
    * try to target a location of the board
    */
   @Override
   public void effect() {
      
      //check if this effect is possible somewhere
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.target(positionChoice);
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if(!board.isTargetted(position)) {
         return true;
      }
      return false;
   }
}
