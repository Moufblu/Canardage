
package canardage.action;

import canardage.Global;

public class Canarchie extends WithTwoLocation {

   private static final int ID;
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public boolean isPlayable(int firstLocation, int secondLocation) {
      return true;
   }

   @Override
   public int getNbCards() {
      return 30;
   }

   @Override
   public int getID() {
      return ID;
   }

   @Override
   public void effect() {
      if(hasEffect()) {
         client.startSwaps();
         while(true) {
            getTwoLocationsChoice();
            if(firstLocation != BAD_LOCATION && secondLocation != BAD_LOCATION) {
               board.swap(firstLocation, secondLocation);
               client.writeLine(Global.ProtocolV1.messageBoardState());
            } else {
               break;
            }
         }
      }
   }

   @Override
   public String getFile() {
      return "/images/CardCanarchie.jpg";
   }
   
}
