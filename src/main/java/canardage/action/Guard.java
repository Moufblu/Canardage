package canardage.action;

/**
 *
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
public class Guard extends WithLocation {

   @Override
   public void effect() {
      if(hasEffect()) {
         board.setGuard(getLocationChoice(), true);
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if(!board.isGuarded(position) && board.isDuck(position)) {
         return true;
      }
      return false;
   }

   
   
}
