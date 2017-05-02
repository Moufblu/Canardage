package canardage.action;

/**
 *
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
public class Shoot extends WithLocation {

   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.fire(positionChoice);
      }
   }

   
   @Override
   public boolean isPlayable(int position) {
      if(board.isTargetted(position)) {
         return true;
      }
      return false;
   }
}
