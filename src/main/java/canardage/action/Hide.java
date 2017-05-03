package canardage.action;

import java.util.Scanner;

/**
 *
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
public class Hide extends WithLocation implements IDirection {

   private boolean direction = true;
   
   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice;
         do {
            positionChoice = getLocationChoice();
            direction = getDirectionChoice();
         } while(isPlayable(positionChoice));
         board.hide(positionChoice, direction);
      }
   }

   @Override
   public boolean isPlayable(int position) {
      if(board.possibleHide(position, direction)) {
         return true;
      }
      return false;
   }

   /**
    * check the two direction possible
    * @return 
    */
   @Override
   public boolean hasEffect() {
      if(super.hasEffect()) {
         return true;
      }
      else {
         direction = !direction;
         if(super.hasEffect()) {
            return true;
         }
         return false;
      }
   }

   @Override
   public boolean getDirectionChoice() {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Souhaitez-vous aller à gauche ? (true/false)");
      return scanner.nextBoolean();
   }
}
