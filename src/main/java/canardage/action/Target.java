package canardage.action;

import canardage.Board;

/**
 * 
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
public class Target extends WithLocation {

   /**
    * try to target a location of the board
    */
   @Override
   public void effect() {
      //check if this effect is possible somewhere
      if(hasEffect()) {
         board.setTarget(super.getLocationChoice(), true);
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
