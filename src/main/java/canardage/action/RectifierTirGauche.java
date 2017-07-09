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
public class RectifierTirGauche extends WithLocation {

   
   private static final int ID;  // Id de la carte
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public boolean isPlayable(int position) {
      if(position == board.getNbLocations() - 1) {
         return false;
      }
      if(!board.isTargetted(position) || board.isTargetted(position + 1)) {
         return false;
      }
      return true;
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
         board.setTarget(positionChoice, false);
         board.forceTarget(positionChoice + 1);
      }
   }

   @Override
   public String getFile() {
      return "/images/CardRectifierTirGauche.jpg";
   }
   
}
