/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import canardage.Board;
import canardage.Global;

/**
 *
 * @author jiver
 */
public class Enchaine extends WithDirection {

   private static final int ID;
   
   static {
      ID = Action.counter++;
   }
   
   public Enchaine() {
      super.sound = Global.SOUNDS.PAN;
   }
   
   @Override
   public boolean isPlayable(int position) {
      if((position == 0 && !direction)
              || (position == Board.NB_LOCATIONS - 1 && direction)) {

         return false;
      }
      int addFromDirection = direction ? 1 : -1;
      if(!board.isTargetted(position) 
              || !board.isTargetted(position + addFromDirection)) {
         return false;
      }
      return true;
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
         if(direction) {
            board.fire(positionChoice);
            board.fire(positionChoice + 1);
         } else {
            board.fire(positionChoice - 1);
            board.fire(positionChoice);
         }
      }
   }

   @Override
   public String getFile() {
      return "/images/CardCanardsEnchaines.jpg";
   }
   
}
