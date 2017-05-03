package canardage.action;

import canardage.Board;
import java.util.Scanner;

/**
 *
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
public class Hide extends WithLocation implements IDirection {

   private boolean direction = true;

   @Override
   public void effect() {
      if (hasEffect()) {
         int positionChoice;
         do {
            positionChoice = getLocationChoice();
            direction = getDirectionChoice();
         } while (isPlayable(positionChoice));
         board.hide(positionChoice, direction);
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if ((position == 0 && !direction)
              || (position == Board.NB_LOCATIONS && direction)) {
         
         return false;
      }
      if (board.possibleHide(position, direction)) {
         return true;
      }
      return false;
   }

   /**
    * check the two direction possible
    *
    * @return
    */
   @Override
   public boolean hasEffect() {
      boolean effect = super.hasEffect();
      direction = !direction;
      effect |= super.hasEffect();

      return effect;
   }

   @Override
   public boolean getDirectionChoice() {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Souhaitez-vous aller à gauche ? (true/false)");
      return scanner.nextBoolean();
   }
}
