
package canardage.action;

import canardage.Global;

public class Canarchie extends WithSwapingUntilActionPlayer {

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
         while(!ended) {
            getTwoLocationsChoice();
            if(firstLocation != BAD_LOCATION && secondLocation != BAD_LOCATION) {
               board.swap(firstLocation, secondLocation);
               client.writeLine(Global.ProtocolV1.messageBoardState());
            }
         }
         synchronized (mutex) {
            ended = false;
         }
      }
   }

   @Override
   public String getFile() {
      return "/images/CardCanarchie.jpg";
   }
   
}
