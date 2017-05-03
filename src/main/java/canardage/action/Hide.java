package canardage.action;

import canardage.Board;
import java.util.InputMismatchException;
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
            direction = getDirectionChoice();
            positionChoice = getLocationChoice();
         } while (!isPlayable(positionChoice));
         board.hide(positionChoice, direction);
      } else {
         System.out.println("aucun emplacement valdie on brûle la carte");
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if ((position == 0 && direction == false)
              || (position == Board.NB_LOCATIONS - 1 && direction == true)) {
         
         return false;
      }
      if (board.possibleHide(position, direction) && board.isMyDuck(position, player)) {
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
      Scanner in = new Scanner(System.in);
      boolean ok;
      while(true) {
         System.out.println("Souhaitez-vous aller à gauche ? (true/false)");
         try {
            ok = in.nextBoolean();
            break;
         } catch(InputMismatchException e) {
            in.nextLine();
         }
      }
      return ok;
   }
}
