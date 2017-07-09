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
public class DoubleTarget extends WithDirection {

   private static final int ID;
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public boolean isPlayable(int position) {
      if((position == 0 && !direction)
              || (position == Board.NB_LOCATIONS - 1 && direction)) {

         return false;
      }
      
      int addFromDirection = direction ? 1 : -1;
      if(board.isTargetted(position) 
              || board.isTargetted(position + addFromDirection)
              || !board.isDuck(position)
              || !board.isDuck(position + addFromDirection)) {
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
         board.setTarget(positionChoice, true);
         board.setTarget(positionChoice + addFromDirection, true);
      }
   }

   @Override
   public String getFile() {
      return "/images/CardDoubleTarget.jpg";
   }
   
}
