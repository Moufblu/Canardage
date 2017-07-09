/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

/**
 *
 * @author jiver
 */
public class DuckyLuke extends WithLocation {
   
   private static final int ID;  // Id de la carte
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public boolean isPlayable(int position) {
      if(board.isDuck(position)) {
         return true;
      }
      return false;
   }

   @Override
   public int getNbCards() {
      return 50;
   }

   @Override
   public int getID() {
      return ID;
   }

   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.setTarget(positionChoice, true);
         board.fire(positionChoice);
      }
   }

   @Override
   public String getFile() {
      return "/images/CardDuckyLuke.jpg";
   }
   
}
