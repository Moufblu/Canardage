package canardage.action;

/**
 *
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
public class PlaceBefore extends WithLocation {

   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.swap(positionChoice, false);
      } else {
         System.out.println("aucun emplacement valdie on brûle la carte");
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if(board.isDuck(position - 1) && board.isMyDuck(position, getPlayer()) ) {
         return true;
      }
      return false;
   }
   
   @Override
   public boolean hasEffect() {
      for(int i = 1; i < board.getNbLocations(); i++) {
         if(isPlayable(i)) {
            return true;
         }
      }
      return false;
   }
}
