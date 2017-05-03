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
      } else {
         System.out.println("aucun emplacement valdie on brûle la carte");
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
