/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import canardage.Board;

/**
 *
 * @author jiver
 */
public class Oups extends WithDirection {

   private static final int ID;  // Id de la carte
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public boolean isPlayable(int position) {
      if((position == 0 && !direction)
              || (position == Board.NB_LOCATIONS - 1 && direction)) {

         return false;
      }
      
      if(!board.isTargetted(position)) {
         return false;
      }
      int addFromDirection = direction ? 1 : -1;
      if(!board.isDuck(position + addFromDirection)) {
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
         int addFromDirection = direction ? 1 : -1;
         board.setTarget(positionChoice, false);
         boolean temp = board.isTargetted(positionChoice + addFromDirection);
         board.setTarget(positionChoice + addFromDirection, true);
         board.fire(positionChoice + addFromDirection);
         board.setTarget(positionChoice + addFromDirection, temp);
      }
   }

   @Override
   public String getFile() {
      return "/images/CardOups.jpg";
   }
   
}
